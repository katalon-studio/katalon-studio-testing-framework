package com.kms.katalon.core.mobile.driver;

import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.WebElement;

import io.appium.java_client.AppiumDriver;

public class AppiumDriverSession {
    
    private AppiumDriver<? extends WebElement> driver;
    private Map<String, Object> properties;
    public AppiumDriver<? extends WebElement> getDriver() {
        return driver;
    }
    public void setDriver(AppiumDriver<? extends WebElement> driver) {
        this.driver = driver;
    }
    public Map<String, Object> getProperties() {
        return properties;
    }
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
    
    public AppiumDriverSession(AppiumDriver<? extends WebElement> driver) {
        this.driver = driver;
        properties = new HashMap<>();
    }
    
}
