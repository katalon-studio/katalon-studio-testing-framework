package com.kms.katalon.core.webui.common.internal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kms.katalon.core.testobject.SelectorMethod;

public class BrokenTestObject {

    @SerializedName("approved")
    @Expose
    private Boolean approved;

    @SerializedName("testObjectId")
    @Expose
    private String testObjectId;

    @SerializedName("brokenLocator")
    @Expose
    private String brokenLocator;

    @SerializedName("brokenLocatorMethod")
    @Expose
    private SelectorMethod brokenLocatorMethod;

    @SerializedName("proposedLocator")
    @Expose
    private String proposedLocator;

    @SerializedName("proposedLocatorMethod")
    @Expose
    private SelectorMethod proposedLocatorMethod;

    @SerializedName("recoveryMethod")
    @Expose
    private SelectorMethod recoveryMethod;

    @SerializedName("pathToScreenshot")
    @Expose
    private String pathToScreenshot;

    public String getTestObjectId() {
        return testObjectId;
    }

    public void setTestObjectId(String testObjectId) {
        this.testObjectId = testObjectId;
    }

    public String getBrokenLocator() {
        return brokenLocator;
    }

    public void setBrokenLocator(String brokenLocator) {
        this.brokenLocator = brokenLocator;
    }

    public SelectorMethod getBrokenLocatorMethod() {
        return brokenLocatorMethod;
    }

    public void setBrokenLocatorMethod(SelectorMethod brokenLocatorMethod) {
        this.brokenLocatorMethod = brokenLocatorMethod;
    }

    public String getProposedLocator() {
        return proposedLocator;
    }

    public void setProposedLocator(String proposedLocator) {
        this.proposedLocator = proposedLocator;
    }

    public SelectorMethod getProposedLocatorMethod() {
        return proposedLocatorMethod;
    }

    public void setProposedLocatorMethod(SelectorMethod proposedLocatorMethod) {
        this.proposedLocatorMethod = proposedLocatorMethod;
    }

    public SelectorMethod getRecoveryMethod() {
        return recoveryMethod;
    }

    public void setRecoveryMethod(SelectorMethod recoveryMethod) {
        this.recoveryMethod = recoveryMethod;
    }

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
        result = prime * result + ((brokenLocator == null) ? 0 : brokenLocator.hashCode());
        result = prime * result + ((brokenLocatorMethod == null) ? 0 : brokenLocatorMethod.hashCode());
        result = prime * result + ((pathToScreenshot == null) ? 0 : pathToScreenshot.hashCode());
        result = prime * result + ((proposedLocator == null) ? 0 : proposedLocator.hashCode());
        result = prime * result + ((proposedLocatorMethod == null) ? 0 : proposedLocatorMethod.hashCode());
        result = prime * result + ((recoveryMethod == null) ? 0 : recoveryMethod.hashCode());
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
        if (brokenLocator == null) {
            if (other.brokenLocator != null)
                return false;
        } else if (!brokenLocator.equals(other.brokenLocator))
            return false;
        if (brokenLocatorMethod != other.brokenLocatorMethod)
            return false;
        if (pathToScreenshot == null) {
            if (other.pathToScreenshot != null)
                return false;
        } else if (!pathToScreenshot.equals(other.pathToScreenshot))
            return false;
        if (proposedLocator == null) {
            if (other.proposedLocator != null)
                return false;
        } else if (!proposedLocator.equals(other.proposedLocator))
            return false;
        if (proposedLocatorMethod != other.proposedLocatorMethod)
            return false;
        if (recoveryMethod != other.recoveryMethod)
            return false;
        if (testObjectId == null) {
            if (other.testObjectId != null)
                return false;
        } else if (!testObjectId.equals(other.testObjectId))
            return false;
        return true;
    }
}
