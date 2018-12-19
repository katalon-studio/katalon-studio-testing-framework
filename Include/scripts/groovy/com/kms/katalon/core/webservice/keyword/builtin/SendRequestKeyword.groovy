package com.kms.katalon.core.webservice.keyword.builtin

import groovy.transform.CompileStatic
import java.text.MessageFormat
import java.util.UUID
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
import com.kms.katalon.core.main.TestCaseExecutor
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.util.BrowserMobProxyManager
import com.kms.katalon.core.util.RequestInformation
import com.kms.katalon.core.webservice.common.ServiceRequestFactory
import com.kms.katalon.core.webservice.constants.StringConstants
import com.kms.katalon.core.webservice.helper.WebServiceCommonHelper
import com.kms.katalon.core.webservice.keyword.internal.WebserviceAbstractKeyword
import com.kms.katalon.core.configuration.RunConfiguration

@Action(value = "sendRequest")
public class SendRequestKeyword extends WebserviceAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        RequestObject request = (RequestObject) params[0]
        FailureHandling flowControl = (FailureHandling)(params.length > 1 && params[1] instanceof FailureHandling ? params[1] : RunConfiguration.getDefaultFailureHandling())
        return sendRequest(request,flowControl)
    }

    @CompileStatic
    public ResponseObject sendRequest(RequestObject request, FailureHandling flowControl) throws Exception {
            Object object = KeywordMain.runKeyword({
                WebServiceCommonHelper.checkRequestObject(request)
                ResponseObject responseObject = null;
                try {
                    BrowserMobProxyManager.newHar()
                    responseObject = ServiceRequestFactory.getInstance(request).send(request)
                } finally {
                    RequestInformation requestInformation = new RequestInformation();
                    requestInformation.setTestObjectId(request.getObjectId());
                    BrowserMobProxyManager.endHar(requestInformation);
                }
                logger.logPassed(StringConstants.KW_LOG_PASSED_SEND_REQUEST_SUCCESS)
                return responseObject
            }, flowControl, StringConstants.KW_LOG_FAILED_CANNOT_SEND_REQUEST)
            if (object instanceof ResponseObject) {
                return (ResponseObject) object
            }
            return null
    }
}
