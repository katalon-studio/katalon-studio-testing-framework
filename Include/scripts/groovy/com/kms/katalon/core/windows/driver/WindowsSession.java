package com.kms.katalon.core.windows.driver;

import java.net.URL;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.kms.katalon.core.network.ProxyInformation;

import io.appium.java_client.windows.WindowsDriver;

public class WindowsSession {
    private WindowsDriver<WebElement> applicationDriver;

    private WindowsDriver<WebElement> desktopDriver;

    private boolean isApplicationSession = true;

    private final URL remoteAddressURL;

    private final String appFile;

    private final DesiredCapabilities initCapabilities;

    private final ProxyInformation proxyInfo;
    
    public WindowsSession(URL remoteAddressURL, String appFile,
            DesiredCapabilities initCapabilities, ProxyInformation proxyInfo) {
        this.remoteAddressURL = remoteAddressURL;
        this.appFile = appFile;
        this.initCapabilities = initCapabilities;
        this.proxyInfo = proxyInfo;
    }

    public WindowsDriver<WebElement> getApplicationDriver() {
        return applicationDriver;
    }

    public void setApplicationDriver(WindowsDriver<WebElement> applicationDriver) {
        this.applicationDriver = applicationDriver;
    }

    public WindowsDriver<WebElement> getDesktopDriver() {
        return desktopDriver;
    }

    public void setDesktopDriver(WindowsDriver<WebElement> desktopDriver) {
        this.desktopDriver = desktopDriver;
    }

    public boolean isApplicationSession() {
        return isApplicationSession;
    }

    public void setApplicationSession(boolean isApplicationSession) {
        this.isApplicationSession = isApplicationSession;
    }

    public WindowsDriver<WebElement> getRunningDriver() {
        return isApplicationSession ? applicationDriver : desktopDriver;
    }

    public URL getRemoteAddressURL() {
        return remoteAddressURL;
    }

    public String getAppFile() {
        return appFile;
    }

    public DesiredCapabilities getInitCapabilities() {
        return initCapabilities;
    }

    public ProxyInformation getProxyInfo() {
        return proxyInfo;
    }
}
