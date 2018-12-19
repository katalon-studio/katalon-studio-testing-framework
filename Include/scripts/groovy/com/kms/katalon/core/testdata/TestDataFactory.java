package com.kms.katalon.core.testdata;

import static com.kms.katalon.core.constants.StringConstants.ID_SEPARATOR;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.constants.StringConstants;
import com.kms.katalon.core.db.DatabaseConnection;
import com.kms.katalon.core.db.DatabaseSettings;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.testdata.reader.CSVSeparator;
import com.kms.katalon.core.testdata.reader.ExcelFactory;
import com.kms.katalon.core.util.internal.Base64;
import com.kms.katalon.core.util.internal.ExceptionsUtil;
import com.kms.katalon.core.util.internal.PathUtil;

public class TestDataFactory {

    private static final KeywordLogger logger = KeywordLogger.getInstance(TestDataFactory.class);

    private static final String TEST_DATA_ROOT_FOLDER_NAME = "Data Files";

    private static final String TEST_DATA_ID_PREFIX = TEST_DATA_ROOT_FOLDER_NAME + ID_SEPARATOR;

    private static final String TEST_DATA_FILE_EXTENSION = ".dat";

    private static final String DRIVER_NODE = "driver";

    private static final String URL_NODE = "dataSourceUrl";

    private static final String SHEET_NAME_NODE = "sheetName";

    private static final String DATA_NODE = "data";

    private static final String INTERNAL_DATA_NODE = "internalDataColumns";

    private static final String INTERNAL_DATA_COLUMN_NAME_ATTRIBUTE = "name";

    private static final String CONTAINS_HEADERS_NODE = "containsHeaders";

    private static final String CSV_SEPERATOR_NODE = "csvSeperator";

    private static final String IS_RELATIVE_PATH_NODE = "isInternalPath";

    private static final String ENCODER_CHARSET = "utf-8";

    private static final String NODE_GLOBAL_DB_SETTING = "usingGlobalDBSetting";

    private static final String NODE_SECURE_USER_ACCOUNT = "secureUserAccount";

    private static final String NODE_USER = "user";

    private static final String NODE_PASSWORD = "password";

    private static final String NODE_SQL_QUERY = "query";

    /**
     * Returns test data id of a its relative id.
     * 
     * @param testDataRelativeId
     * Relative test data's id.
     * @return String of test data id, <code>null</code> if <code>testDataRelativeId</code> is null.
     */
    public static String getTestDataId(final String testDataRelativeId) {
        if (testDataRelativeId == null) {
            return testDataRelativeId;
        }

        if (testDataRelativeId.startsWith(TEST_DATA_ID_PREFIX)) {
            return testDataRelativeId;
        }
        return TEST_DATA_ID_PREFIX + testDataRelativeId;
    }

    /**
     * Returns relative id of a test data's id. The relative id is cut <code>"Data Files/"</code> prefix from the test
     * data's id.
     * 
     * @param testDataId
     * Full test data id.
     * @return String of test data relative id, <code>null</code> if <code>testDataId</code> is null.
     */
    public static String getTestDataRelativeId(final String testDataRelativeId) {
        if (testDataRelativeId == null) {
            return null;
        }
        return testDataRelativeId.replaceFirst(TEST_DATA_ID_PREFIX, StringUtils.EMPTY);
    }

    /**
     * Finds {@link TestData} by its id or relative id
     * 
     * @param testDataRelativeId
     * Can be test data full id or test data relative id
     * </p>
     * Eg: Using "Data Files/Sample Test Data" (full id) OR "Sample Test Data" (relative id) as
     * <code>testDataRelativeId</code> is accepted for the test data with id "Data Files/Sample Test Case"
     * @return an instance of {@link TestData}
     * @throws IllegalArgumentException <code>testCaseRelativeId</code> is null or test data doesn't exist
     */
    public static TestData findTestData(String testDataRelativeId) {
        logger.logDebug(StringConstants.XML_LOG_TEST_DATA_CHECKING_TEST_DATA_ID);
        if (testDataRelativeId == null) {
            throw new IllegalArgumentException(StringConstants.XML_LOG_ERROR_TEST_DATA_NULL_TEST_DATA_ID);
        }
        String testDataId = getTestDataId(testDataRelativeId);
        try {
            return internallyfindTestData(getProjectDir(), testDataId);
        } catch (Exception e) {
            throw new IllegalArgumentException(MessageFormat.format(
                    StringConstants.XML_LOG_ERROR_TEST_DATA_CANNOT_FIND_TEST_DATA_X_BECAUSE_OF_Y, testDataId,
                    ExceptionsUtil.getMessageForThrowable(e)));
        }
    }

    private static TestData internallyfindTestData(String projectDir, String testDataId) throws Exception {
        logger.logDebug(MessageFormat.format(StringConstants.XML_LOG_TEST_DATA_FINDING_TEST_DATA_WITH_ID_X, testDataId));
        File dataFile = new File(projectDir, testDataId + TEST_DATA_FILE_EXTENSION);
        if (dataFile.exists()) {
            SAXReader reader = new SAXReader();
            Document document = reader.read(dataFile);
            Element testDataElement = document.getRootElement();

            String driverName = testDataElement.elementText(DRIVER_NODE);
            switch (TestDataType.fromValue(driverName)) {
                case EXCEL_FILE:
                    logger.logDebug(StringConstants.XML_LOG_TEST_DATA_READING_EXCEL_DATA);
                    return readExcelData(testDataElement, projectDir);
                case INTERNAL_DATA:
                    logger.logDebug(StringConstants.XML_LOG_TEST_DATA_READING_INTERNAL_DATA);
                    return readInternalData(testDataElement, projectDir, dataFile);
                case CSV_FILE:
                    logger.logDebug(StringConstants.XML_LOG_TEST_DATA_READING_CSV_DATA);
                    return readCSVData(testDataElement, projectDir);
                case DB_DATA:
                    logger.logDebug(StringConstants.XML_LOG_TEST_DATA_READING_DB_DATA);
                    return readDBData(testDataElement, projectDir);
                default:
                    return null;
            }
        }
        throw new IllegalArgumentException(MessageFormat.format(StringConstants.XML_LOG_ERROR_TEST_DATA_X_NOT_EXISTS,
                testDataId));
    }

    private static String getProjectDir() {
        return new File(RunConfiguration.getProjectDir()).getAbsolutePath();
    }

    public static TestData findTestDataForExternalBundleCaller(String testDataId, String projectDir) throws Exception {
        return internallyfindTestData(projectDir, testDataId);
    }

    public static TestData readExcelData(Element testDataElement, String projectDir) throws Exception {
        if (testDataElement.element(URL_NODE) == null) {
            throw new IllegalArgumentException(MessageFormat.format(
                    StringConstants.XML_LOG_ERROR_TEST_DATA_MISSING_ELEMENT, URL_NODE));
        }
        if (testDataElement.element(SHEET_NAME_NODE) == null) {
            throw new IllegalArgumentException(MessageFormat.format(
                    StringConstants.XML_LOG_ERROR_TEST_DATA_MISSING_ELEMENT, SHEET_NAME_NODE));
        }
        boolean hasHeaders = true;
        if (testDataElement.element(CONTAINS_HEADERS_NODE) != null) {
            hasHeaders = Boolean.parseBoolean(testDataElement.elementText(CONTAINS_HEADERS_NODE));
        }
        String sourceUrl = testDataElement.elementText(URL_NODE);
        String sheetName = testDataElement.elementText(SHEET_NAME_NODE);
        Boolean isRelativePath = false;
        if (testDataElement.element(IS_RELATIVE_PATH_NODE) != null) {
            isRelativePath = Boolean.valueOf(testDataElement.elementText(IS_RELATIVE_PATH_NODE));
        }
        if (isRelativePath) {
            sourceUrl = PathUtil.relativeToAbsolutePath(sourceUrl, projectDir);
        }
        logger.logDebug(MessageFormat.format(StringConstants.XML_LOG_TEST_DATA_READING_EXCEL_DATA_WITH_SOURCE_X_SHEET_Y,
                sourceUrl, sheetName));
        return ExcelFactory.getExcelDataWithDefaultSheet(sourceUrl, sheetName, hasHeaders);
    }

    public static TestData readInternalData(Element testDataElement, String projectDir, File dataFile)
            throws UnsupportedEncodingException {
        List<String> columnNames = new ArrayList<String>();
        List<String[]> data = new ArrayList<String[]>();
        for (Object internalDataColumnElementObject : testDataElement.elements(INTERNAL_DATA_NODE)) {
            Element internalDataColumnElement = (Element) internalDataColumnElementObject;
            // columnNames.add(internalDataColumnElement.attributeValue(INTERNAL_DATA_COLUMN_NAME_ATTRIBUTE));
            columnNames.add(internalDataColumnElement.element(INTERNAL_DATA_COLUMN_NAME_ATTRIBUTE).getText());
        }

        for (Object dataElementObject : testDataElement.elements(DATA_NODE)) {
            Element dataElement = (Element) dataElementObject;
            String[] rowRawData = dataElement.getText().split(" ");
            String[] rowData = new String[rowRawData.length];
            for (int columnIndex = 0; columnIndex < rowRawData.length; columnIndex++) {
                String decodeValue = URLDecoder.decode(rowRawData[columnIndex].toString(), ENCODER_CHARSET);
                if (!decodeValue.equals("null")) {
                    rowData[columnIndex] = decodeValue;
                } else {
                    rowData[columnIndex] = "";
                }
            }
            data.add(rowData);
        }
        return new InternalData(dataFile.getAbsolutePath(), data, columnNames);
    }

    public static TestData readCSVData(Element testDataElement, String projectDir) throws Exception {
        if (testDataElement.element(URL_NODE) == null) {
            throw new IllegalArgumentException(MessageFormat.format(
                    StringConstants.XML_LOG_ERROR_TEST_DATA_MISSING_ELEMENT, URL_NODE));
        }
        if (testDataElement.element(CONTAINS_HEADERS_NODE) == null) {
            throw new IllegalArgumentException(MessageFormat.format(
                    StringConstants.XML_LOG_ERROR_TEST_DATA_MISSING_ELEMENT, CONTAINS_HEADERS_NODE));
        }
        if (testDataElement.element(CSV_SEPERATOR_NODE) == null) {
            throw new IllegalArgumentException(MessageFormat.format(
                    StringConstants.XML_LOG_ERROR_TEST_DATA_MISSING_ELEMENT, CSV_SEPERATOR_NODE));
        }
        String sourceUrl = testDataElement.element(URL_NODE).getText();
        boolean containsHeader = Boolean.valueOf(testDataElement.element(CONTAINS_HEADERS_NODE).getText());
        CSVSeparator seperator = CSVSeparator.fromValue(testDataElement.element(CSV_SEPERATOR_NODE).getText());
        Boolean isRelativePath = false;
        if (testDataElement.element(IS_RELATIVE_PATH_NODE) != null) {
            isRelativePath = Boolean.valueOf(testDataElement.elementText(IS_RELATIVE_PATH_NODE));
        }

        if (isRelativePath) {
            sourceUrl = PathUtil.relativeToAbsolutePath(sourceUrl, projectDir);
        }

        logger.logDebug(MessageFormat.format(
                StringConstants.XML_LOG_TEST_DATA_READING_CSV_DATA_WITH_SOURCE_X_SEPERATOR_Y_AND_Z,
                seperator.toString(), containsHeader ? "containing header" : "not containing header"));
        return new CSVData(sourceUrl, containsHeader, seperator);
    }

    public static TestData readDBData(Element testDataElement, String projectDir) throws Exception {
        validateTestDataElement(testDataElement, NODE_SQL_QUERY);
        validateTestDataElement(testDataElement, NODE_GLOBAL_DB_SETTING);
        validateTestDataElement(testDataElement, NODE_SECURE_USER_ACCOUNT);
        validateTestDataElement(testDataElement, URL_NODE);

        String query = testDataElement.element(NODE_SQL_QUERY).getText();
        if (StringUtils.isBlank(query)) {
            throw new IllegalArgumentException(StringConstants.XML_ERROR_TEST_DATA_SQL_QUERY_IS_BLANK);
        }

        boolean usingGlobalDBSetting = Boolean.parseBoolean(testDataElement.element(NODE_GLOBAL_DB_SETTING).getText());
        if (usingGlobalDBSetting) {
            DatabaseConnection dbConnection = new DatabaseSettings(projectDir).getDatabaseConnection();
            if (dbConnection == null) {
                throw new IllegalArgumentException(MessageFormat.format(
                        StringConstants.XML_ERROR_TEST_DATA_CONNECTION_IS_NULL,
                        StringConstants.XML_ERROR_TEST_DATA_CONNECTION_URL_IS_BLANK));
            }
            return readDBData(dbConnection, query);
        }

        boolean secureUserAccount = Boolean.parseBoolean(testDataElement.element(NODE_SECURE_USER_ACCOUNT).getText());
        String sourceUrl = testDataElement.element(URL_NODE).getText();
        String user = null;
        String password = null;

        if (StringUtils.isBlank(sourceUrl)) {
            throw new IllegalArgumentException(StringConstants.XML_ERROR_TEST_DATA_CONNECTION_URL_IS_BLANK);
        }

        if (secureUserAccount) {
            validateTestDataElement(testDataElement, NODE_USER);
            validateTestDataElement(testDataElement, NODE_PASSWORD);
            user = testDataElement.element(NODE_USER).getText();
            // decrypt password before use
            password = Base64.decode(testDataElement.element(NODE_PASSWORD).getText());
        }

        return readDBData(new DatabaseConnection(sourceUrl, user, password), query);
    }

    private static TestData readDBData(DatabaseConnection dbConnection, String query) throws SQLException {
        logger.logDebug(MessageFormat.format(StringConstants.XML_LOG_TEST_DATA_READING_DB_DATA_WITH_QUERY_X, query));
        return new DBData(dbConnection, query);
    }

    private static void validateTestDataElement(Element testDataElement, String element) {
        if (testDataElement.element(element) == null) {
            throw new IllegalArgumentException(MessageFormat.format(
                    StringConstants.XML_LOG_ERROR_TEST_DATA_MISSING_ELEMENT, element));
        }
    }
}
