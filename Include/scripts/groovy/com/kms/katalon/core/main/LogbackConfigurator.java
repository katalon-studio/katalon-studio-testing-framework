package com.kms.katalon.core.main;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import com.kms.katalon.core.configuration.RunConfiguration;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;

public class LogbackConfigurator {

    private static final String CONFIG_PROPERTY_FILE = "log.properties";

    private static final String CONFIG_FOLDER = "Include/config";

    private static final String LOG_LEVEL_PROPERTY_PREFIX = "logging.level.";

    /**
     * This should be called early in TestCaseMain.beforeTestCase()
     */
    public static void init() {
        initDefaultLogConfiguration();
        
        Properties configProps = loadCustomConfigProperties();
        overrideLogLevels(configProps);
    }

    private static void initDefaultLogConfiguration() {
        String logbackConfigFileLocation = RunConfiguration.getLogbackConfigFileLocation();
        if (!StringUtils.isBlank(logbackConfigFileLocation)) {
            System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, logbackConfigFileLocation);
        }
    }
    
    private static Properties loadCustomConfigProperties() {
        Properties configProps = new Properties();
        try {
            File propFile = findCustomConfigPropertyFile();
            if (propFile != null) {
                configProps.load(new FileInputStream(propFile));
            }
        } catch (Exception e) {
        }
        return configProps;
    }

    private static void overrideLogLevels(Properties configProps) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        for (Object key : configProps.keySet()) {
            String propKey = (String) key;
            if (propKey.startsWith(LOG_LEVEL_PROPERTY_PREFIX)) {
                String loggerName = propKey.substring(LOG_LEVEL_PROPERTY_PREFIX.length());
                if (!StringUtils.isBlank(loggerName)) {
                    String levelValue = configProps.getProperty(propKey);
                    Level logLevel = Level.toLevel(levelValue);
                    Logger logger = loggerContext.getLogger(loggerName);
                    logger.setLevel(logLevel);
                }
            }
        }
    }

    private static File findCustomConfigPropertyFile() {
        String configFolderPath = RunConfiguration.getProjectDir() + File.separator + CONFIG_FOLDER;
        File configFolder = new File(configFolderPath);
        if (configFolder.exists()) {
            Collection<File> propertyFiles = FileUtils.listFiles(configFolder, new String[] { "properties" }, true);
            Optional<File> propFileOptional = propertyFiles.stream()
                    .filter(f -> CONFIG_PROPERTY_FILE.equals(f.getName()))
                    .findFirst();
            if (propFileOptional.isPresent()) {
                return propFileOptional.get();
            }
        }

        return null;
    }
}
