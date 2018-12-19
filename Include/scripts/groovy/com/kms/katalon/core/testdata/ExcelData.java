package com.kms.katalon.core.testdata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.kms.katalon.core.testdata.reader.SpreadSheet;

public abstract class ExcelData extends AbstractTestData implements SpreadSheet {

    public ExcelData(String sourceUrl, boolean hasHeaders) throws IOException {
        super(sourceUrl, hasHeaders);
    }

    /**
     * Get the max column of a row
     * 
     * @param rowIndex the row index
     * @return the max column of a row, or -1 if the row index is invalid
     * @throws IOException
     */
    public abstract int getMaxColumn(int rowIndex) throws IOException;

    /**
     * Get the type of the test data
     * 
     * @see TestDataType
     * @return type of test data
     */
    @Override
    public final TestDataType getType() {
        return TestDataType.EXCEL_FILE;
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
        int colNum = getColumnNumbers();
        int rowNum = getRowNumbers();
        for (int y = BASE_INDEX; y <= rowNum; y++) {
            List<Object> row = new ArrayList<Object>();
            for (int x = BASE_INDEX; x <= colNum; x++) {
                row.add(getObjectValue(x, y));
            }
            data.add(row);
        }
        return data;
    }

    /**
     * Get the sheet name of this excel data
     * 
     * @return the sheet name
     */
    public abstract String getSheetName();
}
