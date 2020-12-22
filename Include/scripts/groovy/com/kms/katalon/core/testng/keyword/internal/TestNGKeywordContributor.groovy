package com.kms.katalon.core.testng.keyword.internal;

import com.kms.katalon.core.driver.internal.IDriverCleaner;
import com.kms.katalon.core.keyword.internal.IKeywordContributor;
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords;

public class TestNGKeywordContributor implements IKeywordContributor {

    @Override
    public Class<?> getKeywordClass() {
        return TestNGBuiltinKeywords.class;
    }

    @Override
    public String getLabelName() {
        return "TestNG Keywords";
    }

    @Override
    public String getAliasName() {
        return "TestNGKW";
    }

    @Override
    public Class<? extends IDriverCleaner> getDriverCleaner() {
        return TestNGDriverCleaner.class;
    }

    @Override
    public int getPreferredOrder() {
        return 5;
    }

}
