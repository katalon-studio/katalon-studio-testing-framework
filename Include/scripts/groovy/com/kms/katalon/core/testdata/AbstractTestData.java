package com.kms.katalon.core.testdata;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.kms.katalon.core.constants.StringConstants;

public abstract class AbstractTestData implements TestData {

    protected String sourceUrl;

    protected boolean hasHeaders;
    
    protected Map<String, String> properties;

    protected AbstractTestData(String sourceUrl, boolean hasHeaders) {
        this.sourceUrl = sourceUrl;
        this.hasHeaders = hasHeaders;
        this.properties = new HashMap<String, String>();
    }

    /**
     * Get the url of the data source
     * 
     * @return the url of the data source
     */
    @Override
    public final String getSourceUrl() {
        return sourceUrl;
    }

    /**
     * Check if this test data has headers
     * 
     * @return true if this test data uses headers; otherwise false
     * @throws IOException if any io errors happened
     */
    @Override
    public final boolean hasHeaders() {
        return hasHeaders;
    }

    protected void verifyRowIndex(int rowIndex) throws IOException {
        int rowNumber = getRowNumbers();
        if (rowIndex > rowNumber || rowIndex < BASE_INDEX) {
            throw new IllegalArgumentException(MessageFormat.format(
                    StringConstants.TD_ROW_INDEX_X_FOR_TEST_DATA_Y_INVALID, rowIndex, getSourceUrl(), rowNumber));
        }
    }

    protected void verifyColumnIndex(int columnIndex) throws IOException {
        int columnNumber = getColumnNumbers();
        if (columnIndex > columnNumber || columnIndex < BASE_INDEX) {
            throw new IllegalArgumentException(MessageFormat.format(
                    StringConstants.TD_COLUMN_INDEX_X_FOR_TEST_DATA_Y_INVALID, columnIndex, getSourceUrl(),
                    columnNumber));
        }
    }

    protected void verifyColumnName(String columnName) throws IOException {
        boolean isPresent = false;
        String[] columnNames = getColumnNames();
        for (String name : getColumnNames()) {
            if (name != null && name.equals(columnName)) {
                isPresent = true;
                break;
            }
        }
        if (!isPresent) {
            throw new IllegalArgumentException(MessageFormat.format(
                    StringConstants.TD_COLUMN_NAME_X_FOR_TEST_DATA_Y_INVALID, columnName, getSourceUrl(),
                    getAvailableColumnNames(columnNames)));
        }
    }

    /**
     * Get value (in String) of the test data at a cell with the column index and row index
     * 
     * @param columnIndex column index (column number), starting from 1
     * @param rowIndex row index (row number), starting from 1
     * @return value (in String) of the test data
     * @throws IOException if any io errors happened
     */
    @Override
    public final String getValue(int columnIndex, int rowIndex) throws IOException {
        return ObjectUtils.toString(getObjectValue(columnIndex, rowIndex));
    }

    /**
     * Get value (in String) of the test data at a cell with the column name and row index
     * 
     * @param columnName column name
     * @param rowIndex row index (row number), starting from 1
     * @return value (in String) of the test data
     * @throws IOException if any io errors happened
     */
    @Override
    public final String getValue(String columnName, int rowIndex) throws IOException {
        return ObjectUtils.toString(getObjectValue(columnName, rowIndex));
    }

    /**
     * Get raw value of the test data at a cell with the column index and row index
     * 
     * @param columnIndex index of the column, starting from 1
     * @param rowIndex row number
     * @return Object value
     * @throws IOException if any io errors happened
     */
    @Override
    public final Object getObjectValue(int columnIndex, int rowIndex) throws IOException {
        verifyColumnIndex(columnIndex);
        verifyRowIndex(rowIndex);
        return internallyGetValue(columnIndex - BASE_INDEX, rowIndex - BASE_INDEX);
    }

    /**
     * Get raw value of the test data at a cell with the column name and row index
     * 
     * @param columnName column name
     * @param rowIndex row index (row number), starting from 1
     * @return raw value of the test data
     * @throws IOException if any io errors happened
     */
    @Override
    public final Object getObjectValue(String columnName, int rowIndex) throws IOException {
        verifyRowIndex(rowIndex);
        verifyColumnName(columnName);
        return internallyGetValue(columnName, rowIndex - BASE_INDEX);
    }

    protected abstract Object internallyGetValue(int columnIndex, int rowIndex) throws IOException;

    protected abstract Object internallyGetValue(String columnName, int rowIndex) throws IOException;

    protected int getHeaderRowIdx() {
        return hasHeaders ? 1 : 0;
    }

    /**
     * Set if this test data has headers
     * 
     * @param active true to set this test data to use headers
     * @throws IOException if any io errors happened
     */
    @Override
    public void activeHeaders(boolean active) throws IOException {
        hasHeaders = active;
    }

    private String getAvailableColumnNames(String[] columnNames) {
        if (!hasHeaders || ArrayUtils.isEmpty(columnNames)) {
            return "{}";
        }
        List<String> validColumNames = new ArrayList<String>();
        for (String columnName : columnNames) {
            if (StringUtils.isNotEmpty(columnName)) {
                validColumNames.add(columnName);
            }
        }
        return ArrayUtils.toString(validColumNames.toArray(new String[validColumNames.size()]));
    }
    
    @Override
    public TestDataInfo getDataInfo() {
        return null;
    }
    
    @Override 
    public String getProperty(String key) {
        return properties.get(key);
    }
    
    @Override
    public void setProperty(String key, String value) {
        properties.put(key, value);
    }
}
