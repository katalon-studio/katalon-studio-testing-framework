package com.kms.katalon.core.logging;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import com.kms.katalon.core.constants.StringConstants;

public class XmlLogRecord extends LogRecord implements Serializable {

    private static final String LOG_TIME_FORMAT = "MM-dd-yyyy hh:mm:ss a";
    private static final long serialVersionUID = 1L;
    private String method;
    private int nestedLevel;
    private String startTime;
    private String endTime;
    private List<XmlLogRecordException> exceptions;
    private Map<String, String> properties;

    public XmlLogRecord(Level level, String message) {
        super(level, message);
        setLevel(level);
        setMessage(message);
    }

    public XmlLogRecord(Level level, String message, int nestedLevel) {
        this(level, message);
        setNestedLevel(nestedLevel);
    }

    public XmlLogRecord(Level level, String message, int nestedLevel, Map<String, String> properties) {
        this(level, message, nestedLevel);
        if (properties != null) {
            setProperties(properties);
        }
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getNestedLevel() {
        return nestedLevel;
    }

    public void setNestedLevel(int nestedLevel) {
        this.nestedLevel = nestedLevel;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Map<String, String> getProperties() {
        if (properties == null)
            properties = new HashMap<String, String>();
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        String logMess = this.getMessage();
        String logLevel = this.getLevel().getName();
        return getLogTimeString() + " - " + logLevel + " - " + logMess;
    }

    public String getLogTimeString() {
        SimpleDateFormat format = new SimpleDateFormat(LOG_TIME_FORMAT);
        String logTime = format.format(new Date(this.getMillis()));
        return logTime;
    }

    public List<XmlLogRecordException> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<XmlLogRecordException> exceptions) {
        this.exceptions = exceptions;
    }

    public int getIndex() {
        String indexPropertyValue = getProperties().get(StringConstants.XML_LOG_STEP_INDEX);
        if (indexPropertyValue == null) {
            return -1;
        }
        try {
            return Integer.valueOf(indexPropertyValue);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
