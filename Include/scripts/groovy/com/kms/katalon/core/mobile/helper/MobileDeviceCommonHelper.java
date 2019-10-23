package com.kms.katalon.core.mobile.helper;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import java.time.Duration;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

public class MobileDeviceCommonHelper {

    @SuppressWarnings("rawtypes")
    public static void unlockScreen(AppiumDriver driver) {
        if (driver instanceof IOSDriver) {
            Dimension screenSize = driver.manage().window().getSize();
            WebElement element = driver.findElementByName("SlideToUnlock");
            // Wake up the screen
            ((IOSDriver<?>) driver).lockDevice(Duration.ZERO);
            Point location = element.getLocation();
            MobileCommonHelper.swipe(driver, location.getX(), location.getY(), screenSize.width, location.getY());
        }
        if (driver instanceof AndroidDriver) {
            AndroidDriver<?> androidDriver = (AndroidDriver<?>) driver;
            if (androidDriver.isDeviceLocked()) {
                androidDriver.unlockDevice();
            }
        }
    }
}
