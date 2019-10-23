package com.kms.katalon.core.webui.keyword.builtin

import java.text.MessageFormat

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
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.constants.StringConstants
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.driver.WebUIDriverType
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain

import groovy.transform.CompileStatic

@Action(value = "mouseOver")
public class MouseOverKeyword extends WebUIAbstractKeyword {

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
        mouseOver(to,flowControl)
    }

    @CompileStatic
    public void mouseOver(TestObject to, FailureHandling flowControl) throws StepFailedException {
        WebUIKeywordMain.runKeyword({
            boolean isSwitchIntoFrame = false
            try {
                WebUiCommonHelper.checkTestObjectParameter(to)
                if (DriverFactory.getExecutedBrowser() == WebUIDriverType.IE_DRIVER) {
                    WebUiCommonHelper.focusOnBrowser()
                }
                int timeout = KeywordHelper.checkTimeout(RunConfiguration.getTimeOut())
                isSwitchIntoFrame = WebUiCommonHelper.switchToParentFrame(to)
                WebElement hoverElement = WebUIAbstractKeyword.findWebElement(to)
                WebDriverWait wait = new WebDriverWait(webDriver, timeout);
                hoverElement = wait.until(ExpectedConditions.elementToBeClickable(hoverElement));
                logger.logDebug(MessageFormat.format(StringConstants.KW_LOG_INFO_MOVING_MOUSE_OVER_OBJ, to.getObjectId()))
                Actions builder = new Actions(DriverFactory.getWebDriver())
                builder.moveToElement(hoverElement).perform()
                logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_OBJ_IS_HOVERED, to.getObjectId()))
            } finally {
                if (isSwitchIntoFrame) {
                    WebUiCommonHelper.switchToDefaultContent()
                }
            }
        }, flowControl, true, (to != null) ? MessageFormat.format(StringConstants.KW_MSG_CANNOT_MOVE_MOUSE_OVER_OBJ_X, to.getObjectId())
        : StringConstants.KW_MSG_CANNOT_MOVE_MOUSE_OVER_OBJ)
    }
}
