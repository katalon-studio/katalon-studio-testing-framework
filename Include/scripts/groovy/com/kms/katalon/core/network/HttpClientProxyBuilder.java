package com.kms.katalon.core.network;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.protocol.HttpContext;

import com.kms.katalon.core.util.internal.ProxyUtil;

public class HttpClientProxyBuilder {
	
	private static final String TLS = "TLS";
	
	private static final String HTTPS = "HTTPS";
	
	private static final String SOCKET_FACTORY_REGISTRY = "http.socket-factory-registry";

    private static final int DEFAULT_CONNECT_TIMEOUT = 60000; //milliseconds
    
    private static PoolingHttpClientConnectionManager connectionManager;
    
    static {
    	try {
    		SSLContext sc = SSLContext.getInstance(TLS);
    		sc.init(getKeyManagers(), getTrustManagers(), null);
    		Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory> create()
    				.register("http", PlainConnectionSocketFactory.INSTANCE)
    				.register("https", new SSLConnectionSocketFactory(sc))
    				.build();
	        connectionManager = new PoolingHttpClientConnectionManager(reg);
    	} catch (Exception e) {
    		connectionManager = new PoolingHttpClientConnectionManager();
    	}
        connectionManager.setValidateAfterInactivity(1);
        connectionManager.setMaxTotal(2000);
        connectionManager.setDefaultMaxPerRoute(500);
    }
    
    private final HttpClientBuilder clientBuilder;

    public HttpClientProxyBuilder(HttpClientBuilder clientBuilder) {
        this.clientBuilder = clientBuilder;
    }

    public HttpClientBuilder getClientBuilder() {
        return clientBuilder;
    }

    public static HttpClientProxyBuilder create(ProxyInformation proxyInfo)
            throws URISyntaxException, IOException, GeneralSecurityException {
    	
        HttpClientBuilder clientBuilder = HttpClients.custom();
        
        clientBuilder.setConnectionManager(connectionManager);
        clientBuilder.setConnectionManagerShared(true);
        
        SSLContext sc = SSLContext.getInstance(TLS);
        sc.init(getKeyManagers(), getTrustManagers(), null);
        clientBuilder.setSSLContext(sc);
        
        Proxy proxy = proxyInfo == null ? Proxy.NO_PROXY : ProxyUtil.getProxy(proxyInfo);
        if (!Proxy.NO_PROXY.equals(proxy) || proxy.type() != Proxy.Type.DIRECT) {
        	configureProxy(clientBuilder, proxyInfo);
        }
        
        clientBuilder.setSSLHostnameVerifier(getHostnameVerifier());
        
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
                .build();
        
        clientBuilder.setDefaultRequestConfig(config);
        
        return new HttpClientProxyBuilder(clientBuilder);
    }
    
    private static void configureProxy(HttpClientBuilder httpClientBuilder, ProxyInformation proxyInformation) {
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
    
    private static TrustManager[] getTrustManagers() throws IOException {
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
    
    private static KeyManager[] getKeyManagers() throws GeneralSecurityException, IOException {
        return new KeyManager[0];
    }
    
    private static HostnameVerifier getHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };
    }
}
