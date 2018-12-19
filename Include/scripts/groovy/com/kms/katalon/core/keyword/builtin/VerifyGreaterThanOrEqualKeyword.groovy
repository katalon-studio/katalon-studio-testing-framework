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

@Action(value = "verifyGreaterThanOrEqual")
public class VerifyGreaterThanOrEqualKeyword extends AbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return SupportLevel.BUITIN
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        Object actualNumber = (Object) params[0]
        Object expectedNumber = (Object) params[1]
        FailureHandling flowControl = (FailureHandling)(params.length > 2 && params[2] instanceof FailureHandling ? params[2] : RunConfiguration.getDefaultFailureHandling())
        return verifyGreaterThanOrEqual(actualNumber,expectedNumber,flowControl)
    }

    @CompileStatic
    public boolean verifyGreaterThanOrEqual(Object actualNumber, Object expectedNumber, FailureHandling flowControl) throws StepFailedException {
        return KeywordMain.runKeyword({
            boolean isGreaterThanOrEqual = KeywordHelper.comparingNumberObject(actualNumber, expectedNumber) >= 0
            if (isGreaterThanOrEqual) {
                logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_ACTUAL_NUM_IS_GT_OR_EQ_TO_EXPECTED_NUM, [actualNumber, expectedNumber] as Object[]))
            } else {
                throw new StepFailedException(MessageFormat.format(StringConstants.KW_MSG_ACTUAL_NUM_IS_NOT_GT_OR_EQ_TO_EXPECTED_NUM, [actualNumber, expectedNumber] as Object[]))
            }
            return isGreaterThanOrEqual
        }, flowControl, MessageFormat.format(StringConstants.KW_MSG_CANNOT_VERIFY_NUMS_ARE_GT_OR_EQ, [actualNumber, expectedNumber] as Object[]))
    }
}
