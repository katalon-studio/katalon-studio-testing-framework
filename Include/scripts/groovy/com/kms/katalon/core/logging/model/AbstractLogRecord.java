package com.kms.katalon.core.logging.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kms.katalon.core.logging.model.TestStatus.TestStatusValue;
import com.kms.katalon.core.util.internal.DateUtil;

public abstract class AbstractLogRecord implements ILogRecord {
    protected static final String LINE_SEPARATOR = System.getProperty("line.separator");

    protected String type;

    protected String name;

    protected String id;

    protected String source;

    protected String message;

    protected String description;

    protected long startTime;

    protected long endTime;

    protected List<ILogRecord> childRecords;

    protected ILogRecord parentLogRecord;

    protected boolean interuppted;

    public AbstractLogRecord(String name) {
        setName(name);
        childRecords = new ArrayList<ILogRecord>();
        interuppted = false;
        type = "";
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public long getEndTime() {
        return endTime;
    }

    @Override
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public TestStatus getStatus() {
        TestStatus testStatus = new TestStatus();
        if (isInterrupted()) {
            testStatus.setStatusValue(TestStatusValue.INCOMPLETE);
            return testStatus;
        } else {
            testStatus.setStatusValue(TestStatusValue.PASSED);
        }

        if (childRecords == null || childRecords.size() == 0) {
            return testStatus;
        }

        setMessage(childRecords.get(childRecords.size() - 1).getMessage());

        for (int index = childRecords.size() - 1; index >= 0; index--) {
            ILogRecord messageRecord = childRecords.get(index);
            if (!(messageRecord instanceof MessageLogRecord)) {
                continue;
            }
            TestStatusValue logRecordStatusValue = messageRecord.getStatus().getStatusValue();

            if (logRecordStatusValue == TestStatusValue.ERROR || logRecordStatusValue == TestStatusValue.FAILED
                    || logRecordStatusValue == TestStatusValue.INCOMPLETE
                    || logRecordStatusValue == TestStatusValue.PASSED) {
                testStatus.setStatusValue(logRecordStatusValue);
                setMessage(messageRecord.getMessage());
                return testStatus;
            }
        }

        for (ILogRecord logRecord : getChildRecords()) {
            if (!(logRecord instanceof TestCaseLogRecord && ((TestCaseLogRecord) logRecord).isOptional())) {
                TestStatusValue logRecordStatusValue = logRecord.getStatus().getStatusValue();
                if (logRecordStatusValue == TestStatusValue.ERROR || logRecordStatusValue == TestStatusValue.FAILED
                        || logRecordStatusValue == TestStatusValue.INCOMPLETE) {
                    testStatus.setStatusValue(logRecordStatusValue);
                    setMessage(logRecord.getMessage());
                    return testStatus;
                }
            }
        }

        return testStatus;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean hasChildRecords() {
        return childRecords != null && childRecords.size() > 0;
    }

    @Override
    public ILogRecord[] getChildRecords() {
        return childRecords.toArray(new ILogRecord[0]);
    }

    @Override
    public void addChildRecord(ILogRecord childRecord) {
        childRecords.add(childRecord);
        childRecord.setParentLogRecord(this);
    }

    @Override
    public void removeChildRecord(ILogRecord childRecord) {
        childRecords.remove(childRecord);
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public ILogRecord getParentLogRecord() {
        return parentLogRecord;
    }

    @Override
    public void setParentLogRecord(ILogRecord parentLogRecord) {
        this.parentLogRecord = parentLogRecord;
    }

    /**
     * Returns an array of attachments of current record's all descendant.
     * 
     * @return An array of String that each element represents for the location of an attachment file.
     */
    public String[] getAttachments() {
        return getAttachments(false);
    }

    public String[] getAttachments(boolean isInAbsolutePath) {
        List<String> attachments = new ArrayList<String>();

        ILogRecord[] childRecords = getChildRecords();

        if (childRecords != null) {
            for (ILogRecord childRc : childRecords) {
                attachments.addAll(Arrays.asList(((AbstractLogRecord) childRc).getAttachments(isInAbsolutePath)));
            }
        }

        String attachment = null;

        if (this instanceof MessageLogRecord) {
            attachment = ((MessageLogRecord) this).getAttachment(isInAbsolutePath);
        }
        if (!StringUtils.isBlank(attachment)) {
            attachments.add(attachment);
        }

        return attachments.toArray(new String[attachments.size()]);
    }

    @Override
    public boolean isInterrupted() {
        return interuppted;
    }

    @Override
    public void setInterrupted(boolean interrupted) {
        interuppted = interrupted;
    }

    public String[] getJsonExcludedFields() {
        return new String[] { "parentLogRecord" };
    }

    @Override
    public String getJUnitMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append(LINE_SEPARATOR);
        sb.append(DateUtil.getDateTimeFormatted(getStartTime()));
        sb.append(" - ");
        if (!getType().isEmpty()) {
            // for the new generated report
            sb.append("[" + getType() + "]");
        }
        sb.append("[" + getStatus().getStatusValue().name() + "]");
        sb.append(" - ");
        if (!getName().isEmpty()) {
            sb.append(getName() + ": ");
        }
        sb.append(getMessage());
        sb.append(LINE_SEPARATOR);
        return sb.toString();
    }

    /**
     * Get System Out message for JUnit report
     * 
     * @return system-out message
     */
    @Override
    public String getSystemOutMsg() {
        StringBuilder sb = new StringBuilder();
        sb.append(getJUnitMessage());

        if (hasChildRecords()) {
            for (ILogRecord item : getChildRecords()) {
                sb.append(item.getSystemOutMsg());
            }
        }
        return sb.toString();
    }

    /**
     * Get System Error message for JUnit report
     * 
     * @return system-err message
     */
    @Override
    public String getSystemErrorMsg() {
        StringBuilder sb = new StringBuilder();
        if (getStatus().getStatusValue().isError()) {
            sb.append(getJUnitMessage());
        }
        sb.append(LINE_SEPARATOR);
        sb.append(getStatus().getStackTrace());

        if (hasChildRecords()) {
            for (ILogRecord item : getChildRecords()) {
                if (item.getStatus().getStatusValue().isError()) {
                    sb.append(item.getStatus().getStackTrace());
                    sb.append(LINE_SEPARATOR);
                }
            }
        }
        sb.append(LINE_SEPARATOR);
        return sb.toString();
    }

    protected ILogRecord getParentLogRecordByType(ILogRecord parent, String type) {
        if (parent.getType().equals(type)) {
            return parent;
        }
        return getParentLogRecordByType(parent.getParentLogRecord(), type);
    }

}
