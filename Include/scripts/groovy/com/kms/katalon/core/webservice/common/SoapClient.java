package com.kms.katalon.core.webservice.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.http.HTTPOperation;
import javax.wsdl.extensions.soap.SOAPOperation;
import javax.wsdl.extensions.soap12.SOAP12Operation;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;

import com.ibm.wsdl.BindingOperationImpl;
import com.ibm.wsdl.PortImpl;
import com.ibm.wsdl.ServiceImpl;
import com.ibm.wsdl.extensions.http.HTTPAddressImpl;
import com.ibm.wsdl.extensions.http.HTTPBindingImpl;
import com.ibm.wsdl.extensions.soap.SOAPAddressImpl;
import com.ibm.wsdl.extensions.soap.SOAPBindingImpl;
import com.ibm.wsdl.extensions.soap12.SOAP12AddressImpl;
import com.ibm.wsdl.extensions.soap12.SOAP12BindingImpl;
import com.kms.katalon.core.network.ProxyInformation;
import com.kms.katalon.core.testobject.RequestObject;
import com.kms.katalon.core.testobject.ResponseObject;
import com.kms.katalon.core.webservice.constants.CoreWebserviceMessageConstants;
import com.kms.katalon.core.webservice.constants.RequestHeaderConstants;
import com.kms.katalon.core.webservice.exception.WebServiceException;
import com.kms.katalon.core.webservice.helper.WebServiceCommonHelper;

public class SoapClient extends BasicRequestor {

    private static final String POST = RequestHeaderConstants.POST;

    private static final String SSL = RequestHeaderConstants.SSL;

    private static final String HTTPS = RequestHeaderConstants.HTTPS;

    private static final String SOAP = RequestHeaderConstants.SOAP;

    private static final String SOAP12 = RequestHeaderConstants.SOAP12;

    private static final String SOAP_ACTION = RequestHeaderConstants.SOAP_ACTION;

    private static final String CONTENT_TYPE = RequestHeaderConstants.CONTENT_TYPE;

    private static final String TEXT_XML_CHARSET_UTF_8 = RequestHeaderConstants.CONTENT_TYPE_TEXT_XML_UTF_8;

    private static final String APPLICATION_XML = RequestHeaderConstants.CONTENT_TYPE_APPLICATION_XML;

    private String serviceName;

    private String protocol = SOAP; // Default is SOAP

    private String endPoint;

    private String actionUri; // Can be SOAPAction, HTTP location URL

    private RequestObject requestObject;

    public SoapClient(String projectDir, ProxyInformation proxyInformation) {
        super(projectDir, proxyInformation);
    }

    private void parseWsdl() throws WSDLException, WebServiceException {
        WSDLFactory factory = WSDLFactory.newInstance();
        WSDLReader reader = factory.newWSDLReader();
        reader.setFeature("javax.wsdl.verbose", false);
        reader.setFeature("javax.wsdl.importDocuments", true);
        Definition wsdlDefinition = reader.readWSDL(null, requestObject.getWsdlAddress());
        lookForService(wsdlDefinition);
    }

    // Look for the Service, but for now just consider the first one
    private void lookForService(Definition wsdlDefinition) throws WebServiceException {
        ServiceImpl service = null;
        Map<?, ?> services = wsdlDefinition.getAllServices();
        for (Object sKey : services.keySet()) {
            service = (ServiceImpl) services.get(sKey);
            setServiceName(((QName) sKey).getLocalPart());
            break;
        }

        parseService(service);
    }

    private void parseService(ServiceImpl service) throws WebServiceException {
        Map<?, ?> ports = service.getPorts();
        for (Object pKey : ports.keySet()) {

            PortImpl port = (PortImpl) ports.get(pKey);

            Object objBinding = port.getBinding().getExtensibilityElements().get(0);
            String proc = "";
            BindingOperationImpl operation = (BindingOperationImpl) port.getBinding()
                    .getBindingOperation(requestObject.getSoapServiceFunction(), null, null);
            if (operation == null) {
                throw new WebServiceException(CoreWebserviceMessageConstants.MSG_NO_SERVICE_OPERATION);
            }

            if (objBinding != null && objBinding instanceof SOAPBindingImpl) {
                proc = SOAP;
                endPoint = ((SOAPAddressImpl) port.getExtensibilityElements().get(0)).getLocationURI();
                actionUri = ((SOAPOperation) operation.getExtensibilityElements().get(0)).getSoapActionURI();
            } else if (objBinding != null && objBinding instanceof SOAP12BindingImpl) {
                proc = SOAP12;
                endPoint = ((SOAP12AddressImpl) port.getExtensibilityElements().get(0)).getLocationURI();
                actionUri = ((SOAP12Operation) operation.getExtensibilityElements().get(0)).getSoapActionURI();
            } else if (objBinding != null && objBinding instanceof HTTPBindingImpl) {
                proc = ((HTTPBindingImpl) objBinding).getVerb();
                endPoint = ((HTTPAddressImpl) port.getExtensibilityElements().get(0)).getLocationURI();
                actionUri = ((HTTPOperation) operation.getExtensibilityElements().get(0)).getLocationURI();
            }

            if (protocol.equals(proc)) {
                break;
            }
        }
    }

    private boolean isHttps(RequestObject request) {
        return StringUtils.defaultString(request.getWsdlAddress()).toLowerCase().startsWith(HTTPS);
    }

    @Override
    public ResponseObject send(RequestObject request)
            throws IOException, WSDLException, WebServiceException, GeneralSecurityException {
        this.requestObject = request;
        parseWsdl();
        boolean isHttps = isHttps(request);
        if (isHttps) {
            SSLContext sc = SSLContext.getInstance(SSL);
            sc.init(null, getTrustManagers(), new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }

        URL oURL = new URL(endPoint);
        HttpURLConnection con = (HttpURLConnection) oURL.openConnection(getProxy());
        if (con instanceof HttpsURLConnection) {
            ((HttpsURLConnection) con).setHostnameVerifier(getHostnameVerifier());
        }
        con.setRequestMethod(POST);
        con.setDoOutput(true);

        con.setRequestProperty(CONTENT_TYPE, TEXT_XML_CHARSET_UTF_8);
        con.setRequestProperty(SOAP_ACTION, actionUri);
        
        setHttpConnectionHeaders(con, request);

        OutputStream reqStream = con.getOutputStream();
        reqStream.write(request.getSoapBody().getBytes(StandardCharsets.UTF_8));
        
        long startTime = System.currentTimeMillis();
        int statusCode = con.getResponseCode();
        long waitingTime = System.currentTimeMillis() - startTime;
        
        long headerLength = WebServiceCommonHelper.calculateHeaderLength(con);
        long contentDownloadTime = 0L;
        StringBuffer sb = new StringBuffer();

        char[] buffer = new char[1024];
        long bodyLength = 0L;
        try (InputStream inputStream = (statusCode >= 400) ? con.getErrorStream() : con.getInputStream()) {
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                int len = 0;
                startTime = System.currentTimeMillis();
                while ((len = reader.read(buffer)) != -1) {
                    contentDownloadTime += System.currentTimeMillis() - startTime;
                    sb.append(buffer, 0, len);
                    bodyLength += len;
                    startTime = System.currentTimeMillis();
                }
            }
        }
        
        ResponseObject responseObject = new ResponseObject(sb.toString());
        
        String bodyLengthHeader = responseObject.getHeaderFields()
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals("Content-Length"))
                .map(entry -> entry.getValue().get(0))
                .findFirst()
                .orElse("");
        
        if (!StringUtils.isEmpty(bodyLengthHeader)) {
            bodyLength =  Long.parseLong(bodyLengthHeader, 10); 
        }
        // SOAP is HTTP-XML protocol

        responseObject.setContentType(APPLICATION_XML);
        responseObject.setStatusCode(statusCode);
        responseObject.setResponseBodySize(bodyLength);
        responseObject.setResponseHeaderSize(headerLength);
        responseObject.setWaitingTime(waitingTime);
        responseObject.setContentDownloadTime(contentDownloadTime);
        
        setBodyContent(con, sb, responseObject);
        
        return responseObject;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
