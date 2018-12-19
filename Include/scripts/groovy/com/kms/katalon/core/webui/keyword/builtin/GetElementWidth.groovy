package com.kms.katalon.core.webui.keyword.builtin

import java.text.MessageFormat

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


import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.constants.CoreWebuiMessageConstants
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain
import com.kms.katalon.core.keyword.internal.KeywordExecutor
import com.kms.katalon.core.annotation.internal.Action;
import com.kms.katalon.core.configuration.RunConfiguration

import groovy.transform.CompileStatic

@Action(value = "getElementWidth")
public class GetElementWidth extends WebUIAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @Override
    public Object execute(Object... params) {
        TestObject to = getTestObject(params[0])
        FailureHandling flowControl = (FailureHandling)(params.length > 1 && params[1] instanceof FailureHandling ? params[1] : RunConfiguration.getDefaultFailureHandling())
        return getElementWidth(to, flowControl)
    }

    @CompileStatic
    public int getElementWidth(TestObject to, FailureHandling flowControl) throws StepFailedException {
        WebUIKeywordMain.runKeywordAndReturnInt({
            boolean isSwitchIntoFrame = false
            try {
                WebUiCommonHelper.checkTestObjectParameter(to)
                isSwitchIntoFrame = WebUiCommonHelper.switchToParentFrame(to)
                 WebElement webElement = WebUIAbstractKeyword.findWebElement(to)
                 String testObjectID = to.getObjectId()
                 logger.logDebug(MessageFormat.format(CoreWebuiMessageConstants.KW_LOG_INFO_GETTING_ELEMENT_WIDTH, testObjectID))
                 int width = webElement.getSize().width
                 logger.logPassed(MessageFormat.format(CoreWebuiMessageConstants.KW_LOG_PASSED_GET_ELEMENT_WIDTH, testObjectID, width))
                 return width;
            } finally {
                if (isSwitchIntoFrame) {
                    WebUiCommonHelper.switchToDefaultContent()
                }
            }
        }, flowControl, true, (to != null) ? MessageFormat.format(CoreWebuiMessageConstants.KW_MSG_CANNOT_GET_WIDTH_ELEMENT, to.getObjectId())
        : CoreWebuiMessageConstants.KW_MSG_CANNOT_GET_WIDTH_ELEMENT)
    }
}
