package com.kms.katalon.core.webui.contribution;

import com.kms.katalon.core.driver.internal.IDriverCleaner;
import com.kms.katalon.core.webui.driver.DriverFactory;

public class WebUiDriverCleaner implements IDriverCleaner {
    @Override
    public void cleanDrivers() {
        DriverFactory.closeWebDriver();
    }
}
