package com.kms.katalon.core.helper;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.math.NumberUtils;

import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.constants.StringConstants;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.testobject.TestObject;

public class KeywordHelper {
    
    private static final KeywordLogger logger = KeywordLogger.getInstance(KeywordHelper.class);
    
	public static boolean match(String value1, String value2, boolean isRegex) {
		if (isRegex) {
			Pattern p = Pattern.compile(value2, Pattern.DOTALL);
			return p.matcher(value1).matches();
		} else {
			return (value1 != null && value2 != null && value1.equals(value2));
		}
	}
	

	public static void checkTestObjectParameter(TestObject testObject) throws IllegalArgumentException {
	    logger.logDebug(StringConstants.COMM_LOG_INFO_CHECKING_OBJ);
		if (testObject == null) {
			throw new IllegalArgumentException(StringConstants.COMM_EXC_OBJ_IS_NULL);
		}
	}

	public static int checkTimeout(int timeout) throws IllegalArgumentException {
	    logger.logDebug(StringConstants.COMM_LOG_INFO_CHECKING_TIMEOUT);
		if (timeout <= 0) {
			int defaultPageLoadTimeout = RunConfiguration.getTimeOut();
			logger.logWarning(
					MessageFormat.format(StringConstants.COMM_LOG_WARNING_INVALID_TIMEOUT, timeout, defaultPageLoadTimeout));
			return defaultPageLoadTimeout;
		}
		return timeout;
	}

	public static int comparingNumberObject(Object actualNumber, Object expectedNumber)
			throws IllegalArgumentException {
		if (actualNumber == null) {
			throw new IllegalArgumentException(StringConstants.COMM_EXC_ACTUAL_NUM_IS_NULL);
		}

		if (expectedNumber == null) {
			throw new IllegalArgumentException(StringConstants.COMM_EXC_EXPECTED_NUM_IS_NULL);
		}

		String stringNum1 = String.valueOf(actualNumber);
		String stringNum2 = String.valueOf(expectedNumber);

		if (!NumberUtils.isNumber(stringNum1)) {
			throw new IllegalArgumentException(MessageFormat.format(StringConstants.COMM_EXC_INVALID_ACTUAL_NUM, stringNum1));
		}

		if (!NumberUtils.isNumber(stringNum2)) {
			throw new IllegalArgumentException(MessageFormat.format(StringConstants.COMM_EXC_INVALID_EXPECTED_NUM, stringNum2));
		}

		BigDecimal num1 = NumberUtils.createBigDecimal(String.valueOf(actualNumber));
		BigDecimal num2 = NumberUtils.createBigDecimal(String.valueOf(expectedNumber));
		return num1.compareTo(num2);
	}

	public static String integerArrayToString(Integer[] integers) {
		StringBuilder sb = new StringBuilder("[");
		for (int integer : integers) {
			sb.append(integer);
			sb.append(',');
		}
		return sb.substring(0, sb.length() - 1) + "]";
	}

	public static Integer[] indexRangeToArray(String range) {
	    logger.logDebug(MessageFormat.format(StringConstants.COMM_LOG_INFO_CONVERTING_RANGE_PARAM_TO_INDEX_ARRAY, range));
		List<Integer> ints = new ArrayList<Integer>();
		try {
			if (range.indexOf("-") != -1) {
				String start = range.split("-")[0].trim();
				String end = range.split("-")[1].trim();
				for (int i = Integer.parseInt(start); i <= Integer.parseInt(end); i++) {
					ints.add(i);
				}
			} else if (range.indexOf(",") != -1) {
				for (String sInt : range.split(",")) {
					ints.add(Integer.parseInt(sInt.trim()));
				}
			} else {
				ints.add(Integer.parseInt(range.trim()));
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(MessageFormat.format(StringConstants.COMM_EXC_INVALID_RANGE, range, e.getMessage()));
		}
		Integer[] integerArray = ints.toArray(new Integer[] {});
		logger.logDebug(MessageFormat.format(StringConstants.COMM_LOG_INFO_RANGE_PARAM_IS_CONVERTED_TO_INDEX_ARRAY, range, integerArrayToString(integerArray)));
		return ints.toArray(new Integer[] {});
	}
}
