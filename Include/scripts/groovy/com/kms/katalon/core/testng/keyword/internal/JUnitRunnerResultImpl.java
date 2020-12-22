package com.kms.katalon.core.testng.keyword.internal;

import org.junit.runner.Result;

import com.kms.katalon.core.testng.keyword.JUnitRunnerResult;

public class JUnitRunnerResultImpl implements JUnitRunnerResult {
    
    private final String status;
    
    private final Result junitResult;
    
    public JUnitRunnerResultImpl(String status, Result junitResult) {
        this.status = status;
        this.junitResult = junitResult;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public Result getJUnitResult() {
        return junitResult;
    }

}
