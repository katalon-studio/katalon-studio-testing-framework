package com.kms.katalon.core.webui.keyword.builtin

import groovy.transform.CompileStatic

import java.text.MessageFormat
import java.util.concurrent.TimeUnit

import org.apache.commons.io.FileUtils
import org.openqa.selenium.Alert
import org.openqa.selenium.By
import org.openqa.selenium.Dimension
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.NoSuchWindowException
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebDriverException
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.FluentWait
import org.openqa.selenium.support.ui.Select
import org.openqa.selenium.support.ui.Wait
import org.openqa.selenium.support.ui.WebDriverWait

import com.google.common.base.Function
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.BuiltinKeywords
import com.kms.katalon.core.keyword.internal.KeywordExecutor
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.util.internal.ExceptionsUtil
import com.kms.katalon.core.util.internal.PathUtil
import com.kms.katalon.core.webui.common.ScreenUtil
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.constants.StringConstants
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.driver.WebUIDriverType
import com.kms.katalon.core.webui.exception.BrowserNotOpenedException
import com.kms.katalon.core.webui.exception.WebElementNotFoundException
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain
import com.kms.katalon.core.webui.util.FileUtil

@Action(value = "verifyElementNotPresent")
public class VerifyElementNotPresentKeyword extends WebUIAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        TestObject to = getTestObject(params[0])
        int timeOut = (int) params[1]
        FailureHandling flowControl = (FailureHandling)(params.length > 2 && params[2] instanceof FailureHandling ? params[2] : RunConfiguration.getDefaultFailureHandling())
        return verifyElementNotPresent(to,timeOut,flowControl)
    }

    @CompileStatic
    public boolean verifyElementNotPresent(TestObject to, int timeOut, FailureHandling flowControl) throws StepFailedException {
        return WebUIKeywordMain.runKeyword({
            boolean isSwitchIntoFrame = false
            try {
                WebUiCommonHelper.checkTestObjectParameter(to)
                timeOut = WebUiCommonHelper.checkTimeout(timeOut)
                isSwitchIntoFrame = WebUiCommonHelper.switchToParentFrame(to, timeOut)
                boolean elementNotFound = false
                final By locator = WebUiCommonHelper.buildLocator(to)
                try {
                    if (locator != null) {
                        logger.logDebug(MessageFormat.format(StringConstants.KW_LOG_INFO_FINDING_WEB_ELEMENT_W_ID, to.getObjectId(), locator.toString(), timeOut))
                        elementNotFound = new FluentWait<WebDriver>(DriverFactory.getWebDriver())
                                .pollingEvery(500, TimeUnit.MILLISECONDS).withTimeout(timeOut, TimeUnit.SECONDS)
                                .until(new Function<WebDriver, Boolean>() {
                                    @Override
                                    public Boolean apply(WebDriver webDriver) {
                                        try {
                                            webDriver.findElement(locator)
                                            return false
                                        } catch (NoSuchElementException e) {
                                            return true
                                        }
                                    }
                                })
                    } else {
                        throw new IllegalArgumentException(MessageFormat.format(StringConstants.KW_EXC_WEB_ELEMENT_W_ID_DOES_NOT_HAVE_SATISFY_PROP, to.getObjectId()))
                    }
                } catch (TimeoutException e) {
                    // timeOut, do nothing
                }
                if (elementNotFound) {
                    logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_WEB_ELEMT_W_ID_IS_NOT_PRESENT_AFTER, to.getObjectId(), locator.toString(), timeOut))
                    return true
                } else {
                    WebUIKeywordMain.stepFailed(MessageFormat.format(StringConstants.KW_MSG_WEB_ELEMT_W_ID_IS_NOT_PRESENT_AFTER, to.getObjectId(), locator.toString(), timeOut), flowControl, null, true)
                    return false
                }
            } catch (WebElementNotFoundException e) {
                WebUIKeywordMain.stepFailed(ExceptionsUtil.getMessageForThrowable(e), flowControl, null, true)
            } finally {
                if (isSwitchIntoFrame) {
                    WebUiCommonHelper.switchToDefaultContent()
                }
            }
            return false
        }, flowControl, true, (to != null) ? MessageFormat.format(StringConstants.KW_MSG_CANNOT_VERIFY_OBJ_X_IS_NOT_PRESENT, to.getObjectId()) : StringConstants.KW_MSG_CANNOT_VERIFY_OBJ_IS_NOT_PRESENT)
    }
}
