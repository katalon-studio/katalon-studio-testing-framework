package com.kms.katalon.core.webservice.wsdl.support.wsdl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.Input;
import javax.wsdl.Operation;
import javax.wsdl.Types;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.http.HTTPAddress;
import javax.wsdl.extensions.http.HTTPBinding;
import javax.wsdl.extensions.http.HTTPOperation;
import javax.wsdl.extensions.schema.Schema;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.wsdl.extensions.soap.SOAPOperation;
import javax.wsdl.extensions.soap12.SOAP12Address;
import javax.wsdl.extensions.soap12.SOAP12Binding;
import javax.wsdl.extensions.soap12.SOAP12Operation;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLLocator;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.commons.lang3.StringUtils;
import org.reficio.ws.builder.SoapBuilder;
import org.reficio.ws.builder.SoapOperation;
import org.reficio.ws.builder.core.Wsdl;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.wsdl.BindingImpl;
import com.ibm.wsdl.BindingOperationImpl;
import com.ibm.wsdl.PartImpl;
import com.ibm.wsdl.PortImpl;
import com.ibm.wsdl.ServiceImpl;
import com.ibm.wsdl.extensions.http.HTTPBindingImpl;
import com.ibm.wsdl.extensions.soap.SOAPBindingImpl;
import com.ibm.wsdl.extensions.soap12.SOAP12BindingImpl;
import com.kms.katalon.core.webservice.helper.SafeHelper;
import com.kms.katalon.core.webservice.helper.XmlHelper;
import com.kms.katalon.logging.LogUtil;

@SuppressWarnings("unchecked")
public class WsdlParser {

    private static final String THIS_NAMESPACE = "tns";

    private static final String SOAP_HEADER_ACTION = "SOAPAction";

    private static final String SOAP = "SOAP";

    private static final String SOAP12 = "SOAP12";

    private static final String GET = "GET";

    private static final String POST = "POST";

    private Definition definition;

    private Map<String, PortImpl> portMap;

    private WSDLLocator wsdlLocator;

    public WsdlParser(WsdlDefinitionLocator locator) {
        this.wsdlLocator = locator;
    }

    public Definition getDefinition() throws WSDLException {
        if (definition != null) {
            return definition;
        }

        WSDLFactory factory = WSDLFactory.newInstance();
        WSDLReader reader = factory.newWSDLReader();
        reader.setFeature("javax.wsdl.verbose", false);
        reader.setFeature("javax.wsdl.importDocuments", true);

        definition = reader.readWSDL(wsdlLocator);

        return definition;
    }

    public List<QName> getServiceNames() throws WSDLException {
        return new ArrayList<QName>(getDefinition().getServices().keySet());
    }

    public List<PortImpl> getPorts() throws WSDLException {
        ServiceImpl service = getService();
        if (service == null) {
            return Collections.emptyList();
        }

        Collection<PortImpl> ports = service.getPorts().values();
        if (ports.isEmpty()) {
            return Collections.emptyList();
        }

        return new ArrayList<PortImpl>(ports);
    }

    public ServiceImpl getService() throws WSDLException {
        Collection<ServiceImpl> services = getDefinition().getAllServices().values();
        if (services.isEmpty()) {
            return null;
        }
        return new ArrayList<ServiceImpl>(services).get(0);
    }

    public PortImpl getPortByRequestMethod(String requestMethod) throws WSDLException {
        return getPortMap().get(requestMethod);
    }

    public List<BindingImpl> getBindings() throws WSDLException {
        return new ArrayList<BindingImpl>(getDefinition().getAllBindings().values());
    }

    public List<QName> getBindingNames() throws WSDLException {
        return new ArrayList<QName>(getDefinition().getAllBindings().keySet());
    }

    public Binding getBindingByName(QName bindingName) throws WSDLException {
        return getDefinition().getBinding(bindingName);
    }

    public BindingImpl getBindingByRequestMethod(String requestMethod) throws WSDLException {
        PortImpl port = getPortByRequestMethod(requestMethod);
        return (BindingImpl) port.getBinding();
    }

    public List<String> getOperationNamesByRequestMethod(String requestMethod) throws WSDLException {
        List<String> operationNames = new ArrayList<>();
        getOperationsByRequestMethod(requestMethod).forEach(o -> operationNames.add(o.getName()));
        return operationNames;
    }

    public List<Operation> getOperationsByRequestMethod(String requestMethod) throws WSDLException {
        List<Operation> operations = new ArrayList<>();
        getBindingOperationsByRequestMethod(requestMethod).forEach(o -> operations.add(o.getOperation()));
        return operations;
    }

    public BindingOperationImpl getBindingOperationByRequestMethodAndName(String requestMethod, String name)
            throws WSDLException {
        if (StringUtils.isBlank(requestMethod) || StringUtils.isBlank(name)) {
            return null;
        }

        return getBindingOperationsByRequestMethod(requestMethod).stream().filter(bo -> name.equals(bo.getName()))
                .findAny().orElse(null);
    }

    public List<BindingOperationImpl> getBindingOperationsByRequestMethod(String requestMethod) throws WSDLException {
        PortImpl port = getPortMap().get(requestMethod);
        if (port == null) {
            return Collections.emptyList();
        }

        List<BindingOperationImpl> bindingOperations = new ArrayList<>();
        bindingOperations.addAll(port.getBinding().getBindingOperations());
        return bindingOperations;
    }

    public Operation getOperationByRequestMethodNName(String requestMethod, String operationName) throws WSDLException {
        return getOperationsByRequestMethod(requestMethod).stream().filter(o -> o.getName().equals(operationName))
                .findFirst().get();
    }

    public String getOperationURI(BindingOperationImpl bindingOperation, String requestMethod) {
        if (bindingOperation == null) {
            return null;
        }

        if (StringUtils.isBlank(requestMethod)) {
            return null;
        }

        List<?> extensibilityElements = bindingOperation.getExtensibilityElements();
        switch (requestMethod.toUpperCase()) {
        case SOAP: {
            SOAPOperation operation = getExtensiblityElement(extensibilityElements, SOAPOperation.class);
            return operation.getSoapActionURI();
        }
        case SOAP12: {
            SOAP12Operation operation = getExtensiblityElement(extensibilityElements, SOAP12Operation.class);
            return operation.getSoapActionURI();
        }
        case GET:
        case POST: {
            HTTPOperation operation = getExtensiblityElement(extensibilityElements, HTTPOperation.class);
            return operation.getLocationURI();
        }
        default: {
            return null;
        }
        }
    }

    public String getPortAddressLocation(String requestMethod) throws WSDLException {
        if (StringUtils.isBlank(requestMethod)) {
            return null;
        }

        PortImpl port = getPortByRequestMethod(requestMethod);
        if (port == null) {
            return null;
        }

        List<?> extensibilityElements = port.getExtensibilityElements();

        switch (requestMethod.toUpperCase()) {
        case SOAP: {
            SOAPAddress address = getExtensiblityElement(extensibilityElements, SOAPAddress.class);
            return address.getLocationURI();
        }
        case SOAP12: {
            SOAP12Address address = getExtensiblityElement(extensibilityElements, SOAP12Address.class);
            return address.getLocationURI();
        }
        default: {
            HTTPAddress address = getExtensiblityElement(extensibilityElements, HTTPAddress.class);
            return address.getLocationURI();
        }
        }
    }

    public Map<String, PortImpl> getPortMap() throws WSDLException {
        if (portMap == null) {
            portMap = new HashMap<>();
            for (PortImpl p : getPorts()) {
                Object binding = getBindingObject(p.getBinding());

                if (binding == null) {
                    continue;
                }

                if (binding instanceof SOAPBindingImpl) {
                    portMap.put(SOAP, p);
                    continue;
                }

                if (binding instanceof SOAP12BindingImpl) {
                    portMap.put(SOAP12, p);
                    continue;
                }

                if (binding instanceof HTTPBindingImpl) {
                    portMap.put(((HTTPBindingImpl) binding).getVerb().toUpperCase(), p);
                    continue;
                }
            }
        }
        return portMap;
    }

    private static Object getBindingObject(Binding binding) {
        List<?> extensibilityElements = binding.getExtensibilityElements();
        Object objBinding = getExtensiblityElement(extensibilityElements, SOAPBinding.class);
        if (objBinding == null) {
            objBinding = getExtensiblityElement(extensibilityElements, SOAP12Binding.class);
        }
        if (objBinding == null) {
            objBinding = getExtensiblityElement(extensibilityElements, HTTPBinding.class);
        }
        return objBinding;
    }

    public static <T extends ExtensibilityElement> T getExtensiblityElement(List<?> list, Class<T> clazz) {
        List<T> elements = getExtensiblityElements(list, clazz);
        return elements.isEmpty() ? null : elements.get(0);
    }

    public static <T extends ExtensibilityElement> List<T> getExtensiblityElements(
            @SuppressWarnings("rawtypes") List list, Class<T> clazz) {
        List<T> result = new ArrayList<T>();

        for (Iterator< T>i = list.iterator(); i.hasNext();) {
            T elm = i.next();
            if (clazz.isAssignableFrom(elm.getClass())) {
                result.add(elm);
            }
        }

        return result;
    }

    private Map<String, List<String>> getParamMap() {
        Map<String, List<String>> paramMap = new HashMap<>();
        try {
            List<String> operationNames = getOperationNamesByRequestMethod(SOAP);
            Types types = getDefinition().getTypes();
            for (Object obj : SafeHelper.safeList(types.getExtensibilityElements())) {
                if (obj != null && obj instanceof Schema) {
                    Schema schema = (Schema) obj;
                    NodeList nodeList = schema.getElement().getChildNodes();
                    if (nodeList != null) {
                        for (String name : operationNames) {
                            if (StringUtils.isEmpty(name)) {
                                continue;
                            }
                            List<String> params = XmlHelper.wrapNodeList(nodeList).stream()
                                    .filter(a -> a.getNodeType() == Node.ELEMENT_NODE).filter(a -> {
                                        Node node = a.getAttributes().getNamedItem("name");
                                        return node != null && node.getNodeValue().equals(name);
                                    }).flatMap(WsdlParser::flatten).filter(a -> a.getNodeType() == Node.ELEMENT_NODE)
                                    .filter(a -> a.getAttributes().getNamedItem("name") != null)
                                    .filter(a -> !a.getAttributes().getNamedItem("name").getNodeValue().equals(name))
                                    .map(a -> a.getAttributes().getNamedItem("name").getNodeValue())
                                    .filter(a -> a != null).collect(Collectors.toList());
                            paramMap.put(name, params);
                        }
                    }
                }
            }
            return paramMap;
        } catch (Exception ex) {
            return Collections.emptyMap();
        }
    }

    private static Stream<Node> flatten(Node node) {
        return Stream.concat(Stream.of(node),
                XmlHelper.wrapNodeList(node.getChildNodes()).stream().flatMap(WsdlParser::flatten));
    }

    public String generateInputSOAPMessage(String requestMethod, String operationName)
            throws WSDLException, SOAPException, IOException {
        try {
            String wsdlLocation = wsdlLocator.getBaseURI();
            Wsdl wsdl = Wsdl.parse(wsdlLocation);
            BindingImpl binding = getBindingByRequestMethod(requestMethod);
            SoapBuilder builder = wsdl.binding().name(binding.getQName()).find();
            SoapOperation operation = builder.operation().name(operationName).find();
            String message = builder.buildInputMessage(operation);
            return message;
        } catch (Exception e) {
            LogUtil.logError(e);
            // fall back to old mechanism to generate input message in case of
            // exception
            BindingOperationImpl bindingOperation = getBindingOperationsByRequestMethod(requestMethod).stream()
                    .filter(bo -> bo.getName().equals(operationName)).findFirst().get();
            String operationURI = getOperationURI(bindingOperation, requestMethod);

            String namespaceURI = getService().getQName().getNamespaceURI();
            SOAPMessage soapMessage = generateInputSOAPMessageOldMechanism(requestMethod, namespaceURI, operationURI,
                    operationName, getParamMap());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            soapMessage.writeTo(out);
            return new String(out.toByteArray(), StandardCharsets.UTF_8);
        }
    }

    public SOAPMessage generateInputSOAPMessageOldMechanism(String requestMethod, String namespaceURI, String actionUri,
            String operationName, Map<String, List<String>> paramMap) throws SOAPException, WSDLException {
        SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(THIS_NAMESPACE, namespaceURI);

        // SOAP Body
        SOAPElement soapBodyElemment = envelope.getBody().addChildElement(operationName, THIS_NAMESPACE);
        if (paramMap.get(operationName) != null) {

            List<String> params = paramMap.get(operationName);
            for (String param : params) {
                soapBodyElemment.addChildElement(param, THIS_NAMESPACE).addTextNode("?");
            }

        } else {
            // Input Parameters
            Input input = getOperationByRequestMethodNName(requestMethod, operationName).getInput();
            Collection<PartImpl> parts = input.getMessage().getParts().values();

            for (PartImpl part : parts) {
                soapBodyElemment.addChildElement(part.getName(), THIS_NAMESPACE).addTextNode("?");
            }
        }

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader(SOAP_HEADER_ACTION, actionUri);

        soapMessage.saveChanges();
        return soapMessage;
    }
}
