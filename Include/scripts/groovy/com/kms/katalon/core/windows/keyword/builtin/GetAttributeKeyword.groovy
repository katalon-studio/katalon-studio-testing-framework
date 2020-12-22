package com.kms.katalon.core.windows.keyword.builtin

import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebDriverException
import org.openqa.selenium.WebElement

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

@Action(value = "getAttribute")
public class GetAttributeKeyword extends AbstractKeyword {

    private KeywordLogger logger = KeywordLogger.getInstance(GetAttributeKeyword.class)

    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return SupportLevel.NOT_SUPPORT
    }

    @Override
    public Object execute(Object ...params) {
        WindowsTestObject testObject = (WindowsTestObject) params[0]
        String attribute = (String) params[1]
        FailureHandling flowControl = (FailureHandling)(params.length > 2 && params[2] instanceof FailureHandling ? params[2] : RunConfiguration.getDefaultFailureHandling())
        return getAttribute(testObject,attribute,flowControl)
    }

    public String getAttribute(WindowsTestObject testObject, String attribute, FailureHandling flowControl) throws StepFailedException, TimeoutException {
        return KeywordMain.runKeyword({
            try {
                String attrValue = null
                WindowsDriver windowsDriver = WindowsDriverFactory.getWindowsDriver()
                if (windowsDriver == null) {
                    KeywordMain.stepFailed("WindowsDriver has not started. Please try Windows.startApplication first.", flowControl)
                }

                logger.logDebug("Checking attribute")
                if (attribute == null) {
                    throw new IllegalArgumentException("Attribute cannot be null")
                }

                WebElement element = WindowsActionHelper.create(WindowsDriverFactory.getWindowsSession()).findElement(testObject)
                logger.logDebug(String.format("Getting attribute '%s' of object '%s'", attribute, testObject.getObjectId()))
                try {
                    attrValue = element.getAttribute(attribute)
                } catch (WebDriverException getValueException) {
                    logger.logPassed(String.format("Attribute '%s' of object '%s' is: %s" , attribute, testObject.getObjectId(), attrValue))
                    return null;
                }
                logger.logPassed(String.format("Attribute '%s' of object '%s' is: '%s'" , attribute, testObject.getObjectId(), attrValue))
                return attrValue
            } catch (TimeoutException exception) {
                KeywordMain.stepFailed(String.format("Object '%s' does not exist", testObject.getObjectId()), flowControl);
                return null;
            }
        }, flowControl, (testObject != null && attribute != null) ? String.format("Unable to get attribute '%s' of object '%s'" , attribute, testObject.getObjectId())
        : "Unable to get attribute")
    }
}
