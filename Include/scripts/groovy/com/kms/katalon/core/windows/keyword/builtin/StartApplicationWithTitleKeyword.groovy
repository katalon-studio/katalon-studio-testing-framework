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

@Action(value = "startApplicationWithTitle")
public class StartApplicationWithTitleKeyword extends AbstractKeyword {

    private KeywordLogger logger = KeywordLogger.getInstance(StartApplicationKeyword.class)

    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return SupportLevel.NOT_SUPPORT
    }

    @Override
    public Object execute(Object ...params) {
        String appFile = (String) params[0]
        String windowTitle = (String) params[1]
        FailureHandling flowControl = (FailureHandling)(params.length > 2 && params[2] instanceof FailureHandling ? params[2] : RunConfiguration.getDefaultFailureHandling())
        startApplicationWithTitle(appFile, windowTitle, flowControl)
    }

    public void startApplicationWithTitle(String appFile, String windowTitle, FailureHandling flowControl) throws StepFailedException {
        KeywordMain.runKeyword({
            WindowsDriverFactory.startApplication(appFile, windowTitle)
            logger.logPassed("The application at location: ${appFile} started.")
        }, flowControl)
    }
}
