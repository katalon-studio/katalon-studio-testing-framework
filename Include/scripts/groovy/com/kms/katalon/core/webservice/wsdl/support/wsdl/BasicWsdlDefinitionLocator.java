package com.kms.katalon.core.webservice.wsdl.support.wsdl;

import java.io.InputStream;

import org.xml.sax.InputSource;

import com.kms.katalon.core.webservice.definition.WebServiceDefinitionLoader;
import com.kms.katalon.core.webservice.definition.WebServiceDefinitionLoaderSupplier;
import com.kms.katalon.util.UrlUtil;

public class BasicWsdlDefinitionLocator implements WsdlDefinitionLocator {

    private String last;

    private String wsdlLocation;

    private WebServiceDefinitionLoaderSupplier loaderSupplier;

    public BasicWsdlDefinitionLocator(String wsdlLocation, WebServiceDefinitionLoaderSupplier loaderSupplier) {
        this.wsdlLocation = wsdlLocation;
        this.loaderSupplier = loaderSupplier;
    }

    public String getBaseURI() {
        return wsdlLocation;
    }

    public InputSource getBaseInputSource() {
        try {
            InputStream is = load(wsdlLocation);
            return new InputSource(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public InputSource getImportInputSource(String parent, String imp) {
        if (UrlUtil.isAbsoluteUrl(imp)) {
            last = imp;
        } else {
            last = UrlUtil.joinRelativeUrl(parent, imp);
        }

        try {
            InputStream input = load(last);
            return input == null ? null : new InputSource(input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private InputStream load(String location) throws Exception {
        WebServiceDefinitionLoader loader = loaderSupplier.get(location);
        return loader.load();

    }

    public String getLatestImportURI() {
        String result = last == null ? wsdlLocation : last;
        return result;
    }

    public void close() {
    }

    public String getWsdlLocation() {
        return wsdlLocation;
    }
}
