package com.kms.katalon.core.testng.keyword;

import org.testng.TestListenerAdapter;
import org.testng.TestNG;

public interface TestNGRunnerResult {

    TestNG getTestNG();
    
    TestListenerAdapter getResultListener();
    
    String getReportLocation();
}
