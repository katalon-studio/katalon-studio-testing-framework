package com.kms.katalon.core.mobile.keyword.builtin;

import groovy.transform.CompileStatic

import java.text.MessageFormat

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.helper.KeywordHelper
import com.kms.katalon.core.keyword.internal.KeywordMain
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.mobile.constants.CoreMobileMessageConstants;
import com.kms.katalon.core.mobile.constants.StringConstants
import com.kms.katalon.core.mobile.keyword.internal.MobileAbstractKeyword
import com.kms.katalon.core.mobile.keyword.internal.MobileKeywordMain
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject

@Action(value = "verifyElementText")
public class VerifyElementTextKeyword extends MobileAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        TestObject to = getTestObject(params[0])
        String expectedText = (String) params[1]
        FailureHandling flowControl = (FailureHandling)(params.length > 2 && params[2] instanceof FailureHandling ? params[2] : RunConfiguration.getDefaultFailureHandling())
        return verifyElementText(to, expectedText, flowControl)
    }

    @CompileStatic
    public boolean verifyElementText(TestObject to, String expectedText, FailureHandling flowControl) throws StepFailedException {
        return MobileKeywordMain.runKeyword({
            KeywordHelper.checkTestObjectParameter(to)
            int timeout = KeywordHelper.checkTimeout(RunConfiguration.getTimeOut())
            WebElement element = findElement(to, timeout * 1000)
            if (element == null) {
                MobileKeywordMain.stepFailed(MessageFormat.format(StringConstants.KW_LOG_FAILED_ELEMENT_X_EXISTED, to.getObjectId()), flowControl, null, true)
                return false
            }
            String actualText = element.getText()
            boolean textPresent = ObjectUtils.equals(actualText, expectedText)
            if (!textPresent) {
                MobileKeywordMain.stepFailed(
                        MessageFormat.format(CoreMobileMessageConstants.KW_MSG_ACTUAL_ELEMENT_TXT_NOT_MATCHED_EXPECTED_TXT, actualText, expectedText, to.getObjectId()),
                        flowControl, null, true)
            } else {
                logger.logPassed(MessageFormat.format(CoreMobileMessageConstants.KW_LOG_ACTUAL_ELEMENT_TXT_MATCHED_EXPECTED_TXT, to.getObjectId()))
            }
            return textPresent
        }, flowControl, true, MessageFormat.format(CoreMobileMessageConstants.KW_MSG_VERIFY_ELEMENT_TEXT_FAILED, StringUtils.defaultString(to.getObjectId())))
    }
}
