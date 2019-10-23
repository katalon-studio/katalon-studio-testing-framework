package com.kms.katalon.core.webservice.common;

import org.apache.http.client.methods.HttpUriRequest;

public interface CustomHttpMethodRequest extends HttpUriRequest {

    void setMethod(String method);
}
