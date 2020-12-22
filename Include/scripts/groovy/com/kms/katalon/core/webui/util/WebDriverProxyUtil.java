package com.kms.katalon.core.webui.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kms.katalon.core.driver.DriverType;
import com.kms.katalon.core.network.ProxyInformation;
import com.kms.katalon.core.network.ProxyOption;
import com.kms.katalon.core.network.ProxyServerType;
import com.kms.katalon.core.util.internal.PathUtil;
import com.kms.katalon.core.util.internal.ProxyUtil;

public class WebDriverProxyUtil {

    private static final String PROP_SOCKS_PASSWORD = "socksPassword";

    private static final String PROP_SOCKS_USERNAME = "socksUsername";

    private static final String PROP_SOCKS_PROXY = "socksProxy";

    private static final String PROP_HTTP_PROXY = "httpProxy";

    private static final String PROP_SSL_PROXY = "sslProxy";

    private static final String PROP_FTP_PROXY = "ftpProxy";

    private static final String PROP_PROXY_TYPE = "proxyType";
    
    private static final String PROP_NO_PROXY = "noProxy";

    /**
     * Returns an instance of Selenium Proxy based on proxy settings.
     * <br/>
     * 
     * @param proxyInfomation: Proxy settings
     * @return an instance of {@link Proxy}. if the param is null, return a no Proxy.
     */
    public static Map<String, Object> getSeleniumProxy(ProxyInformation proxyInformation) {
        Map<String, Object> proxyMap = new HashMap<>();
        List<String> exceptionList = new ArrayList<String>();
        String exclude = proxyInformation.getExceptionList().trim();
        String[] output = exclude.split(",");
        Arrays.stream(output).forEach(part -> {
            if (!StringUtils.isBlank(part)) {
                exceptionList.add(part.trim());
            }
        });
        String proxyString = getProxyString(proxyInformation);
        switch (ProxyOption.valueOf(proxyInformation.getProxyOption())) {
            case MANUAL_CONFIG:
                proxyMap.put(PROP_PROXY_TYPE, "manual");
                if (exceptionList.size() > 0) {
                    proxyMap.put(PROP_NO_PROXY, exceptionList);
                }
                switch (ProxyServerType.valueOf(proxyInformation.getProxyServerType())) {
                    case HTTP:
                    case HTTPS:
                        proxyMap.put(PROP_HTTP_PROXY, proxyString);
                        proxyMap.put(PROP_FTP_PROXY, proxyString);
                        proxyMap.put(PROP_SSL_PROXY, proxyString);
                        break;
                    case SOCKS:
                        proxyMap.put(PROP_SOCKS_PROXY, proxyString);
                        proxyMap.put(PROP_SOCKS_USERNAME, proxyInformation.getUsername());
                        proxyMap.put(PROP_SOCKS_PASSWORD, proxyInformation.getPassword());
                        break;
                }
                break;
            case USE_SYSTEM:
                proxyMap.put(PROP_PROXY_TYPE, "system");
                break;
            case NO_PROXY:
                proxyMap.put(PROP_PROXY_TYPE, "direct");
                break;
        }
        return proxyMap;
    }

    public static Map<String, Object> getSeleniumProxy(ProxyInformation proxyInformation, String url,
            String driverType) {
        Map<String, Object> proxyMap = new HashMap<>();
        List<String> exceptionList = new ArrayList<String>();
        String[] output = proxyInformation.getExceptionList().split(",");
        Arrays.stream(output).forEach(part -> exceptionList.add(part.trim()));
        URL newUrl = null;
        Proxy proxy = null;
        try {
            newUrl = PathUtil.getUrl(url, "http");
        } catch (MalformedURLException e1) {} catch (URISyntaxException e) {}
        try {
            proxy = ProxyUtil.getProxy(proxyInformation, newUrl);
        } catch (URISyntaxException | IOException e) {}
        String proxyString = getProxyString(proxyInformation);
        switch (ProxyOption.valueOf(proxyInformation.getProxyOption())) {
            case MANUAL_CONFIG:
                proxyMap.put(PROP_PROXY_TYPE, "manual");
                switch (ProxyServerType.valueOf(proxyInformation.getProxyServerType())) {
                    case HTTP:
                    case HTTPS:
                        if (proxy.type() == Proxy.Type.DIRECT) {
                            if (!(driverType.equals("") || driverType.equals("internet explorer"))) {
                                proxyMap.put(PROP_PROXY_TYPE, "direct");
                            }

                        } else {
                            proxyMap.put(PROP_HTTP_PROXY, proxyString);
                            proxyMap.put(PROP_FTP_PROXY, proxyString);
                            proxyMap.put(PROP_SSL_PROXY, proxyString);
                        }

                        break;
                    case SOCKS:
                        proxyMap.put(PROP_SOCKS_PROXY, proxyString);
                        proxyMap.put(PROP_SOCKS_USERNAME, proxyInformation.getUsername());
                        proxyMap.put(PROP_SOCKS_PASSWORD, proxyInformation.getPassword());
                        break;
                }
                break;
            case USE_SYSTEM:
                proxyMap.put(PROP_PROXY_TYPE, "system");
                break;
            case NO_PROXY:
                proxyMap.put(PROP_PROXY_TYPE, "direct");
                break;
        }
        return proxyMap;
    }

    public static String getProxyString(ProxyInformation proxyInformation) {
        return String.format("%s:%d", proxyInformation.getProxyServerAddress(), proxyInformation.getProxyServerPort());
    }

    public static boolean isManualSocks(ProxyInformation proxyInformation) {
        return proxyInformation != null
                && ProxyOption.valueOf(proxyInformation.getProxyOption()) == ProxyOption.MANUAL_CONFIG
                && ProxyServerType.valueOf(proxyInformation.getProxyServerType()) == ProxyServerType.SOCKS;
    }

    public static boolean isNoProxy(ProxyInformation proxyInformation) {
        return proxyInformation != null
                && ProxyOption.valueOf(proxyInformation.getProxyOption()) == ProxyOption.NO_PROXY;
    }
}
