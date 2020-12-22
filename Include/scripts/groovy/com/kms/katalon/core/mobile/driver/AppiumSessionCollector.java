package com.kms.katalon.core.mobile.driver;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.openqa.selenium.WebElement;

import io.appium.java_client.AppiumDriver;

public class AppiumSessionCollector {
    private List<AppiumDriverSession> sessions;
    
    private AppiumSessionCollector() {
        sessions = new LinkedList<>();
    };
    
    private static AppiumSessionCollector instance;
    
    private static AppiumSessionCollector getInstance() {
        if (instance == null) {
            instance = new AppiumSessionCollector();
            instance.sessions = new LinkedList<AppiumDriverSession>();
        }
        return instance;
    }
    
    public static AppiumDriverSession addSession(AppiumDriverSession session) {
        getInstance().sessions.add(session);
        return session;
    }
    
    public static AppiumDriverSession addSession(AppiumDriver<? extends WebElement> driver) {
        AppiumDriverSession session = new AppiumDriverSession(driver);
        getInstance().sessions.add(session);
        return session;
    }
    
    public static AppiumDriverSession getSession(AppiumDriver<? extends WebElement> driver) {
        Optional<AppiumDriverSession> session = ofDriver(driver);
        if (session.isPresent()) {
            return session.get();
        }

        return addSession(driver);
    }
    
    public static void removeSession(AppiumDriver<? extends WebElement> driver) {
        Optional<AppiumDriverSession> session = ofDriver(driver);
        if (session.isPresent()) {
            removeSession(session.get());
        }
    }
    
    public static Optional<AppiumDriverSession> ofDriver(AppiumDriver<? extends WebElement> driver) {
        return getInstance().sessions.stream()
        .filter(s -> s.getDriver() == driver)
        .findFirst();
    }
    
    public static void removeSession(AppiumDriverSession session) {
        getInstance().sessions.remove(session);
    }
    
    public static int getSessionCount() {
        return getInstance().sessions.size();
    }
    
}
