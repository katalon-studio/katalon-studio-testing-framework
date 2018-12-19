package com.kms.katalon.core.logging.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kms.katalon.core.logging.model.TestStatus.TestStatusValue;
import com.kms.katalon.core.model.FailureHandling;

public class TestStepLogRecord extends AbstractLogRecord {
    private List<String> arguments;

    private String attachment;

    private FailureHandling flowControl;

    private int index;

    private boolean isIgnoredIfFailed;

    public TestStepLogRecord(String name) {
        super(name);
        setType(ILogRecord.LOG_TYPE_TEST_STEP);
    }

    public List<String> getArguments() {
        if (arguments == null) {
            arguments = new ArrayList<String>();
        }
        return arguments;
    }

    public void setArguments(List<String> args) {
        this.arguments = args;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    @Override
    public TestStatus getStatus() {
        TestStatus testStatus = super.getStatus();

        if (childRecords == null || childRecords.size() == 0) {
            return testStatus;
        }

        if (childRecords.size() == 1 && childRecords.get(0).getStatus().getStatusValue() == TestStatusValue.NOT_RUN) {
            testStatus.setStatusValue(TestStatusValue.NOT_RUN);
        }

        setMessage(childRecords.get(childRecords.size() - 1).getMessage());

        for (ILogRecord logRecord : getChildRecords()) {
            String childAttachment = null;
            if (logRecord instanceof TestStepLogRecord) {
                childAttachment = ((TestStepLogRecord) logRecord).getAttachment();
            } else if (logRecord instanceof MessageLogRecord) {
                childAttachment = ((MessageLogRecord) logRecord).getAttachment();
            }

            if (!StringUtils.isBlank(childAttachment)) {
                setAttachment(childAttachment);
            }
        }
        if (isIgnoredIfFailed()) {
            testStatus.setStatusValue(TestStatusValue.PASSED);
        }
        return testStatus;
    }

    public FailureHandling getFlowControl() {
        return flowControl;
    }

    public void setFlowControl(FailureHandling flowControl) {
        this.flowControl = flowControl;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getIndexString() {
        int stepIndex = index;
        if (stepIndex == -1 && parentLogRecord != null) {
            return "";
            // for (int i = 0; i < parentLogRecord.getChildRecords().length; i++) {
            // if (parentLogRecord.getChildRecords()[i] == this) {
            // stepIndex = i + 1;
            // break;
            // }
            // }
        }
        if (parentLogRecord == null || !(parentLogRecord instanceof TestStepLogRecord)) {
            return String.valueOf(stepIndex);
        } else {
            String parentIndexString = ((TestStepLogRecord) parentLogRecord).getIndexString();
            String parentIndexStringWithSeparator = StringUtils.isNotEmpty(parentIndexString) ? parentIndexString + "."
                    : StringUtils.EMPTY ;
            return parentIndexStringWithSeparator + Integer.toString(stepIndex);
        }
    }

    public boolean isIgnoredIfFailed() {
        return isIgnoredIfFailed;
    }

    public void setIgnoredIfFailed(boolean isIgnoredIfFailed) {
        this.isIgnoredIfFailed = isIgnoredIfFailed;
    }

}
