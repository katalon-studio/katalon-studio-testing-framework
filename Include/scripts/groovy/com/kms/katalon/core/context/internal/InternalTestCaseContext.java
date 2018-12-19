package com.kms.katalon.core.context.internal;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.kms.katalon.core.context.TestCaseContext;

public class InternalTestCaseContext implements TestCaseContext {
    private boolean isMainTestCase = true;

    private String testCaseStatus;

    private String testCaseId;

    private Map<String, Object> testCaseVariables;
    
    private String message;

    private int testCaseIndex;

    private boolean isSkipped = false; // by default always run a test case
    
    public InternalTestCaseContext(String testCaseId, int testCaseIndex) {
        this.testCaseId = testCaseId;
        this.testCaseIndex = testCaseIndex;
        this.message = StringUtils.EMPTY;
    }

    public InternalTestCaseContext(String testCaseId) {
        this(testCaseId, 0);
    }

    @Override
    public String getTestCaseStatus() {
        return testCaseStatus;
    }

    public void setTestCaseStatus(String testCaseStatus) {
        this.testCaseStatus = testCaseStatus;
    }

    @Override
    public String getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(String testCaseId) {
        this.testCaseId = testCaseId;
    }

    @Override
    public Map<String, Object> getTestCaseVariables() {
        return testCaseVariables;
    }

    public void setTestCaseVariables(Map<String, Object> testCaseVariables) {
        this.testCaseVariables = testCaseVariables;
    }

    public int getTestCaseIndex() {
        return testCaseIndex;
    }

    public void setTestCaseIndex(int testCaseIndex) {
        this.testCaseIndex = testCaseIndex;
    }

    public boolean isMainTestCase() {
        return isMainTestCase;
    }

    public void setMainTestCase(boolean isMainTestCase) {
        this.isMainTestCase = isMainTestCase;
    }

	public void setMessage(String message) {
		this.message = message;		
	}
	
	@Override
	public String getMessage(){
		return message;
	}

	@Override
	public void skipThisTestCase() {
		isSkipped = true;
	}
	
	@Override
	public boolean isSkipped(){
		return isSkipped;
	}
}
