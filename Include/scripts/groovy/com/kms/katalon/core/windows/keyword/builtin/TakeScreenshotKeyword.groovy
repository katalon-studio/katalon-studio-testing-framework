package com.kms.katalon.core.windows.keyword.builtin;

import org.apache.commons.io.FileUtils
import org.openqa.selenium.OutputType
import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.internal.Action;
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.AbstractKeyword;
import com.kms.katalon.core.keyword.internal.KeywordMain
import com.kms.katalon.core.keyword.internal.SupportLevel;
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.windows.driver.WindowsDriverFactory
import com.kms.katalon.core.windows.keyword.helper.WindowsActionHelper

import io.appium.java_client.windows.WindowsDriver

@Action(value = "takeScreenshot")
public class TakeScreenshotKeyword extends AbstractKeyword {

    private KeywordLogger logger = KeywordLogger.getInstance(DoubleClickKeyword.class)

    @Override
    public SupportLevel getSupportLevel(Object... params) {
        return SupportLevel.NOT_SUPPORT;
    }

    @Override
    public Object execute(Object ...params) {
        String screenshotLocation = (String) params[0]
        FailureHandling flowControl = (FailureHandling)(params.length > 1 && params[1] instanceof FailureHandling ? params[1] : RunConfiguration.getDefaultFailureHandling())
        takeScreenshot(screenshotLocation, flowControl)
    }

    public File takeScreenshot(String screenshotLocation, FailureHandling flowControl) throws StepFailedException {
        return (File) KeywordMain.runKeyword({
            WindowsDriver<WebElement> windowsDriver = WindowsDriverFactory.getWindowsDriver()
            if (windowsDriver == null) {
                KeywordMain.stepFailed("WindowsDriver has not started. Please try Windows.startApplication first.", flowControl)
            }
            WindowsActionHelper.create(WindowsDriverFactory.getWindowsSession()).takeScreenshot(screenshotLocation)
            logger.logPassed('Take screenshot of current Windows to location: ' + screenshotLocation + ' succesfully')
        }, flowControl)
    }
}
