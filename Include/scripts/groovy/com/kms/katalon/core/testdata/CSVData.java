package com.kms.katalon.core.testdata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.kms.katalon.core.testdata.reader.CSVReader;
import com.kms.katalon.core.testdata.reader.CSVSeparator;

public class CSVData extends AbstractTestData {

    private CSVReader reader;

    private CSVSeparator separator;

    public CSVData(String sourceUrl, boolean containsHeader, CSVSeparator separator) throws IOException {
        super(sourceUrl, containsHeader);
        this.separator = separator;
    }

    private CSVReader getReader() throws IOException {
        if (reader == null) {
            reader = new CSVReader(sourceUrl, separator, hasHeaders);
        }
        return reader;
    }

    @Override
    protected String internallyGetValue(String columnName, int rowIndex) throws IOException {
        return getReader().getData().get(rowIndex).get(getReader().getColumnIndex(columnName));
    }

    @Override
    protected String internallyGetValue(int columnIndex, int rowIndex) throws IOException {
        return getReader().getData().get(rowIndex).get(columnIndex);
    }

    /**
     * Get the type of the test data
     * 
     * @see TestDataType
     * @return type of test data
     */
    @Override
    public TestDataType getType() {
        return TestDataType.CSV_FILE;
    }

    /**
     * Get all column names of the test data
     * 
     * @return an array that contains names of all columns
     * @throws IOException if any io errors happened
     */
    @Override
    public String[] getColumnNames() throws IOException {
        if (getReader().getColumnNames() != null) {
            return getReader().getColumnNames();
        } else {
            return new String[0];
        }
    }

    /**
     * Get total rows of the test data
     * 
     * @return total rows of the test data
     */
    @Override
    public int getRowNumbers() throws IOException {
        return getReader().getData().size();
    }

    /**
     * Get total column of the test data
     * 
     * @return total columns of the test data
     * @throws IOException if any io errors happened
     */
    @Override
    public int getColumnNumbers() throws IOException {
        return getColumnNames().length;
    }

    /**
     * Set if this test data has headers
     * 
     * @param active true to set this test data to use headers
     * @throws IOException if any io errors happened
     */
    @Override
    public void activeHeaders(boolean active) throws IOException {
        super.activeHeaders(active);
        reader = null;
    }

    /**
     * Collect all data of this csv data with type as String
     * 
     * @return a {@link List} that contains all data of each rows, which each item is a {@link List} of String data from
     * each cell in that row
     * @throws IOException
     */
    public List<List<String>> getData() throws IOException {
        return getReader().getData();
    }

    /**
     * Collect all data of this test data
     * 
     * @return a {@link List} that contains all data of each rows, which each item is a {@link List} of raw data from
     * each cell in that row
     * @throws IOException if any io errors happened
     */
    @Override
    public List<List<Object>> getAllData() throws IOException {
        List<List<Object>> data = new ArrayList<List<Object>>();
        for (List<String> row : getData()) {
            data.add(new ArrayList<Object>(row));
        }
        return data;
    }

}
