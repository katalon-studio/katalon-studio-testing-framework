package com.kms.katalon.core.mobile.contribution;


import groovy.transform.CompileStatic

import com.kms.katalon.core.driver.internal.IDriverCleaner
import com.kms.katalon.core.keyword.internal.IKeywordContributor
import com.kms.katalon.core.mobile.constants.StringConstants
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords

@CompileStatic
public class MobileKeywordContributor implements IKeywordContributor {
    @Override
    public Class<?> getKeywordClass() {
        return MobileBuiltInKeywords.class;
    }

    @Override
    public String getLabelName() {
        return StringConstants.CONTR_LBL_MOBILE_KEYWORD;
    }

	@Override
	public Class<? extends IDriverCleaner> getDriverCleaner() {
		return MobileDriverCleaner.class;
	}

    @Override
    public int getPreferredOrder() {
        return 2;
    }

    @Override
    public String getAliasName() {
        return "Mobile";
    }
}
