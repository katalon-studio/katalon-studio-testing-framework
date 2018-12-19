package com.kms.katalon.core.testobject;

public class TestObjectProperty {
    private String name;

    private ConditionType condition;

    private String value;

    private boolean isActive;

    public TestObjectProperty() {
        isActive = true;
    }

    public TestObjectProperty(String name, ConditionType condition, String value) {
        this.name = name;
        this.condition = condition;
        this.value = value;
        this.isActive = true;
    }

    public TestObjectProperty(String name, ConditionType condition, String value, boolean isActive) {
        this.name = name;
        this.condition = condition;
        this.value = value;
        this.isActive = isActive;
    }

    /**
     * Get name of this property
     * 
     * @return name of this property
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of this property
     * 
     * @param name the new name for this property
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the condition for this property
     * 
     * @return the condition as {@link ConditionType} of this property
     */
    public ConditionType getCondition() {
        return condition;
    }

    /**
     * Set the condition for this property
     * 
     * @param condition the new condition as {@link ConditionType} for this property
     */
    public void setCondition(ConditionType condition) {
        this.condition = condition;
    }

    /**
     * Get the value of this property
     * 
     * @return the value of this property
     */
    public String getValue() {
        return value;
    }

    /**
     * Set the value for this property
     * 
     * @param value the new value to set to this property
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Check if this property is active
     * 
     * @return true if this property is activate; otherwise false
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Set this property to be active
     * 
     * @param isActive the active flag to set to this property
     */
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}
