package com.kms.katalon.core.testdata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InternalData extends AbstractTestData {

    private List<String[]> data;

    private List<String> columnNames;

    public InternalData(String fileSource, List<String[]> data, List<String> columnNames) {
        super(fileSource, true);
        this.data = data;
        this.columnNames = columnNames;
    }

    @Override
    public String internallyGetValue(String columnName, int rowIndex) throws IOException {
        return data.get(rowIndex)[getColumnIndex(columnName)];
    }

    @Override
    public String internallyGetValue(int columnIndex, int rowIndex) throws IOException {
        return data.get(rowIndex)[columnIndex];
    }

    private int getColumnIndex(String columnName) {
        String[] columnNames = getColumnNames();
        for (int i = 0; i < columnNames.length; i++) {
            if (columnNames[i].equals(columnName)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Get the type of the test data
     * 
     * @see TestDataType
     * @return type of test data
     */
    @Override
    public TestDataType getType() {
        return TestDataType.INTERNAL_DATA;
    }

    /**
     * Get all column names of the test data
     * 
     * @return an array that contains names of all columns
     * @throws IOException if any io errors happened
     */
    @Override
    public String[] getColumnNames() {
        return columnNames.toArray(new String[columnNames.size()]);
    }

    /**
     * Get total rows of the test data
     * 
     * @return total rows of the test data
     */
    @Override
    public int getRowNumbers() {
        return data.size();
    }

    /**
     * Get total column of the test data
     * 
     * @return total columns of the test data
     * @throws IOException if any io errors happened
     */
    @Override
    public int getColumnNumbers() {
        return columnNames.size();
    }

    public List<String[]> getData() {
        return data;
    }

    /**
     * Collect all data of this test data
     * 
     * @return a {@link List} that contains all data of each rows, which each item is a {@link List} of raw data from
     * each cell in that row
     * @throws IOException if any io errors happened
     */
    @Override
    public List<List<Object>> getAllData() {
        List<List<Object>> data = new ArrayList<List<Object>>();
        for (String[] row : getData()) {
            data.add(new ArrayList<Object>(Arrays.asList(row)));
        }
        return data;
    }

}
