package com.kms.katalon.core.windows.constants;

import org.eclipse.osgi.util.NLS;

public class CoreWindowsMessageConstants extends NLS {

	private static final String BUNDLE_NAME = "com.kms.katalon.core.windows.constants.coreWindowsMessages";

	public static String COMM_WINDOWS_HAS_NOT_STARTED;

	public static String KW_ENCRYPTED_TEXT_IS_NULL;
	
	public static String KW_CHECK_WINDOWS_DRIVER;
	
	public static String KW_LOG_INFO_CHECKING_TIMEOUT;

	public static String KW_LOG_INFO_CHECKING_TEST_OBJECT;
	
	public static String KW_EXEC_TEST_OBJECT_IS_NULL;

	public static String WindowsActionHelper_INFO_START_FINDING_WINDOW;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, CoreWindowsMessageConstants.class);
	}

	private CoreWindowsMessageConstants() {
	}
}
