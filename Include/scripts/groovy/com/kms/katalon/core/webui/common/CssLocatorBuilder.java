package com.kms.katalon.core.webui.common;

import org.apache.commons.lang3.StringUtils;

import com.kms.katalon.core.testobject.ConditionType;
import com.kms.katalon.core.testobject.TestObject;
import com.kms.katalon.core.testobject.TestObjectProperty;

public class CssLocatorBuilder {
    private static final String CSS_LOCATOR_PROPERTY_NAME = WebUiCommonHelper.CSS_LOCATOR_PROPERTY_NAME;

    public static String buildCssSelectorLocator(TestObject to) {
        String cssLocatorValue = WebUiCommonHelper.findActiveEqualsObjectProperty(to, CSS_LOCATOR_PROPERTY_NAME);
        if (cssLocatorValue != null) {
            return cssLocatorValue;
        }
        StringBuilder cssBuilder = new StringBuilder();
        cssBuilder.append(buildTagSection(to));
        for (TestObjectProperty property : to.getActiveProperties()) {
            final String propertyName = property.getName();
            final String value = property.getValue();
            final ConditionType conditionType = property.getCondition();
            if (WebUiCommonHelper.WEB_ELEMENT_TAG.equals(propertyName)
                    || WebUiCommonHelper.WEB_ELEMENT_XPATH.equals(propertyName)
                    || CSS_LOCATOR_PROPERTY_NAME.equals(propertyName) || StringUtils.isEmpty(value)
                    || conditionType == ConditionType.MATCHES_REGEX || conditionType == ConditionType.NOT_MATCH_REGEX) {
                continue;
            }
            cssBuilder.append(getPrefix(property));
            cssBuilder.append("[" + propertyName + getOperator(property) + "\"" + value + "\"]");
            cssBuilder.append(getSuffix(property));
        }
        final String result = cssBuilder.toString();
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        return result;
    }

    private static String buildTagSection(TestObject to) {
        TestObjectProperty tagProperty = findActiveObjectProperty(to, WebUiCommonHelper.WEB_ELEMENT_TAG);
        if (tagProperty == null || StringUtils.isEmpty(tagProperty.getValue())
                || !(tagProperty.getCondition() == ConditionType.EQUALS
                        || tagProperty.getCondition() == ConditionType.NOT_EQUAL)) {
            return "*";
        }
        StringBuilder locatorString = new StringBuilder();
        locatorString.append(getPrefix(tagProperty));
        locatorString.append(tagProperty.getValue());
        locatorString.append(getSuffix(tagProperty));
        return locatorString.toString();
    }

    private static TestObjectProperty findActiveObjectProperty(TestObject to, String propertyName) {
        for (TestObjectProperty property : to.getActiveProperties()) {
            if (property.getName().equals(propertyName)) {
                return property;
            }
        }
        return null;
    }

    private static String getOperator(TestObjectProperty property) {
        switch (property.getCondition()) {
            case CONTAINS:
            case NOT_CONTAIN:
                return "~=";
            case ENDS_WITH:
                return "$=";
            case EQUALS:
            case NOT_EQUAL:
                return "=";
            case STARTS_WITH:
                return "^=";
            default:
                return "=";

        }
    }

    private static String getSuffix(TestObjectProperty property) {
        switch (property.getCondition()) {
            case NOT_CONTAIN:
            case NOT_EQUAL:
                return ")";
            default:
                return "";

        }
    }

    private static String getPrefix(TestObjectProperty property) {
        switch (property.getCondition()) {
            case NOT_CONTAIN:
            case NOT_EQUAL:
                return ":not(";
            default:
                return "";

        }
    }
}
