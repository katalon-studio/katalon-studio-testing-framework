package com.kms.katalon.core.windows.keyword.helper;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;

import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.constants.StringConstants;
import com.kms.katalon.core.exception.StepFailedException;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.testobject.WindowsTestObject;
import com.kms.katalon.core.windows.constants.WindowsDriverConstants;
import com.kms.katalon.core.windows.constants.CoreWindowsMessageConstants;
import com.kms.katalon.core.windows.driver.WindowsDriverFactory;
import com.kms.katalon.core.windows.driver.WindowsSession;
import com.kms.katalon.core.windows.keyword.exception.DriverNotStartedException;
import com.kms.katalon.core.windows.keyword.exception.SessionNotStartedException;

import io.appium.java_client.windows.WindowsDriver;

public class WindowsActionHelper {

    private static KeywordLogger logger = KeywordLogger.getInstance(WindowsActionHelper.class);

    private WindowsSession windowsSession;

    public WindowsActionHelper(WindowsSession windowsSession) {
        this.windowsSession = windowsSession;
    }

    public static int checkTimeout(int timeout) throws IllegalArgumentException {
        if (timeout < 0) {
            throw new IllegalArgumentException(
                    String.format("Timeout '%s' is invalid. Cannot be a negative number", timeout));
        } else if (timeout == 0) {
            int defaultPageLoadTimeout = getDefaultTimeout();
            logger.logWarning(MessageFormat.format(StringConstants.COMM_LOG_WARNING_INVALID_TIMEOUT, timeout,
                    defaultPageLoadTimeout));
            return defaultPageLoadTimeout;
        }
        return timeout;
    }

    public static WindowsActionHelper create(WindowsSession windowsSession) {
        return new WindowsActionHelper(windowsSession);
    }

    public WebElement findElement(WindowsTestObject testObject) {
        try {
            return this.findElement(testObject, getDefaultTimeout());
        } catch (NoSuchElementException exception) {
            throw new StepFailedException("Element: " + testObject.getObjectId() + " not found");
        }
    }

    public WebElement findElement(WindowsTestObject testObject, int timeout) {
        try {
            return this.findElement(testObject, timeout, true);
        } catch (NoSuchElementException exception) {
            throw new StepFailedException("Element: " + testObject.getObjectId() + " not found");
        }
    }

    public void checkWebElement(WindowsTestObject testObject) throws IllegalArgumentException{
		logger.logDebug(String.format("Checking test object"));
        if (testObject == null) {
            throw new IllegalArgumentException("Test object cannot be null");
        }
    }

    public WebElement findElement(WindowsTestObject testObject, int timeout, boolean continueWhenNotFound)
            throws IllegalArgumentException, DriverNotStartedException, NoSuchElementException {
    	this.checkWebElement(testObject);

        WindowsTestObject.LocatorStrategy selectedLocator = testObject.getLocatorStrategy();
        String locator = testObject.getLocator();
        if (StringUtils.isEmpty(locator)) {
            throw new IllegalArgumentException(String.format("Test object %s does not have locator for strategy: %s. ",
                    testObject.getObjectId(), selectedLocator));
        }

        if (windowsSession == null) {
            throw new SessionNotStartedException("Windows Session has not started yet!");
        }

        timeout = WindowsActionHelper.checkTimeout(timeout);
        WindowsDriver<WebElement> windowsDriver = windowsSession.getRunningDriver();
        if (windowsDriver == null) {
            throw new DriverNotStartedException("WindowsDriver has not started yet!");
        }

        try {
            windowsDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

            FluentWait<WindowsDriver<WebElement>> wait = new FluentWait<WindowsDriver<WebElement>>(windowsDriver)
                    .withTimeout(Duration.ofSeconds(timeout))
                    .pollingEvery(Duration.ofMillis(WindowsDriverConstants.DEFAULT_FLUENT_WAIT_POLLING_TIME_OUT));
            if (continueWhenNotFound) {
                wait.ignoring(NoSuchElementException.class);
            }
            WebElement webElement = wait.until(new Function<WindowsDriver<WebElement>, WebElement>() {
                @Override
                public WebElement apply(WindowsDriver<WebElement> driver) {

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
                        return null;
                    }
                }
            });
            return webElement;
        } finally {
            windowsDriver.manage().timeouts().implicitlyWait(getDefaultTimeout(), TimeUnit.SECONDS);
        }
    }

    public List<WebElement> findElements(WindowsTestObject testObject) {
        try {
            return this.findElements(testObject, getDefaultTimeout());
        } catch (NoSuchElementException exception) {
            throw new StepFailedException("Element: " + testObject.getObjectId() + " not found");
        }
    }

    public List<WebElement> findElements(WindowsTestObject testObject, int timeout) {
        try {
            return this.findElements(testObject, timeout, true);
        } catch (NoSuchElementException exception) {
            throw new StepFailedException("Element: " + testObject.getObjectId() + " not found");
        }
    }

    public List<WebElement> findElements(WindowsTestObject testObject, int timeout, boolean continueWhenNotFound)
            throws IllegalArgumentException, DriverNotStartedException, NoSuchElementException {
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
        try {
            windowsDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

            FluentWait<WindowsDriver<WebElement>> wait = new FluentWait<WindowsDriver<WebElement>>(windowsDriver)
                    .withTimeout(Duration.ofSeconds(timeout))
                    .pollingEvery(Duration.ofMillis(WindowsDriverConstants.DEFAULT_FLUENT_WAIT_POLLING_TIME_OUT));
            if (continueWhenNotFound) {
                wait.ignoring(NoSuchElementException.class);
            }
            List<WebElement> webElementList = wait.until(new Function<WindowsDriver<WebElement>, List<WebElement>>() {

                @Override
                public List<WebElement> apply(WindowsDriver<WebElement> arg0) {
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
                        return null;
                    }
                }
            });
            return webElementList;
        } finally {
            windowsDriver.manage().timeouts().implicitlyWait(getDefaultTimeout(), TimeUnit.SECONDS);
        }
    }
    
    private static int getDefaultTimeout() {
        try {
            return RunConfiguration.getTimeOut();
        } catch (Exception exception1) {
            return 0;
        }
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
    
    public void clickOffset(WindowsTestObject testObject, int offsetX, int offsetY) {
        WebElement webElement = findElement(testObject);

        if (webElement == null) {
            throw new StepFailedException("Element: " + testObject.getObjectId() + " not found");
        }

        logger.logInfo("Clicking on element: " + testObject.getObjectId() + " at offset " + offsetX + " ," + offsetY);
        Actions builder = new Actions(windowsSession.getRunningDriver());
        builder.moveToElement(webElement, offsetX, offsetY).click().build().perform();
    }

    public void rightClickOffset(WindowsTestObject testObject, int offsetX, int offsetY) {
        WebElement webElement = findElement(testObject);

        if (webElement == null) {
            throw new StepFailedException("Element: " + testObject.getObjectId() + " not found");
        }

        logger.logInfo(
                "Right clicking on element: " + testObject.getObjectId() + " at offset " + offsetX + " ," + offsetY);

        WindowsDriver<WebElement> windowsDriver = windowsSession.getRunningDriver();
        Actions action = new Actions(windowsDriver);
        action.moveToElement(webElement, offsetX, offsetY).contextClick().perform();
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

    public Point getPosition(WindowsTestObject testObject) {
        WebElement windowElement = findElement(testObject);
        return windowElement.getLocation();
    }

    public Rectangle getRect(WindowsTestObject testObject) {
        WebElement windowElement = findElement(testObject);
        Point position = windowElement.getLocation();
        Dimension size = windowElement.getSize();
        return new Rectangle(position.x, position.y, size.height, size.width);
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
                    .getAppiumExecutorForRemoteDriver(windowsSession.getRemoteAddressURL(), windowsSession.getProxyInfo()),
                    desiredCapabilities);
            windowsSession.setDesktopDriver(desktopDriver);
        }

        windowsSession.setApplicationSession(false);
    }

    public void switchToApplication() {
        windowsSession.setApplicationSession(true);
    }

    public WindowsDriver<WebElement> switchToWindowTitle(String windowName) throws IOException, URISyntaxException {
        if (windowsSession.getDesktopDriver() == null) {
            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
            desiredCapabilities.setCapability("app", "Root");
            WindowsDriver<WebElement> desktopDriver = new WindowsDriver<WebElement>(WindowsDriverFactory
                    .getAppiumExecutorForRemoteDriver(windowsSession.getRemoteAddressURL(), windowsSession.getProxyInfo()),
                    desiredCapabilities);
            windowsSession.setDesktopDriver(desktopDriver);
        }
        WindowsDriver<WebElement> desktopDriver = windowsSession.getDesktopDriver();

        FluentWait<WindowsDriver<WebElement>> wait = new FluentWait<WindowsDriver<WebElement>>(desktopDriver)
                .withTimeout(Duration.ofMillis(WindowsActionSettings.DF_WAIT_ACTION_TIMEOUT_IN_MILLIS))
                .pollingEvery(Duration.ofMillis(5000));
        
        logger.logInfo(MessageFormat.format(CoreWindowsMessageConstants.WindowsActionHelper_INFO_START_FINDING_WINDOW,
                windowName, WindowsActionSettings.DF_WAIT_ACTION_TIMEOUT_IN_MILLIS));
        
        WebElement webElement = wait.until(new Function<WindowsDriver<WebElement>, WebElement>() {
            @Override
            public WebElement apply(WindowsDriver<WebElement> driver) {
                WebElement webElement = null;
                for (WebElement element : desktopDriver.findElementsByTagName("Window")) {
                    try {
                        if (element.getText().contains(windowName)) {
                            webElement = element;
                            break;
                        }
                    } catch (WebDriverException ignored) {
                    }
                }

                if (webElement == null) {
                    for (WebElement element : desktopDriver.findElementsByTagName("Pane")) {
                        try {
                            if (element.getText().contains(windowName)) {
                                webElement = element;
                                break;
                            }
                        } catch (WebDriverException ignored) {
                        }
                    }
                }
                return webElement;
            }
        });
        if (webElement == null) {
            throw new NoSuchElementException(MessageFormat.format("No such window matches with name: {0}", windowName));
        }

        String appTopLevelWindow = webElement.getAttribute("NativeWindowHandle");

        if (StringUtils.isNotEmpty(appTopLevelWindow)) {
            DesiredCapabilities retryDesiredCapabilities = new DesiredCapabilities(
                    windowsSession.getInitCapabilities());
            retryDesiredCapabilities.setCapability("appTopLevelWindow",
                    Integer.toHexString(Integer.parseInt(appTopLevelWindow)));
            WindowsDriver<WebElement> windowsDriver = WindowsDriverFactory.newWindowsDriver(
                    windowsSession.getRemoteAddressURL(), retryDesiredCapabilities, windowsSession.getProxyInfo());

            windowsSession.setApplicationDriver(windowsDriver);
            windowsSession.setDesktopDriver(desktopDriver);
            windowsSession.setApplicationSession(true);
            return windowsDriver;
        }
        throw new NotFoundException("The found window does not have NativeWindowHandle property");
    }

    public WindowsDriver<WebElement> switchToWindow(WindowsTestObject windowsObject)
            throws IOException, URISyntaxException, IllegalAccessException {
        if (windowsSession.getDesktopDriver() == null) {
            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
            desiredCapabilities.setCapability("app", "Root");
            WindowsDriver<WebElement> desktopDriver = new WindowsDriver<WebElement>(WindowsDriverFactory
                    .getAppiumExecutorForRemoteDriver(windowsSession.getRemoteAddressURL(), windowsSession.getProxyInfo()),
                    desiredCapabilities);
            windowsSession.setDesktopDriver(desktopDriver);
        }
        WindowsDriver<WebElement> desktopDriver = windowsSession.getDesktopDriver();
        WebElement webElement = findElement(windowsObject);
        if (webElement == null) {
            throw new NoSuchElementException("No such window matches with the given windowsObject");
        }

        String appTopLevelWindow = webElement.getAttribute("NativeWindowHandle");

        if (StringUtils.isNotEmpty(appTopLevelWindow)) {
            DesiredCapabilities retryDesiredCapabilities = new DesiredCapabilities(
                    windowsSession.getInitCapabilities());
            retryDesiredCapabilities.setCapability("appTopLevelWindow",
                    Integer.toHexString(Integer.parseInt(appTopLevelWindow)));
            WindowsDriver<WebElement> windowsDriver = WindowsDriverFactory.newWindowsDriver(
                    windowsSession.getRemoteAddressURL(), retryDesiredCapabilities, windowsSession.getProxyInfo());
            
            windowsDriver.manage().timeouts().implicitlyWait(getDefaultTimeout(), TimeUnit.SECONDS);

            windowsSession.setApplicationDriver(windowsDriver);
            windowsSession.setDesktopDriver(desktopDriver);

            windowsSession.setApplicationSession(true);
            return windowsDriver;
        }
        throw new NotFoundException("The found window does not have NativeWindowHandle property");
    }
}
