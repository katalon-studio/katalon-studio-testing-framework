package com.kms.katalon.core.testdata.reader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

public class CSVReader {
    private ICsvListReader listReader;
    private List<List<String>> data;
    private String[] columnNames;
    private boolean containsHeader;

    public CSVReader(String sourceUrl, CSVSeparator separator, boolean containHeader) throws IOException {
        try {
            this.containsHeader = containHeader;
            FileReader reader = new FileReader(new File(sourceUrl));
            switch (separator) {
            case COMMA:
                listReader = new CsvListReader(reader, CsvPreference.STANDARD_PREFERENCE);
                break;
            case SEMICOLON:
                listReader = new CsvListReader(reader, CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
                break;
            case TAB:
                listReader = new CsvListReader(reader, CsvPreference.TAB_PREFERENCE);
                break;
            }
            if (containHeader) {
                columnNames = listReader.getHeader(containHeader);
            }
            data = new ArrayList<List<String>>();
            List<String> rowValues;
            while ((rowValues = listReader.read()) != null) {
                data.add(rowValues);
            }
        } finally {
            IOUtils.closeQuietly(listReader);
        }
    }

    public String[] getColumnNames() throws IOException {
        if (columnNames == null) {
            columnNames = new String[getColumnCount()];
        }
        return columnNames;
    }

    /**
     * Get all available data, include header row, care should be taken when call this method
     * 
     * @return
     */
    public List<List<String>> getData() {
        return data;
    }

    public int getColumnIndex(String columnName) throws IOException {
        if (columnName != null && !columnName.isEmpty()) {
            for (int i = 0; i < getColumnNames().length; i++) {
                if (columnNames[i] != null && getColumnNames()[i].equals(columnName)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int getColumnCount() throws IOException {
        if (containsHeader) {
            return getColumnNames().length;
        } else {
            if (data != null && data.size() > 0) {
                return data.get(0).size();
            }
            return 0;
        }
    }

}
