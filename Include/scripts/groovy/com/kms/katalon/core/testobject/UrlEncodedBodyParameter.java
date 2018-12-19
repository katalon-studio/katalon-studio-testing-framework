package com.kms.katalon.core.testobject;

public class UrlEncodedBodyParameter {
    
    private String name;
    
    private String value;

    public UrlEncodedBodyParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }
    
    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
    
//    public static UrlEncodedBodyParameter create(String name, String value) {
//        UrlEncodedBodyParameter bodyParameter = new UrlEncodedBodyParameter();
//        bodyParameter.name = name;
//        bodyParameter.value = value;
//        return bodyParameter;
//    }
}
