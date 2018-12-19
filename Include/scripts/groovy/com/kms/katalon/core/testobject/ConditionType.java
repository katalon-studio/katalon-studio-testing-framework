package com.kms.katalon.core.testobject;


public enum ConditionType {
    EQUALS("equals"), 
    NOT_EQUAL("not equal"), 
    CONTAINS("contains"), 
    NOT_CONTAIN("not contain"), 
    STARTS_WITH("starts with"), 
    ENDS_WITH("ends with"), 
    MATCHES_REGEX("matches regex"), 
    NOT_MATCH_REGEX("not match regex"),

    //For mobile
    EXPRESSION("expression");
    
    private String text;
    
    private ConditionType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
    
    public static ConditionType fromValue(String value) {
        for (ConditionType condition : values()) {
            if (condition.toString().equals(value)) {
                return condition;
            }
        }
        
        return null;
    }
}
