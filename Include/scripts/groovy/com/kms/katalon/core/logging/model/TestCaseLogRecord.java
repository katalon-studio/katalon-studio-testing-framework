package com.kms.katalon.core.logging.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import com.kms.katalon.core.logging.model.TestStatus.TestStatusValue;

public class TestCaseLogRecord extends AbstractLogRecord {
    private boolean isOptional;

    private String tag;

    public TestCaseLogRecord(String name) {
        super(name);
        setType(ILogRecord.LOG_TYPE_TEST_CASE);
    }

    /**
     * Returns if the result of current test case is optional or not.
     * <p>
     * Used when the current test case is called by another test case.
     * 
     * @return true if result of this is optional. Otherwise, false.
     */
    public boolean isOptional() {
        return isOptional;
    }

    public void setOptional(boolean isOptional) {
        this.isOptional = isOptional;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public ILogRecord[] getChildRecords() {
        List<ILogRecord> resultRecords = new ArrayList<ILogRecord>();
        for (ILogRecord logRecord : childRecords) {
            if (logRecord instanceof TestStepLogRecord) {
                logRecord.setDescription(StringEscapeUtils.unescapeJava(logRecord.getDescription()));
                resultRecords.add(logRecord);
            }
        }
        return resultRecords.toArray(new ILogRecord[resultRecords.size()]);
    }
}
