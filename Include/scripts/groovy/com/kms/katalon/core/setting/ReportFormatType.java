package com.kms.katalon.core.setting;

public enum ReportFormatType {
    HTML("HTML file"), CSV("CSV file"), PDF("PDF file"), LOG("Log files");

    private final String text;

    private ReportFormatType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public String getFileExtension() {
        switch (this) {
            case CSV:
                return "csv";
            case HTML:
                return "html";
            case LOG:
                return "log";
            case PDF:
                return "pdf";
        }
        return "";
    }
    
    public static ReportFormatType getTypeByExtension(String ext) {
        for (ReportFormatType formatType : values()) {
            if (formatType.getFileExtension().equalsIgnoreCase(ext)) {
                return formatType;
            }
        }
        return null;
    }
}
