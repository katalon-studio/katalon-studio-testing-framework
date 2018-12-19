package com.kms.katalon.core.context;

import java.util.Map;

import com.kms.katalon.core.annotation.AfterTestCase;
import com.kms.katalon.core.annotation.AfterTestSuite;
import com.kms.katalon.core.annotation.BeforeTestCase;
import com.kms.katalon.core.annotation.BeforeTestSuite;
import com.kms.katalon.core.annotation.SetUp;
import com.kms.katalon.core.annotation.TearDown;

/**
 * Provides some related information of the current executed test case.
 * </br>
 * System will automatically inject an instance of {@link TestCaseContext} as a parameter in {@link BeforeTestCase} methods,
 * {@link AfterTestCase} methods.
 * </br>
 * </br>
 * Test listener execution flow:
 * 
 * <pre>
 * Invoke all {@link BeforeTestSuite} methods
 * Invoke all Test Suite's {@link SetUp} methods
 *      
 *      Each Test Case
 *          Invoke all {@link BeforeTestCase} methods
 *          Invoke all Test Case's {@link SetUp} methods
 *          
 *          Execute Test Case's Script
 *                  
 *          Invoke all Test Case's {@link TearDown} methods
 *          Invoke all {@link AfterTestCase} methods
 * 
 * Invoke all Test Suite's {@link TearDown} methods
 * Invoke all {@link AfterTestSuite} methods
 * </pre>
 * For more details, please check our document page via 
 * <a href="https://docs.katalon.com/pages/viewpage.action?pageId=5126383">https://docs.katalon.com/pages/viewpage.action?pageId=5126383</a>
 * 
 * 
 * @see BeforeTestCase
 * @see AfterTestCase
 * @since 5.1
 */
public interface TestCaseContext {
    /**
     * @return Id of the current executed test case
     */
    String getTestCaseId();

    /**
     * @return A map stores variables (key is variable's name, value is variable's value) that were used in the current
     * test case.
     */
    Map<String, Object> getTestCaseVariables();

    /**
     * Returns test status after the test case execution completes.
     * 
     * @return It should be <code>PASSED</code>, <code>FAILED</code>, or <code>ERROR</code>
     */
    String getTestCaseStatus();
    
    /**
     * Returns error message if test case fails
     * 
     * @return Stacktrace if test case fails, empty string otherwise
     */
    String getMessage();
    
    /**
     * Set flag to signal this test case was skipped, implementing class determines
     * further logic if necessary 
     */
    void skipThisTestCase();
    
    /**
     * @return true if skipThisTestCase() was called
     */
    boolean isSkipped();
    
}
