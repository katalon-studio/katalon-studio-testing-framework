package com.kms.katalon.core.mobile.keyword.builtin

import java.text.MessageFormat

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

@Action(value = "findImageElement")
public class FindImageElementKeyword extends MobileAbstractKeyword {

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
        return findImageElement(imageFilePath, flowControl)
    }

    @CompileStatic
    public WebElement findImageElement(String imageFilePath, FailureHandling flowControl) throws StepFailedException {
        return (WebElement) MobileKeywordMain.runKeyword({
            FindsByImage<WebElement> driver = (FindsByImage) MobileDriverFactory.getDriver()

            byte[] fileContent = FileUtils.readFileToByteArray(new File(imageFilePath))

            String encodedString = Base64.getEncoder().encodeToString(fileContent)

            WebElement element = driver.findElementByImage(encodedString)
            int x = element.getLocation().x
            int y = element.getLocation().y

            logger.logPassed(MessageFormat.format("Found image ''{0}'' on screen at location ({1}, {2})", imageFilePath, x, y))

            return element
        }, flowControl, true, "Failed to find image element")
    }
}
