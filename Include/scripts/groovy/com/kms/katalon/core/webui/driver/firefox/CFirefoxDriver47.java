package com.kms.katalon.core.webui.driver.firefox;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ClasspathExtension;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.kms.katalon.selenium.driver.CFirefoxDriver;

public class CFirefoxDriver47 extends CFirefoxDriver {
    private static final String WEB_DRIVER_PROP = "webdriver";

    private static final String EXTENSION_FILE_NAME = "webdriver.xpi";

    public CFirefoxDriver47(DesiredCapabilities desiredCapabilities, int actionDelay) {
        super(desiredCapabilities, actionDelay);
    }

    public static CFirefoxDriver47 from(DesiredCapabilities desiredCapabilities, int actionDelay) {
        FirefoxProfile profile = new FirefoxProfile();
        profile.addExtension(WEB_DRIVER_PROP, new ClasspathExtension(CFirefoxDriver47.class, EXTENSION_FILE_NAME));

        desiredCapabilities.setCapability(FirefoxDriver.MARIONETTE, false);
        desiredCapabilities.setCapability(FirefoxDriver.PROFILE, profile);

        return new CFirefoxDriver47(desiredCapabilities, actionDelay);
    }
}
