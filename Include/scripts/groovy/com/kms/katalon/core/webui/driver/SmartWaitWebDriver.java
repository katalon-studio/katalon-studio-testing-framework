package com.kms.katalon.core.webui.driver;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

/**
 * A subclass of {@link EventFiringWebDriver} used
 * by Katalon to register {@link SmartWaitWebEventListener}
 */
public class SmartWaitWebDriver extends EventFiringWebDriver {

    List<SmartWaitWebEventListener> listeners = new ArrayList<>();

    public SmartWaitWebDriver(WebDriver driver) {
        super(driver);
    }

    public void register(SmartWaitWebEventListener listener) {
        listeners.add(listener);
        super.register(listener);
    }

    public void unregister() {
        listeners.forEach(listener -> {
            super.unregister(listener);
        });
    }
}
