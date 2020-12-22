package com.kms.katalon.core.mobile.common;

import java.util.List;

import com.kms.katalon.core.common.XPathBuilder;
import com.kms.katalon.core.testobject.TestObjectProperty;

public class MobileXPathBuilder extends XPathBuilder { 
    public MobileXPathBuilder(List<TestObjectProperty> properties) {
        super(properties);
    }

    @Override
    protected String getXPathTextExpression() {
        return XPATH_TEXT_PROPERTY_EXPRESSION_FOR_MOBILE;
    }
}
