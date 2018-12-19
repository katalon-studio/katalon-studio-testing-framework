package com.kms.katalon.core.webui.common;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import com.kms.katalon.core.webui.constants.StringConstants;

public class EngineLogger {

	public static String getLogMessageFromProperties(String logId) {
		try {
//			initResourceBundle();
//			String[] arrayMess = getString(logId).split(";");
//			return arrayMess[3];
		    return "Error";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	static ResourceBundle resourceBundle = null;
	static final String RESOURCE_BUNDLE_NAME = "ATCLogMessage";
	static final String BUNDLE_PACKAGE_NAME = "com.kms.katalon.core.common";

	public static synchronized void initResourceBundle() throws Exception {
		if (resourceBundle == null) {
			resourceBundle = ResourceBundle.getBundle(BUNDLE_PACKAGE_NAME + "." + RESOURCE_BUNDLE_NAME);
		}

		if (resourceBundle == null) {
			throw new Exception(
					MessageFormat.format(StringConstants.COMM_EXC_UNABLE_TO_INIT_RESRC_BUNDLE, RESOURCE_BUNDLE_NAME));
		}
	}
	
	public static String getString(String key) throws Exception {
		if (resourceBundle == null) {
			throw new Exception(StringConstants.COMM_EXC_RESRC_BUNDLE_IS_NOT_INIT);
		}

		return resourceBundle.getString(key);
	}

}
