package com.kms.katalon.core.context;

import com.kms.katalon.core.annotation.AfterTestCase;
import com.kms.katalon.core.annotation.AfterTestSuite;
import com.kms.katalon.core.annotation.BeforeTestCase;
import com.kms.katalon.core.annotation.BeforeTestSuite;
import com.kms.katalon.core.annotation.SetUp;
import com.kms.katalon.core.annotation.TearDown;

/**
 * Provides some related information of the current executed test suite.
 * </br>
 * System will automatically inject an instance of {@link TestSuiteContext} as a parameter in {@link BeforeTestSuite}
 * methods,
 * {@link AfterTestSuite} methods.
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
 * @see BeforeTestSuite
 * @see AfterTestSuite
 * @since 5.1
 */
public interface TestSuiteContext {
    /**
     * @since 5.1
     * @return Id of the current executed test suite
     */
    String getTestSuiteId();
    
    /**
     * @since 5.3
     * @return <ul>
     * <li>COMPLETE: All test cases completed normally.</li>
     * <li>ERROR: Some errors occurred. Eg: SetUp or TearDown methods failed.</li>
     * </ul>
     */
    String getStatus();
}
