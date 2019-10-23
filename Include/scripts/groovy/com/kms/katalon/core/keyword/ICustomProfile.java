package com.kms.katalon.core.keyword;

import java.util.Map;

public interface ICustomProfile {

	public String getName();

	public String getWebDriverType();

	public Map<String, Object> getDesiredCapabilities();
}
