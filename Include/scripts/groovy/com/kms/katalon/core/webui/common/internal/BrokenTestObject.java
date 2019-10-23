package com.kms.katalon.core.webui.common.internal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BrokenTestObject {
	@SerializedName("approved")
	@Expose
	private Boolean approved;
	@SerializedName("proposedXPath")
	@Expose
	private String proposedXPath;
	@SerializedName("testObjectId")
	@Expose
	private String testObjectId;
	@SerializedName("brokenXPath")
	@Expose
	private String brokenXPath;
	@SerializedName("pathToScreenshot")
	@Expose
	private String pathToScreenshot;

	public String getPathToScreenshot() {
		return pathToScreenshot;
	}

	public void setPathToScreenshot(String pathToScreenshot) {
		this.pathToScreenshot = pathToScreenshot;
	}

	public Boolean getApproved() {
		return approved;
	}

	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((approved == null) ? 0 : approved.hashCode());
		result = prime * result + ((brokenXPath == null) ? 0 : brokenXPath.hashCode());
		result = prime * result + ((pathToScreenshot == null) ? 0 : pathToScreenshot.hashCode());
		result = prime * result + ((proposedXPath == null) ? 0 : proposedXPath.hashCode());
		result = prime * result + ((testObjectId == null) ? 0 : testObjectId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BrokenTestObject other = (BrokenTestObject) obj;
		if (approved == null) {
			if (other.approved != null)
				return false;
		} else if (!approved.equals(other.approved))
			return false;
		if (brokenXPath == null) {
			if (other.brokenXPath != null)
				return false;
		} else if (!brokenXPath.equals(other.brokenXPath))
			return false;
		if (pathToScreenshot == null) {
			if (other.pathToScreenshot != null)
				return false;
		} else if (!pathToScreenshot.equals(other.pathToScreenshot))
			return false;
		if (proposedXPath == null) {
			if (other.proposedXPath != null)
				return false;
		} else if (!proposedXPath.equals(other.proposedXPath))
			return false;
		if (testObjectId == null) {
			if (other.testObjectId != null)
				return false;
		} else if (!testObjectId.equals(other.testObjectId))
			return false;
		return true;
	}

	public String getProposedXPath() {
		return proposedXPath;
	}

	public void setProposedXPath(String proposedXPath) {
		this.proposedXPath = proposedXPath;
	}

	public String getTestObjectId() {
		return testObjectId;
	}

	public void setTestObjectId(String testObjectId) {
		this.testObjectId = testObjectId;
	}

	public String getBrokenXPath() {
		return brokenXPath;
	}

	public void setBrokenXPath(String brokenXPath) {
		this.brokenXPath = brokenXPath;
	}
}
