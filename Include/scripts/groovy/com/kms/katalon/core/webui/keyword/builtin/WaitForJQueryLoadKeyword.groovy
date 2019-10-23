package com.kms.katalon.core.webui.keyword.builtin

import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.WebDriverWait

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.constants.CoreWebuiMessageConstants
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain

import groovy.transform.CompileStatic

@Action(value = "waitForJQueryLoad")
public class WaitForJQueryLoadKeyword extends WebUIAbstractKeyword {

    @Override
    public Object execute(Object[] params) {
        switch (params.length) {
            case 0:
                return waitForJQueryLoad(RunConfiguration.getTimeOut(), RunConfiguration.getDefaultFailureHandling())
            case 1:
                if (params[0] instanceof Integer) {
                    return waitForJQueryLoad(params[0], RunConfiguration.getDefaultFailureHandling())
                }
                if (params[0] instanceof FailureHandling) {
                    return waitForJQueryLoad(RunConfiguration.getTimeOut(), params[0])
                }
            case 2:
                return waitForJQueryLoad(params[0], params[1])
        }
    }

    /**
     * Waits until jQuery loaded in a <code>timeout</code> seconds.
     * @param timeout timeout value in seconds.
     * @param flowControl
     * @return true if jQuery is ready. Otherwise, false.
     * @throws StepFailedException If browser has not started yet or jQuery is not ready
     */
    public boolean waitForJQueryLoad(int timeout, FailureHandling flowControl)
            throws StepFailedException {
        return WebUIKeywordMain.runKeyword({
            WebDriver webDriver = DriverFactory.getWebDriver()
            if (webDriver == null) {
                throw new StepFailedException(CoreWebuiMessageConstants.EXC_BROWSER_IS_NOT_OPENED)
            }

            JavascriptExecutor jsExec = (JavascriptExecutor) webDriver
            if (jsExec.executeScript("return typeof jQuery === 'undefined'")) {
                logger.logWarning(CoreWebuiMessageConstants.KW_MSG_JQUERY_NOT_USED)
                return false
            }

            WebDriverWait jQueryWait = new WebDriverWait(webDriver, WebUiCommonHelper.checkTimeout(timeout))
            ExpectedCondition jQueryLoadExpectation = new ExpectedCondition() {
                        def apply(WebDriver driver) {
                            return jsExec.executeScript("return jQuery.active === 0 && document.readyState === 'complete'")
                        }
            }

            boolean isJQueryReady = jQueryWait.until(jQueryLoadExpectation)
            if (!isJQueryReady) {
                WebUIKeywordMain.stepFailedWithReason(CoreWebuiMessageConstants.KW_LOG_JQUERY_NOT_READY, flowControl, "timeout", true)
                return false
            }

            logger.logPassed(CoreWebuiMessageConstants.KW_LOG_JQUERY_READY)
            return isJQueryReady
        }, flowControl, true, CoreWebuiMessageConstants.KW_MSG_CANNOT_WAIT_FOR_JQUERY_LOAD)
    }
}
