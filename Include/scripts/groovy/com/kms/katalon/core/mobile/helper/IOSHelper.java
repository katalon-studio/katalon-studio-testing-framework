package com.kms.katalon.core.mobile.helper;

import java.awt.image.BufferedImage;
import java.text.MessageFormat;
import java.util.Map;

import org.openqa.selenium.WebElement;

import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.mobile.constants.StringConstants;
import com.kms.katalon.core.mobile.driver.AppiumSessionCollector;
import com.kms.katalon.core.mobile.exception.MobileException;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;

public final class IOSHelper {

    private IOSHelper() {
    }

    private static final String XPATH_NAVIGATION_BAR_IOS = "(//XCUIElementTypeNavigationBar)[1]";

    private static final String IOS_SETTINGS_APP_BUNDLEID = "com.apple.Preferences";

    private static final KeywordLogger logger = KeywordLogger.getInstance(IOSHelper.class);

    public static int getStatusBarHeight(AppiumDriver<? extends WebElement> driver, DevicePixelRatio scaleFactor) {
        int statusBarHeight = 0;
        IOSDriver<? extends WebElement> iosDriver = (IOSDriver<? extends WebElement>) driver;
        try {
            statusBarHeight = getStatusbarHeightByXpath(iosDriver);
            return statusBarHeight;
        } catch (MobileException ignored) {
            logger.logInfo(MessageFormat.format(StringConstants.KW_LOG_FAILED_GET_IOS_STATUSBAR, "XPath"));
        }
        try {
            statusBarHeight = getStatusBarHeightByScreenshot(iosDriver);
            return (int) (statusBarHeight / scaleFactor.ratioY);
        } catch (Exception ignored) {
            logger.logInfo(MessageFormat.format(StringConstants.KW_LOG_FAILED_GET_IOS_STATUSBAR, "Screenshot"));
        }

        return statusBarHeight;
    }

    private static int getStatusBarHeightByScreenshot(IOSDriver<? extends WebElement> driver) throws MobileException {
        BufferedImage screenshot = MobileScreenCaptor.takeScreenshot(driver);
        BufferedImage viewportScreenshot = MobileScreenCaptor.takeViewportScreenshot(driver);
        if (screenshot.getHeight() == viewportScreenshot.getHeight()) {
            throw new MobileException(StringConstants.KW_MSG_SCREENSHOT_STATUSBAR_INFO_FAIL);
        }
        return screenshot.getHeight() - viewportScreenshot.getHeight();
    }

    private static int getStatusbarHeightByXpath(IOSDriver<? extends WebElement> driver) throws MobileException {
        String currentApp = null;
        int statusBarHeight = 0;
        boolean switched = false;
        try {
            currentApp = getActiveAppBundleIdFromSession(driver);
            if (currentApp == null) {
                throw new MobileException(StringConstants.KW_MSG_SCREENSHOT_STATUSBAR_INFO_FAIL);
            }
            driver.activateApp(IOS_SETTINGS_APP_BUNDLEID);
            switched = true;
            WebElement e = driver.findElementByXPath(XPATH_NAVIGATION_BAR_IOS);
            statusBarHeight = e.getLocation().y;
            return statusBarHeight;
        } catch (Exception e) {
            throw new MobileException(e);
        } finally {
            if (switched) {
                driver.activateApp(currentApp);
            }
        }
    }

    public static String getActiveAppBundleIdFromSession(IOSDriver<? extends WebElement> driver) {
        return (String) AppiumSessionCollector.getSession(driver)
                .getProperties()
                .get(MobileCommonHelper.PROPERTY_NAME_IOS_BUNDLEID);
    }

    @SuppressWarnings("unchecked")
    static String getActiveAppInfo(IOSDriver<? extends WebElement> driver) {
        try {
            Map<String, Object> json = (Map<String, Object>) driver.executeScript("mobile: activeAppInfo");
            Object o = json.get("bundleId");
            return o.toString();
        } catch (Exception e) {
            logger.logWarning(StringConstants.KW_LOG_FAILED_GET_IOS_BUNDLE);
        }
        return null;
    }

}
