package com.kms.katalon.core.webservice.exception;

import com.kms.katalon.core.webservice.constants.StringConstants;

public class ResponseSizeLimitException extends WebServiceException {

    private static final long serialVersionUID = 2678876352773217269L;

    public ResponseSizeLimitException() {
        this(StringConstants.MSG_RESPONSE_SIZE_LIMIT_EXCEPTION);
    }

    public ResponseSizeLimitException(String message) {
        super(message);
    }
}
