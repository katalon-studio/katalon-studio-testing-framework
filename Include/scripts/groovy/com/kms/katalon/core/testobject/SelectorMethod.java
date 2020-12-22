package com.kms.katalon.core.testobject;

public enum SelectorMethod {
    BASIC("Attributes"),
    XPATH("XPath"),
    CSS("CSS"),
    IMAGE("Image");

    private String name;

    private SelectorMethod(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
