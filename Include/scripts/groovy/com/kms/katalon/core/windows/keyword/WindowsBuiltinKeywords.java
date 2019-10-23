package com.kms.katalon.core.windows.keyword;

import java.util.List;

import org.openqa.selenium.WebElement;

import com.kms.katalon.core.annotation.Keyword;
import com.kms.katalon.core.exception.StepFailedException;
import com.kms.katalon.core.keyword.BuiltinKeywords;
import com.kms.katalon.core.keyword.internal.KeywordExecutor;
import com.kms.katalon.core.model.FailureHandling;
import com.kms.katalon.core.testobject.WindowsTestObject;
import com.kms.katalon.core.windows.constants.StringConstants;
import com.kms.katalon.core.windows.driver.WindowsDriverFactory;

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
     * This action will append the given text on the element and doesn't clear the current text of the element.
     * 
     * @param windowsObject
     * An object that describes locator and locator strategy to find Windows Element.
     * @param text
     * The text content to set on the element.
     * @throws StepFailedException
     * If the windowsObject doesn't exist, or KS could not perform set text action on the element.
     * @since 7.0.0
     */
    @Keyword(keywordObject = "Element")
    public static void setText(WindowsTestObject windowsObject, String text) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "setText", windowsObject, text);
    }

    /**
     * Performs a set text action on the {@link WebElement} that is found by using locator value of the given
     * windowsObject.<br>
     * This action will append the given text on the element and doesn't clear the current text of the element.
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
     * @since 7.0.0
     */
    @Keyword(keywordObject = "Element")
    public static void setText(WindowsTestObject windowsObject, String text, FailureHandling flowControl)
            throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WINDOWS, "setText", windowsObject, text,
                flowControl);
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
}
