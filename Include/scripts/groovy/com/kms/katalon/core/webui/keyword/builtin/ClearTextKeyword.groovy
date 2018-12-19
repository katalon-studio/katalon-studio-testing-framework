package com.kms.katalon.core.webui.keyword.builtin

import java.text.MessageFormat

import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.constants.CoreWebuiMessageConstants
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain

import groovy.transform.CompileStatic

@Action(value = "clearText")
public class ClearTextKeyword extends WebUIAbstractKeyword {

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
        return clearText(to,flowControl)
    }

    @CompileStatic
    public boolean clearText(TestObject to, FailureHandling flowControl) throws StepFailedException {
        Closure mainMethod = {
            boolean isSwitchIntoFrame = false
            try {
                WebUiCommonHelper.checkTestObjectParameter(to)
                isSwitchIntoFrame = WebUiCommonHelper.switchToParentFrame(to)
                WebElement webElement = WebUIAbstractKeyword.findWebElement(to)
                logger.logDebug(MessageFormat.format(CoreWebuiMessageConstants.KW_LOG_INFO_CLEARING_OBJ_TXT, to.getObjectId()))
                webElement.clear()
                logger.logPassed(MessageFormat.format(CoreWebuiMessageConstants.KW_LOG_PASSED_OBJ_TXT_IS_CLEARED, to.getObjectId()))

                return true
            } finally {
                if (isSwitchIntoFrame) {
                    WebUiCommonHelper.switchToDefaultContent()
                }
            }
        }
        Closure fallback = { return false }
        boolean takeScreenShotIfFailed = true
        String failedMessage = (to != null) ? MessageFormat.format(CoreWebuiMessageConstants.KW_MSG_UNABLE_TO_CLEAR_OBJ_TEXT, to.getObjectId())
                : CoreWebuiMessageConstants.KW_MSG_UNABLE_TO_CLEAR_OBJ_TEXT

        return WebUIKeywordMain.runKeyword(mainMethod, fallback, flowControl, takeScreenShotIfFailed, failedMessage)
    }
}
