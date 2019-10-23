package com.kms.katalon.core.main;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.runtime.InvokerHelper;

import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.constants.StringConstants;
import com.kms.katalon.core.context.internal.ExecutionEventManager;
import com.kms.katalon.core.context.internal.ExecutionListenerEvent;
import com.kms.katalon.core.context.internal.InternalTestCaseContext;
import com.kms.katalon.core.context.internal.InternalTestSuiteContext;
import com.kms.katalon.core.exception.KatalonRuntimeException;
import com.kms.katalon.core.logging.ErrorCollector;
import com.kms.katalon.core.model.FailureHandling;
import com.kms.katalon.core.testcase.TestCaseBinding;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.util.Node;
import groovy.util.NodeList;
import groovy.util.XmlParser;

public class TestCaseMain {

    private static final int DELAY_TIME = 50;

    private static ScriptEngine engine;

    private static ExecutionEventManager eventManager;

    /**
     * Setup test case or test suite before executing.
     *
     * CustomKeywords now has many custom keyword static methods, each one is
     * named with format [packageName].[className].[keywordName] but Groovy compiler
     * itself cannot invoke that formatted name. Therefore, we must change the
     * meta class of CustomKeywords to another one.
     * 
     * @throws IOException
     */
    public static void beforeStart() throws IOException {
        LogbackConfigurator.init();

        GroovyClassLoader classLoader = new GroovyClassLoader(TestCaseMain.class.getClassLoader());
        engine = ScriptEngine.getDefault(classLoader);

        // Load GlobalVariable class
//        loadGlobalVariableClass(classLoader);
        loadInternalGlobalVariableClass(classLoader);
        loadCustomKeywordsClass(classLoader);

        eventManager = ExecutionEventManager.getInstance();
    }

    private static void loadCustomKeywordsClass(GroovyClassLoader cl) {
        // Load CustomKeywords class
        Class<?> clazz = cl.parseClass("class CustomKeywords { }");
        InvokerHelper.metaRegistry.setMetaClass(clazz, new CustomKeywordDelegatingMetaClass(clazz, cl));
    }

    private static void loadGlobalVariableClass(GroovyClassLoader cl) {
        try {
            cl.loadClass(StringConstants.GLOBAL_VARIABLE_CLASS_NAME);
        } catch (ClassNotFoundException ex) {
            try {
                cl.parseClass(new File(RunConfiguration.getProjectDir(), StringConstants.GLOBAL_VARIABLE_FILE_NAME));
            } catch (CompilationFailedException | IOException ignored) {

            }
        }
    }

    private static void loadInternalGlobalVariableClass(GroovyClassLoader cl) {
        try {
            cl.loadClass(StringConstants.INTERNAL_GLOBAL_VARIABLE_CLASS_NAME);
        } catch (ClassNotFoundException ex) {
            try {
                cl.parseClass(
                        new File(RunConfiguration.getProjectDir(), StringConstants.INTERNAL_GLOBAL_VARIABLE_FILE_NAME));
            } catch (CompilationFailedException | IOException ignored) {

            }
        }
    }

    public static TestResult runTestCase(String testCaseId, TestCaseBinding testCaseBinding,
            FailureHandling flowControl) throws InterruptedException {
        return runTestCase(testCaseId, testCaseBinding, flowControl, true, true);
    }

    public static TestResult runTestCase(String testCaseId, TestCaseBinding testCaseBinding,
            FailureHandling flowControl, boolean doCleanUp) throws InterruptedException {
        return runTestCase(testCaseId, testCaseBinding, flowControl, true, doCleanUp);
    }

    public static TestResult runTestCase(String testCaseId, TestCaseBinding testCaseBinding,
            FailureHandling flowControl, boolean isMain, boolean doCleanUp) throws InterruptedException {
        Thread.sleep(DELAY_TIME);
        InternalTestCaseContext testCaseContext = new InternalTestCaseContext(testCaseId);
        testCaseContext.setMainTestCase(isMain);
        return new TestCaseExecutor(testCaseBinding, engine, eventManager, testCaseContext, doCleanUp)
                .execute(flowControl);
    }

    public static TestResult runWSVerificationScript(String verificationScript, FailureHandling flowControl,
            boolean doCleanUp) throws InterruptedException {
        Thread.sleep(DELAY_TIME);
        return new WSVerificationExecutor(verificationScript, engine, eventManager, doCleanUp).execute(flowControl);
    }

    public static TestResult runWSVerificationScript(TestCaseBinding testCaseBinding, String verificationScript,
            FailureHandling flowControl, boolean doCleanUp) throws InterruptedException {
        Thread.sleep(DELAY_TIME);
        return new WSVerificationExecutor(testCaseBinding, verificationScript, engine, eventManager, doCleanUp)
                .execute(flowControl);
    }

    public static TestResult runTestCaseRawScript(String testScript, String testCaseId, TestCaseBinding testCaseBinding,
            FailureHandling flowControl) throws InterruptedException {
        Thread.sleep(DELAY_TIME);
        return new RawTestScriptExecutor(testScript, testCaseBinding, engine, eventManager,
                new InternalTestCaseContext(testCaseId)).execute(flowControl);
    }

    public static TestResult runFeatureFile(String featureFile) throws InterruptedException {
        Thread.sleep(DELAY_TIME);
        String verificationScript = MessageFormat
                .format("import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW\n" +

                        "CucumberKW.runFeatureFile(''{0}'')", featureFile);
        return new WSVerificationExecutor(verificationScript, engine, eventManager, true)
                .execute(FailureHandling.STOP_ON_FAILURE);
    }

    public static TestResult runTestCaseRawScript(String testScript, String testCaseId, TestCaseBinding testCaseBinding,
            FailureHandling flowControl, boolean doCleanUp) throws InterruptedException {
        Thread.sleep(DELAY_TIME);
        return new RawTestScriptExecutor(testScript, testCaseBinding, engine, eventManager,
                new InternalTestCaseContext(testCaseId), doCleanUp).execute(flowControl);
    }

    public static void startTestSuite(String testSuiteId, Map<String, String> suiteProperties,
            File testCaseBindingFile) {
        TestSuiteExecutor testSuiteExecutor = new TestSuiteExecutor(testSuiteId, engine, eventManager);
        testSuiteExecutor.execute(suiteProperties, testCaseBindingFile);
    }

    public static void invokeStartSuite(String testSuiteId) {
        InternalTestSuiteContext testSuiteContext = new InternalTestSuiteContext();
        testSuiteContext.setTestSuiteId(testSuiteId);
        eventManager.publicEvent(ExecutionListenerEvent.BEFORE_TEST_SUITE, new Object[] { testSuiteContext });
    }

    public static void invokeEndSuite(String testSuiteId) {
        InternalTestSuiteContext testSuiteContext = new InternalTestSuiteContext();
        testSuiteContext.setTestSuiteId(testSuiteId);

        eventManager.publicEvent(ExecutionListenerEvent.AFTER_TEST_SUITE, new Object[] { testSuiteContext });
    }
    
    public static Map<String, Object> getGlobalVariables(String profileName) {
        try {
            Map<String, Object> selectedVariables = new HashMap<>();
            Node rootNode = new XmlParser()
                    .parse(new File(RunConfiguration.getProjectDir(), "Profiles/" + profileName + ".glbl"));
            NodeList variableNodes = (NodeList) rootNode.get("GlobalVariableEntity");
            for (int index = 0; index < variableNodes.size(); index++) {
                Node globalVariableNode = (Node) variableNodes.get(index);
                String variableName = ((Node) ((NodeList) globalVariableNode.get("name")).get(0)).text();
                String defaultValue = ((Node) ((NodeList) globalVariableNode.get("initValue")).get(0)).text();
                try {
                    selectedVariables.put(variableName, engine.runScriptWithoutLogging(defaultValue, new Binding()));
                } catch (Exception e) {
                    KatalonRuntimeException runtimeException = new KatalonRuntimeException(String.format(
                            "Could not evaluate default value for variable: %s of profile: %s. Details: %s",
                            variableName, profileName, e.getMessage()), e);
                    ErrorCollector.getCollector().addError(runtimeException);
                }
            }
            return selectedVariables;
        } catch (Exception ex) {
            KatalonRuntimeException runtimeException = new KatalonRuntimeException(
                    String.format("Could not evaluate variable of profile: %s. Details: %s", profileName, ex), ex);
            ErrorCollector.getCollector().addError(runtimeException);
            return Collections.emptyMap();
        }
    }
    
    public static void logGlobalVariableError(Exception e) {
        KatalonRuntimeException runtimeException = new KatalonRuntimeException(
                String.format("There was something wrong in GlobalVariable. Details: %s", e.getMessage()));
        ErrorCollector.getCollector().addError(runtimeException);
    }
    
    public static ScriptEngine getScriptEngine() {
        return engine;
    }
}
