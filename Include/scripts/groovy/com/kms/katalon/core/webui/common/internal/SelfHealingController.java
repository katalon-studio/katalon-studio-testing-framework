package com.kms.katalon.core.webui.common.internal;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.testobject.SelectorMethod;
import com.kms.katalon.core.testobject.TestObject;
import com.kms.katalon.core.testobject.TestObjectBuilder;
import com.kms.katalon.core.webui.common.WebUiCommonHelper;
import com.kms.katalon.core.webui.constants.StringConstants;
import com.kms.katalon.core.webui.util.FileUtil;

/**
 * A controller used by Self-healing Plug-in.
 *
 */
public class SelfHealingController {

	private static final String SELF_HEALING_PREFIX = "[SELF-HEALING]";

    public static final String REPORT_FOLDER_NAME = "Reports";

    public static final String SELF_HEALING_FOLDER_NAME = "Self-healing";

    public static final String SELF_HEALING_DATA_FILE_NAME = "broken-test-objects.json";

    public static final String SELF_HEALING_FOLDER_PATH = REPORT_FOLDER_NAME + "/" + SELF_HEALING_FOLDER_NAME;

	private static KeywordLogger logger = KeywordLogger.getInstance(SelfHealingController.class);

	/**
	 * This method initializes Self-healing Logger with a logger of the calling
	 * object, should be called first before doing anything
	 * 
	 * @param logger
	 *            An KeywordLogger instance
	 */
	public static void setLogger(KeywordLogger logger) {
		SelfHealingController.logger = logger;
	}

	/**
	 * Log an information with Self-healing plug-in's internal prefix. Note that
	 * a KeywordLogger must be set first. see {@link #setLogger(KeywordLogger)}
	 * 
	 * @param message
	 */
	public static void logInfo(String message) {
		logger.logInfo(selfHealingPrefixify(message));
	}

	/**
	 * Log an error with Self-healing plug-in's internal prefix. Note that a
	 * KeywordLogger must be set first. see {@link #setLogger(KeywordLogger)}
	 * 
	 * @param message
	 */
	public static void logError(String message) {
		logger.logError(selfHealingPrefixify(message));
	}
	
	/**
     * Log an error with Self-healing plug-in's internal prefix. Note that a
     * KeywordLogger must be set first. see {@link #setLogger(KeywordLogger)}
     * 
     * @param message
     * @param throwable
     */
    public static void logError(String message, Throwable throwable) {
        logger.logError(selfHealingPrefixify(message), null, throwable);
    }

    /**
     * Log an warning with Self-healing plug-in's internal prefix. Note that a
     * KeywordLogger must be set first. see {@link #setLogger(KeywordLogger)}
     * 
     * @param message
     */
    public static void logWarning(String message) {
        logger.logWarning(selfHealingPrefixify(message));
    }
    
    /**
     * Log an warning with Self-healing plug-in's internal prefix. Note that a
     * KeywordLogger must be set first. see {@link #setLogger(KeywordLogger)}
     * 
     * @param message
     * @param throwable
     */
    public static void logWarning(String message, Throwable throwable) {
        logger.logWarning(selfHealingPrefixify(message), null, throwable);
    }

	private static String selfHealingPrefixify(String message) {
		return SELF_HEALING_PREFIX + " " + message;
	}

    public static BrokenTestObject registerBrokenTestObject(TestObject testObject, String proposedLocator,
            SelectorMethod proposedLocatorMethod, SelectorMethod recoveryMethod, String pathToScreenshot) {
        String dataFolderPath = getSelfHealingFolderPath();
        return registerBrokenTestObject(testObject, proposedLocator, proposedLocatorMethod, recoveryMethod,
                pathToScreenshot, dataFolderPath);
    }

	/**
	 * Register a Test Object as broken, register this information along with a
	 * proposed locator to an internal file provided by Self-healing Plug-in.
	 * 
	 * @param testObject
	 *            The broken Test Object to be registered
	 * @param proposedLocator
	 *            The proposed locator for the broken Test Object
	 * @param proposedLocatorMethod
	 *            The proposed locator method for the broken Test Object
     * @param recoveryMethod
     *            The recovery method that healed the broken Test Object
	 * @param pathToScreenshot
	 *            Path to the screenshot of the web element retrieved by the
	 *            proposed locator
	 */
    public static BrokenTestObject registerBrokenTestObject(TestObject testObject, String proposedLocator,
            SelectorMethod proposedLocatorMethod, SelectorMethod recoveryMethod, String pathToScreenshot,
            String dataFolderPath) {
        String dataFilePath = getSelfHealingDataFilePath(dataFolderPath);
        BrokenTestObject brokenTestObject = buildBrokenTestObject(testObject, proposedLocator, proposedLocatorMethod,
                recoveryMethod, pathToScreenshot);
        BrokenTestObjects existingBrokenTestObjects = readExistingBrokenTestObjects(dataFilePath);
        if (existingBrokenTestObjects != null) {
			existingBrokenTestObjects.getBrokenTestObjects().add(brokenTestObject);
			writeBrokenTestObjects(existingBrokenTestObjects, dataFilePath);
		} else {
			logError(dataFilePath + " does not exist or is provided by Self-healing Plugin!");
		}
        return brokenTestObject;
	}

	private static void writeBrokenTestObjects(BrokenTestObjects brokenTestObjects, String filePath) {
		try {
		    prepareDataFile(filePath);
			Writer writer = new FileWriter(filePath);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			gson.toJson(brokenTestObjects, writer);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			logError(e.getMessage(), e);
		}
	}

	public static BrokenTestObjects readExistingBrokenTestObjects(String filePath) {
		try {
            prepareDataFile(filePath);
			Gson gson = new Gson();
			JsonReader reader = new JsonReader(new FileReader(filePath));
			BrokenTestObjects brokenTestObjects = gson.fromJson(reader, BrokenTestObjects.class);
            reader.close();
			return brokenTestObjects != null
			        ? brokenTestObjects
	                : new BrokenTestObjects();
		} catch (Exception e) {
			logError(e.getMessage(), e);
		}
		return null;
	}
	
	public static File prepareDataFile(String dataFilePath) {
	    File autoHealingFile = new File(dataFilePath);
        File selfHealingDirectory = autoHealingFile.getParentFile();
        
        if (!selfHealingDirectory.exists()) {
            selfHealingDirectory.mkdirs();
            if (!selfHealingDirectory.exists()) {
                logError(MessageFormat
                        .format("The folder \"{0}\" does not exist, no file is created.", selfHealingDirectory.getAbsolutePath()));
                return null;
            }
        }

        if (!autoHealingFile.exists()) {
            try {
                if (autoHealingFile.createNewFile()) {
                    BrokenTestObjects emptyBrokenTestObjects = new BrokenTestObjects();
                    Writer writer = new FileWriter(autoHealingFile.getAbsolutePath());
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    gson.toJson(emptyBrokenTestObjects, writer);
                    writer.flush();
                    writer.close();
                    return autoHealingFile;
                }
            } catch (IOException exception) {
                logError(exception.getMessage());
                return null;
            }
        }
        return autoHealingFile;
    }

    private static BrokenTestObject buildBrokenTestObject(TestObject testObject, String proposedLocator,
            SelectorMethod proposedLocatorMethod, SelectorMethod recoveryMethod, String pathToScreenshot) {
        SelectorMethod brokenLocatorMethod = testObject.getSelectorMethod();
        String brokenLocator = testObject.getSelectorCollection().get(brokenLocatorMethod);
        BrokenTestObject brokenTestObject = new BrokenTestObject();
		brokenTestObject.setTestObjectId(testObject.getObjectId());
		brokenTestObject.setApproved(true);
		brokenTestObject.setBrokenLocator(brokenLocator);
		brokenTestObject.setBrokenLocatorMethod(brokenLocatorMethod);
		brokenTestObject.setProposedLocator(proposedLocator);
        brokenTestObject.setProposedLocatorMethod(proposedLocatorMethod);
        brokenTestObject.setRecoveryMethod(recoveryMethod);
		brokenTestObject.setPathToScreenshot(pathToScreenshot);
		return brokenTestObject;
	}
    
    public static Set<BrokenTestObject> findBrokenTestObjects(TestObject testObject) {
        String testObjectId = testObject.getObjectId();
        String selfHealingDataFilePath = getSelfHealingDataFilePath();
        BrokenTestObjects existingBrokenTestObjects = readExistingBrokenTestObjects(selfHealingDataFilePath);
        if (existingBrokenTestObjects != null) {
            Set<BrokenTestObject> brokenTestObjects = existingBrokenTestObjects.getBrokenTestObjects();
            return brokenTestObjects.stream()
                    .filter(brokenTestObject -> {
                        return StringUtils.equals(brokenTestObject.getTestObjectId(), testObjectId);
                    })
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    public static List<TestObject> findHealedTestObjects(TestObject testObject) {
        Set<BrokenTestObject> brokenTestObjects = findBrokenTestObjects(testObject);
        if (brokenTestObjects != null) {
            if (brokenTestObjects != null && !brokenTestObjects.isEmpty()) {
                return brokenTestObjects.stream()
                        .map(brokenTestObject -> healTestObject(testObject, brokenTestObject))
                        .collect(Collectors.toList());
            }
        }
        return null;
    }
    
    public static TestObject healTestObject(TestObject testObject, BrokenTestObject brokenTestObject) {
        TestObject clone = new TestObjectBuilder(testObject.getObjectId())
                .withImagePath(testObject.getImagePath())
                .withParentObject(testObject.getParentObject())
                .withProperties(testObject.getProperties())
                .withSelectorMethod(testObject.getSelectorMethod())
                .withXPaths(testObject.getXpaths())
                .withUseRelativeImagePathEqual(testObject.getUseRelativeImagePath())
                .withSelectorCollection(testObject.getSelectorCollection())
                .build();
        clone.setSelectorMethod(brokenTestObject.getProposedLocatorMethod());
        clone.setSelectorValue(brokenTestObject.getProposedLocatorMethod(), brokenTestObject.getProposedLocator());
        return clone;
    }

	public static String getSelfHealingFolderPath() {
		return FilenameUtils.concat(RunConfiguration.getProjectDir(), SELF_HEALING_FOLDER_PATH);
	}

    public static String getSelfHealingDataFilePath() {
        return getSelfHealingDataFilePath(getSelfHealingFolderPath());
    }

	public static String getSelfHealingDataFilePath(String atFolder) {
		return FilenameUtils.concat(atFolder, SELF_HEALING_DATA_FILE_NAME);
	}

	/**
	 * Take screenshot of a web element and saved to an internal folder provided
	 * by Self-healing Plug-in
	 * 
	 * @param webDriver
	 *            A WebDriver instance that's being used at the time calling
	 *            this function
	 * @param element
	 *            The web element to be taken screenshot of
	 * @param name
	 *            Name of the screenshot
	 * @return A path to the newly taken screenshot, an empty string otherwise
	 */
	public static String takeScreenShot(WebDriver webDriver, WebElement element, String name) {
		String selfHealingFolder = getSelfHealingFolderPath();
		try {
			String fullPath = WebUiCommonHelper.saveWebElementScreenshot(webDriver, element, name, selfHealingFolder);
			logInfo("Screenshot: " + fullPath);
			return fullPath;
		} catch (Exception ex) {
			logError(MessageFormat.format(StringConstants.KW_LOG_INFO_COULD_NOT_SAVE_SCREENSHOT, selfHealingFolder,
					ex.getMessage()), ex);
		}
		return StringUtils.EMPTY;
	}

    public static String takeScreenShot(WebDriver webDriver, WebElement element, TestObject testObject, String name) {
        return takeScreenShot(webDriver, element, testObject.getObjectId() + "_" + name);
    }

    public static String getRelativePathToSelfHealindDir(String screenshotAbsolutePath) {
        String selfHealingFolder = getSelfHealingFolderPath();
        return FileUtil.getRelativePath(screenshotAbsolutePath, selfHealingFolder);
    }

    public static String getScreenshotAbsolutePath(String screenshotRelativePath) {
        String projectDir = RunConfiguration.getProjectDir();
        return FilenameUtils.concat(projectDir, screenshotRelativePath);
    }
}
