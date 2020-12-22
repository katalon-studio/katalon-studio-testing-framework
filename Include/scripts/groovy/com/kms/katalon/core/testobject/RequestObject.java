package com.kms.katalon.core.testobject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kms.katalon.core.network.ProxyInformation;
import com.kms.katalon.core.testobject.impl.HttpFileBodyContent;
import com.kms.katalon.core.testobject.impl.HttpFormDataBodyContent;
import com.kms.katalon.core.testobject.impl.HttpTextBodyContent;
import com.kms.katalon.core.testobject.impl.HttpUrlEncodedBodyContent;

public class RequestObject extends TestObject implements HttpMessage {

    /**
     * Set connection/socket timeout to this value mean to unset its current timeout.
     * The project's socket timeout will be used.
     */
    public static final int TIMEOUT_UNSET = -1;

    /**
     * Set connection/socket timeout to this value mean to this request has no connection/socket timeout limit.
     */
    public static final int TIMEOUT_UNLIMITED = 0;

    /**
     * The default connection/socket timeout. The default value is set to unlimited.
     */
    public static final int DEFAULT_TIMEOUT = TIMEOUT_UNLIMITED;

    /**
     * Set max response size to this value mean to unset its current max response size.
     * The project's max response size will be used.
     */
    public static final long MAX_RESPONSE_SIZE_UNSET = -1;

    /**
     * Set response size limit to this value mean to this request has no response size limit.
     */
    public static final long MAX_RESPONSE_SIZE_UNLIMITED = 0;

    /**
     * The default maximum response size. Default value is same to unlimited.
     */
    public static final long DEFAULT_MAX_RESPONSE_SIZE = MAX_RESPONSE_SIZE_UNLIMITED;

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

    private String soapServiceEndpoint = "";

    private boolean useServiceInfoFromWsdl = false;

    private List<TestObjectProperty> restParameters;

    private HttpBodyContent bodyContent;

    private String objectId;

    private String verificationScript;

    private Map<String, Object> variables;

    private boolean followRedirects;

    private int redirectTimes = 0;
    
    private int connectionTimeout = TIMEOUT_UNSET;
    
    private int socketTimeout = TIMEOUT_UNSET;
    
    private long maxResponseSize = RequestObject.MAX_RESPONSE_SIZE_UNSET;

    private ProxyInformation proxy;

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
     * @param objectId
     *            the new id of this request object
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
     * @param name
     *            the new name of this request object
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
     * @param serviceType
     *            the new request type for this request object
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
     * @param httpHeaderProperties
     *            the new list contains the http headers for this request object
     */
    public void setHttpHeaderProperties(List<TestObjectProperty> httpHeaderProperties) {
        this.httpHeaderProperties = httpHeaderProperties;
    }

    /**
     * Get the http body for this request object
     * 
     * @return the http body for this request object as a String
     * @deprecated Deprecated from 5.4. Please use
     *             {@link #setBodyContent(HttpBodyContent)} instead.
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
     * @param httpBody
     *            the new http body for this request object as a String
     * @deprecated Deprecated from 5.4. Please use
     *             {@link #setBodyContent(HttpBodyContent)} instead.
     */
    public void setHttpBody(String httpBody) {
        this.bodyContent = new HttpTextBodyContent(httpBody);
        this.httpBody = httpBody;
    }

    /**
     * Get the wsdl address of this request object if it is a "SOAP" request
     * object
     * 
     * @return the wsdl address of this request object if it is a "SOAP" request
     *         object, or null if it is not
     */
    public String getWsdlAddress() {
        return wsdlAddress;
    }

    /**
     * Set the wsdl address of this request object
     * 
     * @param wsdlAddress
     *            the new wsdl address of this request object
     */
    public void setWsdlAddress(String wsdlAddress) {
        this.wsdlAddress = wsdlAddress;
    }

    /**
     * Get the soap body of this request object if it is a "SOAP" request object
     * 
     * @return the soap body of this request object if it is a "SOAP" request
     *         object, or null if it is not
     */
    public String getSoapBody() {
        return soapBody;
    }

    /**
     * Set the soap body for this request object
     * 
     * @param soapBody
     *            the new soap body for this request object
     */
    public void setSoapBody(String soapBody) {
        this.soapBody = soapBody;
    }

    /**
     * Get the soap request method of this request object if it is a "SOAP"
     * request object
     * <p>
     * Possible values: SOAP, SOAP12, GET, POST
     * 
     * @return the soap request method of this request object if it is a "SOAP"
     *         request object, or null if it is not
     */
    public String getSoapRequestMethod() {
        return soapRequestMethod;
    }

    /**
     * Set the soap request method for this request object
     * 
     * @param soapRequestMethod
     *            the new soap request method for this request object
     */
    public void setSoapRequestMethod(String soapRequestMethod) {
        this.soapRequestMethod = soapRequestMethod;
    }

    /**
     * Get a flag that determines whether to use the service info (service
     * endpoint, SOAP action,...) parsed from WSDL when sending a SOAP request.
     * 
     * @since 7.4.5
     */
    public boolean useServiceInfoFromWsdl() {
        return useServiceInfoFromWsdl;
    }

    /**
     * Set a flag that determines whether to use the service info (service
     * endpoint, SOAP action,...) parsed from WSDL when sending a SOAP request.
     * 
     * @param useServiceInfoFromWsdl
     * @since 7.4.5
     */
    public void setUseServiceInfoFromWsdl(boolean useServiceInfoFromWsdl) {
        this.useServiceInfoFromWsdl = useServiceInfoFromWsdl;
    }

    /**
     * Get the rest url of this request object if it is a "RESTful" request
     * object
     * 
     * @return the rest url of this request object if it is a "RESTful" request
     *         object, or null if it is not
     */
    public String getRestUrl() {
        return restUrl;
    }

    /**
     * Set the rest url for this request object
     * 
     * @param restUrl
     *            the new rest url for this request object
     */
    public void setRestUrl(String restUrl) {
        this.restUrl = restUrl;
    }

    /**
     * Get the rest request method of this request object if it is a "RESTful" request object
     * 
     * @return the rest request method of this request object if it is a "RESTful" request object, or null if it is not
     */
    public String getRestRequestMethod() {
        return restRequestMethod;
    }

    /**
     * Set the rest request method for this request object
     * 
     * @param restRequestMethod the new request method for this request object
     */
    public void setRestRequestMethod(String restRequestMethod) {
        this.restRequestMethod = restRequestMethod;
    }

    /**
     * Get the rest parameters of this request object if it is a "RESTful"
     * request object
     * 
     * @return the rest parameters of this request object if it is a "RESTful"
     *         request object, or empty list if it is not
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
     * @param restParameters
     *            the new rest parameters of this request object
     */
    public void setRestParameters(List<TestObjectProperty> restParameters) {
        this.restParameters = restParameters;
    }

    /**
     * Get the soap service function of this request object if it is a "SOAP"
     * request object
     * 
     * @return the soap service function of this request object if it is a
     *         "SOAP" request object, or null if it is not
     */
    public String getSoapServiceFunction() {
        return soapServiceFunction;
    }

    /**
     * Set the soap service function for this request object
     * 
     * @param soapServiceFunction
     *            the new soap service function for this request object
     */
    public void setSoapServiceFunction(String soapServiceFunction) {
        this.soapServiceFunction = soapServiceFunction;
    }

    /**
     * Get SOAP service endpoint
     * 
     * @since 7.4.5
     */
    public String getSoapServiceEndpoint() {
        return soapServiceEndpoint;
    }

    /**
     * Set SOAP service endpoint
     * 
     * @since 7.4.5
     */
    public void setSoapServiceEndpoint(String soapServiceEndpoint) {
        this.soapServiceEndpoint = soapServiceEndpoint;
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
     * 
     * @param bodyContent
     *            an implementation of {@link HttpBodyContent}
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

    public boolean isFollowRedirects() {
        return followRedirects;
    }

    public void setFollowRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
    }

    public int getRedirectTimes() {
        return redirectTimes;
    }

    public void setRedirectTimes(int redirectTimes) {
        this.redirectTimes = redirectTimes;
    }

    /**
     * Get the proxy of this request. This proxy will take precedence over proxy
     * settings in Preferences.
     */
    public ProxyInformation getProxy() {
        return proxy;
    }

    /**
     * Set the proxy for this request. This proxy will take precedence over
     * proxy settings in Preferences.
     */
    public void setProxy(ProxyInformation proxy) {
        this.proxy = proxy;
    }

    /**
     * Get the connection timeout of this request in milliseconds.
     * @since 7.6.0
     */
    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * Set the connection timeout for this request.
     * @param connectionTimeout The connection timeout in milliseconds.<br>
     *      A timeout value of zero or negative is interpreted as an infinite timeout.<br>
     *      
     * <br>Additional available values:
     * <ul>
     *  <li><b>RequestObject.TIMEOUT_UNLIMITED</b>: Set the connection timeout of this request to unlimited.</li>
     *  <li><b>RequestObject.DEFAULT_TIMEOUT</b>: Set the connection timeout of this request to the default value (The default value is set to unlimited).</li>
     *  <li><b>RequestObject.TIMEOUT_UNSET</b>: Unset the connection timeout of this request. The project's connection timeout will be used.</li>
     * </ul>
     * @since 7.6.0
     */
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    /**
     * Get the socket timeout of this request in milliseconds.
     * @since 7.6.0
     */
    public int getSocketTimeout() {
        return socketTimeout;
    }

    /**
     * Set the socket timeout for this request.
     * @param socketTimeout The socket timeout in milliseconds.<br>
     *      A timeout value of zero or negative is interpreted as an infinite timeout.<br>
     *      
     * <br>Additional available values:
     * <ul>
     *  <li><b>RequestObject.TIMEOUT_UNLIMITED</b>: Set the socket timeout of this request to unlimited.</li>
     *  <li><b>RequestObject.DEFAULT_TIMEOUT</b>: Set the socket timeout of this request to the default value (The default value is set to unlimited).</li>
     *  <li><b>RequestObject.TIMEOUT_UNSET</b>: Unset the socket timeout of this request. The project's socket timeout will be used.</li>
     * </ul>
     * @since 7.6.0
     */
    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    /**
     * Get the maximum response size of this request in bytes.
     * @since 7.6.0
     */
    public long getMaxResponseSize() {
        return maxResponseSize;
    }

    /**
     * Set the maximum response size for this request.
     * @param maxResponseSize The response size limit in bytes.<br>
     *      A max response size value of zero or negative is interpreted as an unlimited response size.<br>
     *
     * <br>Additional available values:
     * <ul>
     *  <li><b>RequestObject.MAX_RESPONSE_SIZE_UNLIMITED</b>: Set the maximum response size of this request to unlimited.</li>
     *  <li><b>RequestObject.DEFAULT_MAX_RESPONSE_SIZE</b>: Set the maximum response size of this request to the default value (The default value is set to unlimited).</li>
     *  <li><b>RequestObject.MAX_RESPONSE_SIZE_UNSET</b>: Unset the maximum response size of this request. The project's maximum response size will be used.</li>
     * </ul>
     * @since 7.6.0
     */
    public void setMaxResponseSize(long maxResponseSize) {
        this.maxResponseSize = maxResponseSize;
    }
}
