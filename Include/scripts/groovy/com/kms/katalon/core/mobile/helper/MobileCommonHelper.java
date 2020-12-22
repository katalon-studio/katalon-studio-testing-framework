package com.kms.katalon.core.mobile.helper;

import java.awt.image.BufferedImage;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.kms.katalon.core.exception.StepFailedException;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.mobile.common.MobileXPathBuilder;
import com.kms.katalon.core.mobile.constants.StringConstants;
import com.kms.katalon.core.mobile.driver.AppiumDriverSession;
import com.kms.katalon.core.mobile.driver.AppiumSessionCollector;
import com.kms.katalon.core.mobile.exception.MobileException;
import com.kms.katalon.core.mobile.keyword.internal.AndroidProperties;
import com.kms.katalon.core.mobile.keyword.internal.GUIObject;
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory;
import com.kms.katalon.core.testobject.TestObject;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

public class MobileCommonHelper {
    
    private static final KeywordLogger logger = KeywordLogger.getInstance(MobileCommonHelper.class);

    private static final String ATTRIBUTE_NAME_FOR_ANDROID_RESOURCE_ID = "resourceId";

    private static final String ATTRIBUTE_NAME_FOR_ANDROID_CONTENT_DESC = "name";
    
    public static final String PROPERTY_NAME_DEVICE_PIXEL_RATIO = "devicePixelRatio";
    
    public static final String PROPERTY_NAME_OS_STATUS_BAR_HEIGHT = "osStatusBarHeight";
    
    public static final String PROPERTY_NAME_IOS_BUNDLEID = "iosBundleId";

    @SuppressWarnings("rawtypes")
    public static void swipe(AppiumDriver driver, int startX, int startY, int endX, int endY) {
        TouchAction swipe = new TouchAction(driver).press(PointOption.point(startX, startY))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(500L)))
                .moveTo(PointOption.point(endX, endY)).release();
        swipe.perform();
    }

    public static Map<String, String> deviceModels = new HashMap<String, String>();
    static {
        deviceModels.put("iPhone3,1", "iPhone 4");
        deviceModels.put("iPhone3,3", "iPhone 4");
        deviceModels.put("iPhone4,1", "iPhone 4S");

        deviceModels.put("iPhone5,1", "iPhone 5");
        deviceModels.put("iPhone5,2", "iPhone 5");
        deviceModels.put("iPhone5,3", "iPhone 5c");
        deviceModels.put("iPhone5,4", "iPhone 5c");
        deviceModels.put("iPhone6,1", "iPhone 5s");
        deviceModels.put("iPhone6,2", "iPhone 5s");
        deviceModels.put("iPhone7,1", "iPhone 6 Plus");
        deviceModels.put("iPhone7,2", "iPhone 6");
        deviceModels.put("iPad1,1", "iPad");
        deviceModels.put("iPad2,1", "iPad 2");
        deviceModels.put("iPad2,2", "iPad 2");
        deviceModels.put("iPad2,3", "iPad 2");
        deviceModels.put("iPad2,4", "iPad 2");
        deviceModels.put("iPad2,5", "iPad mini");
        deviceModels.put("iPad2,6", "iPad mini");
        deviceModels.put("iPad2,7", "iPad mini");

        deviceModels.put("iPad3,1", "iPad 3");
        deviceModels.put("iPad3,2", "iPad 3");
        deviceModels.put("iPad3,3", "iPad 3");
        deviceModels.put("iPad3,4", "iPad 4");
        deviceModels.put("iPad3,5", "iPad 4");
        deviceModels.put("iPad3,6", "iPad 4");
        deviceModels.put("iPad4,1", "iPad Air");
        deviceModels.put("iPad4,2", "iPad Air");
        deviceModels.put("iPad4,3", "iPad Air");
        deviceModels.put("iPad4,4", "iPad mini 2");
        deviceModels.put("iPad4,5", "iPad mini 2");
        deviceModels.put("iPad4,6", "iPad mini 2");
        deviceModels.put("iPad4,7", "iPad mini 3");
        deviceModels.put("iPad4,8", "iPad mini 3");
        deviceModels.put("iPad4,9", "iPad mini 3");
        deviceModels.put("iPad5,3", "iPad Air 2");
        deviceModels.put("iPad5,4", "iPad Air 2");

    }

    public static Map<String, String> airPlaneButtonCoords = new HashMap<String, String>();
    static {
        airPlaneButtonCoords.put("iPhone 5s", "40;195");
        airPlaneButtonCoords.put("iPhone 5", "40;195");

        airPlaneButtonCoords.put("iPad 2", "260;905");
        airPlaneButtonCoords.put("iPad 3", "260;905");
        airPlaneButtonCoords.put("iPad 4", "260;905");

        airPlaneButtonCoords.put("iPad Air", "260;905");
        airPlaneButtonCoords.put("iPad Air 2", "260;905");

        airPlaneButtonCoords.put("iPhone 6", "50;290");
        airPlaneButtonCoords.put("iPhone 6 Plus", "59;359");

        airPlaneButtonCoords.put("iPad mini", "265;905");
        airPlaneButtonCoords.put("iPad mini 2", "265;905");
        airPlaneButtonCoords.put("iPad mini 3", "265;905");
    }
    
    public static String getAttributeLocatorValue(TestObject testObject) {
        if (testObject == null || testObject.getProperties().isEmpty()) {
            return null;
        }
        MobileXPathBuilder xpathBuilder = new MobileXPathBuilder(testObject.getActiveProperties());                
        return xpathBuilder.build(); 
    }

    public static String getAttributeValue(WebElement element, String attributeName) {
        switch (attributeName.toString()) {
            case GUIObject.HEIGHT:
                return String.valueOf(element.getSize().height);
            case GUIObject.WIDTH:
                return String.valueOf(element.getSize().width);
            case GUIObject.X:
                return String.valueOf(element.getLocation().x);
            case GUIObject.Y:
                return String.valueOf(element.getLocation().y);
            case AndroidProperties.ANDROID_RESOURCE_ID:
                if (MobileDriverFactory.getDriver() instanceof AndroidDriver) {
                    return element.getAttribute(ATTRIBUTE_NAME_FOR_ANDROID_RESOURCE_ID);
                }
            case AndroidProperties.ANDROID_CONTENT_DESC:
                if (MobileDriverFactory.getDriver() instanceof AndroidDriver) {
                    return element.getAttribute(ATTRIBUTE_NAME_FOR_ANDROID_CONTENT_DESC);
                }
            default:
                try {
                    return element.getAttribute(attributeName);
                } catch (NoSuchElementException e) {
                    // attribute not found, return null
                    return null;
                }
        }
    }

    public static void checkXAndY(Number x, Number y) {
        logger.logDebug(StringConstants.COMM_LOG_INFO_CHECKING_X);
        if (x == null) {
            throw new StepFailedException(
                    MessageFormat.format(StringConstants.KW_MSG_FAILED_PARAM_X_CANNOT_BE_NULL, "x"));
        }
        logger.logDebug(StringConstants.COMM_LOG_INFO_CHECKING_Y);
        if (y == null) {
            throw new StepFailedException(
                    MessageFormat.format(StringConstants.KW_MSG_FAILED_PARAM_X_CANNOT_BE_NULL, "y"));
        }
    }
    
    private static DevicePixelRatio caculateDevicePixelRatio(AppiumDriver<? extends WebElement> driver)
            throws MobileException {
        BufferedImage fullShot = MobileScreenCaptor.takeScreenshot(driver);
        int actualWidth = fullShot.getWidth();
        Dimension deviceSize = driver.manage().window().getSize();
        int deviceWidth = deviceSize.getWidth();
        double ratioX = (actualWidth * 1.0) / deviceWidth;
        return new DevicePixelRatio(ratioX, ratioX);
    }
    
    private static int getStatusBarHeight(AppiumDriver<? extends WebElement> driver, DevicePixelRatio pixelRatio) {
        if (driver instanceof AndroidDriver) {
            return AndroidHelper.getStatusBarHeightAndroid((AndroidDriver<? extends WebElement>)driver);
        }
        if (driver instanceof IOSDriver) {
            return IOSHelper.getStatusBarHeight(driver, pixelRatio);
        }
        return 0;
    }
    
    public static void setCommonAppiumSessionProperties(AppiumDriver<? extends WebElement> driver) {
        AppiumDriverSession session = AppiumSessionCollector.getSession(driver);
        DevicePixelRatio devicePixelRatio;
        
        try {
            devicePixelRatio = caculateDevicePixelRatio(driver);
        } catch (MobileException e) {
            devicePixelRatio = null;
        }
        
        if (driver instanceof IOSDriver) {
            String bundleID = IOSHelper.getActiveAppInfo((IOSDriver<? extends WebElement>)driver);
            session.getProperties().put(PROPERTY_NAME_IOS_BUNDLEID, bundleID);
        }
        int statusbarHeight = getStatusBarHeight(driver, devicePixelRatio);
        session.getProperties().put(PROPERTY_NAME_DEVICE_PIXEL_RATIO, devicePixelRatio);
        session.getProperties().put(PROPERTY_NAME_OS_STATUS_BAR_HEIGHT, statusbarHeight);
    }
    
    public static int getStatusBarHeight(AppiumDriver<? extends WebElement> driver, boolean useDevicePixelRation) {
        AppiumDriverSession session = AppiumSessionCollector.getSession(driver);
        DevicePixelRatio ratio = useDevicePixelRation ? (DevicePixelRatio) session.getProperties()
                .get(PROPERTY_NAME_DEVICE_PIXEL_RATIO) : new DevicePixelRatio(1.0, 1.0);
        int statusBarHeight = (int)session.getProperties().get(PROPERTY_NAME_OS_STATUS_BAR_HEIGHT);
        return (int)(statusBarHeight * ratio.ratioY);
    }
    
    public static DevicePixelRatio getDevicePixelRatio(AppiumDriver<? extends WebElement> driver) {
        AppiumDriverSession session = AppiumSessionCollector.getSession(driver);
        DevicePixelRatio pixelRatio = (DevicePixelRatio)session.getProperties().get(PROPERTY_NAME_DEVICE_PIXEL_RATIO);
        return pixelRatio != null ? pixelRatio : new DevicePixelRatio(1.0, 1.0);
    }
    
}

