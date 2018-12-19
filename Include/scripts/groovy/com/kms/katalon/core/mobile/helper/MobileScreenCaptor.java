package com.kms.katalon.core.mobile.helper;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;

import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.exception.StepFailedException;
import com.kms.katalon.core.helper.screenshot.ScreenCaptor;
import com.kms.katalon.core.helper.screenshot.ScreenCaptureException;
import com.kms.katalon.core.mobile.constants.StringConstants;
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory;

import io.appium.java_client.AppiumDriver;

public class MobileScreenCaptor extends ScreenCaptor {

	/**
	 * Takes screenshot by using
	 * {@link TakesScreenshot#getScreenshotAs(OutputType)}.
	 * </p>
	 * Using try with multi-catch to prevent error when generating groovy
	 * document.
	 */
	@Override
	protected void take(File newFile) throws ScreenCaptureException {
		AppiumDriver<?> driver = getAnyAppiumDriver();
		String context = driver.getContext();
		try {
            internalSwitchToNativeContext(driver);
            
            File tempFile = driver.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(tempFile, newFile);
            FileUtils.forceDelete(tempFile);

		} catch (WebDriverException e) {
			throw new ScreenCaptureException(e);
		} catch (StepFailedException e) {
			throw new ScreenCaptureException(e);
		} catch (IOException e) {
			throw new ScreenCaptureException(e);
		} finally {
            driver.context(context);
        }
	}
	
	protected AppiumDriver<?> getAnyAppiumDriver() {
		AppiumDriver<?> driver = null;
		try {
			driver = MobileDriverFactory.getDriver();
		} catch (StepFailedException e) {
			// Native app not running, so get from driver store
			for (Object driverObject : RunConfiguration.getStoredDrivers()) {
				if (driverObject instanceof AppiumDriver<?>) {
					driver = (AppiumDriver<?>) driverObject;
				}
			}
		}
		if (driver == null) {
			throw new StepFailedException(StringConstants.KW_MSG_UNABLE_FIND_DRIVER);
		}
		return driver;
	}
	
	protected boolean internalSwitchToNativeContext(AppiumDriver<?> driver) {
		return internalSwitchToContext(driver, "NATIVE");
	}
	
	protected boolean internalSwitchToContext(AppiumDriver<?> driver, String contextName) {
		try {
			for (String context : driver.getContextHandles()) {
				if (context.contains(contextName)) {
					driver.context(context);
					return true;
				}
			}
		} catch (WebDriverException e) {
			// Appium will raise WebDriverException error when driver.getContextHandles() is called but ios-webkit-debug-proxy is not started.
			// Catch it here and ignore
		}
		return false;
	}
}
