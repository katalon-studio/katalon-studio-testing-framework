package com.kms.katalon.core.logging;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.constants.StringConstants;
import com.kms.katalon.core.main.ScriptEngine;
import com.kms.katalon.core.util.internal.ExceptionsUtil;

public class KeywordLogger {
    
    private static final int HR_LENGTH = 20;
    
    private static final String TEST_CASE_HR = String.join("", Collections.nCopies(HR_LENGTH, "-"));
    
    private static final String TEST_SUITE_HR = String.join("", Collections.nCopies(HR_LENGTH, "="));

    private static final String PASSED = "\u2713"; // check
    
    private static final String FAILED = "\u274C"; // X
    
    private static final Logger selfLogger = LoggerFactory.getLogger(KeywordLogger.class);
    
    private static final Map<String, KeywordLogger> keywordLoggerLookup = new ConcurrentHashMap<>();
    
    private final Logger logger;
    
    private final XmlKeywordLogger xmlKeywordLogger;
    
    private boolean shouldLogTestSteps;
    
    public static KeywordLogger getInstance(Class<?> clazz) {
        if (clazz == null) { // just in case
            selfLogger.error("Logger name is null. This should be a bug of Katalon Studio.");
            clazz = KeywordLogger.class;
        }
        return getInstance(clazz.getName());
    }

    private static KeywordLogger getInstance(String name) {
        KeywordLogger keywordLogger = keywordLoggerLookup.get(name);
        if (keywordLogger == null) {
            String testCaseName = ScriptEngine.getTestCaseName(name);
            if (testCaseName == null) {
                keywordLogger = new KeywordLogger(name, null);
            } else {
                String fullTestCaseName = "testcase." + testCaseName;
                keywordLogger = new KeywordLogger(fullTestCaseName, null);
            }
            keywordLoggerLookup.put(name, keywordLogger);
        }
        return keywordLogger;
    }
    
    private KeywordLogger(String className, String dummy) {
        logger = LoggerFactory.getLogger(className);
        xmlKeywordLogger = XmlKeywordLogger.getInstance();
        initShouldLogTestSteps();
    }

	private void initShouldLogTestSteps() {
		Map<String, Object> executionProperties = RunConfiguration.getExecutionProperties();
		if (executionProperties == null) {
			shouldLogTestSteps = true;
		} else {
			shouldLogTestSteps = (boolean) Optional
	                .ofNullable(executionProperties.get(RunConfiguration.LOG_TEST_STEPS))
	                .orElse(false);
		}
	}
    
    private boolean shouldLogTestSteps() {
    	return shouldLogTestSteps;
    }

    public KeywordLogger(String className) {
        
        selfLogger.warn("Please use \"KeywordUtil.logInfo()\" instead of \"new KeywordLogger()\" constructor. \"KeywordLogger\" is an internal API and might be changed in the future.");

        if (StringUtils.isBlank(className)) {
            className = KeywordLogger.class.getName();
        }
        logger = LoggerFactory.getLogger(className);
        xmlKeywordLogger = XmlKeywordLogger.getInstance();
        initShouldLogTestSteps();
    }
    
    public KeywordLogger() {
        this(null);
    }


    public void close() {
        xmlKeywordLogger.close();
    }


    public String getLogFolderPath() {
        return xmlKeywordLogger.getLogFolderPath();
    }


    public void startSuite(String name, Map<String, String> attributes) {
        logger.info("START {}", name);
        xmlKeywordLogger.startSuite(name, attributes);
        
        logRunData(RunConfiguration.HOST_NAME, RunConfiguration.getHostName());
        logRunData(RunConfiguration.HOST_OS, RunConfiguration.getOS());
        logRunData(RunConfiguration.HOST_ADDRESS, RunConfiguration.getHostAddress());
        logRunData(RunConfiguration.APP_VERSION, RunConfiguration.getAppVersion());

        RunConfiguration.getCollectedTestDataProperties().entrySet().stream().forEach(collectedDataInfo -> {
            logRunData(collectedDataInfo.getKey(), collectedDataInfo.getValue());
        });
    }


    public void endSuite(String name, Map<String, String> attributes) {
        logger.info(TEST_CASE_HR);
        logger.info("END {}", name);
        logger.info(TEST_SUITE_HR);
        xmlKeywordLogger.endSuite(name, attributes);
    }


    public void startTest(String name, Map<String, String> attributes, Stack<KeywordStackElement> keywordStack) {
        logger.info(TEST_CASE_HR);
        logger.info("START {}", name);
        xmlKeywordLogger.startTest(name, attributes, keywordStack);
    }


    public void endTest(String name, Map<String, String> attributes) {
        logger.info("END {}", name);
        xmlKeywordLogger.endTest(name, attributes);
    }
    
    public void startCalledTest(String name, Map<String, String> attributes, Stack<KeywordStackElement> keywordStack) {
        logger.info(TEST_CASE_HR);
        logger.info("CALL {}", name);
        xmlKeywordLogger.startTest(name, attributes, keywordStack);
    }


    public void endCalledTest(String name, Map<String, String> attributes) {
        logger.info("END CALL {}", name);
        logger.info(TEST_CASE_HR);
        xmlKeywordLogger.endTest(name, attributes);
    }


    public void startListenerKeyword(
            String name, 
            Map<String, String> attributes,
            Stack<KeywordStackElement> keywordStack) {
        
        logStartKeyword(name, attributes);
        xmlKeywordLogger.startListenerKeyword(name, attributes, keywordStack);
    }


    public void startKeyword(
            String name, 
            String actionType, 
            Map<String, String> attributes,
            Stack<KeywordStackElement> keywordStack) {
    	
    	if (shouldLogTestSteps()) {
    		logStartKeyword(name, attributes);
    		xmlKeywordLogger.startKeyword(name, actionType, attributes, keywordStack);
    	}
    }

    private void logStartKeyword(String name, Map<String, String> attributes) {   	
        String stepIndex = getStepIndex(attributes);
        if (stepIndex == null) {
            logger.debug("STEP {}", name);
        } else {
            logger.debug("{}: {}", stepIndex, name);
        }
    }

    private String getStepIndex(Map<String, String> attributes) {
        String stepIndex = null;
        if (attributes != null) {
            stepIndex = attributes.get(StringConstants.XML_LOG_STEP_INDEX);
        }
        return stepIndex;
    }


    public void startKeyword(String name, Map<String, String> attributes, Stack<KeywordStackElement> keywordStack) {
    	
    	if (shouldLogTestSteps()) {
	        logStartKeyword(name, attributes);
	        xmlKeywordLogger.startKeyword(name, attributes, keywordStack);
    	}
    }


    public void startKeyword(String name, Map<String, String> attributes, int nestedLevel) {
    	
    	if (shouldLogTestSteps()) {
	        logStartKeyword(name, attributes);
	        xmlKeywordLogger.startKeyword(name, attributes, nestedLevel);
    	}
    }


    public void endKeyword(String name, Map<String, String> attributes, int nestedLevel) {
    	
    	if (shouldLogTestSteps()) {
	        logEndKeyword(name, attributes);
	        xmlKeywordLogger.endKeyword(name, attributes, nestedLevel);
    	}
    }

    private void logEndKeyword(String name, Map<String, String> attributes) {
        String stepIndex = getStepIndex(attributes);
        if (stepIndex == null) {
            logger.trace("END {}: {}", stepIndex, name);
        } else {
            logger.trace("END STEP {}", name);
        }
    }


    public void endListenerKeyword(
            String name, 
            Map<String, String> attributes,
            Stack<KeywordStackElement> keywordStack) {
        logEndKeyword(name, attributes);
        xmlKeywordLogger.endListenerKeyword(name, attributes, keywordStack);
    }


    public void endKeyword(
            String name, 
            String keywordType, 
            Map<String, String> attributes,
            Stack<KeywordStackElement> keywordStack) {
    	
    	if (shouldLogTestSteps()) {
	        logEndKeyword(name, attributes);
	        xmlKeywordLogger.endKeyword(name, keywordType, attributes, keywordStack);
    	}
    }


    public void endKeyword(String name, Map<String, String> attributes, Stack<KeywordStackElement> keywordStack) {
    	
    	if (shouldLogTestSteps()) {
	        logEndKeyword(name, attributes);
	        xmlKeywordLogger.endKeyword(name, attributes, keywordStack);
    	}
    }
    
    public void logFailed(String message) {
        logFailed(message, null);
    }


    public void logFailed(String message, Map<String, String> attributes, Throwable throwable) {
        if (attributes == null) {
            attributes = new HashMap<>();
        } else {
            attributes = new HashMap<>(attributes);
        }
        Map<String, String> exceptionAttributes = xmlKeywordLogger.getAttributesFrom(throwable);
        attributes.putAll(exceptionAttributes);
        logFailed(message, attributes);
    }


    public void logFailed(String message, Map<String, String> attributes) {
        logger.error("{} {}", FAILED, message);
        xmlKeywordLogger.logFailed(message, attributes);
    }


    public void logWarning(String message) {
        logWarning(message, null);
    }
    
    public void logWarning(String message, Map<String, String> attributes, Throwable throwable) {
        if (attributes == null) {
            attributes = new HashMap<>();
        } else {
            attributes = new HashMap<>(attributes);
        }
        Map<String, String> exceptionAttributes = xmlKeywordLogger.getAttributesFrom(throwable);
        attributes.putAll(exceptionAttributes);
        logWarning(message, attributes);
    }

    public void logWarning(String message, Map<String, String> attributes) {
        logger.warn(message);
        xmlKeywordLogger.logWarning(message, attributes);
    }


    public void logPassed(String message) {
        logPassed(message, null);
    }


    public void logPassed(String message, Map<String, String> attributes) {
        logger.debug("{} {}", PASSED, message);
        xmlKeywordLogger.logPassed(message, attributes);
    }


    public void logInfo(String message) {
        logInfo(message, null);
    }


    public void logInfo(String message, Map<String, String> attributes) {
        logger.info(message);
        xmlKeywordLogger.logInfo(this, message, attributes);
    }


    public void logRunData(String dataKey, String dataValue) {
        logger.info("{} = {}", dataKey, dataValue);
        xmlKeywordLogger.logRunData(dataKey, dataValue);
    }
    
    public void logError(String message, Map<String, String> attributes, Throwable throwable) {
        if (attributes == null) {
            attributes = new HashMap<>();
        } else {
            attributes = new HashMap<>(attributes);
        }
        Map<String, String> exceptionAttributes = xmlKeywordLogger.getAttributesFrom(throwable);
        attributes.putAll(exceptionAttributes);
        logError(message, attributes);
    }


    public void logError(String message) {
        logError(message, null);
    }


    public void logError(String message, Map<String, String> attributes) {
        logger.error("{} {}", FAILED, message);
        xmlKeywordLogger.logError(message, attributes);
    }


    public void logMessage(LogLevel level, String message) {
        logMessage(level, message, new HashMap<>());
    }


    public void logMessage(LogLevel level, String message, Map<String, String> attributes) {
        log(level, message);
        xmlKeywordLogger.logMessage(this, level, message, attributes);
    }

    private void log(LogLevel level, String message) {
        switch (level) {
            case WARNING:
                logger.warn(message);
                break;
            case NOT_RUN:
                logger.warn("SKIP {}", message);
                break;
            case FAILED:
            case ERROR:
            case ABORTED:
            case INCOMPLETE:
                logger.error("{} {}", FAILED, message);
                break;
            default:
                logger.info(message);
        }
    }


    public void logMessage(LogLevel level, String message, Throwable thrown) {
        log(level, message);
        xmlKeywordLogger.logMessage(level, message, thrown);
    }


    public void setPendingDescription(String stepDescription) {
        xmlKeywordLogger.setPendingDescription(stepDescription);
    }


    public void logNotRun(String message) {
        logNotRun(message, null);
    }


    public void logNotRun(String message, Map<String, String> attributes) {
        logger.warn("SKIPPED {}", message);
        xmlKeywordLogger.logNotRun(message, attributes);
    }

    public void logDebug(String message) {
        logger.debug(message);
        xmlKeywordLogger.logDebug(this, message, null);
    }
    
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }
    
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    public static class KeywordStackElement {
        private String keywordName;

        private int nestedLevel;

        public KeywordStackElement(String keywordName, int nestedLevel) {
            this.setKeywordName(keywordName);
            this.setNestedLevel(nestedLevel);
        }

        public String getKeywordName() {
            return keywordName;
        }

        public void setKeywordName(String keywordName) {
            this.keywordName = keywordName;
        }

        public int getNestedLevel() {
            return nestedLevel;
        }

        public void setNestedLevel(int nestedLevel) {
            this.nestedLevel = nestedLevel;
        }
    }
}
