package com.kms.katalon.core.main;

import static com.kms.katalon.core.constants.StringConstants.DF_CHARSET;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.control.CompilationFailedException;

import com.kms.katalon.core.annotation.SetUp;
import com.kms.katalon.core.annotation.SetupTestCase;
import com.kms.katalon.core.annotation.TearDown;
import com.kms.katalon.core.annotation.TearDownIfError;
import com.kms.katalon.core.annotation.TearDownIfFailed;
import com.kms.katalon.core.annotation.TearDownIfPassed;
import com.kms.katalon.core.annotation.TearDownTestCase;
import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.constants.StringConstants;
import com.kms.katalon.core.context.internal.ExecutionEventManager;
import com.kms.katalon.core.context.internal.ExecutionListenerEvent;
import com.kms.katalon.core.context.internal.InternalTestCaseContext;
import com.kms.katalon.core.driver.internal.DriverCleanerCollector;
import com.kms.katalon.core.logging.ErrorCollector;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.logging.KeywordLogger.KeywordStackElement;
import com.kms.katalon.core.logging.LogLevel;
import com.kms.katalon.core.logging.model.TestStatus;
import com.kms.katalon.core.logging.model.TestStatus.TestStatusValue;
import com.kms.katalon.core.model.FailureHandling;
import com.kms.katalon.core.testcase.TestCase;
import com.kms.katalon.core.testcase.TestCaseBinding;
import com.kms.katalon.core.testcase.TestCaseFactory;
import com.kms.katalon.core.util.BrowserMobProxyManager;
import com.kms.katalon.core.util.internal.ExceptionsUtil;

import groovy.lang.Binding;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

public class TestCaseExecutor {
    
    private final KeywordLogger logger = KeywordLogger.getInstance(this.getClass());

    private static ErrorCollector errorCollector = ErrorCollector.getCollector();

    protected TestResult testCaseResult;

    private TestCase testCase;

    private Stack<KeywordStackElement> keywordStack;

    private TestCaseMethodNodeCollector methodNodeCollector;

    private List<Throwable> parentErrors;

    protected ScriptEngine engine;

    protected Binding variableBinding;

    private TestCaseBinding testCaseBinding;

    private ExecutionEventManager eventManager;

    private boolean doCleanUp;

    private InternalTestCaseContext testCaseContext;

    private TestSuiteExecutor testSuiteExecutor;

    public void setTestSuiteExecutor(TestSuiteExecutor testSuiteExecutor) {
        this.testSuiteExecutor = testSuiteExecutor;
    }

    public TestCaseExecutor(TestCaseBinding testCaseBinding, ScriptEngine engine, ExecutionEventManager eventManager,
            InternalTestCaseContext testCaseContext, boolean doCleanUp) {
        this.testCaseBinding = testCaseBinding;
        this.engine = engine;
        this.testCase = TestCaseFactory.findTestCase(testCaseBinding.getTestCaseId());
        this.doCleanUp = doCleanUp;
        this.eventManager = eventManager;

        this.testCaseContext = testCaseContext;
    }

    public TestCaseExecutor(TestCaseBinding testCaseBinding, ScriptEngine engine, ExecutionEventManager eventManager,
            InternalTestCaseContext testCaseContext) {
        this(testCaseBinding, engine, eventManager, testCaseContext, false);
    }

    private void preExecution() {
        testCaseResult = TestResult.getDefault();
        keywordStack = new Stack<KeywordLogger.KeywordStackElement>();
        parentErrors = errorCollector.getCoppiedErrors();
        errorCollector.clearErrors();
    }

    private void onExecutionComplete() {
        endAllUnfinishedKeywords(keywordStack);

        internallyRunMethods(methodNodeCollector.getMethodNodeWrapper(TearDownIfPassed.class));

        internallyRunMethods(methodNodeCollector.getMethodNodeWrapper(TearDown.class));
        logger.logPassed(testCase.getTestCaseId());
    }

    private void onExecutionError(Throwable t) {
        if (!keywordStack.isEmpty()) {
            endAllUnfinishedKeywords(keywordStack);
        }
        testCaseResult.setCause(t);
        testCaseResult.getTestStatus().setStatusValue(getResultByError(t));
        String stackTraceForThrowable;
        try {
            stackTraceForThrowable = ExceptionsUtil.getStackTraceForThrowable(t);
        } catch (Exception e) {
            stackTraceForThrowable = ExceptionsUtil.getStackTraceForThrowable(t);
        }
        String message = MessageFormat.format(
                StringConstants.MAIN_LOG_MSG_FAILED_BECAUSE_OF, 
                testCase.getTestCaseId(),
                stackTraceForThrowable);
        testCaseResult.setMessage(message);
        logError(t, message);

        runTearDownMethodByError(t);
    }

    private boolean processScriptPreparationPhase() {
        // Collect AST nodes for script of test case
        try {
            methodNodeCollector = new TestCaseMethodNodeCollector(testCase);
        } catch (IOException e) {
            onSetupError(e);
            return false;
        }
        try {
            variableBinding = collectTestCaseVariables();
        } catch (CompilationFailedException e) {
            onSetupError(e);
            return false;
        }
        return true;
    }

    private boolean processSetupPhase() {
        // Run setup method
        internallyRunMethods(methodNodeCollector.getMethodNodeWrapper(SetUp.class));
        boolean setupFailed = errorCollector.containsErrors();
        if (setupFailed) {
            internallyRunMethods(methodNodeCollector.getMethodNodeWrapper(TearDownIfError.class));
            internallyRunMethods(methodNodeCollector.getMethodNodeWrapper(TearDown.class));
            onSetupError(errorCollector.getFirstError());
        }
        return !setupFailed;
    }

    protected File getScriptFile() throws IOException {
        return new File(testCase.getGroovyScriptPath());
    }

    private void onSetupError(Throwable t) {
        String message = MessageFormat.format(StringConstants.MAIN_LOG_MSG_ERROR_BECAUSE_OF, testCase.getTestCaseId(),
                ExceptionsUtil.getMessageForThrowable(t));
        testCaseResult.setMessage(message);
        testCaseResult.getTestStatus().setStatusValue(TestStatusValue.ERROR);
        logger.logError(message, null, t);
    }

    private void postExecution() {
    	
		if (RunConfiguration.getProperty(RunConfiguration.SMART_XPATH_BUNDLE_ID) != null) {

			logger.logInfo(StringConstants.SMART_XPATH_REPORT_AVAILABLE_OPENING);
			logger.logInfo(StringConstants.SMART_XPATH_VISIT_BELOW_LINK);
			logger.logInfo(StringConstants.SMART_XPATH_DOCUMENT);
			logger.logInfo(StringConstants.SMART_XPATH_REPORT_AVAILABLE_ENDING);
			
		}

        errorCollector.clearErrors();
        errorCollector.getErrors().addAll(0, parentErrors);
        if (testCaseContext.isMainTestCase()) {
            BrowserMobProxyManager.shutdownProxy();
        }
    }

    @SuppressWarnings("unchecked")
    public TestResult execute(FailureHandling flowControl) {
        try {
            preExecution();

            if (testCaseContext.isMainTestCase()) {
                logger.startTest(testCase.getTestCaseId(), getTestCaseProperties(testCaseBinding, testCase, flowControl),
                        keywordStack);
            } else {
                logger.startCalledTest(testCase.getTestCaseId(), getTestCaseProperties(testCaseBinding, testCase, flowControl),
                        keywordStack);
            }

            if (!processScriptPreparationPhase()) {
                return testCaseResult;
            }

            testCaseContext.setTestCaseStatus(testCaseResult.getTestStatus().getStatusValue().name());

            testCaseContext.setTestCaseVariables(variableBinding.getVariables());

            if (testCaseContext.isMainTestCase()) {
                eventManager.publicEvent(ExecutionListenerEvent.BEFORE_TEST_CASE, new Object[] { testCaseContext });
            }
            
            // By this point, @BeforeTestCase annotated method has already been called
            if(testCaseContext.isSkipped() == false){
                testCaseResult = invokeTestSuiteMethod(SetupTestCase.class.getName(), StringConstants.LOG_SETUP_ACTION,
                        false, testCaseResult);
                if (ErrorCollector.getCollector().containsErrors()) {
                    Throwable error = ErrorCollector.getCollector().getFirstError();
                    testCaseResult.setMessage(ExceptionsUtil.getStackTraceForThrowable(error));
                    logger.logError(testCaseResult.getMessage(), null, error);
                    return testCaseResult;
                }

                accessMainPhase();

                invokeTestSuiteMethod(TearDownTestCase.class.getName(), StringConstants.LOG_TEAR_DOWN_ACTION, true,
                        testCaseResult);
        	} else {
        		 TestStatus testStatus = new TestStatus();
        		 testStatus.setStatusValue(TestStatusValue.SKIPPED);
        		 testStatus.setStackTrace(StringConstants.TEST_CASE_SKIPPED);
        		 testCaseResult.setTestStatus(testStatus);
        	}
            return testCaseResult;
        } finally {
            testCaseContext.setTestCaseStatus(testCaseResult.getTestStatus().getStatusValue().name());
        	testCaseContext.setMessage(testCaseResult.getMessage());

            if (testCaseContext.isMainTestCase()) {
                eventManager.publicEvent(ExecutionListenerEvent.AFTER_TEST_CASE, new Object[] { testCaseContext });
            }

            if (testCaseContext.isMainTestCase()) {
                logger.endTest(testCase.getTestCaseId(), null);
            } else {
                logger.endCalledTest(testCase.getTestCaseId(), null);
            }

            postExecution();
        }
    }

    private TestResult invokeTestSuiteMethod(String methodName, String actionType, boolean ignoredIfFailed,
            TestResult testCaseResult) {
        if (testSuiteExecutor != null) {
            ErrorCollector errorCollector = ErrorCollector.getCollector();
            List<Throwable> coppiedError = errorCollector.getCoppiedErrors();
            errorCollector.clearErrors();

            testSuiteExecutor.invokeEachTestCaseMethod(methodName, actionType, ignoredIfFailed);

            if (!ignoredIfFailed && errorCollector.containsErrors()) {
                coppiedError.add(errorCollector.getFirstError());
            }

            errorCollector.clearErrors();
            errorCollector.getErrors().addAll(coppiedError);

            if (errorCollector.containsErrors() && ignoredIfFailed) {
                Throwable firstError = errorCollector.getFirstError();
                TestStatus testStatus = new TestStatus();
                TestStatusValue errorType = ErrorCollector.isErrorFailed(firstError) ? TestStatusValue.FAILED
                        : TestStatusValue.ERROR;
                testStatus.setStatusValue(errorType);
                String errorMessage = ExceptionsUtil.getMessageForThrowable(firstError);
                testStatus.setStackTrace(errorMessage);
                testCaseResult.setTestStatus(testStatus);

                return testCaseResult;
            }
        }
        return testCaseResult;
    }

    private void accessMainPhase() {
        if (!processSetupPhase()) {
            return;
        }

        processExecutionPhase();
    }

    private void processExecutionPhase() {
        try {
            // Prepare configuration before execution
            engine.changeConfigForExecutingScript();
            setupContextClassLoader();
            doExecute();
        } catch (ExceptionInInitializerError e) {
            // errors happened in static initializer like for Global Variable
            errorCollector.addError(e.getCause());
        } catch (Throwable e) {
            // logError(e, ExceptionsUtil.getMessageForThrowable(e));
            errorCollector.addError(e);
        }

        if (errorCollector.containsErrors()) {
            onExecutionError(errorCollector.getFirstError());
        } else {
            onExecutionComplete();
        }

        if (doCleanUp) {
            cleanUp();
        }
    }

    protected void doExecute() throws ResourceException, ScriptException, IOException, ClassNotFoundException {
        testCaseResult.setScriptResult(runScript(getScriptFile()));
    }

    private void cleanUp() {
        DriverCleanerCollector.getInstance().cleanDrivers();
    }

    private Object runScript(File scriptFile)
            throws ResourceException, ScriptException, IOException, ClassNotFoundException {
        return engine.runScriptAsRawText(
                FileUtils.readFileToString(scriptFile, DF_CHARSET),
                scriptFile.toURI().toURL().toExternalForm(), 
                variableBinding,
                getTestCase().getName());
    }

    protected void runMethod(File scriptFile, String methodName)
            throws ResourceException, ScriptException, ClassNotFoundException, IOException {
        engine.changeConfigForExecutingScript();
        engine.runScriptMethodAsRawText(FileUtils.readFileToString(scriptFile, DF_CHARSET),
                scriptFile.toURI().toURL().toExternalForm(), methodName, variableBinding);
    }

    private Map<String, String> getTestCaseProperties(TestCaseBinding testCaseBinding, TestCase testCase,
            FailureHandling flowControl) {
        Map<String, String> testProperties = new HashMap<String, String>();
        testProperties.put(StringConstants.XML_LOG_NAME_PROPERTY, testCaseBinding.getTestCaseId());
        testProperties.put(StringConstants.XML_LOG_DESCRIPTION_PROPERTY, testCase.getDescription());
        testProperties.put(StringConstants.XML_LOG_ID_PROPERTY, testCase.getTestCaseId());
        testProperties.put(StringConstants.XML_LOG_SOURCE_PROPERTY, testCase.getMetaFilePath());
        testProperties.put(StringConstants.XML_LOG_TAG_PROPERTY, testCase.getTag());
        testProperties.put(StringConstants.XML_LOG_IS_OPTIONAL,
                String.valueOf(flowControl == FailureHandling.OPTIONAL));
        return testProperties;
    }

    /**
     * Returns DEFAULT test case variables and their values.
     */
    private Map<String, Object> getBindedValues() {
        Map<String, Object> bindedValues = testCaseBinding.getBindedValues();
        return bindedValues != null ? bindedValues : Collections.emptyMap();
    }

    private Binding collectTestCaseVariables() {
        Binding variableBinding = new Binding(testCaseBinding != null ? testCaseBinding.getBindedValues() : Collections.emptyMap());
        engine.changeConfigForCollectingVariable();

        logger.logDebug(StringConstants.MAIN_LOG_INFO_START_EVALUATE_VARIABLE);
        testCase.getVariables().stream().forEach(testCaseVariable -> {
            String variableName = testCaseVariable.getName();
            if (getBindedValues().containsKey(variableName)) {
                Object variableValue = testCaseBinding.getBindedValues().get(variableName);
                logVariableValue(variableName, variableValue, testCaseVariable.isMasked(),
                        StringConstants.MAIN_LOG_INFO_VARIABLE_NAME_X_IS_SET_TO_Y);
                variableBinding.setVariable(variableName, variableValue);
                return;
            }

            try {
                String defaultValue = StringUtils.defaultIfEmpty(testCaseVariable.getDefaultValue(),
                        StringConstants.NULL_AS_STRING);
                Object defaultValueObject = engine.runScriptWithoutLogging(defaultValue, null);
                logVariableValue(variableName, defaultValueObject, testCaseVariable.isMasked(),
                        StringConstants.MAIN_LOG_INFO_VARIABLE_NAME_X_IS_SET_TO_Y_AS_DEFAULT);
                variableBinding.setVariable(variableName, defaultValueObject);
            } catch (ExceptionInInitializerError e) {
                logger.logWarning(MessageFormat.format(StringConstants.MAIN_LOG_MSG_SET_TEST_VARIABLE_ERROR_BECAUSE_OF,
                        variableName, e.getCause().getMessage()), null, e);
            } catch (Exception e) {
                logger.logWarning(MessageFormat.format(StringConstants.MAIN_LOG_MSG_SET_TEST_VARIABLE_ERROR_BECAUSE_OF,
                        variableName, e.getMessage()), null, e);
            }
        });
        getBindedValues().entrySet()
                .stream()
                .filter(entry -> !variableBinding.hasVariable(entry.getKey()))
                .forEach(entry -> {
                    String variableName = entry.getKey();
                    Object variableValue = entry.getValue();
                    variableBinding.setProperty(variableName, variableValue);
                    logVariableValue(variableName, variableValue, false,
                            StringConstants.MAIN_LOG_INFO_VARIABLE_NAME_X_IS_SET_TO_Y);
                });
        return variableBinding;
    }

    private void logVariableValue(String variableName, Object value, boolean isMasked, String message) {
        String objectAsString = Objects.toString(value);
        String loggedText = isMasked ? StringUtils.repeat("*", objectAsString.length())
                : Objects.toString(objectAsString);
        logger.logInfo(MessageFormat.format(message, variableName, loggedText));
    }

    private void logError(Throwable t, String message) {
        logger.logMessage(ErrorCollector.fromError(t), message, t);
    }

    private TestStatusValue getResultByError(Throwable t) {
        return TestStatusValue.valueOf(ErrorCollector.fromError(t).name());
    }

    private void endAllUnfinishedKeywords(Stack<KeywordStackElement> keywordStack) {
        while (!keywordStack.isEmpty()) {
            KeywordStackElement keywordStackElement = keywordStack.pop();
            logger.endKeyword(keywordStackElement.getKeywordName(), null, keywordStackElement.getNestedLevel());
        }
    }

    private void internallyRunMethods(TestCaseMethodNodeWrapper methodNodeWrapper) {
        List<MethodNode> methodList = methodNodeWrapper.getMethodNodes();
        if (methodList == null || methodList.isEmpty()) {
            return;
        }

        logger.logDebug(methodNodeWrapper.getStartMessage());
        int count = 1;
        for (MethodNode method : methodList) {
            runMethod(method.getName(), methodNodeWrapper.getActionType(), count++,
                    methodNodeWrapper.isIgnoredIfFailed());
        }
    }

    private void runMethod(String methodName, String actionType, int index, boolean ignoreIfFailed) {
        Stack<KeywordStackElement> keywordStack = new Stack<KeywordStackElement>();
        Map<String, String> startKeywordAttributeMap = new HashMap<String, String>();
        startKeywordAttributeMap.put(StringConstants.XML_LOG_STEP_INDEX, String.valueOf(index));
        if (ignoreIfFailed) {
            startKeywordAttributeMap.put(StringConstants.XML_LOG_IS_IGNORED_IF_FAILED, String.valueOf(ignoreIfFailed));
        }
        logger.startKeyword(methodName, actionType, startKeywordAttributeMap, keywordStack);
        try {
            runMethod(getScriptFile(), methodName);
            endAllUnfinishedKeywords(keywordStack);
            logger.logPassed(MessageFormat.format(StringConstants.MAIN_LOG_PASSED_METHOD_COMPLETED, methodName));
        } catch (Throwable e) {
            endAllUnfinishedKeywords(keywordStack);
            String message = MessageFormat.format(StringConstants.MAIN_LOG_WARNING_ERROR_OCCURRED_WHEN_RUN_METHOD,
                    methodName, e.getClass().getName(), ExceptionsUtil.getMessageForThrowable(e));
            if (ignoreIfFailed) {
                logger.logWarning(message, null, e);
                return;
            }
            logger.logError(message, null, e);
            errorCollector.addError(e);
        } finally {
            logger.endKeyword(methodName, actionType, Collections.emptyMap(), keywordStack);
        }
    }

    private void runTearDownMethodByError(Throwable t) {
        LogLevel errorLevel = ErrorCollector.fromError(t);
        TestCaseMethodNodeWrapper failedMethodWrapper = methodNodeCollector
                .getMethodNodeWrapper(TearDownIfFailed.class);
        if (errorLevel == LogLevel.ERROR) {
            failedMethodWrapper = methodNodeCollector.getMethodNodeWrapper(TearDownIfError.class);
        }

        internallyRunMethods(failedMethodWrapper);
        internallyRunMethods(methodNodeCollector.getMethodNodeWrapper(TearDown.class));
    }

    @SuppressWarnings("unchecked")
    public void setupContextClassLoader() {
        AccessController.doPrivileged(new DoSetContextAction(Thread.currentThread(), engine.getGroovyClassLoader()));
    }
    
    public TestCase getTestCase() {
        return testCase;
    }
}
