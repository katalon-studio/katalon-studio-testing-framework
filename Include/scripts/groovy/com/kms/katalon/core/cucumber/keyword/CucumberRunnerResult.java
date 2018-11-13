package com.kms.katalon.core.cucumber.keyword;

import org.junit.runner.Result;

/**
 * @since 5.7
 * @see CucumberRunnerResult#getStatus()
 * @see CucumberRunnerResult#getReportLocation()
 * @see CucumberRunnerResult#getCucumberRunnerResult()
 */
public interface CucumberRunnerResult {

    /**
     * @return passed or failed
     * @since 5.7
     */
    String getStatus();

    /**
     * Optional: Used when the keyword is {@link CucumberBuiltinKeywords#runFeatureFile(String)}, or
     * {@link CucumberBuiltinKeywords#runFeatureFolder(String)}
     * @return absolute path of generated cucumber report
     * @since 5.7
     */
    String getReportLocation();

    /**
     * Optional: Used when the keyword is {@link CucumberBuiltinKeywords#runWithCucumberRunner(Class)}
     * 
     * @return an instance of JUnit Cucumber Result, null if the keyword is NOT
     * {@link CucumberBuiltinKeywords#runWithCucumberRunner(Class)}.
     * @since 5.7
     */
    Result getCucumberRunnerResult();
}
