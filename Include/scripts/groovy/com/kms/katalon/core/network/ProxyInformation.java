package com.kms.katalon.core.network;

import org.apache.commons.lang.StringUtils;

public class ProxyInformation {
    private String proxyOption;

    private String proxyServerType;

    private String username;

    private String password;

    private String proxyServerAddress;
    
    private int proxyServerPort;

    public String getProxyOption() {
        return proxyOption;
    }

    public void setProxyOption(String proxyOption) {
        if (StringUtils.isEmpty(proxyOption)) {
            proxyOption = ProxyOption.NO_PROXY.name();
        }
        this.proxyOption = proxyOption;
    }

    public String getProxyServerType() {
        return proxyServerType;
    }

    public void setProxyServerType(String proxyServerType) {
        this.proxyServerType = StringUtils.isNotEmpty(proxyServerType) ? proxyServerType : "";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = StringUtils.isNotEmpty(username) ? username : "";
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = StringUtils.isNotEmpty(password) ? password : "";
    }

    public String getProxyServerAddress() {
        return proxyServerAddress;
    }

    public void setProxyServerAddress(String proxyServerAddress) {
        this.proxyServerAddress = StringUtils.isNotEmpty(proxyServerAddress) ? proxyServerAddress : "";
    }

    public int getProxyServerPort() {
        return proxyServerPort;
    }

    public void setProxyServerPort(int proxyServerPort) {
        this.proxyServerPort = proxyServerPort;
    }

    public void setProxyServerPort(String proxyServerPort) {
        try {
            this.proxyServerPort = Integer.parseInt(StringUtils.isNotEmpty(proxyServerPort) ? proxyServerPort : "-1");
        } catch (NumberFormatException ex) {
            this.proxyServerPort = -1;
        }
    }

    @Override
    public String toString() {
        return "ProxyInformation{"
                + "proxyOption=" + proxyOption + ", "
                + "proxyServerType=" + proxyServerType + ", "
                + "password=" + password + ", "
                + "proxyServerAddress=" + proxyServerAddress + ", "
                + "proxyServerPort=" + proxyServerPort
                + "}";
    }
}
