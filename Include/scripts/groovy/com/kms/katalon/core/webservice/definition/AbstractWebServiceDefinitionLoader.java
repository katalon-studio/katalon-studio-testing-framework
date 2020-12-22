package com.kms.katalon.core.webservice.definition;

public abstract class AbstractWebServiceDefinitionLoader implements WebServiceDefinitionLoader {

    protected String definitionLocation;
    
    protected AbstractWebServiceDefinitionLoader(String definitionLocation) {
        this.definitionLocation = definitionLocation;
    }
    
    public String getDefinitionLocation() {
        return definitionLocation;
    }
}
