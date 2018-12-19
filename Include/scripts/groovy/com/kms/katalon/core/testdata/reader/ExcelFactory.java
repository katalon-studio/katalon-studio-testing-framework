package com.kms.katalon.core.testdata.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;

import com.kms.katalon.core.constants.StringConstants;
import com.kms.katalon.core.testdata.ExcelData;

public class ExcelFactory {

    private static ExcelProvider[] providers = new ExcelProvider[] { new WorbookProvider(), new HTMLTableProvider() };

    public static ExcelData getExcelDataWithDefaultSheet(String fullFilePath, String sheetName, boolean hasHeaders)
            throws IOException {
        ExcelData excelData = getExcelData(fullFilePath, hasHeaders);
        excelData.changeSheet(sheetName);
        return excelData;
    }

    public static ExcelData getExcelData(String fullFilePath, boolean hasHeaders) throws IOException {
        File inputFile = new File(fullFilePath);
        if (!inputFile.exists()) {
            throw new FileNotFoundException(fullFilePath);
        }

        ExcelData excel = null;
        for (ExcelProvider provider : providers) {
            try {
                excel = provider.getExcelData(fullFilePath, hasHeaders);
                if (excel != null) {
                    break;
                }
            } catch (Exception ex) {
                // continue
            }
        }

        if (excel == null) {
            throw new IllegalArgumentException(MessageFormat.format(StringConstants.UTIL_EXC_FILE_IS_UNSUPPORTED,
                    fullFilePath));
        }
        
        return excel;
    }
}