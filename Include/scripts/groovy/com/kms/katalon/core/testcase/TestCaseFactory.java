package com.kms.katalon.core.testcase;

import static com.kms.katalon.core.constants.StringConstants.ID_SEPARATOR;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.constants.StringConstants;
import com.kms.katalon.core.util.internal.ExceptionsUtil;

public class TestCaseFactory {
    private static final String TEST_CASE_META_ROOT_FOLDER_NAME = "Test Cases";

    private static final String TEST_CASE_ID_PREFIX = TEST_CASE_META_ROOT_FOLDER_NAME + ID_SEPARATOR;
    
    static final String TEMP_TEST_CASE_META_ROOT_FOLDER_NAME = "Libs";
    
    private static final String TEST_CASE_SCRIPT_ROOT_FOLDER_NAME = "Scripts";

    private static final String TEST_CASE_SCRIPT_ID_PREFIX = TEST_CASE_SCRIPT_ROOT_FOLDER_NAME + ID_SEPARATOR;

    private static final String TEST_CASE_SCRIPT_FILE_EXTENSION = "groovy";

    /* package */static final String TEST_CASE_META_FILE_EXTENSION = ".tc";

    private static final String DESCRIPTION_NODE_NAME = "description";

    private static final String TAG_NODE_NAME = "tag";

    private static final String VARIABLE_NODE_NAME = "variable";
    
    private static final String TEMP_NODE_NAME = "temp";

    private static final String VARIABLE_NAME_PROPERTY = "name";

    private static final String VARIABLE_DEFAULTVALUE_PROPERTY = "defaultValue";

    private static final String VARIABLE_MASKED_PROPERTY = "masked";

    /**
     * Finds {@link TestCase} by its id or relative id
     * 
     * @param testCaseRelativeId
     * Can be test case full id or test case relative id
     * </p>
     * Eg: Using "Test Cases/Sample Test Case" (full id) OR "Sample Test Case" (relative id) as
     * <code>testCaseRelativeId</code> is accepted for the test case with id "Test Cases/Sample Test Case"
     * @return an instance of {@link TestCase}
     * @throws IllegalArgumentException <code>testCaseRelativeId</code> is null or test case doesn't exist
     */
    public static TestCase findTestCase(final String testCaseRelativeId) throws IllegalArgumentException {
        if (testCaseRelativeId == null) {
            throw new IllegalArgumentException(StringConstants.TEST_CASE_FACTORY_MSG_ID_IS_NULL);
        }
        String testCaseId = getTestCaseId(testCaseRelativeId);
        File testCaseMetaFile = new File(getProjectDirPath() + File.separator + testCaseId
                + TEST_CASE_META_FILE_EXTENSION);
        if (testCaseMetaFile.exists()) {
            return readTestCase(testCaseId, testCaseMetaFile);
        }
        throw new IllegalArgumentException(MessageFormat.format(StringConstants.TEST_CASE_FACTORY_MSG_TC_NOT_EXISTED,
                testCaseId));
    }

    private static TestCase readTestCase(String testCaseId, File testCaseMetaFile) {
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(testCaseMetaFile);
            Element rootElement = document.getRootElement();
            TestCase testCase = new TestCase(testCaseId);
            testCase.setDescription(rootElement.element(DESCRIPTION_NODE_NAME).getText());
            testCase.setTag(rootElement.element(TAG_NODE_NAME).getText());
            List<Variable> variables = new ArrayList<Variable>();
            for (Object variableObject : rootElement.elements(VARIABLE_NODE_NAME)) {
                Element variableElement = (Element) variableObject;
                Variable variable = new Variable();
                variable.setName(variableElement.elementText(VARIABLE_NAME_PROPERTY));
                variable.setDefaultValue(variableElement.elementText(VARIABLE_DEFAULTVALUE_PROPERTY));
                String maskValueAsText = variableElement.elementText(VARIABLE_MASKED_PROPERTY);
                variable.setMasked(StringUtils.isNotEmpty(maskValueAsText) ? Boolean.valueOf(maskValueAsText)
                        : variable.isMasked());
                variables.add(variable);
            }
            testCase.setVariables(variables);
            return testCase;
        } catch (DocumentException e) {
            throw new IllegalArgumentException(MessageFormat.format(
                    StringConstants.TEST_CASE_FACTORY_MSG_TC_NOT_EXISTED_WITH_REASON, testCaseId,
                    ExceptionsUtil.getMessageForThrowable(e)));
        }
    }

    /**
     * Returns test case id of a its relative id.
     * 
     * @param testCaseRelativeId
     * Relative test object's id.
     * @return String of test object id, <code>null</code> if <code>testObjectRelativeId</code> is null.
     */
    public static String getTestCaseId(final String testCaseRelativeId) {
        if (testCaseRelativeId == null) {
            return testCaseRelativeId;
        }

        if (testCaseRelativeId.startsWith(TEST_CASE_ID_PREFIX)) {
            return testCaseRelativeId;
        }
        return TEST_CASE_ID_PREFIX + testCaseRelativeId;
    }

    /**
     * Returns relative id of a test case's id. The relative id is cut <code>"Test Cases/"</code> prefix from the test
     * case's id.
     * 
     * @param testCaseId
     * Full test case id.
     * @return String of test case relative id, <code>null</code> if <code>testCaseId</code> is null.
     */
    public static String getTestCaseRelativeId(final String testCaseId) {
        if (testCaseId == null) {
            return null;
        }
        return testCaseId.replaceFirst(TEST_CASE_ID_PREFIX, StringUtils.EMPTY);
    }

    /* package */static String getProjectDirPath() {
        return new File(RunConfiguration.getProjectDir()).getAbsolutePath();
    }

    /* package */static String getScriptPathByTestCaseId(String testCaseId) throws IOException {
        String testCaseScriptRelativePath = testCaseId.replaceFirst(TEST_CASE_ID_PREFIX, TEST_CASE_SCRIPT_ID_PREFIX)
                .replace(ID_SEPARATOR, File.separator);
        File testCaseScriptFolder = new File(getProjectDirPath(), testCaseScriptRelativePath);
        if (!testCaseScriptFolder.exists()) {
            return StringUtils.EMPTY;
        }

        File[] testCaseScriptFiles = testCaseScriptFolder.listFiles();
        if (testCaseScriptFiles == null) {
            return StringUtils.EMPTY;
        }

        for (File file : testCaseScriptFiles) {
            if (TEST_CASE_SCRIPT_FILE_EXTENSION.equals(FilenameUtils.getExtension(file.getName()))) {
                return file.getAbsolutePath();
            }
        }
        return StringUtils.EMPTY;
    }

    /* package */static String getScriptClassNameByTestCaseId(String testCaseId) throws IOException {
        return FilenameUtils.getBaseName(getScriptPathByTestCaseId(testCaseId));
    }
}
