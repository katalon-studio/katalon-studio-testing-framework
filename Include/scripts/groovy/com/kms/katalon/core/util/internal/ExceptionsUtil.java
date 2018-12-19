package com.kms.katalon.core.util.internal;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.codehaus.groovy.runtime.StackTraceUtils;

import com.google.common.base.Throwables;
import com.kms.katalon.core.main.ScriptEngine;

import groovy.lang.MissingPropertyException;

public class ExceptionsUtil {
    
    private static final Pattern CLASS_PATTERN = Pattern.compile("Script[0-9]{13}");
    
    public static String getMessageForThrowable(Throwable t) {
        if (t == null) {
            return "";
        }
        return getExceptionMessage(t);
    }

    private static String getExceptionMessage(Throwable throwable) {
        if (throwable instanceof MissingPropertyException) {
            return getExceptionMessage((MissingPropertyException) throwable);
        } else {
            return throwable.getClass().getName()
                    + (throwable.getMessage() != null ? (": " + throwable.getMessage()) : "");
        }
    }

    private static String getExceptionMessage(MissingPropertyException exception) {
        return "Variable '" + exception.getProperty() + "' is not defined for test case.";
    }

    public static String getStackTraceForThrowable(Throwable t) {
        t = StackTraceUtils.deepSanitize(t);
        StackTraceElement[] newStackTrace = Arrays.stream(t.getStackTrace())
                .map(stackTraceElement -> {
                    String declaringClass = stackTraceElement.getClassName();
                    if (CLASS_PATTERN.matcher(declaringClass).matches()) {
                        declaringClass = ScriptEngine.getTestCaseName(declaringClass);
                        if (declaringClass != null) {
                            return new StackTraceElement(
                                    declaringClass, 
                                    stackTraceElement.getMethodName(), 
                                    declaringClass, 
                                    stackTraceElement.getLineNumber());
                        }
                    }
                    return stackTraceElement;
                })
                .collect(Collectors.toList())
                .toArray(new StackTraceElement[] {});
        t.setStackTrace(newStackTrace);
        String stackTrace = Throwables.getStackTraceAsString(t);
        return stackTrace;
    }
}
