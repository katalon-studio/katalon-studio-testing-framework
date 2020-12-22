package com.kms.katalon.core.windows.keyword.builtin

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.AbstractKeyword
import com.kms.katalon.core.keyword.internal.KeywordMain
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.windows.driver.WindowsDriverFactory

@Action(value = "startApplication")
public class StartApplicationKeyword extends AbstractKeyword {

    private KeywordLogger logger = KeywordLogger.getInstance(StartApplicationKeyword.class)

    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return SupportLevel.NOT_SUPPORT
    }

    @Override
    public Object execute(Object ...params) {
        String appFile = (String) params[0]
        FailureHandling flowControl = (FailureHandling)(params.length > 1 && params[1] instanceof FailureHandling ? params[1] : RunConfiguration.getDefaultFailureHandling())
        startApplication(appFile, flowControl)
    }

    public void startApplication(String appFile, FailureHandling flowControl) throws StepFailedException {
        KeywordMain.runKeyword({
            WindowsDriverFactory.startApplication(appFile, "")
            logger.logPassed("The application at location: ${appFile} started.")
        }, flowControl)
    }
}
