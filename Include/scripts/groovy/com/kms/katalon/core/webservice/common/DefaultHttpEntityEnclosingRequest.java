package com.kms.katalon.core.webservice.common;

import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.protocol.HTTP;

public class DefaultHttpEntityEnclosingRequest extends BaseHttpRequest implements HttpEntityEnclosingRequest {
    private HttpEntity entity;
    
    public DefaultHttpEntityEnclosingRequest(final URI uri) {
        super();
        setURI(uri);
    }
    
    public DefaultHttpEntityEnclosingRequest(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    @Override
    public boolean expectContinue() {
        Header expect = getFirstHeader(HTTP.EXPECT_DIRECTIVE);
        return expect != null && HTTP.EXPECT_CONTINUE.equalsIgnoreCase(expect.getValue());
    }

    @Override
    public HttpEntity getEntity() {
        return this.entity;
    }

    @Override
    public void setEntity(HttpEntity entity) {
        this.entity = entity;
    }
}
