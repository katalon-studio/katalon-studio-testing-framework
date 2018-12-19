package com.kms.katalon.core.testobject.impl;

import java.util.Arrays;

public enum HttpBodyType {
    TEXT("text"),
    URL_ENCODED("x-www-form-urlencoded"),
    FORM_DATA("form-data"),
    FILE("file");
    
    private final String type;

    private HttpBodyType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static HttpBodyType fromType(String type) {
        return Arrays.asList(values()).stream()
            .filter(bodyType -> bodyType.getType().equals(type))
            .findAny().orElse(null);
    }
}
