package com.kms.katalon.core.setting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringEscapeUtils;

import com.kms.katalon.core.constants.StringConstants;

public class PropertySettingStoreUtil {
    private static final String SETTING_ROOT_FOLDER_NAME = "settings";

    public static final String INTERNAL_SETTING_ROOT_FOLDER_NAME = SETTING_ROOT_FOLDER_NAME + File.separator
            + "internal";

    public static final String EXTERNAL_SETTING_ROOT_FOLDER_NAME = SETTING_ROOT_FOLDER_NAME + File.separator
            + "external";

    public static final String PROPERTY_FILE_EXENSION = ".properties";

    private static final String BOOLEAN_REGEX = "^(true|false)$";

    private static final String INTEGER_REGEX = "^(-)?\\d+$";

    private static final String STRING_REGEX = "^\".*\"$";

    private static final String PROPERTY_NAME_REGEX = "^[a-zA-Z0-9\\.\\-_@\\*]+$";

    public static void addNewProperty(String key, String value, File propertyFile) throws IOException {
        FileInputStream fileInput = null;
        FileOutputStream fileOutput = null;
        try {
            fileInput = new FileInputStream(propertyFile);
            LinkedProperties properties = new LinkedProperties();
            properties.load(new InputStreamReader(fileInput, Charset.forName(StringConstants.DF_CHARSET)));
            fileInput.close();
            fileInput = null;

            properties.put(key, value);
            fileOutput = new FileOutputStream(propertyFile);
            properties.store(new OutputStreamWriter(fileOutput, Charset.forName(StringConstants.DF_CHARSET)), null);
            fileOutput.close();
            fileOutput = null;
        } finally {
            if (fileInput != null) {
                fileInput.close();
            }

            if (fileOutput != null) {
                fileOutput.close();
            }
        }
    }

    public static void clearAll(File propertyFile) throws IOException {
        FileInputStream fileInput = null;
        FileOutputStream fileOutput = null;
        try {
            fileInput = new FileInputStream(propertyFile);
            Properties properties = new LinkedProperties();
            properties.load(new InputStreamReader(fileInput, Charset.forName(StringConstants.DF_CHARSET)));
            fileInput.close();
            fileInput = null;

            properties.clear();
            fileOutput = new FileOutputStream(propertyFile);
            properties.store(new OutputStreamWriter(fileOutput, Charset.forName(StringConstants.DF_CHARSET)), null);
            fileOutput.close();
            fileOutput = null;
        } finally {
            if (fileInput != null) {
                fileInput.close();
            }

            if (fileOutput != null) {
                fileOutput.close();
            }
        }
    }

    public static void removeAll(Collection<String> keys, File propertyFile) throws IOException {
        FileInputStream fileInput = null;
        FileOutputStream fileOutput = null;
        try {
            fileInput = new FileInputStream(propertyFile);
            LinkedProperties properties = new LinkedProperties();
            properties.load(new InputStreamReader(fileInput, Charset.forName(StringConstants.DF_CHARSET)));
            fileInput.close();
            fileInput = null;

            Iterator<Object> orderedKeys = properties.orderedKeys().iterator();
            while (orderedKeys.hasNext()) {
                Object propertyKey = orderedKeys.next();
                String rawEntryKey = propertyKey.toString();

                if (keys.contains(rawEntryKey)) {
                    properties.remove(propertyKey);
                }
            }

            fileOutput = new FileOutputStream(propertyFile);
            properties.store(new OutputStreamWriter(fileOutput, Charset.forName(StringConstants.DF_CHARSET)), null);
            fileOutput.close();
            fileOutput = null;
        } finally {
            if (fileInput != null) {
                fileInput.close();
            }

            if (fileOutput != null) {
                fileOutput.close();
            }
        }
    }

    public static String getPropertyValue(String key, File propertyFile) throws IOException {
        if (!propertyFile.exists())
            return null;

        FileInputStream fileInput = new FileInputStream(propertyFile);
        try {
            Properties properties = new LinkedProperties();
            properties.load(new InputStreamReader(fileInput, Charset.forName(StringConstants.DF_CHARSET)));
            return properties.getProperty(key);
        } finally {
            fileInput.close();
        }
    }

    public static Map<String, String> getPropertyValues(String parentKey, File propertyFile) throws IOException {
        if (!propertyFile.exists())
            return Collections.emptyMap();

        FileInputStream fileInput = new FileInputStream(propertyFile);
        try {
            LinkedProperties properties = new LinkedProperties();
            properties.load(new InputStreamReader(fileInput, Charset.forName(StringConstants.DF_CHARSET)));
            Map<String, String> mapProperties = new LinkedHashMap<String, String>();

            Iterator<Object> orderedKeys = properties.orderedKeys().iterator();
            while (orderedKeys.hasNext()) {
                Object propertyKey = orderedKeys.next();
                String rawEntryKey = propertyKey.toString();

                if (rawEntryKey.startsWith(parentKey + ".")) {
                    String entryKey = rawEntryKey.substring(parentKey.length() + 1);
                    String entryValue = properties.getProperty(rawEntryKey);

                    mapProperties.put(entryKey, entryValue);
                }
            }

            return mapProperties;
        } finally {
            fileInput.close();
        }
    }

    public static Object getValue(String rawValue) {
        if (rawValue == null || rawValue.isEmpty())
            return null;

        if (rawValue.matches(BOOLEAN_REGEX)) {
            return Boolean.valueOf(rawValue);
        } else if (rawValue.matches(INTEGER_REGEX)) {
            return Integer.valueOf(rawValue);
        } else if (rawValue.matches(STRING_REGEX)) {
            return StringEscapeUtils.unescapeJava(rawValue.substring(1, rawValue.length() - 1));
        } else {
            return rawValue;
        }
    }

    public static String getRawValue(Object value) {
        if (value == null)
            return null;
        if (value instanceof String) {
            return "\"" + StringEscapeUtils.escapeJava((String) value) + "\"";
        } else {
            return String.valueOf(value);
        }
    }

    public static boolean isValidPropertyName(String name) {
        if (name == null || name.isEmpty())
            return false;
        return name.matches(PROPERTY_NAME_REGEX);
    }

    public static Properties getExternalSettings(String projectFolderLocation, String settingName) throws IOException {
        return getSettings(projectFolderLocation + File.separator + EXTERNAL_SETTING_ROOT_FOLDER_NAME + File.separator
                + settingName + PROPERTY_FILE_EXENSION);
    }

    public static Properties getInternalSettings(String projectFolderLocation, String settingName) throws IOException {
        return getSettings(projectFolderLocation + File.separator + INTERNAL_SETTING_ROOT_FOLDER_NAME + File.separator
                + settingName + PROPERTY_FILE_EXENSION);
    }

    public static void saveExternalSettings(String projectFolderLocation, String settingName, Properties settings,
            String comment) throws IOException {
        saveSettings(settings, projectFolderLocation + File.separator + EXTERNAL_SETTING_ROOT_FOLDER_NAME
                + File.separator + settingName + PROPERTY_FILE_EXENSION, comment);
    }

    public static void saveInternalSettings(String projectFolderLocation, String settingName, Properties settings,
            String comment) throws IOException {
        saveSettings(settings, projectFolderLocation + File.separator + INTERNAL_SETTING_ROOT_FOLDER_NAME
                + File.separator + settingName + PROPERTY_FILE_EXENSION, comment);
    }

    private static Properties getSettings(String filePath) throws IOException {
        File settingFile = new File(filePath);
        if (!settingFile.exists()) {
            settingFile.createNewFile();
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(settingFile);
            Properties settings = new Properties();
            settings.load(new InputStreamReader(fis, Charset.forName(StringConstants.DF_CHARSET)));
            return settings;
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

    private static void saveSettings(Properties settings, String filePath, String comment) throws IOException {
        File settingFile = new File(filePath);
        if (!settingFile.exists()) {
            settingFile.createNewFile();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(settingFile);
            settings.store(new OutputStreamWriter(fos, Charset.forName(StringConstants.DF_CHARSET)), comment);
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }
}
