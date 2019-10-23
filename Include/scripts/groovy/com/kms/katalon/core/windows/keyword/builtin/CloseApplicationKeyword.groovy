package com.kms.katalon.core.windows.keyword.builtin

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.exception.StepFailedException;
import com.kms.katalon.core.keyword.internal.AbstractKeyword;
import com.kms.katalon.core.keyword.internal.KeywordMain
import com.kms.katalon.core.keyword.internal.SupportLevel;
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.model.FailureHandling;
import com.kms.katalon.core.windows.driver.WindowsDriverFactory
import com.kms.katalon.core.windows.keyword.helper.WindowsActionHelper

import io.appium.java_client.windows.WindowsDriver

@Action(value = "closeApplication")
public class CloseApplicationKeyword extends AbstractKeyword {

    private KeywordLogger logger = KeywordLogger.getInstance(CloseApplicationKeyword.class)

    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return SupportLevel.NOT_SUPPORT
    }

    @Override
    public Object execute(Object ...params) {
        FailureHandling flowControl = (FailureHandling)(params.length > 0 && params[0] instanceof FailureHandling ? params[0] : RunConfiguration.getDefaultFailureHandling())
        closeApplication(flowControl)
    }

    public void closeApplication(FailureHandling flowControl) throws StepFailedException {
        KeywordMain.runKeyword({
            WindowsDriver windowsDriver = WindowsDriverFactory.getWindowsDriver()
            if (windowsDriver == null) {
                logger.logWarning('No application opens')
                return
            }
            WindowsActionHelper.create(WindowsDriverFactory.getWindowsSession()).closeApp()
            logger.logPassed('Fire Close event sucessfully.')
        }, flowControl)
    }
}
