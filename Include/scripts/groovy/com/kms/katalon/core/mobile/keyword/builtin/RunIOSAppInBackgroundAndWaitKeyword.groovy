package com.kms.katalon.core.mobile.keyword.builtin

import java.time.Duration

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.mobile.constants.StringConstants
import com.kms.katalon.core.mobile.keyword.internal.MobileAbstractKeyword
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.mobile.keyword.internal.MobileKeywordMain
import com.kms.katalon.core.model.FailureHandling

import groovy.transform.CompileStatic
import io.appium.java_client.AppiumDriver
import io.appium.java_client.ios.IOSDriver

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
            AppiumDriver driver = MobileDriverFactory.getDriver()
            if (driver == null) {
                throw new StepFailedException("The application has not been started yet.")
            }
            if (driver instanceof IOSDriver) {
                IOSDriver<?> iosDriver = (IOSDriver) MobileDriverFactory.getDriver()
    
                iosDriver.runAppInBackground(Duration.ofSeconds(seconds))
                logger.logPassed(StringConstants.KW_LOG_PASSED_RUN_IOS_APP_PASSED)
            } else {
                throw new StepFailedException("runIOSAppInBackgroundAndWait keyword only supports for iOS application only")
            }
        }, flowControl, true, StringConstants.KW_MSG_CANNOT_RUN_IOS_APP_IN_BACKGROUND)
    }
}
