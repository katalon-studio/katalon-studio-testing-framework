package com.kms.katalon.core.keyword;

import java.util.Map;

public class CustomProfile implements ICustomProfile {

	private String driverType;

	private String name;

	private Map<String, Object> desiredCapabilities;

	@Override
	public String getWebDriverType() {
		return driverType;
	}

	@Override
	public Map<String, Object> getDesiredCapabilities() {
		return desiredCapabilities;
	}

	public void setDriverType(String driverType) {
		this.driverType = driverType;
	}

	public void setDesiredCapabilities(Map<String, Object> desiredCapabilities) {
		this.desiredCapabilities = desiredCapabilities;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
