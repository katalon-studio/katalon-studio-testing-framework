package com.kms.katalon.core.db;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.kms.katalon.core.setting.PropertySettingStoreUtil;
import com.kms.katalon.core.util.internal.Base64;

public class DatabaseSettings {
    public static final String URL = "URL";

    public static final String SECURE_USER_ACCOUNT = "SECURE_USER_ACCOUNT";

    public static final String USER = "USER";

    public static final String PASSWORD = "PASSWORD";
    
    public static final String DRIVERCLASSNAME = "DRIVERCLASSNAME";

    private Properties settings;

    public DatabaseSettings(String projectDirLocation) throws IOException {
        setSettings(PropertySettingStoreUtil.getExternalSettings(projectDirLocation, DatabaseSettings.class.getName()));
    }

    public DatabaseConnection getDatabaseConnection() {
        if (StringUtils.isBlank(getUrl())) {
            return null;
        }

		if (isSecureUserAccount()) {

			if (getDriverClassName() != null) {
				return new DatabaseConnection(getUrl(), getUser(), getPassword(), getDriverClassName());
			} else {
				return new DatabaseConnection(getUrl(), getUser(), getPassword());
			}
		}

        return new DatabaseConnection(getUrl());
    }

    public void setSettings(Properties settings) {
        if (settings == null) {
            settings = new Properties();
        }
        this.settings = settings;
    }

    public Properties getSettings() {
        return this.settings;
    }

    public String getUrl() {
        return settings.getProperty(URL);
    }
    
    public String getDriverClassName() {
        return settings.getProperty(DRIVERCLASSNAME);
    }

    public void setUrl(String url) {
        settings.setProperty(URL, StringUtils.trimToEmpty(url));
    }
    
    public void setDriverClassName(String driverClassName) {
        settings.setProperty(DRIVERCLASSNAME, StringUtils.trimToEmpty(driverClassName));
    }

    public boolean isSecureUserAccount() {
        return Boolean.parseBoolean(settings.getProperty(SECURE_USER_ACCOUNT));
    }

    public void setSecureUserAccount(boolean isSecureUserAccount) {
        settings.setProperty(SECURE_USER_ACCOUNT, String.valueOf(isSecureUserAccount));
    }

    public String getUser() {
        return settings.getProperty(USER);
    }

    public void setUser(String user) {
        settings.setProperty(USER, StringUtils.trimToEmpty(user));
    }

    public String getPassword() {
        return Base64.decode(settings.getProperty(PASSWORD));
    }

    public void setPassword(String plainTextPassword) {
        settings.setProperty(PASSWORD, Base64.encode(StringUtils.trimToEmpty(plainTextPassword)));
    }
}
