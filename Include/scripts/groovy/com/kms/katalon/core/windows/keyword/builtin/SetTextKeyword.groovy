package com.kms.katalon.core.windows.keyword.builtin;

import com.kms.katalon.core.annotation.internal.Action;
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.AbstractKeyword
import com.kms.katalon.core.keyword.internal.KeywordMain
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.windows.constants.StringConstants
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.WindowsTestObject
import com.kms.katalon.core.windows.driver.WindowsDriverFactory
import com.kms.katalon.core.windows.keyword.helper.WindowsActionHelper

import io.appium.java_client.windows.WindowsDriver
import org.openqa.selenium.WebElement

@Action(value = "setText")
public class SetTextKeyword extends AbstractKeyword {
    private KeywordLogger logger = KeywordLogger.getInstance(SetTextKeyword.class)
    
        @Override
        public SupportLevel getSupportLevel(Object... params) {
            return SupportLevel.NOT_SUPPORT;
        }

        @Override
        public Object execute(Object ...params) {
            WindowsTestObject testObject = (WindowsTestObject) params[0]
            String text = (String) params[1]
            FailureHandling flowControl = (FailureHandling)(params.length > 2 && params[2] instanceof FailureHandling ? params[2] : RunConfiguration.getDefaultFailureHandling())
            setText(testObject, text, flowControl)
        }

    public String setText(WindowsTestObject testObject, String text, FailureHandling flowControl) throws StepFailedException {
        return (String) KeywordMain.runKeyword({
            WindowsDriver windowsDriver = WindowsDriverFactory.getWindowsDriver()
            if (windowsDriver == null) {
                KeywordMain.stepFailed("WindowsDriver has not started. Please try Windows.startApplication first.", flowControl)
            }

            logger.logDebug(String.format(StringConstants.KW_LOG_INFO_CHECKING_TEST_OBJECT));
            WindowsActionHelper windowsActionHelper = WindowsActionHelper.create(WindowsDriverFactory.getWindowsSession())

            WebElement foundElement = WindowsActionHelper.create(WindowsDriverFactory.getWindowsSession()).findElement(testObject)
            if (!foundElement != null) {
                logger.logDebug(String.format("Clearing text of object '%s'", testObject.getObjectId()))
                windowsActionHelper.clearText(testObject);

                windowsActionHelper.setText(testObject, text)
                logger.logPassed('Text ' + text + ' is set')
            }
        }, flowControl,(testObject != null) ? String.format("Unable to set text for object '%s''", testObject.getObjectId()) : "Unable to set text to object")
    }
}
