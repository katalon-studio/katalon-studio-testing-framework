package com.kms.katalon.core.testobject;

import java.util.List;

public class WindowsTestObject implements ITestObject {

    private String objectId;
    
    private List<TestObjectProperty> properties;
    
    private String locator;

    private LocatorStrategy locatorStrategy;

    public WindowsTestObject(final String objectId) {
        this.objectId = objectId;
    }

    @Override
    public String getObjectId() {
        return objectId;
    }

    public List<TestObjectProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<TestObjectProperty> properties) {
        this.properties = properties;
    }

    public String getLocator() {
        return locator;
    }

    public void setLocator(String locator) {
        this.locator = locator;
    }

    public LocatorStrategy getLocatorStrategy() {
        return locatorStrategy;
    }

    public void setLocatorStrategy(LocatorStrategy locatorStrategy) {
        this.locatorStrategy = locatorStrategy;
    }

    public static enum LocatorStrategy {
        ACCESSIBILITY_ID("Accessibility ID"),
        CLASS_NAME("Class Name"),
        ID("ID"),
        NAME("Name"),
        TAG_NAME("Tag Name"),
        XPATH("XPATH");

        private final String locatorStrategy;

        private LocatorStrategy(String locatorStrategy) {
            this.locatorStrategy = locatorStrategy;
        }

        public String getLocatorStrategy() {
            return locatorStrategy;
        }
    }
}
