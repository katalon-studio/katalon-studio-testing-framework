package com.kms.katalon.core.testobject;

import org.apache.commons.lang3.StringUtils;

/**
 * An extended class of {@link TestObject} for the Mobile object specifically. MobileTestObject describes the way
 * Katalon Studio finds Appium Element via the <b>mobileLocator</b> and <b>mobileLocatorStrategy</b>.
 * 
 * @since 7.6.0
 * @see MobileLocatorStrategy
 */
public class MobileTestObject extends TestObject {
    
    private MobileLocatorStrategy mobileLocatorStrategy;

    private String mobileLocator;

    public MobileTestObject(String objectId) {
        super(objectId);
    }

    /**
     * The locator value of the test object.
     * @since 7.6.0
     */
    public String getMobileLocator() {
        return mobileLocator;
    }

    /**
     * Sets the locator value for the test object
     * @param mobileLocator the locator value
     * @since 7.6.0
     */
    public void setMobileLocator(String mobileLocator) {
        this.mobileLocator = mobileLocator;
    }

    /**
     * The selected locator strategy of the test object.
     * @since 7.6.0
     */
    public MobileLocatorStrategy getMobileLocatorStrategy() {
        return mobileLocatorStrategy;
    }

    /**
     * Sets the selected locator strategy for the test object
     * @param mobileLocatorStrategy an enum value of {@link MobileLocatorStrategy}
     * @since 7.6.0
     */
    public void setMobileLocatorStrategy(MobileLocatorStrategy mobileLocatorStrategy) {
        this.mobileLocatorStrategy = mobileLocatorStrategy;
    }

    /**
     * Support almost of Appium selector strategies that describes via this link <a href="http://appium.io/docs/en/commands/element/find-elements/#selector-strategies">
     * http://appium.io/docs/en/commands/element/find-elements/#selector-strategies</a>
     *
     * @since 7.6.0
     */
    public static enum MobileLocatorStrategy {
        ATTRIBUTES("Attributes"),
        ACCESSIBILITY("Accessibility ID"),
        CLASS_NAME("Class Name"),
        ID("ID"),
        NAME("Name"),
        XPATH("XPATH"),
        IMAGE("Image"),
        ANDROID_UI_AUTOMATOR("Android UI Automator"),
        ANDROID_VIEWTAG("Android View Tag"),
        IOS_PREDICATE_STRING("iOS Predicate String"),
        IOS_CLASS_CHAIN("iOS Class Chain"),
        CUSTOM("Custom");

        private final String locatorStrategy;

        private MobileLocatorStrategy(String locatorStrategy) {
            this.locatorStrategy = locatorStrategy;
        }

        public String getLocatorStrategy() {
            return locatorStrategy;
        }
        
        public static MobileLocatorStrategy valueOfStrategy(String strategy) {
            if (StringUtils.isEmpty(strategy)) {
                return null;
            }
            for (MobileLocatorStrategy str : values()) {
                if (str.getLocatorStrategy().equals(strategy)) {
                    return str;
                }
            }
            throw new IllegalArgumentException("Strategy: " + strategy + " not found");
        }
    }
}
