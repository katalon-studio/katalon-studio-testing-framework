package com.kms.katalon.core.model;

public enum KeyStoreType {
    PKCS12("PKCS12"),
    JKS("JKS");
    
    private final String type;
    
    private KeyStoreType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }   
}
