package com.kms.katalon.core.windows.keyword.builtin

import io.appium.java_client.windows.WindowsDriver
import java.text.MessageFormat
import java.util.concurrent.TimeUnit
import java.time.Duration;

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
import com.kms.katalon.core.keyword.internal.KeywordExecutor
import com.kms.katalon.core.keyword.internal.KeywordMain
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.testobject.WindowsTestObject
import com.kms.katalon.core.keyword.internal.AbstractKeyword
import com.kms.katalon.core.util.internal.ExceptionsUtil
import com.kms.katalon.core.util.internal.PathUtil
import com.kms.katalon.core.windows.constants.StringConstants
import com.kms.katalon.core.windows.driver.WindowsDriverFactory
import com.kms.katalon.core.windows.keyword.helper.WindowsActionHelper
import com.kms.katalon.core.helper.KeywordHelper

@Action(value = "waitForElementPresent")
public class WaitForElementPresentKeyword extends AbstractKeyword {

    private KeywordLogger logger = KeywordLogger.getInstance(WaitForElementPresentKeyword.class)

    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return SupportLevel.NOT_SUPPORT
    }

    @Override
    public Object execute(Object ...params) {
        WindowsTestObject testObject = (WindowsTestObject) params[0]
        int timeOut = (int) params[1]
        FailureHandling flowControl = (FailureHandling)(params.length > 2 && params[2] instanceof FailureHandling ? params[2] : RunConfiguration.getDefaultFailureHandling())
        return waitForElementPresent(testObject,timeOut,flowControl)
    }

    public boolean waitForElementPresent(WindowsTestObject testObject, int timeOut, FailureHandling flowControl) throws StepFailedException {
		KeywordMain.runKeyword({
			try {
				logger.logDebug(StringConstants.KW_CHECK_WINDOWS_DRIVER)
				WindowsDriver windowsDriver = WindowsDriverFactory.getWindowsDriver()
                if (windowsDriver == null) {
                    KeywordMain.stepFailed("WindowsDriver has not started. Please try Windows.startApplication first.", flowControl)
                }
				logger.logDebug(StringConstants.KW_LOG_INFO_CHECKING_TIMEOUT)
                timeOut = WindowsActionHelper.checkTimeout(timeOut)

                WebElement foundElement = WindowsActionHelper.create(WindowsDriverFactory.getWindowsSession()).findElement(testObject, timeOut, true);

                if (foundElement != null){
                    logger.logPassed(String.format("Object '%s' is present", testObject.getObjectId()));
                }
                return true
            } catch (TimeoutException exception) {
                KeywordMain.stepFailed(String.format("Object '%s' is not present within %s second(s)", testObject.getObjectId(), timeOut), flowControl)
                return false
            }
        }, flowControl, (testObject != null) ? String.format("Unable to verify object ''{0}'' is present", testObject.getObjectId()) : "Unable to verify object is present")
        }
    }
