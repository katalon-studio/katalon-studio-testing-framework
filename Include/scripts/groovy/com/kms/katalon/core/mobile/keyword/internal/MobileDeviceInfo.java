package com.kms.katalon.core.mobile.keyword.internal;

import com.kms.katalon.core.mobile.driver.MobileDriverType;

public class MobileDeviceInfo {
    private MobileDriverType driverType;
    
    private String deviceId;

    private String deviceName;
    
    private String deviceOS;
    
    private String deviceOSVersion;

    public MobileDriverType getDriverType() {
        return driverType;
    }

    public void setDriverType(MobileDriverType driverType) {
        this.driverType = driverType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }


    public String getDeviceOS() {
        return deviceOS;
    }

    public void setDeviceOS(String deviceOS) {
        this.deviceOS = deviceOS;
    }
    
    public static MobileDeviceInfo create(MobileDriverType driverType, String deviceId, String deviceName, String deviceOS, String deviceOSVersion) {
        MobileDeviceInfo mobileDriverInfo = new MobileDeviceInfo();
        mobileDriverInfo.setDriverType(driverType);
        mobileDriverInfo.setDeviceId(deviceId);
        mobileDriverInfo.setDeviceName(deviceName);
        mobileDriverInfo.setDeviceOS(deviceOS);
        mobileDriverInfo.setDeviceOS(deviceOSVersion);
        return mobileDriverInfo;
    }

    public String getDeviceOSVersion() {
        return deviceOSVersion;
    }

    public void setDeviceOSVersion(String deviceOSVersion) {
        this.deviceOSVersion = deviceOSVersion;
    }
}
