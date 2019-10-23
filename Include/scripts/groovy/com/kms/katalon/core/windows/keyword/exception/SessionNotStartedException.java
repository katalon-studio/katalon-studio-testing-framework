package com.kms.katalon.core.windows.keyword.exception;

import com.kms.katalon.core.exception.KatalonRuntimeException;

public class SessionNotStartedException extends KatalonRuntimeException {

    private static final long serialVersionUID = -4277101456624531695L;

    public SessionNotStartedException(String message) {
        super(message);
    }
}
