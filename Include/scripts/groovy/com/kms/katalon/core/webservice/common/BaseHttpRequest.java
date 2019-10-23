package com.kms.katalon.core.webservice.common;

import org.apache.http.client.methods.HttpRequestBase;

public class BaseHttpRequest extends HttpRequestBase implements CustomHttpMethodRequest {

    protected String method;
    
    @Override
    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String getMethod() {
        return method;
    }

}
