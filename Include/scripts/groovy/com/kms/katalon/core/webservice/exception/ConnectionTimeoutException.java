package com.kms.katalon.core.webservice.exception;

import com.kms.katalon.core.webservice.constants.StringConstants;

public class ConnectionTimeoutException extends WebServiceException {

    private static final long serialVersionUID = -872039193041187528L;

    private Throwable coreException;

    public ConnectionTimeoutException() {
        this(StringConstants.MSG_CONNECTION_TIMEOUT_EXCEPTION);
    }

    public ConnectionTimeoutException(String message) {
        this(message, null);
    }

    public ConnectionTimeoutException(Throwable exception) {
        this(StringConstants.MSG_CONNECTION_TIMEOUT_EXCEPTION, exception);
    }

    public ConnectionTimeoutException(String message, Throwable exception) {
        super(message);
        this.coreException = exception;
    }

    public Throwable getCoreException() {
        return coreException;
    }

    public void setCoreException(Throwable coreException) {
        this.coreException = coreException;
    }
}
