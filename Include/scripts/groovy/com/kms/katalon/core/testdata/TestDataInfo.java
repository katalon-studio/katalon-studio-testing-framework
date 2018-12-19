package com.kms.katalon.core.testdata;

public class TestDataInfo {
    private String key;

    private String info;

    public TestDataInfo(String key, String info) {
        setKey(key);
        setInfo(info);
    }

    public String getInfo() {
        return info;
    }

    private void setInfo(String info) {
        this.info = info;
    }

    public String getKey() {
        return key;
    }

    private void setKey(String key) {
        this.key = key;
    }
}
