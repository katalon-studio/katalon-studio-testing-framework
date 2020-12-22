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
import io.appium.java_client.touch.offset.PointOption

@Action(value = "doubleTap")
public class DoubleTapKeyword extends MobileAbstractKeyword {

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
        doubleTap(to, timeout, flowControl)
    }

    @CompileStatic
    public void doubleTap(TestObject to, int timeout, FailureHandling flowControl) throws StepFailedException {
        MobileKeywordMain.runKeyword({
            KeywordHelper.checkTestObjectParameter(to)
            timeout = KeywordHelper.checkTimeout(timeout)
            WebElement element = findElement(to, timeout * 1000)

            if (element == null){
                MobileKeywordMain.stepFailed(MessageFormat.format(StringConstants.KW_MSG_OBJ_NOT_FOUND, to.getObjectId()), flowControl, null, true)
                return
            }

            // Exception: org.openqa.selenium.UnsupportedCommandException: touchDoubleTap
//            TouchActions doubleTap = new TouchActions(MobileDriverFactory.getDriver()).doubleTap(element)
//            doubleTap.perform();

            int tapX = element.location.x + 1;
            int tapY = element.location.y + 1;
            TouchAction doubleTap = new TouchAction(MobileDriverFactory.getDriver())
                .press(PointOption.point(tapX, tapY)).release().perform()
                .press(PointOption.point(tapX, tapY)).release().perform();

            logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_DOUBLE_TAPPED_ON_ELEMENT, to.getObjectId()))
        }, flowControl, true, to != null ? MessageFormat.format(StringConstants.KW_MSG_FAILED_TO_DOUBLE_TAP_ON_ELEMENT_X, to.getObjectId()) : StringConstants.KW_MSG_FAILED_TO_TAP_ON_ELEMENT)
    }
}
