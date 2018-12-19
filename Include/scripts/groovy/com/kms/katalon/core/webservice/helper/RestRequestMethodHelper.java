package com.kms.katalon.core.webservice.helper;

import com.kms.katalon.core.webservice.constants.RequestHeaderConstants;

public class RestRequestMethodHelper {
    
    public static String[] getBuiltInMethods() {
        return new String[] {
            RequestHeaderConstants.GET,
            RequestHeaderConstants.POST,
            RequestHeaderConstants.PUT,
            RequestHeaderConstants.DELETE,
            RequestHeaderConstants.PATCH,
            RequestHeaderConstants.HEAD,
            RequestHeaderConstants.CONNECT,
            RequestHeaderConstants.OPTIONS,
            RequestHeaderConstants.TRACE
        };
    }
    
    private static String[] getBodyUnsuppportedMethods() {
        return new String[] {
            RequestHeaderConstants.GET,
            RequestHeaderConstants.HEAD,
            RequestHeaderConstants.TRACE,
            RequestHeaderConstants.OPTIONS,
            RequestHeaderConstants.CONNECT
        };
    }

    public static boolean isBuiltInMethod(String method) {
        String[] builtInMethods = getBuiltInMethods();
        return isMethodInList(method, builtInMethods);
    }
    
    public static boolean isBodySupported(String method) {
        String[] bodyUnsupportedMethods = getBodyUnsuppportedMethods();
        return !isMethodInList(method, bodyUnsupportedMethods);
    }
    
    private static boolean isMethodInList(String method, String[] methods) {
        for (String methodItem : methods) {
            if (methodItem.equalsIgnoreCase(method)) {
                return true;
            }
        }
        return false;
    }
}
