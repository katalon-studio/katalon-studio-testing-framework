package com.kms.katalon.core.ast;

import java.util.Map;

import com.kms.katalon.core.context.internal.ExecutionEventManager;
import com.kms.katalon.core.context.internal.ExecutionListenerEvent;
import com.kms.katalon.core.logging.KeywordLogger;

public class TestStepUtil {

    public static void publishBeforeTestStepEvent(Object[] info) {
        ExecutionEventManager.getInstance().publicEvent(ExecutionListenerEvent.BEFORE_TEST_STEP, info);
    }
    
    public static void publishAfterTestStepEvent(Object[] info) {
        ExecutionEventManager.getInstance().publicEvent(ExecutionListenerEvent.AFTER_TEST_STEP, info);
    }
    
    public static void logNotRun(Class<?> testCaseClass, String keywordName) {
        KeywordLogger logger = getLogger(testCaseClass);
        logger.logNotRun("NOT_RUN: " + keywordName);
    }
    
    public static void logAddDescription(Class<?> testCaseClass, String comment) {
        KeywordLogger logger = getLogger(testCaseClass);
        logger.setPendingDescription(comment);
    }
    
    public static void logStartKeyword(
            Class<?> testCaseClass, 
            String keywordName, 
            Map<String, String> attributes, 
            int nestedLevel) {
        KeywordLogger logger = getLogger(testCaseClass);
        logger.startKeyword(keywordName, attributes, nestedLevel);
    }
    
    private static KeywordLogger getLogger(Class<?> testCaseClass) {
        return KeywordLogger.getInstance(testCaseClass);
    }
}
