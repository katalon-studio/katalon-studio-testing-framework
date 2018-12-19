package com.kms.katalon.core.appium.exception;

import com.kms.katalon.core.util.internal.ExceptionsUtil;

public class IOSWebkitStartException extends Exception {

    private static final long serialVersionUID = 1L;
    public static final String MSG_ERR_START_IOS_WEBKIT = "Failed to start ios_webkit_debug_proxy on default port 27753";

    public IOSWebkitStartException() {
        this(MSG_ERR_START_IOS_WEBKIT);
    }
    
    public IOSWebkitStartException(String message) {
        super(message);
    }

    public IOSWebkitStartException(Throwable t) {
        super(ExceptionsUtil.getMessageForThrowable(t), t);
    }
}
