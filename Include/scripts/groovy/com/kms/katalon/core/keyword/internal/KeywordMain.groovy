package com.kms.katalon.core.keyword.internal

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils

import com.google.common.base.Throwables
import com.kms.katalon.core.exception.StepErrorException
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.logging.ErrorCollector
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.util.internal.ExceptionsUtil

import groovy.transform.CompileStatic


public class KeywordMain {
    private static final String EMPTY_REASON = "";
    private static final KeywordLogger logger = KeywordLogger.getInstance(KeywordMain.class);

    @CompileStatic
    public static stepFailed(String message, FailureHandling flHandling) throws StepFailedException {
        stepFailed(message, flHandling, null)
    }

    @CompileStatic
    public static stepFailed(String message, FailureHandling flHandling, Throwable t, Map<String, String> attributes = null) throws StepFailedException {
        String failedMessage = buildReasonMessage(message, t != null ? ExceptionsUtil.getStackTraceForThrowable(t) : EMPTY_REASON).toString()
        switch (flHandling) {
            case FailureHandling.OPTIONAL:
                logger.logWarning(failedMessage, attributes, t);
                break;
            case FailureHandling.CONTINUE_ON_FAILURE:
                logger.logFailed(failedMessage, attributes, t);
                Exception ex = null;
                if (ErrorCollector.isErrorFailed(t)) {
                    ex = new StepErrorException(failedMessage, t)
                }
                ex = new StepFailedException(failedMessage, t)
                ErrorCollector.getCollector().addError(ex);
                break;
            case FailureHandling.STOP_ON_FAILURE:
                logger.logFailed(failedMessage, attributes, t);
                if (t instanceof StepFailedException || t instanceof StepErrorException) {
                    throw t;
                }
                if (ErrorCollector.isErrorFailed(t)) {
                    throw new StepErrorException(failedMessage, t)
                }
                throw new StepFailedException(failedMessage, t);
        }
    }

    @CompileStatic
    protected static StringBuilder buildReasonMessage(String message, String reason) {
        StringBuilder failMessage = new StringBuilder(org.apache.commons.lang3.StringUtils.defaultString(message));
        if (StringUtils.isNotEmpty(reason)) {
            failMessage.append(" (Root cause: ");
            failMessage.append(reason);
            failMessage.append(")");
        }
        return failMessage;
    }

    @CompileStatic
    public static runKeyword(Closure closure, FailureHandling flowControl, String errorMessage) {
        try {
            return closure.call();
        } catch (Throwable e) {
            stepFailed(errorMessage, flowControl, e);
        }
    }

    @CompileStatic
    public static runKeyword(Closure closure, FailureHandling flowControl) {
        try {
            return closure.call();
        } catch (Throwable e) {
            stepFailed(e.getMessage(), flowControl, e);
        }
    }

    @CompileStatic
    public static int runKeywordAndReturnInt(Closure closure, FailureHandling flowControl, String errorMessage) {
        try {
            return (int) closure.call();
        } catch (Throwable e) {
            stepFailed(errorMessage, flowControl, e);
        }
        return -1;
    }
}
