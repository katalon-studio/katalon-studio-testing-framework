package com.kms.katalon.core.testdata.reader;

import java.io.IOException;

import com.kms.katalon.core.testdata.ExcelData;

public class HTMLTableProvider implements ExcelProvider {
    @Override
    public ExcelData getExcelData(String sourceUrl, boolean hasHeaders) throws IOException {

        HTMLTableData tableData = new HTMLTableData(sourceUrl, hasHeaders);
        return (tableData.getInternalData() != null) ? tableData : null;
    }
}
