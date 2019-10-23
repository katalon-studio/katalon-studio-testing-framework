package com.kms.katalon.core.webui.keyword.builtin

import java.text.MessageFormat

import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.helper.KeywordHelper
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.trymonad.Try
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.constants.StringConstants
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain

import groovy.transform.CompileStatic

@Action(value = "click")
public class ClickKeyword extends WebUIAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        TestObject to = getTestObject(params[0])
        FailureHandling flowControl = (FailureHandling)(params.length > 1 && params[1] instanceof FailureHandling ? params[1] : RunConfiguration.getDefaultFailureHandling())
        click(to,flowControl)
    }

    @CompileStatic
    public void click(TestObject to, FailureHandling flowControl) throws StepFailedException {
        WebUIKeywordMain.runKeyword({
            boolean isSwitchIntoFrame = false
            try {
                WebUiCommonHelper.checkTestObjectParameter(to)
                isSwitchIntoFrame = WebUiCommonHelper.switchToParentFrame(to)
                WebElement webElement = WebUIAbstractKeyword.findWebElement(to)
                WebDriver webDriver = DriverFactory.getWebDriver();
                int timeout = KeywordHelper.checkTimeout(RunConfiguration.getTimeOut())
                WebDriverWait wait = new WebDriverWait(webDriver, timeout);
                webElement = wait.until(ExpectedConditions.elementToBeClickable(webElement));
                logger.logDebug(MessageFormat.format(StringConstants.KW_LOG_INFO_CLICKING_ON_OBJ, to.getObjectId()))
                Try.ofFailable({
                    webElement.click()
                    return Boolean.TRUE;
                }).orElseTry({
                    Actions builder = new Actions(webDriver);
                    builder.moveToElement(webElement);
                    builder.click();
                    builder.build().perform();
                    return Boolean.TRUE;
                }).orElseTry({
                    JavascriptExecutor executor = (JavascriptExecutor) webDriver;
                    executor.executeScript("arguments[0].click();", webElement);
                    return Boolean.TRUE;
                }).onSuccess({
                    logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_OBJ_CLICKED, to.getObjectId()))
                }).get();
            } finally {
                if (isSwitchIntoFrame) {
                    WebUiCommonHelper.switchToDefaultContent()
                }
            }
        }, flowControl, true, (to != null) ? MessageFormat.format(StringConstants.KW_MSG_CANNOT_CLICK_ON_OBJ_X, to.getObjectId())
        : StringConstants.KW_MSG_CANNOT_CLICK_ON_OBJ)
    }
}
