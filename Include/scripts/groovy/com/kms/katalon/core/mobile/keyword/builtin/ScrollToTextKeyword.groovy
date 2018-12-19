package com.kms.katalon.core.mobile.keyword.builtin

import groovy.transform.CompileStatic
import io.appium.java_client.AppiumDriver
import io.appium.java_client.MobileBy
import io.appium.java_client.MobileElement
import io.appium.java_client.TouchAction
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.AndroidKeyCode
import io.appium.java_client.functions.ExpectedCondition
import io.appium.java_client.ios.IOSDriver
import io.appium.java_client.remote.HideKeyboardStrategy

import java.text.MessageFormat
import java.util.concurrent.TimeUnit

import org.apache.commons.io.FileUtils
import org.apache.commons.lang.StringUtils
import org.codehaus.groovy.transform.tailrec.VariableReplacedListener.*
import org.openqa.selenium.Dimension
import org.openqa.selenium.OutputType
import org.openqa.selenium.Point
import org.openqa.selenium.ScreenOrientation
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebDriverException
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.touch.TouchActions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.FluentWait
import org.openqa.selenium.support.ui.WebDriverWait

import com.google.common.base.Function
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.helper.KeywordHelper
import com.kms.katalon.core.keyword.BuiltinKeywords
import com.kms.katalon.core.keyword.internal.KeywordExecutor
import com.kms.katalon.core.keyword.internal.KeywordMain
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.mobile.constants.CoreMobileMessageConstants
import com.kms.katalon.core.mobile.constants.StringConstants
import com.kms.katalon.core.mobile.helper.MobileCommonHelper
import com.kms.katalon.core.mobile.helper.MobileDeviceCommonHelper
import com.kms.katalon.core.mobile.helper.MobileElementCommonHelper
import com.kms.katalon.core.mobile.helper.MobileGestureCommonHelper
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.mobile.keyword.*
import com.kms.katalon.core.mobile.keyword.internal.MobileAbstractKeyword
import com.kms.katalon.core.mobile.keyword.internal.MobileKeywordMain

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
                        element = elements.get(0)
                        TouchAction action = new TouchAction(driver).press(0, 0).moveTo(element).release()
                        action.perform()
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
