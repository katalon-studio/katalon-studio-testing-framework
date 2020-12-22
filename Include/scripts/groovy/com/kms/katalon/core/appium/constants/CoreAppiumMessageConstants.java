package com.kms.katalon.core.appium.constants;

import org.eclipse.osgi.util.NLS;

public class CoreAppiumMessageConstants extends NLS {
    private static final String BUNDLE_NAME = "com.kms.katalon.core.appium.constants.coreAppiumMessages";

    public static String KW_LOG_MOBILE_PROPERTY_SETTING;

    public static String APPIUM_START_EXCEPTION_APPIUM_DIRECTORY_NOT_SET;

    public static String APPIUM_START_EXCEPTION_APPIUM_DIRECTORY_INVALID_CANNOT_FIND_APPIUM_JS;

    public static String APPIUM_STARTED_ON_PORT;

    public static String APPIUM_NOT_STARTED;

    public static String CANNOT_CONNECT_TO_APPIUM_AFTER_X;

    public static String CANNOT_START_MOBILE_DRIVER_INVALID_TYPE;

    public static String CONF_EXECUTED_DEVICE_NAME;

    public static String CONF_EXECUTED_DEVICE_MANUFACTURER;

    public static String CONF_EXECUTED_DEVICE_MODEL;

    public static String CONF_EXECUTED_DEVICE_OS;

    public static String CONF_EXECUTED_DEVICE_OS_VERSON;

    public static String ERR_MSG_CANNOT_START_APPIUM_SERVER_AFTER_X_SECONDS;

    public static String XML_LOG_APPIUM_VERSION;

    public static String XML_LOG_APPIUM_STATUS;

    public static String MSG_UNABLE_TO_GET_APPIUM_STATUS;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, CoreAppiumMessageConstants.class);
    }

    private CoreAppiumMessageConstants() {
    }
}
