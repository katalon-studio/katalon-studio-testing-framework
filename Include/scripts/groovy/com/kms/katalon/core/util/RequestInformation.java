package com.kms.katalon.core.util;

public class RequestInformation {

    private String name;
    
    private String testObjectId;
    
    private String harId;
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getHarId() {
		return harId;
	}

	public void setHarId(String harId) {
		this.harId = harId;
	}

	public String getTestObjectId() {
        return testObjectId;
    }
    
    public void setTestObjectId(String testObjectId) {
        this.testObjectId = testObjectId;
    }
}
