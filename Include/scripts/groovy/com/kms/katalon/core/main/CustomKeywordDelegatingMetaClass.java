package com.kms.katalon.core.main;

import java.util.HashMap;
import java.util.List;

import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.InvokerInvocationException;

import com.kms.katalon.core.exception.StepErrorException;
import com.kms.katalon.core.exception.StepFailedException;
import com.kms.katalon.core.logging.ErrorCollector;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.logging.LogLevel;
import com.kms.katalon.core.util.internal.ExceptionsUtil;

import groovy.lang.DelegatingMetaClass;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;

public class CustomKeywordDelegatingMetaClass extends DelegatingMetaClass {
    
    private static final KeywordLogger logger = KeywordLogger.getInstance(CustomKeywordDelegatingMetaClass.class);
    
    private GroovyClassLoader groovyClassLoader;

    private ErrorCollector errorCollector = ErrorCollector.getCollector();

    CustomKeywordDelegatingMetaClass(final Class<?> clazz, GroovyClassLoader groovyClassLoader) {
        super(clazz);
        initialize();
        this.groovyClassLoader = groovyClassLoader;
    }

    @Override
    public Object invokeStaticMethod(Object object, String methodName, Object[] arguments) {
        List<Throwable> oldErrors = errorCollector.getCoppiedErrors();
        boolean oldIsKeywordPassed = errorCollector.isKeywordPassed();
        try {
            errorCollector.clearErrors();
            errorCollector.setKeywordPassed(false);

            int classAndMethodSeparatorIndex = methodName.lastIndexOf(".");
            String customKeywordClassName = methodName.substring(0, classAndMethodSeparatorIndex);
            Class<?> customKeywordClass = getCustomKeywordClassAndSetMetaClass(customKeywordClassName);
            GroovyObject obj = (GroovyObject) customKeywordClass.newInstance();

            String customKeywordMethodName = methodName.substring(classAndMethodSeparatorIndex + 1,
                    methodName.length());
            Object result = obj.invokeMethod(customKeywordMethodName, arguments);

            if (errorCollector.containsErrors()) {
                Throwable throwable = errorCollector.getFirstError();

                logger.logMessage(ErrorCollector.fromError(throwable),
                        ExceptionsUtil.getMessageForThrowable(throwable),
                        throwable);
            } else if (!errorCollector.isKeywordPassed()) {
                logger.logMessage(LogLevel.PASSED, methodName + " is PASSED");
            }

            return result;
        } catch (Throwable throwable) {
            errorCollector.addError(throwable);

            Throwable errorToThrow = errorCollector.getFirstError();
            if (errorToThrow != null) {
                throwError(errorToThrow);
            }
            return null;
        } finally {
            // return previous errors to error collector
            errorCollector.getErrors().addAll(0, oldErrors);
            errorCollector.setKeywordPassed(oldIsKeywordPassed);
        }
    }

    private static void throwError(Throwable error) {
        if (ErrorCollector.isErrorFailed(error)) {
            logger.logFailed(error.getMessage(), null, error);
            if (error instanceof InvokerInvocationException) {
                throw (InvokerInvocationException) error;
            }
            if (error instanceof AssertionError) {
                throw (AssertionError) error;
            }
            if (error instanceof StepFailedException) {
                throw (StepFailedException) error;
            }
            throw new StepFailedException(error);
        }
        logger.logError(error.getMessage(), null, error);
        if (error instanceof StepErrorException) {
            throw (StepErrorException) error;
        }
        throw new StepErrorException(error);
    }

    private Class<?> getCustomKeywordClassAndSetMetaClass(String customKeywordClassName) throws ClassNotFoundException {
        Class<?> customKeywordClass = groovyClassLoader.loadClass(customKeywordClassName);

        MetaClass keywordMetaClass = InvokerHelper.metaRegistry.getMetaClass(customKeywordClass);
        if (!(keywordMetaClass instanceof KeywordClassDelegatingMetaClass)) {
            InvokerHelper.metaRegistry.setMetaClass(customKeywordClass,
                    new KeywordClassDelegatingMetaClass(customKeywordClass, groovyClassLoader));
        }
        return customKeywordClass;
    }
}
