package com.kms.katalon.core.setting;


import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collection;

import com.kms.katalon.util.CryptoUtil;

public class BundleSettingStore {
    private String bundleId;

    private String parentSettingFolder;

    private String projectDir;

    public BundleSettingStore(String projectDir, String bundleId, boolean isExternal) {
        this.bundleId = bundleId;
        this.projectDir = projectDir;
        parentSettingFolder = PropertySettingStoreUtil.INTERNAL_SETTING_ROOT_FOLDER_NAME;
        if (isExternal) {
            parentSettingFolder = PropertySettingStoreUtil.EXTERNAL_SETTING_ROOT_FOLDER_NAME;
        }
    }

    protected File getPropertyFile() throws IOException {
        File file = new File(projectDir,
                parentSettingFolder + File.separator + bundleId + PropertySettingStoreUtil.PROPERTY_FILE_EXENSION);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    public void setProperty(String key, int value) throws IOException {
        PropertySettingStoreUtil.addNewProperty(key, PropertySettingStoreUtil.getRawValue(value), getPropertyFile());
    }

    public void setProperty(String key, String value) throws IOException {
        PropertySettingStoreUtil.addNewProperty(key, PropertySettingStoreUtil.getRawValue(value), getPropertyFile());
    }

    public void setProperty(String key, boolean value) throws IOException {
        PropertySettingStoreUtil.addNewProperty(key, PropertySettingStoreUtil.getRawValue(value), getPropertyFile());
    }

    private Object getValue(String key, Class<?> castType, Object defaultValueIfNotDefined) throws IOException {
        Object storedValue = PropertySettingStoreUtil
                .getValue(PropertySettingStoreUtil.getPropertyValue(key, getPropertyFile()));
        if (castType != null && castType.isInstance(storedValue)) {
            return storedValue;
        }

        return defaultValueIfNotDefined;
    }

    public boolean getBoolean(String key, boolean defaultValue) throws IOException {
        return (boolean) getValue(key, Boolean.class, defaultValue);
    }

    public int getInt(String key, int defaultValue) throws IOException {
        return (int) getValue(key, Integer.class, defaultValue);
    }

    public String getString(String key, String defaultValue) throws IOException {
        return (String) getValue(key, String.class, defaultValue);
    }

    public String getStringProperty(String key, String defaultValue, boolean shouldDecrypt)
            throws GeneralSecurityException, IOException {
        String storedValue = getString(key, defaultValue);
        return shouldDecrypt ? CryptoUtil.decode(CryptoUtil.getDefault(storedValue)) : storedValue;
    }
    
    public void setStringProperty(String key, String rawValue, boolean shouldEncrypt)
            throws GeneralSecurityException, IOException {
        String storedValue = shouldEncrypt ? CryptoUtil.encode(CryptoUtil.getDefault(rawValue)) : rawValue;
        setProperty(key, storedValue);
    }

    public void removeProperties(Collection<String> keys) throws IOException {
        PropertySettingStoreUtil.removeAll(keys, getPropertyFile());
    }
}
