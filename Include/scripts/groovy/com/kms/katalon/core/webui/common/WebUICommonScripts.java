package com.kms.katalon.core.webui.common;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.kms.katalon.core.webui.constants.WebUICommonScriptConstants;

public class WebUICommonScripts {

    @SuppressWarnings("unchecked")
    public static Map<String, List<String>> generateXPaths(WebDriver webDriver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) webDriver;

        Map<String, List<String>> generatedXPaths = (Map<String, List<String>>) js
                .executeScript(WebUICommonScriptConstants.GENERATE_XPATHS_SCRIPT, element);

        return generatedXPaths;
    }

    public static String generateXPath(WebDriver webDriver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) webDriver;

        String generatedXPath = (String) js
                .executeScript(WebUICommonScriptConstants.GENERATE_XPATH_SCRIPT, element);

        return generatedXPath;
    }
}
