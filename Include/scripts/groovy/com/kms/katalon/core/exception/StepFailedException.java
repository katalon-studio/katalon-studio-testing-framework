package com.kms.katalon.core.exception;

import com.kms.katalon.core.util.internal.ExceptionsUtil;

/**
 * Exception to stop execution and mark keyword or test case as FAILED
 *
 */
public class StepFailedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public StepFailedException(String message) {
        super(message);
    }
    
    public StepFailedException(Throwable t) {
        super(ExceptionsUtil.getMessageForThrowable(t), t);
    }
    
    public StepFailedException(String message, Throwable t) {
        super(message, t);
    }
}
