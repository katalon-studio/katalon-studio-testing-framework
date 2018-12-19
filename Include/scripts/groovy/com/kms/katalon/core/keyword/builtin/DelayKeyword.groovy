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

@Action(value = "delay")
public class DelayKeyword extends AbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return SupportLevel.BUITIN
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        Object second = (Object) params[0]
        FailureHandling flowControl = (FailureHandling)(params.length > 1 && params[1] instanceof FailureHandling ? params[1] : RunConfiguration.getDefaultFailureHandling())
        delay(second,flowControl)
    }

    @CompileStatic
    public void delay(Object second, FailureHandling flowControl) throws StepFailedException {
        KeywordMain.runKeyword({
            logger.logDebug(MessageFormat.format(StringConstants.KW_LOG_INFO_DELAYING_BROWSER_IN_SEC, second))
            int secondIntValue = (int) Integer.valueOf(String.valueOf(second))
            try {
                Thread.sleep(secondIntValue * 1000)
            } catch (InterruptedException e) {
                // Thread is interrupted, do nothing
            }
            logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_DELAYED_SEC, second))
        }, flowControl, StringConstants.KW_MSG_CANNOT_DELAY_BROWSER)
    }
}
