package com.kms.katalon.core.webui.keyword.builtin

import java.text.MessageFormat

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.webui.constants.CoreWebuiMessageConstants
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain

import groovy.transform.CompileStatic

@Action(value = "verifyAllLinksOnCurrentPageAccessible")
public class VerifyAllLinksOnCurrentPageAccessibleKeyword extends WebUIAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        boolean isIncludedExternalLinks = getBooleanValue(params, 0)
        List<String> excludedLinks = getExcludedLinks(params, 1)
        FailureHandling flowControl = getFailureHandling(params, 2)
        return verifyAllLinksOnCurrentPageAccessible(isIncludedExternalLinks, excludedLinks, flowControl)
    }

    @CompileStatic
    public boolean verifyAllLinksOnCurrentPageAccessible(boolean isIncludedExternalLinks, List<String> excludedLinks, FailureHandling flowControl) throws StepFailedException {
        String currentUrl = DriverFactory.getWebDriver().getCurrentUrl()
        return WebUIKeywordMain.runKeyword({
            List<String> links = WebUiBuiltInKeywords.getAllLinksOnCurrentPage(isIncludedExternalLinks, excludedLinks, FailureHandling.OPTIONAL)
            boolean isAllLinksAccessible = WebUiBuiltInKeywords.verifyLinksAccessible(links, FailureHandling.OPTIONAL)

            if (isAllLinksAccessible) {
                logger.logPassed(MessageFormat.format(CoreWebuiMessageConstants.KW_LOG_PASSED_ALL_LINKS_ON_PAGE_ARE_ACCESSIBLE, currentUrl))
            } else {
                WebUIKeywordMain.stepFailed(CoreWebuiMessageConstants.KW_LOG_FAILED_SOME_LINKS_ON_PAGE_ARE_INACCESSIBLE, flowControl, null, false)
            }
            return isAllLinksAccessible
        }, flowControl, false, MessageFormat.format(CoreWebuiMessageConstants.KW_MSG_UNABLE_TO_VERIFY_ALL_LINKS_ON_PAGE_ARE_ACCESSIBLE, currentUrl))
    }

    @CompileStatic
    private List<String> getExcludedLinks(Object[] params, int index) {
        Object param = getParam(params, index)
        if (param instanceof List<String>) {
            return (List<String>) param
        }
        return Collections.emptyList()
    }
}
