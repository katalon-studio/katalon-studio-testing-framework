package com.kms.katalon.core.mobile.keyword.internal;

import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.HttpCommandExecutor;

import com.google.common.base.Preconditions;
import com.kms.katalon.core.appium.driver.AppiumDriverManager;
import com.kms.katalon.core.appium.exception.AppiumStartException;
import com.kms.katalon.core.appium.exception.IOSWebkitStartException;
import com.kms.katalon.core.appium.exception.MobileDriverInitializeException;
import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.driver.DriverType;
import com.kms.katalon.core.driver.ExistingDriverType;
import com.kms.katalon.core.exception.StepFailedException;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.mobile.constants.CoreMobileMessageConstants;
import com.kms.katalon.core.mobile.constants.StringConstants;
import com.kms.katalon.core.mobile.driver.MobileDriverType;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.MobileCapabilityType;

public class MobileDriverFactory {

    private static final KeywordLogger logger = KeywordLogger.getInstance(MobileDriverFactory.class);

    private static final String WAIT_FOR_APP_SCRIPT_TRUE = "true;";

    private static final String WAIT_FOR_APP_SCRIPT = "waitForAppScript";

    private static final String NO_RESET = "noReset";

    private static final String FULL_RESET = "fullReset";

    public static final String MOBILE_DRIVER_PROPERTY = StringConstants.CONF_PROPERTY_MOBILE_DRIVER;

    public static final String EXISTING_DRIVER_PROPERTY = StringConstants.CONF_PROPERTY_EXISTING_DRIVER;

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
     * Get the native app mobile driver type of the current active driver
     * 
     * @see MobileDriverType
     * @return the driver type
     */
    public static MobileDriverType getMobileDriverType() {
        String mobileDriverType = RunConfiguration.getDriverSystemProperty(MOBILE_DRIVER_PROPERTY,
                AppiumDriverManager.EXECUTED_PLATFORM);
        return mobileDriverType != null ? MobileDriverType.valueOf(mobileDriverType) : null;
    }

    /**
     * @return the remote web driver url if running Mobile keyword on cloud services
     */
    public static String getRemoteWebDriverServerUrl() {
        return RunConfiguration.getDriverSystemProperty(RunConfiguration.REMOTE_DRIVER_PROPERTY, "remoteWebDriverUrl");
    }

    /**
     * @return the remote web driver type if running Mobile keyword on cloud services
     */
    public static String getRemoteWebDriverType() {
        return RunConfiguration.getDriverSystemProperty(RunConfiguration.REMOTE_DRIVER_PROPERTY, "browserType");
    }

    public static MobileDriverType getRemoteMobileDriver() {
        return MobileDriverType.valueOf(RunConfiguration
                .getDriverSystemProperty(RunConfiguration.REMOTE_DRIVER_PROPERTY, "remoteMobileDriver"));
    }

    /**
     * @return the remote web driver url if running Mobile keyword on cloud services
     */
    public static Map<String, Object> getRemoteWebDriverPreferences() {
        return RunConfiguration.getDriverPreferencesProperties(RunConfiguration.REMOTE_DRIVER_PROPERTY);
    }

    /**
     * Get the platform of the current active driver
     * <p>
     * Possible values: iOS, Android
     * 
     * @return the platform
     */
    public static String getDevicePlatform() {
        return getMobileDriverType().toString();
    }

    /**
     * Get the id of the current mobile device
     * 
     * @return the id of the current mobile device
     */
    public static String getDeviceId() {
        return AppiumDriverManager.getDeviceId(MOBILE_DRIVER_PROPERTY);
    }

    /**
     * Get the name of the current mobile device
     * 
     * @return the name of the current mobile device
     */
    public static String getDeviceName() {
        return AppiumDriverManager.getDeviceName(MOBILE_DRIVER_PROPERTY);
    }

    /**
     * Get the model of the current mobile device
     * 
     * @return the model of the current mobile device
     */
    public static String getDeviceModel() {
        return AppiumDriverManager.getDeviceModel(MOBILE_DRIVER_PROPERTY);
    }

    /**
     * Get the manufacturer of the current mobile device
     * 
     * @return the manufacturer of the current mobile device
     */
    public static String getDeviceManufacturer() {
        return AppiumDriverManager.getDeviceManufacturer(MOBILE_DRIVER_PROPERTY);
    }

    /**
     * Get the os version of the current mobile device
     * 
     * @return the os version of the current mobile device
     */
    public static String getDeviceOSVersion() {
        return AppiumDriverManager.getDeviceOSVersion(MOBILE_DRIVER_PROPERTY);
    }

    /**
     * Get the os of the current mobile device
     * 
     * @return the os of the current mobile device
     */
    public static String getDeviceOS() {
        return AppiumDriverManager.getDeviceOS(MOBILE_DRIVER_PROPERTY);
    }

    /**
     * Get the current active native app mobile driver
     * 
     * @return the active mobile driver as an {@link AppiumDriver} object
     * @throws StepFailedException
     */
    public static AppiumDriver<?> getDriver() throws StepFailedException {
        try {
            AppiumDriver<?> driver = AppiumDriverManager.getDriver();
            return driver;
        } catch (StepFailedException e) {
            if (isUsingExistingDriver()) {
                try {
                    return startExistingBrowser();
                } catch (MalformedURLException | MobileDriverInitializeException exception) {
                    // Ignore this
                }
            }
            throw e;
        }
    }

    /**
     * Close the current active mobile driver
     */
    public static void closeDriver() {
        AppiumDriverManager.closeDriver();
    }

    private static DesiredCapabilities convertPropertiesMaptoDesireCapabilities(Map<String, Object> propertyMap,
            MobileDriverType mobileDriverType) {
        DesiredCapabilities desireCapabilities = new DesiredCapabilities();
        for (Entry<String, Object> property : propertyMap.entrySet()) {
            logger.logInfo(MessageFormat.format(StringConstants.KW_LOG_MOBILE_PROPERTY_SETTING, property.getKey(),
                    property.getValue()));
            desireCapabilities.setCapability(property.getKey(), property.getValue());
        }
        return desireCapabilities;
    }

    private static DesiredCapabilities createCapabilities(MobileDeviceInfo mobileDeviceInfo, String appId) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        Map<String, Object> driverPreferences = RunConfiguration.getDriverPreferencesProperties(MOBILE_DRIVER_PROPERTY);
        // Running app so no browser name
        if (driverPreferences.containsKey(MobileCapabilityType.BROWSER_NAME)) {
            driverPreferences.remove(MobileCapabilityType.BROWSER_NAME);
        }
        if (driverPreferences != null && mobileDeviceInfo.getDriverType() == MobileDriverType.IOS_DRIVER) {
            capabilities.setCapability("bundleId", appId);
            capabilities.setCapability(WAIT_FOR_APP_SCRIPT, WAIT_FOR_APP_SCRIPT_TRUE);
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
            if (mobileDeviceInfo.getDeviceId() == null) {
                capabilities.setCapability(MobileCapabilityType.PLATFORM, getDeviceOS());
            }
            capabilities
                    .merge(convertPropertiesMaptoDesireCapabilities(driverPreferences, MobileDriverType.IOS_DRIVER));
        } else if (driverPreferences != null && mobileDeviceInfo.getDriverType() == MobileDriverType.ANDROID_DRIVER) {
            // Launch Android Settings app at default
            capabilities.setCapability("appPackage", "com.android.settings");
            capabilities.setCapability("appActivity", ".Settings");
            capabilities.setPlatform(Platform.ANDROID);
            if (isUsingAndroid7OrBigger()) {
                capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AppiumDriverManager.UIAUTOMATOR2);
            }
            capabilities.setCapability("autoGrantPermissions", true);
            capabilities.setCapability(AppiumDriverManager.SYSTEM_PORT, AppiumDriverManager.nextFreePort(8200, 8299));
            capabilities.merge(
                    convertPropertiesMaptoDesireCapabilities(driverPreferences, MobileDriverType.ANDROID_DRIVER));
        }
        if (StringUtils.isNotEmpty(mobileDeviceInfo.getDeviceOS())) {
            capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, mobileDeviceInfo.getDeviceOS());
        }
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, mobileDeviceInfo.getDeviceName());
        if (mobileDeviceInfo.getDeviceId() != null) {
            capabilities.setCapability(MobileCapabilityType.UDID, mobileDeviceInfo.getDeviceId());
        }
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 1800);
        return capabilities;
    }

    private static DesiredCapabilities createCapabilities(MobileDriverType osType, String deviceId, String deviceName,
            String platformVersion, String appFile, boolean uninstallAfterCloseApp) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        Map<String, Object> driverPreferences = RunConfiguration.getDriverPreferencesProperties(MOBILE_DRIVER_PROPERTY);
        // Running app so no browser name
        if (driverPreferences.containsKey(MobileCapabilityType.BROWSER_NAME)) {
            driverPreferences.remove(MobileCapabilityType.BROWSER_NAME);
        }
        if (driverPreferences != null && osType == MobileDriverType.IOS_DRIVER) {
            capabilities.setCapability(WAIT_FOR_APP_SCRIPT, WAIT_FOR_APP_SCRIPT_TRUE);
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
            if (deviceId == null) {
                capabilities.setCapability(MobileCapabilityType.PLATFORM, getDeviceOS());
            }
            capabilities
                    .merge(convertPropertiesMaptoDesireCapabilities(driverPreferences, MobileDriverType.IOS_DRIVER));
        } else if (driverPreferences != null && osType == MobileDriverType.ANDROID_DRIVER) {
            capabilities.setPlatform(Platform.ANDROID);
            if (isUsingAndroid7OrBigger()) {
                capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AppiumDriverManager.UIAUTOMATOR2);
            }
            capabilities.setCapability("autoGrantPermissions", true);
            capabilities.setCapability(AppiumDriverManager.SYSTEM_PORT, AppiumDriverManager.getFreePort());
            capabilities.merge(
                    convertPropertiesMaptoDesireCapabilities(driverPreferences, MobileDriverType.ANDROID_DRIVER));
        }
        if (StringUtils.isNotEmpty(platformVersion)) {
            capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
        }
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
        capabilities.setCapability(MobileCapabilityType.APP, appFile);
        if (deviceId != null) {
            capabilities.setCapability(MobileCapabilityType.UDID, deviceId);
        }
        capabilities.setCapability(FULL_RESET, uninstallAfterCloseApp);
        capabilities.setCapability(NO_RESET, !uninstallAfterCloseApp);
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 1800);
        return capabilities;
    }

    public static boolean isUsingAndroid7OrBigger() {
        try {
            String osVersion = getDeviceOSVersion();
            if (StringUtils.isEmpty(osVersion)) {
                return false;
            }
            final String[] splitParts = osVersion.split("\\.");
            if (splitParts == null || splitParts.length == 0) {
                return false;
            }
            String osVersionMajor = splitParts[0];
            Number androidVersion = NumberUtils.createNumber(osVersionMajor);
            return androidVersion.intValue() >= 7;
        } catch (NumberFormatException e) {
            // Exception happened, ignore
        }
        return false;
    }

    private static boolean isUsingExistingDriver() {
        return getExistingDriver() != null;
    }

    public static AppiumDriver<?> startMobileDriver(String appId) throws AppiumStartException, IOException,
            InterruptedException, MobileDriverInitializeException, IOSWebkitStartException {
        if (isUsingExistingDriver()) {
            return startExistingBrowser();
        }
        MobileDeviceInfo mobileDeviceInfo = MobileDeviceInfo.create(getMobileDriverType(), getDeviceId(),
                getDeviceName(), getDeviceOS(), getDeviceOSVersion());
        AppiumDriver<?> driver;
        String remoteWebUrl = getRemoteWebDriverServerUrl();
        if (StringUtils.isNotEmpty(remoteWebUrl)) {
            driver = AppiumDriverManager.createMobileDriver(mobileDeviceInfo.getDriverType(),
                    createCapabilities(mobileDeviceInfo, appId), new URL(remoteWebUrl));
        } else {
            driver = AppiumDriverManager.createMobileDriver(mobileDeviceInfo.getDriverType(),
                    mobileDeviceInfo.getDeviceId(), createCapabilities(mobileDeviceInfo, appId));
        }
        if (driver != null) {
            saveWebDriverSessionData(driver);
        }
        return driver;
    }

    /**
     * Start a new native app mobile driver
     * 
     * @param appFile the absolute path of the application file
     * @param uninstallAfterCloseApp true to un-install the app after execution
     * @return the newly created driver with type {@link AppiumDriver}
     * @throws AppiumStartException
     * @throws IOException
     * @throws InterruptedException
     * @throws MobileDriverInitializeException
     * @throws IOSWebkitStartException
     */
    public static AppiumDriver<?> startMobileDriver(String appFile, boolean uninstallAfterCloseApp)
            throws AppiumStartException, IOException, InterruptedException, MobileDriverInitializeException,
            IOSWebkitStartException {
        if (isUsingExistingDriver()) {
            return startExistingBrowser();
        }
        AppiumDriver<?> driver;
        String remoteWebUrl = getRemoteWebDriverServerUrl();
        if (StringUtils.isNotEmpty(remoteWebUrl)) {
            driver = startRemoteMobileDriver(remoteWebUrl, new DesiredCapabilities(getRemoteWebDriverPreferences()),
                    getRemoteMobileDriver(), appFile);
        } else {
            driver = startLocalMobileDriver(getMobileDriverType(), getDeviceId(), getDeviceName(), getDeviceOSVersion(),
                    appFile, uninstallAfterCloseApp);
        }
        if (driver != null) {
            saveWebDriverSessionData(driver);
        }
        return driver;
    }

    private static void saveWebDriverSessionData(AppiumDriver<?> remoteWebDriver) {
        try (Socket myClient = new Socket(RunConfiguration.getSessionServerHost(),
                RunConfiguration.getSessionServerPort());
                PrintStream output = new PrintStream(myClient.getOutputStream())) {
            output.println(remoteWebDriver.getSessionId());
            output.println(getWebDriverServerUrl(remoteWebDriver));
            output.println(getMobileDriverType().toString());
            output.println(RunConfiguration.getLogFolderPath());
            DriverType executedDriver = getExecutedDriver();
            if (getRemoteWebDriverServerUrl() != null) {
                output.println(getRemoteWebDriverType());
                output.println(getDeviceName() + " " + getDeviceOSVersion());
            } else {
                output.println("");
                if (executedDriver == MobileDriverType.ANDROID_DRIVER) {
                    output.println(getDeviceManufacturer() + " " + getDeviceModel() + " " + getDeviceOSVersion());
                } else if (executedDriver == MobileDriverType.IOS_DRIVER) {
                    output.println(getDeviceName() + " " + getDeviceOSVersion());
                }
            }
            output.flush();
        } catch (Exception e) {
            // Ignore for this exception
        }
    }

    /**
     * Get the native app mobile driver type of the current active driver
     * 
     * @see MobileDriverType
     * @return the driver type
     */
    public static DriverType getExecutedDriver() {
        ExistingDriverType existingDriver = getExistingDriver();
        return existingDriver != null ? existingDriver : getMobileDriverType();
    }

    private static ExistingDriverType getExistingDriver() {
        if (RunConfiguration.getDriverSystemProperties(EXISTING_DRIVER_PROPERTY) != null) {
            return new ExistingDriverType(StringUtils.EMPTY);
        }
        return null;
    }

    private static String getWebDriverServerUrl(AppiumDriver<?> remoteWebDriver) {
        return ((HttpCommandExecutor) remoteWebDriver.getCommandExecutor()).getAddressOfRemoteServer().toString();
    }

    protected static AppiumDriver<?> startExistingBrowser()
            throws MalformedURLException, MobileDriverInitializeException {
        return AppiumDriverManager.startExisitingMobileDriver(
                MobileDriverType.fromStringValue(RunConfiguration.getExistingSessionDriverType()),
                RunConfiguration.getExistingSessionSessionId(), RunConfiguration.getExistingSessionServerUrl());
    }

    /**
     * Start a new native app mobile driver
     * 
     * @param osType the os type for the new mobile driver with type {@link MobileDriverType}
     * @param deviceId id of the device
     * @param deviceName name of the device
     * @param platformVersion platform version of the device
     * @param appFile absolute path of the application file
     * @param uninstallAfterCloseApp true to un-install the app after execution
     * @return the newly created driver with type {@link AppiumDriver}
     * @throws MobileDriverInitializeException
     * @throws IOException
     * @throws InterruptedException
     * @throws AppiumStartException
     */
    public static AppiumDriver<?> startLocalMobileDriver(MobileDriverType osType, String deviceId, String deviceName,
            String platformVersion, String appFile, boolean uninstallAfterCloseApp)
            throws MobileDriverInitializeException, IOException, InterruptedException, AppiumStartException {
        Preconditions.checkArgument(osType != null && StringUtils.isNotEmpty(deviceName),
                CoreMobileMessageConstants.KW_MSG_DEVICE_MISSING);
        Preconditions.checkArgument(StringUtils.isNotEmpty(appFile),
                CoreMobileMessageConstants.KW_MSG_APP_FILE_MISSING);
        return AppiumDriverManager.createMobileDriver(osType, deviceId,
                createCapabilities(osType, deviceId, deviceName, platformVersion, appFile, uninstallAfterCloseApp));
    }

    public static AppiumDriver<?> startRemoteMobileDriver(String remoteWebUrl, DesiredCapabilities desiredCapabilities,
            MobileDriverType driverType, String appFile) throws MalformedURLException, MobileDriverInitializeException {
        desiredCapabilities.setCapability("app", appFile);
        return AppiumDriverManager.createMobileDriver(driverType, desiredCapabilities, new URL(remoteWebUrl));
    }
}
