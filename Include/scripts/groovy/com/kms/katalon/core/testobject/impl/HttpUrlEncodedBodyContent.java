package com.kms.katalon.core.testobject.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import com.kms.katalon.core.testobject.HttpBodyContent;
import com.kms.katalon.core.testobject.UrlEncodedBodyParameter;

/**
 * Represents the body content of a HTTP message (request/response) that obtains content from an URL encoded
 * {@link String}
 * 
 * @since 5.4
 */
public class HttpUrlEncodedBodyContent implements HttpBodyContent {

    private static final String DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded";

    private static final String DEFAULT_CHARSET = "UTF-8";

    private UrlEncodedFormEntity urlEncodedFormEntity;

    private String charset;

    public HttpUrlEncodedBodyContent(List<UrlEncodedBodyParameter> parameters) throws UnsupportedEncodingException {
        this(parameters, DEFAULT_CHARSET);
    }

    public HttpUrlEncodedBodyContent(List<UrlEncodedBodyParameter> parameters, String charset)
            throws UnsupportedEncodingException {
        this.charset = charset;

        List<NameValuePair> nameValuePairs = toNameValuePairs(parameters);

        urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs, charset);
        urlEncodedFormEntity.setContentType(DEFAULT_CONTENT_TYPE);
    }

    private List<NameValuePair> toNameValuePairs(List<UrlEncodedBodyParameter> parameters) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (UrlEncodedBodyParameter p : parameters) {
            nameValuePairs.add(new BasicNameValuePair(p.getName(), p.getValue()));
        }
        return nameValuePairs;
    }

    @Override
    public String getContentType() {
        return DEFAULT_CONTENT_TYPE;
    }

    @Override
    public long getContentLength() {
        return 0;
    }

    @Override
    public String getContentEncoding() {
        return null;
    }

    @Override
    public InputStream getInputStream() throws IOException, UnsupportedOperationException {
        return urlEncodedFormEntity.getContent();
    }

    @Override
    public void writeTo(OutputStream outstream) throws IOException {
        urlEncodedFormEntity.writeTo(outstream);

    }

    public String getCharset() {
        return charset;
    }
}
