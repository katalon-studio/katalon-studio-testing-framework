package com.kms.katalon.core.webui.keyword.builtin

import groovy.transform.CompileStatic

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

import com.google.common.base.Function
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.BuiltinKeywords
import com.kms.katalon.core.keyword.internal.KeywordExecutor
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.util.internal.ExceptionsUtil
import com.kms.katalon.core.util.internal.PathUtil
import com.kms.katalon.core.webui.common.ScreenUtil
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.constants.StringConstants
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.driver.WebUIDriverType
import com.kms.katalon.core.webui.exception.BrowserNotOpenedException
import com.kms.katalon.core.webui.exception.WebElementNotFoundException
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain
import com.kms.katalon.core.webui.util.FileUtil

/**
 * This sample demonstrates how to create a new built-in keyword.
 * See also "Include/scripts/groovy/com/kms/katalon/core/webui/keyword/WebUiBuiltInKeywords.groovy".
 *
 */
@Action(value = "helloWorldFromWebUi")
public class HelloWorldFromWebUiKeyword extends WebUIAbstractKeyword {

	@CompileStatic
	@Override
	public SupportLevel getSupportLevel(Object ...params) {
		return super.getSupportLevel(params)
	}

	@CompileStatic
	@Override
	public Object execute(Object ...params) {
		// get the greeting message
		String message = (String) params[0]
		// retrieve flowControl (if any)
		// flowControl should be the last argument and of type FailureHandling
		FailureHandling flowControl = (FailureHandling)(
				params.length > 1 && 
				params[1] instanceof FailureHandling ? 
					params[1] : 
					RunConfiguration.getDefaultFailureHandling()
		)
		// action!
		helloWorldFromWebUi(message, flowControl)
	}

	@CompileStatic
	public String helloWorldFromWebUi(String message, FailureHandling flowControl) throws StepFailedException {
		WebUIKeywordMain.runKeyword(
			{
				// This is a debug message
				logger.logInfo("Say hello with message ${message}")
				// Open the alert dialog
				WebUiBuiltInKeywords.executeJavaScript("alert('Hello, ${message}');", null, flowControl)
				// Give users sometime to read the message
				Thread.sleep(2000)
				// Close the alert dialog
				WebUiBuiltInKeywords.acceptAlert()
				// Return the message
				return message
			}, 
			flowControl, 
			false, // no screenshot to be taken
			"Cannot say hello with message '${message}'!" // error message in case of failure
		)
	}
}
