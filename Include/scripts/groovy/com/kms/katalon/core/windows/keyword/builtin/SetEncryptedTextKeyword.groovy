package com.kms.katalon.core.windows.keyword.builtin;

import io.appium.java_client.windows.WindowsDriver
import java.text.MessageFormat
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.internal.Action;
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.KeywordMain
import com.kms.katalon.core.keyword.internal.AbstractKeyword
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.windows.constants.StringConstants
import com.kms.katalon.core.windows.driver.WindowsDriverFactory
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.WindowsTestObject
import com.kms.katalon.core.windows.keyword.helper.WindowsActionHelper
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.util.CryptoUtil

@Action(value = "setEncryptedText")
public class SetEncryptedTextKeyword extends AbstractKeyword {

    private KeywordLogger logger = KeywordLogger.getInstance(SetEncryptedTextKeyword.class)

    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return SupportLevel.NOT_SUPPORT;
    }

    @Override
    public Object execute(Object ...params) {
        WindowsTestObject testObject = (WindowsTestObject) params[0]
        String encryptedText = (String) params[1]
        FailureHandling flowControl = (FailureHandling)(params.length > 2 && params[2] instanceof FailureHandling ? params[2] : RunConfiguration.getDefaultFailureHandling())
        setEncryptedText(testObject, encryptedText, flowControl)
    }

    public String setEncryptedText(WindowsTestObject testObject, String encryptedText, FailureHandling flowControl) throws StepFailedException , TimeoutException {
        return (String) KeywordMain.runKeyword({
			try {
				logger.logDebug(StringConstants.KW_CHECK_WINDOWS_DRIVER)
	            WindowsDriver windowsDriver = WindowsDriverFactory.getWindowsDriver()
	            if (windowsDriver == null) {
	                KeywordMain.stepFailed(StringConstants.COMM_WINDOWS_HAS_NOT_STARTED, flowControl)
	            }

				logger.logDebug("Checking encrypted text")
	            if(encryptedText == null){
	                throw new IllegalArgumentException(StringConstants.KW_ENCRYPTED_TEXT_IS_NULL)
	            }

	            CryptoUtil.CrytoInfo cryptoInfo = CryptoUtil.getDefault(encryptedText)
	            String rawText = CryptoUtil.decode(cryptoInfo)

				logger.logDebug(String.format(StringConstants.KW_LOG_INFO_CHECKING_TEST_OBJECT));
				if (testObject == null) {
					throw new IllegalArgumentException(StringConstants.KW_EXEC_TEST_OBJECT_IS_NULL);
				}

	            WindowsActionHelper windowsActionHelper = WindowsActionHelper.create(WindowsDriverFactory.getWindowsSession())

	            logger.logDebug(String.format("Clearing text of object '%s'", testObject.getObjectId()))
	            windowsActionHelper.clearText(testObject);
	            logger.logDebug(String.format("Setting text of object '%s' to value ******", testObject.getObjectId()))
	            windowsActionHelper.setText(testObject, rawText)
	
	            logger.logPassed(String.format("Text ****** has been set on object '%s'", testObject.getObjectId()))
			} catch (TimeoutException exception) {
				KeywordMain.stepFailed(String.format("Object '%s' does not exist", testObject.getObjectId()), flowControl)
			}
				}, flowControl, (testObject != null) ? String.format("Unable to set encrypted text for object '%s'", testObject.getObjectId())
        : "Unable to set text")
    }
}
