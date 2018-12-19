package com.kms.katalon.core.main;

import java.io.IOException;
import java.security.AccessController;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import com.kms.katalon.core.constants.StringConstants;
import com.kms.katalon.core.context.internal.ExecutionEventManager;
import com.kms.katalon.core.logging.ErrorCollector;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.logging.KeywordLogger.KeywordStackElement;
import com.kms.katalon.core.logging.model.TestStatus.TestStatusValue;
import com.kms.katalon.core.model.FailureHandling;
import com.kms.katalon.core.testcase.TestCaseBinding;
import com.kms.katalon.core.util.internal.ExceptionsUtil;

import groovy.lang.Binding;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

public class WSVerificationExecutor {
    
    private final KeywordLogger logger = KeywordLogger.getInstance(this.getClass());

    private static ErrorCollector errorCollector = ErrorCollector.getCollector();
    
    private String script;

    protected TestResult testResult;

    private Stack<KeywordStackElement> keywordStack;

    private List<Throwable> parentErrors;

    protected ScriptEngine engine;

    private boolean doCleanUp;

    private TestCaseBinding testCaseBinding;


    public WSVerificationExecutor(String verificationScript,
            ScriptEngine engine,
            ExecutionEventManager eventManager,
            boolean doCleanUp) {
        
        this.engine = engine;
        this.doCleanUp = doCleanUp;
        this.script = verificationScript;
    }
    
    public WSVerificationExecutor(
            TestCaseBinding testCaseBinding,
            String verificationScript,
            ScriptEngine engine,
            ExecutionEventManager eventManager,
            boolean doCleanUp) {
        
        this.engine = engine;
        this.doCleanUp = doCleanUp;
        this.script = verificationScript;
        this.testCaseBinding = testCaseBinding;
    }
    
    private void preExecution() {
        testResult = TestResult.getDefault();
        keywordStack = new Stack<KeywordLogger.KeywordStackElement>();
        parentErrors = errorCollector.getCoppiedErrors();
        errorCollector.clearErrors();
    }

    private void onExecutionComplete() {
        endAllUnfinishedKeywords(keywordStack);

        logger.logPassed(StringConstants.WS_VERIFICATION_SUCCESS);
    }

    private void onExecutionError(Throwable t) {
        
        if (!keywordStack.isEmpty()) {
            endAllUnfinishedKeywords(keywordStack);
        }
        testResult.getTestStatus().setStatusValue(getResultByError(t));
        String message =  MessageFormat.format(StringConstants.MAIN_LOG_MSG_FAILED_BECAUSE_OF, "Verification",
                ExceptionsUtil.getStackTraceForThrowable(t));
        testResult.setMessage(message);
        logError(t, message);
    }

    private void postExecution() {
        errorCollector.getErrors().addAll(0, parentErrors);
    }

    public TestResult execute(FailureHandling flowControl) {
        try {
            preExecution();
            
            logger.startTest("Verification", Collections.emptyMap(), keywordStack);

            accessMainPhase();

            return testResult;
        } finally {
        
            logger.endTest("Verification", Collections.emptyMap());

            postExecution();
        }
    }

    private void accessMainPhase() {
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
    }

    protected void doExecute() throws ResourceException, ScriptException, IOException, ClassNotFoundException {
        testResult.setScriptResult(runScript(script));
    }


    private Object runScript(String script)
            throws ResourceException, ScriptException, IOException, ClassNotFoundException {
        return engine.runScriptAsRawText(
                script, 
                "WSVerification" + System.currentTimeMillis(),
                testCaseBinding != null ? new Binding(testCaseBinding.getBindedValues()) : new Binding(),
                testCaseBinding != null ? testCaseBinding.getTestCaseName() : null);
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

    @SuppressWarnings("unchecked")
    public void setupContextClassLoader() {
        AccessController.doPrivileged(new DoSetContextAction(Thread.currentThread(), engine.getGroovyClassLoader()));
    }
}
