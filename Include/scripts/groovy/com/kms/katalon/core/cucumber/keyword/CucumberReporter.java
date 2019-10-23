package com.kms.katalon.core.cucumber.keyword;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Stack;

import com.kms.katalon.core.constants.StringConstants;
import com.kms.katalon.core.logging.ErrorCollector;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.logging.LogLevel;
import com.kms.katalon.core.util.internal.ExceptionsUtil;

import cucumber.api.PickleStepTestStep;
import cucumber.api.Result;
import cucumber.api.Result.Type;
import cucumber.api.TestCase;
import cucumber.api.event.EventHandler;
import cucumber.api.event.EventPublisher;
import cucumber.api.event.TestCaseFinished;
import cucumber.api.event.TestCaseStarted;
import cucumber.api.event.TestRunFinished;
import cucumber.api.event.TestRunStarted;
import cucumber.api.event.TestStepFinished;
import cucumber.api.event.TestStepStarted;
import cucumber.api.formatter.Formatter;

public class CucumberReporter implements Formatter {

    private final KeywordLogger logger = KeywordLogger.getInstance(this.getClass());

    public CucumberReporter() {

    }

    @Override
    public void setEventPublisher(EventPublisher eventPublisher) {
        eventPublisher.registerHandlerFor(TestCaseStarted.class, new EventHandler<TestCaseStarted>() {

            @Override
            public void receive(TestCaseStarted event) {
                TestCase testCase = event.testCase;
                String name = getTestCaseName(testCase);
                logger.startTest(name, new HashMap<String, String>(), new Stack<KeywordLogger.KeywordStackElement>());
            }
        });
        eventPublisher.registerHandlerFor(TestCaseFinished.class, new EventHandler<TestCaseFinished>() {

            @Override
            public void receive(TestCaseFinished event) {
                TestCase testCase = event.testCase;
                String name = getTestCaseName(testCase);
                Result result = event.result;
                logResult(name, result);
                logger.endTest(name, new HashMap<String, String>());
            }
        });
        eventPublisher.registerHandlerFor(TestRunStarted.class, new EventHandler<TestRunStarted>() {

            @Override
            public void receive(TestRunStarted event) {
            }
        });
        eventPublisher.registerHandlerFor(TestRunFinished.class, new EventHandler<TestRunFinished>() {

            @Override
            public void receive(TestRunFinished event) {
            }
        });
        eventPublisher.registerHandlerFor(TestStepStarted.class, new EventHandler<TestStepStarted>() {

            @Override
            public void receive(TestStepStarted event) {
                if (event.testStep instanceof PickleStepTestStep) {
                    logger.startKeyword(getStepText(event), new HashMap<String, String>(),
                            new Stack<KeywordLogger.KeywordStackElement>());
                }
            }
        });
        eventPublisher.registerHandlerFor(TestStepFinished.class, new EventHandler<TestStepFinished>() {

            @Override
            public void receive(TestStepFinished event) {
                if (event.testStep instanceof PickleStepTestStep) {
                    String name = getStepText(event);
                    Result result = event.result;
                    logResult(name, result);
                    logger.endKeyword(name, new HashMap<String, String>(),
                            new Stack<KeywordLogger.KeywordStackElement>());
                }
            }
        });
    }

    private void logResult(String name, Result result) {
        Type status = result.getStatus();
        if (Type.PASSED.equals(status)) {
            logger.logPassed(name);
        } else {
            Throwable t = result.getError();
            if (t == null) {
                LogLevel level;
                if (Type.FAILED.equals(status)) {
                    level = LogLevel.FAILED;
                } else {
                    level = LogLevel.NOT_RUN;
                }
                logger.logMessage(level, name);
            } else {
                String stackTraceForThrowable = ExceptionsUtil.getStackTraceForThrowable(t);
                String message = MessageFormat.format(StringConstants.MAIN_LOG_MSG_FAILED_BECAUSE_OF, name,
                        stackTraceForThrowable);
                logError(t, message);
            }
        }
    }

    private String getStepText(TestStepStarted event) {
        String text;
        if (event.testStep instanceof PickleStepTestStep) {
            text = ((PickleStepTestStep) event.testStep).getStepText();
        } else {
            // text = event.testStep.getStepText();
            text = "";
        }
        return text;
    }

    private String getStepText(TestStepFinished event) {
        String text;
        if (event.testStep instanceof PickleStepTestStep) {
            text = ((PickleStepTestStep) event.testStep).getStepText();
        } else {
            // text = event.testStep.getStepText();
            text = "";
        }
        return text;
    }

    private void logError(Throwable t, String message) {
        logger.logMessage(ErrorCollector.fromError(t), message, t);
    }

    private String getTestCaseName(TestCase testCase) {
        String name = "SCENARIO " + testCase.getName();
        return name;
    }
}
