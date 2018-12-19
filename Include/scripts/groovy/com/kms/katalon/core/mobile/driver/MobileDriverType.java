package com.kms.katalon.core.mobile.driver;

import com.kms.katalon.core.driver.DriverType;
import com.kms.katalon.core.mobile.constants.StringConstants;

/**
 * Enum for all supported mobile platform
 *
 */
public enum MobileDriverType implements DriverType {
    IOS_DRIVER(StringConstants.IOS), ANDROID_DRIVER(StringConstants.ANDROID);

    private String driverName;

    private MobileDriverType(String driverName) {
        this.driverName = driverName;
    }

    /**
     * Get the name of this mobile driver type
     * 
     * @return the name of this mobile driver type
     */
    @Override
    public String getName() {
        return name();
    }

    /**
     * Get the platform of this mobile driver type
     * 
     * @return the platform of this mobile driver type
     */
    public String getPlatform() {
        return toString();
    }

    @Override
    public String toString() {
        return driverName;
    }

    public static String[] stringValues() {
        String[] stringValues = new String[values().length];
        for (int i = 0; i < values().length; i++) {
            stringValues[i] = values()[i].toString();
        }
        return stringValues;
    }

    public static MobileDriverType fromStringValue(String stringValue) {
        if (stringValue == null) {
            return null;
        }
        for (int i = 0; i < values().length; i++) {
            if (values()[i].toString().equals(stringValue)) {
                return values()[i];
            }
        }
        return null;
    }

    @Override
    public String getPropertyKey() {
        return com.kms.katalon.core.appium.constants.AppiumStringConstants.CONF_EXECUTED_PLATFORM;
    }

    @Override
    public String getPropertyValue() {
        return getName();
    }
}
