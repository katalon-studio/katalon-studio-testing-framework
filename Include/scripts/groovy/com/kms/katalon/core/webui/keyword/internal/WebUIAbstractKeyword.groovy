package com.kms.katalon.core.webui.keyword.internal;

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.AbstractKeyword
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.constants.CoreWebuiMessageConstants;
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.exception.WebElementNotFoundException

import groovy.transform.CompileStatic

public abstract class WebUIAbstractKeyword extends AbstractKeyword {

    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return SupportLevel.NOT_SUPPORT;
    }

    @CompileStatic
    public static WebElement findWebElement(TestObject to, int timeOut = RunConfiguration.getTimeOut()) throws IllegalArgumentException, WebElementNotFoundException, StepFailedException {
        return WebUiCommonHelper.findWebElement(to, timeOut);
    }

    @CompileStatic
    public static List<WebElement> findWebElements(TestObject to, int timeOut) throws WebElementNotFoundException {
        return WebUiCommonHelper.findWebElements(to, timeOut);
    }

    protected WebDriver getWebDriver() {
        WebDriver webDriver = DriverFactory.getWebDriver()
        if (webDriver == null) {
            throw new StepFailedException(CoreWebuiMessageConstants.EXC_BROWSER_IS_NOT_OPENED)
        }
        return webDriver
    }
}
