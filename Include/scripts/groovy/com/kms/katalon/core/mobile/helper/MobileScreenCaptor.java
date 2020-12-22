package com.kms.katalon.core.mobile.helper;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import com.google.common.io.BaseEncoding;
import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.exception.StepFailedException;
import com.kms.katalon.core.helper.screenshot.ScreenCaptor;
import com.kms.katalon.core.helper.screenshot.ScreenCaptureException;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.mobile.constants.StringConstants;
import com.kms.katalon.core.mobile.exception.MobileException;
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory;
import com.kms.katalon.core.testobject.TestObject;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;

public class MobileScreenCaptor extends ScreenCaptor {

    /**
     * Takes screenshot by using
     * {@link TakesScreenshot#getScreenshotAs(OutputType)}.
     * </p>
     * Using try with multi-catch to prevent error when generating groovy
     * document.
     */
    @Override
    protected void take(File newFile) throws ScreenCaptureException {
        AppiumDriver<?> driver = getAnyAppiumDriver();
        String context = driver.getContext();
        try {
            internalSwitchToNativeContext(driver);

            File tempFile = driver.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(tempFile, newFile);
            FileUtils.forceDelete(tempFile);

        } catch (WebDriverException e) {
            throw new ScreenCaptureException(e);
        } catch (StepFailedException e) {
            throw new ScreenCaptureException(e);
        } catch (IOException e) {
            throw new ScreenCaptureException(e);
        } finally {
            driver.context(context);
        }
    }

    protected AppiumDriver<?> getAnyAppiumDriver() {
        AppiumDriver<?> driver = null;
        try {
            driver = MobileDriverFactory.getDriver();
        } catch (StepFailedException e) {
            // Native app not running, so get from driver store
            for (Object driverObject : RunConfiguration.getStoredDrivers()) {
                if (driverObject instanceof AppiumDriver<?>) {
                    driver = (AppiumDriver<?>) driverObject;
                }
            }
        }
        if (driver == null) {
            throw new StepFailedException(StringConstants.KW_MSG_UNABLE_FIND_DRIVER);
        }
        return driver;
    }

    protected boolean internalSwitchToNativeContext(AppiumDriver<?> driver) {
        return internalSwitchToContext(driver, "NATIVE");
    }

    protected boolean internalSwitchToContext(AppiumDriver<?> driver, String contextName) {
        try {
            for (String context : driver.getContextHandles()) {
                if (context.contains(contextName)) {
                    driver.context(context);
                    return true;
                }
            }
        } catch (WebDriverException e) {
            // Appium will raise WebDriverException error when driver.getContextHandles() is called but ios-webkit-debug-proxy is not started.
            // Catch it here and ignore
        }
        return false;
    }

    private static final KeywordLogger _logger = KeywordLogger.getInstance(MobileScreenCaptor.class);

    private static final Color DEFAULT_HIDDEN_COLOR = Color.RED;

    protected static String takeScreenshot(AppiumDriver<? extends WebElement> driver, String fileName) {
        File tempFile = driver.getScreenshotAs(OutputType.FILE);
        if (!tempFile.exists()) {
            throw new StepFailedException(StringConstants.KW_MSG_UNABLE_TO_TAKE_SCREENSHOT);
        }
        try {
            FileUtils.copyFile(tempFile, new File(fileName));
            FileUtils.forceDelete(tempFile);
        } catch (Exception e) {
            _logger.logWarning(e.getMessage(), null, e);
            return null;
        }
        Map<String, String> attributes = new HashMap<>();
        attributes.put(StringConstants.XML_LOG_ATTACHMENT_PROPERTY, fileName);
        _logger.logPassed(StringConstants.KW_LOG_PASSED_SCREENSHOT_IS_TAKEN, attributes);
        return fileName;
    }

    public static BufferedImage takeViewportScreenshot(AppiumDriver<? extends WebElement> driver,
            List<TestObject> ignoredElements, Color hidingColor) throws MobileException {
        BufferedImage image;
        Color paintColor = hidingColor != null ? hidingColor : DEFAULT_HIDDEN_COLOR;
        DevicePixelRatio pixelRatio = MobileCommonHelper.getDevicePixelRatio(driver);
        int deviceHeight = (int) (driver.manage().window().getSize().height * pixelRatio.ratioY);
        int statusBarHeight = MobileCommonHelper.getStatusBarHeight(driver, true);
        try {
            image = takeViewportScreenshot(driver);
            handleViewportScreenshot(image, deviceHeight, statusBarHeight);
            hideElements(image, driver, ignoredElements, paintColor, pixelRatio, statusBarHeight);
        } catch (Exception e) {
            throw new MobileException(StringConstants.KW_MSG_UNABLE_TO_TAKE_SCREENSHOT, e);
        }

        return image;
    }

    protected static BufferedImage takeScreenshot(AppiumDriver<? extends WebElement> driver) throws MobileException {
        try {
            byte[] rawImage = driver.getScreenshotAs(OutputType.BYTES);
            return ImageIO.read(new ByteArrayInputStream(rawImage));
        } catch (Exception e) {
            throw new MobileException(StringConstants.KW_MSG_UNABLE_TO_TAKE_SCREENSHOT, e);
        }
    }

    public static BufferedImage takeViewportScreenshot(AppiumDriver<? extends WebElement> driver)
            throws MobileException {
        String rawImage = (String) driver.executeScript("mobile: viewportScreenshot");
        if (rawImage == null) {
            throw new MobileException(StringConstants.KW_MSG_UNABLE_TO_TAKE_SCREENSHOT);
        }

        if (driver instanceof IOSDriver) {
            rawImage = StringUtils.remove(rawImage, System.lineSeparator());
        }

        if (!BaseEncoding.base64().canDecode(rawImage)) {
            throw new MobileException(StringConstants.KW_MSG_UNABLE_TO_TAKE_SCREENSHOT);
        }

        byte[] decodedImage = BaseEncoding.base64().decode(rawImage);
        BufferedImage image;
        try {
            image = ImageIO.read(new ByteArrayInputStream(decodedImage));
        } catch (IOException e) {
            throw new MobileException(StringConstants.KW_MSG_UNABLE_TO_TAKE_SCREENSHOT, e);
        }

        return image;
    }

    /** Hide elements by drawing overlap color layer.
     * @param screenshot the screenshot that need to hide elements
     * @param driver AppiumDriver used to detect hidden elements
     * @param ignoredElements hidden elements
     * @param hideColor color used to draw overlap layer.
     * @return BufferedImage that all elements is hidden.
     */
    public static BufferedImage hideElements(BufferedImage screenshot, AppiumDriver<? extends WebElement> driver,
            List<TestObject> ignoredElements, Color hideColor, DevicePixelRatio pixelRatio, int statusBarHeight) {
        Objects.requireNonNull(driver);
        Objects.requireNonNull(screenshot);
        if (ignoredElements == null || ignoredElements.isEmpty()) {
            _logger.logInfo(MessageFormat.format(StringConstants.KW_LOG_SCREENSHOT_HIDDEN_OBJECTS_COUNT, "0"));
            return screenshot;
        }
        Graphics2D g = screenshot.createGraphics();
        if (hideColor == null) {
            hideColor = DEFAULT_HIDDEN_COLOR;
        }
        g.setColor(hideColor);
        int counter = 0;
        for (TestObject to : ignoredElements) {
            WebElement element;
            try {
                element = MobileElementCommonHelper.findElementWithCheck(to, 0);
                Rectangle boundedRect = getBoundedRect(element, pixelRatio, statusBarHeight);
                g.fillRect(boundedRect.x, boundedRect.y, boundedRect.width, boundedRect.height);
                ++counter;
            } catch (Exception e) {
                _logger.logInfo(
                        MessageFormat.format(StringConstants.KW_LOG_SCREENSHOT_FAILED_HIDE_OBJECT, to.getObjectId()));
            }
        }
        _logger.logInfo(
                MessageFormat.format(StringConstants.KW_LOG_SCREENSHOT_HIDDEN_OBJECTS_COUNT, String.valueOf(counter)));
        return screenshot;
    }

    /** Get the actual rectangle that bounds the element. Returned rectangle is affected by device pixel ratio.
     * @param driver AppiumDriver that the element is linked to.
     * @param element The element that you want to get the rectangle.
     * @return a rectangle in actual size that bounds the elements.
     */
    public static Rectangle getBoundedRect(WebElement element, DevicePixelRatio pixelRatio, int statusBarHeight) {
        Objects.requireNonNull(element);
        Objects.requireNonNull(pixelRatio);
        Rectangle rect = element.getRect();
        int targetRectWidth = (int) (rect.width * pixelRatio.ratioX);
        int tartgetRectHeight = (int) (rect.height * pixelRatio.ratioY);
        int targetRectX = (int) (rect.x * pixelRatio.ratioX);
        int targetRectY = (int) (rect.y * pixelRatio.ratioY) - statusBarHeight;
        return new Rectangle(targetRectX, targetRectY, tartgetRectHeight, targetRectWidth);
    }

    public static BufferedImage removeStatusBar(BufferedImage screenshot, int statusBarHeight) {
        if (statusBarHeight <= 0) {
            _logger.logWarning(StringConstants.KW_LOG_SCREENSHOT_STATUSBAR_EXIST);
            return screenshot;
        }
        int noStatusBarHeight = screenshot.getHeight() - statusBarHeight;
        return screenshot.getSubimage(0, statusBarHeight + 1, screenshot.getWidth(), noStatusBarHeight - 1);
    }

    public static BufferedImage handleViewportScreenshot(BufferedImage screenshot, int deviceActualheight, int statusBarHeight) {
        if (screenshot.getHeight() == deviceActualheight) {
            _logger.logInfo(StringConstants.KW_LOG_SCREENSHOT_STATUSBAR_INFO);
            screenshot = removeStatusBar(screenshot, statusBarHeight);
        }
        return screenshot;
    }

    protected static BufferedImage takeElementScreenshot(AppiumDriver<? extends WebElement> driver, TestObject to,
            List<TestObject> ignoredElements, Color hidingColor) throws Exception {
        BufferedImage screenshot = takeViewportScreenshot(driver, ignoredElements, hidingColor);
        WebElement element = MobileElementCommonHelper.findElementWithCheck(to, 0);
        DevicePixelRatio pixelRatio = MobileCommonHelper.getDevicePixelRatio(driver);
        int statusBarHeight = MobileCommonHelper.getStatusBarHeight(driver, true);
        Rectangle rect = getBoundedRect(element, pixelRatio, statusBarHeight);
        return screenshot.getSubimage(rect.x, rect.y, rect.width, rect.height);
    }

    protected static BufferedImage takeAreaScreenshot(AppiumDriver<? extends WebElement> driver, Rectangle rect,
            List<TestObject> ignoredElements, Color hidingColor) throws MobileException {
        try {
            BufferedImage screenshot = takeViewportScreenshot(driver, ignoredElements, hidingColor);
            return screenshot.getSubimage(rect.x, rect.y, rect.width, rect.height);
        } catch (Exception e) {
            throw new MobileException(StringConstants.KW_MSG_UNABLE_TO_TAKE_SCREENSHOT, e);
        }

    }

}
