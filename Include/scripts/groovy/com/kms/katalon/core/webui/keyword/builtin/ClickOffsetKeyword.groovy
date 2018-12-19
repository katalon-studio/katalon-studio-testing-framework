package com.kms.katalon.core.webui.keyword.builtin

import java.text.MessageFormat

import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.constants.CoreWebuiMessageConstants
import com.kms.katalon.core.webui.constants.StringConstants
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain

import groovy.transform.CompileStatic

@Action(value = "clickOffset")
public class ClickOffsetKeyword extends WebUIAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        TestObject to = getTestObject(params[0])
        int offsetX = (int) params[1];
        int offsetY = (int) params[2];
        FailureHandling flowControl = (FailureHandling)(params.length > 3 && params[3] instanceof FailureHandling ? params[3] : RunConfiguration.getDefaultFailureHandling())
        clickOffset(to, offsetX, offsetY, flowControl)
    }

    @CompileStatic
    public void clickOffset(TestObject to, int offsetX, int offsetY, FailureHandling flowControl) throws StepFailedException {
        WebUIKeywordMain.runKeyword({
            boolean isSwitchIntoFrame = false
            try {
                WebUiCommonHelper.checkTestObjectParameter(to)
                isSwitchIntoFrame = WebUiCommonHelper.switchToParentFrame(to)
                WebElement webElement = WebUIAbstractKeyword.findWebElement(to)
                logger.logDebug(MessageFormat.format(CoreWebuiMessageConstants.KW_LOG_INFO_CLICKING_ON_OBJ_OFFSET_X_Y, to.getObjectId(), offsetX, offsetY))
                Actions builder = new Actions(DriverFactory.getWebDriver())
                builder.moveToElement(webElement, offsetX, offsetY).click().perform()
                logger.logPassed(MessageFormat.format(CoreWebuiMessageConstants.KW_LOG_PASSED_OBJ_CLICKED_OFFSET_X_Y, to.getObjectId(), offsetX, offsetY))
            } finally {
                if (isSwitchIntoFrame) {
                    WebUiCommonHelper.switchToDefaultContent()
                }
            }
        }, flowControl, true, (to != null) ? MessageFormat.format(CoreWebuiMessageConstants.KW_MSG_CANNOT_CLICK_ON_OBJ_X_OFFSET_Y_Z, to.getObjectId(), offsetX, offsetY)
        : CoreWebuiMessageConstants.KW_MSG_CANNOT_CLICK_ON_OBJ_OFFSET)
    }
}
