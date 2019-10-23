package com.kms.katalon.core.windows.keyword.helper;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.kms.katalon.core.exception.StepFailedException;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.testobject.WindowsTestObject;
import com.kms.katalon.core.windows.driver.WindowsDriverFactory;
import com.kms.katalon.core.windows.driver.WindowsSession;
import com.kms.katalon.core.windows.keyword.exception.DriverNotStartedException;
import com.kms.katalon.core.windows.keyword.exception.SessionNotStartedException;

import io.appium.java_client.windows.WindowsDriver;

public class WindowsActionHelper {

    private KeywordLogger logger = KeywordLogger.getInstance(WindowsActionHelper.class);

    private WindowsSession windowsSession;

    public WindowsActionHelper(WindowsSession windowsSession) {
        this.windowsSession = windowsSession;
    }

    public static WindowsActionHelper create(WindowsSession windowsSession) {
        return new WindowsActionHelper(windowsSession);
    }

    public WebElement findElement(WindowsTestObject testObject)
            throws IllegalArgumentException, DriverNotStartedException {
        if (testObject == null) {
            throw new IllegalArgumentException("Test object cannot be null");
        }

        WindowsTestObject.LocatorStrategy selectedLocator = testObject.getLocatorStrategy();
        String locator = testObject.getLocator();
        if (StringUtils.isEmpty(locator)) {
            throw new IllegalArgumentException(String.format("Test object %s does not have locator for strategy: %s. ",
                    testObject.getObjectId(), selectedLocator));
        }
        
        if (windowsSession == null) {
            throw new SessionNotStartedException("Windows Session has not started yet!");
        }

        WindowsDriver<WebElement> windowsDriver = windowsSession.getRunningDriver();
        if (windowsDriver == null) {
            throw new DriverNotStartedException("WindowsDriver has not started yet!");
        }

        switch (selectedLocator) {
            case ACCESSIBILITY_ID:
                return windowsDriver.findElementByAccessibilityId(locator);
            case CLASS_NAME:
                return windowsDriver.findElementByClassName(locator);
            case ID:
                return windowsDriver.findElementById(locator);
            case NAME:
                return windowsDriver.findElementByName(locator);
            case TAG_NAME:
                return windowsDriver.findElementByTagName(locator);
            case XPATH:
                return windowsDriver.findElementByXPath(locator);
            default:
                break;
        }
        return null;
    }

    public List<WebElement> findElements(WindowsTestObject testObject)
            throws IllegalArgumentException, DriverNotStartedException {
        if (testObject == null) {
            throw new IllegalArgumentException("Test object cannot be null");
        }

        WindowsTestObject.LocatorStrategy selectedLocator = testObject.getLocatorStrategy();
        String locator = testObject.getLocator();
        if (StringUtils.isEmpty(locator)) {
            throw new IllegalArgumentException(String.format("Test object %s does not have locator for strategy: %s. ",
                    testObject.getObjectId(), selectedLocator));
        }

        WindowsDriver<WebElement> windowsDriver = windowsSession.getRunningDriver();
        if (windowsDriver == null) {
            throw new DriverNotStartedException("WindowsDriver has not started yet!");
        }

        switch (selectedLocator) {
            case ACCESSIBILITY_ID:
                return windowsDriver.findElementsByAccessibilityId(locator);
            case CLASS_NAME:
                return windowsDriver.findElementsByClassName(locator);
            case ID:
                return windowsDriver.findElementsById(locator);
            case NAME:
                return windowsDriver.findElementsByName(locator);
            case TAG_NAME:
                return windowsDriver.findElementsByTagName(locator);
            case XPATH:
                return windowsDriver.findElementsByXPath(locator);
            default:
                break;
        }
        return null;
    }

    public String getText(WindowsTestObject testObject) {
        WebElement webElement = findElement(testObject);

        if (webElement == null) {
            throw new StepFailedException("Element: " + testObject.getObjectId() + " not found");
        }
        logger.logDebug("Getting text of test object: " + testObject.getObjectId());
        return webElement.getText();
    }

    public void clearText(WindowsTestObject testObject) {
        WebElement webElement = findElement(testObject);

        if (webElement == null) {
            throw new StepFailedException("Element: " + testObject.getObjectId() + " not found");
        }
        logger.logDebug("Clearing text of test object: " + testObject.getObjectId());
        logger.logDebug("Text before clearing: " + webElement.getText());
        webElement.clear();
    }

    public void click(WindowsTestObject testObject) {
        WebElement webElement = findElement(testObject);

        if (webElement == null) {
            throw new StepFailedException("Element: " + testObject.getObjectId() + " not found");
        }

        logger.logInfo("Clicking on element: " + testObject.getObjectId());
        webElement.click();
    }

    public void rightClick(WindowsTestObject testObject) {
        WebElement webElement = findElement(testObject);

        if (webElement == null) {
            throw new StepFailedException("Element: " + testObject.getObjectId() + " not found");
        }

        logger.logInfo("Right clicking on element: " + testObject.getObjectId());

        WindowsDriver<WebElement> windowsDriver = windowsSession.getRunningDriver();
        Actions action = new Actions(windowsDriver);
        action.moveToElement(webElement).contextClick().perform();
    }

    public void doubleClick(WindowsTestObject testObject) {
        WebElement webElement = findElement(testObject);

        if (webElement == null) {
            throw new StepFailedException("Element: " + testObject.getObjectId() + " not found");
        }

        logger.logInfo("Double clicking on element: " + testObject.getObjectId());

        WindowsDriver<WebElement> windowsDriver = windowsSession.getRunningDriver();

        Actions action = new Actions(windowsDriver);
        action.moveToElement(webElement);
        action.doubleClick();
        action.perform();
    }

    public void sendKeys(WindowsTestObject testObject, CharSequence... keys) {
        WebElement windowElement = findElement(testObject);
        windowElement.sendKeys(keys);
    }

    public void setText(WindowsTestObject testObject, String text) {
        WebElement windowElement = findElement(testObject);
        logger.logDebug("Setting text on test object: " + testObject.getObjectId());
        windowElement.sendKeys(text);
    }

    public void closeApp() {
        WindowsDriver<WebElement> windowsDriver = windowsSession.getRunningDriver();
        windowsDriver.closeApp();
    }

    public void takeScreenshot(String screenshotLocation) throws IOException {
        logger.logDebug("Taking screenshot");
        WindowsDriver<WebElement> windowsDriver = windowsSession.getRunningDriver();
        File srcFile = windowsDriver.getScreenshotAs(OutputType.FILE);
        logger.logDebug("Copying screenshot from temporary location: " + srcFile.getAbsolutePath()
                + " to report folder at: " + screenshotLocation);
        FileUtils.copyFile(srcFile, new File(screenshotLocation));
    }

    public void switchToDesktop() throws IOException, URISyntaxException {
        if (windowsSession.getDesktopDriver() == null) {
            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
            desiredCapabilities.setCapability("app", "Root");
            WindowsDriver<WebElement> desktopDriver = new WindowsDriver<WebElement>(WindowsDriverFactory
                    .getAppiumExecutorForRemoteDriver(windowsSession.getRemoteAddressURL(), windowsSession.getProxy()),
                    desiredCapabilities);
            windowsSession.setDesktopDriver(desktopDriver);
        }

        windowsSession.setApplicationSession(false);
    }

    public void switchToApplication() {
        windowsSession.setApplicationSession(true);
    }
}