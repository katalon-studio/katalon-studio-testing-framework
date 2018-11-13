package com.kms.katalon.core.mobile.keyword.internal;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;

import com.kms.katalon.core.testobject.ConditionType;
import com.kms.katalon.core.testobject.TestObject;
import com.kms.katalon.core.testobject.TestObjectProperty;

/**
 * @author duvo
 *
 */
@SuppressWarnings("rawtypes")
public class MobileSearchEngine {

	private AppiumDriver driver;
    private TestObject element;

    public MobileSearchEngine(AppiumDriver driver, TestObject element) {
        this.driver = driver;
        this.element = element;
    }

    @SuppressWarnings("unchecked")
	private List<WebElement> findAndroidElements(AndroidDriver driver) {
        String xpath = null;
        TestObjectProperty xpathProp = element.findProperty(GUIObject.XPATH);
        if(xpathProp != null && xpathProp.isActive()){
        	xpath = xpathProp.getValue();
        }
        if (xpath == null) {
            TestObjectProperty uiAutomatorProp = element.findProperty(AndroidUIAutomator.PROPERTY_NAME);
            if(uiAutomatorProp != null && uiAutomatorProp.isActive()){
                WebElement foundElement = driver.findElementByAndroidUIAutomator(uiAutomatorProp.getValue());
                if (foundElement != null) {
                    List<WebElement> elementList = new ArrayList<WebElement>();
                    elementList.add(foundElement);
                    return elementList;
                } else {
                    return null;
                }
            } else {
                StringBuilder selector = new StringBuilder(AndroidUIAutomator.SELECTOR);
                TestObjectProperty property = element.findProperty(AndroidProperties.ANDROID_CLASS);
                if (property != null && property.isActive()) {
                    if (property.getCondition().equals(ConditionType.EXPRESSION)) {
                        selector.append(String.format(AndroidUIAutomator.BY_CLASS_NAME_MATCH, property.getValue()));
                    } else {
                        selector.append(String.format(AndroidUIAutomator.BY_CLASS_NAME, property.getValue()));
                    }
                }

                property = element.findProperty(AndroidProperties.ANDROID_CONTENT_DESC);
                if (property != null && property.isActive()) {
                    if (property.getCondition().equals(ConditionType.CONTAINS)) {
                        selector.append(String.format(AndroidUIAutomator.BY_CONTENT_DESC_CONTAIN, property.getValue()));
                    } else if (property.getCondition().equals(ConditionType.EXPRESSION)) {
                        selector.append(String.format(AndroidUIAutomator.BY_CONTENT_DESC_MATCH, property.getValue()));
                    } else if (property.getCondition().equals(ConditionType.STARTS_WITH)) {
                        selector.append(String.format(AndroidUIAutomator.BY_CONTENT_DESC_START_WITH, property.getValue()));
                    } else {
                        selector.append(String.format(AndroidUIAutomator.BY_CONTENT_DESC, property.getValue()));
                    }
                }

                property = element.findProperty(AndroidProperties.ANDROID_TEXT);
                if (property != null && property.isActive()) {
                    String propValText = property.getValue().replace("\\n", "\n");
                    if (property.getCondition().equals(ConditionType.CONTAINS)) {
                        selector.append(String.format(AndroidUIAutomator.BY_TEXT_CONTAIN, propValText));
                    } else if (property.getCondition().equals(ConditionType.EXPRESSION)) {
                        selector.append(String.format(AndroidUIAutomator.BY_TEXT_MATCH, propValText));
                    } else if (property.getCondition().equals(ConditionType.STARTS_WITH)) {
                        selector.append(String.format(AndroidUIAutomator.BY_TEXT_START_WITH, propValText));
                    } else {
                        selector.append(String.format(AndroidUIAutomator.BY_TEXT, propValText));
                    }
                }

                property = element.findProperty(AndroidProperties.ANDROID_RESOURCE_ID);
                if (property != null && property.isActive()) {
                    if (property.getCondition().equals(ConditionType.EXPRESSION)) {
                        selector.append(String.format(AndroidUIAutomator.BY_RESOURCE_ID_MATCH, property.getValue()));
                    } else {
                        selector.append(String.format(AndroidUIAutomator.BY_RESOURCE_ID, property.getValue()));
                    }
                }

                property = element.findProperty(AndroidProperties.ANDROID_PACKAGE);
                if (property != null && property.isActive()) {
                    if (property.getCondition().equals(ConditionType.EXPRESSION)) {
                        selector.append(String.format(AndroidUIAutomator.BY_PACKAGE_MATCH, property.getValue()));
                    } else {
                        selector.append(String.format(AndroidUIAutomator.BY_PACKAGE, property.getValue()));
                    }
                }

                property = element.findProperty(AndroidProperties.ANDROID_ENABLED);
                if (property != null && property.isActive()) {
                    selector.append(String.format(AndroidUIAutomator.BY_ENABLED, property.getValue()));
                }

                property = element.findProperty(AndroidProperties.ANDROID_CLICKABLE);
                if (property != null && property.isActive()) {
                    selector.append(String.format(AndroidUIAutomator.BY_CLICKABLE, property.getValue()));
                }

                property = element.findProperty(AndroidProperties.ANDROID_LONG_CLICKABLE);
                if (property != null && property.isActive()) {
                    selector.append(String.format(AndroidUIAutomator.BY_LONG_CLICKABLE, property.getValue()));
                }

                property = element.findProperty(AndroidProperties.ANDROID_CHECKABLE);
                if (property != null && property.isActive()) {
                    selector.append(String.format(AndroidUIAutomator.BY_CHECKABLE, property.getValue()));
                }

                property = element.findProperty(AndroidProperties.ANDROID_CHECKED);
                if (property != null && property.isActive()) {
                    selector.append(String.format(AndroidUIAutomator.BY_CHECKED, property.getValue()));
                }

                property = element.findProperty(AndroidProperties.ANDROID_FOCUSABLE);
                if (property != null && property.isActive()) {
                    selector.append(String.format(AndroidUIAutomator.BY_FOCUSABLE, property.getValue()));
                }

                property = element.findProperty(AndroidProperties.ANDROID_FOCUSED);
                if (property != null && property.isActive()) {
                    selector.append(String.format(AndroidUIAutomator.BY_FOCUSED, property.getValue()));
                }

                property = element.findProperty(AndroidProperties.ANDROID_SCROLLABLE);
                if (property != null && property.isActive()) {
                    selector.append(String.format(AndroidUIAutomator.BY_SCROLLABLE, property.getValue()));
                }

                property = element.findProperty(AndroidProperties.ANDROID_SELECTED);
                if (property != null && property.isActive()) {
                    selector.append(String.format(AndroidUIAutomator.SELECTOR, property.getValue()));
                }

                property = element.findProperty(AndroidProperties.ANDROID_INSTANCE);
                if (property != null && property.isActive()) {
                    selector.append(String.format(AndroidUIAutomator.BY_INSTANCE, property.getValue()));
                }
                return driver.findElementsByAndroidUIAutomator(selector.toString());
            }
        } else {
            List<TestObjectProperty> specialProperties = SelectorBuilderHelper.escapeSpecialProperties(element.getProperties());
            List<WebElement> elements = driver.findElementsByXPath(SelectorBuilderHelper.buildXpathFromProperties(element.getProperties()));
            if (!specialProperties.isEmpty()) {
                for (int i = elements.size() - 1; i >= 0; i--) {
                    WebElement foundElement = elements.get(i);
                    if (!isMatchedAndroidElement(foundElement, specialProperties)) {
                        elements.remove(i);
                    }
                }
            }

            return elements;
        }
    }

    private boolean isMatchedAndroidElement(WebElement actualElement, List<TestObjectProperty> expectedProps) {
        String actualValue = null;
        String expectedValue = null;
        boolean isMatched = true;
        for (TestObjectProperty property : expectedProps) {
            switch (property.getName()) {
            case AndroidProperties.ANDROID_CHECKABLE:
                actualValue = actualElement.getAttribute(AndroidProperties.ANDROID_CHECKABLE);
                break;

            case AndroidProperties.ANDROID_CHECKED:
                actualValue = actualElement.getAttribute(AndroidProperties.ANDROID_CHECKED);
                break;

            case AndroidProperties.ANDROID_CLASS:
                actualValue = actualElement.getTagName();
                break;

            case AndroidProperties.ANDROID_CLICKABLE:
                actualValue = actualElement.getAttribute(AndroidProperties.ANDROID_CLICKABLE);
                break;

            case AndroidProperties.ANDROID_CONTENT_DESC:
                actualValue = actualElement.getAttribute(AndroidProperties.ANDROID_CONTENT_DESC);
                break;

            case AndroidProperties.ANDROID_ENABLED:
                actualValue = String.valueOf(actualElement.isEnabled());
                break;

            case AndroidProperties.ANDROID_FOCUSABLE:
                actualValue = actualElement.getAttribute(AndroidProperties.ANDROID_FOCUSABLE);
                break;

            case AndroidProperties.ANDROID_FOCUSED:
                actualValue = actualElement.getAttribute(AndroidProperties.ANDROID_FOCUSED);
                break;

            case AndroidProperties.ANDROID_INSTANCE:
                actualValue = actualElement.getAttribute(AndroidProperties.ANDROID_INSTANCE);
                break;

            case AndroidProperties.ANDROID_LONG_CLICKABLE:
                actualValue = actualElement.getAttribute(AndroidProperties.ANDROID_LONG_CLICKABLE);
                break;

            case AndroidProperties.ANDROID_PACKAGE:
                actualValue = actualElement.getAttribute(AndroidProperties.ANDROID_PACKAGE);
                break;

            case AndroidProperties.ANDROID_PASSWORD:
                actualValue = actualElement.getAttribute(AndroidProperties.ANDROID_PASSWORD);
                break;

            case AndroidProperties.ANDROID_RESOURCE_ID:
                actualValue = actualElement.getAttribute(AndroidProperties.ANDROID_RESOURCE_ID);
                break;

            case AndroidProperties.ANDROID_SCROLLABLE:
                actualValue = actualElement.getAttribute(AndroidProperties.ANDROID_SCROLLABLE);
                break;

            case AndroidProperties.ANDROID_SELECTED:
                actualValue = String.valueOf(actualElement.isSelected());
                break;

            case AndroidProperties.ANDROID_TEXT:
                actualValue = actualElement.getText();
                break;

            default:
                continue;
            }
            expectedValue = property.getValue();

            isMatched &= isMatchedValue(actualValue, expectedValue, property.getCondition());
        }
        return isMatched;
    }

    public WebElement findWebElement(boolean addLogEntries) throws Exception {
        if (driver instanceof AndroidDriver) {
            List<WebElement> elements = findAndroidElements((AndroidDriver) driver);
            if ((elements == null) || elements.isEmpty()) {
                return null;
            }
            return elements.get(0);
        } else {
            List<WebElement> elements = findIosElements(driver);
            if ((elements == null) || elements.isEmpty()) {
                return null;
            }
            return elements.get(0);
        }
    }

    @SuppressWarnings("unchecked")
	private List<WebElement> findIosElements(AppiumDriver driver) {
        String xpath = null;
        TestObjectProperty xpathProp = element.findProperty(GUIObject.XPATH);
        if(xpathProp != null && xpathProp.isActive()){
        	xpath = xpathProp.getValue();
        }
        TestObjectProperty typeProp = element.findProperty(IOSProperties.IOS_TYPE);
        String type = null;
        if(typeProp != null && typeProp.isActive()){
        	type = typeProp.getValue();
        }
        if ((xpath == null) && (type == null)) {
            List<WebElement> elements = null;
            String selectorValue = null;
            TestObjectProperty selectorProp = element.findProperty(IOSProperties.IOS_NAME);
            if(selectorProp != null && selectorProp.isActive()){
            	selectorValue = selectorProp.getValue();
            }
            if (selectorValue != null) {
                elements = driver.findElementsByName(selectorValue);
            }
            if ((elements != null) && !elements.isEmpty()) {
                for (int i = elements.size() - 1; i >= 0; i--) {
                    WebElement foundElement = elements.get(i);
                    //if (!isMatchedIosElement(foundElement, element.getProperties())) {
                    if (!isMatchedIosElement(foundElement, element.getActiveProperties())) {
                    	elements.remove(i);
                    }
                }
            }

            return elements;
        } else {
            //List<TestObjectProperty> specialProperties = SelectorBuilderHelper.escapeSpecialProperties(element.getProperties());
        	//List<WebElement> elements = driver.findElementsByXPath(SelectorBuilderHelper.buildXpathFromProperties(element.getProperties()));
        	List<TestObjectProperty> specialProperties = SelectorBuilderHelper.escapeSpecialProperties(element.getActiveProperties());
        	List<WebElement> elements = driver.findElementsByXPath(SelectorBuilderHelper.buildXpathFromProperties(element.getActiveProperties()));
            if (!specialProperties.isEmpty()) {
                for (int i = elements.size() - 1; i >= 0; i--) {
                    WebElement foundElement = elements.get(i);
                    if (!isMatchedIosElement(foundElement, specialProperties)) {
                        elements.remove(i);
                    }
                }
            }

            return elements;
        }
    }

    private boolean isMatchedIosElement(WebElement actualElement, List<TestObjectProperty> expectedProps) {
        String actualValue = null;
        String expectedValue = null;
        boolean isMatched = true;
        for (TestObjectProperty property : expectedProps) {
            switch (property.getName()) {
            case IOSProperties.IOS_TYPE:
                actualValue = actualElement.getTagName();
                break;

            case IOSProperties.IOS_ENABLED:
                actualValue = String.valueOf(actualElement.isEnabled());
                break;

            case IOSProperties.IOS_HINT:
                actualValue = actualElement.getAttribute(IOSProperties.IOS_HINT);
                break;

            case IOSProperties.IOS_LABEL:
                actualValue = actualElement.getAttribute(IOSProperties.IOS_LABEL);
                break;

            case IOSProperties.IOS_NAME:
                actualValue = actualElement.getAttribute(IOSProperties.IOS_NAME);
                break;

            case IOSProperties.IOS_VISIBLE:
                actualValue = String.valueOf(actualElement.isDisplayed());
                break;

            case IOSProperties.IOS_VALUE:
                actualValue = actualElement.getAttribute(IOSProperties.IOS_VALUE);
                break;

            default:
                continue;
            }
            expectedValue = property.getValue();

            isMatched &= isMatchedValue(actualValue, expectedValue, property.getCondition());
        }
        return isMatched;
    }

    private boolean isMatchedValue(String actualValue, String expectedValue, ConditionType operatorType) {
        switch (operatorType) {
        case CONTAINS:
            return actualValue.contains(expectedValue);

        case ENDS_WITH:
            return actualValue.endsWith(expectedValue);

        case EQUALS:
            return actualValue.equals(expectedValue);

        case EXPRESSION:
            return actualValue.matches(expectedValue);

        case NOT_CONTAIN:
            return !actualValue.contains(expectedValue);

        case STARTS_WITH:
            return actualValue.startsWith(expectedValue);

        default:
            return false;
        }
    }

    public List<WebElement> findWebElements(boolean addLogEntries) throws Exception {
        if (driver instanceof AndroidDriver) {
            return findAndroidElements((AndroidDriver) driver);
        } else {
            return findIosElements(driver);
        }
    }
}
