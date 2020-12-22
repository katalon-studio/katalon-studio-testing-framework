package com.kms.katalon.core.webui.keyword.builtin

import java.io.File
import java.io.IOException
import java.text.MessageFormat

import org.apache.commons.io.FileUtils
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.constants.StringConstants
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain
import com.kms.katalon.core.webui.util.FileUtil

import groovy.transform.CompileStatic

@Action(value = "uploadFileWithDragAndDrop")
class UploadFileWithDragAndDrop extends WebUIAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object... params) {
        TestObject to = getTestObject(params[0])
        String filePath = params[1];
        FailureHandling flowControl = (FailureHandling)(params.length > 2 && params[2] instanceof FailureHandling ? params[2] : RunConfiguration.getDefaultFailureHandling())
        uploadFileWithDragAndDrop(to, filePath, flowControl)
    }

    @CompileStatic
    public void uploadFileWithDragAndDrop(TestObject to, String filePath, FailureHandling flowControl) throws StepFailedException {
        WebUIKeywordMain.runKeyword({
            boolean isSwitchIntoFrame = false
            try {
                WebElement webElement = null;
                if(to != null) {
                    WebUiCommonHelper.checkTestObjectParameter(to)
                    isSwitchIntoFrame = WebUiCommonHelper.switchToParentFrame(to)
                    webElement = WebUIAbstractKeyword.findWebElement(to)
                }
                String dragAndDropScript = FileUtils.readFileToString(
                        new File(FileUtil.getExtensionsDirectory().getAbsolutePath() + "/drag-and-drop/drag-and-drop.js"));
                WebElement virtualInput = (WebElement) ((JavascriptExecutor) DriverFactory.getWebDriver())
                        .executeScript(dragAndDropScript, webElement);
                logger.logDebug(StringConstants.KW_LOG_INFO_INJECTED_INPUT);
                virtualInput.sendKeys(filePath);
                logger.logDebug(StringConstants.KW_LOG_INFO_EMITTED_DRAG_AND_DROP_EVENT);
            } finally {
                if (isSwitchIntoFrame) {
                    WebUiCommonHelper.switchToDefaultContent()
                }
            }
        }, flowControl, true, (to != null) ? MessageFormat.format(StringConstants.KW_MSG_CANNOT_CLICK_ON_OBJ_X, to.getObjectId())
        : StringConstants.KW_MSG_CANNOT_CLICK_ON_OBJ)
    }
}
