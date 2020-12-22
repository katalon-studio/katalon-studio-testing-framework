package com.kms.katalon.core.webservice.common;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.stream.Collectors;

import javax.wsdl.WSDLException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;

import com.ibm.wsdl.BindingOperationImpl;
import com.kms.katalon.core.network.ProxyInformation;
import com.kms.katalon.core.testobject.RequestObject;
import com.kms.katalon.core.webservice.constants.RequestHeaderConstants;
import com.kms.katalon.core.webservice.definition.WebServiceDefinitionLoaderProvider;
import com.kms.katalon.core.webservice.definition.WebServiceDefinitionLoaderSupplier;
import com.kms.katalon.core.webservice.exception.WebServiceException;
import com.kms.katalon.core.webservice.helper.WsdlLocatorProvider;
import com.kms.katalon.core.webservice.wsdl.support.wsdl.WsdlDefinitionLocator;
import com.kms.katalon.core.webservice.wsdl.support.wsdl.WsdlParser;
import com.kms.katalon.util.UrlUtil;

public class SoapClient extends BasicRequestor {

    private static final String SOAP_ACTION = RequestHeaderConstants.SOAP_ACTION;

    private static final String CONTENT_TYPE = RequestHeaderConstants.CONTENT_TYPE;

    private static final String TEXT_XML_CHARSET_UTF_8 = RequestHeaderConstants.CONTENT_TYPE_TEXT_XML_UTF_8;

    private static final String APPLICATION_XML = RequestHeaderConstants.CONTENT_TYPE_APPLICATION_XML;

    private String serviceName;

    private String endpoint;

    private String actionUri; // Can be SOAPAction, HTTP location URL

    public SoapClient(String projectDir, ProxyInformation proxyInformation) {
        super(projectDir, proxyInformation);
    }

    @Override
    protected HttpUriRequest buildHttpRequest(RequestObject requestObject) throws Exception {
        populateCommonData(requestObject);

        HttpPost post = new HttpPost(endpoint);

        if (requestObject.useServiceInfoFromWsdl()) {
            post.addHeader(CONTENT_TYPE, TEXT_XML_CHARSET_UTF_8);
            if (StringUtils.isNotBlank(actionUri)) {
                post.addHeader(SOAP_ACTION, actionUri);
            }
        }
        setHttpConnectionHeaders(post, requestObject);

        ByteArrayEntity entity = new ByteArrayEntity(requestObject.getSoapBody().getBytes(StandardCharsets.UTF_8));
        entity.setChunked(false);
        post.setEntity(entity);

        return post;
    }

    private void populateCommonData(RequestObject requestObject) throws WebServiceException {
        if (requestObject.useServiceInfoFromWsdl()) {
            populateDataFromWsdl(requestObject);
        } else {
            endpoint = requestObject.getSoapServiceEndpoint();
        }

        if (StringUtils.isBlank(endpoint)) {
            throw new WebServiceException("Service endpoint is undefined.");
        }
    }

    private void populateDataFromWsdl(RequestObject requestObject) throws WebServiceException {
        try {
            WsdlParser parser = getWsdlParser(requestObject);
            String requestMethod = requestObject.getSoapRequestMethod();
            endpoint = parser.getPortAddressLocation(requestMethod);
            BindingOperationImpl bindingOperation = parser
                    .getBindingOperationByRequestMethodAndName(requestMethod, requestObject.getSoapServiceFunction());
            actionUri = parser.getOperationURI(bindingOperation, requestMethod);
        } catch (IOException | GeneralSecurityException | WSDLException e) {
            throw new WebServiceException(e);
        }
    }

    private WsdlParser getWsdlParser(RequestObject requestObject) throws IOException, GeneralSecurityException {
        String wsdlLocation = requestObject.getWsdlAddress();
        WebServiceDefinitionLoaderSupplier loaderSupplier;
        if (UrlUtil.isWebUrl(wsdlLocation)) {
            loaderSupplier = (location) -> WebServiceDefinitionLoaderProvider.getLoaderForWebLocation(
                    location,
                    getHttpHeaderMap(requestObject),
                    proxyInformation,
                    getSslCertificateOption(),
                    getSSLSettings());
        } else {
            loaderSupplier = (location) -> WebServiceDefinitionLoaderProvider.getLoader(wsdlLocation);
        }
        WsdlDefinitionLocator wsdlLocator = WsdlLocatorProvider.getLocator(wsdlLocation, loaderSupplier);
        return new WsdlParser(wsdlLocator);
    }

    private Map<String, String> getHttpHeaderMap(RequestObject requestObject)
            throws GeneralSecurityException,
            IOException {
        Map<String, String> headers = getRequestHeaders(requestObject)
                .stream()
                .collect(Collectors.toMap(h -> h.getName(), h -> h.getValue()));
        return headers;
    }

    @Override
    protected String getResponseContentType(HttpResponse httpResponse) {
        return APPLICATION_XML;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
