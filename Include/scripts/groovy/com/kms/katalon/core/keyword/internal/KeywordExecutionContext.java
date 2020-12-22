package com.kms.katalon.core.keyword.internal;

import com.kms.katalon.core.constants.CoreConstants;

import groovy.transform.CompileStatic;

public class KeywordExecutionContext {

    private static String runningKeyword;

    private static String runningPlatform;
    
    private static boolean hasHealedSomeObjects = false;

    public static void saveRunningKeywordAndPlatform(String runningPlatform, String runningKeyword) {
        KeywordExecutionContext.runningPlatform = runningPlatform;
        KeywordExecutionContext.runningKeyword = runningKeyword;
    }

    public static String getRunningKeyword() {
        return runningKeyword;
    }

    public static void setRunningKeyword(String runningKeyword) {
        KeywordExecutionContext.runningKeyword = runningKeyword;
    }

    public static String getRunningPlatform() {
        return runningPlatform;
    }

    public static void setRunningPlatform(String runningPlatform) {
        KeywordExecutionContext.runningPlatform = runningPlatform;
    }

    public static boolean isRunningWebUI() {
        return runningPlatform.equals(CoreConstants.PLATFORM_WEB);
    }

    @CompileStatic
    public static boolean hasHealedSomeObjects() {
        return hasHealedSomeObjects;
    }

    @CompileStatic
    public static void setHasHealedSomeObjects(boolean hasHealedSomeObjects) {
        KeywordExecutionContext.hasHealedSomeObjects = hasHealedSomeObjects;
    }
}
