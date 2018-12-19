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

@Action(value = "verifyOptionNotSelectedByValue")
public class VerifyOptionNotSelectedByValueKeyword extends WebUIAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        TestObject to = getTestObject(params[0])
        String value = (String) params[1]
        boolean isRegex = (boolean) params[2]
        int timeOut = (int) params[3]
        FailureHandling flowControl = (FailureHandling)(params.length > 4 && params[4] instanceof FailureHandling ? params[4] : RunConfiguration.getDefaultFailureHandling())
        return verifyOptionNotSelectedByValue(to,value,isRegex,timeOut,flowControl)
    }

    @CompileStatic
    public boolean verifyOptionNotSelectedByValue(TestObject to, String value, boolean isRegex, int timeOut,
            FailureHandling flowControl) throws StepFailedException {
        String regularExpressionLog = ((isRegex) ? " using regular expression" : "")
        return WebUIKeywordMain.runKeyword({
            boolean isSwitchIntoFrame = false
            try {
                WebUiCommonHelper.checkTestObjectParameter(to)
                logger.logDebug(StringConstants.KW_LOG_INFO_CHECKING_VAL)
                if (value == null) {
                    throw new IllegalArgumentException(StringConstants.KW_EXC_VAL_CANNOT_BE_NULL)
                }
                isSwitchIntoFrame = WebUiCommonHelper.switchToParentFrame(to, timeOut)
                WebElement webElement = WebUIAbstractKeyword.findWebElement(to, timeOut)
                Select select = new Select(webElement)
                int numValueOptions = WebUiCommonHelper.getNumberOfOptionByValue(select, value, isRegex, to.getObjectId())
                int numNotSelectedOptions = WebUiCommonHelper.getNumberOfNotSelectedOptionByValue(select, value, isRegex,
                        to.getObjectId())

                if (numValueOptions == 0) {
                    WebUIKeywordMain.stepFailed(MessageFormat.format(StringConstants.KW_MSG_THERE_IS_NO_OPT_W_VAL_X_PRESENT_IN_OBJ_Y, value, to.getObjectId(), regularExpressionLog), flowControl, null, true)
                } else if (numNotSelectedOptions < numValueOptions) {
                    WebUIKeywordMain.stepFailed(MessageFormat.format(StringConstants.KW_MSG_ONLY_X_IN_Y_OPTS_W_VAL_Z_UNSELECTED_IN_OBJ, numNotSelectedOptions, numValueOptions, value, to.getObjectId(), regularExpressionLog),
                            flowControl, null, true)
                } else {
                    logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_UNSELECTED_ALL_OPT_W_VAL_X_IN_OBJ_Y, value, to.getObjectId(), regularExpressionLog))
                }
                return (numValueOptions > 0 && (numNotSelectedOptions == numValueOptions))
            } finally {
                if (isSwitchIntoFrame) {
                    WebUiCommonHelper.switchToDefaultContent()
                }
            }
        }, flowControl, true, (to != null && value != null) ?
        MessageFormat.format(StringConstants.KW_MSG_CANNOT_VERIFY_OPT_IS_UNSELECTED_BY_VAL_X_IN_OBJ_Y, value, to.getObjectId(), regularExpressionLog) : MessageFormat.format(StringConstants.KW_MSG_CANNOT_VERIFY_OPT_IS_UNSELECTED_BY_VAL_X, regularExpressionLog))
    }
}
