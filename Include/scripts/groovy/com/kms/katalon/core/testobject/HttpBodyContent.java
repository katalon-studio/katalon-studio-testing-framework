package com.kms.katalon.core.testobject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.kms.katalon.core.testobject.impl.HttpFileBodyContent;
import com.kms.katalon.core.testobject.impl.HttpFormDataBodyContent;
import com.kms.katalon.core.testobject.impl.HttpTextBodyContent;
import com.kms.katalon.core.testobject.impl.HttpUrlEncodedBodyContent;

/**
 * Represents the body content of a HTTP message (request/response).
 * 
 * @since 5.4
 * @see HttpTextBodyContent
 * @see HttpFileBodyContent
 * @see HttpFormDataBodyContent
 * @see HttpUrlEncodedBodyContent
 *
 */
public interface HttpBodyContent {
    /**
     * The <b>Content-Type</b> header of a request/response, if known.
     * 
     * @return The <b>Content-Type</b> header as a non-empty String value of the HTTP message, or null if unknown.
     */
    String getContentType();

    /**
     * The <b>Content-Length</b> header of a request/response, if known.
     * 
     * @return the number of bytes of the content as long value, or a negative number if unknown.
     */
    long getContentLength();

    /**
     * The <b>Content-Encoding</b> header of a request/response, if known.
     * 
     * @return The <b>Content-Encoding</b> header as a non-empty String value of the HTTP message, or null if unknown.
     */
    String getContentEncoding();

    /**
     * @return content stream of the request/response.
     * @throws IOException if the body could not be created.
     * @throws UnsupportedOperationException if the HTTP body could not represent as an {@link InputStream}
     */
    InputStream getInputStream() throws IOException, UnsupportedOperationException;
    
    /**
     * Writes the body content out to the output stream.
     * 
     * @param outstream the output stream to write entity content to
     *
     * @throws IOException if an I/O error occurs
     */
    void writeTo(OutputStream outstream) throws IOException;
}
