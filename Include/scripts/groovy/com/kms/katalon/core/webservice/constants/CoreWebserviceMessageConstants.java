package com.kms.katalon.core.webservice.constants;

import org.eclipse.osgi.util.NLS;

public class CoreWebserviceMessageConstants extends NLS {
    private static final String BUNDLE_NAME = "com.kms.katalon.core.webservice.constants.coreWebserviceMessages";

    public static String KW_LOG_FAILED_ELEMENT_COUNT_NOT_EQUAL;

    public static String KW_LOG_FAILED_ELEMENT_PROP_VAL_NOT_EQUAL;

    public static String KW_LOG_FAILED_ACTUAL_ELEMENT_TEXT_IS;

    public static String KW_STR_NOT_FOUND_IN_RES;

    public static String KW_LOG_FAILED_CANNOT_SEND_REQUEST;
    
    public static String KW_LOG_FAILED_CANNOT_SEND_REQUEST_AND_VERIFY;

    public static String KW_LOG_PASSED_SEND_REQUEST_SUCCESS;
    
    public static String KW_LOG_PASSED_SEND_REQUEST_AND_VERIFY_SUCCESS;

    public static String KW_LOG_FAILED_CANNOT_VERIFY_ELEMENT_COUNT;

    public static String KW_LOG_PASSED_VERIFY_ELEMENT_COUNT;

    public static String KW_LOG_FAILED_CANNOT_VERIFY_ELEMENT_PROPERTY_VALUE;

    public static String KW_LOG_PASSED_VERIFY_ELEMENT_PROPERTY_VALUE;

    public static String KW_LOG_FAILED_CANNOT_VERIFY_ELEMENT_TEXT;

    public static String KW_LOG_PASSED_VERIFY_ELEMENT_TEXT;

    public static String KW_LOG_PASSED_CONTAIN_STRING;

    public static String KW_LOG_FAILED_CONTAIN_STRING;

    public static String KW_LOG_INFO_CHECKING_REQUEST_OBJECT;

    public static String KW_LOG_FAILED_REQUEST_OBJECT_IS_NULL;

    public static String KW_LOG_INFO_CHECKING_RESPONSE_OBJECT;

    public static String KW_LOG_INFO_CHECKING_RESPONSE_OBJECT_CONTENT;

    public static String KW_LOG_FAILED_RESPONSE_OBJECT_IS_NULL;

    public static String KW_LOG_FAILED_RESPONSE_OBJECT_CONTENT_IS_NULL;
    
    public static String MSG_NO_SERVICE_OPERATION;

    // VerifyResponseStatusCodeKeyword
    public static String KW_LOG_PASSED_VERIFY_RESPONSE_STATUS_CODE_SUCCESSFULLY;

    public static String KW_LOG_FAILED_STATUS_CODE_DOES_NOT_MATCH;

    public static String KW_MSG_UNABLE_TO_VERIFY_RESPONSE_STATUS_CODE;

    // VerifyResponseStatusCodeInRangeKeyword
    public static String KW_LOG_FAILED_STATUS_CODE_DOES_NOT_MATCH_IN_EXPECTED_RANGE;
    
    public static String KW_LOG_INFO_VERIFICATION_START;
    
    public static String KW_LOG_VERIFICATION_STEP_FAILED;
    
    public static String KW_LOG_VERIFICATION_STEP_FAILED_BECAUSE_OF_ERROR;

    public static String KW_LOG_VERIFICATION_STEP_PASSED;
    
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, CoreWebserviceMessageConstants.class);
    }

    private CoreWebserviceMessageConstants() {
    }
}
