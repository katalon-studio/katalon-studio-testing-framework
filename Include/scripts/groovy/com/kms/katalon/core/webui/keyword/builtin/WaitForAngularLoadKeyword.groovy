package com.kms.katalon.core.webui.keyword.builtin

import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.WebDriverWait

import com.google.common.base.Predicates.InstanceOfPredicate
import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.constants.CoreWebuiMessageConstants
import com.kms.katalon.core.webui.constants.StringConstants
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain

import groovy.transform.CompileStatic

@Action(value = "waitForAngularLoad")
public class WaitForAngularLoadKeyword extends WebUIAbstractKeyword {

    @Override
    public Object execute(Object... params) {
        switch (params.length) {
            case 0:
                return waitForAgularLoad(RunConfiguration.getTimeOut(), RunConfiguration.getDefaultFailureHandling())
            case 1:
                if (params[0] instanceof Integer) {
                    return waitForAgularLoad(params[0], RunConfiguration.getDefaultFailureHandling())
                }
                if (params[0] instanceof FailureHandling) {
                    return waitForAgularLoad(RunConfiguration.getTimeOut(), params[0])
                }
            case 2:
                return waitForAgularLoad(params[0], params[1])
        }
    }

    /**
     * Waits until Angular elements loaded in a <code>timeout</code> seconds.
     * @param timeout timeout value in seconds.
     * @param flowControl
     * @return true if Angular/AJAX is ready. Otherwise, false.
     * @throws StepFailedException If browser has not started yet or jQuery is not ready
     */
    public boolean waitForAgularLoad(int timeout, FailureHandling flowControl) throws StepFailedException {
        return WebUIKeywordMain.runKeyword({
            WebDriver webDriver = DriverFactory.getWebDriver()
            if (webDriver == null) {
                throw new StepFailedException(CoreWebuiMessageConstants.EXC_BROWSER_IS_NOT_OPENED)
            }
            
            String angularJSDetectScript = '''return (window.angular != null && window.angular.version != null && window.angular.version.full != null)'''
            String angularDetectScript = '''return (window.ng != null && window.ng.coreTokens != null && window.ng.probe != null)'''

            JavascriptExecutor jsExec = (JavascriptExecutor) webDriver
            boolean isAngularJS = (Boolean) jsExec.executeScript(angularJSDetectScript)
            boolean isAngular = (Boolean) jsExec.executeScript(angularDetectScript)
            
            if (!isAngularJS && !isAngular) {
                logger.logWarning(CoreWebuiMessageConstants.KW_MSG_ANGULAR_NOT_USED)
                return false
            }
            
            WebDriverWait wait = new WebDriverWait(webDriver, WebUiCommonHelper.checkTimeout(timeout))
            ExpectedCondition jQueryLoadExpectation = new ExpectedCondition() {
                        def Boolean apply(WebDriver driver) {
                            String waitForAngularJSAllRequestsEnded = '''
                                return document.readyState === 'complete'
                                    && window.angular.element(document.body)
                                        .injector()
                                        .get('$http')
                                        .pendingRequests.length === 0
                            '''
                            String waitForAngularJSStabled = '''
                                var callback = arguments[arguments.length - 1];
                                try {
                                    var testability = window.angular.getTestability(document);
                                    testability.whenStable(() => callback(true));
                                } catch (error) {
                                    callback(null)
                                }
                            '''
                            String waitForAngularStabled = '''
                                return document.readyState === 'complete'
                                    && window.getAllAngularTestabilities().findIndex(node => !node.isStable()) === -1
                            '''
                            
                            if (isAngularJS) {
                                Boolean isAllRequestsEnded = jsExec.executeScript(waitForAngularJSAllRequestsEnded)
                                Boolean isAngularStabled = jsExec.executeAsyncScript(waitForAngularJSStabled)
                                return isAllRequestsEnded && isAngularStabled
                            }
                            if (isAngular) {
                                Boolean isAngularStabled = jsExec.executeScript(waitForAngularStabled)
                                return isAngularStabled
                            }
                            
                            return false
                        }
                    }

            boolean isAngularLoaded = wait.until(jQueryLoadExpectation)
            if (!isAngularLoaded) {
                WebUIKeywordMain.stepFailedWithReason(CoreWebuiMessageConstants.KW_LOG_ANGULAR_NOT_READY, flowControl, "timeout", true)
                return false
            }
            logger.logPassed(CoreWebuiMessageConstants.KW_LOG_ANGULAR_READY)
            return isAngularLoaded
        }, flowControl, true, CoreWebuiMessageConstants.KW_MSG_CANNOT_WAIT_FOR_ANGULAR_LOAD)
    }
}
