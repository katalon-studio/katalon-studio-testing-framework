package com.kms.katalon.core.logging.model;

import java.util.List;

public class TestSuiteCollectionLogRecord {

    private String testSuiteCollectionId;

    private String reportLocation;
    
    private List<TestSuiteLogRecord> testSuiteRecords;
    
    private long startTime;
    
    private long endTime;

    private String totalPassedTestCases;

    private String totalErrorTestCases;

    private String totalFailedTestCases;
    
    private String totalTestCases;

    public String getTestSuiteCollectionId() {
        return testSuiteCollectionId;
    }

    public void setTestSuiteCollectionId(String testSuiteCollectionId) {
        this.testSuiteCollectionId = testSuiteCollectionId;
    }

    public String getReportLocation() {
        return reportLocation;
    }

    public void setReportLocation(String reportLocation) {
        this.reportLocation = reportLocation;
    }

    public List<TestSuiteLogRecord> getTestSuiteRecords() {
        return testSuiteRecords;
    }

    public void setTestSuiteRecords(List<TestSuiteLogRecord> testSuiteRecords) {
        this.testSuiteRecords = testSuiteRecords;
    }
    
    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    
    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getTotalPassedTestCases() {
        return totalPassedTestCases;
    }

    public void setTotalPassedTestCases(String totalPassedTestCases) {
        this.totalPassedTestCases = totalPassedTestCases;
    }

    public String getTotalErrorTestCases() {
        return totalErrorTestCases;
    }

    public void setTotalErrorTestCases(String totalErrorTestCases) {
        this.totalErrorTestCases = totalErrorTestCases;
    }

    public String getTotalFailedTestCases() {
        return totalFailedTestCases;
    }

    public void setTotalFailedTestCases(String totalFailedTestCases) {
        this.totalFailedTestCases = totalFailedTestCases;
    }
    
    public String getTotalTestCases() {
        return totalTestCases;
    }

    public void setTotalTestCases(String totalTestCases) {
        this.totalTestCases = totalTestCases;
    }
}
