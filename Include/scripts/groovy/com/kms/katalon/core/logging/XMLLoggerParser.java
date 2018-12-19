package com.kms.katalon.core.logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.regex.Pattern;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

public class XMLLoggerParser {

    private static final String UTF_8 = "UTF-8";

    public static final String LOG_RECORD_PROP_NAME_ATTRIBUTE = "name";

    public static final String LOG_RECORD_NODE_NAME = "record";

    public static final String LEVEL_NODE_NAME = "level";

    public static final String MESSAGE_NODE_NAME = "message";

    public static final String MILLIS_NODE_NAME = "millis";

    public static final String METHOD_NODE_NAME = "method";

    private static final String NESTED_LEVEL_NODE_NAME = "nestedLevel";

    // private static final String START_TIME_NODE_NAME = "startTime";
    public static final String EXCEPTION_NODE_NAME = "exception";

    public static final String EXCEPTION_FRAME_NODE_NAME = "frame";

    public static final String EXCEPTION_CLASS_NODE_NAME = "class";

    public static final String EXCEPTION_METHOD_NODE_NAME = "method";

    public static final String EXCEPTION_LINE_NODE_NAME = "line";

    public static final String LOG_RECORD_PROP_NODE_NAME = "property";

    private static final String EXECUTION_LOG_FILE_BASE = "execution";

    public static String unescapeString(String text) {
        return StringEscapeUtils.unescapeJava(StringEscapeUtils.unescapeXml(text));
    }

    public static String getRecordDate(LogRecord record) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return format.format(new Date(record.getMillis()));
    }

    public static List<XmlLogRecord> readFromString(String xmlString) throws XMLStreamException, FileNotFoundException {
        if (StringUtils.isEmpty(xmlString)) {
            return Collections.emptyList();
        }
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLStreamReader reader = null;
        try {
            reader = inputFactory.createXMLStreamReader(new StringReader(xmlString));
            return readDocument(reader);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public static File[] getSortedLogFile(String logFolder) {
        File folder = new File(logFolder);
        File[] files = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return Pattern.matches(EXECUTION_LOG_FILE_BASE + "\\d+\\.log", name);
            }
        });
        // Descending sort file names
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                int num1 = Integer
                        .parseInt(FilenameUtils.getBaseName(f1.getName()).replace(EXECUTION_LOG_FILE_BASE, ""));
                int num2 = Integer
                        .parseInt(FilenameUtils.getBaseName(f2.getName()).replace(EXECUTION_LOG_FILE_BASE, ""));
                return num2 - num1;
            }
        });
        return files;
    }

    public static List<XmlLogRecord> readFromXMLFile(File xmlFile) throws XMLStreamException, IOException {
        if (xmlFile == null || !xmlFile.exists()) {
            return Collections.emptyList();
        }
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLStreamReader reader = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(xmlFile);
            reader = inputFactory.createXMLStreamReader(fileInputStream, UTF_8);
            return readDocument(reader);
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
    }

    public static List<XmlLogRecord> readFromLogFolder(String logFolder) throws XMLStreamException, IOException {
        File[] files = getSortedLogFile(logFolder);
        List<XmlLogRecord> xmlLogRecords = new ArrayList<>();
        for (File file : files) {
            xmlLogRecords.addAll(readFromXMLFile(file));
        }
        return xmlLogRecords;
    }

    private static List<XmlLogRecord> readDocument(XMLStreamReader reader) throws XMLStreamException {
        List<XmlLogRecord> logRecords = new ArrayList<>();
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                    String elementName = reader.getLocalName();
                    if (elementName.equals(LOG_RECORD_NODE_NAME)) {
                        logRecords.add(readRecord(reader));
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    break;
            }
        }
        return logRecords;
    }

    public static XmlLogRecord readRecord(XMLStreamReader reader) throws XMLStreamException {
        XmlLogRecord record = new XmlLogRecord(Level.ALL, "");
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                    String elementName = reader.getLocalName();
                    switch (elementName) {
                        case LEVEL_NODE_NAME:
                            record.setLevel(LogLevel.valueOf(readCharacters(reader)).getLevel());
                            break;
                        case MESSAGE_NODE_NAME:
                            record.setMessage(unescapeString(readCharacters(reader)));
                            break;
                        case MILLIS_NODE_NAME:
                            record.setMillis(readLong(reader));
                            break;
                        case METHOD_NODE_NAME:
                            record.setSourceMethodName(readCharacters(reader));
                            break;
                        case EXCEPTION_NODE_NAME:
                            record.setExceptions(readExceptions(reader));
                            break;
                        case LOG_RECORD_PROP_NODE_NAME:
                            String propName = reader.getAttributeValue(null, LOG_RECORD_PROP_NAME_ATTRIBUTE);
                            String propVal = readCharacters(reader);
                            record.getProperties().put(propName, propVal);
                            break;
                        case NESTED_LEVEL_NODE_NAME:
                            record.setNestedLevel(readInt(reader));
                            break;
                        default:
                            break;
                    }
                case XMLStreamReader.END_ELEMENT:
                    elementName = reader.getLocalName();
                    if (elementName.equals(LOG_RECORD_NODE_NAME)) {
                        return record;
                    }
                    break;
            }
        }
        return record;
    }

    private static List<XmlLogRecordException> readExceptions(XMLStreamReader reader) throws XMLStreamException {
        List<XmlLogRecordException> exceptionLogRecords = new ArrayList<>();
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                    if (EXCEPTION_FRAME_NODE_NAME.equals(reader.getLocalName())) {
                        exceptionLogRecords.add(readException(reader));
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    if (EXCEPTION_NODE_NAME.equals(reader.getLocalName())) {
                        return exceptionLogRecords;
                    }
                    break;
            }
        }
        return exceptionLogRecords;

    }

    private static XmlLogRecordException readException(XMLStreamReader reader) throws XMLStreamException {
        XmlLogRecordException logException = new XmlLogRecordException();
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                    String elementName = reader.getLocalName();
                    switch (elementName) {
                        case EXCEPTION_CLASS_NODE_NAME:
                            logException.setClassName(readCharacters(reader));
                            break;
                        case EXCEPTION_METHOD_NODE_NAME:
                            logException.setMethodName(readCharacters(reader));
                            break;
                        case EXCEPTION_LINE_NODE_NAME:
                            logException.setLineNumber(readInt(reader));
                            break;
                        default:
                            break;
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    if (EXCEPTION_FRAME_NODE_NAME.equals(reader.getLocalName())) {
                        return logException;
                    }
                    break;
            }
        }
        return logException;
    }

    private static long readLong(XMLStreamReader reader) throws XMLStreamException {
        String characters = readCharacters(reader);
        try {
            return Long.valueOf(characters);
        } catch (NumberFormatException e) {
            throw new XMLStreamException("Invalid long " + characters);
        }
    }

    private static int readInt(XMLStreamReader reader) throws XMLStreamException {
        String characters = readCharacters(reader);
        try {
            return Integer.valueOf(characters);
        } catch (NumberFormatException e) {
            throw new XMLStreamException("Invalid integer " + characters);
        }
    }

    private static String readCharacters(XMLStreamReader reader) throws XMLStreamException {
        StringBuilder result = new StringBuilder();
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.CHARACTERS:
                case XMLStreamReader.CDATA:
                    result.append(reader.getText());
                    break;
                case XMLStreamReader.END_ELEMENT:
                    return result.toString();
                default:
                    break;
            }
        }
        return result.toString();
    }
}
