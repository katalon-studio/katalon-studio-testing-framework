package com.kms.katalon.core.webservice.common;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.protocol.HttpContext;

import com.kms.katalon.core.network.ProxyInformation;
import com.kms.katalon.core.network.ProxyOption;
import com.kms.katalon.core.util.internal.ProxyUtil;

public class WebServiceProxyRoutePlanner implements HttpRoutePlanner {
    private ProxyInformation proxyInformation;

    public WebServiceProxyRoutePlanner(ProxyInformation proxyInformation) {
        this.proxyInformation = proxyInformation;
    }

    @Override
    public HttpRoute determineRoute(HttpHost host, HttpRequest request, HttpContext context) throws HttpException {
        if (proxyInformation == null) {
            return null;
        }

        if (ProxyOption.valueOf(proxyInformation.getProxyOption()).equals(ProxyOption.NO_PROXY)) {
            return null;
        }

        HttpHost httpProxy = new HttpHost(proxyInformation.getProxyServerAddress(),
                proxyInformation.getProxyServerPort());
        if ((ProxyOption.valueOf(proxyInformation.getProxyOption()).equals(ProxyOption.USE_SYSTEM))) {
            return new SystemDefaultRoutePlanner(ProxyUtil.getAutoProxySelector()).determineRoute(host, request,
                    context);
        } else {
            return new DefaultProxyRoutePlanner(httpProxy).determineRoute(host, request, context);
        }
    }

}
