package com.kms.katalon.core.webservice.keyword.builtin

import groovy.transform.CompileStatic
import java.text.MessageFormat
import java.util.regex.Pattern

import org.apache.commons.lang3.ObjectUtils

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

@Action(value = "verifyElementPropertyValue")
public class VerifyElementPropertyValueKeyword extends WebserviceAbstractKeyword {

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
        Object value = (Object) params[2]
        FailureHandling flowControl = (FailureHandling)(params.length > 3 && params[3] instanceof FailureHandling ? params[3] : RunConfiguration.getDefaultFailureHandling())
        return verifyElementPropertyValue(response,locator,value,flowControl)
    }

    @CompileStatic
    public boolean verifyElementPropertyValue(ResponseObject response, String locator, Object value, FailureHandling flowControl) throws StepErrorException {
        Object object = KeywordMain.runKeyword({
            WebServiceCommonHelper.checkResponseObject(response)
            Object retValue = response.isXmlContentType() ?
                    WebServiceCommonHelper.parseAndGetPropertyValueForXml(locator, response.getResponseBodyContent()) :
                    WebServiceCommonHelper.parseAndGetPropertyValueForJson(locator, response.getResponseBodyContent())
            
            boolean isEqual = String.valueOf(value).equals(String.valueOf(retValue))
            if (!isEqual) {
                KeywordMain.stepFailed(MessageFormat.format(StringConstants.KW_LOG_FAILED_ELEMENT_PROP_VAL_NOT_EQUAL, [value, retValue.toString()] as Object[]), flowControl, null, null)
            } else {
                logger.logPassed(StringConstants.KW_LOG_PASSED_VERIFY_ELEMENT_PROPERTY_VALUE)
            }
            
            return isEqual
        }, flowControl, StringConstants.KW_LOG_FAILED_CANNOT_VERIFY_ELEMENT_PROPERTY_VALUE)
        if (object != null) {
            return Boolean.valueOf(object.toString())
        }
        return false
    }
}
