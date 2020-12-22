package com.kms.katalon.core.webservice.definition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebServiceDefinitionFileLoader extends AbstractWebServiceDefinitionLoader {

    protected WebServiceDefinitionFileLoader(String definitionLocation) {
        super(definitionLocation);
    }

    public InputStream load() throws MalformedURLException, FileNotFoundException, URISyntaxException {
        return new FileInputStream(new File(new URI(definitionLocation)));
    }
}
