package com.kms.katalon.core.testdata.reader;

import com.kms.katalon.core.testdata.ExcelData;

public interface ExcelProvider {
    ExcelData getExcelData(String sourceUrl, boolean hasHeaders) throws Exception;
}
