package com.kms.katalon.core.webui.helper.screenshot;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;

import com.kms.katalon.core.exception.StepFailedException;
import com.kms.katalon.core.helper.screenshot.ScreenCaptor;
import com.kms.katalon.core.helper.screenshot.ScreenCaptureException;
import com.kms.katalon.core.webui.driver.DriverFactory;

public class WebUIScreenCaptor extends ScreenCaptor {

    /**
     * Takes screenshot by using {@link TakesScreenshot#getScreenshotAs(OutputType)}.
     * </p>
     * Using try with multi-catch to prevent error when generating groovy document.
     */
    @Override
    protected void take(File newFile) throws ScreenCaptureException {
        try {
            FileUtils.copyFile(((TakesScreenshot) DriverFactory.getWebDriver()).getScreenshotAs(OutputType.FILE),
                    newFile, false);
        } catch (WebDriverException e) {
            throw new ScreenCaptureException(e);
        } catch (StepFailedException e) {
            throw new ScreenCaptureException(e);
        } catch (IOException e) {
            throw new ScreenCaptureException(e);
        }
    }
}
