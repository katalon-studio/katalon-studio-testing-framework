package com.kms.katalon.core.testobject;

public class FormDataBodyParameter {
    public static final String PARAM_TYPE_TEXT = "Text";

    public static final String PARAM_TYPE_FILE = "File";

    private String name;

    private String value;

    private String type = PARAM_TYPE_TEXT;

    private String contentType;

    public FormDataBodyParameter(String name, String value, String type) {
        this(name, value, type, null);
    }

    public FormDataBodyParameter(String name, String value, String type, String contentType) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.contentType = contentType;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
