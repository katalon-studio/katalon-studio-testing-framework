package com.kms.katalon.core.testobject;

public class FormDataBodyParameter {
    public static final String PARAM_TYPE_TEXT = "Text";

    public static final String PARAM_TYPE_FILE = "File";

    private String name;

    private String value;

    private String type = PARAM_TYPE_TEXT;

    public FormDataBodyParameter(String name, String value, String type) {
        this.name = name;
        this.value = value;
        this.type = type;
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

//    public static FormDataBodyParameter create(String name, String value, String type) {
//        FormDataBodyParameter formDataParameter = new FormDataBodyParameter();
//        formDataParameter.name = name;
//        formDataParameter.value = value;
//        formDataParameter.type = type;
//        return formDataParameter;
//    }
}
