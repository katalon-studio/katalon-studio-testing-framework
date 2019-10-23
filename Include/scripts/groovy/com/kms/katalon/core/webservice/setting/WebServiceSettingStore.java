package com.kms.katalon.core.webservice.setting;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.reflect.TypeToken;
import com.kms.katalon.core.model.SSLClientCertificateSettings;
import com.kms.katalon.core.setting.BundleSettingStore;
import com.kms.katalon.core.util.internal.JsonUtil;
import com.kms.katalon.core.webservice.common.WebServiceMethod;
import com.kms.katalon.core.webservice.constants.StringConstants;
import com.kms.katalon.core.webservice.helper.RestRequestMethodHelper;

/**
 * Provides settings option for WebService bundle.
 *
 */
public class WebServiceSettingStore extends BundleSettingStore {
    
    private static final String SETTINGS_WEBSERVICE_METHODS = "webServiceMethods";

    private WebServiceSettingStore(String projectDir) {
        super(projectDir, StringConstants.WEBSERVICE_BUNDLE_ID, false);
    }

    public static WebServiceSettingStore create(String projectDir) {
        return new WebServiceSettingStore(projectDir);
    }

    public SSLCertificateOption getSSLCertificateOption() throws IOException {
        return SSLCertificateOption
                .valueOf(getString(StringConstants.SETTING_SSL_CERTIFICATE, SSLCertificateOption.BYPASS.name()));
    }
    
    public void saveSSLCertificateOption(SSLCertificateOption option) throws IOException {
        setProperty(StringConstants.SETTING_SSL_CERTIFICATE, option.name());
    }
    
    public SSLClientCertificateSettings getClientCertificateSettings() throws IOException {
        String storedValue = getString(StringConstants.SETTING_SSL_CLIENT_CERTIFICATE, StringUtils.EMPTY);
        if (!StringUtils.isBlank(storedValue)) {
            return JsonUtil.fromJson(storedValue, SSLClientCertificateSettings.class);
        } else {
            SSLClientCertificateSettings defaultSettings = new SSLClientCertificateSettings();
            defaultSettings.setKeyStoreFile(StringUtils.EMPTY);
            defaultSettings.setKeyStorePassword(StringUtils.EMPTY);
            return defaultSettings;
        }
    }
    
    public void saveClientCerfiticateSettings(SSLClientCertificateSettings settings) throws IOException {
        String storedValue = JsonUtil.toJson(settings);
        setProperty(StringConstants.SETTING_SSL_CLIENT_CERTIFICATE, storedValue);
    }
    
    public List<WebServiceMethod> getWebServiceMethods() throws IOException {
        String storedValue = getString(SETTINGS_WEBSERVICE_METHODS, StringUtils.EMPTY);
        List<WebServiceMethod> methods;
        if (!StringUtils.isBlank(storedValue)) {
            methods = parseWebServiceMethods(storedValue);
        } else {
            methods = getDefaultWebServiceMethods();
        }
        return methods;
    }
    
    public List<WebServiceMethod> getDefaultWebServiceMethods() {
        String[] builtInMethods = RestRequestMethodHelper.getBuiltInMethods();
        List<WebServiceMethod> defaultMethods = new ArrayList<>();
        for (String builtInMethod : builtInMethods) {
            WebServiceMethod method = new WebServiceMethod();
            method.setName(builtInMethod);
            method.setType(WebServiceMethod.TYPE_DEFAULT);
            method.setDescription(StringUtils.EMPTY);
            defaultMethods.add(method);
        }
        return defaultMethods;
    }
    
    public void saveWebServiceMethods(List<WebServiceMethod> methods) throws IOException {
        String methodListJson = toMethodListJson(methods);
        setProperty(SETTINGS_WEBSERVICE_METHODS, methodListJson);
    }
    
    private List<WebServiceMethod> parseWebServiceMethods(String methodListJson) {
        Type listType = new TypeToken<List<WebServiceMethod>>() {}.getType();
        List<WebServiceMethod> methods = JsonUtil.fromJson(methodListJson, listType);
        return methods;
    }

    private String toMethodListJson(List<WebServiceMethod> methods) {
        return JsonUtil.toJson(methods);
    }
}
