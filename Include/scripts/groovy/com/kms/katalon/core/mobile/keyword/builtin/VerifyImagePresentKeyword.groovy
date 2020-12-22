package com.kms.katalon.core.mobile.keyword.builtin

import java.text.MessageFormat

import org.apache.commons.io.FileUtils
import org.openqa.selenium.WebDriverException
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

@Action(value = "verifyImagePresent")
public class VerifyImagePresentKeyword extends MobileAbstractKeyword {

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
        return verifyImagePresent(imageFilePath, flowControl)
    }

    @CompileStatic
    public boolean verifyImagePresent(String imageFilePath, FailureHandling flowControl) throws StepFailedException {
        MobileKeywordMain.runKeyword({
            FindsByImage<WebElement> driver = (FindsByImage) MobileDriverFactory.getDriver()

            byte[] fileContent = FileUtils.readFileToByteArray(new File(imageFilePath))

            String encodedString = Base64.getEncoder().encodeToString(fileContent)

            try {
                List<WebElement> elements = driver.findElementsByImage(encodedString)
                if (elements.size() == 0) {
                    throw new StepFailedException(MessageFormat.format("Image ''{0}'' does not present on screen", imageFilePath))
                }
                if (elements.size() == 1) {
                    WebElement element = elements.get(0)
                    int x = element.getLocation().x
                    int y = element.getLocation().y
                    logger.logPassed(MessageFormat.format("Image ''{0}'' presents on screen at location ({1}, {2})", imageFilePath, x, y))
                } else {
                    logger.logPassed(MessageFormat.format("Image ''{0}'' presents on screen at {1} diffrent locations", imageFilePath, elements.size()))
                }
                return true;
            } catch (WebDriverException e) {
                throw new StepFailedException(MessageFormat.format("Image ''{0}'' doesn not present on screen", imageFilePath), e)
            }
        }, flowControl, true, "Failed to verify image present")
    }
}
