package com.kms.katalon.core.testobject.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import com.kms.katalon.core.testobject.FormDataBodyParameter;
import com.kms.katalon.core.testobject.HttpBodyContent;

/**
 * Represents the body content of a HTTP message (request/response) that obtains
 * content from a form.
 * 
 * @since 5.4
 */
public class HttpFormDataBodyContent implements HttpBodyContent {

    private static final String DEFAULT_CHARSET = "US-ASCII";

    private MultipartEntityBuilder multipartEntityBuilder;

    private HttpEntity multipartEntity;

    private String charset;

    public HttpFormDataBodyContent(List<FormDataBodyParameter> parameters) throws FileNotFoundException {
        this.charset = DEFAULT_CHARSET;

        multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setContentType(ContentType.MULTIPART_FORM_DATA);
        for (FormDataBodyParameter parameter : parameters) {
            if (parameter.getType().equals(FormDataBodyParameter.PARAM_TYPE_FILE)) {
                multipartEntityBuilder.addBinaryBody(parameter.getName(), new FileInputStream(parameter.getValue()),
                        ContentType.create(getFileContentType(parameter)), FilenameUtils.getName(parameter.getValue()));
            } else {
                String contentType = parameter.getContentType();
                if (StringUtils.isEmpty(contentType)) {
                    multipartEntityBuilder.addTextBody(parameter.getName(), parameter.getValue());
                } else {
                    multipartEntityBuilder.addTextBody(parameter.getName(), parameter.getValue(),
                            ContentType.create(contentType));
                }
            }
        }

        multipartEntity = multipartEntityBuilder.build();
    }

    private String getFileContentType(FormDataBodyParameter parameter) {
        try {
            if (StringUtils.isEmpty(parameter.getContentType())) {
                URLConnection connection = new File(parameter.getValue()).toURI().toURL().openConnection();
                return connection.getContentType();
            } else {
                return parameter.getContentType();
            }
        } catch (IOException e) {
            return ContentType.DEFAULT_BINARY.getMimeType();
        }
    }

    @Override
    public String getContentType() {
        return multipartEntity.getContentType().getValue();
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
        return multipartEntity.getContent();
    }

    @Override
    public void writeTo(OutputStream outstream) throws IOException {
        multipartEntity.writeTo(outstream);
    }

    public String getCharset() {
        return charset;
    }
}
