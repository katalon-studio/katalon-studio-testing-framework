package com.kms.katalon.core.windows.keyword;

import java.util.List;

import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;

import com.kms.katalon.core.annotation.Keyword;
import com.kms.katalon.core.exception.StepFailedException;
import com.kms.katalon.core.keyword.BuiltinKeywords;
import com.kms.katalon.core.keyword.internal.KeywordExecutor;
import com.kms.katalon.core.model.FailureHandling;
import com.kms.katalon.core.testobject.WindowsTestObject;
import com.kms.katalon.core.windows.constants.StringConstants;
import com.kms.katalon.core.windows.driver.WindowsDriverFactory;
import com.kms.katalon.core.windows.keyword.helper.WindowsActionSettings;

import io.appium.java_client.windows.WindowsDriver;

public class WindowsBuiltinKeywords extends BuiltinKeywords {

    /**
     * Starts Windows driver and starts the Windows application at the given absolute path.
     * 
     * @param appFile
     * Absolute path to the Windows application.
     * @throws StepFailedException
     * If KS could not start Windows Driver, could not start the application or the application file doesn't exist.
     * @since 7.0.0
     */
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_APPLICATION)
    public static void startApplication(String appFile) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "startApplication", appFile);
    }

    /**
     * Starts Windows driver and starts the Windows application at the given absolute path.
     * 
     * @param appFile
     * Absolute path to the Windows application.
     * @param flowControl
     * Optional parameter: Controls the execution flow if the step failed.
     * <p>
     * <ul>
     * <li>STOP_ON_FAILURE: throws {@link StepFailedException} if the step failed (default).</li>
     * <li>CONTINUE_ON_FAILURE: continues the test if the test failed but the test result is still failed.</li>
     * <li>OPTIONAL: continues the test and ignore the test result.</li>
     * </ul>
     * @throws StepFailedException
     * If KS could not start Windows Driver, could not start the application or the application file doesn't exist.
     * @since 7.0.0
     */
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_APPLICATION)
    public static void startApplication(String appFile, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "startApplication", appFile,
                appFile);
    }

    /**
     * Performs a click action on the {@link WebElement} that is found by using locator value of the given
     * windowsObject.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find Windows Element.
     * @throws StepFailedException
     * If the Windows element doesn't exist, or KS could not perform click action on the element.
     * @since 7.0.0
     */
    @Keyword(keywordObject = "Element")
    public static void click(WindowsTestObject windowsObject) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "click", windowsObject);
    }
    
    /**
     * Performs a click action at the given offset of the windows element
     * (relative to its top-left corner) that is found by using locator value of
     * the given windowsObject.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find
     * Windows Element.
     * @param offsetX
     * The horizontal offset relative to the top-left corner of the
     * element.
     * @param offsetY
     * The vertical offset relative to the top-left corner of the
     * element.
     * @throws StepFailedException
     * If the Windows element doesn't exist, or KS could not perform
     * click action on the element. <a>
     * @since 7.7.5
     */
    @Keyword(keywordObject = "Element")
    public static void clickElementOffset(WindowsTestObject windowsObject, int offsetX, int offsetY)
            throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "clickElementOffset", windowsObject,
                offsetX, offsetY);
    }

    /**
     * Performs a click action at the given offset on the windows element
     * (relative to its top-left corner) that is found by using locator value of
     * the given windowsObject.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find
     * Windows Element.
     * @param offsetX
     * The horizontal offset relative to the top-left corner of the
     * element.
     * @param offsetY
     * The vertical offset relative to the top-left corner of the
     * element.
     * @param flowControl
     * Optional parameter: Controls the execution flow if the step
     * failed.
     * <p>
     * <ul>
     * <li>STOP_ON_FAILURE: throws {@link StepFailedException} if the
     * step failed (default).</li>
     * <li>CONTINUE_ON_FAILURE: continues the test if the test failed
     * but the test result is still failed.</li>
     * <li>OPTIONAL: continues the test and ignore the test result.
     * </li>
     * </ul>
     * @throws StepFailedException
     * If the Windows element doesn't exist, or KS could not perform
     * click action on the element.
     * @since 7.7.5
     */
    @Keyword(keywordObject = "Element")
    public static void clickElementOffset(WindowsTestObject windowsObject, int offsetX, int offsetY,
            FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "clickElementOffset", windowsObject,
                offsetX, offsetY, flowControl);
    }

    /**
     * Performs a click action on the {@link WebElement} that is found by using locator value of the given
     * windowsObject.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find Windows Element.
     * @param flowControl
     * Optional parameter: Controls the execution flow if the step failed.
     * <p>
     * <ul>
     * <li>STOP_ON_FAILURE: throws {@link StepFailedException} if the step failed (default).</li>
     * <li>CONTINUE_ON_FAILURE: continues the test if the test failed but the test result is still failed.</li>
     * <li>OPTIONAL: continues the test and ignore the test result.</li>
     * </ul>
     * @throws StepFailedException
     * If the Windows element doesn't exist, or KS could not perform click action on the element.
     * @since 7.0.0
     */
    @Keyword(keywordObject = "Element")
    public static void click(WindowsTestObject windowsObject, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "click", windowsObject,
                flowControl);
    }

    /**
     * Performs a double-click action on the {@link WebElement} that is found by using locator value of the given
     * windowsObject.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find Windows Element.
     * @throws StepFailedException
     * If the Windows element doesn't exist, or KS could not perform double-click action on the element.
     * @since 7.0.0
     */
    @Keyword(keywordObject = "Element")
    public static void doubleClick(WindowsTestObject windowsObject) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "doubleClick", windowsObject);
    }

    /**
     * Performs a double-click action on the {@link WebElement} that is found by using locator value of the given
     * windowsObject.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find Windows Element.
     * @param flowControl
     * Optional parameter: Controls the execution flow if the step failed.
     * <p>
     * <ul>
     * <li>STOP_ON_FAILURE: throws {@link StepFailedException} if the step failed (default).</li>
     * <li>CONTINUE_ON_FAILURE: continues the test if the test failed but the test result is still failed.</li>
     * <li>OPTIONAL: continues the test and ignore the test result.</li>
     * </ul>
     * @throws StepFailedException
     * If the Windows element doesn't exist, or KS could not perform double-click action on the element.
     * @since 7.0.0
     */
    @Keyword(keywordObject = "Element")
    public static void doubleClick(WindowsTestObject windowsObject, FailureHandling flowControl)
            throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "doubleClick", windowsObject,
                flowControl);
    }

    /**
     * Performs a right-click action on the {@link WebElement} that is found by using locator value of the given
     * windowsObject.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find Windows Element.
     * @throws StepFailedException
     * If the Windows element doesn't exist, or KS could not perform right-click action on the element.
     * @since 7.0.0
     */
    @Keyword(keywordObject = "Element")
    public static void rightClick(WindowsTestObject windowsObject) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "rightClick", windowsObject);
    }
    
    /**
     * Performs a right-click action at the provided offset on the
     * windows element that is found by using locator value of the given
     * windowsObject.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find
     * Windows Element.
     * @param offsetX
     * The horizontal offset relative to the top-left corner of the
     * element.
     * @param offsetY
     * The vertical offset relative to the top-left corner of the
     * element.
     * @throws StepFailedException
     * If the Windows element doesn't exist, or KS could not perform
     * click action on the element. <a>
     * @since 7.7.5
     */
    @Keyword(keywordObject = "Element")
    public static void rightClickElementOffset(WindowsTestObject windowsObject, int offsetX, int offsetY)
            throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "rightClickElementOffset",
                windowsObject, offsetX, offsetY);
    }

    /**
     * Performs a right-click action at the provided offset on the
     * windows element that is found by using locator value of the given
     * windowsObject.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find
     * Windows Element.
     * @param offsetX
     * The horizontal offset relative to the top-left corner of the
     * element.
     * @param offsetY
     * The vertical offset relative to the top-left corner of the
     * element.
     * @param flowControl
     * Optional parameter: Controls the execution flow if the step
     * failed.
     * <p>
     * <ul>
     * <li>STOP_ON_FAILURE: throws {@link StepFailedException} if the
     * step failed (default).</li>
     * <li>CONTINUE_ON_FAILURE: continues the test if the test failed
     * but the test result is still failed.</li>
     * <li>OPTIONAL: continues the test and ignore the test result.
     * </li>
     * </ul>
     * @throws StepFailedException
     * If the Windows element doesn't exist, or KS could not perform
     * click action on the element. <a>
     * @since 7.7.5
     */
    @Keyword(keywordObject = "Element")
    public static void rightClickElementOffset(WindowsTestObject windowsObject, int offsetX, int offsetY,
            FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "rightClickElementOffset",
                windowsObject, offsetX, offsetY, flowControl);
    }

    /**
     * Performs a right-click action on the {@link WebElement} that is found by using locator value of the given
     * windowsObject.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find Windows Element.
     * @param flowControl
     * Optional parameter: Controls the execution flow if the step failed.
     * <p>
     * <ul>
     * <li>STOP_ON_FAILURE: throws {@link StepFailedException} if the step failed (default).</li>
     * <li>CONTINUE_ON_FAILURE: continues the test if the test failed but the test result is still failed.</li>
     * <li>OPTIONAL: continues the test and ignore the test result.</li>
     * </ul>
     * @throws StepFailedException
     * If the Windows element doesn't exist, or KS could not perform right-click action on the element.
     * @since 7.0.0
     */
    @Keyword(keywordObject = "Element")
    public static void rightClick(WindowsTestObject windowsObject, FailureHandling flowControl)
            throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "rightClick", windowsObject,
                flowControl);
    }

    /**
     * Performs a set text action on the {@link WebElement} that is found by using locator value of the given
     * windowsObject.<br>
     * This action will clear the current text and append the given text on the element.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find Windows Element.
     * @param text
     * The text content to set on the element.
     * @throws StepFailedException
     * If the windowsObject doesn't exist, or KS could not perform set text action on the element.
     * @throws IllegalArgumentException
     * If the windowsObject is null.
     * @since 7.0.0
     */
    @Keyword(keywordObject = "Element")
    public static void setText(WindowsTestObject windowsObject, String text) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "setText", windowsObject, text);
    }

    /**
     * Performs a set text action on the {@link WebElement} that is found by using locator value of the given
     * windowsObject.<br>
     * This action will clear the current text and append the given text on the element.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find Windows Element.
     * @param text
     * The text content to set on the element.
     * @param flowControl
     * Optional parameter: Controls the execution flow if the step failed.
     * <p>
     * <ul>
     * <li>STOP_ON_FAILURE: throws {@link StepFailedException} if the step failed (default).</li>
     * <li>CONTINUE_ON_FAILURE: continues the test if the test failed but the test result is still failed.</li>
     * <li>OPTIONAL: continues the test and ignore the test result.</li>
     * </ul>
     * @throws StepFailedException
     * If the Windows element doesn't exist, or KS could not perform set text action on the element.
     * @throws IllegalArgumentException
     * If the windowsObject is null.
     * @since 7.0.0
     */
    @Keyword(keywordObject = "Element")
    public static void setText(WindowsTestObject windowsObject, String text, FailureHandling flowControl)
            throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "setText", windowsObject, text,
                flowControl);
    }

    /**
     * Performs a set text action on the {@link WebElement} that is found by using locator value of the given
     * windowsObject.<br>
     * This action will clear the current text and append the new encrypted text on the element.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find Windows element.
     * @param encryptedText
     * The encrypted text content to set on the element, not null.
     * <ul>
     * <li>Throws {@link ArrayIndexOutOfBoundsException} if something went wrong while decoding encryptedText.</li>
     * </ul>
     * @throws StepFailedException
     * If the Windows element doesn't exist, or KS could not perform set text action on the element.
     * @throws IllegalArgumentException
     * If windowsObject or encryptedText is null.
     * @throws ArrayIndexOutOfBoundsException
     * If something went wrong while decoding encryptedText.
     * Encrypted test was an invalid format. Please check your encrypted
     * text with Help > Encrypt Text on the Main Menu.
     * @since 7.6.0
     */
    @Keyword(keywordObject = "Element")
    public static void setEncryptedText(WindowsTestObject windowsObject, String encryptedText)
            throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "setEncryptedText", windowsObject,
                encryptedText);
    }

    /**
     * Performs a set text action on the {@link WebElement} that is found by using locator value of the given
     * windowsObject.<br>
     * This action will clear the current text and append the new encrypted text on the element.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find Windows element.
     * @param encryptedText
     * The encrypted text content to set on the element, not null.
     * <ul>
     * <li>Throws {@link ArrayIndexOutOfBoundsException} if something went wrong while decoding encryptedText.</li>
     * </ul>
     * @param flowControl
     * Optional parameter: Controls the execution flow if the step failed.
     * <ul>
     * <li>STOP_ON_FAILURE: throws {@link StepFailedException} if the step failed (default).</li>
     * <li>CONTINUE_ON_FAILURE: continues the test if the test failed but the test result is still failed.</li>
     * <li>OPTIONAL: continues the test and ignore the test result.</li>
     * </ul>
     * @throws StepFailedException
     * If the Windows element doesn't exist, or KS could not perform set text action on the element.
     * @throws IllegalArgumentException
     * If windowsObject or encryptedText is null.
     * @throws ArrayIndexOutOfBoundsException
     * If something went wrong while decoding encryptedText.
     * Encrypted test was an invalid format. Please check your encrypted
     * text with Help > Encrypt Text on the Main Menu.
     * @since 7.6.0
     */
    @Keyword(keywordObject = "Element")
    public static void setEncryptedText(WindowsTestObject windowsObject, String encryptedText,
            FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "setEncryptedText", windowsObject,
                encryptedText, flowControl);
    }

    /**
     * Gets text content of the {@link WebElement} that is found by using locator value of the given windowsObject.<br>
     * This action will append the given text on the element and doesn't clear the current text of the element.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find Windows Element.
     * @throws StepFailedException
     * If the Windows element doesn't exist, or KS could not get text of the element.
     * @since 7.0.0
     */
    @Keyword(keywordObject = "Element")
    public static String getText(WindowsTestObject windowsObject) throws StepFailedException {
        return (String) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "getText",
                windowsObject);
    }

    /**
     * Gets text content of the {@link WebElement} that is found by using locator value of the given windowsObject.<br>
     * This action will append the given text on the element and doesn't clear the current text of the element.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find Windows Element.
     * @param flowControl
     * Optional parameter: Controls the execution flow if the step failed.
     * <p>
     * <ul>
     * <li>STOP_ON_FAILURE: throws {@link StepFailedException} if the step failed (default).</li>
     * <li>CONTINUE_ON_FAILURE: continues the test if the test failed but the test result is still failed.</li>
     * <li>OPTIONAL: continues the test and ignore the test result.</li>
     * </ul>
     * @throws StepFailedException
     * If the Windows element doesn't exist, or KS could not get text of the element.
     * @since 7.0.0
     */
    @Keyword(keywordObject = "Element")
    public static String getText(WindowsTestObject windowsObject, FailureHandling flowControl)
            throws StepFailedException {
        return (String) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "getText",
                windowsObject, flowControl);
    }

    /**
     * Gets attribute value of a Windows element.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find Windows element.
     * @param attribute
     * Name of the attribute, not null.
     * @param flowControl
     * Optional parameter: Controls the execution flow if the step failed.
     * <ul>
     * <li>STOP_ON_FAILURE: throws {@link StepFailedException} if the step failed (default).</li>
     * <li>CONTINUE_ON_FAILURE: continues the test if the test failed but the test result is still failed.</li>
     * <li>OPTIONAL: continues the test and ignore the test result.</li>
     * </ul>
     * @return
     * Value of the attribute
     * @throws StepFailedException
     * If the Windows element doesn't exist, or KS could not get value of the element's attribute.
     * @throws IllegalArgumentException
     * If windowsObject or attribute is null.
     * @since 7.6.0
     */
    @Keyword(keywordObject = "Element")
    public static String getAttribute(WindowsTestObject windowsObject, String attribute, FailureHandling flowControl)
            throws StepFailedException {
        return (String) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "getAttribute",
                windowsObject, attribute, flowControl);
    }

    /**
     * Gets attribute value of a Windows element.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find Windows element.
     * @param attribute
     * Name of the attribute, not null.
     * @return
     * Value of the attribute
     * @throws StepFailedException
     * If the Windows element doesn't exist, or KS could not get value of the element's attribute.
     * @throws IllegalArgumentException
     * If windowsObject or attribute is null.
     * @since 7.6.0
     */
    @Keyword(keywordObject = "Element")
    public static String getAttribute(WindowsTestObject windowsObject, String attribute) throws StepFailedException {
        return (String) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "getAttribute",
                windowsObject, attribute);
    }

    /**
     * Clears text content of the {@link WebElement} that is found by using locator value of the given windowsObject.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find Windows Element.
     * @throws StepFailedException
     * If the Windows element doesn't exist, or KS could not clear text of the element.
     * @since 7.0.0
     */
    @Keyword(keywordObject = "Element")
    public static void clearText(WindowsTestObject windowsObject) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "clearText", windowsObject);
    }

    /**
     * Clears text content of the {@link WebElement} that is found by using locator value of the given windowsObject.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find Windows Element.
     * @param flowControl
     * Optional parameter: Controls the execution flow if the step failed.
     * <p>
     * <ul>
     * <li>STOP_ON_FAILURE: throws {@link StepFailedException} if the step failed (default).</li>
     * <li>CONTINUE_ON_FAILURE: continues the test if the test failed but the test result is still failed.</li>
     * <li>OPTIONAL: continues the test and ignore the test result.</li>
     * </ul>
     * @throws StepFailedException
     * If the Windows element doesn't exist, or KS could not get text of the element.
     * @since 7.0.0
     */
    @Keyword(keywordObject = "Element")
    public static void clearText(WindowsTestObject windowsObject, FailureHandling flowControl)
            throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "clearText", windowsObject,
                flowControl);
    }

    /**
     * Fires the Close event of the running application to the Windows System.<br>
     * This action is similar to pressing 'ALT + F4' and also does not force close the application.
     * <p>
     * If the application shows a confirmation to close, users need to do some extra steps to actually close the
     * application.
     * 
     * @throws StepFailedException
     * If KS could not close the Windows Driver.
     * @since 7.0.0
     */
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_APPLICATION)
    public static void closeApplication() throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "closeApplication");
    }

    /**
     * Gets the current Windows Driver.
     * 
     * @since 7.0.0
     */
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_APPLICATION)
    public static WindowsDriver<WebElement> getDriver() {
        return WindowsDriverFactory.getWindowsDriver();
    }

    /**
     * Switches the current running driver to desktop session of Windows Driver. KS will initialize another
     * WindowsDriver with 'app: Root' desired capability and the same WinAppDriver URL and Proxy settings of the
     * application driver.
     * <p>
     * All of WindowsBuiltinKeywords now are manipulated by the desktop WindowsDriver.
     * <p>
     * Depends on how many Windows elements on Desktop of the test machine, Katalon Studio may take a couples of minutes
     * to complete this action.
     * 
     * @throws StepFailedException
     * If KS could not initialize the desktop Windows Driver.
     * 
     * @since 7.0.0
     */
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_APPLICATION)
    public static void switchToDesktop() throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "switchToDesktop");
    }

    /**
     * Switches the current running driver to desktop session of Windows Driver. KS will initialize another
     * WindowsDriver with 'app: Root' desired capability and the same WinAppDriver URL and Proxy settings of the
     * application driver.
     * <p>
     * All of WindowsBuiltinKeywords now are manipulated by the desktop WindowsDriver.
     * <p>
     * Depends on how many Windows elements on Desktop of the test machine, Katalon Studio may take a couples of minutes
     * to complete this action.
     * 
     * @param flowControl
     * Optional parameter: Controls the execution flow if the step failed.
     * <p>
     * <ul>
     * <li>STOP_ON_FAILURE: throws {@link StepFailedException} if the step failed (default).</li>
     * <li>CONTINUE_ON_FAILURE: continues the test if the test failed but the test result is still failed.</li>
     * <li>OPTIONAL: continues the test and ignore the test result.</li>
     * </ul>
     * @throws StepFailedException
     * If KS could not initialize the desktop Windows Driver.
     * 
     * @since 7.0.0
     */
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_APPLICATION)
    public static void switchToDesktop(FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "switchToDesktop, flowControl");
    }

    /**
     * Switches the current running driver to the application WindowsDriver.
     * 
     * @since 7.0.0
     */
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_APPLICATION)
    public static void switchToApplication() throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "switchToApplication");
    }

    /**
     * Switches the current running driver to the application WindowsDriver.
     * 
     * @param flowControl
     * Optional parameter: Controls the execution flow if the step failed.
     * <p>
     * <ul>
     * <li>STOP_ON_FAILURE: throws {@link StepFailedException} if the step failed (default).</li>
     * <li>CONTINUE_ON_FAILURE: continues the test if the test failed but the test result is still failed.</li>
     * <li>OPTIONAL: continues the test and ignore the test result.</li>
     * </ul>
     * @since 7.0.0
     */
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_APPLICATION)
    public static void switchToApplication(FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "switchToApplication", flowControl);
    }

    /**
     * Simulates keystroke events on the specified element, as though you typed the value key-by-key
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find Windows Element.
     * 
     * @param strKeys
     * The combination of keys to type
     * 
     * @param flowControl
     * Optional parameter: Controls the execution flow if the step failed.
     * <p>
     * <ul>
     * <li>STOP_ON_FAILURE: throws {@link StepFailedException} if the step failed (default).</li>
     * <li>CONTINUE_ON_FAILURE: continues the test if the test failed but the test result is still failed.</li>
     * <li>OPTIONAL: continues the test and ignore the test result.</li>
     * </ul>
     * 
     * @throws StepFailedException
     * 
     * @since 7.0.0
     */
    @Keyword(keywordObject = "Element")
    public static void sendKeys(WindowsTestObject windowsObject, String strKeys, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "sendKeys", windowsObject, strKeys, flowControl);
    }

    /**
     * Simulates keystroke events on the specified element, as though you typed the value key-by-key
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find Windows Element.
     * 
     * @param strKeys
     * The combination of keys to type
     * 
     * @throws StepFailedException
     * 
     * @since 7.0.0
     */
    @Keyword(keywordObject = "Element")
    public static void sendKeys(WindowsTestObject windowsObject, String strKeys) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "sendKeys", windowsObject, strKeys);
    }

    /**
     * Get the position of the {@link WebElement} that is found by using locator value of the given
     * windowsObject.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find Windows Element.
     * 
     * @param flowControl
     * Optional parameter: Controls the execution flow if the step failed.
     * <ul>
     *  <li>STOP_ON_FAILURE: throws {@link StepFailedException} if the step failed (default).</li>
     *  <li>CONTINUE_ON_FAILURE: continues the test if the test failed but the test result is still failed.</li>
     *  <li>OPTIONAL: continues the test and ignore the test result.</li>
     * </ul>
     * 
     * @return
     * The element's position.
     * 
     * @throws StepFailedException
     * If KS could not find the specified element.
     * 
     * @since 7.2.0
     */
    @Keyword(keywordObject = "Element")
    public static Point getElementPosition(WindowsTestObject windowsObject, FailureHandling flowControl)
            throws StepFailedException {
        return (Point) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "getElementPosition", windowsObject, flowControl);
    }

    /**
     * Get the position of the {@link WebElement} that is found by using locator value of the given
     * windowsObject.

     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find Windows Element.
     * 
     * @return
     * The element's position.
     * 
     * @throws StepFailedException
     * If KS could not find the specified element.
     * 
     * @since 7.2.0
     */
    @Keyword(keywordObject = "Element")
    public static Point getElementPosition(WindowsTestObject windowsObject)
            throws StepFailedException {
        return (Point) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "getElementPosition", windowsObject);
    }

    /**
     * Get the bounding rectangle of the {@link WebElement} that is found by using locator value of
     * the given windowsObject.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find Windows Element.
     * 
     * @param flowControl
     * Optional parameter: Controls the execution flow if the step failed.
     * <ul>
     *  <li>STOP_ON_FAILURE: throws {@link StepFailedException} if the step failed (default).</li>
     *  <li>CONTINUE_ON_FAILURE: continues the test if the test failed but the test result is still failed.</li>
     *  <li>OPTIONAL: continues the test and ignore the test result.</li>
     * </ul>
     * 
     * @return
     * The element's bounding rectangle.
     * 
     * @throws StepFailedException
     * If KS could not find the specified element.
     * 
     * @since 7.2.0
     */
    @Keyword(keywordObject = "Element")
    public static Rectangle getElementRect(WindowsTestObject windowsObject, FailureHandling flowControl)
            throws StepFailedException {
        return (Rectangle) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "getElementRect", windowsObject, flowControl);
    }

    /**
     * Get the bounding rectangle of the {@link WebElement} that is found by using locator value of
     * the given windowsObject.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find Windows Element.
     * 
     * @return
     * The element's bounding rectangle.
     * 
     * @throws StepFailedException
     * If KS could not find the specified element.
     * 
     * @since 7.2.0
     */
    @Keyword(keywordObject = "Element")
    public static Rectangle getElementRect(WindowsTestObject windowsObject)
            throws StepFailedException {
        return (Rectangle) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "getElementRect", windowsObject);
    }

    /**
     * Finds element by using locator value of the given windowsObject.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find Windows Element.
     * 
     * @param flowControl
     * Optional parameter: Controls the execution flow if the step failed.
     * <p>
     * <ul>
     * <li>STOP_ON_FAILURE: throws {@link StepFailedException} if the step failed (default).</li>
     * <li>CONTINUE_ON_FAILURE: continues the test if the test failed but the test result is still failed.</li>
     * <li>OPTIONAL: continues the test and ignore the test result.</li>
     * </ul>
     * 
     * @return
     * The found element.
     *      
     * @throws StepFailedException
     * If KS could not find any element.
     * 
     * @since 7.0.0
     */
    @Keyword(keywordObject = "Element")
    public static WebElement findElement(WindowsTestObject windowsObject, FailureHandling flowControl)
            throws StepFailedException {
        return (WebElement) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "findElement", windowsObject);
    }


    /**
     * Finds element by using locator value of the given windowsObject.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find Windows Element.
     * 
     * @return
     * The found element.
     *      
     * @throws StepFailedException
     * If KS could not find any element.
     * 
     * @since 7.0.0
     */
    @Keyword(keywordObject = "Element")
    public static WebElement findElement(WindowsTestObject windowsObject)
            throws StepFailedException {
        return (WebElement) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "findElement", windowsObject);
    }

    /**
     * Finds elements by using locator value of the given windowsObject.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find Windows Elements.
     * 
     * @param flowControl
     * Optional parameter: Controls the execution flow if the step failed.
     * <p>
     * <ul>
     * <li>STOP_ON_FAILURE: throws {@link StepFailedException} if the step failed (default).</li>
     * <li>CONTINUE_ON_FAILURE: continues the test if the test failed but the test result is still failed.</li>
     * <li>OPTIONAL: continues the test and ignore the test result.</li>
     * </ul>
     * 
     * @return
     * The found elements.
     *      
     * @throws StepFailedException
     * If KS could not find any element.
     * 
     * @since 7.0.0
     */
    @SuppressWarnings("unchecked")
    @Keyword(keywordObject = "Element")
    public static List<WebElement> findElements(WindowsTestObject windowsObject, FailureHandling flowControl)
            throws StepFailedException {
        return (List<WebElement>) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "findElements", windowsObject, flowControl);
    }

    /**
     * Finds elements by using locator value of the given windowsObject.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find Windows Elements.
     * 
     * @return
     * The found elements.
     *      
     * @throws StepFailedException
     * If KS could not find any element.
     * 
     * @since 7.0.0
     */
    @SuppressWarnings("unchecked")
    @Keyword(keywordObject = "Element")
    public static List<WebElement> findElements(WindowsTestObject windowsObject)
            throws StepFailedException {
        return (List<WebElement>) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "findElements", windowsObject);
    }

    /**
     * Finds and attaches the opening application window that describes by the given windowsObject 
     * to the working WindowsDriver session on the current desktop. 
     * This keyword should use when:
     * <ul>
     *  <li>The main application window has been closed and replaced by another window.</li>
     *  <li>The application has multiple working windows. We can switch among these windows.</li>
     *  <li>We already have an opened application and need to switch to without reopening requires</li>
     * </ul>
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find the opening application. 
     * 
     * @param flowControl
     * Optional parameter: Controls the execution flow if the step failed.
     * <p>
     * <ul>
     * <li>STOP_ON_FAILURE: throws {@link StepFailedException} if the step failed (default).</li>
     * <li>CONTINUE_ON_FAILURE: continues the test if the test failed but the test result is still failed.</li>
     * <li>OPTIONAL: continues the test and ignore the test result.</li>
     * </ul>
     * 
     * @return The WindowsDriver after Katalon Studio attaches successfully.
     * @throws StepFailedException
     * If Katalon Studio could not find any window that matches with the given windowsObject.
     * @since 7.1.0
     */
    @SuppressWarnings("unchecked")
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_APPLICATION)
    public static WindowsDriver<WebElement> switchToWindow(WindowsTestObject windowsObject, FailureHandling flowControl) throws StepFailedException {
        return (WindowsDriver<WebElement>) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS,
                "switchToWindow", windowsObject, flowControl);
    }
    
    /**
     * Finds and attaches the opening application window that describes by the given windowsObject 
     * to the working WindowsDriver session on the current desktop. 
     * This keyword should use when:
     * <ul>
     *  <li>The main application window has been closed and replaced by another window.</li>
     *  <li>The application has multiple working windows. We can switch among these windows.</li>
     *  <li>We already have an opened application and need to switch to without reopening requires</li>
     * </ul>
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find the opening application.
     * 
     * @return The WindowsDriver after Katalon Studio switches successfully.
     * @throws StepFailedException
     * If Katalon Studio could not find any window that matches with the given windowsObject.
     * @since 7.1.0
     */
    @SuppressWarnings("unchecked")
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_APPLICATION)
    public static WindowsDriver<WebElement> switchToWindow(WindowsTestObject windowsObject) throws StepFailedException {
        return (WindowsDriver<WebElement>) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS,
                "switchToWindow", windowsObject);
    }

    /**
     * Finds and switches the opening application window to the working WindowsDriver session on the current desktop by the given title in the preset timeout (120 seconds by default). 
     * This keyword should use when:
     * <ul>
     *  <li>The main application window has been closed and replaced by another window.</li>
     *  <li>The application has multiple working windows. We can switch among these windows.</li>
     *  <li>We already have an opened application and need to switch to without reopening requires</li>
     * </ul>
     * 
     * <p>
     * Since 7.5.5, you can change the application title waiting timeout by changing: {@link WindowsActionSettings#DF_WAIT_ACTION_TIMEOUT_IN_MILLIS}
     * 
     * @param windowName
     * Title or name of the opening application windows. Full text or partial text is acceptable.
     * 
     * @param flowControl
     * Optional parameter: Controls the execution flow if the step failed.
     * <p>
     * <ul>
     * <li>STOP_ON_FAILURE: throws {@link StepFailedException} if the step failed (default).</li>
     * <li>CONTINUE_ON_FAILURE: continues the test if the test failed but the test result is still failed.</li>
     * <li>OPTIONAL: continues the test and ignore the test result.</li>
     * </ul>
     * 
     * @return The WindowsDriver after Katalon Studio switches successfully.
     * @throws StepFailedException
     * If Katalon Studio could not find any window that matches with the given name.
     * @since 7.1.0
     */
    @SuppressWarnings("unchecked")
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_APPLICATION)
    public static WindowsDriver<WebElement> switchToWindowTitle(String windowName, FailureHandling flowControl) throws StepFailedException {
        return (WindowsDriver<WebElement>) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS,
                "switchToWindowTitle", windowName, flowControl);
    }

    /**
     * Finds and switches the opening application window to the working WindowsDriver session on the current desktop by the given title in the preset timeout (120 seconds by default). 
     * This keyword should use when:
     * <ul>
     *  <li>The main application window has been closed and replaced by another window.</li>
     *  <li>The application has multiple working windows. We can switch among these windows.</li>
     *  <li>We already have an opened application and need to switch to without reopening requires</li>
     * </ul>
     * 
     * <p>
     * Since 7.5.5, you can change the application title waiting timeout by changing: {@link WindowsActionSettings#DF_WAIT_ACTION_TIMEOUT_IN_MILLIS} 
     * 
     * @param windowTitle
     * Title of the opening application windows. Full text or partial text is acceptable.
     * 
     * @return The WindowsDriver after Katalon Studio switches successfully.
     * @throws StepFailedException
     * If Katalon Studio could not find any window that matches with the given title.
     * @since 7.1.0
     */
    @SuppressWarnings("unchecked")
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_APPLICATION)
    public static WindowsDriver<WebElement> switchToWindowTitle(String windowTitle) throws StepFailedException {
        return (WindowsDriver<WebElement>) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS,
                "switchToWindowTitle", windowTitle);
    }

    /**
     * Starts Windows driver and starts the Windows application at the given absolute path.
     * After the application starts, if WinAppDriver cannot not detect the main application window correctly, Katalon Studio will use
     * the given windowTitle in the preset timeout (120 seconds by default) to find the opened application to continue working.
     * <p>
     * Since 7.5.5, you can change the application title waiting timeout by changing: {@link WindowsActionSettings#DF_WAIT_ACTION_TIMEOUT_IN_MILLIS} 
     * 
     * @param appFile
     * Absolute path to the Windows application.
     * @param windowTitle
     * Title of the opening application windows. Full text or partial text is acceptable.
     * 
     * @throws StepFailedException
     * If KS could not start Windows Driver, could not start the application, the application file doesn't exist or there is no application maches
     * with the given windowTitle
     * @since 7.1.0
     */
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_APPLICATION)
    public static void startApplicationWithTitle(String appFile, String windowTitle, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "startApplicationWithTitle", appFile, windowTitle, flowControl);
    }

    /**
     * Starts Windows driver and starts the Windows application at the given absolute path.
     * After the application starts, if WinAppDriver cannot not detect the main application window correctly, Katalon Studio will use
     * the given windowTitle in the preset timeout (120 seconds by default) to find the opened application to continue working.
     * 
     * <p>
     * Since 7.5.5, you can change the application title waiting timeout by changing: {@link WindowsActionSettings#DF_WAIT_ACTION_TIMEOUT_IN_MILLIS}
     * 
     * @param appFile
     * Absolute path to the Windows application.
     * @param windowTitle
     * Title of the opening application windows. Full text or partial text is acceptable.
     * 
     * @throws StepFailedException
     * If KS could not start Windows Driver, could not start the application, the application file doesn't exist or there is no application maches
     * with the given windowTitle
     * @since 7.1.0
     */
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_APPLICATION)
    public static void startApplicationWithTitle(String appFile, String windowTitle) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "startApplicationWithTitle", appFile, windowTitle);
    }

    /**
     * Waits until the given element has an attribute with the specific
     * name and value within the given time in second unit.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy of
     * the target element that needs to wait for.
     * @param attributeName
     * The name of the attribute name to verify, not null.
     * @param attributeValue
     * The value of the expected attribute value to verify, not null.
     * @param timeout
     * System will wait at most timeout (seconds) to return result.
     * <ul>
     * <li>If timeout = 0, Katalon Studio will use default page load timeout.
     * <li>If timeout < 0, throws {@link IllegalArgumentException}.
     * </ul>
     * @param flowControl
     * Optional parameter: Controls the execution flow if the step failed.
     * <ul>
     * <li>STOP_ON_FAILURE: throws {@link StepFailedException} if the step failed (default).</li>
     * <li>CONTINUE_ON_FAILURE: continues the test if the test failed but the test result is still failed.</li>
     * <li>OPTIONAL: continues the test and ignore the test result.</li>
     * </ul>
     * @throws StepFailedException
     * If The Windows element doesn't exist.
     * @throws IllegalArgumentException
     * If:
     * <ul>
     * <li>windowsObject, attributeName or attributeValue is null.</li>
     * <li>timeout < 0.</li>
     * </ul>
     * @return true if element has the attribute with the specific name and value; Otherwise, false.
     * @since 7.6.0
     */
    @Keyword(keywordObject = "Element")
    public static boolean verifyElementAttributeValue(WindowsTestObject windowsObject, String attributeName,
            String attributeValue, int timeout, FailureHandling flowControl) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS,
                "verifyElementAttributeValue", windowsObject, attributeName, attributeValue, timeout, flowControl);
    }

    /**
     * Waits until the given element has an attribute with the specific
     * name and value within the given time in second unit.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy of
     * the target element that needs to wait for.
     * @param attributeName
     * The name of the attribute name to verify, not null.
     * @param attributeValue
     * The value of the expected attribute value to verify, not null.
     * @param timeout
     * System will wait at most timeout (seconds) to return result.
     * <ul>
     * <li>If timeout = 0, Katalon Studio will use default page load timeout.
     * <li>If timeout < 0, throws {@link IllegalArgumentException}.
     * </ul>
     * @throws StepFailedException
     * If The Windows element doesn't exist.
     * @throws IllegalArgumentException
     * If:
     * <ul>
     * <li>windowsObject, attributeName or attributeValue is null.</li>
     * <li>timeout < 0.</li>
     * </ul>
     * @return true if element has the attribute with the specific name and value; Otherwise, false.
     * @since 7.6.0
     */
    @Keyword(keywordObject = "Element")
    public static boolean verifyElementAttributeValue(WindowsTestObject windowsObject, String attributeName,
            String attributeValue, int timeout) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS,
                "verifyElementAttributeValue", windowsObject, attributeName, attributeValue, timeout);
    }

    /**
     * Waits until the given element to present (appear) within the
     * given time in second unit.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy of
     * the target element that needs to wait for.
     * @param timeout
     * System will wait at most timeout (seconds) to return result.
     * <ul>
     * <li>If timeout = 0, Katalon Studio will use default page load timeout.
     * <li>If timeout < 0, throws {@link IllegalArgumentException}.
     * </ul>
     * @param flowControl
     * Optional parameter: Controls the execution flow if the step failed.
     * <ul>
     * <li>STOP_ON_FAILURE: throws {@link StepFailedException} if the step failed (default).</li>
     * <li>CONTINUE_ON_FAILURE: continues the test if the test failed but the test result is still failed.</li>
     * <li>OPTIONAL: continues the test and ignore the test result.</li>
     * </ul>
     * @return true if element presents; Otherwise, false.
     * @throws StepFailedException
     * If the Windows element doesn't exist.
     * @throws IllegalArgumentException
     * If:
     * <ul>
     * <li>windowsObject is null.</li>
     * <li>timeout < 0.</li>
     * </ul>
     * @since 7.6.0
     */
    @Keyword(keywordObject = "Element")
    public static boolean verifyElementPresent(WindowsTestObject windowsObject, int timeout,
            FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS,
                "verifyElementPresent", windowsObject, timeout, flowControl);
    }

    /**
     * Waits until the given element to present (appear) within the
     * given time in second unit.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy of
     * the target element that needs to wait for.
     * @param timeout
     * System will wait at most timeout (seconds) to return result.
     * <ul>
     * <li>If timeout = 0, Katalon Studio will use default page load timeout.
     * <li>If timeout < 0, throws {@link IllegalArgumentException}.
     * </ul>
     * @return true if element presents; Otherwise, false.
     * @throws StepFailedException
     * If the Windows element doesn't exist.
     * @throws IllegalArgumentException
     * If:
     * <ul>
     * <li>windowsObject is null.</li>
     * <li>timeout < 0.</li>
     * </ul>
     * @since 7.6.0
     */
    @Keyword(keywordObject = "Element")
    public static boolean verifyElementPresent(WindowsTestObject windowsObject, int timeout)
            throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS,
                "verifyElementPresent", windowsObject, timeout);
    }

    /**
     * Waits until the given element not to present (disappear) within the
     * given time in second unit.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy of
     * the target element that needs to wait for.
     * @param timeout
     * System will wait at most timeout (seconds) to return result.
     * <ul>
     * <li>If timeout = 0, Katalon Studio will use default page load timeout.
     * <li>If timeout < 0, throws {@link IllegalArgumentException}.
     * </ul>
     * @param flowControl
     * Optional parameter: Controls the execution flow if the step failed.
     * <ul>
     * <li>STOP_ON_FAILURE: throws {@link StepFailedException} if the step failed (default).</li>
     * <li>CONTINUE_ON_FAILURE: continues the test if the test failed but the test result is still failed.</li>
     * <li>OPTIONAL: continues the test and ignore the test result.</li>
     * </ul>
     * @return true if element does not present; Otherwise, false.
     * @throws StepFailedException
     * If the Windows element exists.
     * @throws IllegalArgumentException
     * If:
     * <ul>
     * <li>windowsObject is null.</li>
     * <li>timeout < 0.</li>
     * </ul>
     * @since 7.6.0
     */
    @Keyword(keywordObject = "Element")
    public static boolean verifyElementNotPresent(WindowsTestObject windowsObject, int timeout,
            FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS,
                "verifyElementNotPresent", windowsObject, timeout, flowControl);
    }

    /**
     * Waits until the given element not to present (disappear) within the
     * given time in second unit.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy of
     * the target element that needs to wait for.
     * @param timeout
     * System will wait at most timeout (seconds) to return result.
     * <ul>
     * <li>If timeout = 0, Katalon Studio will use default page load timeout.
     * <li>If timeout < 0, throws {@link IllegalArgumentException}.
     * </ul>
     * @return true if element does not present; Otherwise, false.
     * @throws StepFailedException
     * If the Windows element exists.
     * @throws IllegalArgumentException
     * If:
     * <ul>
     * <li>windowsObject is null.</li>
     * <li>timeout < 0.</li>
     * </ul>
     * @since 7.6.0
     */
    @Keyword(keywordObject = "Element")
    public static boolean verifyElementNotPresent(WindowsTestObject windowsObject, int timeout)
            throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS,
                "verifyElementNotPresent", windowsObject, timeout);
    }

    /**
     * Waits until the given element has an attribute with the specific
     * name and value within the given time in second unit.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy of
     * the target element that needs to wait for.
     * @param attributeName
     * The name of the attribute to verify, not null.
     * @param attributeValue
     * The value of the expected attribute value to verify, not null.
     * @param timeout
     * System will wait at most timeout (seconds) to check the given element.
     * <ul>
     * <li>If timeout = 0, Katalon Studio will use default page load timeout.
     * <li>If timeout < 0, throws {@link IllegalArgumentException}.
     * </ul>
     * @param flowControl
     * Optional parameter: Controls the execution flow if the step failed.
     * <ul>
     * <li>STOP_ON_FAILURE: throws {@link StepFailedException} if the step failed (default).</li>
     * <li>CONTINUE_ON_FAILURE: continues the test if the test failed but the test result is still failed.</li>
     * <li>OPTIONAL: continues the test and ignore the test result.</li>
     * </ul>
     * @throws StepFailedException
     * If The Windows element doesn't exist.
     * @throws IllegalArgumentException
     * If:
     * <ul>
     * <li>windowsObject, attributeName or attributeValue is null.</li>
     * <li>timeout < 0.</li>
     * </ul>
     * @return true if element has the attribute with the specific name and value; Otherwise, false.
     * @since 7.6.0
     */
    @Keyword(keywordObject = "Element")
    public static boolean waitForElementAttributeValue(WindowsTestObject windowsObject, String attributeName,
            String attributeValue, int timeout, FailureHandling flowControl) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS,
                "waitForElementAttributeValue", windowsObject, attributeName, attributeValue, timeout, flowControl);
    }

    /**
     * Waits until the given element has an attribute with the specific
     * name and value within the given time in second unit.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy of
     * the target element that needs to wait for.
     * @param attributeName
     * The name of the attribute to verify, not null.
     * @param attributeValue
     * The value of the expected attribute value to verify, not null.
     * @param timeout
     * System will wait at most timeout (seconds) to check the given element.
     * <ul>
     * <li>If timeout = 0, Katalon Studio will use default page load timeout.
     * <li>If timeout < 0, throws {@link IllegalArgumentException}.
     * </ul>
     * @throws StepFailedException
     * If The Windows element doesn't exist.
     * @throws IllegalArgumentException
     * If:
     * <ul>
     * <li>windowsObject, attributeName or attributeValue is null.</li>
     * <li>timeout < 0.</li>
     * </ul>
     * @return true if element has the attribute with the specific name and value; Otherwise, false.
     * @since 7.6.0
     */
    @Keyword(keywordObject = "Element")
    public static boolean waitForElementAttributeValue(WindowsTestObject windowsObject, String attributeName,
            String attributeValue, int timeout) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS,
                "waitForElementAttributeValue", windowsObject, attributeName, attributeValue, timeout);
    }

    /**
     * Waits for the given element not to present (disappear) within the given
     * time in second unit.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy of
     * the target element that needs to wait for.
     * @param timeout
     * System will wait at most timeout (seconds) to check the given element.
     * <ul>
     * <li>If timeout = 0, Katalon Studio will use default page load timeout.
     * <li>If timeout < 0, throws {@link IllegalArgumentException}.
     * </ul>
     * @param flowControl
     * Optional parameter: Controls the execution flow if the step failed.
     * <ul>
     * <li>STOP_ON_FAILURE: throws {@link StepFailedException} if the step failed (default).</li>
     * <li>CONTINUE_ON_FAILURE: continues the test if the test failed but the test result is still failed.</li>
     * <li>OPTIONAL: continues the test and ignore the test result.</li>
     * </ul>
     * @return true if element does not present; Otherwise, false.
     * @throws StepFailedException
     * If the Windows element exists.
     * @throws IllegalArgumentException
     * If:
     * <ul>
     * <li>windowsObject is null.</li>
     * <li>timeout < 0.</li>
     * </ul>
     * @since 7.6.0
     */
    @Keyword(keywordObject = "Element")
    public static boolean waitForElementNotPresent(WindowsTestObject windowsObject, int timeout,
            FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS,
                "waitForElementNotPresent", windowsObject, timeout, flowControl);
    }

    /**
     * Waits for the given element not to present (disappear) within the given
     * time in second unit.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy of
     * the target element that needs to wait for.
     * @param timeout
     * System will wait at most timeout (seconds) to check the given element.
     * <ul>
     * <li>If timeout = 0, Katalon Studio will use default page load timeout.
     * <li>If timeout < 0, throws {@link IllegalArgumentException}.
     * </ul>
     * @return true if element does not present; Otherwise, false.
     * @throws StepFailedException
     * If the Windows element exists.
     * @throws IllegalArgumentException
     * If:
     * <ul>
     * <li>windowsObject is null.</li>
     * <li>timeout < 0.</li>
     * </ul>
     * @since 7.6.0
     */
    @Keyword(keywordObject = "Element")
    public static boolean waitForElementNotPresent(WindowsTestObject windowsObject, int timeout)
            throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS,
                "waitForElementNotPresent", windowsObject, timeout);
    }

    /**
     * Waits for the given element to present (appear) within the given
     * time in second unit.
     *
     * @param windowsObject
     * An object that describes locator and locator strategy of
     * the target element that needs to wait for.
     * @param timeout
     * System will wait at most timeout (seconds) to check the given element.
     * <ul>
     * <li>If timeout = 0, Katalon Studio will use default page load timeout.
     * <li>If timeout < 0, throws {@link IllegalArgumentException}.
     * </ul>
     * @param flowControl
     * Optional parameter: Controls the execution flow if the step failed.
     * <ul>
     * <li>STOP_ON_FAILURE: throws {@link StepFailedException} if the step failed (default).</li>
     * <li>CONTINUE_ON_FAILURE: continues the test if the test failed but the test result is still failed.</li>
     * <li>OPTIONAL: continues the test and ignore the test result.</li>
     * </ul>
     * @return true if element presents; Otherwise, false
     * @throws StepFailedException
     * If The Windows element doesn't exist.
     * @throws IllegalArgumentException
     * If:
     * <ul>
     * <li>windowsObject is null.</li>
     * <li>timeout < 0.</li>
     * </ul>
     * @since 7.6.0
     */
    @Keyword(keywordObject = "Element")
    public static boolean waitForElementPresent(WindowsTestObject windowsObject, int timeout,
            FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS,
                "waitForElementPresent", windowsObject, timeout, flowControl);
    }

    /**
     * Waits for the given element to present (appear) within the given
     * time in second unit.
     *
     * @param windowsObject
     * An object that describes locator and locator strategy of
     * the target element that needs to wait for.
     * @param timeout
     * System will wait at most timeout (seconds) to check the given element.
     * <ul>
     * <li>If timeout = 0, Katalon Studio will use default page load timeout.
     * <li>If timeout < 0, throws {@link IllegalArgumentException}.
     * </ul>
     * @return true if element presents; Otherwise, false.
     * @throws StepFailedException
     * If The Windows element doesn't exist.
     * @throws IllegalArgumentException
     * If:
     * <ul>
     * <li>windowsObject is null.</li>
     * <li>timeout < 0.</li>
     * </ul>
     * @since 7.6.0
     */
    @Keyword(keywordObject = "Element")
    public static boolean waitForElementPresent(WindowsTestObject windowsObject, int timeout)
            throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS,
                "waitForElementPresent", windowsObject, timeout);
    }
}
