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

@Action(value = "findImageElements")
public class FindImageElementsKeyword extends MobileAbstractKeyword {

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
        return findImageElements(imageFilePath, flowControl)
    }

    @CompileStatic
    public List<WebElement> findImageElements(String imageFilePath, FailureHandling flowControl) throws StepFailedException {
        return (List<WebElement>) MobileKeywordMain.runKeyword({
            FindsByImage<WebElement> driver = (FindsByImage) MobileDriverFactory.getDriver()

            byte[] fileContent = FileUtils.readFileToByteArray(new File(imageFilePath))

            String encodedString = Base64.getEncoder().encodeToString(fileContent)

            List<WebElement> elements = driver.findElementsByImage(encodedString)
            
            if (elements.isEmpty()) {
                logger.logInfo("No image element present on screen")
            } else {
                logger.logPassed(MessageFormat.format("Found {0} images on screen", elements.size()))
            }

            return elements
        }, flowControl, true, "Failed to find image elements")
    }
}
