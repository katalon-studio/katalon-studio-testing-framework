package com.kms.katalon.core.testcase;

import java.util.Map;

public class TestCaseBinding {
    private String testCaseName;

    private String testCaseId;

    private Map<String, Object> bindedValues;

    public TestCaseBinding(String testCaseName, String testCaseId, Map<String, Object> bindedValues) {
        this.testCaseName = testCaseName;
        this.testCaseId = testCaseId;
        this.bindedValues = bindedValues;
    }

    public TestCaseBinding(String testCaseId, Map<String, Object> bindedValues) {
        this(testCaseId, testCaseId, bindedValues);
    }

    public Map<String, Object> getBindedValues() {
        return bindedValues;
    }

    public void setBindedValues(Map<String, Object> bindedValues) {
        this.bindedValues = bindedValues;
    }

    public String getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(String testCaseId) {
        this.testCaseId = testCaseId;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName;
    }
}
