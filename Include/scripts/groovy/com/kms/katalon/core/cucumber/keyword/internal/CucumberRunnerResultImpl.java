package com.kms.katalon.core.cucumber.keyword.internal;

import org.junit.runner.Result;

import com.kms.katalon.core.cucumber.keyword.CucumberRunnerResult;

public class CucumberRunnerResultImpl implements CucumberRunnerResult {
    private String status;
    private String reportLocation;
    private Result result;

    public CucumberRunnerResultImpl(String status, String reportLocation) {
        this(status, reportLocation, null);
    }

    public CucumberRunnerResultImpl(String status, String reportLocation, Result result) {
        this.status = status;
        this.reportLocation = reportLocation;
        this.result = result;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getReportLocation() {
        return reportLocation;
    }

    @Override
    public Result getCucumberRunnerResult() {
        return result;
    }

    @Override
    public String toString() {
        return "CucumberRunnerResultImpl{"
                + "status: " + status
                + ", reportLocation: " + reportLocation
                + ", cucumberRunnerResult: " + (result != null ? result.toString() : "null")
                    + "}";
    }
}
