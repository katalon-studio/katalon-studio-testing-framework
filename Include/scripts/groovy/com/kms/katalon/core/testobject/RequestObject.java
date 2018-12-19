package com.kms.katalon.core.testobject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kms.katalon.core.testobject.impl.HttpFileBodyContent;
import com.kms.katalon.core.testobject.impl.HttpFormDataBodyContent;
import com.kms.katalon.core.testobject.impl.HttpTextBodyContent;
import com.kms.katalon.core.testobject.impl.HttpUrlEncodedBodyContent;

public class RequestObject extends TestObject implements HttpMessage {

    private static final String DF_CHARSET = "UTF-8";

    private String name;

    private String serviceType;

    private List<TestObjectProperty> httpHeaderProperties;

    @Deprecated
    private String httpBody = "";

    private String wsdlAddress = "";

    private String soapBody = "";

    private String soapRequestMethod = "";

    private String restUrl = "";

    private String restRequestMethod = "";

    private String soapServiceFunction = "";

    private List<TestObjectProperty> restParameters;

    private HttpBodyContent bodyContent;

    private String objectId;
    
    private String verificationScript;
    
    private Map<String, Object> variables;

    public RequestObject(String objectId) {
        this.objectId = objectId;
    }

    /**
     * Get the id of this request object
     * 
     * @return the id of this request object
     */
    public String getObjectId() {
        return objectId;
    }

    /**
     * Set the id for this request object
     * 
     * @param objectId the new id of this request object
     */
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    /**
     * Get the name of this request object
     * 
     * @return the name of this request object
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name for this request object
     * 
     * @param name the new name of this request object
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return the request type for this request object
     * <p>
     * Possible values: SOAP, RESTful
     * 
     * @return the request type for this request object
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * Set the service type for this request object
     * 
     * @param serviceType the new request type for this request object
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * Get the http headers of this request object
     * 
     * @return the list contains the http headers of this request object
     */
    public List<TestObjectProperty> getHttpHeaderProperties() {
        if (httpHeaderProperties == null) {
            httpHeaderProperties = new ArrayList<TestObjectProperty>();
        }
        return httpHeaderProperties;
    }

    /**
     * Set the http headers of this request object
     * 
     * @param httpHeaderProperties the new list contains the http headers for this request object
     */
    public void setHttpHeaderProperties(List<TestObjectProperty> httpHeaderProperties) {
        this.httpHeaderProperties = httpHeaderProperties;
    }

    /**
     * Get the http body for this request object
     * 
     * @return the http body for this request object as a String
     * @deprecated Deprecated from 5.4. Please use {@link #setBodyContent(HttpBodyContent)} instead.
     */
    public String getHttpBody() {
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        try {
            bodyContent.writeTo(outstream);
            return outstream.toString(DF_CHARSET);
        } catch (IOException ignored) {
        }
        return httpBody;
    }

    /**
     * Set the http body for this request object
     * 
     * @param httpBody the new http body for this request object as a String
     * @deprecated Deprecated from 5.4. Please use {@link #setBodyContent(HttpBodyContent)} instead.
     */
    public void setHttpBody(String httpBody) {
        this.bodyContent = new HttpTextBodyContent(httpBody);
        this.httpBody = httpBody;
    }

    /**
     * Get the wsdl address of this request object if it is a "SOAP" request object
     * 
     * @return the wsdl address of this request object if it is a "SOAP" request object, or null if it is not
     */
    public String getWsdlAddress() {
        return wsdlAddress;
    }

    /**
     * Set the wsdl address of this request object
     * 
     * @param wsdlAddress the new wsdl address of this request object
     */
    public void setWsdlAddress(String wsdlAddress) {
        this.wsdlAddress = wsdlAddress;
    }

    /**
     * Get the soap body of this request object if it is a "SOAP" request object
     * 
     * @return the soap body of this request object if it is a "SOAP" request object, or null if it is not
     */
    public String getSoapBody() {
        return soapBody;
    }

    /**
     * Set the soap body for this request object
     * 
     * @param soapBody the new soap body for this request object
     */
    public void setSoapBody(String soapBody) {
        this.soapBody = soapBody;
    }

    /**
     * Get the soap request method of this request object if it is a "SOAP" request object
     * <p>
     * Possible values: SOAP, SOAP12, GET, POST
     * 
     * @return the soap request method of this request object if it is a "SOAP" request object, or null if it is not
     */
    public String getSoapRequestMethod() {
        return soapRequestMethod;
    }

    /**
     * Set the soap request method for this request object
     * 
     * @param soapRequestMethod the new soap request method for this request object
     */
    public void setSoapRequestMethod(String soapRequestMethod) {
        this.soapRequestMethod = soapRequestMethod;
    }

    /**
     * Get the rest url of this request object if it is a "RESTful" request object
     * 
     * @return the rest url of this request object if it is a "RESTful" request object, or null if it is not
     */
    public String getRestUrl() {
        return restUrl;
    }

    /**
     * Set the rest url for this request object
     * 
     * @param restUrl the new rest url for this request object
     */
    public void setRestUrl(String restUrl) {
        this.restUrl = restUrl;
    }

    /**
     * Get the rest request of this request object if it is a "RESTful" request object
     * <p>
     * Possible values: GET, POST, PUT, DELETE
     * 
     * @return the get request of this request object if it is a "RESTful" request object, or null if it is not
     */
    public String getRestRequestMethod() {
        return restRequestMethod;
    }

    /**
     * Set the rest request for this request object
     * 
     * @param restRequestMethod the new get request for this request object
     */
    public void setRestRequestMethod(String restRequestMethod) {
        this.restRequestMethod = restRequestMethod;
    }

    /**
     * Get the rest parameters of this request object if it is a "RESTful" request object
     * 
     * @return the rest parameters of this request object if it is a "RESTful" request object, or empty list if it is
     * not
     */
    public List<TestObjectProperty> getRestParameters() {
        if (restParameters == null) {
            restParameters = new ArrayList<TestObjectProperty>();
        }
        return restParameters;
    }

    /**
     * Set the rest parameters for this request object
     * 
     * @param restParameters the new rest parameters of this request object
     */
    public void setRestParameters(List<TestObjectProperty> restParameters) {
        this.restParameters = restParameters;
    }

    /**
     * Get the soap service function of this request object if it is a "SOAP" request object
     * 
     * @return the soap service function of this request object if it is a "SOAP" request object, or null if it is not
     */
    public String getSoapServiceFunction() {
        return soapServiceFunction;
    }

    /**
     * Set the soap service function for this request object
     * 
     * @param soapServiceFunction the new soap service function for this request object
     */
    public void setSoapServiceFunction(String soapServiceFunction) {
        this.soapServiceFunction = soapServiceFunction;
    }

    /**
     * Gets the body content of request.
     * 
     * @see {@link HttpTextBodyContent}
     * @since 5.4
     */
    @Override
    public HttpBodyContent getBodyContent() {
        return bodyContent;
    }

    /**
     * Sets the body content for this request.
     * @param bodyContent an implementation of {@link HttpBodyContent}
     * 
     * @see {@link HttpTextBodyContent}
     * @see {@link HttpFileBodyContent}
     * @see {@link HttpFormDataBodyContent}
     * @see {@link HttpUrlEncodedBodyContent}
     */
    public void setBodyContent(HttpBodyContent bodyContent) {
        this.bodyContent = bodyContent;
    }

    public String getVerificationScript() {
        return verificationScript;
    }

    public void setVerificationScript(String verificationScript) {
        this.verificationScript = verificationScript;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }
}
