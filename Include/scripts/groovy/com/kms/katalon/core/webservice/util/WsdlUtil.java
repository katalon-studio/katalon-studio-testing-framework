package com.kms.katalon.core.webservice.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.http.HTTPAddress;
import javax.wsdl.extensions.http.HTTPBinding;
import javax.wsdl.extensions.http.HTTPOperation;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.wsdl.extensions.soap.SOAPOperation;
import javax.wsdl.extensions.soap12.SOAP12Address;
import javax.wsdl.extensions.soap12.SOAP12Binding;
import javax.wsdl.extensions.soap12.SOAP12Operation;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLLocator;
import javax.wsdl.xml.WSDLReader;

import org.apache.commons.lang3.StringUtils;
import org.reficio.ws.builder.SoapBuilder;
import org.reficio.ws.builder.SoapOperation;
import org.reficio.ws.builder.core.Wsdl;

import com.kms.katalon.constants.SystemProperties;
import com.kms.katalon.core.webservice.constants.RequestHeaderConstants;
import com.kms.katalon.entity.repository.WebElementPropertyEntity;
import com.kms.katalon.entity.repository.WebServiceRequestEntity;
import com.kms.katalon.entity.util.Util;
import com.kms.katalon.logging.LogUtil;

public class WsdlUtil {

    private static final String SOAP = "SOAP";

    private static final String SOAP12 = "SOAP12";

    public static Definition getDefinition(WSDLLocator wsdlLocator) throws WSDLException {
        WSDLFactory factory = WSDLFactory.newInstance();
        WSDLReader reader = factory.newWSDLReader();
        reader.setFeature("javax.wsdl.verbose", false);
        reader.setFeature("javax.wsdl.importDocuments", true);

        Definition definition = reader.readWSDL(wsdlLocator);

        return definition;
    }

    public static Port getFirstPortByBindingMethod(Definition definition, String method) {
        @SuppressWarnings("unchecked")
        Collection<Service> services = definition.getAllServices().values();
        if (services != null && !services.isEmpty()) {
            for (Service service : services) {
                @SuppressWarnings("unchecked")
                Collection<Port> ports = service.getPorts().values();
                if (ports != null && !ports.isEmpty()) {
                    for (Port port : ports) {
                        Binding binding = port.getBinding();
                        String bindingMethod = getBindingMethod(binding);
                        if (bindingMethod.equals(method)) {
                            return port;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static Binding getFirstBindingByMethod(Definition definition, String method) {
        @SuppressWarnings("unchecked")
        Collection<Binding> bindings = definition.getAllBindings().values();
        for (Binding binding : bindings) {
            String bindingMethod = getBindingMethod(binding);
            if (StringUtils.isNotBlank(bindingMethod) && bindingMethod.equals(method)) {
                return binding;
            }
        }
        return null;
    }

    public static String getPortAddressLocation(Port port) {
        List<?> extensibilityElements = port.getExtensibilityElements();

        SOAPAddress soapAddress = getExtensiblityElement(extensibilityElements, SOAPAddress.class);
        if (soapAddress != null) {
            return soapAddress.getLocationURI();
        }

        SOAP12Address soap12Address = getExtensiblityElement(extensibilityElements, SOAP12Address.class);
        if (soap12Address != null) {
            return soap12Address.getLocationURI();
        }

        HTTPAddress httpAddress = getExtensiblityElement(extensibilityElements, HTTPAddress.class);
        return httpAddress.getLocationURI();
    }

    public static WebServiceRequestEntity generateRequestEntity(
            Supplier<WebServiceRequestEntity> requestSupplier,
            String requestName,
            String wsdlLocation,
            String serviceEndpoint,
            String requestMethod,
            String operationName,
            String operationUri,
            String requestMessage) throws Exception {

        WebServiceRequestEntity entity = requestSupplier.get();

        entity.setElementGuidId(Util.generateGuid());
        entity.setName(requestName);
        entity.setSoapRequestMethod(requestMethod);
        entity.setWsdlAddress(wsdlLocation);
        entity.setSoapServiceFunction(operationName);
        entity.setKatalonVersion(System.getProperty(SystemProperties.KATALON_VERSION));
        entity.setUseServiceInfoFromWsdl(false);

        if (StringUtils.isNotBlank(serviceEndpoint)) {
            entity.setSoapServiceEndpoint(serviceEndpoint);
        }

        List<WebElementPropertyEntity> headers = new ArrayList<>();
        if (WebServiceRequestEntity.SOAP.equals(requestMethod) && StringUtils.isNotBlank(operationUri)) {
            WebElementPropertyEntity soapActionHeader = new WebElementPropertyEntity();
            soapActionHeader.setName(RequestHeaderConstants.SOAP_ACTION);
            soapActionHeader.setValue(operationUri);
            headers.add(soapActionHeader);
        }

        WebElementPropertyEntity contentTypeHeader = new WebElementPropertyEntity();
        contentTypeHeader.setName(RequestHeaderConstants.CONTENT_TYPE);
        contentTypeHeader.setValue(RequestHeaderConstants.CONTENT_TYPE_TEXT_XML_UTF_8);
        headers.add(contentTypeHeader);

        entity.setHttpHeaderProperties(headers);

        entity.setSoapBody(requestMessage != null ? requestMessage : "");

        return entity;
    }

    public static String getBindingMethod(Binding binding) {
        ExtensibilityElement bindingExElement = getBindingExtensiblityElement(binding);
        if (bindingExElement == null) {
            throw new IllegalArgumentException("Port has no binding extensibility element.");
        }

        if (bindingExElement instanceof SOAPBinding) {
            return SOAP;
        }

        if (bindingExElement instanceof SOAP12Binding) {
            return SOAP12;
        }

        if (bindingExElement instanceof HTTPBinding) {
            return ((HTTPBinding) bindingExElement).getVerb().toUpperCase();
        }

        throw new IllegalStateException("Unknown type of binding extensibility element");
    }

    private static ExtensibilityElement getBindingExtensiblityElement(Binding binding) {
        List<?> extensibilityElements = binding.getExtensibilityElements();
        ExtensibilityElement objBinding = getExtensiblityElement(extensibilityElements, SOAPBinding.class);
        if (objBinding == null) {
            objBinding = getExtensiblityElement(extensibilityElements, SOAP12Binding.class);
        }
        if (objBinding == null) {
            objBinding = getExtensiblityElement(extensibilityElements, HTTPBinding.class);
        }
        return objBinding;
    }

    public static BindingOperation getBindingOperation(Binding binding, String operationName) {
        @SuppressWarnings("unchecked")
        List<BindingOperation> operations = binding.getBindingOperations();
        if (operations != null && !operations.isEmpty()) {
            return operations.stream().filter(o -> o.getName().equals(operationName)).findFirst().orElse(null);
        }
        return null;
    }

    public static String getOperationUri(BindingOperation operation) {
        List<?> extensibilityElements = operation.getExtensibilityElements();

        SOAPOperation soapOperation = getExtensiblityElement(extensibilityElements, SOAPOperation.class);
        if (soapOperation != null) {
            return soapOperation.getSoapActionURI();
        }

        SOAP12Operation soap12Operation = getExtensiblityElement(extensibilityElements, SOAP12Operation.class);
        if (soap12Operation != null) {
            return soap12Operation.getSoapActionURI();
        }

        HTTPOperation httpOperation = getExtensiblityElement(extensibilityElements, HTTPOperation.class);
        if (httpOperation != null) {
            return httpOperation.getLocationURI();
        }

        throw new IllegalStateException("Unknown type of binding operation");
    }

    public static <T extends ExtensibilityElement> T getExtensiblityElement(List<?> list, Class<T> clazz) {
        List<T> elements = getExtensiblityElements(list, clazz);
        return elements.isEmpty() ? null : elements.get(0);
    }

    public static <T extends ExtensibilityElement> List<T> getExtensiblityElements(
            @SuppressWarnings("rawtypes") List list,
            Class<T> clazz) {
        List<T> result = new ArrayList<T>();

        for (@SuppressWarnings("unchecked")
        Iterator< T>i = list.iterator(); i.hasNext();) {
            T elm = i.next();
            if (clazz.isAssignableFrom(elm.getClass())) {
                result.add(elm);
            }
        }

        return result;
    }

    public static String getSampleRequestMessage(String wsdlLocation, String bindingName, String operationName) {
        try {
            Wsdl wsdl = Wsdl.parse(wsdlLocation);

            SoapBuilder builder = wsdl.binding().name(bindingName).find();
            SoapOperation operation = builder.operation().name(operationName).find();
            String message = builder.buildInputMessage(operation);
            return message;
        } catch (Exception e) {
            LogUtil.printAndLogError(e);
            return "";
        }
    }
}
