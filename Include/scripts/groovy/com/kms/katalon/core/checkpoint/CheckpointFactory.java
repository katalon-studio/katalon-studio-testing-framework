package com.kms.katalon.core.checkpoint;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.constants.StringConstants;
import com.kms.katalon.core.db.DatabaseConnection;
import com.kms.katalon.core.db.DatabaseSettings;
import com.kms.katalon.core.exception.StepFailedException;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.testdata.CSVData;
import com.kms.katalon.core.testdata.DBData;
import com.kms.katalon.core.testdata.ExcelData;
import com.kms.katalon.core.testdata.TestData;
import com.kms.katalon.core.testdata.TestDataFactory;
import com.kms.katalon.core.testdata.TestDataType;
import com.kms.katalon.core.testdata.reader.CSVSeparator;
import com.kms.katalon.core.testdata.reader.ExcelFactory;
import com.kms.katalon.core.util.internal.Base64;
import com.kms.katalon.core.util.internal.ExceptionsUtil;
import com.kms.katalon.core.util.internal.JsonUtil;
import com.kms.katalon.core.util.internal.PathUtil;

public class CheckpointFactory {

    private static final String TAKEN_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    private static final String CHECKPOINT_FILE_EXTENSION = ".cpt";

    private static final String NODE_IS_FROM_TEST_DATA = "is-from-test-data";

    private static final String NODE_SOURCE_INFO = "source-info";

    private static final String NODE_DB_SOURCE_INFO = "db-source-info";

    private static final String NODE_EXCEL_SOURCE_INFO = "excel-source-info";

    private static final String NODE_CSV_SOURCE_INFO = "csv-source-info";

    private static final String NODE_DATA = "checkpoint-data";

    private static final String NODE_TAKEN_DATE = "taken-date";

    private static final String NODE_SOURCE_URL = "source-url";

    private static final String NODE_SOURCE_TYPE = "source-type";

    private static final String NODE_SHEETNAME_SEPARATOR = "sheet-name-or-separator";

    private static final String NODE_USING_RELAITVE_PATH = "is-using-relative-path";

    private static final String NODE_USING_FIRST_ROW_AS_HEADER = "is-using-first-row-as-header";

    private static final String NODE_USING_GLOBAL_DB_SETTING = "is-using-global-db-setting";

    private static final String NODE_SECURE_USER_ACCOUNT = "is-secure-user-account";

    private static final String NODE_DB_USER = "user";

    private static final String NODE_DB_PASSWORD = "password";

    private static final String NODE_DB_QUERY = "query";

    private static final String TEST_DATA = "Test Data";

    private static final KeywordLogger logger = KeywordLogger.getInstance(CheckpointFactory.class);

    private static final String[] SUPPORTED_TEST_DATA_TYPES = new String[] { TestDataType.EXCEL_FILE.toString(),
            TestDataType.CSV_FILE.toString(), TestDataType.DB_DATA.toString() };

    /**
     * Finds {@link Checkpoint} by its id
     * 
     * @param checkpointId
     * Must be checkpoint full id
     * </p>
     * Eg: Using "Checkpoints/Sample Checkpoint" (full id)
     * <code>checkpointRelativeId</code> "Sample Checkpoint" is not accepted
     * @return an instance of {@link Checkpoint}
     * @throws IllegalArgumentException 
     *  <code>checkpointId</code> is null, 
     *  checkpoint doesn't exist or 
     *  checkpoint is missing element(s)
     */
    public static Checkpoint findCheckpoint(String checkpointId) throws IllegalArgumentException {
        if (checkpointId == null) {
            throw new IllegalArgumentException(StringConstants.EXC_MSG_CHECKPOINT_ID_IS_NULL);
        }
        try {
            File checkpointFile = new File(getProjectDir() + File.separator + checkpointId + CHECKPOINT_FILE_EXTENSION);
            logger.logDebug(MessageFormat.format(StringConstants.INFO_MSG_FINDING_CHECKPOINT_WITH_ID, checkpointId));
            if (checkpointFile == null || !checkpointFile.exists()) {
                throw new IllegalArgumentException(StringConstants.EXC_MSG_CHECKPOINT_DOES_NOT_EXIST);
            }

            SAXReader reader = new SAXReader();
            Document document = reader.read(checkpointFile);
            Element checkpointElement = document.getRootElement();

            Element sourceInfoElement = null;
            if (checkpointElement.element(NODE_SOURCE_INFO) != null) {
                sourceInfoElement = checkpointElement.element(NODE_SOURCE_INFO);
            } else if (checkpointElement.element(NODE_DB_SOURCE_INFO) != null) {
                sourceInfoElement = checkpointElement.element(NODE_DB_SOURCE_INFO);
            } else if (checkpointElement.element(NODE_EXCEL_SOURCE_INFO) != null) {
                sourceInfoElement = checkpointElement.element(NODE_EXCEL_SOURCE_INFO);
            } else if (checkpointElement.element(NODE_CSV_SOURCE_INFO) != null) {
                sourceInfoElement = checkpointElement.element(NODE_CSV_SOURCE_INFO);
            } else {
                throw new IllegalArgumentException(
                        MessageFormat.format(StringConstants.EXC_MSG_CHECKPOINT_IS_MISSING_ELEMENT,
                                StringUtils.join(new String[] { NODE_SOURCE_INFO, NODE_DB_SOURCE_INFO,
                                        NODE_EXCEL_SOURCE_INFO, NODE_CSV_SOURCE_INFO }, " or ")));
            }

            validateElementName(checkpointElement, NODE_DATA);
            validateElementName(checkpointElement, NODE_TAKEN_DATE);
            validateElementName(sourceInfoElement, NODE_IS_FROM_TEST_DATA);
            validateElementName(sourceInfoElement, NODE_SOURCE_URL);

            boolean isFromTestData = Boolean.parseBoolean(sourceInfoElement.element(NODE_IS_FROM_TEST_DATA).getText());
            String sourceUrl = sourceInfoElement.element(NODE_SOURCE_URL).getText();
            Date takenDate = new SimpleDateFormat(TAKEN_DATE_FORMAT)
                    .parse(checkpointElement.element(NODE_TAKEN_DATE).getText());

            Checkpoint checkpoint = new Checkpoint(checkpointId);
            checkpoint.setTakenDate(takenDate);

            String jsonData = checkpointElement.element(NODE_DATA).getText();
            if (StringUtils.isNotBlank(jsonData)) {
                List<List<CheckpointCell>> checkpointData = JsonUtil.fromJson(jsonData,
                        Checkpoint.CHECKPOINT_DATA_TYPE);
                checkpoint.setCheckpointData(checkpointData);
            }

            // source data is from Test Data
            if (isFromTestData) {
                logger.logDebug(MessageFormat.format(StringConstants.INFO_MSG_CHECKPOINT_HAS_SOURCE_FROM_X, TEST_DATA));
                return updateSourceData(checkpoint, getTestData(sourceUrl));
            }

            // otherwise, external data source will be used
            validateElementName(sourceInfoElement, NODE_SOURCE_TYPE);
            String sourceType = sourceInfoElement.element(NODE_SOURCE_TYPE).getText();
            if (!ArrayUtils.contains(SUPPORTED_TEST_DATA_TYPES, sourceType)) {
                throw new IllegalArgumentException(
                        MessageFormat.format(StringConstants.EXC_MSG_CHECKPOINT_INVALID_SOURCE_TYPE, sourceType));
            }

            TestDataType testDataSourceType = TestDataType.fromValue(sourceType);
            logger.logDebug(MessageFormat.format(StringConstants.INFO_MSG_CHECKPOINT_HAS_SOURCE_FROM_X,
                    testDataSourceType.toString()));

            // source data is from Database
            if (testDataSourceType == TestDataType.DB_DATA) {
                return updateSourceData(checkpoint, getDBData(sourceInfoElement));
            }

            validateElementName(sourceInfoElement, NODE_USING_FIRST_ROW_AS_HEADER);
            validateElementName(sourceInfoElement, NODE_SHEETNAME_SEPARATOR);
            validateElementName(sourceInfoElement, NODE_USING_RELAITVE_PATH);

            boolean isUsingFirstRowAsHeader = Boolean
                    .parseBoolean(sourceInfoElement.element(NODE_USING_FIRST_ROW_AS_HEADER).getText());
            String sheetNameOrSeparator = sourceInfoElement.element(NODE_SHEETNAME_SEPARATOR).getText();
            boolean isUsingRelativePath = Boolean
                    .parseBoolean(sourceInfoElement.element(NODE_USING_RELAITVE_PATH).getText());
            sourceUrl = getSourcePath(sourceUrl, isUsingRelativePath);

            // source data is from Excel
            if (testDataSourceType == TestDataType.EXCEL_FILE) {
                return updateSourceData(checkpoint,
                        getExcelData(sourceUrl, sheetNameOrSeparator, isUsingFirstRowAsHeader));
            }

            // source data is from CSV
            if (testDataSourceType == TestDataType.CSV_FILE) {
                return updateSourceData(checkpoint,
                        getCSVData(sourceUrl, sheetNameOrSeparator, isUsingFirstRowAsHeader));
            }

            // This won't happen
            return null;
        } catch (Exception e) {
            throw new StepFailedException(
                    MessageFormat.format(StringConstants.EXC_MSG_CANNOT_FIND_CHECKPOINT_WITH_ID_ROOT_CAUSE,
                            checkpointId, ExceptionsUtil.getMessageForThrowable(e)));
        }
    }

    private static String getSourcePath(String sourceUrl, boolean isRelativePath) {
        if (isRelativePath) {
            return PathUtil.relativeToAbsolutePath(sourceUrl, getProjectDir());
        }
        return sourceUrl;
    }

    private static Checkpoint updateSourceData(Checkpoint checkpoint, List<List<Object>> sourceData) {
        checkpoint.setSourceData(sourceData);
        return checkpoint;
    }

    private static List<List<Object>> getTestData(String testDataId) throws Exception {
        TestData testData = TestDataFactory.findTestData(testDataId);
        if (testData == null) {
            throw new IllegalArgumentException(
                    MessageFormat.format(StringConstants.EXC_MSG_NOT_FOUND_TEST_DATA_WITH_ID, testDataId));
        }
        return testData.getAllData();
    }

    private static List<List<Object>> getDBData(Element sourceInfoElement) throws Exception {
        validateElementName(sourceInfoElement, NODE_DB_QUERY);
        String query = sourceInfoElement.element(NODE_DB_QUERY).getText();
        if (StringUtils.isBlank(query)) {
            throw new IllegalArgumentException(StringConstants.XML_ERROR_TEST_DATA_SQL_QUERY_IS_BLANK);
        }
        DatabaseConnection dbConnection = getDatabaseConnection(sourceInfoElement);
        if (dbConnection == null) {
            throw new IllegalArgumentException(StringConstants.EXC_MSG_DB_CONNECTION_SETTIGNS_ARE_EMPTY);
        }
        DBData dbData = new DBData(dbConnection, query);
        return dbData.getAllData();
    }

    private static List<List<Object>> getExcelData(String sourceUrl, String sheetNameOrSeparator,
            boolean isUsingFirstRowAsHeader) throws IOException {
        if (StringUtils.isEmpty(sheetNameOrSeparator)) {
            throw new IllegalArgumentException(StringConstants.EXC_MSG_EXCEL_SHEET_NAME_IS_EMPTY);
        }
        ExcelData excelData = ExcelFactory.getExcelDataWithDefaultSheet(sourceUrl, sheetNameOrSeparator,
                isUsingFirstRowAsHeader);
        return excelData.getAllData();
    }

    private static List<List<Object>> getCSVData(String sourceUrl, String separator, boolean isUsingFirstRowAsHeader)
            throws IOException {
        if (!ArrayUtils.contains(CSVSeparator.stringValues(), separator)) {
            throw new IllegalArgumentException(StringConstants.EXC_MSG_INVALID_CSV_SEPARATOR);
        }
        CSVData csvData = new CSVData(sourceUrl, isUsingFirstRowAsHeader, CSVSeparator.fromValue(separator));
        return csvData.getAllData();
    }

    private static String getProjectDir() {
        return new File(RunConfiguration.getProjectDir()).getAbsolutePath();
    }

    private static void validateElementName(Element element, String elementName) {
        if (element.element(elementName) == null) {
            throw new IllegalArgumentException(
                    MessageFormat.format(StringConstants.EXC_MSG_CHECKPOINT_IS_MISSING_ELEMENT, elementName));
        }
    }

    private static DatabaseConnection getDatabaseConnection(Element sourceInfoElement) throws Exception {
        validateElementName(sourceInfoElement, NODE_USING_GLOBAL_DB_SETTING);
        boolean isUsingGlobalDBSetting = Boolean
                .parseBoolean(sourceInfoElement.element(NODE_USING_GLOBAL_DB_SETTING).getText());

        if (isUsingGlobalDBSetting) {
            return new DatabaseSettings(getProjectDir()).getDatabaseConnection();
        }

        validateElementName(sourceInfoElement, NODE_SECURE_USER_ACCOUNT);
        boolean isSecureUserAccount = Boolean
                .parseBoolean(sourceInfoElement.element(NODE_SECURE_USER_ACCOUNT).getText());

        String user = null;
        String password = null;
        if (isSecureUserAccount) {
            validateElementName(sourceInfoElement, NODE_DB_USER);
            validateElementName(sourceInfoElement, NODE_DB_PASSWORD);

            user = sourceInfoElement.element(NODE_DB_USER).getText();
            password = Base64.decode(sourceInfoElement.element(NODE_DB_PASSWORD).getText());
        }

        return new DatabaseConnection(sourceInfoElement.element(NODE_SOURCE_URL).getText(), user, password);
    }
}
