package com.kms.katalon.core.webservice.exception;

public class WebServiceException extends Exception {

    private static final long serialVersionUID = 1L;

    public WebServiceException(String message) {
        super(message);
    }

    public WebServiceException(Exception e) {
        super(e);
    }
}
