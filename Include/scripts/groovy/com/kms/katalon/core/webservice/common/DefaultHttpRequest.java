package com.kms.katalon.core.webservice.common;

import java.net.URI;

public class DefaultHttpRequest extends BaseHttpRequest {
    
    public DefaultHttpRequest(final URI uri) {
        super();
        setURI(uri);
    }
    
    public DefaultHttpRequest(final String uri) {
        super();
        setURI(URI.create(uri));
    }
}
