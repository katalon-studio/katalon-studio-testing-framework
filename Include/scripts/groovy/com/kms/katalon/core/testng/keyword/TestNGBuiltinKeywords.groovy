package com.kms.katalon.core.testng.keyword

import java.text.MessageFormat

import org.junit.runner.JUnitCore
import org.junit.runner.Result
import org.testng.TestListenerAdapter
import org.testng.TestNG

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.keyword.BuiltinKeywords
import com.kms.katalon.core.keyword.internal.KeywordMain
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testng.keyword.internal.JUnitRunnerResultImpl
import com.kms.katalon.core.testng.keyword.internal.TestNGRunnerResultImpl

import groovy.transform.CompileStatic

@CompileStatic
public class TestNGBuiltinKeywords extends BuiltinKeywords {
    
    private static final KeywordLogger logger = KeywordLogger.getInstance(TestNGBuiltinKeywords.class);

    /**
     * Runs the given list of test suites as TestNG .xml files by invoking {@link TestNG#run()}.
     * </p>
     * The generated reports will be extracted in the current report folder with the following path: <code>&lt;report_folder&gt;/testng_report/&lt;current_time_stamp&gt;<code>.
     * 
     * @param testSuites
     * a list of TestNG .xml test suite files.
     * @param flowControl
     * an instance {@link FailureHandling} that controls the running flow
     * @return
     * an instance of {@link TestNGRunnerResult} that includes the TestNG instance, the TestNG result listener and the report folder location.
     * @since 7.3.1
     */
    @Keyword
    public static TestNGRunnerResult runTestNGTestSuites(List testSuites, FailureHandling flowControl) {
        return (TestNGRunnerResult) KeywordMain.runKeyword({
            TestNG runner = new TestNG()
            runner.setTestSuites(testSuites)
            TestListenerAdapter resultListener = new TestListenerAdapter()
            runner.addListener(resultListener);
            String testNGReportDir = RunConfiguration.getReportFolder() + "/testng_report/" + System.currentTimeMillis()
            runner.setOutputDirectory(testNGReportDir);
            
            logger.logInfo(
                    MessageFormat.format("Starting runTestNGTestSuites keyword and extract report to folder: ''{0}''...", testNGReportDir))
            runner.run()

            if (runner.getStatus() != 0) {
                KeywordMain.stepFailed(
                    MessageFormat.format("Running TestNG test suites: ''{0}'' failed", testSuites), flowControl)
            } else {
                logger.logPassed(MessageFormat.format("Running TestNG test suites: ''{0}'' passed", testSuites))
            }

            return new TestNGRunnerResultImpl(runner, resultListener, testNGReportDir)
        }, flowControl, "Keyword runTestNGTestSuites was failed")
    }

    /**
     * Runs the given list of test suites as TestNG .xml files by invoking {@link TestNG#run()}.
     * </p>
     * The generated reports will be extracted in the current report folder with the following path: <code>&lt;report_folder&gt;/testng_report/&lt;current_time_stamp&gt;<code>.
     *
     * @param testSuites
     * a list of TestNG .xml test suite files.
     * @return
     * an instance of {@link TestNGRunnerResult} that includes the TestNG instance, the TestNG result listener and the report folder location.
     * @since 7.3.1
     */
    @Keyword
    public static TestNGRunnerResult runTestNGTestSuites(List testSuites) {
        return runTestNGTestSuites(testSuites, RunConfiguration.getDefaultFailureHandling())
    }
    
    /**
     * Runs the given list of TestNG test classes by invoking {@link TestNG#run()}.
     * </p>
     * The generated reports will be extracted in the current report folder with the following path: <code>&lt;report_folder&gt;/testng_report/&lt;current_time_stamp&gt;<code>.
     *
     * @param testClasses
     * a list of TestNG test classes.
     * @param flowControl
     * an instance {@link FailureHandling} that controls the running flow
     * @return
     * an instance of {@link TestNGRunnerResult} that includes the TestNG instance, the TestNG result listener and the report folder location.
     * @since 7.3.1
     */
    @Keyword
    public static TestNGRunnerResult runTestNGTestClasses(List testClasses, FailureHandling flowControl) {
        return (TestNGRunnerResult) KeywordMain.runKeyword({
            TestNG runner = new TestNG()
            TestListenerAdapter resultListener = new TestListenerAdapter()
            runner.addListener(resultListener)
            runner.setTestClasses(testClasses as Class[])

            String testNGReportDir = RunConfiguration.getReportFolder() + "/testng_report/" + System.currentTimeMillis()
            runner.setOutputDirectory(testNGReportDir);
            
            logger.logInfo(
                    MessageFormat.format("Starting runTestNGTestClasses keyword and extract report to folder: ''{0}''...", testNGReportDir))
            runner.run()
            
           if (runner.getStatus() != 0) {
                KeywordMain.stepFailed(
                    MessageFormat.format("Running TestNG test classes: ''{0}'' failed", testClasses), flowControl)
            } else {
                logger.logPassed(MessageFormat.format("Running TestNG test classes: ''{0}'' passed", testClasses));
            }
            return new TestNGRunnerResultImpl(runner, resultListener, testNGReportDir)
        }, flowControl, "Keyword runTestNGTestClasses was failed");
    }

    /**
     * Runs the given list of TestNG test classes by invoking {@link TestNG#run()}.
     * </p>
     * The generated reports will be extracted in the current report folder with the following path: <code>&lt;report_folder&gt;/testng_report/&lt;current_time_stamp&gt;<code>.
     *
     * @param testClasses
     * a list of TestNG test classes
     * @return
     * an instance of {@link TestNGRunnerResult} that includes the TestNG instance, the TestNG result listener and the report folder location.
     * @since 7.3.1
     */
    @Keyword
    public static void runTestNGTestClasses(List testClasses) {
        runTestNGTestClasses(testClasses, RunConfiguration.getDefaultFailureHandling())
    }

    /**
     * Runs the given list of JUnit test classes by invoking {@link JUnitCore#runClasses(Class[])}.
     *
     * @param testClasses
     * a list of JUnit test classes
     * @param flowControl
     * an instance {@link FailureHandling} that controls the running flow
     * @return
     * an instance of {@link JUnitRunnerResult} that includes status of keyword and the JUnit Result.
     * @since 7.3.1
     */
    @Keyword
    public static JUnitRunnerResult runJUnitTestClasses(List testClasses, FailureHandling flowControl) {
        return (JUnitRunnerResult) KeywordMain.runKeyword({
            Result result = JUnitCore.runClasses(testClasses as Class[])
            
            boolean runSuccess = result.wasSuccessful();
            JUnitRunnerResultImpl junitRunnerResult = new JUnitRunnerResultImpl(runSuccess ? 'passed' : 'failed', result)
            if (runSuccess) {
                KeywordMain.stepFailed(
                    MessageFormat.format("Running TestNG test classes: ''{0}'' failed", testClasses), flowControl)
            } else {
                logger.logPassed(MessageFormat.format("Running JUnit test classes: ''{0}'' passed", testClasses));
            }
            return junitRunnerResult
        }, flowControl, "Keyword runJUnitTestClasses was failed");
    }

    /**
     * Runs the given list of TestNG test classes by invoking {@link TestNG#run()}.
     * </p>
     * The generated reports will be extracted in the current report folder with the following path: <code>&lt;report_folder&gt;/testng_report/&lt;current_time_stamp&gt;<code>.
     *
     * @param testClasses
     * a list of TestNG test classes
     * @return
     * an instance of {@link TestNGRunnerResult} that includes the TestNG instance, the TestNG result listener and the report folder location.
     * @since 7.3.1
     */
    @Keyword
    public static JUnitRunnerResult runJUnitTestClasses(List testClasses) {
        return runJUnitTestClasses(testClasses, RunConfiguration.getDefaultFailureHandling())
    }
}
