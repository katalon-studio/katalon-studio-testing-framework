package com.kms.katalon.core.webui.keyword.builtin

import java.text.MessageFormat

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.KeywordExecutor
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.webui.constants.CoreWebuiMessageConstants
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain

import groovy.transform.CompileStatic

@Action(value = "verifyLinksAccessible")
public class VerifyLinksAccessibleKeyword extends WebUIAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        List<String> links = getLinks(params, 0)
        FailureHandling flowControl = getFailureHandling(params, 1)
        return verifyLinksAccessible(links, flowControl)
    }

    @CompileStatic
    public boolean verifyLinksAccessible(List<String> links, FailureHandling flowControl) throws StepFailedException {
        return WebUIKeywordMain.runKeyword({
            RequestObject request = newRequest()
            List<String> inaccessibleLinks = new ArrayList<String>()
            for(String link : links) {
                request.setRestUrl(link)
                ResponseObject responseObject = (ResponseObject) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB_SERVICE, "sendRequest", request)
                boolean isAccessible = responseObject != null && responseObject.getStatusCode() >=200 && responseObject.getStatusCode() < 400
                if (!isAccessible) {
                    inaccessibleLinks.add(link)
                }
            }
            if (inaccessibleLinks.isEmpty()) {
                logger.logPassed(CoreWebuiMessageConstants.KW_LOG_PASSED_ALL_LINKS_ARE_ACCESSIBLE)
                return true
            }
            WebUIKeywordMain.stepFailed(MessageFormat.format(CoreWebuiMessageConstants.KW_LOG_FAILED_FOUND_X_INACCESSIBLE_LINKS, inaccessibleLinks.size(), inaccessibleLinks), flowControl, null, false)
            return false
        }, flowControl, false, CoreWebuiMessageConstants.KW_MSG_UNABLE_TO_VERIFY_LINKS_ARE_ACCESSIBLE)
    }

    @CompileStatic
    private RequestObject newRequest() {
        RequestObject request = new RequestObject("Temporary RESTful request object")
        request.setServiceType("RESTful")
        request.setRestRequestMethod("GET")
        return request
    }

    @CompileStatic
    private List<String> getLinks(Object[] params, int index) {
        Object param = getParam(params, index)
        if (param instanceof List<String>) {
            return (List<String>) param
        }
        return Collections.emptyList()
    }
}
