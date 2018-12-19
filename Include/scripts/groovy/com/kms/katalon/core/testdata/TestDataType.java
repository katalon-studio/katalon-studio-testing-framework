package com.kms.katalon.core.testdata;

public enum TestDataType {
    EXCEL_FILE("ExcelFile"), CSV_FILE("CSV"), DB_DATA("DBData"), INTERNAL_DATA("InternalData");

    private final String text;

    private TestDataType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static TestDataType fromValue(String value) {
        for (TestDataType type : values()) {
            if (type.toString().equals(value)) {
                return type;
            }
        }
        return valueOf(value);
    }
}
