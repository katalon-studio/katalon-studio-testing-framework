package com.kms.katalon.core.webui.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebElement;

import com.kms.katalon.core.testobject.SelectorMethod;
import com.kms.katalon.core.testobject.TestObject;

public class FindElementsResult {

    private List<WebElement> elements;

    private String locator;

    private SelectorMethod locatorMethod;

    private String screenshot;

    public FindElementsResult() {
        //
    }

    public FindElementsResult(List<WebElement> elements, String locator, SelectorMethod locatorMethod, String screenshot) {
        this.locatorMethod = locatorMethod;
        this.locator = locator;
        this.elements = elements;
        this.screenshot = screenshot;
    }
    
    public FindElementsResult(WebElement element, String locator, SelectorMethod locatorMethod, String screenshot) {
        this(new ArrayList<WebElement>(), locator, locatorMethod, screenshot);
        if (element != null) {
            elements.add(element);
        }
    }

    public static FindElementsResult from(List<WebElement> elements, String locator, SelectorMethod locatorMethod,
            String screenshot) {
        return new FindElementsResult(elements, locator, locatorMethod, screenshot);
    }

    public static FindElementsResult from(List<WebElement> elements, String locator, SelectorMethod locatorMethod) {
        return new FindElementsResult(elements, locator, locatorMethod, StringUtils.EMPTY);
    }

    public static FindElementsResult from(WebElement element, String locator, SelectorMethod locatorMethod,
            String screenshot) {
        return new FindElementsResult(element, locator, locatorMethod, screenshot);
    }

    public static FindElementsResult from(String locator, SelectorMethod locatorMethod, String screenshot) {
        return from(Collections.emptyList(), locator, locatorMethod, screenshot);
    }

    public static FindElementsResult from(String locator, SelectorMethod locatorMethod) {
        return from(locator, locatorMethod, StringUtils.EMPTY);
    }

    public static FindElementsResult from(SelectorMethod locatorMethod) {
        return from(StringUtils.EMPTY, locatorMethod, StringUtils.EMPTY);
    }

    public static FindElementsResult from(List<WebElement> elements, TestObject healedTestObject) {
        SelectorMethod method = healedTestObject.getSelectorMethod();
        String locator = healedTestObject.getSelectorCollection().get(method);
        return from(elements, locator, method);
    }

    public SelectorMethod getLocatorMethod() {
        return locatorMethod;
    }

    public void setLocatorMethod(SelectorMethod locatorMethod) {
        this.locatorMethod = locatorMethod;
    }

    public String getLocator() {
        return locator;
    }

    public void setLocator(String locator) {
        this.locator = locator;
    }

    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
    }

    public List<WebElement> getElements() {
        return elements;
    }

    public void setElements(List<WebElement> elements) {
        this.elements = elements;
    }

    public WebElement getElement() {
        return elements != null && elements.size() > 0 ? elements.get(0) : null;
    }

    public void setElement(WebElement element) {
        if (elements == null) {
            elements = new ArrayList<WebElement>();
        } else {
            elements.clear();
        }
        elements.add(element);
    }

    public boolean isEmpty() {
        return elements == null || elements.isEmpty();
    }
}
