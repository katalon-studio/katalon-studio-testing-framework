package com.kms.katalon.core.mobile.helper;

public class DevicePixelRatio {
    double ratioX;

    double ratioY;

    public double getRatioX() {
        return ratioX;
    }

    public void setRatioX(double scaleFactorX) {
        this.ratioX = scaleFactorX;
    }

    public double getRatioY() {
        return ratioY;
    }

    public void setRatioY(double scaleFactorY) {
        this.ratioY = scaleFactorY;
    }

    public DevicePixelRatio(double ratioX, double ratioY) {
        super();
        this.ratioX = ratioX;
        this.ratioY = ratioY;
    }
}
