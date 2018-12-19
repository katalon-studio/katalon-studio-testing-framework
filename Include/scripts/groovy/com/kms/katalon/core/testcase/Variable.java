package com.kms.katalon.core.testcase;

public class Variable {
    private String name;

    private String defaultValue;

    /**
     * A flag that marks the variable's default value will be masked in logs.
     */
    private boolean masked = false;

    public String getName() {
        if (name == null) {
            name = "";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultValue() {
        if (defaultValue == null) {
            defaultValue = "";
        }
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isMasked() {
        return masked;
    }

    public void setMasked(boolean masked) {
        this.masked = masked;
    }
}
