package com.kms.katalon.core.testdata;

import java.io.IOException;
import java.util.List;

public interface TestData {

    int BASE_INDEX = 1;

    /**
     * Get value (in String) of the test data at a cell with the column name and row index
     * 
     * @param columnName column name
     * @param rowIndex row index (row number), starting from 1
     * @return value (in String) of the test data
     * @throws IOException if any io errors happened
     */
    String getValue(String columnName, int rowIndex) throws IOException;

    /**
     * Get value (in String) of the test data at a cell with the column index and row index
     * 
     * @param columnIndex column index (column number), starting from 1
     * @param rowIndex row index (row number), starting from 1
     * @return value (in String) of the test data
     * @throws IOException if any io errors happened
     */
    String getValue(int columnIndex, int rowIndex) throws IOException;

    /**
     * Get raw value of the test data at a cell with the column name and row index
     * 
     * @param columnName column name
     * @param rowIndex row index (row number), starting from 1
     * @return raw value of the test data
     * @throws IOException if any io errors happened
     */
    Object getObjectValue(String columnName, int rowIndex) throws IOException;

    /**
     * Get raw value of the test data at a cell with the column index and row index
     * 
     * @param columnIndex index of the column, starting from 1
     * @param rowIndex row number
     * @return Object value
     * @throws IOException if any io errors happened
     */
    Object getObjectValue(int columnIndex, int rowIndex) throws IOException;

    /**
     * Get the type of the test data
     * 
     * @see TestDataType
     * @return type of test data
     */
    TestDataType getType();

    /**
     * Get the url of the data source
     * 
     * @return the url of the data source
     */
    String getSourceUrl();

    /**
     * Get all column names of the test data
     * 
     * @return an array that contains names of all columns
     * @throws IOException if any io errors happened
     */
    String[] getColumnNames() throws IOException;

    /**
     * Get total rows of the test data
     * 
     * @return total rows of the test data
     */
    int getRowNumbers() throws IOException;

    /**
     * Get total column of the test data
     * 
     * @return total columns of the test data
     * @throws IOException if any io errors happened
     */
    int getColumnNumbers() throws IOException;

    /**
     * Check if this test data has headers
     * 
     * @return true if this test data uses headers; otherwise false
     * @throws IOException if any io errors happened
     */
    boolean hasHeaders();

    /**
     * Set if this test data has headers
     * 
     * @param active true to set this test data to use headers
     * @throws IOException if any io errors happened
     */
    void activeHeaders(boolean active) throws IOException;

    /**
     * Used for logging
     * 
     * @return the information of the current test data, can be null if not implemented.
     */
    TestDataInfo getDataInfo();

    /**
     * Collect all data of this test data
     * 
     * @return a {@link List} that contains all data of each rows, which each item is a {@link List} of raw data from
     * each cell in that row
     * @throws IOException if any io errors happened
     */
    List<List<Object>> getAllData() throws IOException;
    
    /**
     * Used to retrieve custom properties relating to a test data.
     * Implementing classes should initialize their own maps and 
     * handle the set/get themselves
     * @param key a {@link String} that represents the key into the map
     * @return An {@link Object} indexed by key, or null if not exists
     */
    String getProperty(String key);
    
    /**
     * Used to retrieve custom properties relating to a test data.
     * Implementing classes should initialize their own maps and 
     * handle the set/get themselves
     * @param key a {@link String} that represents the key into the map
     * @param value an {@link Object} that associates with the key
     */
    void setProperty(String key, String value);
}
