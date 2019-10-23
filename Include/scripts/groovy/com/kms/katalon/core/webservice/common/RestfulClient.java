package com.kms.katalon.core.webservice.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.net.ssl.SSLContext;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import com.google.common.net.MediaType;
import com.google.common.net.UrlEscapers;
import com.kms.katalon.core.network.ProxyInformation;
import com.kms.katalon.core.testobject.RequestObject;
import com.kms.katalon.core.testobject.ResponseObject;
import com.kms.katalon.core.testobject.TestObjectProperty;
import com.kms.katalon.core.util.internal.ProxyUtil;
import com.kms.katalon.core.webservice.constants.RequestHeaderConstants;
import com.kms.katalon.core.webservice.helper.RestRequestMethodHelper;
import com.kms.katalon.core.webservice.helper.WebServiceCommonHelper;
import com.kms.katalon.core.webservice.support.UrlEncoder;
import com.kms.katalon.util.URLBuilder;
import com.kms.katalon.util.collections.NameValuePair;

public class RestfulClient extends BasicRequestor {

    private static final String TLS = "TLS";

    private static final String HTTPS = RequestHeaderConstants.HTTPS;
    
    private static final int MAX_REDIRECTS = 5;
    
    public RestfulClient(String projectDir, ProxyInformation proxyInfomation) {
        super(projectDir, proxyInfomation);
    }

    @Override
    public ResponseObject send(RequestObject request) throws Exception {
        ResponseObject responseObject;
        responseObject = sendRequest(request);
        return responseObject;
    }

    private ResponseObject sendRequest(RequestObject request) throws Exception {
        HttpClientBuilder clientBuilder = HttpClients.custom();
        
        if (!request.isFollowRedirects()) {
            clientBuilder.disableRedirectHandling();
        }
        
        clientBuilder.setConnectionManager(connectionManager);
        clientBuilder.setConnectionManagerShared(true);
        
        if (StringUtils.defaultString(request.getRestUrl()).toLowerCase().startsWith(HTTPS)) {
            SSLContext sc = SSLContext.getInstance(TLS);
            sc.init(getKeyManagers(), getTrustManagers(), null);
            clientBuilder.setSSLContext(sc);
        }
        
        ProxyInformation proxyInfo = request.getProxy() != null ? request.getProxy() : proxyInformation;
        Proxy proxy = proxyInfo == null ? Proxy.NO_PROXY : ProxyUtil.getProxy(proxyInfo);
        if (!Proxy.NO_PROXY.equals(proxy) || proxy.type() != Proxy.Type.DIRECT) {
            configureProxy(clientBuilder, proxyInfo);
        }

        if (StringUtils.defaultString(request.getRestUrl()).toLowerCase().startsWith(HTTPS)) {
            clientBuilder.setSSLHostnameVerifier(getHostnameVerifier());
        }
        
        BaseHttpRequest httpRequest = getHttpRequest(request);

        CloseableHttpClient httpClient = clientBuilder.build();
        
        // Default if not set
        setHttpConnectionHeaders(httpRequest, request);
        
        ResponseObject responseObject = response(httpClient, httpRequest);
        
        IOUtils.closeQuietly(httpClient);

        return responseObject;
    }

    private static boolean isBodySupported(String requestMethod) {
        return RestRequestMethodHelper.isBodySupported(requestMethod);
    }

    private static void setRequestMethod(CustomHttpMethodRequest httpRequest, String method) {
        httpRequest.setMethod(method);
    }
    
    private static BaseHttpRequest getHttpRequest(RequestObject request) throws UnsupportedOperationException, IOException {
        BaseHttpRequest httpRequest;
        String url = escapeUrl(request.getRestUrl());
        if (isBodySupported(request.getRestRequestMethod()) && request.getBodyContent() != null) {
            httpRequest = new DefaultHttpEntityEnclosingRequest(url);
            ByteArrayOutputStream outstream = new ByteArrayOutputStream();
            request.getBodyContent().writeTo(outstream);
            byte[] bytes = outstream.toByteArray();
            ByteArrayInputStream instream = new ByteArrayInputStream(bytes);
            ((DefaultHttpEntityEnclosingRequest) httpRequest)
                    .setEntity(new InputStreamEntity(instream));
        } else {
            httpRequest = new DefaultHttpRequest(url);
        }

        setRequestMethod(httpRequest, request.getRestRequestMethod());

        return httpRequest;
    }

    private static String escapeUrl(String url) throws MalformedURLException {
        String escapedUrl = UrlEscapers.urlFragmentEscaper().escape(url);
        return escapedUrl;
    }

    public static void processRequestParams(RequestObject request) throws MalformedURLException {
        StringBuilder paramString = new StringBuilder();
        for (TestObjectProperty property : request.getRestParameters()) {
            if (StringUtils.isEmpty(property.getName())) {
                continue;
            }
            if (!StringUtils.isEmpty(paramString.toString())) {
                paramString.append("&");
            }
            paramString.append(UrlEncoder.encode(property.getName()));
            paramString.append("=");
            paramString.append(UrlEncoder.encode(property.getValue()));
        }
        if (!StringUtils.isEmpty(paramString.toString())) {
            URL url = new URL(request.getRestUrl());
            request.setRestUrl(
                    request.getRestUrl() + (StringUtils.isEmpty(url.getQuery()) ? "?" : "&") + paramString.toString());
        }
    }
    
    private ResponseObject response(CloseableHttpClient httpClient, BaseHttpRequest httpRequest) throws Exception {
        if (httpClient == null || httpRequest == null) {
            return null;
        }
        
        long startTime = System.currentTimeMillis();
        CloseableHttpResponse response = httpClient.execute(httpRequest, getHttpContext());
        int statusCode = response.getStatusLine().getStatusCode();
        long waitingTime = System.currentTimeMillis() - startTime;
        long contentDownloadTime = 0L;
        StringBuffer sb = new StringBuffer();

        char[] buffer = new char[1024];
        long bodyLength = 0L;
        
        Charset charset = StandardCharsets.UTF_8;
        try {
	        String contentType = getResponseContentType(response);
	        if (StringUtils.isNotBlank(contentType)) {
	        	MediaType mediaType = MediaType.parse(contentType);
	        	charset = mediaType.charset().or(StandardCharsets.UTF_8);
	        }
        } catch (Exception e) {
        	// ignored - don't let tests fail just because charset could not be detected
        }
        
        try (InputStream inputStream = response.getEntity().getContent()) {
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset));
                int len = 0;
                startTime = System.currentTimeMillis();
                while ((len = reader.read(buffer)) != -1) {
                    contentDownloadTime += System.currentTimeMillis() - startTime;
                    sb.append(buffer, 0, len);
                    bodyLength += len;
                    startTime = System.currentTimeMillis();
                }
            }
        }

        long headerLength = WebServiceCommonHelper.calculateHeaderLength(response);

        ResponseObject responseObject = new ResponseObject(sb.toString());
        responseObject.setContentType(getResponseContentType(response));
        responseObject.setHeaderFields(getResponseHeaderFields(response));
        responseObject.setStatusCode(statusCode);
        responseObject.setResponseBodySize(bodyLength);
        responseObject.setResponseHeaderSize(headerLength);
        responseObject.setWaitingTime(waitingTime);
        responseObject.setContentDownloadTime(contentDownloadTime);
        
        setBodyContent(response, sb, responseObject);

        IOUtils.closeQuietly(response);
        
        return responseObject;
    }
    
    private Map<String, List<String>> getResponseHeaderFields(HttpResponse httpResponse) {
        Map<String, List<String>> headerFields = new HashMap<>();
        Header[] headers = httpResponse.getAllHeaders();
        for (Header header : headers) {
            String name = header.getName();
            if (!headerFields.containsKey(name)) {
                headerFields.put(name, new ArrayList<>());
            }
            headerFields.get(name).add(header.getValue());
        }
        return headerFields;
    }

}
