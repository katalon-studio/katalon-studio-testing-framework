package com.kms.katalon.core.webservice.common;

public class WebServiceMethod {

    public static final String TYPE_DEFAULT = "Standard";
    
    public static final String TYPE_CUSTOM = "Custom";
    
    private String name = "";
    
    private String type;
    
    private String description = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
