package com.kms.katalon.core.logging.testops;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kms.katalon.core.logging.LogLevel;
import com.kms.katalon.core.util.internal.ExceptionsUtil;

public final class TestOpsLogHelper {
    
    private static final String TESTOPS_LOG_PROPERTY_IS_ASSERTION = "testops-is-assertion";
    
    private static final String TESTOPS_LOG_PROPERTY_METHOD_NAME = "testops-method-name";
    
    private static final String TESTOPS_LOG_PROPERTY_EXECUTION_STACKTRACE = "testops-execution-stacktrace";
    
    private static final String TESTOPS_LOG_PROPERTY_SKIP = "testops-skip-record";
    
    public static Map<String, String> getTestOpsAttributes(LogLevel status, StackTraceElement[] stackTraces) {
        if (isLogForTestCase(stackTraces)) {
            return new HashMap<>();
        }
        StackTraceElement keywordStack = getKeywordRunningStack(stackTraces);
        if (keywordStack == null) {
            Map<String, String> attbs = new HashMap<>();
            attbs.put(TESTOPS_LOG_PROPERTY_EXECUTION_STACKTRACE, getFormattedStackTrace(stackTraces));
            return attbs;
        }
        String className = keywordStack.getClassName();
        boolean isBuiltIn = className.startsWith("com.kms.katalon") && className.contains("builtin");
        if (isBuiltIn) {
            return getTestOpsAttributesForBuiltInKeyword(keywordStack, status, stackTraces);
        }
        return getTestOpsAttributesForNormalKeyword(keywordStack, stackTraces);
    }
    
    public static Map<String, String> getTestOpsAttributesForNormalKeyword(StackTraceElement stacktrace, StackTraceElement[] stackTraces) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put(TESTOPS_LOG_PROPERTY_METHOD_NAME, stacktrace.getClassName() + "." + stacktrace.getMethodName());
        attributes.put(TESTOPS_LOG_PROPERTY_EXECUTION_STACKTRACE, getFormattedStackTrace(stackTraces));
        return attributes;
    }
    
    public static Map<String, String> getTestOpsAttributesForBuiltInKeyword(StackTraceElement stacktrace, LogLevel status, StackTraceElement[] stackTraces) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put(TESTOPS_LOG_PROPERTY_METHOD_NAME, stacktrace.getClassName() + "." + stacktrace.getMethodName());
        attributes.put(TESTOPS_LOG_PROPERTY_EXECUTION_STACKTRACE, StringUtils.EMPTY);
        
        boolean isAssertion = stacktrace.getMethodName().startsWith("verify");
        if (isAssertion) {
            attributes.put(TESTOPS_LOG_PROPERTY_IS_ASSERTION, String.valueOf(isAssertion));
        }
        if (status == LogLevel.FAILED) {
            attributes.put(TESTOPS_LOG_PROPERTY_EXECUTION_STACKTRACE, getFormattedStackTrace(stackTraces));
        }
        
        return attributes;
    }
    
    private static String getFormattedStackTrace(StackTraceElement[] stackTraces) {
        Throwable t = new Throwable();
        t.setStackTrace(stackTraces);
        StackTraceElement[] leanStacks = trimStackTrace(ExceptionsUtil.getRawStackTraceForThrowable(t));
        return Arrays.asList(leanStacks).stream()
                .map(st -> st.toString() + "\n")
                .reduce("", StringUtils::join);
    }
    
    private static StackTraceElement[] trimStackTrace(StackTraceElement[] stacks) {
        int trimLength = stacks.length;
        for (int i = 0; i < stacks.length; ++i) {
            StackTraceElement s = stacks[i];
            if (s.getClassName().equals("com.kms.katalon.core.main.ScriptEngine") && s.getMethodName().equals("run")) {
                trimLength = i;
                break;
            }
        }
        return Arrays.copyOf(stacks, trimLength);
    }
    
    public static StackTraceElement getKeywordRunningStack(StackTraceElement[] stackTraces) {
        for (int i = 0; i < stackTraces.length; ++i) {
            StackTraceElement s = stackTraces[i];
            if (isKeywordExecutorStack(s)) {
                if (i + 1 >= stackTraces.length) {
                    return null;
                }
                return stackTraces[i + 1];
            }
        }
        return null;
    }
    
    public static boolean isKeywordExecutorStack(StackTraceElement stack) {
        return stack.getClassName().startsWith("com.kms.katalon.core")
                && stack.getFileName() != null
                && stack.getFileName().endsWith("KeywordMain.groovy")
                && stack.getMethodName().startsWith("runKeyword");
    }
    
    public static boolean isLogForTestCase(StackTraceElement[] stackTraces) {
        return Arrays.stream(stackTraces)
                .anyMatch(st -> "onExecutionComplete".equals(st.getMethodName())
                        && "com.kms.katalon.core.main.TestCaseExecutor".equals(st.getClassName()));
    }
    
    public static void handleFailedLogEntry(Map<String, String> attributes, Throwable throwable) {
        if (throwable == null || throwable.getCause() == null) {
            StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
            attributes.putAll(getTestOpsAttributes(LogLevel.FAILED, stackTraces));
        }
        attributes.put(TESTOPS_LOG_PROPERTY_SKIP, "true");
    }
    
    public static void handleFailedLogEntry(Map<String, String> attributes) {
        if (!isHandledTestOpsProperties(attributes)) {
            StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
            attributes.putAll(TestOpsLogHelper.getTestOpsAttributes(LogLevel.FAILED, stackTraces));
            attributes.remove(TESTOPS_LOG_PROPERTY_SKIP);
        }
    }
    
    private static boolean isHandledTestOpsProperties(Map<String, String> properties) {
        return properties.keySet().stream().anyMatch(s -> s.equals(TESTOPS_LOG_PROPERTY_SKIP));
    }
    
}
