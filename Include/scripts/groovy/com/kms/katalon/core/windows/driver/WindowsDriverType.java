package com.kms.katalon.core.windows.driver;

import com.kms.katalon.core.driver.DriverType;

public class WindowsDriverType implements DriverType {
    
    private static final WindowsDriverType INSTANCE = new WindowsDriverType();

    @Override
    public String getName() {
        return "Windows";
    }

    @Override
    public String getPropertyKey() {
        return "platformName";
    }

    @Override
    public String getPropertyValue() {
        return "Windows";
    }
    
    public static WindowsDriverType getInstance() {
        return INSTANCE;
    }
    
    @Override
    public String toString() {
        return "Windows";
    }
}
