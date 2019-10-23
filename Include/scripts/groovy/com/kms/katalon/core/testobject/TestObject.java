package com.kms.katalon.core.testobject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebElement;

public class TestObject implements SelectorCollector, ITestObject {

    private TestObject parentObject; // Typically is parent Frame

    private boolean isParentObjectShadowRoot;

    private List<TestObjectProperty> properties;

    private List<TestObjectXpath> xpaths;
    
    private String objectId;

    private String imagePath;

    private boolean useRelativeImagePath;

    private SelectorMethod selectorMethod = SelectorMethod.BASIC;

    private Map<SelectorMethod, String> selectorCollection;

    private WebElement cachedWebElement;

    public TestObject(String objectId) {
        this.properties = new ArrayList<TestObjectProperty>();
        this.xpaths = new ArrayList<TestObjectXpath>();
        this.selectorCollection = new HashMap<>();
        this.objectId = objectId;
    }

    public TestObject() {
        this(StringUtils.EMPTY);
    }

    /**
     * Get all properties of the test object
     * 
     * @return a list contains all properties of the test object
     */
    public List<TestObjectProperty> getProperties() {
        return properties;
    }

    /**
     * Get all active properties of the test object
     * 
     * @return a list contains all active properties of the test object
     */
    public List<TestObjectProperty> getActiveProperties() {
        List<TestObjectProperty> activeProperties = new ArrayList<TestObjectProperty>();
        for (TestObjectProperty property : properties) {
            if (property.isActive()) {
                activeProperties.add(property);
            }
        }
        return activeProperties;
    }

    /**
     * Set the properties of the test object
     * 
     * @param properties a list of properties to set to the test object
     */
    public void setProperties(List<TestObjectProperty> properties) {
        this.properties = properties;
    }

    /**
     * Add a new property to the test object
     * 
     * @param property the new {@link TestObjectProperty} to add
     * @return this test object after adding the new property
     */
    public TestObject addProperty(TestObjectProperty property) {
        this.properties.add(property);
        return this;
    }

    /**
     * Add a new property to the test object
     * 
     * @param name the name of the new property
     * @param condition the {@link ConditionType} of the new property
     * @param value the value of the new property
     * @return this test object after adding the new property
     */
    public TestObject addProperty(String name, ConditionType condition, String value) {
        this.properties.add(new TestObjectProperty(name, condition, value));
        return this;
    }

    /**
     * Add a new property to the test object
     * 
     * @param name the name of the new property
     * @param condition the {@link ConditionType} of the new property
     * @param value the value of the new property
     * @param isActive the active flag of the new property
     * @return this test object after adding the new property
     */
    public TestObject addProperty(String name, ConditionType condition, String value, boolean isActive) {
        this.properties.add(new TestObjectProperty(name, condition, value, isActive));
        return this;
    }

    /**
     * Find the value of a property using the property name
     * 
     * @param name the name of the property to find
     * @return the value of a property, or blank if not found
     */
    public String findPropertyValue(String name) {
        for (TestObjectProperty property : properties) {
            if (property.getName().equals(name)) {
                return property.getValue();
            }
        }
        return "";
    }

    /**
     * Find the value of a property using the property name
     * 
     * @param name the name of the property to find
     * @param caseSensitive boolean value to indicate if the finding process is case sensitive or not
     * @return the value of a property, or blank if not found
     */
    public String findPropertyValue(String name, boolean caseSensitive) {
        if (caseSensitive) {
            return findPropertyValue(name);
        }
        for (TestObjectProperty property : properties) {
            if (property.getName().equalsIgnoreCase(name)) {
                return property.getValue();
            }
        }
        return "";
    }

    /**
     * Find the property using the property name
     * 
     * @param name the name of the property to find
     * @return the found property, or null if not found
     */
    public TestObjectProperty findProperty(String name) {
        for (TestObjectProperty property : properties) {
            if (property.getName().equals(name)) {
                return property;
            }
        }
        return null;
    }
    
    
    /**
     * Get all properties of the test object
     * 
     * @return a list contains all properties of the test object
     */
    public List<TestObjectXpath> getXpaths() {
        return xpaths;
    }

    /**
     * Get all active properties of the test object
     * 
     * @return a list contains all active properties of the test object
     */
    public List<TestObjectXpath> getActiveXpaths() {
        List<TestObjectXpath> activeXpaths = new ArrayList<TestObjectXpath>();
        for (TestObjectXpath xpath : xpaths) {
            if (xpath.isActive()) {
                activeXpaths.add(xpath);
            }
        }
        return activeXpaths;
    }

    /**
     * Set the properties of the test object
     * 
     * @param xpaths a list of properties to set to the test object
     */
    public void setXpaths(List<TestObjectXpath> xpaths) {
        this.xpaths = xpaths;
    }

    /**
     * Add a new property to the test object
     * 
     * @param xpath the new {@link TestObjectProperty} to add
     * @return this test object after adding the new property
     */
    public TestObject addXpath(TestObjectXpath xpath) {
        this.xpaths.add(xpath);
        return this;
    }

    /**
     * Add a new property to the test object
     * 
     * @param name the name of the new property
     * @param condition the {@link ConditionType} of the new property
     * @param value the value of the new property
     * @return this test object after adding the new property
     */
    public TestObject addXpath(String name, ConditionType condition, String value) {
        this.xpaths.add(new TestObjectXpath(name, condition, value));
        return this;
    }

    /**
     * Add a new property to the test object
     * 
     * @param name the name of the new property
     * @param condition the {@link ConditionType} of the new property
     * @param value the value of the new property
     * @param isActive the active flag of the new property
     * @return this test object after adding the new property
     */
    public TestObject addXpath(String name, ConditionType condition, String value, boolean isActive) {
        this.xpaths.add(new TestObjectXpath(name, condition, value, isActive));
        return this;
    }

    /**
     * Find the value of a property using the property name
     * 
     * @param name the name of the property to find
     * @return the value of a property, or blank if not found
     */
    public String findXpathValue(String name) {
        for (TestObjectXpath xpaths : xpaths) {
            if (xpaths.getName().equals(name)) {
                return xpaths.getValue();
            }
        }
        return "";
    }

    /**
     * Find the value of a property using the property name
     * 
     * @param name the name of the property to find
     * @param caseSensitive boolean value to indicate if the finding process is case sensitive or not
     * @return the value of a property, or blank if not found
     */
    public String findXpathValue(String name, boolean caseSensitive) {
        if (caseSensitive) {
            return findXpathValue(name);
        }
        for (TestObjectXpath xpath : xpaths) {
            if (xpath.getName().equalsIgnoreCase(name)) {
                return xpath.getValue();
            }
        }
        return "";
    }

    /**
     * Find the property using the property name
     * 
     * @param name the name of the property to find
     * @return the found property, or null if not found
     */
    public TestObjectXpath findXpath(String name) {
        for (TestObjectXpath xpath : xpaths) {
            if (xpath.getName().equals(name)) {
                return xpath;
            }
        }
        return null;
    }


    /**
     * Get the id of this test object
     * 
     * @return the id of this test object
     */
    @Override
    public String getObjectId() {
        return objectId;
    }

    /**
     * Get the parent object of this test object
     * 
     * @return the parent object of this test object
     */
    public TestObject getParentObject() {
        return parentObject;
    }

    /**
     * Set the parent object of this test object
     * 
     * @param parentObject the parent object to set
     */
    public void setParentObject(TestObject parentObject) {
        this.parentObject = parentObject;
    }

    public boolean isParentObjectShadowRoot() {
        return isParentObjectShadowRoot;
    }

    public void setParentObjectShadowRoot(boolean isParentObjectShadowRoot) {
        this.isParentObjectShadowRoot = isParentObjectShadowRoot;
    }

    /**
     * Get the path of the image this test object contains
     * 
     * @return the path of the image this test object contains, or null if this test object does not contains any image
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Set the path of the image this test object contains
     * 
     * @param imagePath the new image path to set
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * Check if the path of the image this test object contains is relative or not
     * 
     * @return true if the path of the image this test object contains is relative or not; otherwise false
     */
    public boolean getUseRelativeImagePath() {
        return useRelativeImagePath;
    }

    /**
     * Set if the path of the image this test object contains is relative or not
     * 
     * @param useRelativeImagePath the boolean value indicate if the path of the image this test object contains is
     * relative or not
     */
    public void setUseRelativeImagePath(boolean useRelativeImagePath) {
        this.useRelativeImagePath = useRelativeImagePath;
    }

    @Override
    public String toString() {
        return "TestObject - '" + getObjectId() + "'";
    }

    public SelectorMethod getSelectorMethod() {
        return selectorMethod;
    }


    public void setSelectorMethod(SelectorMethod selectorMethod) {
        this.selectorMethod = selectorMethod;
    }


    public void setSelectorValue(SelectorMethod selectorMethod, String selectorValue) {
        selectorCollection.put(selectorMethod, selectorValue);
    }

    public Map<SelectorMethod, String> getSelectorCollection() {
        return selectorCollection;
    }
    
    /**
     * Set a cached WebElement to this TestObject. This cached
     * WebElement will be used by built-in keywords instead of calling Selenium API
     * to retrieve WebElement from TestObject's properties
     * 
     * @param cachedWebElement
     */
    public void setCachedWebElement(WebElement cachedWebElement) {
        this.cachedWebElement = cachedWebElement;
    }

    /**
     * Retrieve cached WebElement set by {@link TestObject#setCachedWebElement(WebElement)}
     * 
     * @return The cached {@link WebElement} if exists, null otherwise
     */
    public WebElement getCachedWebElement() {
        return cachedWebElement;
    }
}
