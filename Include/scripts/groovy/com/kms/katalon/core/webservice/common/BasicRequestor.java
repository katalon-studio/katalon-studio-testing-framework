package com.kms.katalon.core.webservice.common;

import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.ssl.KeyMaterial;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.auth.oauth.OAuthRsaSigner;
import com.google.api.client.auth.oauth.OAuthSigner;
import com.google.api.client.http.GenericUrl;
import com.kms.katalon.core.model.SSLClientCertificateSettings;
import com.kms.katalon.core.network.ProxyInformation;
import com.kms.katalon.core.network.ProxyOption;
import com.kms.katalon.core.testobject.ConditionType;
import com.kms.katalon.core.testobject.RequestObject;
import com.kms.katalon.core.testobject.ResponseObject;
import com.kms.katalon.core.testobject.TestObjectProperty;
import com.kms.katalon.core.testobject.impl.HttpFormDataBodyContent;
import com.kms.katalon.core.testobject.impl.HttpTextBodyContent;
import com.kms.katalon.core.util.internal.ProxyUtil;
import com.kms.katalon.core.webservice.constants.RequestHeaderConstants;
import com.kms.katalon.core.webservice.exception.WebServiceException;
import com.kms.katalon.core.webservice.setting.SSLCertificateOption;
import com.kms.katalon.core.webservice.setting.WebServiceSettingStore;

public abstract class BasicRequestor implements Requestor {
    private static final String TLS = "TLS";
    
    private static final String SOCKET_FACTORY_REGISTRY = "http.socket-factory-registry";
    
    protected static PoolingHttpClientConnectionManager connectionManager;
    
    static {
        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setValidateAfterInactivity(1);
        connectionManager.setMaxTotal(2000);
        connectionManager.setDefaultMaxPerRoute(500);
    }

    private String projectDir;

    protected ProxyInformation proxyInformation;

    public BasicRequestor(String projectDir, ProxyInformation proxyInformation) {
        this.projectDir = projectDir;
        this.proxyInformation = proxyInformation;
    }

    private SSLCertificateOption getSslCertificateOption() throws IOException {
        return WebServiceSettingStore.create(projectDir).getSSLCertificateOption();
    }
    
    private SSLClientCertificateSettings getSSLSettings() throws IOException {
        return WebServiceSettingStore.create(projectDir).getClientCertificateSettings();
    }

    protected TrustManager[] getTrustManagers() throws IOException {
        if (getSslCertificateOption() == SSLCertificateOption.BYPASS) {
            return new TrustManager[] { new X509TrustManager() {
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }
            } };
        }
        return new TrustManager[0];
    }
    
    protected KeyManager[] getKeyManagers() throws GeneralSecurityException, IOException {
        SSLClientCertificateSettings sslSettings = getSSLSettings();
        String keyStoreFilePath = sslSettings.getKeyStoreFile();
        if (!StringUtils.isBlank(keyStoreFilePath)) {
            File keyStoreFile = new File(keyStoreFilePath);
            String keyStorePassword = !StringUtils.isBlank(sslSettings.getKeyStorePassword())
                    ? sslSettings.getKeyStorePassword() : StringUtils.EMPTY;
            if (keyStoreFile.exists()) {
                KeyManagerFactory keyManagerFactory = KeyManagerFactory
                        .getInstance(KeyManagerFactory.getDefaultAlgorithm());
                KeyMaterial km = new KeyMaterial(keyStoreFile, keyStorePassword.toCharArray());
                keyManagerFactory.init(km.getKeyStore(), keyStorePassword.toCharArray());
                return keyManagerFactory.getKeyManagers();
            }
        }
        return new KeyManager[0];
    }

    public HostnameVerifier getHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String urlHostName, SSLSession session) {
                try {
                    return getSslCertificateOption() == SSLCertificateOption.BYPASS;
                } catch (IOException e) {
                    return false;
                }
            }
        };
    }

    public Proxy getProxy() throws WebServiceException {
        Proxy systemProxy = getSystemProxy();
//        if(proxyInformation.getDisableMobBroserProxy()){
//            return systemProxy;
//        }
//        Proxy proxy = BrowserMobProxyManager.getWebServiceProxy(systemProxy);
//        return proxy;
        return systemProxy;
    }

    private Proxy getSystemProxy() throws WebServiceException {
        if (proxyInformation == null) {
            return Proxy.NO_PROXY;
        }
        try {
            return ProxyUtil.getProxy(proxyInformation);
        } catch (URISyntaxException e) {
            throw new WebServiceException(e);
        } catch (IOException e) {
            throw new WebServiceException(e);
        }
    }

    protected void setHttpConnectionHeaders(HttpRequest httpRequest, RequestObject request)
            throws GeneralSecurityException, IOException {
        List<TestObjectProperty> complexAuthAttributes = request.getHttpHeaderProperties()
                .stream()
                .filter(header -> StringUtils.startsWith(header.getName(), RequestHeaderConstants.AUTH_META_PREFIX))
                .collect(Collectors.toList());
        List<TestObjectProperty> headers = new ArrayList<>(request.getHttpHeaderProperties());
        if (!complexAuthAttributes.isEmpty()) {
            headers.removeAll(complexAuthAttributes);
            String authorizationValue = generateAuthorizationHeader(getRequestUrl(request), complexAuthAttributes);
            if (!authorizationValue.isEmpty()) {
                headers.add(new TestObjectProperty(RequestHeaderConstants.AUTHORIZATION, ConditionType.EQUALS,
                        authorizationValue));
            }
        }
        
        headers.forEach(header -> {
            if (request.getBodyContent() instanceof HttpFormDataBodyContent 
                    && header.getName().equalsIgnoreCase("Content-Type")) {
                httpRequest.addHeader(header.getName(), request.getBodyContent().getContentType());
            } else {
                httpRequest.addHeader(header.getName(), header.getValue());
            }
        });
    }

    private String getRequestUrl(RequestObject request) {
        return StringUtils.equals(request.getServiceType(), RequestHeaderConstants.RESTFUL) ? request.getRestUrl()
                : request.getWsdlAddress();
    }

    private static String generateAuthorizationHeader(String requestUrl, List<TestObjectProperty> complexAuthAttributes)
            throws GeneralSecurityException, IOException {
        Map<String, String> map = complexAuthAttributes.stream()
                .collect(Collectors.toMap(TestObjectProperty::getName, TestObjectProperty::getValue));
        String authType = map.get(RequestHeaderConstants.AUTHORIZATION_TYPE);
        if (StringUtils.isBlank(authType)) {
            return StringUtils.EMPTY;
        }

        if (RequestHeaderConstants.AUTHORIZATION_TYPE_OAUTH_1_0.equals(authType)) {
            return createOAuth1AuthorizationHeaderValue(requestUrl, map);
        }

        // Other authorization type will be handled here

        return StringUtils.EMPTY;
    }

    public static String createOAuth1AuthorizationHeaderValue(String requestUrl, Map<String, String> map)
            throws GeneralSecurityException, IOException {
        OAuthParameters params = new OAuthParameters();
        params.consumerKey = map.getOrDefault(RequestHeaderConstants.AUTHORIZATION_OAUTH_CONSUMER_KEY,
                StringUtils.EMPTY);

        String signatureMethod = map.getOrDefault(RequestHeaderConstants.AUTHORIZATION_OAUTH_SIGNATURE_METHOD,
                StringUtils.EMPTY);
        String consumerSecret = map.getOrDefault(RequestHeaderConstants.AUTHORIZATION_OAUTH_CONSUMER_SECRET,
                StringUtils.EMPTY);
        String tokenSecret = map.getOrDefault(RequestHeaderConstants.AUTHORIZATION_OAUTH_TOKEN_SECRET,
                StringUtils.EMPTY);
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
        params.computeSignature(RequestHeaderConstants.GET, new GenericUrl(requestUrl));
        return params.getAuthorizationHeader();
    }

    private static OAuthSigner getSigner(String signatureMethod, String consumerSecret, String tokenSecret)
            throws IOException, GeneralSecurityException {
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
    
    protected void setBodyContent(HttpResponse httpRequest, StringBuffer sb, ResponseObject responseObject) {
        String contentTypeHeader = getResponseContentType(httpRequest);
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
                charset = contentTypeHeader.substring(charsetIdx + "charset=".length(), separatorIdx)
                        .trim().replace("\"", "");
            }
        }
        HttpTextBodyContent textBodyContent = new HttpTextBodyContent(sb.toString(), charset, contentType);
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
    
    protected void configureProxy(HttpClientBuilder httpClientBuilder, ProxyInformation proxyInformation) {
        HttpHost httpProxy = new HttpHost(proxyInformation.getProxyServerAddress(),
                proxyInformation.getProxyServerPort());
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        String username = proxyInformation.getUsername();
        String password = proxyInformation.getPassword();
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            credentialsProvider.setCredentials(new AuthScope(httpProxy),
                    new UsernamePasswordCredentials(username, password));
        }
        httpClientBuilder.setRoutePlanner(new HttpRoutePlanner() {

            @Override
            public HttpRoute determineRoute(HttpHost arg0, HttpRequest arg1, HttpContext arg2) throws HttpException {
                if ((ProxyOption.valueOf(proxyInformation.getProxyOption()).equals(ProxyOption.USE_SYSTEM))) {
                    return new SystemDefaultRoutePlanner(ProxySelector.getDefault()).determineRoute(arg0, arg1, arg2);
                } else {
                    return new DefaultProxyRoutePlanner(httpProxy).determineRoute(arg0, arg1, arg2);
                }
            }
        }).setDefaultCredentialsProvider(credentialsProvider);
    }
    
    protected HttpContext getHttpContext() throws KeyManagementException, GeneralSecurityException, IOException {
        HttpContext httpContext = new BasicHttpContext();
        SSLContext sc = SSLContext.getInstance(TLS);
        sc.init(getKeyManagers(), getTrustManagers(), null);
        Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sc))
                .build();
        httpContext.setAttribute(SOCKET_FACTORY_REGISTRY, reg);
        return httpContext;
    }
}
