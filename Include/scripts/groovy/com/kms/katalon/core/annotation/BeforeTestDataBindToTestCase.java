package com.kms.katalon.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.kms.katalon.core.context.TestCaseContext;

/**
 * Marks method that will be invoked before a test case in a test suite is executed.
 * </br>
 * </br>
 * In {@link BeforeTestDataBindToTestCase} method, clients can get some related information for the current executed
 * test suite and test case through {@link TestCaseContext} and {@link TestSuiteContext} parameters.
 * A third parameter is a mapping from String to Object that represents the mapping between this test case variables to
 * corresponding test data columns
 * </br>
 * </br>
 * Test listener execution flow:
 * 
 * <pre>
 * Invoke all {@link BeforeTestSuite} methods
 * Invoke all Test Suite's {@link SetUp} methods
 *      
 *      Each Test Case
 *          If Test Case is in Test Suite
 *              Invoke all {@link BeforeTestDataBindToTestCase} methods
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
 * 
 * For more details, please check our document page via
 * <a href="https://docs.katalon.com/pages/viewpage.action?pageId=5126383">https://docs.katalon.com/pages/viewpage.
 * action?pageId=5126383</a>
 * 
 * @since 6.2.0
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface BeforeTestDataBindToTestCase {

}
