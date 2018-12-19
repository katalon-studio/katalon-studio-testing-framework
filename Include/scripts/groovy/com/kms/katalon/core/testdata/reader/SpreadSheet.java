package com.kms.katalon.core.testdata.reader;

public interface SpreadSheet {

    String[] getSheetNames();
    
    void changeSheet(String sheetName);
}
