package com.kms.katalon.core.windows.keyword.builtin

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.AbstractKeyword
import com.kms.katalon.core.keyword.internal.KeywordMain
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.WindowsTestObject
import com.kms.katalon.core.windows.driver.WindowsDriverFactory
import com.kms.katalon.core.windows.keyword.helper.WindowsActionHelper
import io.appium.java_client.windows.WindowsDriver

@Action(value = "sendKeys")
class SendKeysKeyword extends AbstractKeyword {
    private KeywordLogger logger = KeywordLogger.getInstance(SetTextKeyword.class)

    @Override
    public SupportLevel getSupportLevel(Object... params) {
        return SupportLevel.NOT_SUPPORT;
    }

    @Override
    public Object execute(Object... params) {
        WindowsTestObject testObject = (WindowsTestObject) params[0]
        String strKeys = (String) params[1]
        FailureHandling flowControl = (FailureHandling)(params.length > 2 && params[2] instanceof FailureHandling ? params[2] : RunConfiguration.getDefaultFailureHandling())
        sendKeys(testObject, strKeys, flowControl)
    }
    
    public String sendKeys(WindowsTestObject windowsObject, String strKeys, FailureHandling flowControl) throws StepFailedException {
        return (String) KeywordMain.runKeyword({
            WindowsDriver windowsDriver = WindowsDriverFactory.getWindowsDriver()
            if (windowsDriver == null) {
                KeywordMain.stepFailed("WindowsDriver has not started. Please try Windows.startApplication first.", flowControl)
            }

            WindowsActionHelper.create(WindowsDriverFactory.getWindowsSession()).sendKeys(windowsObject, strKeys)
            logger.logPassed('Keys ' + strKeys + ' are pressed')
        }, flowControl)
    }
}
