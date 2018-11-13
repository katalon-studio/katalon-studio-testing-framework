package com.kms.katalon.core.mobile.keyword.builtin

import groovy.transform.CompileStatic
import io.appium.java_client.AppiumDriver
import io.appium.java_client.MobileElement
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.AndroidKeyCode
import io.appium.java_client.ios.IOSDriver
import io.appium.java_client.remote.HideKeyboardStrategy

import java.text.MessageFormat
import java.util.concurrent.TimeUnit

import org.apache.commons.io.FileUtils
import org.apache.commons.lang.StringUtils
import org.codehaus.groovy.transform.tailrec.VariableReplacedListener.*
import org.openqa.selenium.Dimension
import org.openqa.selenium.OutputType
import org.openqa.selenium.Point
import org.openqa.selenium.ScreenOrientation
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebDriverException
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.touch.TouchActions
import org.openqa.selenium.support.ui.FluentWait

import com.google.common.base.Function
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.helper.KeywordHelper
import com.kms.katalon.core.keyword.BuiltinKeywords
import com.kms.katalon.core.keyword.internal.KeywordExecutor
import com.kms.katalon.core.keyword.internal.KeywordMain
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.mobile.constants.StringConstants
import com.kms.katalon.core.mobile.helper.MobileCommonHelper
import com.kms.katalon.core.mobile.helper.MobileDeviceCommonHelper
import com.kms.katalon.core.mobile.helper.MobileElementCommonHelper
import com.kms.katalon.core.mobile.helper.MobileGestureCommonHelper
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.mobile.keyword.*
import com.kms.katalon.core.mobile.keyword.internal.MobileAbstractKeyword
import com.kms.katalon.core.mobile.keyword.internal.MobileKeywordMain

@Action(value = "hideKeyboard")
public class HideKeyboardKeyword extends MobileAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        FailureHandling flowControl = (FailureHandling)(params.length > 0 && params[0] instanceof FailureHandling ? params[0] : RunConfiguration.getDefaultFailureHandling())
        hideKeyboard(flowControl)
    }

    public void hideKeyboard(FailureHandling flowControl) throws StepFailedException {
        MobileKeywordMain.runKeyword({
            AppiumDriver<?> driver = getAnyAppiumDriver()
            String context = driver.getContext()
            try {
                internalSwitchToNativeContext(driver)
                try {
                    driver.hideKeyboard()
                } catch (WebDriverException e) {
                    if (!(e.getMessage().startsWith(StringConstants.APPIUM_DRIVER_ERROR_JS_FAILED) && driver instanceof IOSDriver<?>)) {
                        throw e
                    }
                    // default hide keyboard strategy (tap outside) failed on iOS, use "Done" button
                    IOSDriver<?> iosDriver = (IOSDriver<?>) driver
                    iosDriver.hideKeyboard(HideKeyboardStrategy.PRESS_KEY, "Done")
                }
                logger.logPassed(StringConstants.KW_LOG_PASSED_HIDE_KEYBOARD)
            } finally {
                driver.context(context)
            }
        }, flowControl,true, StringConstants.KW_MSG_CANNOT_HIDE_KEYBOARD)
    }
}
