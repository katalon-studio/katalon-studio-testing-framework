package com.kms.katalon.core.webservice.common;

import com.kms.katalon.core.testobject.RequestObject;
import com.kms.katalon.core.testobject.ResponseObject;

public interface Requestor {

    public ResponseObject send(RequestObject request) throws Exception;
}
