package com.kms.katalon.core.keyword.internal;

import org.apache.commons.lang.ObjectUtils

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject

public abstract class AbstractKeyword implements IKeyword {

    protected final KeywordLogger logger = KeywordLogger.getInstance(this.getClass());

    protected TestObject getTestObject(Object param) {
        if (param instanceof TestObject) {
            return (TestObject) param
        }
        return ObjectRepository.findTestObject(ObjectUtils.toString(param))
    }

    protected FailureHandling getFailureHandling(Object[] params, int index) {
        Object param = getParam(params, index)
        if (param instanceof FailureHandling) {
            return (FailureHandling) param
        }
        return RunConfiguration.getDefaultFailureHandling()
    }

    protected boolean getBooleanValue(Object[] params, int index) {
        Object param = getParam(params, index)
        return param instanceof Boolean && (boolean) param
    }

    protected Object getParam(Object[] params, int index) {
        try {
            return params[index]
        } catch(Exception e) {
            return null
        }
    }
}
