package com.kms.katalon.core.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kms.katalon.constants.GlobalStringConstants;
import com.kms.katalon.core.constants.StringConstants;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.model.FailureHandling;
import com.kms.katalon.core.model.RunningMode;
import com.kms.katalon.core.network.ProxyInformation;
import com.kms.katalon.core.setting.BundleSettingStore;
import com.kms.katalon.core.setting.VideoRecorderSetting;
import com.kms.katalon.core.util.internal.JsonUtil;

/**
 * Provides access to execution properties and settings
 *
 */
@SuppressWarnings("unchecked")
public class RunConfiguration {
	
	public static final String SMART_XPATH_BUNDLE_ID = "com.katalon.katalon-studio-smart-xpath";

	public static final String OVERRIDING_GLOBAL_VARIABLES = "overridingGlobalVariables";

    public static final String REPORT_FOLDER_PATH_PROPERTY = "reportFolder";

    public static final String LOG_FILE_PATH_PROPERTY = StringConstants.CONF_PROPERTY_LOG_FILE_PATH;

    public static final String TIMEOUT_PROPERTY = StringConstants.CONF_PROPERTY_TIMEOUT;

    public static final String PROJECT_DIR_PROPERTY = StringConstants.CONF_PROPERTY_PROJECT_DIR;

    public static final String HOST = StringConstants.CONF_PROPERTY_HOST;

    public static final String HOST_NAME = StringConstants.CONF_PROPERTY_HOST_NAME;

    public static final String HOST_OS = StringConstants.CONF_PROPERTY_HOST_OS;

    public static final String HOST_ADDRESS = StringConstants.CONF_PROPERTY_HOST_ADDRESS;

    public static final String HOST_PORT = StringConstants.CONF_PROPERTY_HOST_PORT;

    public static final String EXECUTION_GENERAL_PROPERTY = StringConstants.CONF_PROPERTY_GENERAL;

    public static final String EXECUTION_DRIVER_PROPERTY = StringConstants.CONF_PROPERTY_DRIVER;

    public static final String EXECUTION_SYSTEM_PROPERTY = StringConstants.CONF_PROPERTY_EXECUTION_SYSTEM_PROPERTY;

    public static final String EXECUTION_PREFS_PROPERTY = StringConstants.CONF_PROPERTY_EXECUTION_PREFS_PROPERTY;

    public static final String EXECUTION_TEST_DATA_INFO_PROPERTY = StringConstants.CONF_PROPERTY_TEST_DATA_INFO;
    
    public static final String REMOTE_DRIVER_PROPERTY = "Remote";

    public static final String EXECUTION_PROPERTY = StringConstants.CONF_PROPERTY_EXEC;

    public static final String EXCUTION_SOURCE = StringConstants.CONF_PROPERTY_EXECUTION_SOURCE;

    public static final String EXCUTION_SOURCE_NAME = StringConstants.CONF_PROPERTY_EXECUTION_SOURCE_NAME;

    public static final String EXCUTION_SOURCE_ID = StringConstants.CONF_PROPERTY_EXECUTION_SOURCE_ID;

    public static final String EXCUTION_SOURCE_DESCRIPTION = StringConstants.CONF_PROPERTY_EXECUTION_SOURCE_DESCRIPTION;

    public static final String APP_VERSION = StringConstants.APP_VERSION;

    public static final String APP_INFO_FILE_LOCATION = StringConstants.APP_INFO_FILE_LOCATION;

    public static final String SESSION_SERVER_HOST = StringConstants.CONF_SESSION_SERVER_HOST;

    public static final String SESSION_SERVER_PORT = StringConstants.CONF_SESSION_SERVER_PORT;

    public static final String EXCUTION_DEFAULT_FAILURE_HANDLING = StringConstants.CONF_PROPERTY_DEFAULT_FAILURE_HANDLING;

    public static final String PROXY_PROPERTY = StringConstants.CONF_PROPERTY_PROXY;

    public static final String TERMINATE_DRIVER_AFTER_TEST_CASE = "terminateDriverAfterTestCase";

    public static final String TERMINATE_DRIVER_AFTER_TEST_SUITE = "terminateDriverAfterTestSuite";

    public static final String EXECUTION_PROFILE_PROPERTY = "executionProfile";
    
    public static final String LOGBACK_CONFIG_FILE_LOCATION = "logbackConfigFileLocation";
    
    public static final String RUNNING_MODE = "runningMode";

    // This property is available for record - playback mode only. 
    public static final String RECORD_CAPTURED_OBJECTS_FILE = "recordCapturedObjectsCache";
    
    public static final String AUTO_APPLY_NEIGHBOR_XPATHS = "autoApplyNeighborXpaths";
    
    public static final String PLUGIN_TEST_LISTENERS = "pluginTestListeners";
    
    public static final String LOCAL_SMART_WAIT_MODE = "localSmartWaitEnabled";
    
    public static final String GLOBAL_SMART_WAIT_MODE = "globalSmartWaitEnabled";
    
    public static final String LOG_TEST_STEPS = "logTestSteps";
    
    private static String settingFilePath;

    private static final ThreadLocal<Map<String, Object>> localExecutionSettingMapStorage = new InheritableThreadLocal<Map<String, Object>>(){
        @Override
        protected Map<String, Object> initialValue() {
            return new HashMap<String, Object>();
        }
    };
    
    private static final ThreadLocal<String> localAppiumDriverStores = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return new String();
        }
    };

    private static final ThreadLocal<List<Object>> localDriverStorage = new ThreadLocal<List<Object>>() {
        @Override
        protected List<Object> initialValue() {
            return new ArrayList<Object>();
        }
    };

    private static final ThreadLocal<Properties> applicationInfo = new ThreadLocal<Properties>() {
        @Override
        protected Properties initialValue() {
            File appPropFile = new File(APP_INFO_FILE_LOCATION);
            Properties appProp = new Properties();
            if (!appPropFile.exists()) {
                return appProp;
            }

            try {
                appProp.load(new FileInputStream(appPropFile));
            } catch (FileNotFoundException e) {
                // do nothing
            } catch (IOException e) {
                // do nothing
            }
            return appProp;
        }
    };

    public static void setAppiumLogFilePath(String logFilePath) {
        if (logFilePath == null) {
            return;
        }
        localAppiumDriverStores.set(logFilePath);
    }

    public static void setExecutionSettingFile(String executionSettingFilePath) {
        if (executionSettingFilePath == null) {
            return;
        }
        settingFilePath = executionSettingFilePath;

        File executionSettingFile = new File(executionSettingFilePath);
        if (executionSettingFile.exists() && executionSettingFile.isFile()) {
            Gson gsonObj = new Gson();
            try {
                String propertyConfigFileContent = FileUtils.readFileToString(executionSettingFile);
                Type collectionType = new TypeToken<Map<String, Object>>() {}.getType();
                Map<String, Object> result = gsonObj.fromJson(propertyConfigFileContent, collectionType);
                if (result != null) {
                    localExecutionSettingMapStorage.set(result);
                }
            } catch (IOException exception) {
                // reading file failed --> do nothing;
            } catch (JsonSyntaxException exception) {
                // parsing json failed --> do nothing;
            }
        }
    }

    public static void setExecutionSetting(Map<String, Object> executionSettingMap) {
        if (executionSettingMap == null) {
            return;
        }
        localExecutionSettingMapStorage.set(executionSettingMap);
    }

    public static void setWebDriverPreferencesProperty(String propName, Object propVal) {
        setDriverPreferencesProperty(StringConstants.CONF_PROPERTY_WEBUI_DRIVER, propName, propVal);
    }

    public static void setMobileDriverPreferencesProperty(String propName, Object propVal) {
        setDriverPreferencesProperty(StringConstants.CONF_PROPERTY_MOBILE_DRIVER, propName, propVal);
    }

    private static void setDriverPreferencesProperty(String driverType, String propName, Object propVal) {
        Map<String, Object> props = RunConfiguration.getDriverPreferencesProperties(driverType);
        if (props != null) {
            props.put(propName, propVal);
        }
    }

    public static Object getProperty(String propertyKey) {
        return localExecutionSettingMapStorage.get().get(propertyKey);
    }

    public static String getStringProperty(String propertyKey) {
        return String.valueOf(getProperty(propertyKey));
    }

    public static String getStringProperty(String propertyKey, Map<String, Object> jsonObjProperties) {
        return String.valueOf(jsonObjProperties.get(propertyKey));
    }

    public static int getIntProperty(String propertyKey, Map<String, Object> jsonObjProperties) {
        if (jsonObjProperties == null) {
            return 0;
        }
        Number doubleValue = (Number) jsonObjProperties.get(propertyKey);

        return doubleValue.intValue();
    }

    public static boolean getBooleanProperty(String propertyKey, Map<String, Object> jsonObjProperties) {
        return Boolean.valueOf(getStringProperty(propertyKey, jsonObjProperties));
    }

    public static Map<String, Object> getDriverExecutionProperties(String driverName) {
        Map<String, Object> driverProps = (Map<String, Object>) getExecutionProperties().get(EXECUTION_DRIVER_PROPERTY);

        return (Map<String, Object>) driverProps.get(driverName);
    }

    public static String getDriverSystemProperty(String driverConnectorId, String propertyName) {
        Map<String, Object> properties = getDriverSystemProperties(driverConnectorId);

        return (properties != null) ? (String) properties.get(propertyName) : null;
    }

    public static String getDriverSystemProperty(String driverConnectorId, String propertyName, String defaultValue) {
        Map<String, Object> properties = getDriverSystemProperties(driverConnectorId);
        if (properties != null && properties.get(propertyName) != null) {
            return String.valueOf(properties.get(propertyName));
        }
        return defaultValue;
    }

    public static String getDriverPreferencesProperty(String driverConnectorId, String propertyName) {
        Map<String, Object> properties = getDriverPreferencesProperties(driverConnectorId);

        return (properties != null) ? (String) properties.get(propertyName) : null;
    }

    public static Map<String, Object> getDriverSystemProperties(String driverConnectorId) {
        Map<String, Object> systemProperties = getDriverExecutionProperties(EXECUTION_SYSTEM_PROPERTY);
        return systemProperties.containsKey(driverConnectorId)
                ? (Map<String, Object>) systemProperties.get(driverConnectorId) : null;
    }

    public static Map<String, Object> getDriverPreferencesProperties(String driverConnectorId) {
        return (Map<String, Object>) getDriverPreferencesProperties().get(driverConnectorId);
    }

    public static Map<String, Object> getDriverPreferencesProperties() {
        return (Map<String, Object>) getDriverExecutionProperties(EXECUTION_PREFS_PROPERTY);
    }

    public static Map<String, Object> getHostProperties() {
        return (Map<String, Object>) localExecutionSettingMapStorage.get().get(HOST);
    }

    public static int getIntProperty(String propertyKey) {
        Number doubleValue = (Number) getProperty(propertyKey);
        return doubleValue.intValue();
    }

    public static String getSettingFilePath() {
        return settingFilePath;
    }

    /**
     * Get the absolute path of the execution folder that contains log files
     * 
     * @return the absolute path of the execution folder that contains log files
     */
    public static String getLogFolderPath() {
        String logFilePath = RunConfiguration.getSettingFilePath();
        return (logFilePath != null) ? new File(logFilePath).getParentFile().getAbsolutePath() : "";
    }

    public static String getAppiumLogFilePath() {
        if (StringUtils.isBlank(localAppiumDriverStores.get())) {
            String appiumLogFilePath = getLogFolderPath() + File.separator + RunConfiguration.getDriverSystemProperty(
                    StringConstants.CONF_PROPERTY_MOBILE_DRIVER, StringConstants.CONF_APPIUM_LOG_FILE);
            localAppiumDriverStores.set(appiumLogFilePath);
        }
        return localAppiumDriverStores.get();
    }

    public static String getAppiumDirectory() {
        return RunConfiguration.getDriverSystemProperty(StringConstants.CONF_PROPERTY_MOBILE_DRIVER,
                StringConstants.CONF_APPIUM_DIRECTORY);
    }

    public static String getDeviceConsoleExecutable() {
        return RunConfiguration.getDriverSystemProperty(StringConstants.CONF_PROPERTY_MOBILE_DRIVER,
                StringConstants.XML_LOG_DEVICE_CONSOLE_PATH_PROPERTY);
    }

    /**
     * Get the execution time out (in seconds)
     * 
     * @return the execution time out (in seconds)
     */
    public static int getTimeOut() {
        return getIntProperty(TIMEOUT_PROPERTY, getExecutionGeneralProperties());
    }

    /**
     * Get the absolute path of the current project's folder
     * 
     * @return the absolute path of the current project's folder
     */
    public static String getProjectDir() {
        return getStringProperty(PROJECT_DIR_PROPERTY);
    }

    /**
     * Get the absolute path of the executed target ( test case | test suite | test suite collection )
     * 
     * @return the absolute path of the executed target ( test case | test suite | test suite collection )
     */
    public static String getExecutionSource() {
        return getStringProperty(EXCUTION_SOURCE);
    }

    /**
     * Get the name of the executed target ( test case | test suite | test suite collection )
     * 
     * @return the name of the executed target ( test case | test suite | test suite collection )
     */
    public static String getExecutionSourceName() {
        return getStringProperty(EXCUTION_SOURCE_NAME);
    }

    /**
     * Get the id of the executed target ( test case | test suite | test suite collection )
     * 
     * @return the id of the executed target ( test case | test suite | test suite collection )
     */
    public static String getExecutionSourceId() {
        return getStringProperty(EXCUTION_SOURCE_ID);
    }

    /**
     * Get the description of the executed target ( test case | test suite | test suite collection )
     * 
     * @return the description of the executed target ( test case | test suite | test suite collection )
     */
    public static String getExecutionSourceDescription() {
        return getStringProperty(EXCUTION_SOURCE_DESCRIPTION);
    }

    public static int getSessionServerPort() {
        return getIntProperty(SESSION_SERVER_PORT);
    }

    public static String getSessionServerHost() {
        return getStringProperty(SESSION_SERVER_HOST);
    }

    public static String getExistingSessionSessionId() {
        return RunConfiguration.getDriverSystemProperty(StringConstants.CONF_PROPERTY_EXISTING_DRIVER,
                StringConstants.CONF_PROPERTY_EXISTING_SESSION_SESSION_ID);
    }

    public static String getExistingSessionServerUrl() {
        return RunConfiguration.getDriverSystemProperty(StringConstants.CONF_PROPERTY_EXISTING_DRIVER,
                StringConstants.CONF_PROPERTY_EXISTING_SESSION_SERVER_URL);
    }

    public static String getExistingSessionDriverType() {
        return RunConfiguration.getDriverSystemProperty(StringConstants.CONF_PROPERTY_EXISTING_DRIVER,
                StringConstants.CONF_PROPERTY_EXISTING_SESSION_DRIVER_TYPE);
    }

    /**
     * Get the host name of the current execution machine
     * 
     * @return the host name of the current execution machine
     */
    public static String getHostName() {
        return getStringProperty(HOST_NAME, getHostProperties());
    }

    /**
     * Get the information of the OS of the current execution machine
     * 
     * @return the information of the OS of the current execution machine
     */
    public static String getOS() {
        return getStringProperty(HOST_OS, getHostProperties());
    }

    public static int getPort() {
        return getIntProperty(HOST_PORT, getHostProperties());
    }

    public static String getHostAddress() {
        return getStringProperty(HOST_ADDRESS, getHostProperties());
    }

    public static Map<String, Object> getExecutionProperties() {
        return (Map<String, Object>) getProperty(EXECUTION_PROPERTY);
    }

    public static Map<String, Object> getExecutionGeneralProperties() {
        return (Map<String, Object>) getExecutionProperties().get(EXECUTION_GENERAL_PROPERTY);
    }

    public static String getLogbackConfigFileLocation() {
        return getStringProperty(LOGBACK_CONFIG_FILE_LOCATION);
    }
    
    public static Object[] getStoredDrivers() {
        return localDriverStorage.get().toArray();
    }

    public static void storeDriver(Object driver) {
        if (!localDriverStorage.get().contains(driver)) {
            localDriverStorage.get().add(driver);
        }
    }

    public static void removeDriver(Object driver) {
        if (localDriverStorage.get().contains(driver)) {
            localDriverStorage.get().remove(driver);
        }
    }

    /**
     * Get the current version of Katalon Studio
     * 
     * @return the current version of Katalon Studio
     */
    public static String getAppVersion() {
        Properties appInfo = applicationInfo.get();
        if (appInfo.isEmpty()) {
            return GlobalStringConstants.UNKNOWN;
        }
        return appInfo.getProperty(GlobalStringConstants.APP_VERSION_NUMBER_KEY) + "."
                + appInfo.getProperty(GlobalStringConstants.APP_BUILD_NUMBER_KEY);
    }

    public static FailureHandling getDefaultFailureHandling() {
        try {
            return FailureHandling
                    .valueOf(getStringProperty(EXCUTION_DEFAULT_FAILURE_HANDLING, getExecutionGeneralProperties()));
        } catch (NullPointerException e) {
            return FailureHandling.STOP_ON_FAILURE;
        } catch (IllegalArgumentException e) {
            return FailureHandling.STOP_ON_FAILURE;
        }
    }

    public static Map<String, String> getCollectedTestDataProperties() {
        Map<String, Object> generalProperties = getExecutionGeneralProperties();
        return (Map<String, String>) generalProperties.get(EXECUTION_TEST_DATA_INFO_PROPERTY);
    }

    public static ProxyInformation getProxyInformation() {
        Map<String, Object> generalProperties = getExecutionGeneralProperties();
        if (!generalProperties.containsKey(PROXY_PROPERTY)) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson((String) generalProperties.get(PROXY_PROPERTY), ProxyInformation.class);
    }

    public static boolean shouldTerminateDriverAfterTestCase() {
        Map<String, Object> generalProperties = getExecutionGeneralProperties();
        if (generalProperties == null) {
            return false;
        }
        return (boolean) generalProperties.getOrDefault(TERMINATE_DRIVER_AFTER_TEST_CASE, false);
    }

    public static boolean shouldTerminateDriverAfterTestSuite() {
        Map<String, Object> generalProperties = getExecutionGeneralProperties();
        if (generalProperties == null) {
            return false;
        }
        return (boolean) generalProperties.getOrDefault(TERMINATE_DRIVER_AFTER_TEST_SUITE, false);
    }

    /**
     * Returns name of selected execution profile. Default value is 'default' profile.
     * 
     * @since 5.4
     */
    public static String getExecutionProfile() {
        Map<String, Object> generalProperties = getExecutionGeneralProperties();
        if (generalProperties == null) {
            return "default";
        }
        return (String) generalProperties.getOrDefault(EXECUTION_PROFILE_PROPERTY, "default");
    }

    private static Map<String, Object> getReportProperties() {
        Map<String, Object> generalProperties = getExecutionGeneralProperties();
        if (generalProperties == null) {
            return Collections.emptyMap();
        }
        return (Map<String, Object>) generalProperties.getOrDefault(StringConstants.CONF_PROPERTY_REPORT,
                Collections.emptyMap());
    }

    public static String getReportFolder() {
        return (String) getReportProperties().getOrDefault(REPORT_FOLDER_PATH_PROPERTY, StringUtils.EMPTY);
    }

    public static VideoRecorderSetting getRecorderSetting() {
        return JsonUtil
                .fromJson(
                        JsonUtil.toJson(getReportProperties()
                                .getOrDefault(StringConstants.CONF_PROPERTY_VIDEO_RECORDER_OPTION, StringUtils.EMPTY)),
                        VideoRecorderSetting.class);
    }
    
    public static String getCapturedObjectsCacheFile() {
        return getStringProperty(RECORD_CAPTURED_OBJECTS_FILE);
    }
    
	public static Boolean shouldApplySmartXPath() {
		if (getProperty(SMART_XPATH_BUNDLE_ID) != null) {
			try {
				return (Boolean) new BundleSettingStore(getProjectDir(), SMART_XPATH_BUNDLE_ID, true)
						.getBoolean("SmartXPathEnabled", true);
			} catch (IOException e) {
				KeywordLogger.getInstance(RunConfiguration.class).logError(e.getMessage(), null, e);
			}
		}
		return false;
	}
    
    public static RunningMode getRunningMode() {
        return RunningMode.valueOf(getStringProperty(RUNNING_MODE));
    }
    
    public static Map<String, Object> getOverridingParameters(){
    	Map<String, Object> overridingParameters = (Map<String, Object>) getProperty(OVERRIDING_GLOBAL_VARIABLES);
    	if(overridingParameters == null){
    		return new HashMap<String, Object>();
    	}
    	return overridingParameters;
    }
    
    public static List<String> getPluginTestListeners() {
        Object testListeners = getProperty(PLUGIN_TEST_LISTENERS);
        if (testListeners != null) {
            return (List<String>) testListeners;
        }
        return Collections.emptyList();
    }
    
    public static String getOverridingGlobalVariable(String globalVariableName){
    	Object object = getOverridingParameters().get(globalVariableName);
    	if(object != null){
    		return String.valueOf(object);
    	}
    	return null;
    }
}
