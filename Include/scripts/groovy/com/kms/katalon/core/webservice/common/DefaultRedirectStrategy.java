package com.kms.katalon.core.webservice.common;

import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

public class DefaultRedirectStrategy extends LaxRedirectStrategy {
    
    private static final int SC_PERMANENT_REDIRECT = 308;
    
    @Override
    public boolean isRedirected(
            final HttpRequest request,
            final HttpResponse response,
            final HttpContext context) throws ProtocolException {
        Args.notNull(request, "HTTP request");
        Args.notNull(response, "HTTP response");

        final int statusCode = response.getStatusLine().getStatusCode();
        final String method = request.getRequestLine().getMethod();
        final Header locationHeader = response.getFirstHeader("location");
        switch (statusCode) {
            case HttpStatus.SC_MOVED_TEMPORARILY:
                return isRedirectable(method) && locationHeader != null;
            case HttpStatus.SC_MOVED_PERMANENTLY:
            case HttpStatus.SC_TEMPORARY_REDIRECT:
            case SC_PERMANENT_REDIRECT:
                return isRedirectable(method);
            case HttpStatus.SC_SEE_OTHER:
                return true;
            default:
                return false;
        } 
    }
    
    public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
        URI uri = this.getLocationURI(request, response, context);
        String method = request.getRequestLine().getMethod();
        if (method.equalsIgnoreCase(HttpHead.METHOD_NAME)) {
            return new HttpHead(uri);
        } else if (method.equalsIgnoreCase(HttpGet.METHOD_NAME)) {
            return new HttpGet(uri);
        } else {
            int status = response.getStatusLine().getStatusCode();
            HttpUriRequest toReturn = null;
            if(status == HttpStatus.SC_TEMPORARY_REDIRECT || status == SC_PERMANENT_REDIRECT) {
                toReturn = RequestBuilder.copy(request).setUri(uri).build();
            } else {
                toReturn = new HttpGet(uri);
            }
            return toReturn;
        }
    }

}
