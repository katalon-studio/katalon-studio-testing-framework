package com.kms.katalon.core.main;

import static com.kms.katalon.core.constants.StringConstants.DF_CHARSET;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.groovy.ast.MethodNode;

import com.google.common.base.Optional;
import com.kms.katalon.core.annotation.SetUp;
import com.kms.katalon.core.annotation.SetupTestCase;
import com.kms.katalon.core.annotation.TearDown;
import com.kms.katalon.core.annotation.TearDownTestCase;
import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.constants.StringConstants;
import com.kms.katalon.core.context.internal.ExecutionEventManager;
import com.kms.katalon.core.context.internal.ExecutionListenerEvent;
import com.kms.katalon.core.context.internal.InternalTestCaseContext;
import com.kms.katalon.core.context.internal.InternalTestSuiteContext;
import com.kms.katalon.core.context.internal.VideoRecorderService;
import com.kms.katalon.core.driver.internal.DriverCleanerCollector;
import com.kms.katalon.core.execution.TestExecutionSocketServer;
import com.kms.katalon.core.execution.TestExecutionSocketServerEndpoint;
import com.kms.katalon.core.logging.ErrorCollector;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.logging.KeywordLogger.KeywordStackElement;
import com.kms.katalon.core.logging.model.TestStatus.TestStatusValue;
import com.kms.katalon.core.model.FailureHandling;
import com.kms.katalon.core.testcase.TestCaseBinding;
import com.kms.katalon.core.util.internal.JsonUtil;

import groovy.lang.Binding;

public class TestSuiteExecutor {

    private static final String SHOULD_STOP_IMMEDIATELY_KEY = "stopImmediately";

    private final KeywordLogger logger = KeywordLogger.getInstance(this.getClass());

    private final String testSuiteId;

    private final ScriptEngine scriptEngine;

    private InternalTestSuiteContext testSuiteContext;

    private ExecutionEventManager eventManger;

    private ScriptCache scriptCache;
    
    private static final int TEST_EXECUTION_WEBSOCKET_PORT = 12954;

    private static final Set<String> TEST_SUITE_ANNOTATION_METHODS;
    static {
        TEST_SUITE_ANNOTATION_METHODS = new HashSet<>();
        TEST_SUITE_ANNOTATION_METHODS.add(SetUp.class.getName());
        TEST_SUITE_ANNOTATION_METHODS.add(TearDown.class.getName());
        TEST_SUITE_ANNOTATION_METHODS.add(SetupTestCase.class.getName());
        TEST_SUITE_ANNOTATION_METHODS.add(TearDownTestCase.class.getName());
    }

    public TestSuiteExecutor(String testSuiteId, ScriptEngine scriptEngine, ExecutionEventManager eventManger) {
        this.testSuiteId = testSuiteId;
        this.scriptEngine = scriptEngine;

        this.eventManger = eventManger;
        this.testSuiteContext = new InternalTestSuiteContext();
        testSuiteContext.setTestSuiteId(testSuiteId);

        VideoRecorderService videoRecorderService = new VideoRecorderService(RunConfiguration.getReportFolder(),
                RunConfiguration.getRecorderSetting());
        eventManger.addListenerEventHandle(videoRecorderService);
    }

    public void execute(Map<String, String> suiteProperties, File testCaseBindingFile) {
        eventManger.publicEvent(ExecutionListenerEvent.BEFORE_TEST_EXECUTION, new Object[0]);

        logger.startSuite(testSuiteId, suiteProperties);

        eventManger.publicEvent(ExecutionListenerEvent.BEFORE_TEST_SUITE, new Object[] { testSuiteContext });
        
        openExecutionEndNotifyingClient();

        accessTestSuiteMainPhase(suiteProperties, testCaseBindingFile);

        String status = "COMPLETE";
        if (ErrorCollector.getCollector().containsErrors()) {
            status = "ERROR";
        }
        testSuiteContext.setStatus(status);

        eventManger.publicEvent(ExecutionListenerEvent.AFTER_TEST_SUITE, new Object[] { testSuiteContext });

        if (RunConfiguration.shouldTerminateDriverAfterTestSuite()) {
            DriverCleanerCollector.getInstance().cleanDrivers();
        }

        logger.endSuite(testSuiteId, Collections.emptyMap());

        eventManger.publicEvent(ExecutionListenerEvent.AFTER_TEST_EXECUTION, new Object[0]);
    }
    
    private static void openExecutionEndNotifyingClient() {
        TestExecutionSocketServer.getInstance().start(TestExecutionSocketServerEndpoint.class,
                TEST_EXECUTION_WEBSOCKET_PORT);
    }


    private void accessTestSuiteMainPhase(Map<String, String> suiteProperties, File testCaseBindingFile) {
        ErrorCollector errorCollector = ErrorCollector.getCollector();
        try {
            this.scriptCache = new ScriptCache(testSuiteId);
        } catch (IOException e) {
            errorCollector.addError(e);
            return;
        }
        invokeTestSuiteMethod(SetUp.class.getName(), StringConstants.LOG_SETUP_ACTION, false);
        if (errorCollector.containsErrors()) {
            return;
        }
        
        try {
            List<String> bindings = FileUtils.readLines(testCaseBindingFile, "UTF-8");
            for (int i = 0; i < bindings.size(); i++) {
                TestCaseBinding testCaseBinding = JsonUtil.fromJson(bindings.get(i), TestCaseBinding.class);
                Map<String, Object> values = testCaseBinding.getBindedValues() != null ? testCaseBinding.getBindedValues() : new HashMap<>();
                Map<String, Object> bindedValues = new HashMap<>();

                scriptEngine.changeConfigForCollectingVariable();
                for (Entry<String, Object> entry : values.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    try {
                        Object runScript = scriptEngine.runScriptWithoutLogging(value != null ? value.toString() : null, new Binding());
                        bindedValues.put(key, runScript);
                    } catch (Exception e) {
                        bindedValues.put(key, value);
                    }
                }
                testCaseBinding.setBindedValues(bindedValues);
                TestResult tcExecutedResult = accessTestCaseMainPhase(i, testCaseBinding);

                if (shouldStopImmediately(suiteProperties, tcExecutedResult)) {
                    break;
                }
            }
        } catch (IOException e) {
            errorCollector.addError(e);
        }

        invokeTestSuiteMethod(TearDown.class.getName(), StringConstants.LOG_TEAR_DOWN_ACTION, true);
    }

    /**
     * See TestSuiteExecutedEntity#getAttributes
     * 
     * Check if this execution fails, and Retry Immediately is enabled
     * 
     * @param suiteProperties
     * @param tcExecutedResult
     * @return A boolean indicating if suite execution should stop
     */
    private boolean shouldStopImmediately(Map<String, String> suiteProperties, TestResult tcExecutedResult) {
        if (TestStatusValue.ERROR.equals(tcExecutedResult.getTestStatus().getStatusValue())
                || TestStatusValue.FAILED.equals(tcExecutedResult.getTestStatus().getStatusValue())) {
            String stopImmediatelyWhenTestCaseFails = suiteProperties.get(SHOULD_STOP_IMMEDIATELY_KEY);
            if (!StringUtils.isEmpty(stopImmediatelyWhenTestCaseFails)) {
                return Boolean.valueOf(stopImmediatelyWhenTestCaseFails).booleanValue();
            }
        }
        return false;
    }

    private TestResult accessTestCaseMainPhase(int index, TestCaseBinding tcBinding) {
        ErrorCollector errorCollector = ErrorCollector.getCollector();
        List<Throwable> coppiedErrors = errorCollector.getCoppiedErrors();
        errorCollector.clearErrors();

        try {
            Map<String, Object> bindedValues = new HashMap<>(
                    Optional.fromNullable(tcBinding.getBindedValues()).or(new HashMap<String, Object>()));
            
            InternalTestCaseContext testCaseContext = new InternalTestCaseContext(tcBinding.getTestCaseId(), index);
            eventManger.publicEvent(ExecutionListenerEvent.BEFORE_TEST_DATA_BIND_INTO_TEST_CASE
                    , new Object[] { testSuiteContext, testCaseContext
                            , bindedValues });
            tcBinding.setBindedValues(bindedValues);
            
            TestCaseExecutor testCaseExecutor = new TestCaseExecutor(tcBinding, scriptEngine, eventManger,
                    testCaseContext);
            testCaseExecutor.setTestSuiteExecutor(this);
            return testCaseExecutor.execute(FailureHandling.STOP_ON_FAILURE);
        } finally {
            errorCollector.clearErrors();
            errorCollector.getErrors().addAll(coppiedErrors);
        }
    }

    public void invokeEachTestCaseMethod(String methodName, String actionType, boolean ignoredIfFailed) {
        invokeTestSuiteMethod(methodName, actionType, ignoredIfFailed);
    }

    private void invokeTestSuiteMethod(String annotatedMethodName, String actionType, boolean ignoredIfFailed) {
        if (!scriptCache.hasScriptContent()) {
            return;
        }

        try {
            List<MethodNode> annotatedMethods = scriptCache.getMethodNodes(annotatedMethodName);
            if (annotatedMethods.isEmpty()) {
                return;
            }

            scriptEngine.changeConfigForExecutingScript();

            annotatedMethods.forEach(methodNode -> {
                runMethod(methodNode.getName(), actionType, ignoredIfFailed);
            });
        } catch (IOException e) {
            if (!ignoredIfFailed) {
                ErrorCollector.getCollector().addError(e);
            }
        } catch (ClassNotFoundException ignored) {}
    }

    private void runMethod(String methodName, String actionType, boolean ignoredIfFailed) {
        Stack<KeywordStackElement> keywordStack = new Stack<>();

        Map<String, String> startKeywordAttributeMap = new HashMap<>();
        startKeywordAttributeMap.put(StringConstants.XML_LOG_IS_IGNORED_IF_FAILED, String.valueOf(ignoredIfFailed));
        logger.startKeyword(methodName, actionType, startKeywordAttributeMap, keywordStack);

        ErrorCollector errorCollector = ErrorCollector.getCollector();
        List<Throwable> oldErrors = errorCollector.getCoppiedErrors();

        try {
            errorCollector.clearErrors();
            scriptEngine.runScriptMethodAsRawText(scriptCache.scriptContent, scriptCache.className, methodName,
                    new Binding());
            endAllUnfinishedKeywords(keywordStack);
        } catch (Throwable e) {
            errorCollector.getErrors().add(e);
        }

        boolean isKeyword = true;
        if (errorCollector.containsErrors()) {
            endAllUnfinishedKeywords(keywordStack);
            Throwable firstError = errorCollector.getFirstError();
            String errorMessage = firstError.getMessage();
            if (ignoredIfFailed) {
                logger.logWarning(errorMessage, null, firstError, isKeyword);
            } else {
                oldErrors.add(errorCollector.getFirstError());
                logger.logError(errorMessage, null, firstError, isKeyword);
            }
        } else {
            logger.logPassed(MessageFormat.format(StringConstants.MAIN_LOG_PASSED_METHOD_COMPLETED, methodName),
                    Collections.emptyMap(), isKeyword);
        }

        errorCollector.clearErrors();
        errorCollector.getErrors().addAll(oldErrors);
        logger.endKeyword(methodName, Collections.emptyMap(), keywordStack);
    }

    private void endAllUnfinishedKeywords(Stack<KeywordStackElement> keywordStack) {
        while (!keywordStack.isEmpty()) {
            KeywordStackElement keywordStackElement = keywordStack.pop();
            logger.endKeyword(keywordStackElement.getKeywordName(), null, keywordStackElement.getNestedLevel());
        }
    }

    private class ScriptCache {
        private File scriptFile;

        private AnnotatedMethodCollector annotatedMethodCollector;

        private String className;

        private String scriptContent;

        private ScriptCache(String testSuiteId) throws IOException {
            this.scriptFile = getTestSuiteScriptFile(testSuiteId);
            if (this.scriptFile != null && this.scriptFile.exists()) {
                scriptContent = FileUtils.readFileToString(scriptFile, DF_CHARSET);
                className = scriptFile.toURI().toURL().toExternalForm();

                this.annotatedMethodCollector = new AnnotatedMethodCollector(TEST_SUITE_ANNOTATION_METHODS);
            }
        }

        public List<MethodNode> getMethodNodes(String annotatedMethodName) throws IOException {
            Map<String, List<MethodNode>> methodNodeCollection = annotatedMethodCollector.getMethodNodes(scriptContent);
            if (!methodNodeCollection.containsKey(annotatedMethodName)) {
                return Collections.emptyList();
            }
            return methodNodeCollection.get(annotatedMethodName);
        }

        public boolean hasScriptContent() {
            return scriptFile != null && scriptFile.exists();
        }

        private File getTestSuiteScriptFile(String testSuiteId) {
            return new File(RunConfiguration.getProjectDir(), testSuiteId + ".groovy");
        }
    }
}
