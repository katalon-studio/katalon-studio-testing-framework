package com.kms.katalon.core.keyword.builtin

import groovy.transform.CompileStatic

import java.text.MessageFormat

import org.apache.commons.lang.math.NumberUtils

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.constants.StringConstants
import com.kms.katalon.core.exception.StepErrorException
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.helper.KeywordHelper
import com.kms.katalon.core.keyword.internal.AbstractKeyword
import com.kms.katalon.core.keyword.internal.KeywordExecutor
import com.kms.katalon.core.keyword.internal.KeywordMain
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.logging.ErrorCollector
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.logging.model.TestStatus
import com.kms.katalon.core.main.TestCaseMain
import com.kms.katalon.core.main.TestResult
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testcase.TestCaseBinding
import com.kms.katalon.core.configuration.RunConfiguration

@Action(value = "callTestCase")
public class CallTestCaseKeyword extends AbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return SupportLevel.BUITIN
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        TestCase calledTestCase = (TestCase) params[0]
        Map<String, Object> binding = (Map<String, Object>) params[1]
        FailureHandling flowControl = (FailureHandling)(params.length > 2 && params[2] instanceof FailureHandling ? params[2] : RunConfiguration.getDefaultFailureHandling())
        return callTestCase(calledTestCase,binding,flowControl)
    }

    @CompileStatic
    public Object callTestCase(TestCase calledTestCase, Map<String, Object> binding, FailureHandling flowControl) throws Exception {
        KeywordMain.runKeyword({
            List<Throwable> parentErrors = ErrorCollector.getCollector().getCoppiedErrors()
            try {
                logger.logDebug(StringConstants.KW_LOG_INFO_CHECKING_CALLED_TC)
                if (calledTestCase == null) {
                    throw new IllegalArgumentException(StringConstants.KW_EXC_CALLED_TC_IS_NULL)
                }

                logger.logDebug(MessageFormat.format(StringConstants.KW_LOG_INFO_STARTING_TO_CALL_TC, calledTestCase.getTestCaseId()))
                TestResult result = TestCaseMain.runTestCase(calledTestCase.getTestCaseId(), new TestCaseBinding(
                        calledTestCase.getTestCaseId(), binding), flowControl, false, false)
                switch (result.getTestStatus().getStatusValue()) {
                    case TestStatus.TestStatusValue.FAILED:
                        throw new StepFailedException(MessageFormat.format(StringConstants.KW_MSG_CALL_TC_FAILED, calledTestCase.getTestCaseId()))
                        break
                    case TestStatus.TestStatusValue.ERROR:
                        throw new StepErrorException(MessageFormat.format(StringConstants.KW_MSG_CALL_TC_X_FAILED_BECAUSE_OF_ERROR, calledTestCase.getTestCaseId()))
                        break
                    case TestStatus.TestStatusValue.PASSED:
                        logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_CALL_TC_X_SUCCESSFULLY, calledTestCase.getTestCaseId()))
                        break
                    default:
                        break
                }
                return result.getScriptResult()
            } finally {
                if (flowControl == FailureHandling.OPTIONAL) {
                    ErrorCollector.getCollector().clearErrors()
                    ErrorCollector.getCollector().getErrors().addAll(parentErrors)
                }
            }
        }, flowControl, (calledTestCase != null) ? MessageFormat.format(StringConstants.KW_MSG_CANNOT_CALL_TC_W_ID_X, calledTestCase.getTestCaseId())
        : StringConstants.KW_MSG_CANNOT_CALL_TC)
    }
}
