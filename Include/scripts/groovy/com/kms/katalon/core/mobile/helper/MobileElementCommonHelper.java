package com.kms.katalon.core.mobile.helper;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.RemoteWebElement;

import com.kms.katalon.core.exception.StepFailedException;
import com.kms.katalon.core.helper.KeywordHelper;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.mobile.constants.StringConstants;
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory;
import com.kms.katalon.core.mobile.keyword.internal.MobileSearchEngine;
import com.kms.katalon.core.mobile.keyword.internal.SelectorBuilderHelper;
import com.kms.katalon.core.model.FailureHandling;
import com.kms.katalon.core.testobject.TestObject;

public class MobileElementCommonHelper {
    
    private static final KeywordLogger logger = KeywordLogger.getInstance(MobileElementCommonHelper.class);
    
    private static final int ANDROID_SEEKBAR_PADDING = 56;

    private static final int DEFAULT_DRAG_AND_DROP_DELAY = 2000;

    public static final int DEFAULT_TAP_DURATION = 50;

    private static final String IOS_CHECKED_ATTRIBUTE_IS_CHECKED = "1";

    private static final String IOS_CHECKED_ATTRIBUTE = "value";

    private static final String ANDROID_CHECKED_ATTRIBUTE = "checked";

    // Added this method here as duplicated for common methods to use
    // TODO: merge this method with MobileBuiltinKeywors.findElement() and
    // re-factor for better code
    @SuppressWarnings("rawtypes")
    public static WebElement findElement(TestObject to, int timeOut) throws Exception {
        Date startTime = new Date();
        Date endTime;
        long span = 0;
        WebElement webElement = null;
        Point elementLocation = null;
        AppiumDriver<?> driver = MobileDriverFactory.getDriver();
        MobileSearchEngine searchEngine = new MobileSearchEngine(driver, to);

        Dimension screenSize = driver.manage().window().getSize();

        while (span < timeOut) {
            webElement = searchEngine.findWebElement(false);
            if (webElement != null) {
                elementLocation = webElement.getLocation();
                if (elementLocation.y < screenSize.height) {
                    break;
                }
                try {
                    if (driver instanceof AndroidDriver) {
                        TouchActions ta = new TouchActions((AndroidDriver) driver);
                        ta.down(screenSize.width / 2, screenSize.height / 2).perform();
                        ta.move(screenSize.width / 2, (int) ((screenSize.height / 2) * 0.5)).perform();
                        ta.release().perform();
                    } else {
                        MobileCommonHelper.swipe(driver, screenSize.width / 2, screenSize.height / 2,
                                screenSize.width / 2, (int) ((screenSize.height / 2) * 0.5));

                    }
                } catch (Exception e) {
                    // Ignore exception while finding elements
                }
            }
            endTime = new Date();
            span = endTime.getTime() - startTime.getTime();
        }
        return webElement;
    }

    public static void tapAndHold(TestObject to, Number duration, int timeout) throws StepFailedException, Exception {
        boolean useCustomDuration = checkDuration(duration);
        WebElement element = findElementWithCheck(to, timeout);
        TouchAction longPressAction = new TouchAction(MobileDriverFactory.getDriver());
        longPressAction = (useCustomDuration)
                ? longPressAction.longPress(element, Duration.ofSeconds(gitIntValueForDuration(duration)))
                : longPressAction.longPress(element);
        longPressAction.release().perform();
        logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_TAP_AND_HOLD_ON_ELEMENT_X_WITH_DURATION_Y,
                        to.getObjectId(), getStringForDuration(duration)));
    }

    public static boolean checkDuration(Number duration) {
        boolean useCustomDuration = true;
        logger.logDebug(StringConstants.COMM_LOG_INFO_CHECKING_DURATION);
        if (isDurationInvalid(duration)) {
            logger.logWarning(MessageFormat.format(StringConstants.COMM_LOG_WARNING_INVALID_DURATION, duration));
            useCustomDuration = false;
        }
        return useCustomDuration;
    }

    private static boolean isDurationInvalid(Number duration) {
        return duration == null || duration.floatValue() <= 0;
    }

    public static void tapAndHold(Number x, Number y, Number duration) throws StepFailedException, Exception {
        MobileCommonHelper.checkXAndY(x, y);
        boolean useCustomDuration = checkDuration(duration);
        TouchAction longPressAction = new TouchAction(MobileDriverFactory.getDriver());
        longPressAction = (useCustomDuration)
                ? longPressAction.longPress(x.intValue(), y.intValue(),
                        Duration.ofSeconds(gitIntValueForDuration(duration)))
                : longPressAction.longPress(x.intValue(), y.intValue());
        longPressAction.release().perform();
        logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_TAP_AND_HOLD_AT_X_Y_WITH_DURATION_Z, x, y,
                        getStringForDuration(duration)));
    }

    private static int gitIntValueForDuration(Number duration) {
        return Math.round(duration.floatValue() * 1000);
    }

    public static String getStringForDuration(Number duration) {
        return (isDurationInvalid(duration)) ? "(default)" : duration.toString();
    }

    public static WebElement findElementWithCheck(TestObject to, int timeout) throws Exception {
        KeywordHelper.checkTestObjectParameter(to);
        timeout = KeywordHelper.checkTimeout(timeout);
        WebElement element = findElement(to, timeout * 1000);
        if (element == null) {
            throw new StepFailedException(MessageFormat.format(StringConstants.KW_MSG_OBJ_NOT_FOUND, to.getObjectId()));
        }
        return element;
    }

    public static void checkElement(TestObject to, int timeout) throws StepFailedException, Exception {
        WebElement element = findElementWithCheck(to, timeout);
        if (!isElementChecked(element)) {
            TouchAction tap = new TouchAction(MobileDriverFactory.getDriver()).tap(element, 1, 1);
            tap.perform();
        }
        logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_CHECK_ELEMENT, to.getObjectId()));
    }

    public static boolean isElementChecked(WebElement element) {
        if (MobileDriverFactory.getDriver() instanceof AndroidDriver<?>) {
            String checkedAttribute = element.getAttribute(ANDROID_CHECKED_ATTRIBUTE);
            return checkedAttribute != null && Boolean.valueOf(checkedAttribute);
        }
        if (MobileDriverFactory.getDriver() instanceof IOSDriver<?>) {
            String checkedAttribute = element.getAttribute(IOS_CHECKED_ATTRIBUTE);
            return checkedAttribute != null && IOS_CHECKED_ATTRIBUTE_IS_CHECKED.equals(checkedAttribute);
        }
        return false;
    }

    public static void uncheckElement(TestObject to, int timeout) throws StepFailedException, Exception {
        WebElement element = findElementWithCheck(to, timeout);
        if (isElementChecked(element)) {
            TouchAction tap = new TouchAction(MobileDriverFactory.getDriver()).tap(element, 1, 1);
            tap.perform();
        }
        logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_UNCHECK_ELEMENT, to.getObjectId()));
    }

    public static void selectItemByIndex(TestObject to, int index, int timeout, FailureHandling flowControl)
            throws Exception {
        findElementWithCheck(to, timeout * 1000);
        // Find direct children
        AppiumDriver<?> driver = MobileDriverFactory.getDriver();
        String xpath = SelectorBuilderHelper.makeXpath(to.getActiveProperties());
        List<?> items = driver.findElementsByXPath(xpath + "/*");
        if (items.size() < index) {
            throw new StepFailedException(
                    MessageFormat.format(StringConstants.KW_LOG_FAILED_LIST_ITEM_INDEX_EXCEED, index, items.size()));
        }
        if (driver instanceof IOSDriver) {
            // Ensure element is visible
            WebElement itemElement = driver.findElementByXPath(xpath + "/*" + "[" + index + "]");
            HashMap<String, String> scrollObject = new HashMap<String, String>();
            scrollObject.put("direction", "down");
            scrollObject.put("element", ((MobileElement) itemElement).getId());
            driver.executeScript("mobile: scrollTo", scrollObject);
            // Click on element
            TouchAction tap = new TouchAction(driver).tap(itemElement, 1, 1).release();
            tap.perform();
        } else if (driver instanceof AndroidDriver) {
            throw new StepFailedException(StringConstants.KW_LOG_FAILED_FEATURE_NOT_AVAILABLE);
        }
        logger.logPassed(
                MessageFormat.format(StringConstants.KW_LOG_PASSED_LIST_ITEM_CLICKED, index, to.getObjectId()));
    }

    public static boolean isElementChecked(TestObject to, int timeout) throws StepFailedException, Exception {
        return isElementChecked(findElementWithCheck(to, timeout));
    }

    public static void dragAndDrop(TestObject fromObj, TestObject toObj, int timeout)
            throws StepFailedException, Exception {
        WebElement fromElement = findElement(fromObj, timeout);
        WebElement toElement = findElement(toObj, timeout);
        AppiumDriver<?> driver = MobileDriverFactory.getDriver();
        TouchAction dragAndDropAction = new TouchAction(driver);
        dragAndDropAction = (driver instanceof AndroidDriver)
                ? dragAndDropAction.longPress(fromElement).moveTo(toElement).release()
                : dragAndDropAction.longPress(fromElement)
                        .waitAction(Duration.ofMillis(DEFAULT_DRAG_AND_DROP_DELAY))
                        .moveTo(toElement)
                        .waitAction(Duration.ofMillis(DEFAULT_DRAG_AND_DROP_DELAY))
                        .release();
        dragAndDropAction.perform();
        logger.logPassed(MessageFormat
                .format(StringConstants.KW_LOG_PASSED_DRAG_AND_DROP_ELEMENT_X_TO_ELEMENT_Y, fromObj.getObjectId()));
    }

    public static void selectListItemByLabel(TestObject to, String label, int timeout, FailureHandling flowControl)
            throws Exception {
        WebElement element = findElementWithCheck(to, timeout);
        if (element == null) {
            throw new StepFailedException(MessageFormat.format(StringConstants.KW_MSG_OBJ_NOT_FOUND, to.getObjectId()));
        }
        // Find direct children
        AppiumDriver<?> driver = MobileDriverFactory.getDriver();
        String xpath = SelectorBuilderHelper.makeXpath(to.getActiveProperties());

        scrollToFindElementWithText(driver, (RemoteWebElement) element, label);

        // Find child element with specified label
        String itemXpath = (driver instanceof IOSDriver)
                ? (xpath + "//*" + "[@label='" + label + "' or @value='" + label + "']")
                : (xpath + "//*" + "[@text='" + label + "' or text()='" + label + "']");
        List<?> itemElements = driver.findElementsByXPath(itemXpath);
        if (itemElements.size() > 0) {
            WebElement itemElement = (WebElement) itemElements.get(0);
            // Click on element
            TouchAction tap = new TouchAction(driver).tap(itemElement, 1, 1);
            tap.perform();
        } else {
            throw new StepFailedException(MessageFormat.format(StringConstants.KW_LOG_FAILED_LABELED_ITEM_NOT_FOUND,
                    label, to.getObjectId()));
        }

        logger.logPassed(
                MessageFormat.format(StringConstants.KW_LOG_PASSED_LIST_LABELED_ITEM_CLICKED, label, to.getObjectId()));
    }

    private static void scrollToFindElementWithText(AppiumDriver<?> driver, RemoteWebElement element, String text) {
        // Ensure element visible
        try {
            HashMap<String, String> scrollObject = new HashMap<String, String>();
            scrollObject = new HashMap<String, String>();
            scrollObject.put("direction", "down");
            scrollObject.put("element", element.getId());
            scrollObject.put("text", text);
            driver.executeScript("mobile: scrollTo", scrollObject);
        } catch (WebDriverException ex) {
            // Scroll action sometime throw unknown server error
        }
    }

    public static void moveSlider(TestObject to, Number percent, int timeout) throws StepFailedException, Exception {
        logger.logDebug(StringConstants.COMM_LOG_INFO_CHECKING_PERCENTAGE);
        if (percent == null || percent.floatValue() < 0 || percent.floatValue() > 100) {
            throw new StepFailedException(
                    MessageFormat.format(StringConstants.KW_MSG_FAILED_SET_SLIDER_INVALID_PERCENTAGE_X, percent));
        }
        WebElement element = findElementWithCheck(to, timeout);
        AppiumDriver<?> driver = MobileDriverFactory.getDriver();
        float percentValue = percent.floatValue() / 100;
        if (driver instanceof AndroidDriver<?>) {
            moveAndroidSeekbar(percentValue, element, driver);
        } else if (driver instanceof IOSDriver<?>) {
            moveIosUIASlider(percentValue, element);
        }
        logger.logPassed(
                MessageFormat.format(StringConstants.KW_LOG_PASSED_SET_SLIDER_X_TO_Y, to.getObjectId(), percent));
    }

    private static void moveIosUIASlider(float percentValue, WebElement element) {
        element.sendKeys(String.valueOf(percentValue));
    }

    private static void moveAndroidSeekbar(float percentValue, WebElement element, AppiumDriver<?> driver)
            throws WebDriverException {
        int startX = element.getLocation().getX();
        int width = element.getSize().getWidth() - (ANDROID_SEEKBAR_PADDING * 2);
        int relativeX = Math.round(width * percentValue);
        TouchAction tap = new TouchAction(driver)
                .tap(startX + ANDROID_SEEKBAR_PADDING + relativeX, element.getLocation().getY())
                .waitAction(Duration.ofMillis(DEFAULT_TAP_DURATION))
                .release();
        tap.perform();
    }

    public static int getElementLeftPosition(TestObject to, int timeout, FailureHandling flowControl) throws Exception {
        WebElement element = findElementWithCheck(to, timeout);
        Point location = element.getLocation();
        logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_OBJ_HAS_LEFT_POSITION,
                to.getObjectId(), location.getX()));
        return location.getX();
    }

    public static int getElementTopPosition(TestObject to, int timeout, FailureHandling flowControl) throws Exception {
        WebElement element = findElementWithCheck(to, timeout);
        Point location = element.getLocation();
        logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_OBJ_HAS_TOP_POSITION,
                to.getObjectId(), location.getY()));
        return location.getY();
    }

    public static void tapAtPosition(Number x, Number y) {
        MobileCommonHelper.checkXAndY(x, y);

        TouchAction tap = new TouchAction(MobileDriverFactory.getDriver()).press(x.intValue(), y.intValue())
                .waitAction(Duration.ofMillis(DEFAULT_TAP_DURATION))
                .release();
        tap.perform();
        logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_TAPPED_AT_X_Y, x, y));
    }

    public static int getElementWidth(TestObject to, int timeout) throws Exception {
        WebElement element = findElementWithCheck(to, timeout);
        int width = element.getSize().getWidth();
        logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_OBJ_HAS_WIDTH, to.getObjectId(), width));
        return width;
    }

    public static int getElementHeight(TestObject to, int timeout) throws Exception {
        WebElement element = findElementWithCheck(to, timeout);
        int height = element.getSize().getHeight();
        logger.logPassed(
                MessageFormat.format(StringConstants.KW_LOG_PASSED_OBJ_HAS_HEIGHT, to.getObjectId(), height));
        return height;
    }
}
