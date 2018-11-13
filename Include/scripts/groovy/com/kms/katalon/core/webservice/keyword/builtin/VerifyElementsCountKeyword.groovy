package com.kms.katalon.core.webservice.keyword.builtin

import groovy.transform.CompileStatic
import java.text.MessageFormat
import java.util.regex.Pattern
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.exception.StepErrorException
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.BuiltinKeywords
import com.kms.katalon.core.keyword.internal.KeywordExecutor
import com.kms.katalon.core.keyword.internal.KeywordMain
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.webservice.common.ServiceRequestFactory
import com.kms.katalon.core.webservice.constants.StringConstants
import com.kms.katalon.core.webservice.helper.WebServiceCommonHelper
import com.kms.katalon.core.webservice.keyword.internal.WebserviceAbstractKeyword
import com.kms.katalon.core.configuration.RunConfiguration

@Action(value = "verifyElementsCount")
public class VerifyElementsCountKeyword extends WebserviceAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        ResponseObject response = (ResponseObject) params[0]
        String locator = (String) params[1]
        int count = (int) params[2]
        FailureHandling flowControl = (FailureHandling)(params.length > 3 && params[3] instanceof FailureHandling ? params[3] : RunConfiguration.getDefaultFailureHandling())
        return verifyElementsCount(response,locator,count,flowControl)
    }

    @CompileStatic
    public  boolean verifyElementsCount(ResponseObject response, String locator, int count, FailureHandling flowControl) throws Exception {
        Object object = KeywordMain.runKeyword({
            WebServiceCommonHelper.checkResponseObject(response)
            Object retValue = response.isXmlContentType() ?
                    WebServiceCommonHelper.parseAndExecuteExpressionForXml(locator, "size()", response.getResponseBodyContent())
                    : WebServiceCommonHelper.parseAndExecuteExpressionForJson(locator, "size()", response.getResponseBodyContent())
            int actualValue = Integer.parseInt(String.valueOf(retValue))
            boolean isEqual = (count == actualValue)
            if (!isEqual) {
                KeywordMain.stepFailed(MessageFormat.format(StringConstants.KW_LOG_FAILED_ELEMENT_COUNT_NOT_EQUAL, [count, actualValue] as Object[]), flowControl, null, null)
            } else {
                logger.logPassed(StringConstants.KW_LOG_PASSED_VERIFY_ELEMENT_COUNT)
            }
            return isEqual
        }, flowControl, StringConstants.KW_LOG_FAILED_CANNOT_VERIFY_ELEMENT_COUNT)
        if (object != null) {
            return Boolean.valueOf(object.toString())
        }
        return false
    }
}
