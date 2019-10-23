package com.kms.katalon.core.constants;

import java.io.File;

public class StringConstants {
    public static final String DF_CHARSET = "UTF-8";

    public static final String ID_SEPARATOR = "/";

    // KeywordLogger
    public static final String LOG_START_SUITE = "Start Test Suite";

    public static final String LOG_START_SUITE_METHOD = "startSuite";

    public static final String LOG_START_TEST = "Start Test Case";

    public static final String LOG_START_TEST_METHOD = "startTest";

    public static final String LOG_START_KEYWORD = "Start action";
    
    public static final String LOG_START_ACTION_PREFIX = LOG_START_KEYWORD + " : ";
    
    public static final String LOG_SETUP_ACTION = "setup action";
    
    public static final String LOG_TEAR_DOWN_ACTION = "tear down";

    public static final String LOG_START_KEYWORD_METHOD = "startKeyword";

    public static final String LOG_END_SUITE = "End Test Suite";

    public static final String LOG_END_SUITE_METHOD = "endSuite";

    public static final String LOG_END_TEST = "End Test Case";

    public static final String LOG_END_TEST_METHOD = "endTest";
    
    public static final String LOG_LISTENER_ACTION = "listener action";

    public static final String LOG_END_KEYWORD = "End action";

    public static final String LOG_END_KEYWORD_METHOD = "endKeyword";

    public static final String DF_LOCAL_HOST_ADDRESS = "127.0.0.1";

    // TestCaseMain
    public static final String MAIN_LOG_ERROR_UNKNOWN_PROP_OF_TEST_CASE = CoreMessageConstants.MAIN_LOG_ERROR_UNKNOWN_PROP_OF_TEST_CASE;

    public static final String MAIN_LOG_PASSED_METHOD_COMPLETED = CoreMessageConstants.MAIN_LOG_PASSED_METHOD_COMPLETED;

    public static final String MAIN_LOG_WARNING_ERROR_OCCURRED_WHEN_RUN_METHOD = CoreMessageConstants.MAIN_LOG_WARNING_ERROR_OCCURRED_WHEN_RUN_METHOD;

    public static final String MAIN_MSG_START_RUNNING_SETUP_METHODS_FOR_TC = CoreMessageConstants.MAIN_MSG_START_RUNNING_SETUP_METHODS_FOR_TC;

    public static final String MAIN_MSG_START_RUNNING_TEAR_DOWN_METHODS_FOR_PASSED_TC = CoreMessageConstants.MAIN_MSG_START_RUNNING_TEAR_DOWN_METHODS_FOR_PASSED_TC;

    public static final String MAIN_MSG_START_RUNNING_TEAR_DOWN_METHODS_FOR_TC = CoreMessageConstants.MAIN_MSG_START_RUNNING_TEAR_DOWN_METHODS_FOR_TC;

    public static final String MAIN_MSG_START_RUNNING_TEAR_DOWN_METHODS_FOR_FAILED_TC = CoreMessageConstants.MAIN_MSG_START_RUNNING_TEAR_DOWN_METHODS_FOR_FAILED_TC;

    public static final String MAIN_MSG_START_RUNNING_TEAR_DOWN_METHODS_FOR_ERROR_TC = CoreMessageConstants.MAIN_MSG_START_RUNNING_TEAR_DOWN_METHODS_FOR_ERROR_TC;

    public static final String MAIN_LOG_MSG_FAILED_BECAUSE_OF = CoreMessageConstants.MAIN_LOG_MSG_FAILED_BECAUSE_OF;

    public static final String MAIN_LOG_MSG_ERROR_BECAUSE_OF = CoreMessageConstants.MAIN_LOG_MSG_ERROR_BECAUSE_OF;

    public static final String MAIN_LOG_INFO_START_EVALUATE_VARIABLE = CoreMessageConstants.MAIN_LOG_INFO_START_EVALUATE_VARIABLE;

    public static final String MAIN_LOG_INFO_VARIABLE_NAME_X_IS_SET_TO_Y = CoreMessageConstants.MAIN_LOG_INFO_VARIABLE_NAME_X_IS_SET_TO_Y;

    public static final String MAIN_LOG_INFO_VARIABLE_NAME_X_IS_SET_TO_Y_AS_DEFAULT = CoreMessageConstants.MAIN_LOG_INFO_VARIABLE_NAME_X_IS_SET_TO_Y_AS_DEFAULT;

    public static final String MAIN_LOG_MSG_SET_TEST_VARIABLE_ERROR_BECAUSE_OF = CoreMessageConstants.MAIN_LOG_MSG_SET_TEST_VARIABLE_ERROR_BECAUSE_OF;

    // TestData
    public static final String TD_ROW_INDEX_X_FOR_TEST_DATA_Y_INVALID = CoreMessageConstants.TD_ROW_INDEX_X_FOR_TEST_DATA_Y_INVALID;

    public static final String TD_COLUMN_INDEX_X_FOR_TEST_DATA_Y_INVALID = CoreMessageConstants.TD_COLUMN_INDEX_X_FOR_TEST_DATA_Y_INVALID;

    public static final String TD_COLUMN_NAME_X_FOR_TEST_DATA_Y_INVALID = CoreMessageConstants.TD_COLUMN_NAME_X_FOR_TEST_DATA_Y_INVALID;

    // AppPOI
    public static final String UTIL_EXC_FILE_IS_UNSUPPORTED = CoreMessageConstants.UTIL_EXC_FILE_IS_UNSUPPORTED;

    public static final String UTIL_EXC_FILE_IS_NOT_HTML_FORMAT = CoreMessageConstants.UTIL_EXC_FILE_IS_NOT_HTML_FORMAT;

    public static final String UTIL_EXC_FILE_NOT_FOUND = CoreMessageConstants.UTIL_EXC_FILE_NOT_FOUND;

    // ShettPOI
    public static final String EXCEL_INVALID_ROW_NUMBER = CoreMessageConstants.EXCEL_INVALID_ROW_NUMBER;

    public static final String EXCEL_INVALID_COL_NUMBER = CoreMessageConstants.EXCEL_INVALID_COL_NUMBER;

    // ObjectRepository
    public static final String TO_LOG_WARNING_TEST_OBJ_NULL = CoreMessageConstants.TO_LOG_WARNING_TEST_OBJ_NULL;

    public static final String TO_LOG_INFO_FINDING_TEST_OBJ_W_ID = CoreMessageConstants.TO_LOG_INFO_FINDING_TEST_OBJ_W_ID;

    public static final String TO_LOG_WARNING_TEST_OBJ_DOES_NOT_EXIST = CoreMessageConstants.TO_LOG_WARNING_TEST_OBJ_DOES_NOT_EXIST;

    public static final String TO_LOG_WARNING_CANNOT_GET_TEST_OBJECT_X_BECAUSE_OF_Y = CoreMessageConstants.TO_LOG_WARNING_CANNOT_GET_TEST_OBJECT_X_BECAUSE_OF_Y;

    // BuiltinKeywords
    public static final String KW_LOG_INFO_MATCHING_ACTUAL_TXT_W_EXPECTED_TXT = CoreMessageConstants.KW_LOG_INFO_MATCHING_ACTUAL_TXT_W_EXPECTED_TXT;

    public static final String KW_LOG_PASSED_ACTUAL_TXT_MATCHED_EXPECTED_TXT = CoreMessageConstants.KW_LOG_PASSED_ACTUAL_TXT_MATCHED_EXPECTED_TXT;

    public static final String KW_MSG_ACTUAL_TXT_NOT_MATCHED_EXPECTED_TXT = CoreMessageConstants.KW_MSG_ACTUAL_TXT_NOT_MATCHED_EXPECTED_TXT;

    public static final String KW_MSG_CANNOT_VERIFY_MATCHING_BETWEEN_TXTS = CoreMessageConstants.KW_MSG_CANNOT_VERIFY_MATCHING_BETWEEN_TXTS;

    public static final String KW_LOG_INFO_MATCHING_ACTUAL_TXT_W_EXPECTED_VAL = CoreMessageConstants.KW_LOG_INFO_MATCHING_ACTUAL_TXT_W_EXPECTED_VAL;

    public static final String KW_MSG_TXTS_MATCHED_BUT_EXPECTED_UNMATCHED = CoreMessageConstants.KW_MSG_TXTS_MATCHED_BUT_EXPECTED_UNMATCHED;

    public static final String KW_LOG_PASSED_TXTS_UNMATCHED = CoreMessageConstants.KW_LOG_PASSED_TXTS_UNMATCHED;

    public static final String KW_MSG_CANNOT_VERIFY_TXTS_ARE_UNMATCHED = CoreMessageConstants.KW_MSG_CANNOT_VERIFY_TXTS_ARE_UNMATCHED;

    public static final String KW_MSG_OBJECTS_ARE_NOT_EQUAL = CoreMessageConstants.KW_MSG_OBJECTS_ARE_NOT_EQUAL;

    public static final String KW_LOG_PASSED_OBJECTS_ARE_EQUAL = CoreMessageConstants.KW_LOG_PASSED_OBJECTS_ARE_EQUAL;

    public static final String KW_MSG_CANNOT_VERIFY_OBJECTS_ARE_EQUAL = CoreMessageConstants.KW_MSG_CANNOT_VERIFY_OBJECTS_ARE_EQUAL;

    public static final String KW_MSG_OBJECTS_ARE_EQUAL = CoreMessageConstants.KW_MSG_OBJECTS_ARE_EQUAL;

    public static final String KW_LOG_PASSED_OBJECTS_ARE_NOT_EQUAL = CoreMessageConstants.KW_LOG_PASSED_OBJECTS_ARE_NOT_EQUAL;

    public static final String KW_MSG_CANNOT_VERIFY_OBJECTS_ARE_NOT_EQUAL = CoreMessageConstants.KW_MSG_CANNOT_VERIFY_OBJECTS_ARE_NOT_EQUAL;

    public static final String KW_LOG_PASSED_ACTUAL_NUM_IS_GREATER_THAN_EXPECTED_NUM = CoreMessageConstants.KW_LOG_PASSED_ACTUAL_NUM_IS_GREATER_THAN_EXPECTED_NUM;

    public static final String KW_MSG_ACTUAL_NUM_IS_NOT_GREATER_THAN_EXPECTED_NUM = CoreMessageConstants.KW_MSG_ACTUAL_NUM_IS_NOT_GREATER_THAN_EXPECTED_NUM;

    public static final String KW_MSG_CANNOT_VERIFY_WHICH_NUM_IS_GREATER = CoreMessageConstants.KW_MSG_CANNOT_VERIFY_WHICH_NUM_IS_GREATER;

    public static final String KW_LOG_PASSED_ACTUAL_NUM_IS_GT_OR_EQ_TO_EXPECTED_NUM = CoreMessageConstants.KW_LOG_PASSED_ACTUAL_NUM_IS_GT_OR_EQ_TO_EXPECTED_NUM;

    public static final String KW_MSG_ACTUAL_NUM_IS_NOT_GT_OR_EQ_TO_EXPECTED_NUM = CoreMessageConstants.KW_MSG_ACTUAL_NUM_IS_NOT_GT_OR_EQ_TO_EXPECTED_NUM;

    public static final String KW_MSG_CANNOT_VERIFY_NUMS_ARE_GT_OR_EQ = CoreMessageConstants.KW_MSG_CANNOT_VERIFY_NUMS_ARE_GT_OR_EQ;

    public static final String KW_LOG_PASSED_ACTUAL_NUM_IS_LT_EXPECTED_NUM = CoreMessageConstants.KW_LOG_PASSED_ACTUAL_NUM_IS_LT_EXPECTED_NUM;

    public static final String KW_MSG_ACTUAL_NUM_IS_NOT_LT_EXPECTED_NUM = CoreMessageConstants.KW_MSG_ACTUAL_NUM_IS_NOT_LT_EXPECTED_NUM;

    public static final String KW_MSG_CANNOT_VERIFY_WHICH_NUM_IS_LT = CoreMessageConstants.KW_MSG_CANNOT_VERIFY_WHICH_NUM_IS_LT;

    public static final String KW_LOG_PASSED_ACTUAL_NUM_IS_LT_OR_EQ_TO_EXPECTED_NUM = CoreMessageConstants.KW_LOG_PASSED_ACTUAL_NUM_IS_LT_OR_EQ_TO_EXPECTED_NUM;

    public static final String KW_MSG_ACTUAL_NUM_IS_NOT_LT_OR_EQ_EXPECTED_NUM = CoreMessageConstants.KW_MSG_ACTUAL_NUM_IS_NOT_LT_OR_EQ_EXPECTED_NUM;

    public static final String KW_MSG_CANNOT_VERIFY_WHICH_NUM_IS_LT_OR_EQ_TO = CoreMessageConstants.KW_MSG_CANNOT_VERIFY_WHICH_NUM_IS_LT_OR_EQ_TO;

    public static final String KW_LOG_INFO_CHECING_STRINGS_PARAM = CoreMessageConstants.KW_LOG_INFO_CHECING_STRINGS_PARAM;

    public static final String KW_EXC_STRS_PARAM_IS_NULL = CoreMessageConstants.KW_EXC_STRS_PARAM_IS_NULL;

    public static final String KW_LOG_INFO_CONCAT_STR_ARRAY = CoreMessageConstants.KW_LOG_INFO_CONCAT_STR_ARRAY;

    public static final String KW_LOG_PASSED_CONCAT_STR_ARRAY = CoreMessageConstants.KW_LOG_PASSED_CONCAT_STR_ARRAY;

    public static final String KW_CANNOT_CONCAT_STR_ARRAY = CoreMessageConstants.KW_CANNOT_CONCAT_STR_ARRAY;

    public static final String KW_CANNOT_CONCAT = CoreMessageConstants.KW_CANNOT_CONCAT;

    public static final String KW_LOG_INFO_CHECKING_CALLED_TC = CoreMessageConstants.KW_LOG_INFO_CHECKING_CALLED_TC;

    public static final String KW_EXC_CALLED_TC_IS_NULL = CoreMessageConstants.KW_EXC_CALLED_TC_IS_NULL;

    public static final String KW_LOG_INFO_STARTING_TO_CALL_TC = CoreMessageConstants.KW_LOG_INFO_STARTING_TO_CALL_TC;

    public static final String KW_MSG_CALL_TC_FAILED = CoreMessageConstants.KW_MSG_CALL_TC_FAILED;

    public static final String KW_MSG_CALL_TC_X_FAILED_BECAUSE_OF_ERROR = CoreMessageConstants.KW_MSG_CALL_TC_X_FAILED_BECAUSE_OF_ERROR;

    public static final String KW_LOG_PASSED_CALL_TC_X_SUCCESSFULLY = CoreMessageConstants.KW_LOG_PASSED_CALL_TC_X_SUCCESSFULLY;

    public static final String KW_MSG_CANNOT_CALL_TC_W_ID_X = CoreMessageConstants.KW_MSG_CANNOT_CALL_TC_W_ID_X;

    public static final String KW_MSG_CANNOT_CALL_TC = CoreMessageConstants.KW_MSG_CANNOT_CALL_TC;

    public static final String KW_LOG_INFO_DELAYING_BROWSER_IN_SEC = CoreMessageConstants.KW_LOG_INFO_DELAYING_BROWSER_IN_SEC;

    public static final String KW_LOG_PASSED_DELAYED_SEC = CoreMessageConstants.KW_LOG_PASSED_DELAYED_SEC;

    public static final String KW_MSG_CANNOT_DELAY_BROWSER = CoreMessageConstants.KW_MSG_CANNOT_DELAY_BROWSER;

    public static final String KW_CATEGORIZE_NUMBER = "Number";

    public static final String KW_CATEGORIZE_TEXT = "Text";

    public static final String KW_CATEGORIZE_UTILITIES = "Utilities";

    public static final String KW_MSG_VERIFY_CHECKPOINT = CoreMessageConstants.KW_MSG_VERIFY_CHECKPOINT;

    public static final String KW_MSG_CHECKPOINT_IS_NULL = CoreMessageConstants.KW_MSG_CHECKPOINT_IS_NULL;

    public static final String KW_MSG_CHECKPOINT_ID_X = CoreMessageConstants.KW_MSG_CHECKPOINT_ID_X;

    public static final String KW_MSG_NO_SNAPSHOT = CoreMessageConstants.KW_MSG_NO_SNAPSHOT;

    public static final String KW_MSG_CHECKPOINT_DATA_IS_NULL = CoreMessageConstants.KW_MSG_CHECKPOINT_DATA_IS_NULL;

    public static final String KW_MSG_CHECKPOINT_SRC_DATA_IS_NULL = CoreMessageConstants.KW_MSG_CHECKPOINT_SRC_DATA_IS_NULL;

    public static final String KW_MSG_CHECKPOINT_DATA_MATCHES_WITH_NULL = CoreMessageConstants.KW_MSG_CHECKPOINT_DATA_MATCHES_WITH_NULL;

    public static final String KW_MSG_CHECKPOINT_DATA_DOES_NOT_MATCH = CoreMessageConstants.KW_MSG_CHECKPOINT_DATA_DOES_NOT_MATCH;

    public static final String KW_MSG_CHECKPOINT_DATA_MATCHES = CoreMessageConstants.KW_MSG_CHECKPOINT_DATA_MATCHES;

    public static final String KW_MSG_CHECKPOINT_ROW_NUMBER_MATCHES = CoreMessageConstants.KW_MSG_CHECKPOINT_ROW_NUMBER_MATCHES;

    public static final String KW_MSG_CHECKPOINT_ROW_NUMBER_DOES_NOT_MATCH = CoreMessageConstants.KW_MSG_CHECKPOINT_ROW_NUMBER_DOES_NOT_MATCH;

    public static final String KW_MSG_CHECKPOINT_COL_NUMBER_MATCHES = CoreMessageConstants.KW_MSG_CHECKPOINT_COL_NUMBER_MATCHES;

    public static final String KW_MSG_CHECKPOINT_COL_NUMBER_DOES_NOT_MATCH = CoreMessageConstants.KW_MSG_CHECKPOINT_COL_NUMBER_DOES_NOT_MATCH;

    public static final String KW_MSG_VERIFY_CHECKED_VALUES = CoreMessageConstants.KW_MSG_VERIFY_CHECKED_VALUES;

    public static final String KW_MSG_CHECKPOINT_NOT_MATCH_AT_ROW_X_COL_Y_CHECKED_VAL_NEW_VAL = CoreMessageConstants.KW_MSG_CHECKPOINT_NOT_MATCH_AT_ROW_X_COL_Y_CHECKED_VAL_NEW_VAL;

    public static final String KW_MSG_UNABLE_TO_VERIFY_CHECKPOINT_X = CoreMessageConstants.KW_MSG_UNABLE_TO_VERIFY_CHECKPOINT_X;

    // KeywordHelper
    public static final String COMM_LOG_INFO_CHECKING_OBJ = CoreMessageConstants.COMM_LOG_INFO_CHECKING_OBJ;

    public static final String COMM_EXC_OBJ_IS_NULL = CoreMessageConstants.COMM_EXC_OBJ_IS_NULL;

    public static final String COMM_LOG_INFO_CHECKING_TIMEOUT = CoreMessageConstants.COMM_LOG_INFO_CHECKING_TIMEOUT;

    public static final String COMM_LOG_WARNING_INVALID_TIMEOUT = CoreMessageConstants.COMM_LOG_WARNING_INVALID_TIMEOUT;

    public static final String COMM_EXC_ACTUAL_NUM_IS_NULL = CoreMessageConstants.COMM_EXC_ACTUAL_NUM_IS_NULL;

    public static final String COMM_EXC_EXPECTED_NUM_IS_NULL = CoreMessageConstants.COMM_EXC_EXPECTED_NUM_IS_NULL;

    public static final String COMM_EXC_INVALID_ACTUAL_NUM = CoreMessageConstants.COMM_EXC_INVALID_ACTUAL_NUM;

    public static final String COMM_EXC_INVALID_EXPECTED_NUM = CoreMessageConstants.COMM_EXC_INVALID_EXPECTED_NUM;

    public static final String COMM_LOG_INFO_COMPARE_ACTUAL_W_EXPECTED = CoreMessageConstants.COMM_LOG_INFO_COMPARE_ACTUAL_W_EXPECTED;

    public static final String COMM_LOG_INFO_CONVERTING_RANGE_PARAM_TO_INDEX_ARRAY = CoreMessageConstants.COMM_LOG_INFO_CONVERTING_RANGE_PARAM_TO_INDEX_ARRAY;

    public static final String COMM_EXC_INVALID_RANGE = CoreMessageConstants.COMM_EXC_INVALID_RANGE;

    public static final String COMM_LOG_INFO_RANGE_PARAM_IS_CONVERTED_TO_INDEX_ARRAY = CoreMessageConstants.COMM_LOG_INFO_RANGE_PARAM_IS_CONVERTED_TO_INDEX_ARRAY;

    public static final String COMM_LOG_INFO_CHECKING_NUM_PARAMS = CoreMessageConstants.COMM_LOG_INFO_CHECKING_NUM_PARAMS;

    public static final String KW_LOG_INFO_CONVERT_WEB_ELEMENT_TO_TEST_OBJECT = CoreMessageConstants.KW_LOG_INFO_CONVERT_WEB_ELEMENT_TO_TEST_OBJECT;
    
    public static final String KW_LOG_INFO_FAIL_TO_CONVERT_WEB_ELEMENT_TO_TEST_OBJECT = CoreMessageConstants.KW_LOG_INFO_FAIL_TO_CONVERT_WEB_ELEMENT_TO_TEST_OBJECT;
    
    
    // XML LOG
    public static final String XML_LOG_DESCRIPTION_PROPERTY = "description";
    
    public static final String XML_LOG_TAG_PROPERTY = "tag";

    public static final String XML_LOG_ATTACHMENT_PROPERTY = "attachment";

    public static final String XML_LOG_NAME_PROPERTY = "name";

    public static final String XML_LOG_ID_PROPERTY = "id";

    public static final String XML_LOG_SOURCE_PROPERTY = "source";

    public static final String XML_LOG_IS_OPTIONAL = "isOptional";

    public static final String XML_LOG_BROWSER_TYPE_PROPERTY = "browserType";

    public static final String XML_LOG_DEVICE_ID_PROPERTY = "deviceId";

    public static final String XML_LOG_DEVICE_NAME_PROPERTY = "deviceName";

    public static final String XML_LOG_DEVICE_PLATFORM_PROPERTY = "devicePlatform";
    
    public static final String XML_LOG_DEVICE_CONSOLE_PATH_PROPERTY = "deviceConsolePath";

    public static final String XML_LOG_START_LINE_PROPERTY = "startLine";

    public static final String XML_LOG_STEP_INDEX = "stepIndex";

    public static final String XML_LOG_OS_PROPERTY = "os";

    public static final String XML_LOG_HOST_NAME_PROPERTY = "hostName";

    public static final String XML_LOG_HOST_ADDRESS_PROPERTY = "hostAddress";

    public static final String XML_LOG_DB_SERVER_INFO = "dbServerInfo";

    public static final String XML_LOG_IS_IGNORED_IF_FAILED = "isIgnoredIfFailed";
    
    // CustomKeywords 
    public static final String GENERATED_SCRIPT_FOLDER = "Libs"; 
 
    public static final String SCRIPT_FILE_EXT = "groovy"; 
 
    public static final String CUSTOM_KEYWORD_FOLDER_NAME = "Keywords"; 
 
    public static final String CUSTOM_KEYWORD_CLASS_NAME = "CustomKeywords"; 
 
    public static final String GLOBAL_VARIABLE_CLASS_NAME = "GlobalVariable"; 
    
    public static final String INTERNAL_GLOBAL_VARIABLE_CLASS_NAME = "internal.GlobalVariable";
 
    public static final String GLOBAL_VARIABLE_FILE_NAME = GENERATED_SCRIPT_FOLDER + File.separator + "GlobalVariable." + SCRIPT_FILE_EXT;
    
    public static final String INTERNAL_GLOBAL_VARIABLE_FILE_NAME = GENERATED_SCRIPT_FOLDER + File.separator + "internal" + File.separator + "GlobalVariable." + SCRIPT_FILE_EXT;

    // TestDataFactory
    public static final String XML_LOG_TEST_DATA_CHECKING_TEST_DATA_ID = CoreMessageConstants.XML_LOG_TEST_DATA_CHECKING_TEST_DATA_ID;

    public static final String XML_LOG_TEST_DATA_FINDING_TEST_DATA_WITH_ID_X = CoreMessageConstants.XML_LOG_TEST_DATA_FINDING_TEST_DATA_WITH_ID_X;

    public static final String XML_LOG_ERROR_TEST_DATA_NULL_TEST_DATA_ID = CoreMessageConstants.XML_LOG_ERROR_TEST_DATA_NULL_TEST_DATA_ID;

    public static final String XML_LOG_ERROR_TEST_DATA_X_NOT_EXISTS = CoreMessageConstants.XML_LOG_ERROR_TEST_DATA_X_NOT_EXISTS;

    public static final String XML_LOG_ERROR_TEST_DATA_CANNOT_FIND_TEST_DATA_X_BECAUSE_OF_Y = CoreMessageConstants.XML_LOG_ERROR_TEST_DATA_CANNOT_FIND_TEST_DATA_X_BECAUSE_OF_Y;

    public static final String XML_LOG_ERROR_TEST_DATA_MISSING_ELEMENT = CoreMessageConstants.XML_LOG_ERROR_TEST_DATA_MISSING_ELEMENT;

    public static final String XML_LOG_TEST_DATA_READING_EXCEL_DATA = CoreMessageConstants.XML_LOG_TEST_DATA_READING_EXCEL_DATA;

    public static final String XML_LOG_TEST_DATA_READING_INTERNAL_DATA = CoreMessageConstants.XML_LOG_TEST_DATA_READING_INTERNAL_DATA;

    public static final String XML_LOG_TEST_DATA_READING_CSV_DATA = CoreMessageConstants.XML_LOG_TEST_DATA_READING_CSV_DATA;

    public static final String XML_LOG_TEST_DATA_READING_DB_DATA = CoreMessageConstants.XML_LOG_TEST_DATA_READING_DB_DATA;

    public static final String XML_LOG_TEST_DATA_READING_EXCEL_DATA_WITH_SOURCE_X_SHEET_Y = CoreMessageConstants.XML_LOG_TEST_DATA_READING_EXCEL_DATA_WITH_SOURCE_X_SHEET_Y;

    public static final String XML_LOG_TEST_DATA_READING_CSV_DATA_WITH_SOURCE_X_SEPERATOR_Y_AND_Z = CoreMessageConstants.XML_LOG_TEST_DATA_READING_CSV_DATA_WITH_SOURCE_X_SEPERATOR_Y_AND_Z;

    public static final String XML_ERROR_TEST_DATA_CONNECTION_URL_IS_BLANK = CoreMessageConstants.XML_ERROR_TEST_DATA_CONNECTION_URL_IS_BLANK;

    public static final String XML_ERROR_TEST_DATA_CONNECTION_IS_NULL = CoreMessageConstants.XML_ERROR_TEST_DATA_CONNECTION_IS_NULL;

    public static final String XML_ERROR_TEST_DATA_SQL_QUERY_IS_BLANK = CoreMessageConstants.XML_ERROR_TEST_DATA_SQL_QUERY_IS_BLANK;

    public static final String XML_LOG_TEST_DATA_READING_DB_DATA_WITH_QUERY_X = CoreMessageConstants.XML_LOG_TEST_DATA_READING_DB_DATA_WITH_QUERY_X;

    // ExcelData
    public static final String XML_LOG_ERROR_SHEET_NAME_X_NOT_EXISTS = CoreMessageConstants.XML_LOG_ERROR_SHEET_NAME_X_NOT_EXISTS;
    
    // RunConfiguration
    public static final String CONF_PROPERTY_WEBUI_DRIVER = "WebUI";

    public static final String CONF_PROPERTY_MOBILE_DRIVER = "Mobile";
    
    public static final String CONF_PROPERTY_EXISTING_DRIVER = "Existing";

    public static final String CONF_PROPERTY_LOG_FILE_PATH = "logFilePath";

    public static final String CONF_APPIUM_LOG_FILE = "appiumLogFile";

    public static final String CONF_APPIUM_DIRECTORY = "appiumDirectory";

    public static final String CONF_APPIUM_LOG_LEVEL = "appiumLogLevel";

    public static final String CONF_PROPERTY_TIMEOUT = "timeout";

    public static final String CONF_PROPERTY_PROJECT_DIR = "projectDir";

    public static final String CONF_PROPERTY_HOST = "host";

    public static final String CONF_PROPERTY_HOST_NAME = XML_LOG_HOST_NAME_PROPERTY;

    public static final String CONF_PROPERTY_HOST_OS = XML_LOG_OS_PROPERTY;

    public static final String CONF_PROPERTY_HOST_ADDRESS = XML_LOG_HOST_ADDRESS_PROPERTY;

    public static final String CONF_PROPERTY_HOST_PORT = "hostPort";

    public static final String CONF_PROPERTY_GENERAL = "general";

    public static final String CONF_PROPERTY_DRIVER = "drivers";

    public static final String CONF_PROPERTY_EXEC = "execution";

    public static final String CONF_PROPERTY_EXECUTION_SOURCE = XML_LOG_SOURCE_PROPERTY;

    public static final String CONF_PROPERTY_EXECUTION_SOURCE_NAME = XML_LOG_NAME_PROPERTY;

    public static final String CONF_PROPERTY_EXECUTION_SOURCE_ID = XML_LOG_ID_PROPERTY;

    public static final String CONF_PROPERTY_EXECUTION_SOURCE_DESCRIPTION = XML_LOG_DESCRIPTION_PROPERTY;

    public static final String CONF_PROPERTY_EXECUTION_PREFS_PROPERTY = "preferences";

    public static final String CONF_PROPERTY_EXECUTION_SYSTEM_PROPERTY = "system";

    public static final String CONF_PROPERTY_REPORT = "report";

    public static final String CONF_PROPERTY_SCREEN_CAPTURE_OPTION = "screenCaptureOption";

    public static final String CONF_PROPERTY_VIDEO_RECORDER_OPTION = "videoRecorderOption";

    public static final String CONF_PROPERTY_DEFAULT_FAILURE_HANDLING = "defaultFailureHandling";
        
    public static final String CONF_PROPERTY_PROXY = "proxy";
    
    public static final String CONF_PROPERTY_SSL = "ssl";

    public static final String CONF_PROPERTY_TEST_DATA_INFO = "testDataInfo";

    public static final String APP_VERSION = "katalonVersion";
    
    public static final String CONF_SESSION_SERVER_HOST = "sessionServer.host";
    
    public static final String CONF_SESSION_SERVER_PORT = "sessionServer.port";
    
    public static final String CONF_PROPERTY_EXISTING_SESSION_SESSION_ID = "existingSession.sessionId";
    
    public static final String CONF_PROPERTY_EXISTING_SESSION_SERVER_URL = "existingSession.serverUrl";

    public static final String CONF_PROPERTY_EXISTING_SESSION_DRIVER_TYPE = "existingSession.driverType";

    // TestCaseExecutor
    public static final String TEST_STEP_TRANSFORMATION_CLASS = "com.kms.katalon.core.ast.RequireAstTestStepTransformation";

    public static final String NULL_AS_STRING = "null";

    // alias of GlobalStringConstants.APP_INFO_FILE_LOCATION
    public static final String APP_INFO_FILE_LOCATION = System.getProperty("user.home") + File.separator + ".katalon" + File.separator + "application.properties"; //$NON-NLS-1$

    // MobileDriverType
    public static final String ANDROID = "Android";

    public static final String IOS = "iOS";

    // SqlRunner
    public static final String EXC_DATABASE_CONNECTION_IS_CLOSED = CoreMessageConstants.EXC_DATABASE_CONNECTION_IS_CLOSED;

    public static final String EXC_DATABASE_CONNECTION_IS_NULL = CoreMessageConstants.EXC_DATABASE_CONNECTION_IS_NULL;

    // AstTestStepTransformation
    public static final String NOT_RUN_LABEL = "not_run";

    // KeywordMain
    public static final String KW_LOG_WARNING_CANNOT_TAKE_SCREENSHOT = CoreMessageConstants.KW_LOG_WARNING_CANNOT_TAKE_SCREENSHOT;

    public static final String TESTCASE_SETTINGS_FILE_NAME = "com.kms.katalon.composer.testcase.settings";

    // JsonUtils
    public static final String EXC_MSG_INVALID_JSON_SYNTAX = CoreMessageConstants.EXC_MSG_INVALID_JSON_SYNTAX;

    // CheckpointFactory
    public static final String EXC_MSG_CHECKPOINT_ID_IS_NULL = CoreMessageConstants.EXC_MSG_CHECKPOINT_ID_IS_NULL;

    public static final String INFO_MSG_FINDING_CHECKPOINT_WITH_ID = CoreMessageConstants.INFO_MSG_FINDING_CHECKPOINT_WITH_ID;

    public static final String EXC_MSG_CHECKPOINT_DOES_NOT_EXIST = CoreMessageConstants.EXC_MSG_CHECKPOINT_DOES_NOT_EXIST;

    public static final String EXC_MSG_CHECKPOINT_INVALID_SOURCE_TYPE = CoreMessageConstants.EXC_MSG_CHECKPOINT_INVALID_SOURCE_TYPE;

    public static final String INFO_MSG_CHECKPOINT_HAS_SOURCE_FROM_X = CoreMessageConstants.INFO_MSG_CHECKPOINT_HAS_SOURCE_FROM_X;

    public static final String EXC_MSG_CANNOT_FIND_CHECKPOINT_WITH_ID_ROOT_CAUSE = CoreMessageConstants.EXC_MSG_CANNOT_FIND_CHECKPOINT_WITH_ID_ROOT_CAUSE;

    public static final String EXC_MSG_NOT_FOUND_TEST_DATA_WITH_ID = CoreMessageConstants.EXC_MSG_NOT_FOUND_TEST_DATA_WITH_ID;

    public static final String EXC_MSG_DB_CONNECTION_SETTIGNS_ARE_EMPTY = CoreMessageConstants.EXC_MSG_DB_CONNECTION_SETTIGNS_ARE_EMPTY;

    public static final String EXC_MSG_EXCEL_SHEET_NAME_IS_EMPTY = CoreMessageConstants.EXC_MSG_EXCEL_SHEET_NAME_IS_EMPTY;

    public static final String EXC_MSG_INVALID_CSV_SEPARATOR = CoreMessageConstants.EXC_MSG_INVALID_CSV_SEPARATOR;

    public static final String EXC_MSG_CHECKPOINT_IS_MISSING_ELEMENT = CoreMessageConstants.EXC_MSG_CHECKPOINT_IS_MISSING_ELEMENT;

    // TestCaseFactory
    public static final String TEST_CASE_FACTORY_MSG_ID_IS_NULL = CoreMessageConstants.TEST_CASE_FACTORY_MSG_ID_IS_NULL;

    public static final String TEST_CASE_FACTORY_MSG_TC_NOT_EXISTED = CoreMessageConstants.TEST_CASE_FACTORY_MSG_TC_NOT_EXISTED;

    public static final String TEST_CASE_FACTORY_MSG_TC_NOT_EXISTED_WITH_REASON = CoreMessageConstants.TEST_CASE_FACTORY_MSG_TC_NOT_EXISTED_WITH_REASON;
    
    // Common method name
    public static final String METHOD_FIND_TEST_CASE = "findTestCase";

    public static final String METHOD_FIND_TEST_OBJECT = "findTestObject";

    public static final String METHOD_FIND_TEST_DATA = "findTestData";
    
    // KeywordExecutor
    public static final String KEYWORD_X_DOES_NOT_EXIST_ON_PLATFORM_Y = CoreMessageConstants.KEYWORD_X_DOES_NOT_EXIST_ON_PLATFORM_Y;

    public static final String KEYWORD_X_DOES_NOT_EXIST = CoreMessageConstants.KEYWORD_X_DOES_NOT_EXIST;

    public static final String KEYWORD_EXECUTOR_ERROR_MSG = CoreMessageConstants.KEYWORD_EXECUTOR_ERROR_MSG;

    // KeywordLoader
    public static final String KW_MSG_LIST_KEYWORD_CLASS_FAILED_X = CoreMessageConstants.KW_MSG_LIST_KEYWORD_CLASS_FAILED_X;

    // Web Service Verification
    public static final String WS_VERIFICATION_RESPONSE_OBJECT = "responseObject";
    
    public static final String WS_VERIFICATION_REQUEST_OBJECT_ID = "requestObjectId";
    
    public static final String WS_VERIFICATION_REQUEST_VARIABLES = "requestObjectVariables";
    
    public static final String WS_VERIFICATION_SUCCESS = CoreMessageConstants.WS_VERIFICATION_SUCCESS;
    
    // TestCaseExecutor
	public static final String TEST_CASE_SKIPPED = CoreMessageConstants.TEST_CASE_SKIPPED;

	public static final String SMART_XPATH_REPORT_AVAILABLE_OPENING = "=================== Smart XPath Report =======================";
	
	public static final String SMART_XPATH_REPORT_AVAILABLE_ENDING = "===============================================================";

	public static final String SMART_XPATH_VISIT_BELOW_LINK = CoreMessageConstants.SMART_XPATH_VISIT_BELOW_LINK;
	
	public static final String SMART_XPATH_DOCUMENT = CoreMessageConstants.SMART_XPATH_DOCUMENT;
}
