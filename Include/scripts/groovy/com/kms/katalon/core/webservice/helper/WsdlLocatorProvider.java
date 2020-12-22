package com.kms.katalon.core.webservice.helper;

import org.apache.commons.lang3.StringUtils;

import com.kms.katalon.core.webservice.definition.WebServiceDefinitionLoader;
import com.kms.katalon.core.webservice.definition.WebServiceDefinitionLoaderProvider;
import com.kms.katalon.core.webservice.definition.WebServiceDefinitionLoaderSupplier;
import com.kms.katalon.core.webservice.wsdl.support.wsdl.BasicWsdlDefinitionLocator;
import com.kms.katalon.core.webservice.wsdl.support.wsdl.WsdlDefinitionLocator;

public class WsdlLocatorProvider {

    private WsdlLocatorProvider() {

    }

    public static WsdlDefinitionLocator getLocator(String wsdlLocation) {
        if (StringUtils.isBlank(wsdlLocation)) {
            throw new IllegalArgumentException("WSDL location must not be null or empty.");
        }
        WebServiceDefinitionLoaderSupplier loaderSupplier = new WebServiceDefinitionLoaderSupplier() {
            @Override
            public WebServiceDefinitionLoader get(String location) throws Exception {
                return WebServiceDefinitionLoaderProvider.getLoader(location);
            }
        };
        return new BasicWsdlDefinitionLocator(wsdlLocation, loaderSupplier);
    }

    public static WsdlDefinitionLocator getLocator(
            String wsdlLocation,
            WebServiceDefinitionLoaderSupplier loaderSupplier) {
        if (StringUtils.isBlank(wsdlLocation)) {
            throw new IllegalArgumentException("WSDL location must not be null or empty.");
        }

        return new BasicWsdlDefinitionLocator(wsdlLocation, loaderSupplier);
    }
}
