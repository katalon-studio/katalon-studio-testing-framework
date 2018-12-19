package com.kms.katalon.core.helper.screenrecorder;

import org.apache.commons.lang.StringUtils;

public enum VideoQuality {
    LOW("LOW", 8), MEDIUM("MEDIUM", 16), HIGH("HIGH", 24);

    private String name;

    private int depth;

    private VideoQuality(String name, int depth) {
        this.name = name;
        this.setDepth(depth);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getReadableName() {
        return StringUtils.capitalize(getName().toLowerCase());
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
