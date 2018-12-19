package com.kms.katalon.core.testobject.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;

import org.apache.commons.lang.StringUtils;

import com.kms.katalon.core.testobject.HttpBodyContent;

/**
 * Represents the body content of a HTTP message (request/response) that obtains content from a {@link String}.
 * 
 * @since 5.4
 */
public class HttpTextBodyContent implements HttpBodyContent {

    private static final String DF_CHARSET = "UTF-8";

    private static final String DF_CONTENT_TYPE = "text/plain";

    private final String text;

    private final String charset;

    private final String contentType;

    private final byte[] bytes;

    /**
     * Concretes an instance with the given <code>text</code>, default charset <b>UTF-8</b>, and default contentType
     * <b>text/plain</b>.
     * 
     * @param text The raw content as plain text.
     */
    public HttpTextBodyContent(String text) {
        this(text, DF_CHARSET, DF_CONTENT_TYPE);
    }

    /**
     * Concretes an instance with the given <code>text</code>, <code>charset</code>, and default contentType
     * <b>text/plain</b>
     * 
     * @param text The raw content as plain text.
     * @param charset the character set to be used. In case charset is null or empty, the default charset
     * <code>UTF-8</code> is used.
     */
    public HttpTextBodyContent(String text, String charset) {
        this(text, charset, DF_CONTENT_TYPE);
    }

    /**
     * Concretes an instance with the given <code>text</code>, <code>charset</code>, and <code>charset</code>.
     * 
     * @param text The raw content as plain text.
     * @param charset the character set to be used. In case <code>charset</code> is null or empty, the default
     * <code>charset</code>
     * <code>UTF-8</code> is used.
     * @param contentType the content type to be used. In case <code>contentType</code> is null or empty, the default
     * <code>contentType</code>
     * <code>text/plain</code> is used.
     */
    public HttpTextBodyContent(String text, String charset, String contentType) {
        this.text = text;
        this.charset = charset;
        this.contentType = contentType;

        if (StringUtils.isNotEmpty(text)) {
            Charset charsetObject;
            try {
                charsetObject = Charset.forName(StringUtils.defaultString(charset, DF_CHARSET));
            } catch (IllegalCharsetNameException e) {
                charsetObject = Charset.forName(DF_CHARSET);
            }
            this.bytes = text.getBytes(charsetObject);
        } else {
            this.bytes = new byte[0];
        }
    }

    @Override
    public String getContentType() {
        if (StringUtils.isEmpty(contentType)) {
            return DF_CONTENT_TYPE;
        }
        return contentType;
    }

    @Override
    public long getContentLength() {
        return bytes.length;
    }

    @Override
    public String getContentEncoding() {
        return null;
    }

    @Override
    public InputStream getInputStream() throws IOException, UnsupportedOperationException {
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public void writeTo(OutputStream outstream) throws IOException {
        outstream.write(bytes);
        outstream.flush();
    }

    public String getText() {
        return text;
    }

    public String getCharset() {
        return charset;
    }
}
