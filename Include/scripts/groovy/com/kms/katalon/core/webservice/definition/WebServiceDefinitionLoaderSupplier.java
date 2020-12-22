package com.kms.katalon.core.webservice.definition;

public interface WebServiceDefinitionLoaderSupplier {

    public WebServiceDefinitionLoader get(String location) throws Exception;
}
