package com.kms.katalon.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.kms.katalon.core.context.TestSuiteContext;

/**
 * Marks method that will be invoked after every test suite launches.
 * </br>
 * </br>
 * In {@link AfterTestSuite} method, clients can get some related information for the current executed test suite by
 * declaring a {@link TestSuiteContext} parameter. 
 * </br>
 * </br>
 * Test listener execution flow:
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
 * 
 * For more details, please check our document page via this
 * <a href="https://docs.katalon.com/pages/viewpage.action?pageId=5126383">link</a>
 * 
 * @since 5.1
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface AfterTestSuite {

}
