package com.kms.katalon.core.mobile.keyword.builtin

import java.text.MessageFormat

import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.helper.KeywordHelper
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.mobile.constants.StringConstants
import com.kms.katalon.core.mobile.keyword.internal.MobileAbstractKeyword
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.mobile.keyword.internal.MobileKeywordMain
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject

import groovy.transform.CompileStatic
import io.appium.java_client.TouchAction
import io.appium.java_client.touch.LongPressOptions
import io.appium.java_client.touch.offset.ElementOption

@Action(value = "longPress")
public class LongPressKeyword extends MobileAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        TestObject to = getTestObject(params[0])
        int timeout = (int) params[1]
        FailureHandling flowControl = (FailureHandling)(params.length > 2 && params[2] instanceof FailureHandling ? params[2] : RunConfiguration.getDefaultFailureHandling())
        longPress(to, timeout, flowControl)
    }

    @CompileStatic
    public void longPress(TestObject to, int timeout, FailureHandling flowControl) throws StepFailedException {
        MobileKeywordMain.runKeyword({
            KeywordHelper.checkTestObjectParameter(to)
            timeout = KeywordHelper.checkTimeout(timeout)
            WebElement element = findElement(to, timeout * 1000)

            if (element == null){
                MobileKeywordMain.stepFailed(MessageFormat.format(StringConstants.KW_MSG_OBJ_NOT_FOUND, to.getObjectId()), flowControl, null, true)
                return
            }

            TouchAction longPress = new TouchAction(MobileDriverFactory.getDriver())
                    .longPress(LongPressOptions.longPressOptions().withElement(ElementOption.element(element, 1, 1)))
                    .release()
            longPress.perform();

            logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_LONG_PRESSED_ON_ELEMENT, to.getObjectId()))
        }, flowControl, true, to != null ? MessageFormat.format(StringConstants.KW_MSG_FAILED_TO_LONG_PRESS_ON_ELEMENT_X, to.getObjectId()) : StringConstants.KW_MSG_FAILED_TO_LONG_PRESS_ON_ELEMENT)
    }
}
