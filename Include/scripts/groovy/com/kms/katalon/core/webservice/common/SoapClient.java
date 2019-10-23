package com.kms.katalon.core.webservice.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.wsdl.Definition;
import javax.wsdl.extensions.http.HTTPOperation;
import javax.wsdl.extensions.soap.SOAPOperation;
import javax.wsdl.extensions.soap12.SOAP12Operation;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLLocator;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.xml.sax.InputSource;

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
import com.kms.katalon.core.util.internal.ProxyUtil;
import com.kms.katalon.core.webservice.constants.CoreWebserviceMessageConstants;
import com.kms.katalon.core.webservice.constants.RequestHeaderConstants;
import com.kms.katalon.core.webservice.exception.WebServiceException;
import com.kms.katalon.core.webservice.helper.WebServiceCommonHelper;
import com.kms.katalon.util.Tools;

public class SoapClient extends BasicRequestor {
    
    private static final String GET = RequestHeaderConstants.GET;

    private static final String POST = RequestHeaderConstants.POST;

    private static final String SSL = RequestHeaderConstants.SSL;

    private static final String HTTPS = RequestHeaderConstants.HTTPS;

    private static final String SOAP = RequestHeaderConstants.SOAP;

    private static final String SOAP12 = RequestHeaderConstants.SOAP12;

    private static final String SOAP_ACTION = RequestHeaderConstants.SOAP_ACTION;

    private static final String CONTENT_TYPE = RequestHeaderConstants.CONTENT_TYPE;

    private static final String TEXT_XML_CHARSET_UTF_8 = RequestHeaderConstants.CONTENT_TYPE_TEXT_XML_UTF_8;

    private static final String APPLICATION_XML = RequestHeaderConstants.CONTENT_TYPE_APPLICATION_XML;

    private static final int MAX_REDIRECTS = 5;
    
    private String serviceName;

    private String protocol = SOAP; // Default is SOAP

    private String endPoint;

    private String actionUri; // Can be SOAPAction, HTTP location URL

    private RequestObject requestObject;

    public SoapClient(String projectDir, ProxyInformation proxyInformation) {
        super(projectDir, proxyInformation);
    }

    private void parseWsdl() throws WebServiceException {
        try {
            WSDLFactory factory = WSDLFactory.newInstance();
            WSDLReader reader = factory.newWSDLReader();
            reader.setFeature("javax.wsdl.verbose", false);
            reader.setFeature("javax.wsdl.importDocuments", true);
            
            Definition wsdlDefinition = reader.readWSDL(new CustomWSDLLocator(requestObject));
            
            lookForService(wsdlDefinition);
        } catch (Exception e) {
            throw new WebServiceException(e);
        }
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
            throws Exception {
        HttpClientBuilder clientBuilder = HttpClients.custom();
        
        if (!request.isFollowRedirects()) {
            clientBuilder.disableRedirectHandling();
        }
        
        clientBuilder.setConnectionManager(connectionManager);
        clientBuilder.setConnectionManagerShared(true);
        
        this.requestObject = request;
        parseWsdl();
       
        ProxyInformation proxyInfo = request.getProxy() != null ? request.getProxy() : proxyInformation;
        Proxy proxy = proxyInfo == null ? Proxy.NO_PROXY : ProxyUtil.getProxy(proxyInfo);
        if (!Proxy.NO_PROXY.equals(proxy) || proxy.type() != Proxy.Type.DIRECT) {
            configureProxy(clientBuilder, proxyInfo);
        }
        
//        HttpURLConnection con = (HttpURLConnection) oURL.openConnection(proxy);
        if (StringUtils.defaultString(endPoint).toLowerCase().startsWith(HTTPS)) {
            clientBuilder.setSSLHostnameVerifier(getHostnameVerifier());
        }
        HttpPost post = new HttpPost(endPoint);
        post.addHeader(CONTENT_TYPE, TEXT_XML_CHARSET_UTF_8);
        post.addHeader(SOAP_ACTION, actionUri);
        
        setHttpConnectionHeaders(post, request);

        InputStreamEntity reqStream = new InputStreamEntity(
                new ByteArrayInputStream(request.getSoapBody().getBytes(StandardCharsets.UTF_8)));
        post.setEntity(reqStream);
        
        CloseableHttpClient httpClient = clientBuilder.build();
        
        long startTime = System.currentTimeMillis();
        CloseableHttpResponse response = httpClient.execute(post, getHttpContext());
        int statusCode = response.getStatusLine().getStatusCode();
        long waitingTime = System.currentTimeMillis() - startTime;
        
        long headerLength = WebServiceCommonHelper.calculateHeaderLength(response);
        long contentDownloadTime = 0L;
        StringBuffer sb = new StringBuffer();

        char[] buffer = new char[1024];
        long bodyLength = 0L;
        try (InputStream inputStream = response.getEntity().getContent()) {
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
        
        setBodyContent(response, sb, responseObject);
        
        return responseObject;
    }
    
    

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
    // Refer to
    // https://github.com/SmartBear/soapui/blob/fe41852da853365c8be1ab331f713462695fd519/soapui/src/main/java/com/eviware/soapui/impl/wsdl/support/wsdl/AbstractWsdlDefinitionLoader.java
    private class CustomWSDLLocator implements WSDLLocator {

        private RequestObject request;

        private String last;

        public CustomWSDLLocator(RequestObject request) {
            this.request = request;
        }

        @Override
        public InputSource getBaseInputSource() {
            try {
                InputStream is = load(requestObject.getWsdlAddress());
                return new InputSource(is);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String getBaseURI() {
            return requestObject.getWsdlAddress();
        }

        @Override
        public InputSource getImportInputSource(String parent, String imp) {
            if (isAbsoluteUrl(imp)) {
                last = imp;
            } else {
                last = Tools.joinRelativeUrl(parent, imp);
            }
            try {
                InputStream input = load(last);
                return input == null ? null : new InputSource(input);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private boolean isAbsoluteUrl(String url) {
            url = url.toLowerCase();
            return url.startsWith("http:") || url.startsWith("https:") || url.startsWith("file:");
        }

        private InputStream load(String url) throws GeneralSecurityException, IOException, WebServiceException, URISyntaxException {
            HttpClientBuilder clientBuilder = HttpClients.custom();
            
            clientBuilder.disableRedirectHandling();

            clientBuilder.setConnectionManager(connectionManager);
            clientBuilder.setConnectionManagerShared(true);
            
            boolean isHttps = isHttps(url);
            if (isHttps) {
                SSLContext sc = SSLContext.getInstance(SSL);
                sc.init(null, getTrustManagers(), new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            }

            ProxyInformation proxyInfo = request.getProxy() != null ? request.getProxy() : proxyInformation;
            Proxy proxy = proxyInfo == null ? Proxy.NO_PROXY : ProxyUtil.getProxy(proxyInfo);
            if (!Proxy.NO_PROXY.equals(proxy) || proxy.type() != Proxy.Type.DIRECT) {
                configureProxy(clientBuilder, proxyInfo);
            }
            
            if (StringUtils.defaultString(url).toLowerCase().startsWith(HTTPS)) {
                clientBuilder.setSSLHostnameVerifier(getHostnameVerifier());
            }
            HttpGet get = new HttpGet(url);

            setHttpConnectionHeaders(get, requestObject);

            CloseableHttpClient httpClient = clientBuilder.build();
            CloseableHttpResponse response = httpClient.execute(get);
            InputStream is = response.getEntity().getContent();
            
            IOUtils.closeQuietly(httpClient);
            
            return is;
        }

        private boolean isHttps(String url) {
            url = url.toLowerCase();
            return url.startsWith("https");
        }

        @Override
        public String getLatestImportURI() {
            String result = last == null ? request.getWsdlAddress() : last;
            return result;
        }

        @Override
        public void close() {
        }
    }
}
