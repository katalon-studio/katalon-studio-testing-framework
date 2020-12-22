package com.kms.katalon.core.webservice.definition;

import java.io.InputStream;

public interface WebServiceDefinitionLoader {
    
    InputStream load() throws Exception;
    
    String getDefinitionLocation();
}
