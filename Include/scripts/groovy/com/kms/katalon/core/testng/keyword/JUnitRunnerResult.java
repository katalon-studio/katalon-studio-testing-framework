package com.kms.katalon.core.testng.keyword;

import org.junit.runner.Result;

public interface JUnitRunnerResult {
    /**
     * @return passed or failed
     * @since 7.3.1
     */
    String getStatus();

    Result getJUnitResult();
}
