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

@Action(value = "verifyNotMatch")
public class VerifyNotMatchKeyword extends AbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return SupportLevel.BUITIN
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        String actualText = (String) params[0]
        String expectedText = (String) params[1]
        boolean isRegex = (boolean) params[2]
        FailureHandling flowControl = (FailureHandling)(params.length > 3 && params[3] instanceof FailureHandling ? params[3] : RunConfiguration.getDefaultFailureHandling())
        return verifyNotMatch(actualText,expectedText,isRegex,flowControl)
    }

    @CompileStatic
    public boolean verifyNotMatch(String actualText, String expectedText, boolean isRegex, FailureHandling flowControl) throws StepFailedException {
        String regularExpressionLog = ((isRegex) ? " using regular expression" : "")
        return KeywordMain.runKeyword({
            logger.logDebug(MessageFormat.format(StringConstants.KW_LOG_INFO_MATCHING_ACTUAL_TXT_W_EXPECTED_VAL, actualText, expectedText, regularExpressionLog))
            if (KeywordHelper.match(actualText, expectedText, isRegex)) {
                throw new StepFailedException(MessageFormat.format(StringConstants.KW_MSG_TXTS_MATCHED_BUT_EXPECTED_UNMATCHED, actualText, expectedText, regularExpressionLog))
                return false
            } else {
                logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_TXTS_UNMATCHED, actualText, expectedText, regularExpressionLog))
                return true
            }
        }, flowControl, MessageFormat.format(StringConstants.KW_MSG_CANNOT_VERIFY_TXTS_ARE_UNMATCHED, actualText, expectedText, regularExpressionLog))
    }
}
