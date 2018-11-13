package com.kms.katalon.core.keyword.internal;

import com.kms.katalon.core.exception.StepErrorException;
import com.kms.katalon.core.exception.StepFailedException;
import com.kms.katalon.core.logging.ErrorCollector;
import com.kms.katalon.core.model.FailureHandling;

public class KeywordExceptionHandler {
    public static void throwExceptionIfStepFailed(String message) throws StepFailedException {
        throw new StepFailedException(message);
    }

    public static void throwExceptionIfStepFailed(String message, FailureHandling flowControl) throws StepFailedException {
        StepFailedException ex = new StepFailedException(message);
        if (flowControl != FailureHandling.OPTIONAL) {
            ErrorCollector.getCollector().addError(ex);
        }
        
        if (flowControl == null || flowControl == FailureHandling.STOP_ON_FAILURE){
            throw new StepFailedException(message); 
        }
    }
    
    public static void throwExceptionIfStepHasError(String message) throws StepErrorException {
        throw new StepErrorException(message);
    }    
}
