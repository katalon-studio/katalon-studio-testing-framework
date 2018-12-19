package com.kms.katalon.core.context.internal;

import com.kms.katalon.core.context.TestSuiteContext;

public class InternalTestSuiteContext implements TestSuiteContext {

    private String testSuiteId;
    
    private String status;

    public String getTestSuiteId() {
        return testSuiteId;
    }

    public void setTestSuiteId(String testSuiteId) {
        this.testSuiteId = testSuiteId;
    }

    @Override
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
