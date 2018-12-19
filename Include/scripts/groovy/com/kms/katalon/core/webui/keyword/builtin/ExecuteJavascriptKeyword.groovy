package com.kms.katalon.core.webui.keyword.builtin

import groovy.transform.CompileStatic

import java.text.MessageFormat

import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.webui.constants.CoreWebuiMessageConstants
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain

@Action(value = "executeJavaScript")
public class ExecuteJavaScriptKeyword extends WebUIAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        String script = params[0]
        List arguments = params[1] != null ? (List) params[1] : null
        FailureHandling flowControl = RunConfiguration.getDefaultFailureHandling()
        if (params.length > 2) {
            flowControl = (FailureHandling) params[2]
        }
        return executeJavascript(script, arguments, flowControl)
    }

    @CompileStatic
    public Object executeJavascript(String script, List arguments, FailureHandling flowControl) {
        WebUIKeywordMain.runKeyword({
            WebDriver webDriver = getWebDriver()
            if (!(webDriver instanceof JavascriptExecutor)) {
                throw new StepFailedException(MessageFormat.format(CoreWebuiMessageConstants.KW_MSG_WEBDRIVER_DOES_NOT_SUPPORT_JS, webDriver.getClass().getName()))
            }
            JavascriptExecutor jsExecutor = (JavascriptExecutor) webDriver
            Object result = jsExecutor.executeScript(script, arguments != null ? arguments.toArray() : new Object[0])
            logger.logPassed(MessageFormat.format(CoreWebuiMessageConstants.KW_LOG_PASSED_EXECUTE_JS_SUCESSFULLY, script))
            return result
        }, flowControl, true, CoreWebuiMessageConstants.KW_MSG_UNABLE_TO_EXECUTE_JS)
    }
}