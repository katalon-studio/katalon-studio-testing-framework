package com.kms.katalon.core.testobject;

import static com.kms.katalon.core.constants.StringConstants.ID_SEPARATOR;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.google.common.reflect.TypeToken;
import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.constants.StringConstants;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.main.ScriptEngine;
import com.kms.katalon.core.testobject.impl.HttpTextBodyContent;
import com.kms.katalon.core.testobject.internal.impl.HttpBodyContentReader;
import com.kms.katalon.core.testobject.internal.impl.WindowsObjectRepository;
import com.kms.katalon.core.util.StrSubstitutor;
import com.kms.katalon.core.util.internal.ExceptionsUtil;
import com.kms.katalon.core.util.internal.JsonUtil;

import groovy.lang.Binding;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

public class ObjectRepository {

    private static final KeywordLogger logger = KeywordLogger.getInstance(ObjectRepository.class);

    private static final String TEST_OBJECT_ROOT_FOLDER_NAME = "Object Repository";

    private static final String TEST_OBJECT_ID_PREFIX = TEST_OBJECT_ROOT_FOLDER_NAME + ID_SEPARATOR;

    private static final String WEB_SERVICES_TYPE_NAME = "WebServiceRequestEntity";

    private static final String WEB_ELEMENT_TYPE_NAME = "WebElementEntity";

    private static final String WEBELEMENT_FILE_EXTENSION = ".rs";

    private static final String WEB_ELEMENT_PROPERTY_NODE_NAME = "webElementProperties";
    
    private static final String WEB_ELEMENT_XPATH_NODE_NAME = "webElementXpaths";

    private static final String PROPERTY_NAME = "name";

    private static final String PROPERTY_CONDITION = "matchCondition";

    private static final String PROPERTY_VALUE = "value";

    private static final String PROPERTY_IS_SELECTED = "isSelected";

    private static final String[] PARENT_FRAME_ATTRS = new String[] { "ref_element", "parent_frame" };

    private static final String PARENT_SHADOW_ROOT_ATTRIBUTE = "ref_element_is_shadow_root";

    private static final String PROPERTY_SELECTOR_METHOD = "selectorMethod";

    private static final String PROPERTY_SELECTOR_COLLECTION = "selectorCollection";

    private static final String PROPERTY_ENTRY = "entry";

    private static final String PROPERTY_KEY = "key";
    
    private static Map<String, TestObject> recordedTestObjects;

    /**
     * Returns test object id of a its relative id.
     * 
     * @param testObjectRelativeId
     * Relative test object's id.
     * @returnString of test object id, <code>null</code> if <code>testObjectRelativeId</code> is null.
     */
    public static String getTestObjectId(final String testObjectRelativeId) {
        if (testObjectRelativeId == null) {
            return null;
        }

        if (testObjectRelativeId.startsWith(TEST_OBJECT_ID_PREFIX)) {
            return testObjectRelativeId;
        }
        return TEST_OBJECT_ID_PREFIX + testObjectRelativeId;
    }

    /**
     * Returns relative id of a test object's id. The relative id is cut <code>"Object Repository/"</code> prefix from
     * the
     * test object's id.
     * 
     * @param testObjectId
     * Full test object's id.
     * @return String of test object relative id, <code>null</code> if <code>testObjectId</code> is null.
     */
    public static String getTestObjectRelativeId(final String testObjectId) {
        if (testObjectId == null) {
            return null;
        }
        return testObjectId.replaceFirst(TEST_OBJECT_ID_PREFIX, StringUtils.EMPTY);
    }

    /**
     * Finds {@link TestObject} by its id or relative id
     * 
     * @param testObjectRelativeId
     * Can be test object full id or test object relative id
     * <p>
     * Eg: Using "Object Repository/Sample Test Object" (full id) OR "Sample Test Object" (relative id) as
     * <code>testObjectRelativeId</code> is accepted for the test object with id "Object Repository/Sample Test Object"
     * 
     * @return an instance of {@link TestObject} or <code>null</code> if the parameter is null or test object doesn't
     * exist
     * @see {@link #findTestObject(String, Map) findTestObject} for parameterizing test object
     */
    public static TestObject findTestObject(String testObjectRelativeId) {
        return findTestObject(testObjectRelativeId, new HashMap<String, Object>());
    }

    /**
     * Finds {@link TestObject} by its id or relative id using a variables map to parameterized its properties values
     * <p>
     * Object properties values are parameterized using the ${variable} syntax
     * <p>
     * For example: the test object has a xpath property with value ".//div[@class='class']//a[text()='${Variable}']"
     * <p>
     * If the test object is created using findTestObject(testObjectId, ['Variable': 'Text']) then the result xpath
     * property of the created test object will be ".//div[@class='class']//a[text()='Text']"
     * <p>
     * Use "$" to escape the "${" special characters, for example: "The variable $${${name}} must be used."
     * 
     * @param testObjectRelativeId
     * Can be test object full id or test object relative id
     * <p>
     * Eg: Using "Object Repository/Sample Test Object" (full id) OR "Sample Test Object" (relative id) as
     * <code>testObjectRelativeId</code> is accepted for the test object with id "Object Repository/Sample Test Object"
     * 
     * @param variables the variables map to parameterized the found test object
     * 
     * @return an instance of {@link TestObject} or <code>null</code> if test object id is null
     */
    public static TestObject findTestObject(String testObjectRelativeId, Map<String, Object> variables) {
        if (testObjectRelativeId == null) {
            logger.logWarning(StringConstants.TO_LOG_WARNING_TEST_OBJ_NULL);
            return null;
        }

        String testObjectId = getTestObjectId(testObjectRelativeId);
        logger.logDebug(MessageFormat.format(StringConstants.TO_LOG_INFO_FINDING_TEST_OBJ_W_ID, testObjectId));

        // Read test objects cached in temporary in record session.
        Map<String, TestObject> testObjectsCached = getCapturedTestObjects();

        if (testObjectRelativeId != null && testObjectsCached.containsKey(testObjectRelativeId)) {
            return testObjectsCached.get(testObjectRelativeId);
        }

        File objectFile = new File(RunConfiguration.getProjectDir(), testObjectId + WEBELEMENT_FILE_EXTENSION);
        if (!objectFile.exists()) {
            logger.logWarning(
                    MessageFormat.format(StringConstants.TO_LOG_WARNING_TEST_OBJ_DOES_NOT_EXIST, testObjectId));
            return null;
        }
        return readTestObjectFile(testObjectId, objectFile, RunConfiguration.getProjectDir(), variables);
    }
    
    public static WindowsTestObject findWindowsObject(final String windowsObjectId) {
        File objectFile = new File(RunConfiguration.getProjectDir(), windowsObjectId + ".wrs");
        return WindowsObjectRepository.readWindowsTestObjectFile(windowsObjectId, objectFile, RunConfiguration.getProjectDir(), Collections.emptyMap());
    }
    
    public static WindowsTestObject findWindowsObject(final String windowsObjectId, Map<String, Object> variables) {
        File objectFile = new File(RunConfiguration.getProjectDir(), windowsObjectId + ".wrs");
        return WindowsObjectRepository.readWindowsTestObjectFile(windowsObjectId, objectFile, RunConfiguration.getProjectDir(), variables);
    }

    private static Map<String, TestObject> getCapturedTestObjects() {
        if (recordedTestObjects != null) {
            return recordedTestObjects;
        }
        try {
            String capturedObjectCacheFilePath = StringUtils
                    .defaultString(RunConfiguration.getCapturedObjectsCacheFile());
            if (!capturedObjectCacheFilePath.isEmpty()) {
                File capturedObjectCacheFile = new File(capturedObjectCacheFilePath);
                recordedTestObjects = JsonUtil.fromJson(
                        FileUtils.readFileToString(capturedObjectCacheFile, StringConstants.DF_CHARSET),
                        new TypeToken<Map<String, TestObject>>() {}.getType());
            }
        } catch (IOException ignored) {
            recordedTestObjects = Collections.emptyMap();
        }
        return recordedTestObjects;
    }

    public static TestObject readTestObjectFile(String testObjectId, File objectFile, String projectDir) {
        return readTestObjectFile(testObjectId, objectFile, projectDir, Collections.emptyMap());
    }

    public static TestObject readTestObjectFile(String testObjectId, File objectFile, String projectDir,
            Map<String, Object> variables) {
        try {
            Element rootElement = new SAXReader().read(objectFile).getRootElement();
            String elementName = rootElement.getName();
            if (WEB_ELEMENT_TYPE_NAME.equals(elementName)) {
                return findWebUIObject(testObjectId, rootElement, variables);
            }

            if (WEB_SERVICES_TYPE_NAME.equals(elementName)) {
                return findRequestObject(testObjectId, rootElement, projectDir, variables);
            }
            return null;
        } catch (DocumentException e) {
            logger.logWarning(MessageFormat.format(StringConstants.TO_LOG_WARNING_CANNOT_GET_TEST_OBJECT_X_BECAUSE_OF_Y,
                    testObjectId, ExceptionsUtil.getMessageForThrowable(e)), null, e);
            return null;
        }
    }
  
    private static TestObject findWebUIObject(String testObjectId, Element element, Map<String, Object> variables) {
        TestObject testObject = new TestObject(testObjectId);

        // For image
        Element imagePathElement = element.element("imagePath");
        if (imagePathElement != null) {
            String imagePath = imagePathElement.getText();
            testObject.setImagePath(imagePath);
        }

        Element relativeImagePathElement = element.element("useRalativeImagePath");
        if (relativeImagePathElement != null) {
            String useRelavitePathString = relativeImagePathElement.getText();
            testObject.setUseRelativeImagePath(Boolean.parseBoolean(useRelavitePathString));
        }

        Element dfSelectorMethodElement = element.element(PROPERTY_SELECTOR_METHOD);
        if (dfSelectorMethodElement != null) {
            testObject.setSelectorMethod(SelectorMethod.valueOf(dfSelectorMethodElement.getText()));
        }

        Map<String, Object> variablesStringMap = new HashMap<String, Object>();
        for (Entry<String, Object> entry : variables.entrySet()) {
            variablesStringMap.put(String.valueOf(entry.getKey()), entry.getValue());
        }

        try {
            ScriptEngine scriptEngine = ScriptEngine.getDefault(ObjectRepository.class.getClassLoader());
            variablesStringMap.put("GlobalVariable", scriptEngine.runScriptWithoutLogging("internal.GlobalVariable", new Binding()));
        } catch (ClassNotFoundException | ResourceException | ScriptException | IOException e) {
        }
        
        StrSubstitutor strSubtitutor = new StrSubstitutor(variablesStringMap);
        
        Element propertySelectorCollection = element.element(PROPERTY_SELECTOR_COLLECTION);
        if (propertySelectorCollection != null) {
            List<?> selectorEntry = propertySelectorCollection.elements(PROPERTY_ENTRY);
            if (selectorEntry != null) {
                selectorEntry.forEach(entry -> {
                    Element selectorMethodElement = ((Element) entry);
                    SelectorMethod entryKey = SelectorMethod.valueOf(selectorMethodElement.elementText(PROPERTY_KEY));
                    String entryValue = strSubtitutor.replace(selectorMethodElement.elementText(PROPERTY_VALUE));
                    testObject.setSelectorValue(entryKey, entryValue);
                });
            }
        }

        for (Object propertyElementObject : element.elements(WEB_ELEMENT_PROPERTY_NODE_NAME)) {
            TestObjectProperty objectProperty = new TestObjectProperty();
            Element propertyElement = (Element) propertyElementObject;

            String propertyName = StringEscapeUtils.unescapeXml(propertyElement.elementText(PROPERTY_NAME));
            ConditionType propertyCondition = ConditionType
                    .fromValue(StringEscapeUtils.unescapeXml(propertyElement.elementText(PROPERTY_CONDITION)));
            String propertyValue = StringEscapeUtils.unescapeXml(propertyElement.elementText(PROPERTY_VALUE));
            boolean isPropertySelected = Boolean
                    .valueOf(StringEscapeUtils.unescapeXml(propertyElement.elementText(PROPERTY_IS_SELECTED)));

            objectProperty.setName(propertyName);
            objectProperty.setCondition(propertyCondition);
            objectProperty.setValue(strSubtitutor.replace(propertyValue));
            objectProperty.setActive(isPropertySelected);

            // Check if this element is inside a frame
            if (Arrays.asList(PARENT_FRAME_ATTRS).contains(propertyName) && isPropertySelected) {
                TestObject parentObject = findTestObject(propertyValue);
                testObject.setParentObject(parentObject);
            } else if (PARENT_SHADOW_ROOT_ATTRIBUTE.equals(propertyName)) {
                testObject.setParentObjectShadowRoot(true);
            } else {
                testObject.addProperty(objectProperty);
            }
        }
        
        for (Object xpathElementObject : element.elements(WEB_ELEMENT_XPATH_NODE_NAME)) {
            TestObjectXpath objectXpath = new TestObjectXpath();
            Element xpathElement = (Element) xpathElementObject;

            String propertyName = StringEscapeUtils.unescapeXml(xpathElement.elementText(PROPERTY_NAME));
            ConditionType propertyCondition = ConditionType
                    .fromValue(StringEscapeUtils.unescapeXml(xpathElement.elementText(PROPERTY_CONDITION)));
            String propertyValue = StringEscapeUtils.unescapeXml(xpathElement.elementText(PROPERTY_VALUE));
            boolean isPropertySelected = Boolean
                    .valueOf(StringEscapeUtils.unescapeXml(xpathElement.elementText(PROPERTY_IS_SELECTED)));

            objectXpath.setName(propertyName);
            objectXpath.setCondition(propertyCondition);
            objectXpath.setValue(strSubtitutor.replace(propertyValue));
            objectXpath.setActive(isPropertySelected);

            // Check if this element is inside a frame
            if (Arrays.asList(PARENT_FRAME_ATTRS).contains(propertyName) && isPropertySelected) {
                TestObject parentObject = findTestObject(propertyValue);
                testObject.setParentObject(parentObject);
            } else if (PARENT_SHADOW_ROOT_ATTRIBUTE.equals(propertyName)) {
                testObject.setParentObjectShadowRoot(true);
            } else {
                testObject.addXpath(objectXpath);
            }
        }

        return testObject;
    }

    @SuppressWarnings("unchecked")
    private static RequestObject findRequestObject(String requestObjectId, Element reqElement, String projectDir,
            Map<String, Object> variables) {
        RequestObject requestObject = new RequestObject(requestObjectId);
        requestObject.setName(reqElement.elementText("name"));

        String serviceType = reqElement.elementText("serviceType");
        requestObject.setServiceType(serviceType);
        
        Map<String, Object> defaultVariables = new HashMap<>();
        // Use default value of variables if available in case user passes nothing or null
        
        List<Element> variableElements = reqElement.elements("variables");
        if (variableElements != null && variableElements.size() > 0) {
            Map<String, String> rawVariables = new HashMap<>();
            for (Element variableElement : variableElements) {
                if (variableElement != null) {
                    Element defaultValue = variableElement.element("defaultValue");
                    Element name = variableElement.element("name");

                    if (!defaultValue.equals(StringUtils.EMPTY)) {
                        rawVariables.put(name.getData().toString(), defaultValue.getData().toString());
                    }
                }
            }
            boolean exception = false;
            try {
                defaultVariables = evaluateVariables(rawVariables);
            } catch (Exception e) {
                exception = true;
            } finally {
                if (exception == true) {
                    defaultVariables = new HashMap<>();
                }
            }
        }

        Map<String, Object> mergedVariables = new HashMap<>();
        mergedVariables.putAll(defaultVariables);

        if (variables != null && variables.size() > 0) {
            mergedVariables.putAll(variables);
        }
            
        try {
            ScriptEngine scriptEngine = ScriptEngine.getDefault(ObjectRepository.class.getClassLoader());
            mergedVariables.put("GlobalVariable", scriptEngine.runScriptWithoutLogging("internal.GlobalVariable", new Binding()));
        } catch (ClassNotFoundException | ResourceException | ScriptException | IOException e) {
        }

        
        StrSubstitutor substitutor = new StrSubstitutor(mergedVariables);
        if ("SOAP".equals(serviceType)) {
            requestObject.setWsdlAddress(substitutor.replace(reqElement.elementText("wsdlAddress")));
            requestObject.setSoapRequestMethod(reqElement.elementText("soapRequestMethod"));
            requestObject.setSoapServiceFunction(reqElement.elementText("soapServiceFunction"));
            requestObject.setHttpHeaderProperties(parseProperties(reqElement.elements("httpHeaderProperties"), substitutor));
            requestObject.setSoapBody(substitutor.replace(reqElement.elementText("soapBody")));
        } else if ("RESTful".equals(serviceType)) {
            requestObject.setRestUrl(substitutor.replace(reqElement.elementText("restUrl")));
            String requestMethod = reqElement.elementText("restRequestMethod");
            requestObject.setRestRequestMethod(requestMethod);
            requestObject.setRestParameters(parseProperties(reqElement.elements("restParameters")));
            requestObject
                    .setHttpHeaderProperties(parseProperties(reqElement.elements("httpHeaderProperties"), substitutor));
            requestObject.setHttpBody(reqElement.elementText("httpBody"));

            String httpBodyType = reqElement.elementText("httpBodyType");
            if (StringUtils.isBlank(httpBodyType)) {
                // migrated from 5.3.1 (KAT-3200)
                httpBodyType = "text";
                String body = reqElement.elementText("httpBody");
                HttpTextBodyContent httpBodyContent = new HttpTextBodyContent(body);
                requestObject.setBodyContent(httpBodyContent);
            } else if (isBodySupported(requestObject)) {
                String httpBodyContent = reqElement.elementText("httpBodyContent");
                HttpBodyContent bodyContent = HttpBodyContentReader.fromSource(httpBodyType, httpBodyContent,
                        projectDir, substitutor);
                requestObject.setBodyContent(bodyContent);
                
                //Backward compatible with 5.3.1
//                ByteArrayOutputStream outstream = new ByteArrayOutputStream();
//                try {
//                    bodyContent.writeTo(outstream);
//                    requestObject.setHttpBody(outstream.toString());
//                } catch (IOException ignored) {
//                }
            }
        }
        
        requestObject.setVariables(mergedVariables);

        String verificationScript = reqElement.elementText("verificationScript");
        requestObject.setVerificationScript(verificationScript);
        
        boolean followRedirects = Boolean.valueOf(reqElement.elementText("followRedirects"));
        requestObject.setFollowRedirects(followRedirects);
        
        return requestObject;
    }
    
    @SuppressWarnings("unchecked")
    public static RequestObject findRequestObject(String requestObjectId, File objectFile) {
        try {
            Element reqElement = new SAXReader().read(objectFile).getRootElement();
            
            List<Object> variableElements = reqElement.elements("variables");
            Map<String, Object> variables = Collections.emptyMap();
            if (variableElements != null) {
                Map<String, String> rawVariables = parseRequestObjectVariables(variableElements);
                variables = evaluateVariables(rawVariables);
            }
            
            return findRequestObject(requestObjectId, reqElement, RunConfiguration.getProjectDir(), variables);
        } catch (Exception e) {
            logger.logWarning(MessageFormat.format(StringConstants.TO_LOG_WARNING_CANNOT_GET_TEST_OBJECT_X_BECAUSE_OF_Y,
                    requestObjectId, ExceptionsUtil.getMessageForThrowable(e)), null, e);
            return null;
        }
    }

    private static Map<String, String> parseRequestObjectVariables(List<Object> elements) {
        Map<String, String> variableMap = elements.stream()
            .collect(Collectors.toMap(element -> ((Element) element).elementText("name"),
                   element -> ((Element) element).elementText("defaultValue")));
        return variableMap;
    }
    
    private static Map<String, Object> evaluateVariables(Map<String, String> rawVariables) 
            throws IOException, ClassNotFoundException, ResourceException, ScriptException {
        ScriptEngine scriptEngine = ScriptEngine.getDefault(ObjectRepository.class.getClassLoader());
        Map<String, Object> evaluatedVariables = new HashMap<>();
        for (Map.Entry<String, String> variableEntry : rawVariables.entrySet()) {
            String variableName = variableEntry.getKey();
            String variableValue = variableEntry.getValue();
            Object evaluatedValue = scriptEngine.runScriptWithoutLogging(variableValue, new Binding());
            evaluatedVariables.put(variableName, evaluatedValue);
        }       
        return evaluatedVariables;
    }
    
    private static boolean isBodySupported(RequestObject requestObject) {
        String restRequestMethod = requestObject.getRestRequestMethod();
        return !("GET".contains(restRequestMethod));
    }

    private static List<TestObjectProperty> parseProperties(List<Object> objects) {
        return parseProperties(objects, new StrSubstitutor());
    }

    private static List<TestObjectProperty> parseProperties(List<Object> objects, StrSubstitutor substitutor) {
        List<TestObjectProperty> props = new ArrayList<TestObjectProperty>();
        for (Object propertyElementObject : objects) {
            TestObjectProperty objectProperty = new TestObjectProperty();
            Element propertyElement = (Element) propertyElementObject;

            String propertyName = propertyElement.elementText(PROPERTY_NAME);
            ConditionType propertyCondition = ConditionType.fromValue(propertyElement.elementText(PROPERTY_CONDITION));
            String propertyValue = propertyElement.elementText(PROPERTY_VALUE);
            boolean isPropertySelected = Boolean.valueOf(propertyElement.elementText(PROPERTY_IS_SELECTED));

            objectProperty.setName(substitutor.replace(propertyName));
            objectProperty.setCondition(propertyCondition);
            objectProperty.setValue(substitutor.replace(propertyValue));
            objectProperty.setActive(isPropertySelected);

            props.add(objectProperty);
        }
        return props;
    }
}
