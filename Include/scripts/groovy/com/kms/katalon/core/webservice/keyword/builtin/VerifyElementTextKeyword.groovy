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

@Action(value = "verifyElementText")
public class VerifyElementTextKeyword extends WebserviceAbstractKeyword {

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
        String text = (String) params[2]
        FailureHandling flowControl = (FailureHandling)(params.length > 3 && params[3] instanceof FailureHandling ? params[3] : RunConfiguration.getDefaultFailureHandling())
        return verifyElementText(response,locator,text,flowControl)
    }

    @CompileStatic
    public  boolean verifyElementText(ResponseObject response, String locator, String text, FailureHandling flowControl) throws StepFailedException, StepErrorException {
        Object object = KeywordMain.runKeyword({
            WebServiceCommonHelper.checkResponseObject(response)
            Object retValue = response.isXmlContentType() ?
                    WebServiceCommonHelper.parseAndExecuteExpressionForXml(locator, "text()", response.getResponseBodyContent()) :
                    WebServiceCommonHelper.parseAndExecuteExpressionForJson(locator, "text()", response.getResponseBodyContent())
            boolean isEqual = (text.equals(String.valueOf(retValue)))
            if (!isEqual) {
                KeywordMain.stepFailed(MessageFormat.format(StringConstants.KW_LOG_FAILED_ACTUAL_ELEMENT_TEXT_IS, [text, retValue] as Object[]), flowControl, null, null)
            } else {
                logger.logPassed(StringConstants.KW_LOG_PASSED_VERIFY_ELEMENT_TEXT)
            }
            return isEqual
        }, flowControl, StringConstants.KW_LOG_FAILED_CANNOT_VERIFY_ELEMENT_TEXT)
        if (object != null) {
            return Boolean.valueOf(object.toString())
        }
        return false
    }
}
