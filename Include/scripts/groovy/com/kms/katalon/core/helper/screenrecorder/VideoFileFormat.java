package com.kms.katalon.core.helper.screenrecorder;

public enum VideoFileFormat {
    AVI("AVI", ".avi"), MOV("MOV", ".mov");

    private String name;

    private String extension;

    private VideoFileFormat(String name, String extension) {
        this.name = name;
        this.extension = extension;
    }

    public String getName() {
        return name;
    }

    public String getExtension() {
        return extension;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", getName(), getExtension());
    }
}
