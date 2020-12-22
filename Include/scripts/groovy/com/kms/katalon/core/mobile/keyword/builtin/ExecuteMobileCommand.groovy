package com.kms.katalon.core.mobile.keyword.builtin

import java.text.MessageFormat

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.mobile.constants.StringConstants
import com.kms.katalon.core.mobile.keyword.internal.MobileAbstractKeyword
import com.kms.katalon.core.mobile.keyword.internal.MobileKeywordMain
import com.kms.katalon.core.model.FailureHandling

import groovy.transform.CompileStatic
import io.appium.java_client.AppiumDriver

@Action(value = "executeMobileCommand")
public class ExecuteMobileCommand extends MobileAbstractKeyword {
    
    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        String command = params[0]
        Map args = (Map) params[1]
        FailureHandling flowControl = (FailureHandling)(params.length > 2 && params[2] instanceof FailureHandling ? params[2] : RunConfiguration.getDefaultFailureHandling())
        return executeMobileCommand(command, args, flowControl)
    }

    @CompileStatic
    public Object executeMobileCommand(String command, Map args, FailureHandling flowControl) throws StepFailedException {
        MobileKeywordMain.runKeyword({
            AppiumDriver driver = getAnyAppiumDriver()
            String context = driver.getContext()
            try {
                internalSwitchToNativeContext(driver)
                Object result = driver.executeScript(command, args)
                logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_EXECUTE_MOBILE_COMMAND, command))
                return result
            } finally {
                driver.context(context)
            }
        }, flowControl, true, MessageFormat.format(StringConstants.KW_LOG_FAILED_TO_EXECUTE_MOBILE_COMMAND, command))
    }
}
