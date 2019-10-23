package com.kms.katalon.core.testobject.internal.impl;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringEscapeUtils;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.kms.katalon.core.constants.StringConstants;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.testobject.TestObjectProperty;
import com.kms.katalon.core.testobject.WindowsTestObject;
import com.kms.katalon.core.util.StrSubstitutor;
import com.kms.katalon.core.util.internal.ExceptionsUtil;

public class WindowsObjectRepository {

    private static KeywordLogger logger = KeywordLogger.getInstance(WindowsObjectRepository.class);

    public static WindowsTestObject readWindowsTestObjectFile(String testObjectId, File objectFile, String projectDir,
            Map<String, Object> variables) {
        try {
            WindowsTestObject testObject = new WindowsTestObject(testObjectId);

            Element element = new SAXReader().read(objectFile).getRootElement();

            String locator = element.elementText("locator");
            testObject.setLocator(locator);

            WindowsTestObject.LocatorStrategy locatorStrategy = WindowsTestObject.LocatorStrategy
                    .valueOf(element.elementText("locatorStrategy"));
            testObject.setLocatorStrategy(locatorStrategy);

            List<TestObjectProperty> properties = new ArrayList<>();
            for (Object propertyElementObject : element.elements("properties")) {
                TestObjectProperty objectProperty = new TestObjectProperty();
                Element propertyElement = (Element) propertyElementObject;

                String propertyName = StringEscapeUtils.unescapeXml(propertyElement.elementText("name"));
                String propertyValue = StringEscapeUtils.unescapeXml(propertyElement.elementText("value"));

                objectProperty.setName(propertyName);
                objectProperty.setValue(propertyValue);
            }

            if (!variables.isEmpty()) {
                Map<String, Object> variablesStringMap = new HashMap<String, Object>();
                for (Entry<String, Object> entry : variables.entrySet()) {
                    variablesStringMap.put(String.valueOf(entry.getKey()), entry.getValue());
                }

                StrSubstitutor strSubtitutor = new StrSubstitutor(variablesStringMap);
                for (TestObjectProperty objectProperty : properties) {
                    objectProperty.setValue(strSubtitutor.replace(objectProperty.getValue()));
                }
            }

            testObject.setProperties(properties);

            return testObject;
        } catch (DocumentException e) {
            logger.logWarning(MessageFormat.format(StringConstants.TO_LOG_WARNING_CANNOT_GET_TEST_OBJECT_X_BECAUSE_OF_Y,
                    testObjectId, ExceptionsUtil.getMessageForThrowable(e)), null, e);
            return null;
        }
    }
}
