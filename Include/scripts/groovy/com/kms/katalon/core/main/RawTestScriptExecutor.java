package com.kms.katalon.core.main;

import java.io.File;
import java.io.IOException;

import com.kms.katalon.core.context.internal.ExecutionEventManager;
import com.kms.katalon.core.context.internal.InternalTestCaseContext;
import com.kms.katalon.core.testcase.TestCaseBinding;

import groovy.util.ResourceException;
import groovy.util.ScriptException;

public class RawTestScriptExecutor extends TestCaseExecutor {
    private String rawScript;

    public RawTestScriptExecutor(String rawScript, TestCaseBinding testCaseBinding,
            ScriptEngine engine, ExecutionEventManager eventManager, InternalTestCaseContext testCaseContext) {
        this(rawScript, testCaseBinding, engine, eventManager, testCaseContext, false);
    }

    public RawTestScriptExecutor(String rawScript, TestCaseBinding testCaseBinding,
            ScriptEngine engine, ExecutionEventManager eventManager, InternalTestCaseContext testCaseContext, boolean doCleanUp) {
        super(testCaseBinding, engine, eventManager, testCaseContext, doCleanUp);
        this.rawScript = rawScript;
    }

    @Override
    protected void doExecute() throws ResourceException, ScriptException, IOException, ClassNotFoundException {
        testCaseResult.setScriptResult(runScript(rawScript));
    }

    private Object runScript(String rawScript)
            throws ResourceException, ScriptException, IOException, ClassNotFoundException {
        return engine.runScriptAsRawText(
                rawScript, 
                getScriptFile().getName(), 
                variableBinding,
                getTestCase().getName());
    }

    @Override
    protected void runMethod(File scriptFile, String methodName)
            throws ResourceException, ScriptException, ClassNotFoundException, IOException {
        engine.changeConfigForExecutingScript();
        engine.runScriptMethodAsRawText(rawScript, scriptFile.getName(), methodName, variableBinding);
    }
}
