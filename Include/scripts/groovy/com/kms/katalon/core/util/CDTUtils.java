package com.kms.katalon.core.util;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.WebDriver;

import com.github.kklisura.cdt.services.ChromeDevToolsService;
import com.github.kklisura.cdt.services.ChromeService;
import com.github.kklisura.cdt.services.impl.ChromeServiceImpl;
import com.github.kklisura.cdt.services.types.ChromeTab;

public class CDTUtils {
    
    private CDTUtils() {
        
    }

    public static ChromeDevToolsService getService(WebDriver driver) {
        String[] debugAddress = getServiceEndpoint(driver);
        if (debugAddress == null || debugAddress.length < 2) {
            return null;
        }

        String host = debugAddress[0];
        int port = Integer.parseInt(debugAddress[1]);

        ChromeService chromeService = new ChromeServiceImpl(host, port);
        List<ChromeTab> tabs = chromeService.getTabs();

        for (ChromeTab tabI : tabs) {
            if (tabI.getUrl().equals(driver.getCurrentUrl())) {
                return chromeService.createDevToolsService(tabI);
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static String[] getServiceEndpoint(WebDriver driver) {
        HasCapabilities hashCapabilities = ((HasCapabilities) driver);
        Map<String, Object> capabilities = hashCapabilities.getCapabilities().asMap();

        if (capabilities.containsKey("goog:chromeOptions")) {
            capabilities = (Map<String, Object>) capabilities.get("goog:chromeOptions");
        } else if (capabilities.containsKey("ms:edgeOptions")) {
            capabilities = (Map<String, Object>) capabilities.get("ms:edgeOptions");
        } else {
            return new String[0];
        }

        String debugAddress = (String) capabilities.get("debuggerAddress");
        return StringUtils.isNotBlank(debugAddress) ? debugAddress.split(":") : new String[0];
    }
}
