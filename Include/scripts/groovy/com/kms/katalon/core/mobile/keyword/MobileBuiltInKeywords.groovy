package com.kms.katalon.core.mobile.keyword;

import groovy.transform.CompileStatic
import io.appium.java_client.AppiumDriver
import io.appium.java_client.MobileElement
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.AndroidKeyCode
import io.appium.java_client.ios.IOSDriver
import io.appium.java_client.remote.HideKeyboardStrategy

import java.text.MessageFormat
import java.util.concurrent.TimeUnit

import org.apache.commons.io.FileUtils
import org.apache.commons.lang.StringUtils
import org.codehaus.groovy.transform.tailrec.VariableReplacedListener.*
import org.openqa.selenium.Dimension
import org.openqa.selenium.Keys
import org.openqa.selenium.OutputType
import org.openqa.selenium.Point
import org.openqa.selenium.ScreenOrientation
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebDriverException
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.touch.TouchActions
import org.openqa.selenium.support.ui.FluentWait

import com.google.common.base.Function
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.helper.KeywordHelper
import com.kms.katalon.core.keyword.BuiltinKeywords
import com.kms.katalon.core.keyword.internal.KeywordExecutor
import com.kms.katalon.core.keyword.internal.KeywordMain
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.mobile.constants.StringConstants
import com.kms.katalon.core.mobile.helper.MobileCommonHelper
import com.kms.katalon.core.mobile.helper.MobileElementCommonHelper
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject

@CompileStatic
public class MobileBuiltInKeywords extends BuiltinKeywords {

    /**
     * Start up an application
     * @param appFile
     *      The absolute path or relative path of the mobile application installation file.</br>
     *      In case relative path is used, the base directory is the project location.
     * @param uninstallAfterCloseApp
     *      true if uninstalling the application automatically after run completed; otherwise, false
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_APPLICATION)
    public static void startApplication(String appFile, boolean uninstallAfterCloseApp, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "startApplication", appFile,uninstallAfterCloseApp, flowControl)
    }

    /**
     * Start up an application
     * @param appFile
     *      The absolute path or relative path of the mobile application installation file.</br>
     *      In case relative path is used, the base directory is the project location.
     * @param uninstallAfterCloseApp
     *      true if uninstalling the application automatically after run completed; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_APPLICATION)
    public static void startApplication(String appFile, boolean uninstallAfterCloseApp) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "startApplication", appFile,uninstallAfterCloseApp)
    }
    
    /**
     * Starts Appium driver and activate an installed application by it's given application ID.
     * @param appId
     *      ID of the application under test. It is package name for Android app, and bundleId for iOS app.
     * @throws StepFailedException
     *      If KS could not start Appium Driver, could not start the application or the application doesn't exist.
     * @since 6.2.0
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_APPLICATION)
    public static void startExistingApplication(String appId) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "startExistingApplication", appId)
    }

    /**
     * Starts Appium driver and activate an installed application by it's given application ID.
     * @param appId
     *      ID of the application under test. It is package name for Android app, and bundleId for iOS app.
     * @param flowControl 
     *      Optional parameter: Used to control the step if the step failed. <p>
     *      <ul>
     *      <li>STOP_ON_FAILURE: throws a StepFailedException if the step failed (default).</li>
     *      <li>CONTINUE_ON_FAILURE: continue the test if the test failed but the test result is still failed.</li>
     *      <li>OPTIONAL: continue the test and ignore the test result.</li>
     *      </ul> 
     * @throws StepFailedException
     *      If KS could not start Appium Driver, could not start the application or the application doesn't exist.
     * @since 6.2.0
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_APPLICATION)
    public static void startExistingApplication(String appId, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "startExistingApplication", appId, flowControl)
    }

    /**
     * Close the current running application
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_APPLICATION)
    public static void closeApplication(FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "closeApplication", flowControl)
    }

    /**
     * Close the current running application
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_APPLICATION)
    public static void closeApplication() throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "closeApplication")
    }

    /**
     * Simulate pressing back button on a mobile device (Android only)
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_UTILITIES)
    public static void pressBack(FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "pressBack", flowControl)
    }

    /**
     * Simulate pressing back button on a mobile device (Android only)
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_UTILITIES)
    public static void pressBack() throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "pressBack")
    }

    /**
     * Simulate swiping fingers on the mobile device
     * @param startX
     *      starting x position
     * @param startY
     *      starting y position
     * @param endX
     *      relative x position of startX
     * @param endY
     *      relative y position of startY
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_SCREEN)
    public static void swipe(int startX, int startY, int endX, int endY, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "swipe", startX, startY, endX, endY, flowControl)
    }

    /**
     * Simulate swiping fingers on the mobile device
     * @param startX
     *      starting x position
     * @param startY
     *      starting y position
     * @param endX
     *      relative x position of startX
     * @param endY
     *      relative y position of startY
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_SCREEN)
    public static void swipe(int startX, int startY, int endX, int endY) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "swipe", startX, startY, endX, endY)
    }

    /**
     * Taking screenshot of the mobile device screen
     * @param fileName
     *      the absolute path of the saved screenshot image file
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_UTILITIES)
    public static String takeScreenshot(String fileName, FailureHandling flowControl) throws StepFailedException {
        return KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "takeScreenshot", fileName, flowControl)
    }

    /**
     * Taking screenshot of the mobile device screen
     * @param fileName
     *      the absolute path of the saved screenshot image file
     * @throws StepFailedException
     * @return the absolute path of the saved screenshot image file
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_UTILITIES)
    public static String takeScreenshot(String fileName) throws StepFailedException {
        return KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "takeScreenshot", fileName)
    }

    /**
     * Taking screenshot of the mobile device screen
     * @throws StepFailedException
     * @param flowControl
     * @return the absolute path of the saved screenshot image file
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_UTILITIES)
    public static String takeScreenshot(FailureHandling flowControl) throws StepFailedException {
        return KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "takeScreenshot", flowControl)
    }

    /**
     * Taking screenshot of the mobile device screen
     * @throws StepFailedException
     * @return the absolute path of the saved screenshot image file
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_UTILITIES)
    public static String takeScreenshot() throws StepFailedException {
        return KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "takeScreenshot")
    }

    /**
     * Simulate opening notification action on mobile devices
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_NOTIFICATION)
    public static void openNotifications(FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "openNotifications", flowControl)
    }

    /**
     * Simulate opening notification action on mobile devices
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_NOTIFICATION)
    public static void openNotifications() throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "openNotifications")
    }

    /**
     * Simulate pressing home button on mobile devices (Android only)
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_UTILITIES)
    public static void pressHome(FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "pressHome", flowControl)
    }

    /**
     * Simulate pressing home button on mobile devices (Android only)
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_UTILITIES)
    public static void pressHome() throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "pressHome")
    }

    /**
     * Get the manufacturer of the current active mobile device
     * @param flowControl
     * @return
     *      the manufacturer of the current active mobile device
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static String getDeviceManufacturer(FailureHandling flowControl) throws StepFailedException {
        return (String) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "getDeviceManufacturer", flowControl)
    }

    /**
     * Get the manufacturer of the current active mobile device
     * @return
     *      the manufacturer of the current active mobile device
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static String getDeviceManufacturer() throws StepFailedException {
        return (String) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "getDeviceManufacturer")
    }

    /**
     * Get the device os of the current active mobile device
     * @param flowControl
     * @return
     *      the device os of the current active mobile device
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static String getDeviceOS(FailureHandling flowControl) throws StepFailedException {
        return (String) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "getDeviceOS", flowControl)
    }

    /**
     * Get the device os of the current active mobile device
     * @return
     *      the device os of the current active mobile device
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static String getDeviceOS() throws StepFailedException {
        return (String) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "getDeviceOS")
    }

    /**
     * Get the device os version of the current active mobile device
     * @param flowControl
     * @return
     *      the device os version of the current active mobile device
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static String getDeviceOSVersion(FailureHandling flowControl) throws StepFailedException {
        return (String) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "getDeviceOSVersion", flowControl)
    }

    /**
     * Get the device os version of the current active mobile device
     * @return
     *      the device os version of the current active mobile device
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static String getDeviceOSVersion() throws StepFailedException {
        return (String) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "getDeviceOSVersion")
    }

    /**
     * Simulate closing notification action on mobile devices
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_NOTIFICATION)
    public static void closeNotifications(FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "closeNotifications", flowControl)
    }

    /**
     * Simulate closing notification action on mobile devices
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_NOTIFICATION)
    public static void closeNotifications() throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "closeNotifications")
    }

    /**
     * Simulate toggling airplane mode on mobile devices
     * @param mode
     *          ["yes", "on", "true"] to turn on airplane mode; otherwise, airplane mode is turn off
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_UTILITIES)
    public static void toggleAirplaneMode(String mode, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "toggleAirplaneMode", mode, flowControl)
    }

    /**
     * Simulate toggling airplane mode on mobile devices
     * @param mode
     *          ["yes", "on", "true"] to turn on airplane mode; otherwise, airplane mode is turn off
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_UTILITIES)
    public static void toggleAirplaneMode(String mode) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "toggleAirplaneMode", mode)
    }

    /**
     * Running the active application in background
     * @param seconds
     *      amounts of time (in seconds) for the application to run in background
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_APPLICATION)
    public static void runIOSAppInBackgroundAndWait(int seconds, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "runIOSAppInBackgroundAndWait", seconds, flowControl)
    }

    /**
     * Running the active application in background
     * @param seconds
     *      amounts of time (in seconds) for the application to run in background
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_APPLICATION)
    public static void runIOSAppInBackgroundAndWait(int seconds) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "runIOSAppInBackgroundAndWait", seconds)
    }

    /**
     * Get text of a mobile element
     * @param to
     *      represent a mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return
     *      the text of the mobile element
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_TEXT)
    public static String getText(TestObject to, int timeout, FailureHandling flowControl) throws StepFailedException {
        return (String) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "getText", to, timeout, flowControl)
    }

    /**
     * Get text of a mobile element
     * @param to
     *      represent a mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @return
     *      the text of the mobile element
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_TEXT)
    public static String getText(TestObject to, int timeout) throws StepFailedException {
        return (String) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "getText", to, timeout)
    }

    /**
     * Set text to a mobile element
     * @param to
     *      represent a mobile element
     * @param text
     *      the text to set to the mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_TEXT)
    public static void setText(TestObject to, String text, int timeout, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "setText", to, text, timeout, flowControl)
    }

    /**
     * Set text to a mobile element
     * @param to
     *      represent a mobile element
     * @param text
     *      the text to set to the mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_TEXT)
    public static void setText(TestObject to, String text, int timeout) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "setText", to, text, timeout)
    }

    /**
     * Tap on an mobile element
     * @param to
     *      represent a mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_SCREEN)
    public static void tap(TestObject to, int timeout, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "tap", to, timeout, flowControl)
    }

    /**
     * Tap on an mobile element
     * @param to
     *      represent a mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_SCREEN)
    public static void tap(TestObject to, int timeout) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "tap", to, timeout)
    }

    /**
     * Tap and hold on a mobile element for a duration
     * @param to
     *      represent a mobile element
     * @param duration
     *      duration (in seconds) that the tap is hold on the element, if set to <= 0 then will use default duration
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void tapAndHold(TestObject to, Number duration, int timeout, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "tapAndHold", to, duration, timeout, flowControl)
    }

    /**
     * Tap and hold on a mobile element for a duration
     * @param to
     *      represent a mobile element
     * @param duration
     *      duration (in seconds) that the tap is hold on the element, if set to <= 0 then will use default duration
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void tapAndHold(TestObject to, Number duration, int timeout) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "tapAndHold", to, duration, timeout)
    }

    /**
     * Get a specific attribute of a mobile element
     * @param to
     *      represent a mobile element
     * @param name
     *      name of the attribute to get
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return
     *      value of the attribute
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ATTRIBUTE)
    public static String getAttribute(TestObject to, String name, int timeout, FailureHandling flowControl) throws StepFailedException {
        return (String) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "getAttribute", to, name, timeout, flowControl)
    }

    /**
     * Get a specific attribute of a mobile element
     * @param to
     *      represent a mobile element
     * @param name
     *      name of the attribute to get
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @return
     *      value of the attribute
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ATTRIBUTE)
    public static String getAttribute(TestObject to, String name, int timeout) throws StepFailedException {
        return (String) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "getAttribute", to, name, timeout)
    }

    /**
     * Wait for a mobile element to present
     * @param to
     *      represent a mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return
     *      true if the element is presented; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean waitForElementPresent(TestObject to, int timeout, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "waitForElementPresent", to, timeout, flowControl)
    }

    /**
     * Wait for a mobile element to present
     * @param to
     *      represent a mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @return
     *      true if the element is presented; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean waitForElementPresent(TestObject to, int timeout) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "waitForElementPresent", to, timeout)
    }

    /**
     * Verify if a mobile element is presented
     * @param to
     *      represent a mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return
     *      true if the element is presented; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementExist(TestObject to, int timeout, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "verifyElementExist", to, timeout, flowControl)
    }

    /**
     * Verify if a mobile element is presented
     * @param to
     *      represent a mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @return
     *      true if the element is presented; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementExist(TestObject to, int timeout) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "verifyElementExist", to, timeout)
    }

    /**
     * Verify if a mobile element is NOT presented
     * @param to
     *      represent a mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return
     *      true if the element is NOT presented; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementNotExist(TestObject to, int timeout, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "verifyElementNotExist", to, timeout, flowControl)
    }

    /**
     * Verify if a mobile element is NOT presented
     * @param to
     *      represent a mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @return
     *      true if the element is NOT presented; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementNotExist(TestObject to, int timeout) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "verifyElementNotExist", to, timeout)
    }

    /**
     * Clear text of a mobile element
     * @param to
     *      represent a mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_TEXT)
    public static void clearText(TestObject to, int timeout, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "clearText", to, timeout, flowControl)
    }

    /**
     * Clear text of a mobile element
     * @param to
     *      represent a mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_TEXT)
    public static void clearText(TestObject to, int timeout) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "clearText", to, timeout)
    }

    /**
     * Verify if current device is in landscape mode
     * @param flowControl
     * @return
     *      true if the device is in landscape mode ; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static boolean verifyIsLandscape(FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "verifyIsLandscape", flowControl)
    }

    /**
     * Verify if current device is in landscape mode
     * @return
     *      true if the device is in landscape mode ; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static boolean verifyIsLandscape() throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "verifyIsLandscape")
    }

    /**
     * Verify if current device is in portrait mode
     * @param flowControl
     * @return
     *      true if the device is in portrait mode ; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static boolean verifyIsPortrait(FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "verifyIsPortrait", flowControl)
    }

    /**
     * Verify if current device is in portrait mode
     * @return
     *      true if the device is in portrait mode ; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static boolean verifyIsPortrait() throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "verifyIsPortrait")
    }

    /**
     * Switch the current device's mode to landscape mode
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static boolean switchToLandscape(FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "switchToLandscape", flowControl)
    }

    /**
     * Switch the current device's mode to landscape mode
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static boolean switchToLandscape() throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "switchToLandscape")
    }

    /**
     * Switch the current device's mode to portrait mode
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static boolean switchToPortrait(FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "switchToPortrait", flowControl)
    }

    /**
     * Switch the current device's mode to portrait mode
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static boolean switchToPortrait() throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "switchToPortrait")
    }

    /**
     * Get current screen orientation of the device
     * @param flowControl
     * @return current screen orientation (portrait, landscape)
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static String getCurrentOrientation(FailureHandling flowControl) throws StepFailedException {
        return (String) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "getCurrentOrientation", flowControl)
    }

    /**
     * Get current screen orientation of the device
     * @return current screen orientation (portrait, landscape)
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static String getCurrentOrientation() throws StepFailedException {
        return (String) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "getCurrentOrientation")
    }

    /**
     * Switch the current device driver to web view context
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static void switchToWebView(FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "switchToWebView", flowControl)
    }

    /**
     * Switch the current device driver to web view context
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static void switchToWebView() throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "switchToWebView")
    }

    /**
     * Switch the current device driver to native context
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static void switchToNative(FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "switchToNative", flowControl)
    }

    /**
     * Switch the current device driver to native context
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static void switchToNative() throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "switchToNative")
    }


    /**
     * Scroll to an element which contains the given text.
     * @param text : text of an element to scroll to
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_TEXT)
    public static void scrollToText(String text, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "scrollToText", text, flowControl)
    }

    /**
     * Scroll to an element which contains the given text.
     * @param text : text of an element to scroll to
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_TEXT)
    public static void scrollToText(String text) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "scrollToText", text)
    }

    /**
     * Verify if a mobile element is visible
     * @param to
     *      represent a mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return
     *      true if the element is visible; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementVisible(TestObject to, int timeout, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "verifyElementVisible", to, timeout, flowControl)
    }

    /**
     * Verify if a mobile element is visible
     * @param to
     *      represent a mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @return
     *      true if the element is visible; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementVisible(TestObject to, int timeout) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "verifyElementVisible", to, timeout)
    }

    /**
     * Verify if a mobile element is NOT visible
     * @param to
     *      represent a mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return
     *      true if the element is NOT exists or is NOT visible; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementNotVisible(TestObject to, int timeout, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "verifyElementNotVisible", to, timeout, flowControl)
    }

    /**
     * Verify if a mobile element is NOT visible
     * @param to
     *      represent a mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @return
     *      true if the element is NOT exists or is NOT visible; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementNotVisible(TestObject to, int timeout) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "verifyElementNotVisible", to, timeout)
    }

    /**
     * Get device's physical width
     * @param flowControl
     * @return device's physical width
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static int getDeviceWidth(FailureHandling flowControl) throws StepFailedException {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "getDeviceWidth", flowControl)
    }

    /**
     * Get device's physical width
     * @return device's physical width
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static int getDeviceWidth() throws StepFailedException {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "getDeviceWidth")
    }

    /**
     * Get device's physical height
     * @param flowControl
     * @return device's physical height
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static int getDeviceHeight(FailureHandling flowControl) throws StepFailedException {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "getDeviceHeight", flowControl)
    }

    /**
     * Get device's physical height
     * @return device's physical height
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static int getDeviceHeight() throws StepFailedException {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "getDeviceHeight")
    }

    /**
     * Verify if the element has an attribute with the specific name
     * @param to
     *      represent a mobile element
     * @param attributeName
     *      the name of the attribute to verify
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return true if element has the attribute with the specific name; otherwise, false
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementHasAttribute(TestObject to, String attributeName, int timeout, FailureHandling flowControl) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "verifyElementHasAttribute", to, attributeName, timeout, flowControl)
    }

    /**
     * Verify if the element has an attribute with the specific name
     * @param to
     *      represent a mobile element
     * @param attributeName
     *      the name of the attribute to verify
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @return true if element has the attribute with the specific name; otherwise, false
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementHasAttribute(TestObject to, String attributeName, int timeout) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "verifyElementHasAttribute", to, attributeName, timeout)
    }

    /**
     * Verify if the element doesn't have an attribute with the specific name
     * @param to
     *      represent a mobile element
     * @param attributeName
     *      the name of the attribute to verify
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return true if element has the attribute with the specific name; otherwise, false
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementNotHasAttribute(TestObject to, String attributeName, int timeout, FailureHandling flowControl) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "verifyElementNotHasAttribute", to, attributeName, timeout, flowControl)
    }

    /**
     * Verify if the element doesn't have an attribute with the specific name
     * @param to
     *      represent a mobile element
     * @param attributeName
     *      the name of the attribute to verify
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @return true if element has the attribute with the specific name; otherwise, false
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementNotHasAttribute(TestObject to, String attributeName, int timeout) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "verifyElementNotHasAttribute", to, attributeName, timeout)
    }

    /**
     * Verify if the element has an attribute with the specific name and value
     * @param to
     *      represent a mobile element
     * @param attributeName
     *      the name of the attribute to verify
     * @param attributeValue
     *       the value of the attribute to verify
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return true if element has the attribute with the specific name and value; otherwise, false
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementAttributeValue(TestObject to, String attributeName, String attributeValue, int timeout, FailureHandling flowControl) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "verifyElementAttributeValue", to, attributeName, attributeValue, timeout, flowControl)
    }

    /**
     * Verify if the element has an attribute with the specific name and value
     * @param to
     *      represent a mobile element
     * @param attributeName
     *      the name of the attribute to verify
     * @param attributeValue
     *       the value of the attribute to verify
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @return true if element has the attribute with the specific name and value; otherwise, false
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementAttributeValue(TestObject to, String attributeName, String attributeValue, int timeout) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "verifyElementAttributeValue", to, attributeName, attributeValue, timeout)
    }

    /**
     * Wait until the given web element has an attribute with the specific name
     * @param to
     *      represent a mobile element
     * @param attributeName
     *      the name of the attribute to wait for
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return true if element has the attribute with the specific name; otherwise, false
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean waitForElementHasAttribute(TestObject to, String attributeName, int timeout, FailureHandling flowControl) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "waitForElementHasAttribute", to, attributeName, timeout, flowControl)
    }

    /**
     * Wait until the given web element has an attribute with the specific name
     * @param to
     *      represent a mobile element
     * @param attributeName
     *      the name of the attribute to wait for
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @return true if element has the attribute with the specific name; otherwise, false
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean waitForElementHasAttribute(TestObject to, String attributeName, int timeout) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "waitForElementHasAttribute", to, attributeName, timeout)
    }

    /**
     * Wait until the given web element doesn't have an attribute with the specific name
     * @param to
     *      represent a web element
     * @param attributeName
     *      the name of the attribute to wait for
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return true if element doesn't have the attribute with the specific name; otherwise, false
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean waitForElementNotHasAttribute(TestObject to, String attributeName, int timeout, FailureHandling flowControl) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "waitForElementNotHasAttribute", to, attributeName, timeout, flowControl)
    }

    /**
     * Wait until the given web element doesn't have an attribute with the specific name
     * @param to
     *      represent a web element
     * @param attributeName
     *      the name of the attribute to wait for
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @return true if element doesn't have the attribute with the specific name; otherwise, false
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean waitForElementNotHasAttribute(TestObject to, String attributeName, int timeout) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "waitForElementNotHasAttribute", to, attributeName, timeout)
    }

    /**
     * Wait until the given web element has an attribute with the specific name and value
     * @param to
     *      represent a mobile element
     * @param attributeName
     *      the name of the attribute to wait for
     * @param attributeValue
     *      the value of the attribute to wait for
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return true if element has the attribute with the specific name and value; otherwise, false
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean waitForElementAttributeValue(TestObject to, String attributeName, String attributeValue, int timeout, FailureHandling flowControl) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "waitForElementAttributeValue", to, attributeName, attributeValue, timeout, flowControl)
    }

    /**
     * Wait until the given web element has an attribute with the specific name and value
     * @param to
     *      represent a mobile element
     * @param attributeName
     *      the name of the attribute to wait for
     * @param attributeValue
     *      the value of the attribute to wait for
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @return true if element has the attribute with the specific name and value; otherwise, false
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean waitForElementAttributeValue(TestObject to, String attributeName, String attributeValue, int timeout) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "waitForElementAttributeValue", to, attributeName, attributeValue, timeout)
    }


    /**
     * Drag and drop an element into another element
     * @param fromObject
     *      represent the drag-able mobile element
     * @param toObject
     *      represent the drop-able mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void dragAndDrop(TestObject fromObject, TestObject toObject, int timeout, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "dragAndDrop", fromObject, toObject, timeout, flowControl)
    }

    /**
     * Drag and drop an element into another element
     * @param fromObject
     *      represent the drag-able mobile element
     * @param toObject
     *      represent the drop-able mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void dragAndDrop(TestObject fromObject, TestObject toObject, int timeout) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "dragAndDrop", fromObject, toObject, timeout)
    }

    /**
     * Set the value for Slider control (android.widget.SeekBar for Android, UIASlider for iOS) at specific percentage
     * @param to
     *      represent a mobile element (android.widget.SeekBar for Android, UIASlider for iOS)
     * @param percent
     *      percentage value to set to the slider ( 0 <= percent <= 100 )
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void setSliderValue(TestObject to, Number percent, int timeOut, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "setSliderValue", to, percent, timeOut, flowControl)
    }

    /**
     * Set the value for Slider control (android.widget.SeekBar for Android, UIASlider for iOS) at specific percentage
     * @param to
     *      represent a mobile element (android.widget.SeekBar for Android, UIASlider for iOS)
     * @param percent
     *      percentage value to set to the slider ( 0 <= percent <= 100 )
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void setSliderValue(TestObject to, Number percent, int timeOut) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "setSliderValue", to, percent, timeOut)
    }

    /** Hide the keyboard if it is showing
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static void hideKeyboard(FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "hideKeyboard", flowControl)
    }

    /**
     * Hide the keyboard if it is showing
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static void hideKeyboard() throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "hideKeyboard")
    }

    /**
     * Check a check-box mobile element (android.widget.CheckBox for Android, UIASwitch for iOS)
     * @param to
     *      represent a mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void checkElement(TestObject to, int timeout, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "checkElement", to, timeout, flowControl)
    }

    /**
     * Check a check-box mobile element (android.widget.CheckBox for Android, UIASwitch for iOS)
     * @param to
     *      represent a mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void checkElement(TestObject to, int timeout) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "checkElement", to, timeout)
    }

    /**
     * Un-check a check-box mobile element (android.widget.CheckBox for Android, UIASwitch for iOS)
     * @param to
     *      represent a mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void uncheckElement(TestObject to, int timeout, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "uncheckElement", to, timeout, flowControl)
    }

    /**
     * Un-check a check-box mobile element (android.widget.CheckBox for Android, UIASwitch for iOS)
     * @param to
     *      represent a mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void uncheckElement(TestObject to, int timeout) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "uncheckElement", to, timeout)
    }

    /**
     * Verify if a mobile element is checked
     * @param to
     *      represent a mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return
     *      true if the element is checked; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementChecked(TestObject to, int timeout, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "verifyElementChecked", to, timeout, flowControl)
    }

    /**
     * Verify if a mobile element is checked
     * @param to
     *      represent a mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @return
     *      true if the element is checked; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementChecked(TestObject to, int timeout) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "verifyElementChecked", to, timeout)
    }

    /**
     * Verify if a mobile element is not checked
     * @param to
     *      represent a mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return
     *      true if the element is not checked; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementNotChecked(TestObject to, int timeout, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "verifyElementNotChecked", to, timeout, flowControl)
    }

    /**
     * Verify if a mobile element is not checked
     * @param to
     *      represent a mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @return
     *      true if the element is not checked; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementNotChecked(TestObject to, int timeout) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "verifyElementNotChecked", to, timeout)
    }

    /**
     * Select item of list view control by its label.
     * @param to 
     *      represent a mobile element
     * @param label 
     *      item label
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void selectListItemByLabel(TestObject to, String label, int timeout, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "selectListItemByLabel", to, label, timeout, flowControl)
    }

    /**
     * Select item of list view control by its label.
     * @param to 
     *      represent a mobile element
     * @param label 
     *      item label
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void selectListItemByLabel(TestObject to, String label, int timeout) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "selectListItemByLabel", to, label, timeout)
    }

    /**
     * Unlock device screen
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static void unlockScreen(FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "unlockScreen", flowControl)
    }

    /**
     * Unlock device screen
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static void unlockScreen() throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "unlockScreen")
    }

    /**
     *  Tap at a specific position on the screen of the mobile device
     * @param x
     *      x position
     * @param y
     *      y position
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_SCREEN)
    public static void tapAtPosition(Number x, Number y, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "tapAtPosition", x, y, flowControl)
    }

    /**
     *  Tap at a specific position on the screen of the mobile device
     * @param x
     *      x position
     * @param y
     *      y position
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_SCREEN)
    public static void tapAtPosition(Number x, Number y) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "tapAtPosition", x, y)
    }

    /**
     *  Tap and hold at a specific position on the screen of the mobile device
     * @param x
     *      x position
     * @param y
     *      y position
     * @param duration
     *      duration (in seconds) that the tap is hold on the element, if set to <= 0 then will use default duration
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_SCREEN)
    public static void tapAndHoldAtPosition(Number x, Number y, Number duration, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "tapAndHoldAtPosition", x, y, duration, flowControl)
    }

    /**
     *  Tap and hold at a specific position on the screen of the mobile device
     * @param x
     *      x position
     * @param y
     *      y position
     * @param duration
     *      duration (in seconds) that the tap is hold on the element, if set to <= 0 then will use default duration
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_SCREEN)
    public static void tapAndHoldAtPosition(Number x, Number y, Number duration) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "tapAndHoldAtPosition", x, y, duration)
    }

    /**
     *  Pinch to zoom in at a specific position on the screen of the mobile device
     * @param x
     *      x position
     * @param y
     *      y position
     * @param offset
     *      the offset length to pinch
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_SCREEN)
    public static void pinchToZoomInAtPosition(Number x, Number y, Number offset, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "pinchToZoomInAtPosition", x, y, offset, flowControl)
    }

    /**
     *  Pinch to zoom in at a specific position on the screen of the mobile device
     * @param x
     *      x position
     * @param y
     *      y position
     * @param offset
     *      the offset length to pinch
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_SCREEN)
    public static void pinchToZoomInAtPosition(Number x, Number y, Number offset) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "pinchToZoomInAtPosition", x, y, offset)
    }

    /**
     * Get the width of mobile element
     * @param to 
     *      represent a mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return 
     *      width of the element
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static int getElementWidth(TestObject to, int timeout, FailureHandling flowControl) throws StepFailedException {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "getElementWidth", to, timeout, flowControl)
    }

    /**
     * Get the width of mobile element
     * @param to 
     *      represent a mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @return 
     *      width of the element
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static int getElementWidth(TestObject to, int timeout) throws StepFailedException {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "getElementWidth", to, timeout)
    }

    /**
     *  Pinch to zoom out at a specific position on the screen of the mobile device
     * @param x
     *      x position
     * @param y
     *      y position
     * @param offset
     *      the offset length to pinch
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_SCREEN)
    public static void pinchToZoomOutAtPosition(Number x, Number y, Number offset, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "pinchToZoomOutAtPosition", x, y, offset, flowControl)
    }

    /**
     *  Pinch to zoom out at a specific position on the screen of the mobile device
     * @param x
     *      x position
     * @param y
     *      y position
     * @param offset
     *      the offset length to pinch
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_SCREEN)
    public static void pinchToZoomOutAtPosition(Number x, Number y, Number offset) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "pinchToZoomOutAtPosition", x, y, offset)
    }

    /**
     * Get the top position of mobile element
     * @param to mobile element object
     * @param timeout
     * @param flowControl
     * @return element top position
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static int getElementTopPosition(TestObject to, int timeout, FailureHandling flowControl) throws StepFailedException {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "getElementTopPosition", to, timeout, flowControl)
    }

    /**
     * Get the top position of mobile element
     * @param to mobile element object
     * @param timeout
     * @return element top position
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static int getElementTopPosition(TestObject to, int timeout) throws StepFailedException {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "getElementTopPosition", to, timeout)
    }

    /**
     * Get the height of mobile element
     * @param to 
     *      represent a mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return 
     *      height of the element
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static int getElementHeight(TestObject to, int timeout, FailureHandling flowControl) throws StepFailedException {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "getElementHeight", to, timeout, flowControl)
    }

    /**
     * Get the height of mobile element
     * @param to 
     *      represent a mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @return 
     *      height of the element
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static int getElementHeight(TestObject to, int timeout) throws StepFailedException {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "getElementHeight", to, timeout)
    }

    /**
     * Get the left position of mobile element
     * @param to
     *      represent a mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return 
     *      the left position of the mobile element
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static int getElementLeftPosition(TestObject to, int timeout, FailureHandling flowControl) throws StepFailedException {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "getElementLeftPosition", to, timeout, flowControl)
    }

    /**
     * Get the left position of mobile element
     * @param to
     *      represent a mobile element
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @return
     *      the left position of the mobile element
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_DEVICE)
    public static int getElementLeftPosition(TestObject to, int timeout) throws StepFailedException {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "getElementLeftPosition", to, timeout)
    }

    /**
     * Select item of list view control by its index. Have not implemented for Android because its list view is async loaded
     * @param to 
     *      represent a mobile element
     * @param index 
     *      item index (1-based indexed)
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void selectListItemByIndex(TestObject to, int index, int timeout, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "selectListItemByIndex", to, index, timeout, flowControl)
    }

    /**
     * Select item of list view control by its index. Have not implemented for Android because its list view is async loaded
     * @param to 
     *      represent a mobile element
     * @param index 
     *      item index (1-based indexed)
     * @param timeout
     *      system will wait at most timeout (seconds) to return result
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void selectListItemByIndex(TestObject to, int index, int timeout) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "selectListItemByIndex", to, index, timeout)
    }

    /**
     * Verify text of an element.
     *
     * @param to
     *          represent a mobile element.
     * @param expectedText
     *          text of the element to verify.
     * @param flowControl
     * @return true if the element has the desired text, otherwise false.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementText(TestObject to, String expectedText, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "verifyElementText", to, expectedText, flowControl)
    }

    /**
     * Verify text of an element.
     *
     * @param to
     *          represent a mobile element.
     * @param expectedText
     *          text of the element to verify.
     * @return true if the element has the desired text, otherwise false.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementText(TestObject to, String expectedText) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "verifyElementText", to, expectedText)
    }

    /**
     * Simulates keystroke events on the specified element, as though you typed the value key-by-key.
     *
     * @param to
     *          represent a mobile element.
     * @param strokeKeys
     *          the combination of keys to type
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void sendKeys(TestObject to, String strokeKeys, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "sendKeys", to, strokeKeys, flowControl)
    }

    /**
     * Simulates keystroke events on the specified element, as though you typed the value key-by-key.
     *
     * @param to
     *          represent a mobile element.
     * @param strokeKeys
     *          the combination of keys to type
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void sendKeys(TestObject to, String strokeKeys) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_MOBILE, "sendKeys", to, strokeKeys)
    }
}