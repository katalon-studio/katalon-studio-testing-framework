package com.kms.katalon.core.testobject;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.kms.katalon.core.testobject.impl.HttpFileBodyContent;
import com.kms.katalon.core.testobject.impl.HttpFormDataBodyContent;
import com.kms.katalon.core.testobject.impl.HttpTextBodyContent;
import com.kms.katalon.core.testobject.impl.HttpUrlEncodedBodyContent;

public class RestRequestObjectBuilder {

    private RequestObject requestObject;

    public RestRequestObjectBuilder() {
        this.requestObject = new RequestObject(null);
    }

    /**
     * Specify method for the request object
     * <p>
     * Possible values: GET, POST, PUT, DELETE
     * 
     * @param requestMethod
     * @return The current builder instance
     */
    public RestRequestObjectBuilder withRestRequestMethod(String requestMethod) {
        requestObject.setRestRequestMethod(requestMethod);
        return this;
    }
    
    public RestRequestObjectBuilder withRestUrl(String url) {
        requestObject.setRestUrl(url);
        return this;
    }
    
    /**
     * Specify the REST parameters for the request object
     * @param restParameters The new rest parameters of this request object
     * @return
     */
    public RestRequestObjectBuilder withRestParameters(List<TestObjectProperty> restParameters) {
        requestObject.setRestParameters(restParameters);
        return this;
    }

    /**
     * Specify HTTP headers for the request object
     * 
     * @param httpHeaderProperties list contains the HTTP headers for the request object
     * @return The current builder instance
     */
    public RestRequestObjectBuilder withHttpHeaders(List<TestObjectProperty> httpHeaderProperties) {
        requestObject.setHttpHeaderProperties(httpHeaderProperties);
        return this;
    }

    /**
     * Specify the text body content, default charset UTF-8
     * 
     * @param text The raw content as plain text
     * @return The current builder instance
     */
    public RestRequestObjectBuilder withTextBodyContent(String text) {
        HttpTextBodyContent textBodyContent = new HttpTextBodyContent(text);
        requestObject.setBodyContent(textBodyContent);
        return this;
    }

    /**
     * Specify the text body content
     * 
     * @param text The raw content as plain text
     * @param charset The character set to be used
     * @return The current builder instance
     */
    public RestRequestObjectBuilder withTextBodyContent(String text, String charset) {
        HttpTextBodyContent textBodyContent = new HttpTextBodyContent(text, charset);
        requestObject.setBodyContent(textBodyContent);
        return this;
    }

    /**
     * Specify the binary body content
     * 
     * @param filePath
     * @return The current builder instance
     * @throws IllegalArgumentException
     * @throws FileNotFoundException
     */
    public RestRequestObjectBuilder withFileBodyContent(String filePath)
            throws IllegalArgumentException, FileNotFoundException {
        HttpFileBodyContent fileBodyContent = new HttpFileBodyContent(filePath);
        requestObject.setBodyContent(fileBodyContent);
        return this;
    }

    /**
     * Specify the body content that composes of a list of url-encoded pairs,
     * default charset UTF-8
     * 
     * @param parameters List of url-encoded pairs
     * @return The current builder instance
     * @throws UnsupportedEncodingException
     */
    public RestRequestObjectBuilder withUrlEncodedBodyContent(List<UrlEncodedBodyParameter> parameters)
            throws UnsupportedEncodingException {

        HttpUrlEncodedBodyContent urlEncodedBodyContent = new HttpUrlEncodedBodyContent(parameters);
        requestObject.setBodyContent(urlEncodedBodyContent);
        return this;
    }

    /**
     * Specify the body content that composes of a list of url-encoded pairs
     * 
     * @param parameters List of url-encoded pairs
     * @param charset The character set to be used
     * @return The current builder instance
     * @throws UnsupportedEncodingException
     */
    public RestRequestObjectBuilder withUrlEncodedBodyContent(List<UrlEncodedBodyParameter> parameters, String charset)
            throws UnsupportedEncodingException {

        HttpUrlEncodedBodyContent urlEncodedBodyContent = new HttpUrlEncodedBodyContent(parameters, charset);
        requestObject.setBodyContent(urlEncodedBodyContent);
        return this;
    }

    /**
     * Specify the body content for multipart/form-data type, default charset UTF-8
     * 
     * @param parameters List of parameters which define body parts, either in text or binary form
     * @return The current builder instance
     * @throws FileNotFoundException
     */
    public RestRequestObjectBuilder withMultipartFormDataBodyContent(List<FormDataBodyParameter> parameters)
            throws FileNotFoundException {

        HttpFormDataBodyContent formDataBodyContent = new HttpFormDataBodyContent(parameters);
        requestObject.setBodyContent(formDataBodyContent);
        return this;
    }

    public RequestObject build() {
        return requestObject;
    }
}
