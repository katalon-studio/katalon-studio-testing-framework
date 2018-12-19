package com.kms.katalon.core.webui.contribution;

import com.kms.katalon.core.driver.internal.IDriverCleaner
import com.kms.katalon.core.keyword.internal.IKeywordContributor
import com.kms.katalon.core.webui.constants.StringConstants;
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords;

public class WebUiKeywordContributor implements IKeywordContributor {

	@Override
	public Class<?> getKeywordClass() {
		return WebUiBuiltInKeywords.class;
	}

    @Override
    public String getLabelName() {
        return StringConstants.CONTR_LBL_WEB_UI_KEYWORD;
    }

	@Override
	public Class<? extends IDriverCleaner> getDriverCleaner() {
		return WebUiDriverCleaner.class;
	}

    @Override
    public int getPreferredOrder() {
        return 1;
    }

    @Override
    public String getAliasName() {
        return "WebUI";
    }
}

