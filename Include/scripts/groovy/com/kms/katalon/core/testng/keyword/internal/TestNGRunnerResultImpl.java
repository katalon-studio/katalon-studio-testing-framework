package com.kms.katalon.core.testng.keyword.internal;

import org.testng.TestListenerAdapter;
import org.testng.TestNG;

import com.kms.katalon.core.testng.keyword.TestNGRunnerResult;

public class TestNGRunnerResultImpl implements TestNGRunnerResult {

    private final TestNG testNG;

    private final TestListenerAdapter resultListener;

    private final String reportLocation;

    public TestNGRunnerResultImpl(TestNG testNG, TestListenerAdapter resultListener, String reportLocation) {
        this.testNG = testNG;
        this.resultListener = resultListener;
        this.reportLocation = reportLocation;
    }

    @Override
    public TestNG getTestNG() {
        return testNG;
    }

    @Override
    public TestListenerAdapter getResultListener() {
        return resultListener;
    }

    @Override
    public String getReportLocation() {
        return reportLocation;
    }
}
