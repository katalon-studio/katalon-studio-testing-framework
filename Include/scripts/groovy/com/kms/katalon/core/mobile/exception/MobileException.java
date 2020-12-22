package com.kms.katalon.core.mobile.exception;

public class MobileException extends Exception {
    public MobileException(String msg) {
        super(msg);
    }
    
    public MobileException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
    
    public MobileException() {
        super("Cannot complete request on mobile device");
    }
    
    public MobileException(Throwable throwable) {
        super(throwable);
    }
    
}
