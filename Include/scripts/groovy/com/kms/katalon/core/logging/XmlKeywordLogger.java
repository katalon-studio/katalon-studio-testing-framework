package com.kms.katalon.core.logging;

import static com.kms.katalon.core.constants.StringConstants.DF_CHARSET;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SocketHandler;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.constants.CoreMessageConstants;
import com.kms.katalon.core.constants.StringConstants;
import com.kms.katalon.core.logging.KeywordLogger.KeywordStackElement;
import com.kms.katalon.core.util.internal.ExceptionsUtil;

class XmlKeywordLogger {
    
    private static final int MAXIMUM_LOG_FILES = 100;

    private static final int MAXIMUM_LOG_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    private Logger logger;

    private String pendingDescription = null;

    private Stack<KeywordStackElement> currentKeywordStack = null;

    private Stack<Stack<KeywordStackElement>> keywordStacksContainer = new Stack<Stack<KeywordStackElement>>();

    private int nestedLevel;

    private static final ThreadLocal<XmlKeywordLogger> localXmlKeywordLoggerStorage = new ThreadLocal<XmlKeywordLogger>() {
    
        protected XmlKeywordLogger initialValue() {
            return new XmlKeywordLogger();
        }
    };

    static XmlKeywordLogger getInstance() {
        return localXmlKeywordLoggerStorage.get();
    }

    private XmlKeywordLogger() {
    }

    /**
     * @return Returns current logger if it exists. Otherwise, create a new one includes: customized log file with XML
     * format and customized console handler.
     */
    private Logger getLogger() {
        if (logger == null) {
            logger = Logger.getLogger(XmlKeywordLogger.class.getName());

            // remove default parent's setting
            logger.setUseParentHandlers(false);
        }

        if (isInRunningMode()) {
            String logFolder = getLogFolderPath();
            if (logger.getHandlers().length == 0 && StringUtils.isNotEmpty(logFolder)) {
                try {
                    // Split log into 100 files, every file is maximum 10MB
                    FileHandler fileHandler = new FileHandler(logFolder + File.separator + "execution%g.log",
                            MAXIMUM_LOG_FILE_SIZE, MAXIMUM_LOG_FILES, true);
    
                    fileHandler.setEncoding(DF_CHARSET);
                    fileHandler.setFormatter(new CustomXmlFormatter());
                    logger.addHandler(fileHandler);
    
                    SocketHandler socketHandler = new SystemSocketHandler(StringConstants.DF_LOCAL_HOST_ADDRESS, getPort());
                    socketHandler.setEncoding(DF_CHARSET);
                    socketHandler.setFormatter(new CustomSocketLogFomatter());
                    logger.addHandler(socketHandler);
                } catch (SecurityException e) {
                    System.err.println(
                            MessageFormat.format(CoreMessageConstants.MSG_ERR_UNABLE_TO_CREATE_LOGGER, e.getMessage()));
                } catch (IOException e) {
                    System.err.println(
                            MessageFormat.format(CoreMessageConstants.MSG_ERR_UNABLE_TO_CREATE_LOGGER, e.getMessage()));
                }
            }
        }
        return logger;
    }

    private boolean isInRunningMode() {
        return getPort() != 0;
    }

    /* (non-Javadoc)
     * @see com.kms.katalon.core.logging.IKeywordLogger#close()
     */

    void close() {
        for (Handler handler : logger.getHandlers()) {
            handler.close();
        }
    }

    static void cleanUp() {

    }

    /* (non-Javadoc)
     * @see com.kms.katalon.core.logging.IKeywordLogger#getLogFolderPath()
     */

    String getLogFolderPath() {
        String logFilePath = RunConfiguration.getSettingFilePath();
        return (logFilePath != null) ? new File(logFilePath).getParentFile().getAbsolutePath() : null;
    }

    private int getPort() {
        return RunConfiguration.getPort();
    }

    /* (non-Javadoc)
     * @see com.kms.katalon.core.logging.IKeywordLogger#startSuite(java.lang.String, java.util.Map)
     */

    void startSuite(String name, Map<String, String> attributes) {
        getLogger().log(new XmlLogRecord(
                LogLevel.START.getLevel(), 
                StringConstants.LOG_START_SUITE + " : " + name,
                nestedLevel, 
                attributes));
    }

    /* (non-Javadoc)
     * @see com.kms.katalon.core.logging.IKeywordLogger#endSuite(java.lang.String, java.util.Map)
     */

    void endSuite(String name, Map<String, String> attributes) {
        getLogger().log(new XmlLogRecord(
                LogLevel.END.getLevel(), 
                StringConstants.LOG_END_SUITE + " : " + name,
                nestedLevel, 
                attributes));
    }

    /* (non-Javadoc)
     * @see com.kms.katalon.core.logging.IKeywordLogger#startTest(java.lang.String, java.util.Map, java.util.Stack)
     */

    void startTest(String name, Map<String, String> attributes, Stack<KeywordStackElement> keywordStack) {
        nestedLevel++;
        getLogger().log(new XmlLogRecord(
                LogLevel.START.getLevel(), 
                StringConstants.LOG_START_TEST + " : " + name,
                nestedLevel, 
                attributes));
        setCurrentKeywordStack(keywordStack);
    }
    
    private void setCurrentKeywordStack(Stack<KeywordStackElement> newKeywordStack) {
        if (currentKeywordStack != null) {
            keywordStacksContainer.push(currentKeywordStack);
        }
        this.currentKeywordStack = newKeywordStack;
    }
    
    /* (non-Javadoc)
     * @see com.kms.katalon.core.logging.IKeywordLogger#endTest(java.lang.String, java.util.Map)
     */

    void endTest(String name, Map<String, String> attributes) {
        nestedLevel--;
        getLogger().log(new XmlLogRecord(
                LogLevel.END.getLevel(), 
                StringConstants.LOG_END_TEST + " : " + name,
                nestedLevel, 
                attributes));
        restorePreviousKeywordStack();
    }
    
    private void restorePreviousKeywordStack() {
        if (!keywordStacksContainer.isEmpty()) {
            currentKeywordStack = keywordStacksContainer.pop();
        } else {
            currentKeywordStack = null;
        }
    }

    /* (non-Javadoc)
     * @see com.kms.katalon.core.logging.IKeywordLogger#startListenerKeyword(java.lang.String, java.util.Map, java.util.Stack)
     */

    void startListenerKeyword(String name, Map<String, String> attributes,
            Stack<KeywordStackElement> keywordStack) {
        Map<String, String> modifiedAttr = new HashMap<>(attributes != null ? attributes : Collections.emptyMap());
        modifiedAttr.put(StringConstants.XML_LOG_IS_IGNORED_IF_FAILED, Boolean.toString(true));
        startKeyword(name, StringConstants.LOG_LISTENER_ACTION, attributes, keywordStack);
    }

    /* (non-Javadoc)
     * @see com.kms.katalon.core.logging.IKeywordLogger#startKeyword(java.lang.String, java.lang.String, java.util.Map, java.util.Stack)
     */

    void startKeyword(String name, String actionType, Map<String, String> attributes,
            Stack<KeywordStackElement> keywordStack) {
        if (attributes == null) {
            attributes = new HashMap<String, String>();
        }
        if (pendingDescription != null) {
            attributes.put(StringConstants.XML_LOG_DESCRIPTION_PROPERTY, pendingDescription);
            pendingDescription = null;
        }
        getLogger().log(new XmlLogRecord(
                LogLevel.START.getLevel(), 
                "Start " + actionType + " : " + name, 
                nestedLevel, 
                attributes));
        if (currentKeywordStack != null) {
            keywordStacksContainer.push(currentKeywordStack);
        }
        this.currentKeywordStack = keywordStack;
    }

    /* (non-Javadoc)
     * @see com.kms.katalon.core.logging.IKeywordLogger#startKeyword(java.lang.String, java.util.Map, java.util.Stack)
     */

    void startKeyword(String name, Map<String, String> attributes, Stack<KeywordStackElement> keywordStack) {
        if (attributes == null) {
            attributes = new HashMap<String, String>();
        }
        if (pendingDescription != null) {
            attributes.put(StringConstants.XML_LOG_DESCRIPTION_PROPERTY, pendingDescription);
            pendingDescription = null;
        }
        getLogger().log(new XmlLogRecord(
                LogLevel.START.getLevel(), 
                StringConstants.LOG_START_ACTION_PREFIX + name,
                nestedLevel, 
                attributes));
        if (currentKeywordStack != null) {
            keywordStacksContainer.push(currentKeywordStack);
        }
        this.currentKeywordStack = keywordStack;
    }

    /* (non-Javadoc)
     * @see com.kms.katalon.core.logging.IKeywordLogger#startKeyword(java.lang.String, java.util.Map, int)
     */

    void startKeyword(String name, Map<String, String> attributes, int nestedLevel) {
        if (attributes == null) {
            attributes = new HashMap<String, String>();
        }
        if (pendingDescription != null) {
            attributes.put(StringConstants.XML_LOG_DESCRIPTION_PROPERTY, pendingDescription);
            pendingDescription = null;
        }
        popKeywordFromStack(nestedLevel);
        getLogger().log(new XmlLogRecord(
                LogLevel.START.getLevel(), 
                StringConstants.LOG_START_ACTION_PREFIX + name,
                nestedLevel, 
                attributes));
        pushKeywordToStack(name, nestedLevel);
    }

    private void pushKeywordToStack(String keywordName, int nestedLevel) {
        if (currentKeywordStack != null) {
            currentKeywordStack.push(new KeywordStackElement(keywordName, nestedLevel));
        }
    }

    private void popKeywordFromStack(int nestedLevel) {
        while (currentKeywordStack != null && !currentKeywordStack.isEmpty()
                && currentKeywordStack.peek().getNestedLevel() >= nestedLevel) {
            KeywordStackElement keywordStackElement = currentKeywordStack.pop();
            endKeyword(keywordStackElement.getKeywordName(), null, keywordStackElement.getNestedLevel());
        }
    }

    /* (non-Javadoc)
     * @see com.kms.katalon.core.logging.IKeywordLogger#endKeyword(java.lang.String, java.util.Map, int)
     */

    void endKeyword(String name, Map<String, String> attributes, int nestedLevel) {
        getLogger().log(new XmlLogRecord(
                LogLevel.END.getLevel(), 
                StringConstants.LOG_END_KEYWORD + " : " + name,
                nestedLevel, 
                attributes));
    }

    /* (non-Javadoc)
     * @see com.kms.katalon.core.logging.IKeywordLogger#endListenerKeyword(java.lang.String, java.util.Map, java.util.Stack)
     */

    void endListenerKeyword(String name, Map<String, String> attributes,
            Stack<KeywordStackElement> keywordStack) {
        endKeyword(name, StringConstants.LOG_LISTENER_ACTION, attributes, keywordStack);
    }

    /* (non-Javadoc)
     * @see com.kms.katalon.core.logging.IKeywordLogger#endKeyword(java.lang.String, java.lang.String, java.util.Map, java.util.Stack)
     */

    void endKeyword(String name, String keywordType, Map<String, String> attributes,
            Stack<KeywordStackElement> keywordStack) {
        getLogger().log(new XmlLogRecord(LogLevel.END.getLevel(), 
                "End " + keywordType + " : " + name, nestedLevel, attributes));
        if (currentKeywordStack == keywordStack && !keywordStacksContainer.isEmpty()) {
            currentKeywordStack = keywordStacksContainer.pop();
        } else {
            currentKeywordStack = null;
        }
    }

    /* (non-Javadoc)
     * @see com.kms.katalon.core.logging.IKeywordLogger#endKeyword(java.lang.String, java.util.Map, java.util.Stack)
     */

    void endKeyword(String name, Map<String, String> attributes, Stack<KeywordStackElement> keywordStack) {
        endKeyword(name, StringConstants.LOG_END_KEYWORD, attributes, keywordStack);
    }

    /* (non-Javadoc)
     * @see com.kms.katalon.core.logging.IKeywordLogger#logFailed(java.lang.String, java.util.Map)
     */

    void logFailed(String message, Map<String, String> attributes) {
        logMessage(null, LogLevel.FAILED, message, attributes);
    }

    /* (non-Javadoc)
     * @see com.kms.katalon.core.logging.IKeywordLogger#logWarning(java.lang.String, java.util.Map)
     */

    void logWarning(String message, Map<String, String> attributes) {
        logMessage(null, LogLevel.WARNING, message, attributes);
    }

    /* (non-Javadoc)
     * @see com.kms.katalon.core.logging.IKeywordLogger#logPassed(java.lang.String, java.util.Map)
     */

    void logPassed(String message, Map<String, String> attributes) {
        logMessage(null, LogLevel.PASSED, message, attributes);
    }

    /* (non-Javadoc)
     * @see com.kms.katalon.core.logging.IKeywordLogger#logInfo(java.lang.String, java.util.Map)
     */

    void logInfo(KeywordLogger keywordLogger, String message, Map<String, String> attributes) {
        logMessage(keywordLogger, LogLevel.INFO, message, attributes);
    }
    
    void logDebug(KeywordLogger keywordLogger, String message, Map<String, String> attributes) {
        logMessage(keywordLogger, LogLevel.DEBUG, message, attributes);
    }

    /* (non-Javadoc)
     * @see com.kms.katalon.core.logging.IKeywordLogger#logRunData(java.lang.String, java.lang.String)
     */

    void logRunData(String dataKey, String dataValue) {
        Map<String, String> attributeMap = new HashMap<String, String>();
        attributeMap.put(dataKey, dataValue);
        logMessage(null, LogLevel.RUN_DATA, dataKey + " = " + dataValue, attributeMap);
    }

    /* (non-Javadoc)
     * @see com.kms.katalon.core.logging.IKeywordLogger#logError(java.lang.String, java.util.Map)
     */

    void logError(String message, Map<String, String> attributes) {
        logMessage(null, LogLevel.ERROR, message, attributes);
    }

    /* (non-Javadoc)
     * @see com.kms.katalon.core.logging.IKeywordLogger#logMessage(com.kms.katalon.core.logging.LogLevel, java.lang.String, java.util.Map)
     */

    void logMessage(KeywordLogger keywordLogger, LogLevel originalLevel, String message, Map<String, String> attributes) {
        if (message == null) {
            message = "";
        }
        Logger logger = getLogger();
        LogLevel level;
        if (LogLevel.DEBUG.equals(originalLevel)) {
            level = LogLevel.INFO;
        } else {
            level = originalLevel;
        }
        if (logger != null) {
            XmlLogRecord xmlLogRecord = new XmlLogRecord(level.getLevel(), message, nestedLevel, attributes);
            if (shouldLog(keywordLogger, originalLevel)) {
                logger.log(xmlLogRecord);
            }
        }

    }
    
    private boolean shouldLog(KeywordLogger keywordLogger, LogLevel originalLevel) {
        if (keywordLogger == null) {
            return true;
        }
        if (LogLevel.INFO.equals(originalLevel) && !keywordLogger.isInfoEnabled()) {
            return false;
        }
        if (LogLevel.DEBUG.equals(originalLevel) && !keywordLogger.isDebugEnabled()) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see com.kms.katalon.core.logging.IKeywordLogger#logMessage(com.kms.katalon.core.logging.LogLevel, java.lang.String, java.lang.Throwable)
     */

    void logMessage(LogLevel level, String message, Throwable thrown) {
        Logger logger = getLogger();
        Throwable rootCause = ExceptionUtils.getRootCause(thrown);
        if (rootCause == null) {
            rootCause = thrown;
        }
        if (message == null) {
            message = "";
        }
        if (logger != null) {
            XmlLogRecord logRecord = new XmlLogRecord(level.getLevel(), message);
            Map<String, String> attributes = getAttributesFrom(thrown);
            logRecord.setThrown(thrown);
            logRecord.setProperties(attributes);
            logger.log(logRecord);
        }
    }

    /* (non-Javadoc)
     * @see com.kms.katalon.core.logging.IKeywordLogger#setPendingDescription(java.lang.String)
     */

    void setPendingDescription(String stepDescription) {
        pendingDescription = stepDescription;
    }

    /* (non-Javadoc)
     * @see com.kms.katalon.core.logging.IKeywordLogger#logNotRun(java.lang.String, java.util.Map)
     */

    void logNotRun(String message, Map<String, String> attributes) {
        logMessage(null, LogLevel.NOT_RUN, message, attributes);
    }
    
    public Map<String, String> getAttributesFrom(Throwable throwable) {
        Map<String, String> attributes = new HashMap<>();
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        if (rootCause == null) {
            rootCause = throwable;
        }
        if (rootCause != null) {
            attributes.put("failed.exception.class", rootCause.getClass().getName());
            attributes.put("failed.exception.message", rootCause.getMessage());
            attributes.put("failed.exception.stacktrace", ExceptionsUtil.getStackTraceForThrowable(rootCause));
        }
        return attributes;
    }
}
