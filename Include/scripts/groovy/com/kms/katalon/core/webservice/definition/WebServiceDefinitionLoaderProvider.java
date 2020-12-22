package com.kms.katalon.core.webservice.definition;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kms.katalon.constants.SystemProperties;
import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.model.SSLClientCertificateSettings;
import com.kms.katalon.core.network.ProxyInformation;
import com.kms.katalon.core.util.internal.JsonUtil;
import com.kms.katalon.core.webservice.constants.RequestHeaderConstants;
import com.kms.katalon.core.webservice.setting.SSLCertificateOption;
import com.kms.katalon.core.webservice.setting.WebServiceSettingStore;

public class WebServiceDefinitionLoaderProvider {

    private WebServiceDefinitionLoaderProvider() {

    }

    public static WebServiceDefinitionLoader getLoader(String location) throws IOException {
        if (StringUtils.isBlank(location)) {
            throw new IllegalArgumentException("Location must not be null or empty.");
        }

        if (isWebUrl(location)) {
            return getLoaderForWebLocation(location);
        }
        return getLoaderForFileLocation(location);
    }

    private static WebServiceDefinitionLoader getLoaderForFileLocation(String location) {
        return new WebServiceDefinitionFileLoader(location);
    }

    public static WebServiceDefinitionLoader getLoaderForWebLocation(String location) throws IOException {
        return getLoaderForWebLocation(location, Collections.emptyMap());
    }

    public static WebServiceDefinitionLoader getLoaderForWebLocation(String location, Map<String, String> httpHeaders)
            throws IOException {
        String projectLocation = getProjectLocation();
        if (StringUtils.isNotBlank(projectLocation)) {
            WebServiceSettingStore settingStore = WebServiceSettingStore.create(projectLocation);
            SSLCertificateOption certOption = settingStore.getSSLCertificateOption();
            SSLClientCertificateSettings clientCertSettings = settingStore.getClientCertificateSettings();
            ProxyInformation proxyInfo = getProxyInformation();

            return getLoaderForWebLocation(location, httpHeaders, proxyInfo, certOption, clientCertSettings);
        }
        return null;
    }

    private static ProxyInformation getProxyInformation() {
        ProxyInformation proxyInformation = null;

        try {
            proxyInformation = RunConfiguration.getProxyInformation();
        } catch (Exception ignored) {

        }

        if (proxyInformation == null) {
            String proxyJson = System.getProperty(SystemProperties.SYSTEM_PROXY);
            proxyInformation = JsonUtil.fromJson(proxyJson, ProxyInformation.class);
        }

        return proxyInformation;
    }

    public static WebServiceDefinitionLoader getLoaderForWebLocation(
            String location,
            Map<String, String> httpHeaders,
            ProxyInformation proxyInfo,
            SSLCertificateOption certOption,
            SSLClientCertificateSettings clientCertSettings) {
        WebServiceDefinitionWebLoader loader = new WebServiceDefinitionWebLoader(location);
        loader.setHttpHeaders(httpHeaders);
        loader.setProxyInformation(proxyInfo);
        loader.setSslCertificateOption(certOption);
        loader.setSslClientCertSettings(clientCertSettings);
        return loader;
    }

    private static String getProjectLocation() {
        String projectLocation = null;
        try {
            projectLocation = RunConfiguration.getProjectDir();
        } catch (Exception ignored) {
        }
        if (StringUtils.isBlank(projectLocation) || "null".equals(projectLocation)) {
            projectLocation = System.getProperty(SystemProperties.PROJECT_LOCATION);
        }

        return projectLocation;
    }

    private static boolean isWebUrl(String url) {
        return url.startsWith(RequestHeaderConstants.HTTP) || url.startsWith(RequestHeaderConstants.HTTPS);
    }
}
