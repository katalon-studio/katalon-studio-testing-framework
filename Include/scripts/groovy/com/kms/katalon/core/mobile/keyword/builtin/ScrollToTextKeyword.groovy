package com.kms.katalon.core.mobile.keyword.builtin

import java.text.MessageFormat

import org.codehaus.groovy.transform.tailrec.VariableReplacedListener.*
import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.RemoteWebElement

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.mobile.constants.CoreMobileMessageConstants
import com.kms.katalon.core.mobile.constants.StringConstants
import com.kms.katalon.core.mobile.keyword.*
import com.kms.katalon.core.mobile.keyword.internal.MobileAbstractKeyword
import com.kms.katalon.core.mobile.keyword.internal.MobileKeywordMain
import com.kms.katalon.core.model.FailureHandling

import groovy.transform.CompileStatic
import io.appium.java_client.AppiumDriver
import io.appium.java_client.MobileBy
import io.appium.java_client.MobileElement
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.ios.IOSDriver

@Action(value = "scrollToText")
public class ScrollToTextKeyword extends MobileAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        String text = (String) params[0]
        FailureHandling flowControl = (FailureHandling)(params.length > 1 && params[1] instanceof FailureHandling ? params[1] : RunConfiguration.getDefaultFailureHandling())
        scrollToText(text,flowControl)
    }

    public void scrollToText(String text, FailureHandling flowControl) throws StepFailedException {
        MobileKeywordMain.runKeyword({
            logger.logDebug(StringConstants.COMM_LOG_INFO_CHECKING_TEXT)
            if (text == null) {
                throw new IllegalArgumentException(StringConstants.COMM_EXC_TEXT_IS_NULL)
            }
            AppiumDriver driver = getAnyAppiumDriver()
            String context = driver.getContext()
            try {
                internalSwitchToNativeContext(driver)

                Thread.sleep(500L)

                WebElement element = null

                if (driver instanceof AndroidDriver) {
                    String uiSelector = String.format("new UiSelector().textContains(\"%s\")", text)
                    String uiScrollable = String.format("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(%s)", uiSelector)
                    element = driver.findElementByAndroidUIAutomator(uiScrollable)
                } else if (driver instanceof IOSDriver) {
                    List<MobileElement> elements = ((IOSDriver) driver).findElements(MobileBy
                            .xpath("//*[contains(@label, '" + text + "') or contains(@text, '" + text + "')]"));
                    if (elements != null && !elements.isEmpty()) {
                        logger.logDebug(MessageFormat.format(CoreMobileMessageConstants.KW_LOG_TEXT_FOUND_IN_ELEMENTS, text, elements.size()))
                        RemoteWebElement remoteElement = (RemoteWebElement) elements.get(0)
                        String parentID = remoteElement.getId();
                        HashMap<String, String> scrollObject = new HashMap<String, String>();
                        scrollObject.put("element", parentID);
                        scrollObject.put("toVisible", text);
                        driver.executeScript("mobile:scroll", scrollObject);
                        element = remoteElement
                    }
                }
                if (element != null) {
                    logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_SCROLL_TO_TEXT_X, text))
                } else {
                    MobileKeywordMain.stepFailed(MessageFormat.format(CoreMobileMessageConstants.KW_MSG_TEXT_NOT_FOUND, text), flowControl, null, true)
                }
            } finally {
                driver.context(context)
            }
        }, flowControl, true, MessageFormat.format(StringConstants.KW_MSG_UNABLE_SCROLL_TO_TEXT_X, text))
    }

    private Fiz
}
