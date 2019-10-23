package com.kms.katalon.core.webui.keyword.builtin;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.kms.katalon.core.annotation.internal.Action;
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.webui.constants.CoreWebuiMessageConstants;
import com.kms.katalon.core.webui.constants.StringConstants;
import com.kms.katalon.core.exception.StepFailedException;
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.model.FailureHandling;
import com.kms.katalon.core.testobject.TestObject;
import com.kms.katalon.core.webui.common.WebUiCommonHelper;
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword;
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain;

import groovy.transform.CompileStatic;
import java.text.MessageFormat

@Action(value = "verifyOptionsPresent")
public class VerifyOptionsPresentKeyword extends WebUIAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        TestObject to = getTestObject(params[0])
        List expectedOptions = (List) params[1]
        FailureHandling flowControl = (FailureHandling)(params.length > 2 && params[2] instanceof FailureHandling ? 
            params[2] : RunConfiguration.getDefaultFailureHandling())
        return verifyOptionsPresent(to, expectedOptions, flowControl)
    }

    @CompileStatic
    public boolean verifyOptionsPresent(TestObject to, List expectedOptions,
            FailureHandling flowControl) throws StepFailedException {
        return WebUIKeywordMain.runKeyword({
            boolean isSwitchIntoFrame = false
            int timeout = RunConfiguration.getTimeOut()
            try {
                WebUiCommonHelper.checkTestObjectParameter(to)
                isSwitchIntoFrame = WebUiCommonHelper.switchToParentFrame(to, timeout)
                WebElement webElement = WebUIAbstractKeyword.findWebElement(to, timeout)

                List<String> actualOptions = []
                for (WebElement childElement : new Select(webElement).getOptions()) {
                    actualOptions.add(childElement.getText())
                }
                boolean presented = actualOptions.containsAll(expectedOptions)
                if (presented) {
                    logger.logPassed(MessageFormat.format(CoreWebuiMessageConstants.KW_MSG_TXT_OPTIONS_PRESENT, expectedOptions))
                    return true
                }
                WebUIKeywordMain.stepFailedWithReason(
                        MessageFormat.format(CoreWebuiMessageConstants.KW_MSG_TXT_OPTIONS_NOT_PRESENT, expectedOptions),
                        flowControl,
                        MessageFormat.format(CoreWebuiMessageConstants.KW_MSG_TXT_OPTIONS_NOT_PRESENT_REASON, expectedOptions, actualOptions),
                        true)
                return false
            } finally {
                if (isSwitchIntoFrame) {
                    WebUiCommonHelper.switchToDefaultContent()
                }
            }
        }, flowControl, true, MessageFormat.format(CoreWebuiMessageConstants.KW_MSG_TXT_OPTIONS_NOT_PRESENT, expectedOptions))
    }
}
