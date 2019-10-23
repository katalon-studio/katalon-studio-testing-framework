package com.kms.katalon.core.windows.keyword.contribution;

import com.kms.katalon.core.driver.internal.IDriverCleaner;
import com.kms.katalon.core.keyword.internal.IKeywordContributor;
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords;

public class WindowsBuiltinKeywordContributor implements IKeywordContributor {

    @Override
    public Class<?> getKeywordClass() {
        return WindowsBuiltinKeywords.class;
    }

    @Override
    public String getLabelName() {
        return "Windows Keyword";
    }

    @Override
    public String getAliasName() {
        return "Windows";
    }

    @Override
    public Class<? extends IDriverCleaner> getDriverCleaner() {
        return WindowsDriverCleaner.class;
    }

    @Override
    public int getPreferredOrder() {
        return 4;
    }

}
