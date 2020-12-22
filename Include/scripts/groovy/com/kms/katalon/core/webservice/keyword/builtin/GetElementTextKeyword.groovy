package com.kms.katalon.core.webservice.keyword.builtin

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepErrorException
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.KeywordMain
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.webservice.keyword.internal.WebserviceAbstractKeyword
import groovy.transform.CompileStatic
import com.kms.katalon.core.webservice.constants.StringConstants
import com.kms.katalon.core.webservice.helper.WebServiceCommonHelper


@Action(value = "getElementText")
class GetElementTextKeyword extends WebserviceAbstractKeyword {

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
        FailureHandling flowControl = (FailureHandling)(params.length > 2 && params[2] instanceof FailureHandling ? params[2] : RunConfiguration.getDefaultFailureHandling())
        return getElementText(response, locator, flowControl)
    }

    @CompileStatic
    public  String getElementText(ResponseObject response, String locator, FailureHandling flowControl) throws StepErrorException, StepFailedException {
        Object retValue = KeywordMain.runKeyword({
            WebServiceCommonHelper.checkResponseObject(response)
            Object retValue = response.isXmlContentType() ?
                    WebServiceCommonHelper.parseAndExecuteExpressionForXml(locator, "text()", response.getResponseBodyContent()) :
                    WebServiceCommonHelper.parseAndExecuteExpressionForJson(locator, "text()", response.getResponseBodyContent())
            return retValue
        }, flowControl, StringConstants.KW_LOG_FAILED_CANNOT_GET_ELEMENT_TEXT)
        return String.valueOf(retValue)
    }
}
