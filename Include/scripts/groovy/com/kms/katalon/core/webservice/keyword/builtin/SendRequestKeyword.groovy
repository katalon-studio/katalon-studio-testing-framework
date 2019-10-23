package com.kms.katalon.core.webservice.keyword.builtin

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.keyword.internal.KeywordMain
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.webservice.common.HarLogger
import com.kms.katalon.core.webservice.common.ServiceRequestFactory
import com.kms.katalon.core.webservice.constants.StringConstants
import com.kms.katalon.core.webservice.helper.WebServiceCommonHelper
import com.kms.katalon.core.webservice.keyword.internal.WebserviceAbstractKeyword
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import groovy.transform.CompileStatic

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
                HarLogger harLogger = new HarLogger()
                harLogger.initHarFile()
                ResponseObject responseObject = ServiceRequestFactory.getInstance(request).send(request)
                harLogger.logHarFile(request, responseObject, RunConfiguration.getReportFolder())
                
                logger.logPassed(StringConstants.KW_LOG_PASSED_SEND_REQUEST_SUCCESS)
                return responseObject
            }, flowControl, StringConstants.KW_LOG_FAILED_CANNOT_SEND_REQUEST)
            if (object instanceof ResponseObject) {
                return (ResponseObject) object
            }
            return null
    }
}
