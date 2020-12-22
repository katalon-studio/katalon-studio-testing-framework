package com.kms.katalon.core.appium.driver;

import java.net.URL;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteTouchScreen;

import io.appium.java_client.android.AndroidDriver;

@SuppressWarnings({ "rawtypes" })
public class SwipeableAndroidDriver extends AndroidDriver implements HasTouchScreen {
    private RemoteTouchScreen touch;

    public SwipeableAndroidDriver(URL remoteAddress, Capabilities desiredCapabilities) {
        super(remoteAddress, desiredCapabilities);
        touch = new RemoteTouchScreen(getExecuteMethod());
    }
    
    public SwipeableAndroidDriver(HttpCommandExecutor executor, Capabilities desiredCapabilities) {
        super(executor, desiredCapabilities);
        touch = new RemoteTouchScreen(getExecuteMethod());
    }

    @Override
    public TouchScreen getTouch() {
        return touch;
    }

}
