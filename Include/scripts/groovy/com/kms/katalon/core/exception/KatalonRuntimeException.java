package com.kms.katalon.core.exception;

public class KatalonRuntimeException extends RuntimeException {
    
    private static final long serialVersionUID = 4188128690553825234L;

    public KatalonRuntimeException(String message) {
        super(message);
    }
    
    public KatalonRuntimeException(Throwable e) {
        super(e);
    }
    
    public KatalonRuntimeException(String message, Throwable e) {
        super(message, e);
    }
}
