package com.kms.katalon.core.webservice.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ByteArrayEntity;

import com.kms.katalon.core.network.ProxyInformation;
import com.kms.katalon.core.testobject.RequestObject;
import com.kms.katalon.core.testobject.TestObjectProperty;
import com.kms.katalon.core.webservice.support.UrlEncoder;

public class RestfulClient extends BasicRequestor {

    public RestfulClient(String projectDir, ProxyInformation proxyInfomation) {
        super(projectDir, proxyInfomation);
    }

    @Override
    protected HttpUriRequest buildHttpRequest(RequestObject requestObject) throws Exception {
        HttpUriRequest httpRequest;
        String url = requestObject.getRestUrl();
        if (requestObject.getBodyContent() != null) {
            httpRequest = buildHttpRequestWithBody(requestObject);
        } else {
            httpRequest = new DefaultHttpRequest(url);
        }

        setRequestMethod(httpRequest, requestObject);

        setHttpConnectionHeaders(httpRequest, requestObject);

        return httpRequest;
    }

    private HttpUriRequest buildHttpRequestWithBody(RequestObject request) throws IOException {
        String url = request.getRestUrl();
        HttpUriRequest httpRequest = new DefaultHttpEntityEnclosingRequest(url);

        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        request.getBodyContent().writeTo(outstream);
        byte[] bytes = outstream.toByteArray();
        ByteArrayEntity entity = new ByteArrayEntity(bytes);
        entity.setChunked(false);
        ((DefaultHttpEntityEnclosingRequest) httpRequest).setEntity(entity);

        return httpRequest;
    }

    private static void setRequestMethod(CustomHttpMethodRequest httpRequest, RequestObject requestObject) {
        String method = requestObject.getRestRequestMethod();
        httpRequest.setMethod(method);
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
}
