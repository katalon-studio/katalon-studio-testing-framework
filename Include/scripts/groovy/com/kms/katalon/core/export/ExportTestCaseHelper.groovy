package com.kms.katalon.core.export

import groovy.transform.CompileStatic

import java.lang.reflect.Method

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.ImportNode
import org.codehaus.groovy.ast.ModuleNode

import com.kms.katalon.core.ast.GroovyParser
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.constants.StringConstants
import com.kms.katalon.core.driver.internal.DriverCleanerCollector
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.IKeywordContributor
import com.kms.katalon.core.keyword.internal.KeywordContributorCollection
import com.kms.katalon.core.keyword.internal.KeywordExceptionHandler
import com.kms.katalon.core.logging.ErrorCollector
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.logging.LogLevel
import com.kms.katalon.core.logging.model.TestStatus
import com.kms.katalon.core.logging.model.TestStatus.TestStatusValue
import com.kms.katalon.core.main.TestCaseMain
import com.kms.katalon.core.main.TestResult
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testcase.TestCaseFactory
import com.kms.katalon.core.testcase.Variable
import com.kms.katalon.core.testdata.TestDataColumn
import com.kms.katalon.core.util.internal.ExceptionsUtil

public class ExportTestCaseHelper {
    
    private static final KeywordLogger logger = KeywordLogger.getInstance(ExportTestCaseHelper.class);
    
    private static String LOG_FILE_NAME = "execution0.log";

    @CompileStatic
    public static prepareTest(String testCaseId, String projectLocation, String driverType, String driverLocation, int pageLoadTimeout, int waitForIEHang, String logFolderLocation, def scriptClass) {
        setSystemProperties(projectLocation, pageLoadTimeout, driverType, driverLocation, waitForIEHang, logFolderLocation)
        prepareTestRunData(testCaseId, scriptClass);
    }

    @CompileStatic
    private static prepareTestRunData(String testCaseId, scriptClass) {
        for (IKeywordContributor contributor in KeywordContributorCollection.getKeywordContributors()) {
            if (contributor.getDriverCleaner() != null) {
                DriverCleanerCollector.getInstance().addDriverCleaner(contributor.getDriverCleaner().getConstructor().newInstance());
            }
        }
        TestCaseMain.beforeStart()
        addTestCaseVariableToMethod(testCaseId, scriptClass)
    }

    @CompileStatic
    private static void stepFailed(String message, FailureHandling flHandling, String reason) throws StepFailedException {
        StringBuilder failMessage = new StringBuilder(message);
        if (reason != null) {
            failMessage.append(" (Root cause: ");
            failMessage.append(reason);
            failMessage.append(")");
        }
        if (flHandling == null || flHandling != FailureHandling.OPTIONAL) {
            logger.logFailed(failMessage.toString());
            KeywordExceptionHandler.throwExceptionIfStepFailed(failMessage.toString(), flHandling);
        } else {
            logger.logWarning(failMessage.toString());
        }
    }

    public static void callTestCase(TestCase calledTestCase, Map<String, Object> bindings, FailureHandling flowControl) throws Exception {
        String keywordName = "callTestCase";
        List<Throwable> parentErrors = ErrorCollector.getCollector().getCoppiedErrors();
        try {
            logger.logDebug("Checking called test case");
            if (calledTestCase == null) {
                throw new IllegalArgumentException("Called test case is null");
            }
            logger.logDebug("Calling Test Case '" + calledTestCase.getTestCaseId() + "'");
            //			TestStatusEntity result = TestCaseMain.runTestCase(calledTestCase.getTestCaseId(), new TestCaseBinding(
            //					calledTestCase.getTestCaseId(), binding), flowControl);
            TestCase testCase = TestCaseFactory.findTestCase(calledTestCase.getTestCaseId());
            GroovyClassLoader classLoader = new GroovyClassLoader()
            Class scriptClass = classLoader.parseClass(new File(testCase.getGroovyScriptPath()))
            def newScriptClassInstance = scriptClass.getConstructor().newInstance()
            prepareTestRunData(testCase.getTestCaseId(), newScriptClassInstance);
            for ( binding in bindings ) {
                if (!(binding.getValue() instanceof TestDataColumn)) {
                    newScriptClassInstance.metaClass."${binding.getKey()}" = binding.getValue()
                }
            }
            TestResult result = new TestResult();
            TestStatus statusEntity = new TestStatus();
            statusEntity.setStatusValue(TestStatusValue.PASSED);
            result.setTestStatus(statusEntity)
            try {
                result = ExportTestCaseScript.internallyRunScript(calledTestCase.getTestCaseId(), scriptClass, newScriptClassInstance);
            } catch (Throwable t) {
                ErrorCollector.getCollector().addError(t);
                Throwable firstError = ErrorCollector.getCollector().getFirstError();
                // Log the first error, not the current caught error
                statusEntity.setStatusValue(getResultByError(firstError, calledTestCase.getTestCaseId()));
                result.setMessage(ErrorCollector.getCollector().getFirstError().getMessage());
            } finally {
                ErrorCollector.getCollector().getErrors().addAll(0, parentErrors);
            }
            statusEntity = result.getTestStatus();
            switch (statusEntity.getStatusValue()) {
                case TestStatusValue.FAILED:
                    stepFailed("Call test case '" + calledTestCase.getTestCaseId() + "' failed", flowControl, null);
                    break;
                case TestStatusValue.ERROR:
                    stepFailed("Call test case '" + calledTestCase.getTestCaseId() + "' failed because of error(s)",
                    flowControl, result.getMessage());
                    break;
                case TestStatusValue.PASSED:
                    logger.logPassed("Call test case '" + calledTestCase.getTestCaseId() + "' sucessfully");
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            if (ex instanceof StepFailedException) {
                throw (StepFailedException) ex;
            }
            stepFailed(
                    "Unable to call test case"
                    + ((calledTestCase != null) ? " with id '" + calledTestCase.getTestCaseId() + "'" : ""),
                    flowControl, ExceptionsUtil.getMessageForThrowable(ex));
        } finally {
            if (flowControl == FailureHandling.OPTIONAL) {
                ErrorCollector.getCollector().clearErrors();
                ErrorCollector.getCollector().getErrors().addAll(parentErrors);
            }
        }
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

    public static void addTestCaseVariableToMethod(String testCaseId, def scriptClassInstance) {
        TestCase testCase = TestCaseFactory.findTestCase(testCaseId);

        Binding binding = new Binding();
        GroovyShell groovyShell = new GroovyShell();
        for (Variable testCaseVariable : testCase.getVariables()) {
            if (!binding.hasVariable(testCaseVariable.getName())) {
                String defaultValue = testCaseVariable.getDefaultValue();
                if (defaultValue.isEmpty()) {
                    defaultValue = "null";
                }
                scriptClassInstance.metaClass."${testCaseVariable.getName()}" = groovyShell.evaluate(defaultValue)
            }
        }
    }
    
    @CompileStatic
    private static setSystemProperties(String projectLocation, int pageLoadTimeout, String driverType, String driverLocation, int waitForIEHang, String logFolderLocation) {
        System.setProperty(RunConfiguration.PROJECT_DIR_PROPERTY, projectLocation)
        System.setProperty(RunConfiguration.TIMEOUT_PROPERTY, String.valueOf(pageLoadTimeout))
        System.setProperty(StringConstants.XML_LOG_BROWSER_TYPE_PROPERTY, getDriverFromString(driverType))
        switch (driverType) {
            case "Chrome":
                System.setProperty("chromeDriverPath", driverLocation);
                break;
            case "IE":
                System.setProperty("ieDriverPath", driverLocation);
                break;
        }
        System.setProperty("waitForIEHanging", String.valueOf(waitForIEHang))
        File testCaseLogFile = getLogFileFromLogFolderLocation(logFolderLocation)
        System.setProperty(RunConfiguration.LOG_FILE_PATH_PROPERTY, testCaseLogFile.getAbsolutePath().replace("\\", "/"))
    }

    @CompileStatic
    public static File getLogFileFromLogFolderLocation(String logFolderLocation) {
        File testCaseLogDir = new File(logFolderLocation)
        ensureFolderExist(testCaseLogDir)
        return new File(testCaseLogDir.getAbsolutePath() + File.separator + LOG_FILE_NAME);
    }

    @CompileStatic
    public static cleanUpTest(Class<?> scriptClass) {
    }

    @CompileStatic
    private static ensureFolderExist(File folder) throws Exception {
        if (folder.exists()) {
            return
        } else {
            if (!folder.getParentFile().exists()) {
                ensureFolderExist(folder.getParentFile())
            }
            folder.mkdirs()
        }
    }

    @CompileStatic
    public static ClassNode getMainClassNode(List<ASTNode> astNodes) {
        for (ASTNode astNode : astNodes) {
            if (astNode instanceof ClassNode) {
                if (((ClassNode) astNode).isScript()) {
                    return ((ClassNode) astNode);
                }
            }
        }
        return null;
    }

    @CompileStatic
    public static String getImportString(ClassNode classNode) {
        StringBuilder importString = new StringBuilder();
        GroovyParser groovyParser = new GroovyParser(importString);
        if (classNode != null) {
            ModuleNode moduleNode = classNode.getModule();
            if (moduleNode != null) {
                for (ImportNode importNode : moduleNode.getImports()) {
                    groovyParser.parse(importNode);
                }
            }
        }
        return importString.toString();
    }

    @CompileStatic
    public static String getDriverFromString(String driver) {
        switch (driver) {
            case "Firefox":
                return "FIREFOX_DRIVER";
            case "Chrome":
                return "CHROME_DRIVER";
            case "IE":
                return "IE_DRIVER";
            case "Safari":
                return "SAFARI_DRIVER"
        }
        throw new IllegalArgumentException("Invalid driver: '" + driver + "'")
    }

    public static List<Method> findAllMethodForClassWithAnotation( Class<?> scriptClass, Class<?> annotationClass ) {
        return scriptClass.declaredMethods.findAll {
            annotationClass in it.getDeclaredAnnotations()*.annotationType()
        }
    }
}
