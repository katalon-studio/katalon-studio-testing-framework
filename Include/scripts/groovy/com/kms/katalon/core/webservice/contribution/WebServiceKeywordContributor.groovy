package com.kms.katalon.core.webservice.contribution;

import groovy.transform.CompileStatic

import com.kms.katalon.core.driver.internal.IDriverCleaner
import com.kms.katalon.core.keyword.internal.IKeywordContributor
import com.kms.katalon.core.webservice.constants.StringConstants
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords

@CompileStatic
public class WebServiceKeywordContributor implements IKeywordContributor {
    @Override
    public Class<?> getKeywordClass() {
        return WSBuiltInKeywords.class;
    }

    @Override
    public String getLabelName() {
        return StringConstants.CONTR_LBL_WEB_SERVICE_KEYWORD;
    }

	@Override
	public Class<? extends IDriverCleaner> getDriverCleaner() {
		return null;
	}

    @Override
    public int getPreferredOrder() {
        return 3;
    }

    @Override
    public String getAliasName() {
        return "WS";
    }
}
