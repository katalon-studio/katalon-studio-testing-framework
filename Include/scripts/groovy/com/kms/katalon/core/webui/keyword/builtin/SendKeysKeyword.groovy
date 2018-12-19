package com.kms.katalon.core.webui.keyword.builtin

import groovy.transform.CompileStatic

import java.text.MessageFormat

import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.constants.StringConstants
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain
import com.kms.katalon.selenium.util.SeleniumKeysUtil

@Action(value = "sendKeys")
public class SendKeysKeyword extends WebUIAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        TestObject to = getTestObject(params[0])
        String strKeys = (String) params[1]
        FailureHandling flowControl = (FailureHandling)(params.length > 2 && params[2] instanceof FailureHandling ? params[2] : RunConfiguration.getDefaultFailureHandling())
        sendKeys(to,strKeys,flowControl)
    }

    @CompileStatic
    public void sendKeys(TestObject to, String strKeys, FailureHandling flowControl) throws StepFailedException {
        WebUIKeywordMain.runKeyword({
            boolean isSwitchIntoFrame = false
            try {
                if (to == null) {
                    to = new TestObject("tempBody").addProperty("css", ConditionType.EQUALS, "body")
                }
                isSwitchIntoFrame = WebUiCommonHelper.switchToParentFrame(to)
                String readableText = SeleniumKeysUtil.getReadableText(strKeys)
                logger.logDebug(MessageFormat.format(StringConstants.KW_LOG_INFO_SENDING_KEYS_TO_OBJ, [readableText, to.getObjectId()] as Object[]))
                WebElement webElement = WebUIAbstractKeyword.findWebElement(to)
                webElement.sendKeys(strKeys)
                logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_KEYS_SENT_TO_OBJ, [readableText, to.getObjectId()] as Object[]))
            } finally {
                if (isSwitchIntoFrame) {
                    WebUiCommonHelper.switchToDefaultContent()
                }
            }
        }, flowControl, true, (to != null) ? MessageFormat.format(StringConstants.KW_MSG_CANNOT_SED_KEYS_TO_OBJ_X, [strKeys, to.getObjectId()] as Object[]) : MessageFormat.format(StringConstants.KW_MSG_CANNOT_SED_KEYS_TO_OBJ, strKeys))
    }
}
