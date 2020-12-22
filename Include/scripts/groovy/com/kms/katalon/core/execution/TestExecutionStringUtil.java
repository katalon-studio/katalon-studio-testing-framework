package com.kms.katalon.core.execution;

public class TestExecutionStringUtil {

    public static String getUnoffensiveTestCaseName(String tc) {
        return tc.replace(" ", "-").replace("/", "_");
    }
}
