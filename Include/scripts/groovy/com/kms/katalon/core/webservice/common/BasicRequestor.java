package com.kms.katalon.core.webservice.common;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;

import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.auth.oauth.OAuthRsaSigner;
import com.google.api.client.auth.oauth.OAuthSigner;
import com.google.api.client.http.GenericUrl;
import com.kms.katalon.core.model.SSLClientCertificateSettings;
import com.kms.katalon.core.network.ProxyInformation;
import com.kms.katalon.core.testobject.ConditionType;
import com.kms.katalon.core.testobject.RequestObject;
import com.kms.katalon.core.testobject.ResponseObject;
import com.kms.katalon.core.testobject.TestObjectProperty;
import com.kms.katalon.core.testobject.impl.HttpFormDataBodyContent;
import com.kms.katalon.core.testobject.impl.HttpTextBodyContent;
import com.kms.katalon.core.webservice.constants.RequestHeaderConstants;
import com.kms.katalon.core.webservice.helper.WebServiceCommonHelper;
import com.kms.katalon.core.webservice.setting.SSLCertificateOption;
import com.kms.katalon.core.webservice.setting.WebServiceSettingStore;

public abstract class BasicRequestor implements Requestor {

    protected String projectDir;

    protected ProxyInformation proxyInformation;

    protected WebServiceSettingStore settingStore;

    public BasicRequestor(String projectDir, ProxyInformation proxyInformation) {
        this.projectDir = projectDir;
        this.proxyInformation = proxyInformation;
    }

    public ResponseObject send(RequestObject requestObject) throws Exception {
        HttpUriRequest httpRequest = buildHttpRequest(requestObject);

        long startTime = System.currentTimeMillis();
        HttpResponse httpResponse = HttpUtil.sendRequest(
                httpRequest,
                requestObject.isFollowRedirects(),
                proxyInformation,
                requestObject.getConnectionTimeout(),
                requestObject.getSocketTimeout(),
                requestObject.getMaxResponseSize(),
                getSslCertificateOption(),
                getSSLSettings());
        long waitingTime = System.currentTimeMillis() - startTime;

        ResponseObject responseObject = toResponseObject(httpResponse);
        responseObject.setWaitingTime(waitingTime);

        return responseObject;
    }

    protected abstract HttpUriRequest buildHttpRequest(RequestObject requestObject) throws Exception;

    protected SSLCertificateOption getSslCertificateOption() throws IOException {
        return getSettingStore().getSSLCertificateOption();
    }

    protected SSLClientCertificateSettings getSSLSettings() throws IOException {
        return getSettingStore().getClientCertificateSettings();
    }

    protected void setHttpConnectionHeaders(HttpRequest httpRequest, RequestObject request)
            throws GeneralSecurityException,
            IOException {
        List<TestObjectProperty> headers = getRequestHeaders(request);

        headers.forEach(header -> {
            if (request.getBodyContent() instanceof HttpFormDataBodyContent
                    && header.getName().equalsIgnoreCase("Content-Type")) {
                httpRequest.addHeader(header.getName(), request.getBodyContent().getContentType());
            } else {
                httpRequest.addHeader(header.getName(), header.getValue());
            }
        });
    }

    protected List<TestObjectProperty> getRequestHeaders(RequestObject request)
            throws GeneralSecurityException,
            IOException {
        List<TestObjectProperty> headers = new ArrayList<>(request.getHttpHeaderProperties());

        List<TestObjectProperty> complexAuthAttributes = request.getHttpHeaderProperties().stream()
                .filter(header -> StringUtils.startsWith(header.getName(), RequestHeaderConstants.AUTH_META_PREFIX))
                .collect(Collectors.toList());

        if (!complexAuthAttributes.isEmpty()) {
            headers.removeAll(complexAuthAttributes);
            String authorizationValue = generateAuthorizationHeader(
                    getRequestUrl(request),
                    complexAuthAttributes,
                    request);
            if (!authorizationValue.isEmpty()) {
                headers.add(
                        new TestObjectProperty(
                                RequestHeaderConstants.AUTHORIZATION,
                                ConditionType.EQUALS,
                                authorizationValue));
            }
        }

        return headers;
    }

    private String getRequestUrl(RequestObject request) {
        return StringUtils.equals(request.getServiceType(), RequestHeaderConstants.RESTFUL) ? request.getRestUrl()
                : request.getWsdlAddress();
    }

    private static String generateAuthorizationHeader(
            String requestUrl,
            List<TestObjectProperty> complexAuthAttributes,
            RequestObject request) throws GeneralSecurityException, IOException {
        Map<String, String> map = complexAuthAttributes.stream()
                .collect(Collectors.toMap(TestObjectProperty::getName, TestObjectProperty::getValue));
        String authType = map.get(RequestHeaderConstants.AUTHORIZATION_TYPE);
        if (StringUtils.isBlank(authType)) {
            return StringUtils.EMPTY;
        }

        if (RequestHeaderConstants.AUTHORIZATION_TYPE_OAUTH_1_0.equals(authType)) {
            if (StringUtils.equals(request.getServiceType(), RequestHeaderConstants.SOAP)) {
                return createOAuth1AuthorizationHeaderValue(requestUrl, map, RequestHeaderConstants.POST);
            }
            return createOAuth1AuthorizationHeaderValue(requestUrl, map, request.getRestRequestMethod());
        }

        // Other authorization type will be handled here

        return StringUtils.EMPTY;
    }

    public static String createOAuth1AuthorizationHeaderValue(
            String requestUrl,
            Map<String, String> map,
            String requestMethod) throws GeneralSecurityException, IOException {
        OAuthParameters params = new OAuthParameters();
        params.consumerKey = map
                .getOrDefault(RequestHeaderConstants.AUTHORIZATION_OAUTH_CONSUMER_KEY, StringUtils.EMPTY);

        String signatureMethod = map
                .getOrDefault(RequestHeaderConstants.AUTHORIZATION_OAUTH_SIGNATURE_METHOD, StringUtils.EMPTY);
        String consumerSecret = map
                .getOrDefault(RequestHeaderConstants.AUTHORIZATION_OAUTH_CONSUMER_SECRET, StringUtils.EMPTY);
        String tokenSecret = map
                .getOrDefault(RequestHeaderConstants.AUTHORIZATION_OAUTH_TOKEN_SECRET, StringUtils.EMPTY);
        OAuthSigner signer = getSigner(signatureMethod, consumerSecret, tokenSecret);
        if (signer == null) {
            return StringUtils.EMPTY;
        }

        params.signer = signer;
        params.computeNonce();
        params.computeTimestamp();
        params.version = "1.0";
        String token = map.getOrDefault(RequestHeaderConstants.AUTHORIZATION_OAUTH_TOKEN, StringUtils.EMPTY);
        if (StringUtils.isNotBlank(token)) {
            params.token = token;
        }
        String realm = map.getOrDefault(RequestHeaderConstants.AUTHORIZATION_OAUTH_REALM, StringUtils.EMPTY);
        if (StringUtils.isNotBlank(realm)) {
            params.realm = realm;
        }
        params.computeSignature(requestMethod, new GenericUrl(requestUrl));
        return params.getAuthorizationHeader();
    }

    private static OAuthSigner getSigner(String signatureMethod, String consumerSecret, String tokenSecret)
            throws IOException,
            GeneralSecurityException {
        if (StringUtils.equals(signatureMethod, RequestHeaderConstants.SIGNATURE_METHOD_HMAC_SHA1)) {
            OAuthHmacSigner signer = new OAuthHmacSigner();
            signer.clientSharedSecret = consumerSecret;
            if (StringUtils.isNotBlank(tokenSecret)) {
                signer.tokenSharedSecret = tokenSecret;
            }
            return signer;
        }

        if (StringUtils.equals(signatureMethod, RequestHeaderConstants.SIGNATURE_METHOD_RSA_SHA1)) {
            OAuthRsaSigner signer = new OAuthRsaSigner();
            // https://en.wikipedia.org/wiki/PKCS
            // https://tools.ietf.org/html/rfc5208
            signer.privateKey = PrivateKeyReader.getPrivateKey(consumerSecret);
            return signer;
        }

        return null;
    }

    protected ResponseObject toResponseObject(HttpResponse httpResponse) {
        long startTime = System.currentTimeMillis();
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        long contentDownloadTime = 0L;
        String responseBody = StringUtils.EMPTY;

        long bodyLength = 0L;

        HttpEntity responseEntity = httpResponse.getEntity();
        if (responseEntity != null) {
            bodyLength = responseEntity.getContentLength();
            startTime = System.currentTimeMillis();
            try {
                responseBody = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
            } catch (Exception e) {
                responseBody = ExceptionUtils.getFullStackTrace(e);
            }
            contentDownloadTime = System.currentTimeMillis() - startTime;
        }

        long headerLength = WebServiceCommonHelper.calculateHeaderLength(httpResponse);

        ResponseObject responseObject = new ResponseObject(responseBody);
        responseObject.setContentType(getResponseContentType(httpResponse));
        responseObject.setHeaderFields(getResponseHeaderFields(httpResponse));
        responseObject.setStatusCode(statusCode);
        responseObject.setResponseBodySize(bodyLength);
        responseObject.setResponseHeaderSize(headerLength);
        responseObject.setContentDownloadTime(contentDownloadTime);

        setResponseBodyContent(httpResponse, responseBody, responseObject);

        return responseObject;
    }

    protected void setResponseBodyContent(
            HttpResponse httpResponse,
            String responseBody,
            ResponseObject responseObject) {
        String contentTypeHeader = getResponseContentType(httpResponse);
        String contentType = contentTypeHeader;
        String charset = "UTF-8";
        if (contentTypeHeader != null && contentTypeHeader.contains(";")) {
            // Content-Type: [content-type]; charset=[charset]
            contentType = contentTypeHeader.split(";")[0].trim();
            int charsetIdx = contentTypeHeader.lastIndexOf("charset=");
            if (charsetIdx >= 0) {
                int separatorIdx = StringUtils.indexOf(contentTypeHeader, ";", charsetIdx);
                if (separatorIdx < 0) {
                    separatorIdx = contentTypeHeader.length();
                }
                charset = contentTypeHeader.substring(charsetIdx + "charset=".length(), separatorIdx).trim()
                        .replace("\"", "");
            }
        }
        HttpTextBodyContent textBodyContent = new HttpTextBodyContent(responseBody, charset, contentType);
        responseObject.setBodyContent(textBodyContent);
        responseObject.setContentCharset(charset);
    }

    protected String getResponseContentType(HttpResponse httpResponse) {
        Header contentTypeHeader = httpResponse.getFirstHeader("Content-Type");
        if (contentTypeHeader != null) {
            return contentTypeHeader.getValue();
        } else {
            return null;
        }
    }

    protected Map<String, List<String>> getResponseHeaderFields(HttpResponse httpResponse) {
        Map<String, List<String>> headerFields = new HashMap<>();
        Header[] headers = httpResponse.getAllHeaders();
        for (Header header : headers) {
            String name = header.getName();
            if (!headerFields.containsKey(name)) {
                headerFields.put(name, new ArrayList<>());
            }
            headerFields.get(name).add(header.getValue());
        }
        StatusLine statusLine = httpResponse.getStatusLine();
        if (statusLine != null) {
            headerFields.put("#status#", Arrays.asList(String.valueOf(statusLine)));
        }
        return headerFields;
    }

    public WebServiceSettingStore getSettingStore() {
        if (settingStore == null) {
            settingStore = WebServiceSettingStore.create(projectDir);
        }
        return settingStore;
    }

    public void setSettingStore(WebServiceSettingStore store) {
        this.settingStore = store;
    }
}
