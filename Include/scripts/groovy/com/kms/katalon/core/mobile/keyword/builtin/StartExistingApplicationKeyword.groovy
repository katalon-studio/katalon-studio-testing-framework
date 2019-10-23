package com.kms.katalon.core.mobile.keyword.builtin


import java.text.MessageFormat

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.mobile.constants.CoreMobileMessageConstants
import com.kms.katalon.core.mobile.constants.StringConstants
import com.kms.katalon.core.mobile.keyword.internal.MobileAbstractKeyword
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.mobile.keyword.internal.MobileKeywordMain
import com.kms.katalon.core.model.FailureHandling

import groovy.transform.CompileStatic
import io.appium.java_client.AppiumDriver

@Action(value = "startExistingApplication")
public class StartExistingApplicationKeyword extends MobileAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        String appId = (String) params[0]
        FailureHandling flowControl = (FailureHandling)(params.length > 1 && params[1] instanceof FailureHandling ? params[1] : RunConfiguration.getDefaultFailureHandling())
        startApplication(appId, flowControl)
    }

    public void startApplication(String appId, FailureHandling flowControl) throws StepFailedException {
        MobileKeywordMain.runKeyword({
            logger.logDebug(MessageFormat.format(CoreMobileMessageConstants.KW_LOG_INFO_STARTING_APP_WITH_ID, appId))
            AppiumDriver<?> driver = MobileDriverFactory.startMobileDriver(appId)
            driver.activateApp(appId)
            logger.logPassed(MessageFormat.format(CoreMobileMessageConstants.KW_LOG_INFO_STARTING_APP_WITH_ID, appId))
        }, flowControl, false, MessageFormat.format(CoreMobileMessageConstants.KW_MSG_UNABLE_TO_START_APP_WITH_ID, appId))
    }  
}
