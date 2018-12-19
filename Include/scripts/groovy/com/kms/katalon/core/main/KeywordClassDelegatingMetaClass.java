package com.kms.katalon.core.main;

import groovy.lang.DelegatingMetaClass;
import groovy.lang.GroovyClassLoader;

import com.kms.katalon.core.constants.StringConstants;

public class KeywordClassDelegatingMetaClass extends DelegatingMetaClass {
    private GroovyClassLoader groovyClassLoader;
    KeywordClassDelegatingMetaClass(final Class<?> clazz, GroovyClassLoader groovyClassLoader) {
        super(clazz);
        initialize();
        this.groovyClassLoader = groovyClassLoader;
    }

    private Class<?> getGlobalVariableClassByPropertyName(String propertyName) {
        try {
            if (StringConstants.GLOBAL_VARIABLE_CLASS_NAME.equals(propertyName)) {
                return groovyClassLoader.loadClass(StringConstants.GLOBAL_VARIABLE_CLASS_NAME);
            }
        } catch (ClassNotFoundException e) {
            // Cannot find GlobalVariable so let default Meta Class decide what to return
        }
        return null;
    }

    @Override
    public Object getProperty(Object object, String property) {
        Class<?> globalVariableClass = getGlobalVariableClassByPropertyName(property);
        return globalVariableClass != null ? globalVariableClass : super.getProperty(object, property);
    }

    @Override
    public Object invokeMissingProperty(Object instance, String propertyName, Object optionalValue, boolean isGetter) {
        Class<?> globalVariableClass = getGlobalVariableClassByPropertyName(propertyName);
        return globalVariableClass != null ? globalVariableClass : super.invokeMissingProperty(instance, propertyName,
                optionalValue, isGetter);
    }
}
