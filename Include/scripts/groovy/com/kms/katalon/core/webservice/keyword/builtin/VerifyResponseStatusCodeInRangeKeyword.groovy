package com.kms.katalon.core.webservice.keyword.builtin

import java.text.MessageFormat

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.KeywordMain
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.webservice.constants.CoreWebserviceMessageConstants
import com.kms.katalon.core.webservice.helper.WebServiceCommonHelper
import com.kms.katalon.core.webservice.keyword.internal.WebserviceAbstractKeyword

import groovy.transform.CompileStatic

@Action(value = "verifyResponseStatusCodeInRange")
public class VerifyResponseStatusCodeInRangeKeyword extends WebserviceAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        ResponseObject response = (ResponseObject) params[0]
        int fromStatusCode = (int) params[1]
        int toStatusCode = (int) params[2]
        FailureHandling flowControl = getFailureHandling(params, 3)
        return verifyResponseStatusCodeInRange(response, fromStatusCode, toStatusCode, flowControl)
    }

    @CompileStatic
    public boolean verifyResponseStatusCodeInRange(ResponseObject responseObject, int fromStatusCode, int toStatusCode, FailureHandling flowControl) throws StepFailedException {
        return KeywordMain.runKeyword({
            WebServiceCommonHelper.checkResponseObject(responseObject)
            int statusCode = responseObject.getStatusCode()
            return verifyStatusCode(
                statusCode >= fromStatusCode && statusCode <= toStatusCode, 
                MessageFormat.format(CoreWebserviceMessageConstants.KW_LOG_FAILED_STATUS_CODE_DOES_NOT_MATCH_IN_EXPECTED_RANGE, fromStatusCode, toStatusCode, statusCode), 
                flowControl)
        }, flowControl, CoreWebserviceMessageConstants.KW_MSG_UNABLE_TO_VERIFY_RESPONSE_STATUS_CODE)
    }

    @CompileStatic
    private boolean verifyStatusCode(boolean isEqual, String message, FailureHandling flowControl) throws StepFailedException {
        if (isEqual) {
            logger.logPassed(CoreWebserviceMessageConstants.KW_LOG_PASSED_VERIFY_RESPONSE_STATUS_CODE_SUCCESSFULLY)
        } else {
            KeywordMain.stepFailed(message, flowControl, null, null)
        }
        return isEqual
    }
}
