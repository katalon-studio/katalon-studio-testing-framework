package com.kms.katalon.core.webservice.exception;

public class SendRequestException extends Exception {

    private static final long serialVersionUID = 9198565757425399036L;

    public SendRequestException(Exception cause) {
        super(cause);
    }

    public SendRequestException(String message, Exception cause) {
        super(message, cause);
    }
}
