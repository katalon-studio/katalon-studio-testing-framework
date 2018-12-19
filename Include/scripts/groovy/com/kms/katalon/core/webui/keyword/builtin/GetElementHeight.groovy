package com.kms.katalon.core.webui.keyword.builtin


import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.constants.CoreWebuiMessageConstants
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain


import groovy.transform.CompileStatic

import java.text.MessageFormat

import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.internal.Action;
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException;

@Action(value = "getElementHeight")
class GetElementHeight extends WebUIAbstractKeyword{
    
    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object... params) {
        return super.getSupportLevel(params)
    }
    
    @CompileStatic
    @Override
    public Object execute(Object... params) {
        TestObject to = getTestObject(params[0])
        FailureHandling flowControl = (FailureHandling)(params.length > 1 && params[1] instanceof FailureHandling ? params[1] : RunConfiguration.getDefaultFailureHandling())
        return getElementHeight(to, flowControl)
    }
    
    @CompileStatic
    public int getElementHeight(TestObject to, FailureHandling flowControl) throws StepFailedException {
        WebUIKeywordMain.runKeywordAndReturnInt({
            boolean isSwitchIntoFrame = false
            try {
                WebUiCommonHelper.checkTestObjectParameter(to)
                isSwitchIntoFrame = WebUiCommonHelper.switchToParentFrame(to)
                WebElement element = WebUIAbstractKeyword.findWebElement(to)
                String testObjectID = to.getObjectId()
                logger.logDebug(MessageFormat.format(CoreWebuiMessageConstants.KW_LOG_INFO_GETTING_ELEMENT_HEIGHT, testObjectID))
                int elementHeight = element.getSize().height;
                logger.logPassed(MessageFormat.format(CoreWebuiMessageConstants.KW_LOG_PASSED_GET_ELEMENT_HEIGHT, to.getObjectId(), elementHeight))
                return elementHeight
            } finally {
                if (isSwitchIntoFrame) {
                    WebUiCommonHelper.switchToDefaultContent()
                }
            }
        }, flowControl, true, (to != null) ? MessageFormat.format(CoreWebuiMessageConstants.KW_MSG_CANNOT_GET_ELEMENT_HEIGHT, to.getObjectId())
        : CoreWebuiMessageConstants.KW_MSG_CANNOT_GET_ELEMENT_HEIGHT)
    }
    
}
