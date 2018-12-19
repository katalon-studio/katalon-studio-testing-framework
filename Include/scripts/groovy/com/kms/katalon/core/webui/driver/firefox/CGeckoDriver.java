package com.kms.katalon.core.webui.driver.firefox;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.kms.katalon.core.webui.driver.DriverFactory;
import com.kms.katalon.selenium.driver.CFirefoxDriver;

public class CGeckoDriver extends CFirefoxDriver {
    public CGeckoDriver(Capabilities capabilities, int actionDelay) {
        super(capabilities, actionDelay);
    }

    public static FirefoxDriver from(DesiredCapabilities desiredCapabilities, int actionDelay) {
        System.setProperty("webdriver.gecko.driver", DriverFactory.getGeckoDriverPath());
        return new CGeckoDriver(desiredCapabilities, actionDelay);
    }
}
