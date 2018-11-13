package com.kms.katalon.core.cucumber.keyword.internal;

import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords;
import com.kms.katalon.core.driver.internal.IDriverCleaner;
import com.kms.katalon.core.keyword.internal.IKeywordContributor;

public class CucumberKeywordContributor implements IKeywordContributor {

    @Override
    public Class<?> getKeywordClass() {
        return CucumberBuiltinKeywords.class;
    }

    @Override
    public String getLabelName() {
        return "Cucumber Keywords";
    }

    @Override
    public String getAliasName() {
        return "CucumberKW";
    }

    @Override
    public Class<? extends IDriverCleaner> getDriverCleaner() {
        return CucumberDriverCleaner.class;
    }

    @Override
    public int getPreferredOrder() {
        return 3;
    }

}
