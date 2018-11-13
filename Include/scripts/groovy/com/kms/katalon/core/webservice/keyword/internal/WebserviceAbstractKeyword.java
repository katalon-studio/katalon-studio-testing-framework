package com.kms.katalon.core.webservice.keyword.internal;

import com.kms.katalon.core.keyword.internal.AbstractKeyword;
import com.kms.katalon.core.keyword.internal.SupportLevel;

public abstract class WebserviceAbstractKeyword extends AbstractKeyword {
    
	@Override
	public SupportLevel getSupportLevel(Object ...params) {
		return SupportLevel.NOT_SUPPORT;
	}
}
