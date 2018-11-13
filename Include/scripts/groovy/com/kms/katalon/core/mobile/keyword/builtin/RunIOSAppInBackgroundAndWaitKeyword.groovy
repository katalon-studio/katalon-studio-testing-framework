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
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.mobile.keyword.internal.MobileKeywordMain

@Action(value = "runIOSAppInBackgroundAndWait")
public class RunIOSAppInBackgroundAndWaitKeyword extends MobileAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        int seconds = (int) params[0]
        FailureHandling flowControl = (FailureHandling)(params.length > 1 && params[1] instanceof FailureHandling ? params[1] : RunConfiguration.getDefaultFailureHandling())
        runIOSAppInBackgroundAndWait(seconds,flowControl)
    }

    @CompileStatic
    public void runIOSAppInBackgroundAndWait(int seconds, FailureHandling flowControl) throws StepFailedException {
        MobileKeywordMain.runKeyword({
            AppiumDriver<?> driver = MobileDriverFactory.getDriver()
            String osVersion = MobileDriverFactory.getDeviceOSVersion()
            int majorversion = Integer.parseInt(osVersion.split("\\.")[0])
            if (majorversion >= 8) {
                String command = String.format("UIATarget.localTarget().deactivateAppForDuration(%d);", (int)(seconds/2))
                driver.executeScript(command + command)
            } else {
                String command = String.format("UIATarget.localTarget().deactivateAppForDuration(%d);", seconds)
                driver.executeScript(command)
            }
            logger.logPassed(StringConstants.KW_LOG_PASSED_RUN_IOS_APP_PASSED)
        }, flowControl, true, StringConstants.KW_MSG_CANNOT_RUN_IOS_APP_IN_BACKGROUND)
    }
}
