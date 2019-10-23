package com.kms.katalon.core.logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;

import com.kms.katalon.core.constants.CoreMessageConstants;
import com.kms.katalon.core.constants.StringConstants;
import com.kms.katalon.core.logging.model.ILogRecord;
import com.kms.katalon.core.logging.model.MessageLogRecord;
import com.kms.katalon.core.logging.model.TestCaseLogRecord;
import com.kms.katalon.core.logging.model.TestStatus;
import com.kms.katalon.core.logging.model.TestStatus.TestStatusValue;
import com.kms.katalon.core.logging.model.TestStepLogRecord;
import com.kms.katalon.core.logging.model.TestSuiteLogRecord;

public class TestSuiteXMLLogParser {
    private static final String XML_DESCRIPTOR_SUFFIX = "?>";

    private static final String XML_DESCRIPTOR_PREFIX = "<?xml";

    private static final String LOG_FILE_START_TAG = "<log>";

    private static final String LOG_FILE_END_TAG = "</log>";

    private static final String UTF_8 = "UTF-8";

    private TestSuiteLogRecord testSuiteLogRecord;

    private XmlLogRecord xmlLogRecord;

    public TestSuiteLogRecord readTestSuiteLogFromXMLFiles(String logFolder, IProgressMonitor progressMonitor)
            throws XMLStreamException, IOException {
        File[] xmlFiles = XMLLoggerParser.getSortedLogFile(logFolder);
        if (xmlFiles == null || xmlFiles.length == 0) {
            return null;
        }
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        Deque<Object> stack = new ArrayDeque<Object>();
        progressMonitor.beginTask(CoreMessageConstants.MSG_INFO_PARSING_LOG_FILES, xmlFiles.length);
        for (File xmlFile : xmlFiles) {
            try {
                processFile(logFolder, progressMonitor, inputFactory, stack, xmlFile);
            } catch (XMLStreamException e) {
                // error parsing the log file, try to clean up and parsing again
                cleanUpXmlLogFile(xmlFile);
                processFile(logFolder, progressMonitor, inputFactory, stack, xmlFile);
            }
        }
        // If execution process crashed before completed
        while (stack.size() > 0) {
            Object object = stack.pollLast();
            if (object instanceof ILogRecord) {
                processInterruptedLog((ILogRecord) object);
            }
        }
        return testSuiteLogRecord;
    }

    private void processFile(String logFolder, IProgressMonitor progressMonitor, XMLInputFactory inputFactory,
            Deque<Object> stack, File xmlFile) throws IOException, XMLStreamException, FileNotFoundException {
        progressMonitor.subTask(MessageFormat.format(CoreMessageConstants.MSG_INFO_PARSING_X, xmlFile.getName()));
        if (progressMonitor.isCanceled()) {
            return;
        }
        XMLStreamReader reader = null;
        try {
            reader = inputFactory.createXMLStreamReader(new FileReader(xmlFile));
            while (reader.hasNext()) {
                int eventType = reader.next();
                switch (eventType) {
                    case XMLStreamReader.START_ELEMENT:
                        String elementName = reader.getLocalName();
                        if (!elementName.equals(XMLLoggerParser.LOG_RECORD_NODE_NAME)) {
                            continue;
                        }
                        xmlLogRecord = XMLLoggerParser.readRecord(reader);
                        String sourceMethodName = xmlLogRecord.getSourceMethodName();
                        if (LogLevel.START.toString().equals(xmlLogRecord.getLevel().getName())) {
                            if (StringConstants.LOG_START_SUITE_METHOD.equals(sourceMethodName)) {
                                testSuiteLogRecord = processStartTestSuiteLog(stack, logFolder, xmlLogRecord);
                            } else if (StringConstants.LOG_START_TEST_METHOD.equals(sourceMethodName)) {
                                processStartTestCaseLog(stack, xmlLogRecord);
                            } else if (StringConstants.LOG_START_KEYWORD_METHOD.equals(sourceMethodName)) {
                                processStartKeywordLog(stack, xmlLogRecord);
                            }
                        } else if (LogLevel.END.toString().equals(xmlLogRecord.getLevel().getName())) {
                            if (StringConstants.LOG_END_KEYWORD_METHOD.equals(sourceMethodName)
                                    || StringConstants.LOG_END_TEST_METHOD.equals(sourceMethodName)
                                    || StringConstants.LOG_END_SUITE_METHOD.equals(sourceMethodName)) {
                                processEndLog(stack, xmlLogRecord);
                            }
                        } else if (LogLevel.RUN_DATA.toString().equals(xmlLogRecord.getLevel().getName())) {
                            testSuiteLogRecord.addRunData(xmlLogRecord.getProperties());
                        } else {
                            Object object = stack.peekLast();
                            if (object instanceof ILogRecord) {
                                processStepMessageLog(xmlLogRecord, (ILogRecord) object);
                            }
                        }
                        break;
                    case XMLStreamReader.END_ELEMENT:
                        break;
                }
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
            progressMonitor.worked(1);
        }
    }

    /**
     * Make sure xml log file is well-formed before parsing
     * 
     * @param xmlFile
     * @throws IOException
     */
    private static void cleanUpXmlLogFile(File xmlFile) throws IOException {
        FileInputStream inputStream = null;
        Scanner sc = null;
        StringBuilder cleanContentBuilder = new StringBuilder();
        try {
            inputStream = new FileInputStream(xmlFile);
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (isMalformXmlLine(cleanContentBuilder, line)) {
                    // skip this
                    continue;
                }
                if (line.startsWith(LOG_FILE_END_TAG)) {
                    line = line.replaceFirst(LOG_FILE_END_TAG, StringUtils.EMPTY);
                }
                cleanContentBuilder.append(line).append("\n");
            }
            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }
        String cleanContent = cleanContentBuilder.toString();
        if (!cleanContent.endsWith(LOG_FILE_END_TAG)) {
            cleanContent += LOG_FILE_END_TAG;
        }
        FileUtils.writeStringToFile(xmlFile, cleanContent, UTF_8);
    }

    private static boolean isMalformXmlLine(StringBuilder cleanContentBuilder, String line) {
        return ((line.startsWith(XML_DESCRIPTOR_PREFIX) && line.endsWith(XML_DESCRIPTOR_SUFFIX)
                && cleanContentBuilder.lastIndexOf(XML_DESCRIPTOR_PREFIX) != -1)
                || (line.equals(LOG_FILE_START_TAG) && cleanContentBuilder.lastIndexOf(LOG_FILE_START_TAG) != -1)
                || line.equals(LOG_FILE_END_TAG));
    }

    private static void processInterruptedLog(ILogRecord logRecord) {
        ILogRecord[] childRecords = logRecord.getChildRecords();
        if (childRecords != null && childRecords.length > 0) {
            ILogRecord lastLogRecord = childRecords[childRecords.length - 1];
            logRecord.setEndTime(
                    lastLogRecord.getEndTime() != 0 ? lastLogRecord.getEndTime() : lastLogRecord.getStartTime());
        } else {
            logRecord.setEndTime(logRecord.getStartTime());
        }
        logRecord.setInterrupted(true);
    }

    private static void processStepMessageLog(XmlLogRecord xmlLogRecord, ILogRecord logRecord) {
        if (logRecord instanceof TestSuiteLogRecord) {
            // not adding any message log to test suite
            return;
        }
        MessageLogRecord messageLogRecord = new MessageLogRecord();
        messageLogRecord.setStartTime(xmlLogRecord.getMillis());
        messageLogRecord.setMessage(xmlLogRecord.getMessage());

        if (xmlLogRecord.getProperties() != null
                && xmlLogRecord.getProperties().get(StringConstants.XML_LOG_ATTACHMENT_PROPERTY) != null) {
            messageLogRecord
                    .setAttachment(xmlLogRecord.getProperties().get(StringConstants.XML_LOG_ATTACHMENT_PROPERTY));
        }
        LogLevel logLevel = LogLevel.valueOf(xmlLogRecord.getLevel().toString());
        TestStatus testStatus = evalTestStatus(logRecord, logLevel);
        messageLogRecord.setStatus(testStatus);
        logRecord.addChildRecord(messageLogRecord);
    }

    private static void processEndLog(Deque<Object> stack, XmlLogRecord xmlLogRecord) {
        Object object = stack.pollLast();
        if (object != null && object instanceof ILogRecord) {
            ((ILogRecord) object).setEndTime(xmlLogRecord.getMillis());
        }
    }

    private static String getTestLogName(XmlLogRecord xmlLogRecord) {
        String testLogName = xmlLogRecord.getMessage();
        if (testLogName == null) {
            return "";
        }

        String startKeywordString = StringConstants.LOG_START_ACTION_PREFIX;
        if (testLogName.startsWith(startKeywordString)) {
            return testLogName.substring(startKeywordString.length(), testLogName.length());
        } else {
            return testLogName;
        }
    }

    private static void processStartKeywordLog(Deque<Object> stack, XmlLogRecord xmlLogRecord) {
        TestStepLogRecord testStepLogRecord = new TestStepLogRecord(getTestLogName(xmlLogRecord));
        testStepLogRecord.setStartTime(xmlLogRecord.getMillis());
        testStepLogRecord
                .setDescription(xmlLogRecord.getProperties().containsKey(StringConstants.XML_LOG_DESCRIPTION_PROPERTY)
                        ? xmlLogRecord.getProperties().get(StringConstants.XML_LOG_DESCRIPTION_PROPERTY) : "");
        try {
            testStepLogRecord
                    .setIndex(Integer.valueOf(xmlLogRecord.getProperties().get(StringConstants.XML_LOG_STEP_INDEX)));
        } catch (NumberFormatException e) {
            // error with log, set -1 to indicate error
            testStepLogRecord.setIndex(-1);
        }

        testStepLogRecord.setIgnoredIfFailed(
                Boolean.valueOf(xmlLogRecord.getProperties().get(StringConstants.XML_LOG_IS_IGNORED_IF_FAILED)));
        Object object = stack.peekLast();
        if (object instanceof TestCaseLogRecord || object instanceof TestStepLogRecord) {
            ((ILogRecord) object).addChildRecord(testStepLogRecord);
        }

        stack.add(testStepLogRecord);
    }

    private static void processStartTestCaseLog(Deque<Object> stack, XmlLogRecord xmlLogRecord) {
        TestCaseLogRecord testCaseLogRecord = new TestCaseLogRecord(
                xmlLogRecord.getProperties().containsKey(StringConstants.XML_LOG_NAME_PROPERTY)
                        ? xmlLogRecord.getProperties().get(StringConstants.XML_LOG_NAME_PROPERTY)
                        : getTestLogName(xmlLogRecord));
        testCaseLogRecord.setStartTime(xmlLogRecord.getMillis());
        testCaseLogRecord.setId(xmlLogRecord.getProperties().containsKey(StringConstants.XML_LOG_ID_PROPERTY)
                ? xmlLogRecord.getProperties().get(StringConstants.XML_LOG_ID_PROPERTY) : "");
        testCaseLogRecord.setSource(xmlLogRecord.getProperties().containsKey(StringConstants.XML_LOG_SOURCE_PROPERTY)
                ? xmlLogRecord.getProperties().get(StringConstants.XML_LOG_SOURCE_PROPERTY) : "");
        testCaseLogRecord
                .setDescription(xmlLogRecord.getProperties().containsKey(StringConstants.XML_LOG_DESCRIPTION_PROPERTY)
                        ? StringEscapeUtils.unescapeJava(xmlLogRecord.getProperties().get(StringConstants.XML_LOG_DESCRIPTION_PROPERTY)) : "");
        testCaseLogRecord.setTag(xmlLogRecord.getProperties().containsKey(StringConstants.XML_LOG_TAG_PROPERTY)
                ? StringEscapeUtils.unescapeJava(xmlLogRecord.getProperties().get(StringConstants.XML_LOG_TAG_PROPERTY))
                : "");
        testCaseLogRecord.setOptional(xmlLogRecord.getProperties().containsKey(StringConstants.XML_LOG_IS_OPTIONAL)
                ? Boolean.valueOf(xmlLogRecord.getProperties().get(StringConstants.XML_LOG_IS_OPTIONAL)) : false);
        Object object = stack.peekLast();
        if (object instanceof TestSuiteLogRecord || object instanceof TestStepLogRecord) {
            ((ILogRecord) object).addChildRecord(testCaseLogRecord);
        }
        stack.add(testCaseLogRecord);
    }

    private static TestSuiteLogRecord processStartTestSuiteLog(Deque<Object> stack, String logFolder,
            XmlLogRecord xmlLogRecord) {
        TestSuiteLogRecord testSuiteLogRecord = new TestSuiteLogRecord("", logFolder);
        testSuiteLogRecord.setStartTime(xmlLogRecord.getMillis());
        testSuiteLogRecord.setName(xmlLogRecord.getProperties().containsKey(StringConstants.XML_LOG_NAME_PROPERTY)
                ? xmlLogRecord.getProperties().get(StringConstants.XML_LOG_NAME_PROPERTY)
                : getTestLogName(xmlLogRecord));
        testSuiteLogRecord.setId(xmlLogRecord.getProperties().containsKey(StringConstants.XML_LOG_ID_PROPERTY)
                ? xmlLogRecord.getProperties().get(StringConstants.XML_LOG_ID_PROPERTY) : "");
        testSuiteLogRecord.setSource(xmlLogRecord.getProperties().containsKey(StringConstants.XML_LOG_SOURCE_PROPERTY)
                ? xmlLogRecord.getProperties().get(StringConstants.XML_LOG_SOURCE_PROPERTY) : "");
        testSuiteLogRecord.setDevicePlatform(
                xmlLogRecord.getProperties().containsKey(StringConstants.XML_LOG_DEVICE_PLATFORM_PROPERTY)
                        ? xmlLogRecord.getProperties().get(StringConstants.XML_LOG_DEVICE_PLATFORM_PROPERTY) : "");
        testSuiteLogRecord
                .setDescription(xmlLogRecord.getProperties().containsKey(StringConstants.XML_LOG_DESCRIPTION_PROPERTY)
                        ? xmlLogRecord.getProperties().get(StringConstants.XML_LOG_DESCRIPTION_PROPERTY) : "");
        stack.add(testSuiteLogRecord);
        return testSuiteLogRecord;
    }

    private static TestStatus evalTestStatus(ILogRecord logRecord, LogLevel level) {
        TestStatus testStatus = new TestStatus();
        testStatus.setStatusValue(TestStatusValue.valueOf(level.name()));
        return testStatus;
    }
}
