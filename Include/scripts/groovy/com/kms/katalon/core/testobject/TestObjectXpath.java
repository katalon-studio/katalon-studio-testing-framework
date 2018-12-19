package com.kms.katalon.core.testobject;

public class TestObjectXpath {
    private String name;

    private ConditionType condition;

    private String value;

    private boolean isActive;

    public TestObjectXpath() {
        isActive = true;
    }

    public TestObjectXpath(String name, ConditionType condition, String value) {
        this.name = name;
        this.condition = condition;
        this.value = value;
        this.isActive = true;
    }

    public TestObjectXpath(String name, ConditionType condition, String value, boolean isActive) {
        this.name = name;
        this.condition = condition;
        this.value = value;
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public ConditionType getCondition() {
        return condition;
    }

    public void setCondition(ConditionType condition) {
        this.condition = condition;
    }


    public String getValue() {
        return value;
    }


    public void setValue(String value) {
        this.value = value;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}
