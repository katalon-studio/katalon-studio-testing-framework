package com.kms.katalon.core.driver;

public class ExistingDriverType implements DriverType {
    private String originalDriverName;

    public ExistingDriverType(String originalDriverName) {
        this.originalDriverName = originalDriverName;
    }

    public String getOriginalDriverName() {
        return originalDriverName;
    }

    @Override
    public String getName() {
        return ExistingDriverType.class.getSimpleName();
    }

    @Override
    public String getPropertyKey() {
        return null;
    }

    @Override
    public String getPropertyValue() {
        return null;
    }

    @Override
    public String toString() {
        return getOriginalDriverName();
    }
}
