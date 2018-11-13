package com.kms.katalon.core.mobile.keyword.builtin

import groovy.transform.CompileStatic
import io.appium.java_client.MobileElement

import java.text.MessageFormat

import org.apache.commons.lang3.StringUtils
import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.helper.KeywordHelper
import com.kms.katalon.core.keyword.internal.KeywordMain
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.mobile.constants.CoreMobileMessageConstants
import com.kms.katalon.core.mobile.constants.StringConstants
import com.kms.katalon.core.mobile.keyword.internal.MobileAbstractKeyword
import com.kms.katalon.core.mobile.keyword.internal.MobileKeywordMain
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.selenium.util.SeleniumKeysUtil

@Action(value = "sendKeys")
public class SendKeysKeyword extends MobileAbstractKeyword {
    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        TestObject to = getTestObject(params[0])
        String strokeKeys = (String) params[1]
        FailureHandling flowControl = (FailureHandling)(params.length > 2 && params[2] instanceof FailureHandling ? params[2] : RunConfiguration.getDefaultFailureHandling())
        sendKeys(to, strokeKeys, flowControl)
    }

    @CompileStatic
    public void sendKeys(TestObject to, String strokeKeys, FailureHandling flowControl) throws StepFailedException {
        String readableText = SeleniumKeysUtil.getReadableText(strokeKeys)
        MobileKeywordMain.runKeyword({
            KeywordHelper.checkTestObjectParameter(to)
            int timeout = KeywordHelper.checkTimeout(RunConfiguration.getTimeOut())
            WebElement element = findElement(to, timeout * 1000)
            if (element == null) {
                MobileKeywordMain.stepFailed(MessageFormat.format(StringConstants.KW_LOG_FAILED_ELEMENT_X_EXISTED, to.getObjectId()), flowControl, null, true)
            }
            element.sendKeys(strokeKeys);
            logger.logPassed(MessageFormat.format(CoreMobileMessageConstants.KW_LOG_SEND_KEYS_X_ON_ELEMENT_Y_SUCCESSFULLY,
                    StringUtils.defaultString(readableText)
                    , StringUtils.defaultString(to.getObjectId())))
        }, flowControl, true, MessageFormat.format(CoreMobileMessageConstants.KW_MSG_CANNOT_SEND_KEYS_X_ON_ELEMENT_Y,
        StringUtils.defaultString(readableText)
        , StringUtils.defaultString(to.getObjectId())))
    }
}
