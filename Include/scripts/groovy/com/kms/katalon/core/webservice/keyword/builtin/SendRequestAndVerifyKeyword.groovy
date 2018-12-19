package com.kms.katalon.core.webservice.keyword.builtin

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepErrorException
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.KeywordMain
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.logging.model.TestStatus
import com.kms.katalon.core.main.TestCaseMain
import com.kms.katalon.core.main.TestResult
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCaseBinding
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.webservice.common.ServiceRequestFactory
import com.kms.katalon.core.webservice.constants.StringConstants
import com.kms.katalon.core.webservice.helper.WebServiceCommonHelper
import com.kms.katalon.core.webservice.keyword.internal.WebserviceAbstractKeyword
import com.kms.katalon.core.webservice.verification.WSResponseManager
import groovy.transform.CompileStatic

@Action(value = "sendRequestAndVerify")
public class SendRequestAndVerifyKeyword extends WebserviceAbstractKeyword {
    
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
        return sendRequestAndVerify(request,flowControl)
    }

    @CompileStatic
    public ResponseObject sendRequestAndVerify(RequestObject request, FailureHandling flowControl) throws Exception {
        Object object = KeywordMain.runKeyword({
            WebServiceCommonHelper.checkRequestObject(request)
            ResponseObject responseObject = ServiceRequestFactory.getInstance(request).send(request)
            
            logger.logDebug(StringConstants.KW_LOG_INFO_VERIFICATION_START)
            String verificationScript = request.getVerificationScript()
            WSResponseManager.getInstance().setRequestObjectId(request.getObjectId())
            WSResponseManager.getInstance().setCurrentResponse(responseObject)
            
            TestResult result = TestCaseMain.runWSVerificationScript(createTestCaseBinding(request),
                verificationScript, flowControl, true)
            switch(result.getTestStatus().getStatusValue()) {
                case TestStatus.TestStatusValue.FAILED:
                    throw new StepFailedException(StringConstants.KW_LOG_VERIFICATION_STEP_FAILED)
                    break
                case TestStatus.TestStatusValue.ERROR:
                    throw new StepErrorException(StringConstants.KW_LOG_VERIFICATION_STEP_FAILED_BECAUSE_OF_ERROR)
                    break
                case TestStatus.TestStatusValue.PASSED:
                    logger.logPassed(StringConstants.KW_LOG_VERIFICATION_STEP_PASSED)
                    break
                default:
                    break
            }
            logger.logPassed(StringConstants.KW_LOG_PASSED_SEND_REQUEST_AND_VERIFY_SUCCESS)
            return responseObject
        }, flowControl, StringConstants.KW_LOG_FAILED_CANNOT_SEND_REQUEST_AND_VERIFY)
        if (object instanceof ResponseObject) {
            return (ResponseObject) object
        }
        return null
    }
    
    private TestCaseBinding createTestCaseBinding(RequestObject requestObject) {
        Map<String, Object> variables = new HashMap<String, Object>()
        TestCaseBinding testCaseBinding = new TestCaseBinding(requestObject.getName(), variables);
        return testCaseBinding;
    }
}
