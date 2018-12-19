package com.kms.katalon.core.webui.driver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.MobileCapabilityType;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.kms.katalon.core.appium.constants.AppiumStringConstants;
import com.kms.katalon.core.appium.driver.AppiumDriverManager;
import com.kms.katalon.core.appium.exception.AppiumStartException;
import com.kms.katalon.core.appium.exception.MobileDriverInitializeException;
import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.driver.DriverType;
import com.kms.katalon.core.exception.StepFailedException;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.webui.constants.StringConstants;

public class WebMobileDriverFactory {
    
    private static final KeywordLogger logger = KeywordLogger.getInstance(WebMobileDriverFactory.class);

    private static final String CHROME = "Chrome";

    private static final String SAFARI = "Safari";

    public static final String MOBILE_DRIVER_PROPERTY = StringConstants.CONF_PROPERTY_MOBILE_DRIVER;

    public static final String EXECUTED_MOBILE_PLATFORM = AppiumStringConstants.CONF_EXECUTED_PLATFORM;

    public static final String EXECUTED_MOBILE_DEVICE_ID = AppiumStringConstants.CONF_EXECUTED_DEVICE_ID;

    /**
     * Clean up all running drivers and processes
     * 
     * @throws InterruptedException
     * @throws IOException
     */
    public static void cleanup() throws InterruptedException, IOException {
        AppiumDriverManager.cleanup();
    }

    /**
     * Get the current active web mobile driver
     * 
     * @return the active mobile driver as an {@link AppiumDriver} object
     * @throws StepFailedException
     */
    public static AppiumDriver<?> getDriver() throws StepFailedException {
        return AppiumDriverManager.getDriver();
    }

    /**
     * Close the current active mobile driver
     */
    public static void closeDriver() {
        AppiumDriverManager.closeDriver();
    }

    private static DesiredCapabilities toDesireCapabilities(Map<String, Object> propertyMap,
            WebUIDriverType WebUIDriverType) {
        DesiredCapabilities desireCapabilities = new DesiredCapabilities();
        for (Entry<String, Object> property : propertyMap.entrySet()) {
            logger.logInfo(MessageFormat.format(StringConstants.KW_LOG_WEB_UI_PROPERTY_SETTING,
                    property.getKey(), property.getValue()));
            desireCapabilities.setCapability(property.getKey(), property.getValue());
        }
        return desireCapabilities;
    }

    private static DesiredCapabilities createCapabilities(WebUIDriverType osType) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        Map<String, Object> driverPreferences = RunConfiguration.getDriverPreferencesProperties(MOBILE_DRIVER_PROPERTY);
        String deviceId = getDeviceId();
        if (driverPreferences != null && osType == WebUIDriverType.IOS_DRIVER) {
            capabilities.merge(toDesireCapabilities(driverPreferences, WebUIDriverType.IOS_DRIVER));
            capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, SAFARI);
            if (deviceId == null) {
                capabilities.setCapability(MobileCapabilityType.PLATFORM, getDeviceOS());
                capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, getDeviceOSVersion());
            }
            try {
                if (AppiumDriverManager.getXCodeVersion() >= 8) {
                    capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AppiumDriverManager.XCUI_TEST);
                    capabilities.setCapability(AppiumDriverManager.REAL_DEVICE_LOGGER,
                            RunConfiguration.getDeviceConsoleExecutable());
                    capabilities.setCapability(AppiumDriverManager.WDA_LOCAL_PORT, AppiumDriverManager.getFreePort());
                }
            } catch (ExecutionException e) {
                // XCode version not found, ignore this
            }
        } else if (driverPreferences != null && osType == WebUIDriverType.ANDROID_DRIVER) {
            capabilities.merge(toDesireCapabilities(driverPreferences, WebUIDriverType.ANDROID_DRIVER));
            capabilities.setPlatform(Platform.ANDROID);
            capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, CHROME);
        }
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, getDeviceName());
        if (deviceId != null) {
            capabilities.setCapability(MobileCapabilityType.UDID, deviceId);
        }
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 1800);
        return capabilities;
    }

    public static AppiumDriver<?> startExisitingMobileDriver(DriverType driverType, String sessionId,
            String remoteServerUrl) throws MalformedURLException, MobileDriverInitializeException {
        return AppiumDriverManager.startExisitingMobileDriver(driverType, sessionId, remoteServerUrl);
    }

    /**
     * Create a new web mobile driver
     * 
     * @param osType the os type of the new mobile driver with type {@link WebUIDriverType}
     * @return the newly created mobile driver
     * @throws MobileDriverInitializeException
     * @throws IOException
     * @throws InterruptedException
     * @throws AppiumStartException
     */
    public static AppiumDriver<?> createMobileDriver(WebUIDriverType osType)
            throws MobileDriverInitializeException, IOException, InterruptedException, AppiumStartException {
        return AppiumDriverManager.createMobileDriver(osType, getDeviceId(), createCapabilities(osType));
    }

    /**
     * Get the web mobile driver type of the current active driver
     * 
     * @see WebUIDriverType
     * @return the driver type
     */
    public static WebUIDriverType getWebMobileDriverType() {
        return WebUIDriverType.valueOf(RunConfiguration.getDriverSystemProperty(DriverFactory.WEB_UI_DRIVER_PROPERTY,
                AppiumDriverManager.EXECUTED_PLATFORM));
    }

    /**
     * Get the platform of the current active driver
     * <p>
     * Possible values: iOS, Android
     * 
     * @return the platform
     */
    public static String getDevicePlatform() {
        return getWebMobileDriverType().toString();
    }

    /**
     * Get the id of the current mobile device
     * 
     * @return the id of the current mobile device
     */
    public static String getDeviceId() {
        return AppiumDriverManager.getDeviceId(DriverFactory.MOBILE_DRIVER_PROPERTY);
    }

    /**
     * Get the name of the current mobile device
     * 
     * @return the name of the current mobile device
     */
    public static String getDeviceName() {
        return AppiumDriverManager.getDeviceName(DriverFactory.MOBILE_DRIVER_PROPERTY);
    }

    /**
     * Get the model of the current mobile device
     * 
     * @return the model of the current mobile device
     */
    public static String getDeviceModel() {
        return AppiumDriverManager.getDeviceModel(DriverFactory.MOBILE_DRIVER_PROPERTY);
    }

    /**
     * Get the manufacturer of the current mobile device
     * 
     * @return the manufacturer of the current mobile device
     */
    public static String getDeviceManufacturer() {
        return AppiumDriverManager.getDeviceManufacturer(DriverFactory.MOBILE_DRIVER_PROPERTY);
    }

    /**
     * Get the os version of the current mobile device
     * 
     * @return the os version of the current mobile device
     */
    public static String getDeviceOSVersion() {
        return AppiumDriverManager.getDeviceOSVersion(DriverFactory.MOBILE_DRIVER_PROPERTY);
    }

    /**
     * Get the os of the current mobile device
     * 
     * @return the os of the current mobile device
     */
    public static String getDeviceOS() {
        return AppiumDriverManager.getDeviceOS(DriverFactory.MOBILE_DRIVER_PROPERTY);
    }
}
