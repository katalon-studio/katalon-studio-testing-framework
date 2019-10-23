package com.kms.katalon.core.mobile.keyword.builtin

import java.time.Duration

import org.codehaus.groovy.transform.tailrec.VariableReplacedListener.*

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.mobile.constants.StringConstants
import com.kms.katalon.core.mobile.keyword.*
import com.kms.katalon.core.mobile.keyword.internal.MobileAbstractKeyword
import com.kms.katalon.core.mobile.keyword.internal.MobileKeywordMain
import com.kms.katalon.core.model.FailureHandling

import groovy.transform.CompileStatic
import io.appium.java_client.AppiumDriver
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.AndroidKeyCode
import io.appium.java_client.ios.IOSDriver

@Action(value = "pressHome")
public class PressHomeKeyword extends MobileAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        FailureHandling flowControl = (FailureHandling)(params.length > 0 && params[0] instanceof FailureHandling ? params[0] : RunConfiguration.getDefaultFailureHandling())
        pressHome(flowControl)
    }

    @CompileStatic
    public void pressHome(FailureHandling flowControl) throws StepFailedException {
        MobileKeywordMain.runKeyword({
            AppiumDriver<?> driver = getAnyAppiumDriver()
            String context = driver.getContext()
            try {
                if (driver instanceof AndroidDriver) {
                    internalSwitchToNativeContext(driver)
                    ((AndroidDriver)driver).pressKeyCode(AndroidKeyCode.HOME)
                } else {
                     ((IOSDriver) driver).runAppInBackground(Duration.ofSeconds(-1))
                }
                logger.logPassed(StringConstants.KW_LOG_PASSED_HOME_BTN_PRESSED)
            } finally {
                driver.context(context)
            }
        }, flowControl, true, StringConstants.KW_MSG_CANNOT_PRESS_HOME_BTN)
    }
}
