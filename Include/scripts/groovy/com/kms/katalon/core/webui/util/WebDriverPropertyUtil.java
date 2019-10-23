package com.kms.katalon.core.webui.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.util.internal.JsonUtil;
import com.kms.katalon.core.webui.constants.StringConstants;
import com.kms.katalon.core.webui.driver.WebUIDriverType;
import com.kms.katalon.selenium.firefox.CFirefoxProfile;

public class WebDriverPropertyUtil {
    
    private static final KeywordLogger logger = KeywordLogger.getInstance(WebDriverPropertyUtil.class);
    
    public static final String DISABLE_EXTENSIONS = "--disable-extensions";
    public static final String CHROME_SWITCHES = "chrome.switches";
    public static final String CHROME_NO_SANDBOX = "--no-sandbox";
    private static final String CHROME_ARGUMENT_PROPERTY_KEY = "args";
    private static final String CHROME_BINARY_PROPERTY_KEY = "binary";
    private static final String CHROME_EXTENSIONS_PROPERTY_KEY = "extensions";
    private static final String CHROME_PREFERENCES_PROPERTY_KEY = "prefs";
    private static final String CHROME_LOCALSTATE_PROPERTY_KEY = "localState";
    private static final String CHROME_DETACH_PROPERTY_KEY = "detach";
    private static final String CHROME_DEBUGGER_ADDRESS_PROPERTY_KEY = "debuggerAddress";
    private static final String CHROME_EXCLUDE_SWITCHES_PROPERTY_KEY = "excludeSwitches";
    private static final String CHROME_MINI_DUMP_PATH_PROPERTY_KEY = "minidumpPath";
    private static final String CHROME_MOBILE_EMULATION_PROPERTY_KEY = "mobileEmulation";
    private static final String CHROME_PREF_LOGGING_PREFS_PROPERTY_KEY = "perfLoggingPrefs";
    private static final String[] CHROME_CAPABILITIES = { CHROME_ARGUMENT_PROPERTY_KEY, CHROME_BINARY_PROPERTY_KEY,
            CHROME_EXTENSIONS_PROPERTY_KEY, CHROME_PREFERENCES_PROPERTY_KEY, CHROME_LOCALSTATE_PROPERTY_KEY,
            CHROME_DETACH_PROPERTY_KEY, CHROME_DEBUGGER_ADDRESS_PROPERTY_KEY, CHROME_EXCLUDE_SWITCHES_PROPERTY_KEY,
            CHROME_MINI_DUMP_PATH_PROPERTY_KEY, CHROME_MOBILE_EMULATION_PROPERTY_KEY,
            CHROME_PREF_LOGGING_PREFS_PROPERTY_KEY };

    private static final String STARTUP_HOMEPAGE_WELCOME_URL_ADDITIONAL_PREFERENCE = "startup.homepage_welcome_url.additional";
    private static final String STARTUP_HOMEPAGE_WELCOME_URL_PREFERENCE = "startup.homepage_welcome_url";
    private static final String BROWSER_STARTUP_HOMEPAGE_PREFERENCE = "browser.startup.homepage";
    private static final String FIREFOX_BLANK_PAGE = "about:blank";

    public static final String KATALON_DOCKER_ENV_KEY = "KATALON_DOCKER";

    public static DesiredCapabilities toDesireCapabilities(Map<String, Object> propertyMap,
            WebUIDriverType webUIDriverType) {
        if (propertyMap == null) {
            return null;
        }
        switch (webUIDriverType) {
        case CHROME_DRIVER:
        case HEADLESS_DRIVER:
            return getDesireCapabilitiesForChrome(propertyMap);
        case FIREFOX_DRIVER:
        case FIREFOX_HEADLESS_DRIVER:
            return getDesireCapabilitiesForFirefox(propertyMap);
        default:
            return toDesireCapabilities(propertyMap);
        }
    }

    public static DesiredCapabilities toDesireCapabilities(Map<String, Object> propertyMap) {
        return toDesireCapabilities(propertyMap, new DesiredCapabilities(), true);
    }

    public static DesiredCapabilities toDesireCapabilities(Map<String, Object> propertyMap,
            DesiredCapabilities desireCapabilities, boolean isLog) {
        for (Entry<String, Object> property : propertyMap.entrySet()) {
            if (isLog) {
                logger.logInfo(
                        MessageFormat.format(StringConstants.KW_LOG_WEB_UI_PROPERTY_SETTING, property.getKey(),
                                property.getValue()));
            }
            desireCapabilities.setCapability(property.getKey(), property.getValue());
        }
        return desireCapabilities;
    }

    public static DesiredCapabilities getDesireCapabilitiesForFirefox(Map<String, Object> propertyMap) {
        DesiredCapabilities desireCapabilities = DesiredCapabilities.firefox();
        FirefoxProfile firefoxProfile = createDefaultFirefoxProfile();
        for (Entry<String, Object> property : propertyMap.entrySet()) {
            if (property.getKey().equals(FirefoxDriver.PROFILE) && property.getValue() instanceof Map<?, ?>) {
                processFirefoxPreferencesSetting(firefoxProfile, (Map<?, ?>) property.getValue());
            } else {
                desireCapabilities.setCapability(property.getKey(), property.getValue());
                logger.logInfo(
                        MessageFormat.format(StringConstants.KW_LOG_WEB_UI_PROPERTY_SETTING, property.getKey(),
                                property.getValue()));
            }
        }
        desireCapabilities.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
        return desireCapabilities;
    }

    private static void processFirefoxPreferencesSetting(FirefoxProfile firefoxProfile, Map<?, ?> firefoxPropertyMap) {
        for (Entry<?, ?> entry : firefoxPropertyMap.entrySet()) {
            if (!(entry.getKey() instanceof String)) {
                continue;
            }
            String entryKey = (String) entry.getKey();
            if (setFirefoxPreferenceValue(firefoxProfile, entryKey, entry.getValue())) {
                logger.logInfo(
                        MessageFormat.format(StringConstants.KW_LOG_FIREFOX_PROPERTY_SETTING, entryKey,
                                entry.getValue()));
            }
        }
    }

    private static boolean setFirefoxPreferenceValue(FirefoxProfile firefoxProfile, String entryKey, Object entryValue) {
        if (entryValue instanceof Number) {
            firefoxProfile.setPreference(entryKey, ((Number) entryValue).intValue());
            return true;
        }
        if (entryValue instanceof Boolean) {
            firefoxProfile.setPreference(entryKey, (Boolean) entryValue);
            return true;
        }
        if (entryValue instanceof String) {
            firefoxProfile.setPreference(entryKey, (String) entryValue);
            return true;
        }
        return false;
    }

    public static FirefoxProfile createDefaultFirefoxProfile() {
        FirefoxProfile firefoxProfile = new CFirefoxProfile();
        firefoxProfile.setPreference(BROWSER_STARTUP_HOMEPAGE_PREFERENCE, FIREFOX_BLANK_PAGE);
        firefoxProfile.setPreference(STARTUP_HOMEPAGE_WELCOME_URL_PREFERENCE, FIREFOX_BLANK_PAGE);
        firefoxProfile.setPreference(STARTUP_HOMEPAGE_WELCOME_URL_ADDITIONAL_PREFERENCE, FIREFOX_BLANK_PAGE);
        return firefoxProfile;
    }

    public static DesiredCapabilities getDesireCapabilitiesForChrome(Map<String, Object> propertyMap) {
        DesiredCapabilities desireCapabilities = DesiredCapabilities.chrome();
        Map<String, Object> chromeOptions = new HashMap<String, Object>();
        for (Entry<String, Object> driverProperty : propertyMap.entrySet()) {
            if (Arrays.asList(CHROME_CAPABILITIES).contains(driverProperty.getKey())) {
                chromeOptions.put(driverProperty.getKey(), driverProperty.getValue());
            } else {
                desireCapabilities.setCapability(driverProperty.getKey(), driverProperty.getValue());
            }
            logger.logInfo(
                    MessageFormat.format(StringConstants.KW_LOG_WEB_UI_PROPERTY_SETTING, driverProperty.getKey(),
                            driverProperty.getValue()));
        }
        injectAddtionalArgumentsForChrome(chromeOptions);
        desireCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        return desireCapabilities;
    }

    private static void injectAddtionalArgumentsForChrome(Map<String, Object> chromeOptions) {
        if (chromeOptions == null) {
            return;
        }
        List<Object> argumentsList = new ArrayList<Object>();
        if (chromeOptions.get(CHROME_ARGUMENT_PROPERTY_KEY) instanceof List) {
            argumentsList.addAll((List<?>) chromeOptions.get(CHROME_ARGUMENT_PROPERTY_KEY));
        }
        // https://github.com/kms-technology/katalon/issues/2815
        // argumentsList.add(CHROME_SWITCHES);
        // argumentsList.add(DISABLE_EXTENSIONS);
        if (isRunningInDocker()) {
            argumentsList.add(CHROME_NO_SANDBOX);
        }
        chromeOptions.put(CHROME_ARGUMENT_PROPERTY_KEY, argumentsList);
    }

    public static void addArgumentsForChrome(DesiredCapabilities caps, String... args) {
        @SuppressWarnings("unchecked")
        Map<String, Object> chromeOptions = (Map<String, Object>) caps.getCapability(ChromeOptions.CAPABILITY);
        if (chromeOptions == null) {
            chromeOptions= new HashMap<>();
        }
        
        @SuppressWarnings("unchecked")
        List<String> argsEntry = (List<String>) chromeOptions.get(CHROME_ARGUMENT_PROPERTY_KEY);
        if (argsEntry == null) {
            argsEntry = new ArrayList<>();
        }
        argsEntry.addAll(Arrays.asList(args));
        if (isRunningInDocker()) {
            argsEntry.add(CHROME_NO_SANDBOX);
        }
        chromeOptions.put(CHROME_ARGUMENT_PROPERTY_KEY, argsEntry);
        caps.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
    }
    
    public static void removeArgumentsForChrome(DesiredCapabilities caps, String... args) {
        @SuppressWarnings("unchecked")
        Map<String, Object> chromeOptions = (Map<String, Object>) caps.getCapability(ChromeOptions.CAPABILITY);
        if (chromeOptions == null) {
            chromeOptions= new HashMap<>();
        }
        
        @SuppressWarnings("unchecked")
        List<String> argsEntry = (List<String>) chromeOptions.get(CHROME_ARGUMENT_PROPERTY_KEY);
        if (argsEntry == null) {
            argsEntry = new ArrayList<>();
        }
        argsEntry.removeAll(Arrays.asList(args));
        if (isRunningInDocker()) {
            argsEntry.add(CHROME_NO_SANDBOX);
        }
        chromeOptions.put(CHROME_ARGUMENT_PROPERTY_KEY, argsEntry);
        caps.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
    }

    public static boolean isRunningInDocker() {
        if (System.getenv().containsKey(KATALON_DOCKER_ENV_KEY)) {
            return Boolean.valueOf(System.getenv(KATALON_DOCKER_ENV_KEY));
        }
        return false;
    }
}
