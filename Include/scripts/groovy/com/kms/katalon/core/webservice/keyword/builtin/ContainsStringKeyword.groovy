package com.kms.katalon.core.webservice.keyword.builtin

import groovy.transform.CompileStatic

import java.text.MessageFormat
import java.util.regex.Pattern

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.KeywordMain
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.webservice.constants.StringConstants
import com.kms.katalon.core.webservice.helper.WebServiceCommonHelper
import com.kms.katalon.core.webservice.keyword.internal.WebserviceAbstractKeyword

@Action(value = "containsString")
public class ContainsStringKeyword extends WebserviceAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        ResponseObject response = (ResponseObject) params[0]
        String string = (String) params[1]
        boolean useRegex = (boolean) params[2]
        FailureHandling flowControl = (FailureHandling)(params.length > 3 && params[3] instanceof FailureHandling ? params[3] : RunConfiguration.getDefaultFailureHandling())
        return containsString(response,string,useRegex,flowControl)
    }

    @CompileStatic
    public boolean containsString(ResponseObject response, String string, boolean useRegex, FailureHandling flowControl) throws StepFailedException {
        Object object = KeywordMain.runKeyword({
            WebServiceCommonHelper.checkResponseObject(response)
            WebServiceCommonHelper.checkResponseObjectContent(response)
            boolean isMatch = false
            String responseText = response.getResponseText();
            if (useRegex) {
                Pattern p = Pattern.compile(string, Pattern.DOTALL)
                isMatch = p.matcher(responseText).matches()
            } else {
                isMatch = (string != null && responseText.contains(string))
            }
            if (isMatch) {
                logger.logPassed(StringConstants.KW_LOG_PASSED_CONTAIN_STRING)
            } else {
                KeywordMain.stepFailed(MessageFormat.format(StringConstants.KW_STR_NOT_FOUND_IN_RES, [string, response.getResponseText()] as Object[]), flowControl, null, null)
            }
            return isMatch
        }, flowControl, StringConstants.KW_LOG_FAILED_CONTAIN_STRING)
        if (object != null) {
            return Boolean.valueOf(object.toString())
        }
        return false
    }
}
