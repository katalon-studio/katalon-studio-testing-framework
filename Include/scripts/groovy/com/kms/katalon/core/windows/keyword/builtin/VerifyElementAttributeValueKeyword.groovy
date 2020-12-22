package com.kms.katalon.core.windows.keyword.builtin

import io.appium.java_client.windows.WindowsDriver
import java.text.MessageFormat
import java.util.concurrent.TimeUnit

import org.apache.commons.io.FileUtils
import org.openqa.selenium.Alert
import org.openqa.selenium.By
import org.openqa.selenium.Dimension
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.NoSuchWindowException
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebDriverException
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.FluentWait
import org.openqa.selenium.support.ui.Select
import org.openqa.selenium.support.ui.Wait
import org.openqa.selenium.support.ui.WebDriverWait

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.BuiltinKeywords
import com.kms.katalon.core.keyword.internal.AbstractKeyword
import com.kms.katalon.core.keyword.internal.KeywordExecutor
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.testobject.WindowsTestObject
import com.kms.katalon.core.util.internal.ExceptionsUtil
import com.kms.katalon.core.util.internal.PathUtil
import com.kms.katalon.core.keyword.internal.KeywordMain
import com.kms.katalon.core.windows.constants.StringConstants
import com.kms.katalon.core.windows.driver.WindowsDriverFactory
import com.kms.katalon.core.helper.KeywordHelper
import com.kms.katalon.core.windows.keyword.helper.WindowsActionHelper

@Action(value = "verifyElementAttributeValue")
public class VerifyElementAttributeValueKeyword extends AbstractKeyword {

    private KeywordLogger logger = KeywordLogger.getInstance(VerifyElementAttributeValueKeyword.class)

    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return SupportLevel.NOT_SUPPORT
    }

    @Override
    public Object execute(Object ...params) {
        WindowsTestObject testObject = (WindowsTestObject) params[0]
        String attributeName = (String) params[1]
        String attributeValue = (String) params[2]
        int timeOut = (int) params[3]
        FailureHandling flowControl = (FailureHandling)(params.length > 4 && params[4] instanceof FailureHandling ? params[4] : RunConfiguration.getDefaultFailureHandling())
        return verifyElementAttributeValue(testObject,attributeName,attributeValue,timeOut,flowControl)
    }

    public boolean verifyElementAttributeValue(WindowsTestObject testObject, String attributeName, String attributeValue, int timeout, FailureHandling flowControl) {
        KeywordMain.runKeyword({
            try {
				logger.logDebug(StringConstants.KW_CHECK_WINDOWS_DRIVER)
                WindowsDriver windowsDriver = WindowsDriverFactory.getWindowsDriver()
                if (windowsDriver == null) {
                    KeywordMain.stepFailed("WindowsDriver has not started. Please try Windows.startApplication first.", flowControl)
                }

                logger.logDebug("Checking attribute name")
                if (attributeName == null) {
                    throw new IllegalArgumentException("Attribute name cannot be null")
                }

                logger.logDebug("Checking attribute value")
                if (attributeValue == null) {
                    throw new IllegalArgumentException("Attribute value cannot be null")
                }

				logger.logDebug(StringConstants.KW_LOG_INFO_CHECKING_TIMEOUT)
                timeout = WindowsActionHelper.checkTimeout(timeout)

                WebElement foundElement = WindowsActionHelper.create(WindowsDriverFactory.getWindowsSession()).findElement(testObject, timeout, true);

                logger.logDebug(String.format("Getting attribute '%s' of object '%s'", attributeName, testObject.getObjectId()));
                String actualAttributeValue = null
                try {
                    actualAttributeValue = foundElement.getAttribute(attributeName)
                } catch (WebDriverException getValueException) {
                    KeywordMain.stepFailed(String.format("Object '%s' does not have attribute '%s'", testObject.getObjectId() , attributeName), flowControl)
                    return false;
                }
                if (actualAttributeValue.equals(attributeValue)) {
                    logger.logPassed(String.format("Object '%s' has attribute '%s' with name '%s'", testObject.getObjectId(), attributeName, attributeValue));
                    return true
                } else {
                    KeywordMain.stepFailed(String.format("Object '%s' has attribute '%s' with actual value '%s' instead of expected value '%s'", testObject.getObjectId(), attributeName, actualAttributeValue, attributeValue), flowControl)
                    return false
                }
            } catch (TimeoutException e) {
                KeywordMain.stepFailed(String.format("Object '%s' is not present", testObject.getObjectId()), flowControl)
				return false
            }
            return false
        }, flowControl, (testObject != null && attributeName !=null && attributeValue != null) ? String.format("Unable to verify if object '%s' has attribute '%s' with value '%s'", testObject.getObjectId(), attributeName, attributeValue)
        : "Unable to verify element attribute value")
    }
}
