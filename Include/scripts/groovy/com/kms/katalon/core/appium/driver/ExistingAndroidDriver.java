package com.kms.katalon.core.appium.driver;

import java.net.URL;
import java.util.Map;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.Response;

import com.kms.katalon.selenium.driver.IExistingRemoteWebDriver;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ExistingAndroidDriver extends SwipeableAndroidDriver implements IExistingRemoteWebDriver {
    private String oldSessionId;

    public ExistingAndroidDriver(URL remoteAddress, String oldSessionId) {
        super(remoteAddress, new DesiredCapabilities());
        setSessionId(oldSessionId);
        this.oldSessionId = oldSessionId;
        startSession(new DesiredCapabilities());
    }

    @Override
    protected void startSession(Capabilities desiredCapabilities) {
        if (this.oldSessionId == null) {
            return;
        }
        super.startSession(desiredCapabilities);
    }
    
    @Override
    public Response execute(String driverCommand, Map parameters) {
        if (DriverCommand.NEW_SESSION.equals(driverCommand)) {
            return createResponseForNewSession(oldSessionId);
        }
        return super.execute(driverCommand, parameters);
    }
}
