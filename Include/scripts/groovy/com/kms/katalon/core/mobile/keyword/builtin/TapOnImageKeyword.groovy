package com.kms.katalon.core.mobile.keyword.builtin

import java.text.MessageFormat
import java.time.Duration

import org.apache.commons.io.FileUtils
import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.mobile.keyword.internal.MobileAbstractKeyword
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.mobile.keyword.internal.MobileKeywordMain
import com.kms.katalon.core.model.FailureHandling

import groovy.transform.CompileStatic
import io.appium.java_client.FindsByImage
import io.appium.java_client.TouchAction
import io.appium.java_client.touch.WaitOptions
import io.appium.java_client.touch.offset.PointOption

@Action(value = "tapOnImage")
public class TapOnImageKeyword extends MobileAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        String imageFilePath = (String) params[0]
        FailureHandling flowControl = (FailureHandling)(params.length > 1 && params[1] instanceof FailureHandling ? params[1] : RunConfiguration.getDefaultFailureHandling())
        tapOnImage(imageFilePath, flowControl)
    }

    @CompileStatic
    public void tapOnImage(String imageFilePath, FailureHandling flowControl) throws StepFailedException {
        MobileKeywordMain.runKeyword({
            FindsByImage<WebElement> driver = (FindsByImage) MobileDriverFactory.getDriver()

            byte[] fileContent = FileUtils.readFileToByteArray(new File(imageFilePath))

            String encodedString = Base64.getEncoder().encodeToString(fileContent)

            List elements = driver.findElementsByImage(encodedString)
            logger.logInfo(MessageFormat.format("Found {0} element(s) on screen", elements.size()))
            if (elements.isEmpty()) {
                throw new StepFailedException("No image element found")
            }
            
            WebElement element = elements.get(0)
            int x = element.getLocation().x
            int y = element.getLocation().y

            logger.logInfo(MessageFormat.format("Tap on image at location ({0}, {1})", x, y))
            TouchAction<?> tap = new TouchAction<WebElement>(MobileDriverFactory.getDriver())
                    .tap(PointOption.point(x, y))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(50)));
            tap.perform();

            logger.logPassed(MessageFormat.format("Tap on image ''{0}'' at location ({1}, {2}) successfully", imageFilePath, x, y))
        }, flowControl, true, "Failed to tap on image")
    }
}
