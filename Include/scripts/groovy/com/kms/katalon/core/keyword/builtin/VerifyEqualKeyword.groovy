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

@Action(value = "verifyEqual")
public class VerifyEqualKeyword extends AbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return SupportLevel.BUITIN
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        Object actualObject = (Object) params[0]
        Object expectedObject = (Object) params[1]
        FailureHandling flowControl = (FailureHandling)(params.length > 2 && params[2] instanceof FailureHandling ? params[2] : RunConfiguration.getDefaultFailureHandling())
        return verifyEqual(actualObject,expectedObject,flowControl)
    }

    @CompileStatic
    public boolean verifyEqual(Object actualObject, Object expectedObject, FailureHandling flowControl) throws StepFailedException, StepErrorException {
        return KeywordMain.runKeyword({
            logger.logDebug(MessageFormat.format(StringConstants.COMM_LOG_INFO_COMPARE_ACTUAL_W_EXPECTED, [actualObject, expectedObject] as Object[]))
            boolean isEqual
            if (NumberUtils.isNumber(String.valueOf(actualObject)) && NumberUtils.isNumber(String.valueOf(expectedObject))) {
                isEqual = KeywordHelper.comparingNumberObject(actualObject, expectedObject) == 0
            } else {
                isEqual = actualObject == expectedObject
            }
            if (!isEqual) {
                throw new StepFailedException(MessageFormat.format(StringConstants.KW_MSG_OBJECTS_ARE_NOT_EQUAL, [actualObject, expectedObject] as Object[]))
            } else {
                logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_OBJECTS_ARE_EQUAL, [actualObject, expectedObject] as Object[]))
            }
            return isEqual
        }, flowControl, MessageFormat.format(StringConstants.KW_MSG_CANNOT_VERIFY_OBJECTS_ARE_EQUAL, [actualObject, expectedObject] as Object[]))
    }
}
