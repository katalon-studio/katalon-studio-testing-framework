package com.kms.katalon.core.logging.model;

import java.util.Date;

public class TestStatus {

    public enum TestStatusValue {
        PASSED, FAILED, INCOMPLETE, ERROR, INFO, WARNING, NOT_RUN, SKIPPED;	// Suite & Test status

        public boolean isError() {
            return this == ERROR || this == FAILED || this == INCOMPLETE;
        }
    }

    // Error Java stack Trace
    protected String stackTrace = "";

    // Default is PASSED
    protected TestStatusValue statusValue = TestStatusValue.PASSED;

    protected Date startTime;

    protected Date endTime;

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public TestStatusValue getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(TestStatusValue statusValue) {
        this.statusValue = statusValue;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

}
