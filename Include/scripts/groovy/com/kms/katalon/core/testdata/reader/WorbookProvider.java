package com.kms.katalon.core.testdata.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.kms.katalon.core.testdata.ExcelData;

public class WorbookProvider implements ExcelProvider {
    @Override
    public ExcelData getExcelData(String sourceUrl, boolean hasHeaders) throws IOException {
        File excelFile = new File(sourceUrl);
        if (!excelFile.exists()) {
            throw new FileNotFoundException(excelFile.toString());
        }
        
        FileInputStream fis = null;
        try {
        	fis = new FileInputStream(excelFile);
            return new SheetPOI(sourceUrl, hasHeaders, WorkbookFactory.create(fis));
        } catch (InvalidFormatException e) {
            return null;
        } finally {
        	if (fis != null) {
        		fis.close();
        	}
        }
    }
}
