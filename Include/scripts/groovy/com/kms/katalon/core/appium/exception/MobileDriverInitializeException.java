package com.kms.katalon.core.appium.exception;

import com.kms.katalon.core.util.internal.ExceptionsUtil;

public class MobileDriverInitializeException extends Exception {

    private static final long serialVersionUID = 1L;

    public MobileDriverInitializeException(String message) {
        super(message);
    }

    public MobileDriverInitializeException(Throwable t) {
        super(ExceptionsUtil.getMessageForThrowable(t), t);
    }
}
