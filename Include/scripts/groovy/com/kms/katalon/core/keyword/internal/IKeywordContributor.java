package com.kms.katalon.core.keyword.internal;

import com.kms.katalon.core.driver.internal.IDriverCleaner;


public interface IKeywordContributor {
    public Class<?> getKeywordClass();
    public String getLabelName();
    String getAliasName();
    public Class<? extends IDriverCleaner> getDriverCleaner();
    public int getPreferredOrder();
}
