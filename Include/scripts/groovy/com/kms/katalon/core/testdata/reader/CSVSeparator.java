package com.kms.katalon.core.testdata.reader;


public enum CSVSeparator {
    COMMA("comma"),
    TAB("tab"),
    SEMICOLON("semicolon");
    
    private final String text;

    private CSVSeparator(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
    
    public static CSVSeparator fromValue(String value) {
        for (CSVSeparator type : values()) {
            if (type.toString().equals(value)) {
                return type;
            }
        }
        return valueOf(value);
    }
    
    public static String[] stringValues() {
        String[] stringValues = new String[values().length];
        for (int i = 0; i < values().length; i++) {
            stringValues[i] = values()[i].name();
        }
        return stringValues;
    }
}
