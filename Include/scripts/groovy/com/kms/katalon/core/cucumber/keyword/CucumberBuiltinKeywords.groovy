package com.kms.katalon.core.cucumber.keyword;

import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils
import org.junit.runner.Computer;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure

import com.kms.katalon.core.annotation.Keyword;
import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.cucumber.keyword.internal.CucumberRunnerResultImpl
import com.kms.katalon.core.keyword.BuiltinKeywords;
import com.kms.katalon.core.keyword.internal.KeywordMain;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.model.FailureHandling;
import com.kms.katalon.core.model.RunningMode
import com.kms.katalon.core.util.internal.PathUtil

import cucumber.api.cli.Main;
import groovy.transform.CompileStatic

public class CucumberBuiltinKeywords extends BuiltinKeywords {

    private static final KeywordLogger logger = KeywordLogger.getInstance(CucumberBuiltinKeywords.class);

    /**
     * Runs the given Feature file with <code>featureId</code> by invoking
     * {@link cucumber.api.cli.Main#run(String[], ClassLoader)}.
     * </p>
     * The generated reports will be extracted in the current report folder with the following path: <code>&lt;report_folder&gt;/cucumber_report/&lt;current_time_stamp&gt;<code> 
     * 
     * @param relativeFilePath
     * relativeFilePath of Feature file
     * @param flowControl
     * an instance {@link FailureHandling} that controls the running flow
     * @return
     * an instance of {@link CucumberRunnerResult} that includes status of keyword and report folder location.
     * 
     * @since 5.7
     * @see CucumberRunnerResult
     */
    @Keyword
    public static CucumberRunnerResult runFeatureFile(String relativeFilePath, FailureHandling flowControl) {
        return KeywordMain.runKeyword({
            if (StringUtils.isEmpty(relativeFilePath)) {
                throw new IllegalArgumentException("featureRelativeFilePath param must not be null or empty")
            }
            String reportDir = RunConfiguration.getReportFolder() + "/cucumber_report/" + System.currentTimeMillis()
            String projectDir = RunConfiguration.getProjectDir()
            RunningMode runningMode = RunConfiguration.getRunningMode()

            logger.logInfo(
                MessageFormat.format("Starting run keyword runFeatureFile: ''{0}'' and extract report to folder: ''{1}''...",
                    relativeFilePath, reportDir))
            String[] argv = [
                "-g",
                "",
                projectDir + "/" + relativeFilePath,
                "--strict",
//                "--plugin",
//                "pretty",
                "--plugin",
                "html:" + reportDir + "/html",
                "--plugin",
                "json:" + reportDir + "/cucumber.json",
                "--plugin",
                "junit:"+ reportDir + "/cucumber.xml",
                "--plugin",
                CucumberReporter.class.getName()
            ]
            if (runningMode == RunningMode.CONSOLE) {
                argv = argv + ["--monochrome"]
            }
            boolean runSuccess = Main.run(argv, CucumberBuiltinKeywords.class.getClassLoader()) == 0;
            CucumberRunnerResultImpl cucumberResult = new CucumberRunnerResultImpl(
                runSuccess ? 'passed' : 'failed', reportDir)
            if (runSuccess) {
                logger.logPassed(MessageFormat.format("Feature file: ''{0}'' was passed", relativeFilePath));
            } else {
                KeywordMain.stepFailed(MessageFormat.format("Feature file ''{0}'' was failed", relativeFilePath), flowControl)
            }
            return cucumberResult;
        }, flowControl, "Keyword runFeatureFile was failed");
    }

    /**
     * Runs the given Feature file with <code>featureId</code> by invoking
     * {@link cucumber.api.cli.Main#run(String[], ClassLoader)}
     *
     * @param relativeFilePath
     * relativeFilePath of Feature file
     * @return
     * an instance of {@link CucumberRunnerResult} that includes status of keyword and report folder location.
     *
     * @since 5.7
     */
    @Keyword
    public static boolean runFeatureFile(String relativeFilePath) {
        return runFeatureFile(relativeFilePath, RunConfiguration.getDefaultFailureHandling());
    }

    /**
     * Runs the given Feature folder and its nested sub-folder with <code>folderRelativePath</code>
     * by invoking {@link cucumber.api.cli.Main#run(String[], ClassLoader)}.
     * </p>
     * The generated reports will be extracted in the current report folder with the following path: <code>&lt;report_folder&gt;/cucumber_report/&lt;current_time_stamp&gt;<code> 
     * 
     * @param folderRelativePath
     * folder relative path that starts from the current project location
     * @param flowControl
     * an instance {@link FailureHandling} that controls the running flow
     * @return
     * an instance of {@link CucumberRunnerResult} that includes status of keyword and report folder location.
     * @since 5.7
     */
    @Keyword
    public static boolean runFeatureFolder(String folderRelativePath, FailureHandling flowControl) {
        return KeywordMain.runKeyword({
            if (StringUtils.isEmpty(folderRelativePath)) {
                throw new IllegalArgumentException("folderRelativePath param must not be null")
            }
            String reportDir = RunConfiguration.getReportFolder() + "/cucumber_report/" + System.currentTimeMillis()
            String projectDir = RunConfiguration.getProjectDir()
            RunningMode runningMode = RunConfiguration.getRunningMode()
            logger.logInfo(
                MessageFormat.format("Starting run keyword runFeatureFolder: ''{0}'' and extract report to folder: ''{1}''...",
                    folderRelativePath, reportDir))
            String[] argv = [
                "-g",
                "",
                projectDir + "/" + folderRelativePath,
                "--strict",
                "--plugin",
                "pretty",
                "--plugin",
                "html:" + reportDir + "/html",
                "--plugin",
                "json:" + reportDir + "/cucumber.json",
                "--plugin",
                "junit:"+ reportDir + "/cucumber.xml"
            ]
            if (runningMode == RunningMode.CONSOLE) {
                argv = argv + ["--monochrome"]
            }

            boolean runSuccess = Main.run(argv, CucumberBuiltinKeywords.class.getClassLoader()) == 0;
            CucumberRunnerResultImpl cucumberResult = new CucumberRunnerResultImpl(
                runSuccess ? 'passed' : 'failed', reportDir)
            if (runSuccess) {
                logger.logPassed(MessageFormat.format("All feature files in ''{0}'' were passed", folderRelativePath));
            } else {
                KeywordMain.stepFailed(MessageFormat.format("Run feature folder ''{0}'' failed", folderRelativePath));
            }
            return cucumberResult;
        }, flowControl, "Keyword runFeatureFolder was failed");
    }

    /**
     * Runs the given Feature folder and its nested sub-folder with <code>folderRelativePath</code>
     * by invoking {@link cucumber.api.cli.Main#run(String[], ClassLoader)}
     *
     * @param folderRelativePath
     * folder relative path that starts from current project location
     * @return
     * an instance of {@link CucumberRunnerResult} that includes status of keyword and report folder location.
     * @since 5.7
     */
    @Keyword
    public static boolean runFeatureFolder(String folderRelativePath) {
        return runFeatureFolder(folderRelativePath, RunConfiguration.getDefaultFailureHandling())
    }

    /**
     * Runs the given <code>cucumberRunnerClass</code> that is annotated with {@link Cucumber} runner by invoke JUnit
     * runner.
     * 
     * @param cucumberRunnerClass
     * a class that is annotated with {@link Cucumber} runner.
     * <p>
     * Example of <code>cucumberRunnerClass</code>:
     * <ul>
     * <li>Example #1: Run all Feature files in <b>Include/features</b> Folder
     * 
     * <pre>
     * import org.junit.runner.RunWith;
     * import cucumber.api.CucumberOptions;
     * import cucumber.api.junit.Cucumber;
     * 
     * &#64;RunWith(Cucumber.class)
     * &#64;CucumberOptions(features = "Include/features", glue = "")
     * public class MyCucumberRunner {}
     * </pre>
     * 
     * </li>
     * 
     * <li>Example #2: Run all Feature files in a specified file/folder
     * 
     * <pre>
     * import org.junit.runner.RunWith;
     * import cucumber.api.CucumberOptions;
     * import cucumber.api.junit.Cucumber;
     * 
     * &#64;RunWith(Cucumber.class)
     * &#64;CucumberOptions(features = "Your_Folder_Or_File_Path", glue = "")
     * public class MyCucumberRunner {}
     * </pre>
     * 
     * </li>
     * 
     * </li>
     * <li>Example #3: Run all Feature files in a specified file/folder, generate JUnit Cucumber report with XML pretty
     * format,
     * and copy to a specified folder
     * 
     * <pre>
     * import org.junit.runner.RunWith;
     * import cucumber.api.CucumberOptions;
     * import cucumber.api.junit.Cucumber;
     * &#64;RunWith(Cucumber.class)
     * &#64;CucumberOptions(features="Your_Folder_Path", glue="", plugin = ["pretty",
     *                      "junit:Folder_Name/cucumber.xml"])
     * public class MyCucumberRunner {
     * }
     * </pre>
     * 
     * </li>
     * 
     * </li>
     * <li>Example #4: Run all Feature files in a specified file/folder, generate multi Cucumber reports with XML, JSON,
     * HTML pretty format,
     * and copy to a specified folder
     * 
     * <pre>
     * import org.junit.runner.RunWith;
     * import cucumber.api.CucumberOptions;
     * import cucumber.api.junit.Cucumber;
     * &#64;RunWith(Cucumber.class)
     * &#64;CucumberOptions(features="Your_Folder_Path", glue="", plugin = ["pretty",
     *                      "junit:Folder_Name/cucumber.xml",
     *                      "html:Folder_Name",
     *                      "json:Folder_Name/cucumber.json"])
     * public class MyCucumberRunner {
     * }
     * </pre>
     * 
     * </li>
     * @param flowControl
     * an instance {@link FailureHandling} that controls the running flow
     * @return
     * an instance of {@link CucumberRunnerResult} that includes status of keyword and JUnit Runner result.
     * @since 5.7
     */
    @Keyword
    public static CucumberRunnerResult runWithCucumberRunner(Class cucumberRunnerClass, FailureHandling flowControl) {
        return KeywordMain.runKeyword({
            JUnitCore core = new JUnitCore();
            Computer computer = new Computer();
            Result result = core.run(computer, cucumberRunnerClass);
            boolean runSuccess = result.wasSuccessful();
            CucumberRunnerResultImpl cucumberResult = new CucumberRunnerResultImpl(
                runSuccess ? 'passed' : 'failed', '', result)
            if (runSuccess) {
                logger.logPassed(MessageFormat.format("Run with ''{0}'' was passed", cucumberRunnerClass.getName()));
            } else {
                List failuresDescriptions = []
                for (Failure failure : result.getFailures()) {
                    failuresDescriptions.add(failure.getMessage())
                }
                KeywordMain.stepFailed(
                    MessageFormat.format("These following reason:\n {0}", failuresDescriptions));
            }
            return cucumberResult;
        }, flowControl, "Keyword runWithCucumberRunner was failed");
    }

    /**
     * Runs the given <code>cucumberRunnerClass</code> that is annotated with {@link Cucumber} runner by invoke JUnit
     * runner.
     * 
     * @param cucumberRunnerClass
     * a class that is annotated with {@link Cucumber} runner.
     * <p>
     * Example of <code>cucumberRunnerClass</code>:
     * <ul>
     * <li>Example #1: Run all Feature files in <b>Include/features</b> Folder
     * 
     * <pre>
     * import org.junit.runner.RunWith;
     * import cucumber.api.CucumberOptions;
     * import cucumber.api.junit.Cucumber;
     * 
     * &#64;RunWith(Cucumber.class)
     * &#64;CucumberOptions(features = "Include/features", glue = "")
     * public class MyCucumberRunner {}
     * </pre>
     * 
     * </li>
     * 
     * <li>Example #2: Run all Feature files in a specified file/folder
     * 
     * <pre>
     * import org.junit.runner.RunWith;
     * import cucumber.api.CucumberOptions;
     * import cucumber.api.junit.Cucumber;
     * 
     * &#64;RunWith(Cucumber.class)
     * &#64;CucumberOptions(features = "Your_Folder_Or_File_Path", glue = "")
     * public class MyCucumberRunner {}
     * </pre>
     * 
     * </li>
     * 
     * </li>
     * <li>Example #3: Run all Feature files in a specified file/folder, generate JUnit Cucumber report with XML pretty
     * format,
     * and copy to a specified folder
     * 
     * <pre>
     * import org.junit.runner.RunWith;
     * import cucumber.api.CucumberOptions;
     * import cucumber.api.junit.Cucumber;
     * &#64;RunWith(Cucumber.class)
     * &#64;CucumberOptions(features="Your_Folder_Path", glue="", plugin = ["pretty",
     *                      "junit:Folder_Name/cucumber.xml"])
     * public class MyCucumberRunner {
     * }
     * </pre>
     * 
     * </li>
     * 
     * </li>
     * <li>Example #4: Run all Feature files in a specified file/folder, generate multi Cucumber reports with XML, JSON,
     * HTML pretty format,
     * and copy to a specified folder
     * 
     * <pre>
     * import org.junit.runner.RunWith;
     * import cucumber.api.CucumberOptions;
     * import cucumber.api.junit.Cucumber;
     * &#64;RunWith(Cucumber.class)
     * &#64;CucumberOptions(features="Your_Folder_Path", glue="", plugin = ["pretty",
     *                      "junit:Folder_Name/cucumber.xml",
     *                      "html:Folder_Name",
     *                      "json:Folder_Name/cucumber.json"])
     * public class MyCucumberRunner {
     * }
     * </pre>
     * 
     * </li>
     * an instance of {@link CucumberRunnerResult} that includes status of keyword and JUnit Runner result.
     * @since 5.7
     */
    @Keyword
    public static CucumberRunnerResult runWithCucumberRunner(Class cucumberRunnerClass) {
        return runWithCucumberRunner(cucumberRunnerClass, RunConfiguration.getDefaultFailureHandling());
    }
}
