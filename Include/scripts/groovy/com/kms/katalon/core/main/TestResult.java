package com.kms.katalon.core.main;

import com.kms.katalon.core.logging.model.TestStatus;
import com.kms.katalon.core.logging.model.TestStatus.TestStatusValue;

public class TestResult {
    private TestStatus testStatus;
    
    private Object scriptResult;

    private String message;
    
    private Throwable cause;

    public TestStatus getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(TestStatus testStatus) {
        this.testStatus = testStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public Object getScriptResult() {
        return scriptResult;
    }

    public void setScriptResult(Object scriptResult) {
        this.scriptResult = scriptResult;
    }

    public static TestResult getDefault() {
        TestResult result = new TestResult();

        TestStatus testStatus = new TestStatus();
        testStatus.setStatusValue(TestStatusValue.PASSED);

        result.setTestStatus(testStatus);
        result.setMessage("");

        return result;
    }

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }
}
