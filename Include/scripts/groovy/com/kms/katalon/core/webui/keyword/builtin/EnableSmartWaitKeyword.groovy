package com.kms.katalon.core.webui.keyword.builtin

import org.openqa.selenium.WebDriver

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.driver.SmartWaitWebDriver
import com.kms.katalon.core.webui.driver.SmartWaitWebEventListener
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain

import groovy.transform.CompileStatic

@Action(value = "enableSmartWait")
class EnableSmartWaitKeyword extends WebUIAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        enableSmartWait();
    }

    @CompileStatic
    public void enableSmartWait() throws StepFailedException {
        WebUIKeywordMain.runKeyword({
            RunConfiguration.getExecutionProperties().put(RunConfiguration.LOCAL_SMART_WAIT_MODE, true);
        }, FailureHandling.CONTINUE_ON_FAILURE, true, "Unable to enable smart wait !");
    }
}
