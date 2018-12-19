package com.kms.katalon.core.logging.model;

public interface ILogRecord {
    public static final String LOG_TYPE_TEST_SUITE = "TEST_SUITE";

    public static final String LOG_TYPE_TEST_CASE = "TEST_CASE";

    public static final String LOG_TYPE_TEST_STEP = "TEST_STEP";

    public static final String LOG_TYPE_MESSAGE = "MESSAGE";

    public String getName();

    public void setName(String name);

    public String getId();

    public void setId(String id);

    public long getStartTime();

    public void setStartTime(long startTime);

    public long getEndTime();

    public void setEndTime(long endTime);

    public String getSource();

    public void setSource(String source);

    public TestStatus getStatus();

    public String getDescription();

    public void setDescription(String description);

    public boolean hasChildRecords();

    public ILogRecord[] getChildRecords();

    public void addChildRecord(ILogRecord childRecord);

    public void removeChildRecord(ILogRecord childRecord);

    public String getMessage();

    public void setMessage(String message);

    public ILogRecord getParentLogRecord();

    public void setParentLogRecord(ILogRecord parentLogRecord);

    public boolean isInterrupted();

    public void setInterrupted(boolean interrupted);

    public String getType();

    public String getJUnitMessage();

    public String getSystemOutMsg();

    public String getSystemErrorMsg();
}
