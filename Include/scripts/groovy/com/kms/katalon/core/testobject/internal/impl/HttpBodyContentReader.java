package com.kms.katalon.core.testobject.internal.impl;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.google.gson.reflect.TypeToken;
import com.kms.katalon.core.exception.KatalonRuntimeException;
import com.kms.katalon.core.testobject.FormDataBodyParameter;
import com.kms.katalon.core.testobject.HttpBodyContent;
import com.kms.katalon.core.testobject.UrlEncodedBodyParameter;
import com.kms.katalon.core.testobject.impl.HttpBodyType;
import com.kms.katalon.core.testobject.impl.HttpFileBodyContent;
import com.kms.katalon.core.testobject.impl.HttpFormDataBodyContent;
import com.kms.katalon.core.testobject.impl.HttpTextBodyContent;
import com.kms.katalon.core.testobject.impl.HttpUrlEncodedBodyContent;
import com.kms.katalon.core.util.StrSubstitutor;
import com.kms.katalon.core.util.internal.JsonUtil;
import com.kms.katalon.core.util.internal.PathUtil;

public class HttpBodyContentReader {
    private HttpBodyContentReader() {
        // Disable default constructor
    }

    public static HttpBodyContent fromSource(String httpBodyType, String httpBodyContent, String projectDir,
            StrSubstitutor substitutor) throws KatalonRuntimeException {
        switch (HttpBodyType.fromType(httpBodyType)) {
            case TEXT:
                InternalTextBodyContent textBodyContent = JsonUtil.fromJson(httpBodyContent,
                        InternalTextBodyContent.class);
                return new HttpTextBodyContent(substitutor.replace(textBodyContent.getText()),
                        textBodyContent.getCharset(), textBodyContent.getContentType());
            case FILE:
                InternalFileBodyContent fileBodyContent = JsonUtil.fromJson(httpBodyContent,
                        InternalFileBodyContent.class);
                try {
                    String filePath = substitutor.replace(fileBodyContent.getFilePath());
                    String absoluteFilePath = filePath;
                    if (StringUtils.isNotEmpty(filePath)) {
                        absoluteFilePath = PathUtil.relativeToAbsolutePath(filePath, projectDir);
                    }
                    return new HttpFileBodyContent(absoluteFilePath);
                } catch (IllegalArgumentException | FileNotFoundException e) {
                    throw new KatalonRuntimeException(e);
                }
            case FORM_DATA:
                InternalParameterizedBodyContent<FormDataBodyParameter> formDataBodyContent = JsonUtil.fromJson(
                        httpBodyContent,
                        new TypeToken<InternalParameterizedBodyContent<FormDataBodyParameter>>() {}.getType());
                try {
                    List<FormDataBodyParameter> bindedFormDataParameters = formDataBodyContent.getParameters()
                            .stream()
                            .map(p -> {
                                String value = substitutor.replace(p.getValue());
                                if (p.getType().equals(FormDataBodyParameter.PARAM_TYPE_FILE)
                                        && StringUtils.isNotEmpty(value)) { //value is a file path
                                    // convert to absolute path if possible
                                    value = PathUtil.relativeToAbsolutePath(value, projectDir); 
                                }
                                return new FormDataBodyParameter(substitutor.replace(p.getName()), value, p.getType());
                            })
                            .collect(Collectors.toList());
                    return new HttpFormDataBodyContent(bindedFormDataParameters);
                } catch (FileNotFoundException e) {
                    throw new KatalonRuntimeException(e);
                }
            case URL_ENCODED:
                InternalParameterizedBodyContent<UrlEncodedBodyParameter> urlEncodedBodyContent = JsonUtil.fromJson(
                        httpBodyContent,
                        new TypeToken<InternalParameterizedBodyContent<UrlEncodedBodyParameter>>() {}.getType());
                try {
                    List<UrlEncodedBodyParameter> bindedUrlEncodedParameters = urlEncodedBodyContent.getParameters()
                            .stream()
                            .map(u -> new UrlEncodedBodyParameter(substitutor.replace(u.getName()),
                                    substitutor.replace(u.getValue())))
                            .collect(Collectors.toList());
                    return new HttpUrlEncodedBodyContent(bindedUrlEncodedParameters);
                } catch (UnsupportedEncodingException e) {
                    throw new KatalonRuntimeException(e);
                }
            default:
                break;
        }
        throw new KatalonRuntimeException(MessageFormat.format("There is no implementation for {0}", httpBodyType));
    }

    private interface InternalHttpBodyContent {

        String getContentType();

        long getContentLength();

        String getCharset();
    }

    private class InternalTextBodyContent implements InternalHttpBodyContent {

        private String text;

        private String contentType = "text/plain";

        private String charset = "UTF-8";

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public long getContentLength() {
            if (text == null) {
                return -1L;
            }
            return text.length();
        }

        @Override
        public String getCharset() {
            return charset;
        }

        public String getText() {
            if (text == null) {
                text = "";
            }
            return text;
        }

    }

    private class InternalFileBodyContent implements InternalHttpBodyContent {

        private String filePath;

        private long fileSize;

        private String contentType = "";

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public long getContentLength() {
            return fileSize;
        }

        @Override
        public String getCharset() {
            // Nothing to do
            return null;
        }

        public String getFilePath() {
            return filePath;
        }
    }

    private class InternalParameterizedBodyContent<P> implements InternalHttpBodyContent {

        private String contentType;

        private String charset;

        private List<P> parameters = new ArrayList<>();

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public long getContentLength() {
            return 0;
        }

        @Override
        public String getCharset() {
            return charset;
        }

        public List<P> getParameters() {
            return parameters;
        }
    }

}
