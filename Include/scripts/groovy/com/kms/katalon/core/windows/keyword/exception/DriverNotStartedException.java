package com.kms.katalon.core.windows.keyword.exception;

import com.kms.katalon.core.exception.KatalonRuntimeException;

public class DriverNotStartedException extends KatalonRuntimeException {

    private static final long serialVersionUID = 9148589293138603088L;

    public DriverNotStartedException(String message) {
        super(message);
    }

}
