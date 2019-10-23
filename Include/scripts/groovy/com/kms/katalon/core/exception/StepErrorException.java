package com.kms.katalon.core.exception;

/**
 * Exception to stop execution and mark keyword or test case as ERROR
 *
 */
public class StepErrorException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public StepErrorException(String message) {
        super(message);
    }
    
    public StepErrorException(Throwable t) {
        super(t);
    }
    
    public StepErrorException(String message, Throwable t) {
        super(message, t);
    }
}
