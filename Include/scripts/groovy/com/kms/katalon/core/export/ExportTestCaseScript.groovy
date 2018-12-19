package com.kms.katalon.core.export

import groovy.transform.CompileStatic

import java.lang.reflect.Method

import com.kms.katalon.core.annotation.SetUp
import com.kms.katalon.core.annotation.TearDown
import com.kms.katalon.core.annotation.TearDownIfError
import com.kms.katalon.core.annotation.TearDownIfFailed
import com.kms.katalon.core.annotation.TearDownIfPassed
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.logging.ErrorCollector
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.logging.LogLevel
import com.kms.katalon.core.logging.KeywordLogger.KeywordStackElement
import com.kms.katalon.core.logging.model.TestStatus
import com.kms.katalon.core.logging.model.TestStatus.TestStatusValue
import com.kms.katalon.core.main.TestResult
import com.kms.katalon.core.util.internal.ExceptionsUtil

@CompileStatic
public abstract class ExportTestCaseScript {
    
    private static final KeywordLogger logger = KeywordLogger.getInstance(ExportTestCaseScript.class);
    
	protected ExportTestCaseScript() {}
	protected abstract run()

	/***
	 * Run a script independently
	 * @param exportScriptClass the script class
	 * @param testCaseId id of the test case
	 * @param projectLocation location of the project
	 * @param driver driver to run the test case (Firefox, Chrome, IE, Safari)
	 * @param pageLoadTimeout pageLoadTimeOut value
	 */
	@CompileStatic
	public static main(Class<? extends ExportTestCaseScript> exportScriptClass, String testCaseId, String projectLocation, String driver, String driverLocation, int pageLoadTimeout) {
		String logFolderLocation = projectLocation + File.separator + "Temp" + File.separator + testCaseId + File.separator + "log"
		def newScriptClassInstance = exportScriptClass.getConstructor().newInstance()
		
		ExportTestCaseHelper.prepareTest(testCaseId, projectLocation, driver, driverLocation,
				pageLoadTimeout, 600, logFolderLocation, newScriptClassInstance)
		return internallyRunScript(testCaseId, exportScriptClass, newScriptClassInstance)
	}

	@CompileStatic
	public static TestResult internallyRunScript(String testCaseId, Class exportScriptClass, ExportTestCaseScript newScriptClassInstance) {
		TestResult testResult = new TestResult();
		TestStatus statusEntity = new TestStatus();
		testResult.setTestStatus(statusEntity);
		statusEntity.setStatusValue(TestStatusValue.PASSED);
		List<Method> beforeRunMethods = null;
		List<Method> afterRunMethods = null;
		List<Method> afterRunPassedMethods = null;
		List<Method> afterRunFailedMethods = null;
		List<Method> afterRunErrorMethods = null;

		List<Throwable> parentErrors = ErrorCollector.getCollector().getCoppiedErrors();
		Stack<KeywordStackElement> keywordStack = new Stack<KeywordStackElement>();
		logger.startTest(testCaseId, null, keywordStack)
		try {
			beforeRunMethods = ExportTestCaseHelper.findAllMethodForClassWithAnotation(exportScriptClass, SetUp);
			afterRunMethods = ExportTestCaseHelper.findAllMethodForClassWithAnotation(exportScriptClass, TearDown);
			afterRunPassedMethods = ExportTestCaseHelper.findAllMethodForClassWithAnotation(exportScriptClass, TearDownIfPassed);
			afterRunFailedMethods = ExportTestCaseHelper.findAllMethodForClassWithAnotation(exportScriptClass, TearDownIfFailed);
			afterRunErrorMethods = ExportTestCaseHelper.findAllMethodForClassWithAnotation(exportScriptClass, TearDownIfError);

			ErrorCollector.getCollector().clearErrors();

			internallyRunMethods(testCaseId, beforeRunMethods, newScriptClassInstance, "Start running set up methods for test case");
			def runMethod = newScriptClassInstance.&run
			runMethod.call()
			if (ErrorCollector.getCollector().containsErrors()) {
				Throwable firstError = ErrorCollector.getCollector().getFirstError();
				runTearDownMethodByError(testCaseId, firstError, newScriptClassInstance, afterRunFailedMethods, afterRunErrorMethods,
						afterRunMethods);
				statusEntity.setStatusValue(getResultByError(firstError, testCaseId));
			} else {
				internallyRunMethods(testCaseId, afterRunPassedMethods, newScriptClassInstance,
						"Start running tear down methods for passed test case");
				internallyRunMethods(testCaseId, afterRunMethods, newScriptClassInstance,
						"Start running tear down methods for test case");
				logger.logPassed(testCaseId);
			}
		} catch (Throwable t) {
			ErrorCollector.getCollector().addError(t);
			Throwable firstError = ErrorCollector.getCollector().getFirstError();
			statusEntity.setStatusValue(getResultByError(firstError, testCaseId));
			testResult.setMessage(ErrorCollector.getCollector().getFirstError().getMessage());
			endAllUnfinishedKeywords(keywordStack);
			runTearDownMethodByError(testCaseId, firstError, newScriptClassInstance, afterRunFailedMethods, afterRunErrorMethods,
					afterRunMethods);
			// Log the first error, not the current caught error
		} finally {
			ErrorCollector.getCollector().getErrors().addAll(0, parentErrors);
			logger.endTest(testCaseId, null)
		}
		return testResult
	}
	
	@CompileStatic
	private static void endAllUnfinishedKeywords(Stack<KeywordStackElement> keywordStack) {
		while (!keywordStack.isEmpty()) {
			KeywordStackElement keywordStackElement = keywordStack.pop();
			logger.endKeyword(keywordStackElement.getKeywordName(), null, keywordStackElement.getNestedLevel());
		}
	}

	@CompileStatic
	private static void runTearDownMethodByError(String testCaseId, Throwable t, def newScriptClassInstance,
			List<Method> afterRunFailedMethods, List<Method> afterRunErrorMethods,
			List<Method> afterRunMethods) {
		if (t.getClass().getName().equals(StepFailedException.class.getName()) || t instanceof AssertionError) {
			internallyRunMethods(testCaseId, afterRunFailedMethods, newScriptClassInstance,
					"Start running tear down methods for failed test case");
		} else {
			internallyRunMethods(testCaseId, afterRunErrorMethods, newScriptClassInstance,
					"Start running tear down methods for error test case");
		}
		internallyRunMethods(testCaseId, afterRunMethods, newScriptClassInstance, "Start running tear down methods for test case");
	}
			
	@CompileStatic
	private static TestStatusValue getResultByError(Throwable t, String testCaseId) {
		if (t.getClass().getName().equals(StepFailedException.class.getName()) || t instanceof AssertionError) {
			logger.logMessage(LogLevel.FAILED,
					testCaseId + " FAILED because (of) " + ExceptionsUtil.getMessageForThrowable(t), t);
			return TestStatusValue.FAILED;
		} else {
			logger.logMessage(LogLevel.ERROR,
					testCaseId + " has ERROR(s) because (of) " + ExceptionsUtil.getMessageForThrowable(t), t);
			return TestStatusValue.ERROR;
		}
	}

	@CompileStatic
	private static void internallyRunMethods(String testCaseId, List<Method> methodList, def newScriptClassInstance,
			String startMessage) {
		if (methodList != null && methodList.size() > 0) {
			logger.logDebug(startMessage);
			for (Method method : methodList) {
                Stack<KeywordStackElement> keywordStack = new Stack<KeywordStackElement>();
                logger.startKeyword(method.getName(), null, keywordStack);
				try {
					def methodClosure = (newScriptClassInstance).&"${method.getName()}"
					ExportTestCaseHelper.addTestCaseVariableToMethod(testCaseId, newScriptClassInstance)
					methodClosure.call()
					logger.logPassed("Method '" + method.getName() + "' complete successfully")
				} catch (Exception e) {
					logger.logWarning("Error occurred when try to run method '" + method.getName() + "' (Root cause: "
							+ e.getClass().getName().toString() + " - " + e.getMessage() + ")");
				} finally {
                    endAllUnfinishedKeywords(keywordStack);
                    logger.endKeyword(method.getName(), null, keywordStack);
                }
			}
		}
	}
}
