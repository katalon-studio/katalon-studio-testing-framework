package com.kms.katalon.core.webui.common.internal;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.testobject.TestObject;
import com.kms.katalon.core.testobject.TestObjectXpath;
import com.kms.katalon.core.webui.common.WebUiCommonHelper;
import com.kms.katalon.core.webui.constants.StringConstants;

/**
 * A controller used by Smart XPath Plug-in.
 *
 */
public class SmartXPathController {

	private static String SMART_XPATH_PREFIX = "[SMART_XPATH]";

	private static KeywordLogger logger;

	/**
	 * This method initializes Smart XPath Logger with a logger of the calling
	 * object, should be called first before doing anything
	 * 
	 * @param logger
	 *            An KeywordLogger instance
	 */
	public static void setLogger(KeywordLogger logger) {
		SmartXPathController.logger = logger;
	}

	/**
	 * Log an information with Smart XPath plug-in's internal prefix. Note that
	 * a KeywordLogger must be set first. see {@link #setLogger(KeywordLogger)}
	 * 
	 * @param message
	 */
	public static void logInfo(String message) {
		logger.logInfo(smartXPathPrefixify(message));
	}

	/**
	 * Log an error with Smart XPath plug-in's internal prefix. Note that a
	 * KeywordLogger must be set first. see {@link #setLogger(KeywordLogger)}
	 * 
	 * @param message
	 */
	public static void logError(String message) {
		logger.logError(smartXPathPrefixify(message));
	}
	
	/**
     * Log an error with Smart XPath plug-in's internal prefix. Note that a
     * KeywordLogger must be set first. see {@link #setLogger(KeywordLogger)}
     * 
     * @param message
     * @param throwable
     */
    public static void logError(String message, Throwable throwable) {
        logger.logError(smartXPathPrefixify(message), null, throwable);
    }

	private static String smartXPathPrefixify(String message) {
		return SMART_XPATH_PREFIX + " " + message;
	}

	/**
	 * Register a Test Object as broken, register this information along with a
	 * proposed XPath to an internal file provided by Smart Path Plug-in.
	 * 
	 * @param testObject
	 *            The broken Test Object to be registered
	 * @param thisXPath
	 *            The proposed XPath for the broken Test Object
	 * @param pathToScreenshot
	 *            Path to the screenshot of the web element retrieved by the
	 *            proposed XPath
	 */
	public static void registerBrokenTestObject(TestObject testObject, TestObjectXpath thisXPath,
			String pathToScreenshot) {
		String jsAutoHealingPath = getSmartXPathInternalFilePath();
		BrokenTestObject brokenTestObject = buildBrokenTestObject(testObject, thisXPath.getValue(), pathToScreenshot);
		BrokenTestObjects existingBrokenTestObjects = readExistingBrokenTestObjects(jsAutoHealingPath);
		if (existingBrokenTestObjects != null) {
			existingBrokenTestObjects.getBrokenTestObjects().add(brokenTestObject);
			writeBrokenTestObjects(existingBrokenTestObjects, jsAutoHealingPath);
		} else {
			logError(jsAutoHealingPath + " does not exist or is provided by Smart XPath Plugin!");
		}
	}

	private static void writeBrokenTestObjects(BrokenTestObjects brokenTestObjects, String filePath) {
		try {
			Writer writer = new FileWriter(filePath);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			gson.toJson(brokenTestObjects, writer);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			logError(e.getMessage(), e);
		}
	}

	private static BrokenTestObjects readExistingBrokenTestObjects(String filePath) {
		try {
			Gson gson = new Gson();
			JsonReader reader = new JsonReader(new FileReader(filePath));
			return gson.fromJson(reader, BrokenTestObjects.class);
		} catch (Exception e) {
			logError(e.getMessage(), e);
		}
		return null;
	}

	private static BrokenTestObject buildBrokenTestObject(TestObject testObject, String newXPath,
			String pathToScreenshot) {
		String oldXPath = testObject.getSelectorCollection().get(testObject.getSelectorMethod());
		BrokenTestObject brokenTestObject = new BrokenTestObject();
		brokenTestObject.setTestObjectId(testObject.getObjectId());
		brokenTestObject.setApproved(false);
		brokenTestObject.setBrokenXPath(oldXPath);
		brokenTestObject.setProposedXPath(newXPath);
		brokenTestObject.setPathToScreenshot(pathToScreenshot);
		return brokenTestObject;
	}

	public static String getSmartXPathFolderPath() {
		return RunConfiguration.getProjectDir() + "/Reports/smart_xpath";
	}

	public static String getSmartXPathInternalFilePath() {
		return RunConfiguration.getProjectDir() + "/Reports/smart_xpath/waiting-for-approval.json";
	}

	/**
	 * Take screenshot of a web element and saved to an internal folder provided
	 * by Smart XPath Plug-in
	 * 
	 * @param webDriver
	 *            A WebDriver instance that's being used at the time calling
	 *            this function
	 * @param ele
	 *            The web element to be taken screenshot of
	 * @param name
	 *            Name of the screenshot
	 * @return A path to the newly taken screenshot, an empty string otherwise
	 */
	public static String takeScreenShot(WebDriver webDriver, WebElement ele, String name) {
		String smartXPathFolder = getSmartXPathFolderPath();
		try {
			String fullPath = WebUiCommonHelper.saveWebElementScreenshot(webDriver, ele, name, smartXPathFolder);
			logInfo("Screenshot: " + fullPath);
			return fullPath;
		} catch (Exception ex) {
			logError(MessageFormat.format(StringConstants.KW_LOG_INFO_COULD_NOT_SAVE_SCREENSHOT, smartXPathFolder,
					ex.getMessage()), ex);
		}
		return StringUtils.EMPTY;
	}
}
