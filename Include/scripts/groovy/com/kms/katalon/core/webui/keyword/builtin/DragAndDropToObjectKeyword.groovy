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

@Action(value = "dragAndDropToObject")
public class DragAndDropToObjectKeyword extends WebUIAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        TestObject sourceObject = getTestObject(params[0])
        TestObject destinationObject = getTestObject(params[1])
        FailureHandling flowControl = (FailureHandling)(params.length > 2 && params[2] instanceof FailureHandling ? params[2] : RunConfiguration.getDefaultFailureHandling())
        dragAndDropToObject(sourceObject,destinationObject,flowControl)
    }

    @CompileStatic
    public void dragAndDropToObject(TestObject sourceObject, TestObject destinationObject,
            FailureHandling flowControl) {
        WebUIKeywordMain.runKeyword({
            boolean isSwitchIntoFrame = false
            try {
                logger.logDebug(StringConstants.KW_LOG_INFO_CHK_SRC_OBJ)
                if (sourceObject == null) {
                    throw new IllegalArgumentException(StringConstants.KW_EXC_SRC_OBJ_IS_NULL)
                }
                logger.logDebug(StringConstants.KW_LOG_INFO_CHK_DEST_OBJ)
                if (destinationObject == null) {
                    throw new IllegalArgumentException(StringConstants.KW_EXC_DEST_OBJ_IS_NULL)
                }
                logger.logDebug(MessageFormat.format(StringConstants.KW_LOG_INFO_START_DRAGGING_OBJ_W_ID_X_TO_OBJ_W_ID_Y, sourceObject.getObjectId(), destinationObject))

                Actions builder = new Actions(DriverFactory.getWebDriver())
                isSwitchIntoFrame = WebUiCommonHelper.switchToParentFrame(sourceObject)
                builder.clickAndHold(WebUIAbstractKeyword.findWebElement(sourceObject))
                builder.perform()
                Thread.sleep(250)
                if (isSwitchIntoFrame) {
                    WebUiCommonHelper.switchToDefaultContent()
                }
                isSwitchIntoFrame = WebUiCommonHelper.switchToParentFrame(destinationObject)

                WebElement destinationWebElement = WebUIAbstractKeyword.findWebElement(destinationObject)
                builder.moveToElement(destinationWebElement, 5, 5)
                builder.perform()
                Thread.sleep(250)
                builder.release(destinationWebElement)
                builder.perform()

                logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_DRAGGED_OBJ_W_ID_X_TO_OBJ_W_ID_Y, sourceObject.getObjectId(), destinationObject))
            } finally {
                if (isSwitchIntoFrame) {
                    WebUiCommonHelper.switchToDefaultContent()
                }
            }
        }, flowControl, true, StringConstants.KW_MSG_CANNOT_DRAG_AND_DROP_TO_OBJ)
    }
}
