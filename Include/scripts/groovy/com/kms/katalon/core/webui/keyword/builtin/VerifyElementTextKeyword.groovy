package com.kms.katalon.core.webui.keyword.builtin

import groovy.transform.CompileStatic

import java.text.MessageFormat

import org.apache.commons.lang3.ObjectUtils
import org.apache.commons.lang3.StringUtils
import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.internal.ExceptionsUtil
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.constants.CoreWebuiMessageConstants
import com.kms.katalon.core.webui.exception.WebElementNotFoundException
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain

@Action(value = "verifyElementText")
public class VerifyElementTextKeyword extends WebUIAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        TestObject to = getTestObject(params[0])
        String expectedText = (String) params[1]
        FailureHandling flowControl = (FailureHandling)(params.length > 2 && params[2] instanceof FailureHandling ? params[2] : RunConfiguration.getDefaultFailureHandling())
        return verifyElementText(to, expectedText, flowControl)
    }

    @CompileStatic
    public boolean verifyElementText(TestObject to, String expectedText, FailureHandling flowControl) throws StepFailedException {
        return WebUIKeywordMain.runKeyword({
            boolean isSwitchIntoFrame = false
            int timeout = RunConfiguration.getTimeOut()
            try {
                WebUiCommonHelper.checkTestObjectParameter(to)
                isSwitchIntoFrame = WebUiCommonHelper.switchToParentFrame(to, timeout)
                WebElement foundElement = null
                foundElement = WebUIAbstractKeyword.findWebElement(to, timeout)
                String actualText = foundElement.getText()
                boolean textPresent = ObjectUtils.equals(actualText, expectedText);
                if (!textPresent) {
                    WebUIKeywordMain.stepFailed(
                            MessageFormat.format(CoreWebuiMessageConstants.KW_MSG_ACTUAL_ELEMENT_TXT_NOT_MATCHED_EXPECTED_TXT, actualText, expectedText, to.getObjectId()),
                            flowControl, null, true)
                } else {
                    logger.logPassed(MessageFormat.format(CoreWebuiMessageConstants.KW_LOG_ACTUAL_ELEMENT_TXT_MATCHED_EXPECTED_TXT, to.getObjectId()))
                }
                return textPresent
            } catch (WebElementNotFoundException ex) {
                WebUIKeywordMain.stepFailed(ExceptionsUtil.getMessageForThrowable(ex), flowControl, null, true)
            } finally {
                if (isSwitchIntoFrame) {
                    WebUiCommonHelper.switchToDefaultContent()
                }
            }
            return false
        }, flowControl, true, MessageFormat.format(CoreWebuiMessageConstants.KW_MSG_VERIFY_ELEMENT_TEXT_FAILED,
        StringUtils.defaultString(to.getObjectId())))
    }
}
