package com.kms.katalon.core.webservice.keyword.builtin

import com.kms.katalon.core.webservice.constants.StringConstants
import com.kms.katalon.core.webservice.helper.WebServiceCommonHelper
import com.kms.katalon.core.webservice.keyword.internal.WebserviceAbstractKeyword

import groovy.transform.CompileStatic

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.exception.StepErrorException
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.KeywordMain
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ResponseObject

@Action(value = "getResponseStatusCode")
public class GetResponseStatusCodeKeyword extends WebserviceAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        ResponseObject response = (ResponseObject) params[0]
        FailureHandling flowControl = getFailureHandling(params, 1)
        return getResponseStatusCode(response, flowControl)
    }

    @CompileStatic
    public int getResponseStatusCode(ResponseObject responseObject, FailureHandling flowControl) throws StepErrorException, StepFailedException {
        Object retValue = KeywordMain.runKeyword({
            WebServiceCommonHelper.checkResponseObject(responseObject)
            int statusCode = responseObject.getStatusCode()
            return statusCode
        }, flowControl, StringConstants.KW_LOG_FAILED_CANNOT_GET_RESPONSE_STATUS_CODE)
        return (int) retValue
    }
}
