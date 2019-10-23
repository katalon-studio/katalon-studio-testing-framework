package com.kms.katalon.core.webui.keyword;

import org.openqa.selenium.WebDriverException
import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.BuiltinKeywords
import com.kms.katalon.core.keyword.internal.KeywordExecutor
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.constants.StringConstants
import com.kms.katalon.core.webui.exception.WebElementNotFoundException
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword

import groovy.transform.CompileStatic

@CompileStatic
public class WebUiBuiltInKeywords extends BuiltinKeywords {

    /**
     * Open browser and navigate to the specified url; if url is left empty then just open browser
     * @param rawUrl
     *         url of the web page to be opened, can be left empty or null. If rawUrl doesn't contain protocol prefix, 
     *         then the protocol will be <code>http://</code>.
     *      </p>Example:
     *      <ul>
     *          <li>http://katalon.kms-technology.com/</li>
     *          <li>https://www.google.com</li>
     *          <li>file:///D:/Development/index.html</li>
     *          <li>kms-technology.com => http://kms-technology.com</li>
     *      </ul>
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_BROWSER)
    public static void openBrowser(String rawUrl, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "openBrowser", rawUrl, flowControl)
    }

    /**
     * Open browser and navigate to the specified url; if url is left empty then just open browser
     * @param rawUrl
     *         url of the web page to be opened, can be left empty or null. If rawUrl doesn't contain protocol prefix,
     *         then the protocol will be <code>http://</code>.
     *      </p>Example:
     *      <ul>
     *          <li>http://katalon.kms-technology.com/</li>
     *          <li>https://www.google.com</li>
     *          <li>file:///D:/Development/index.html</li>
     *          <li>kms-technology.com => http://kms-technology.com</li>
     *      </ul>
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_BROWSER)
    public static void openBrowser(String rawUrl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "openBrowser", rawUrl)
    }

    /**
     * Close the browser. This action will close all windows of the browser.
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_BROWSER)
    public static void closeBrowser(FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "closeBrowser", flowControl)
    }

    /**
     * Close the browser. This action will close all windows of the browser.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_BROWSER)
    public static void closeBrowser() throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "closeBrowser")
    }

    /**
     * Simulate users clicking "back" button on their browser
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_BROWSER)
    public static void back(FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "back", flowControl)
    }

    /**
     * Simulate users clicking "back" button on their browser
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_BROWSER)
    public static void back() throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "back")
    }

    /**
     * Simulate users clicking "forward" button on their browser
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_BROWSER)
    public static void forward(FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "forward", flowControl)
    }

    /**
     * Simulate users clicking "forward" button on their browser
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_BROWSER)
    public static void forward() throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "forward")
    }

    /**
     * Simulate users clicking "refresh" button on their browser
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_BROWSER)
    public static void refresh(FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "refresh", flowControl)
    }

    /**
     * Simulate users clicking "refresh" button on their browser
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_BROWSER)
    public static void refresh() throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "refresh")
    }

    /**
     * Navigate to the specified web page
     * @param rawUrl
     *          url of the web page to navigate to. If rawUrl doesn't contain protocol prefix, then the protocol will be <code>http://</code>.
     *      </p>Example:
     *      <ul>
     *          <li>http://katalon.kms-technology.com/</li>
     *          <li>https://www.google.com</li>
     *          <li>file:///D:/Development/index.html</li>
     *          <li>kms-technology.com => http://kms-technology.com</li>
     *      </ul>
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_BROWSER)
    public static void navigateToUrl(String rawUrl, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "navigateToUrl", rawUrl, flowControl)
    }

    /**
     * Navigate to the specified web page
     * @param rawUrl
     *          url of the web page to navigate to. If rawUrl doesn't contain protocol prefix, then the protocol will be <code>http://</code>.
     *      </p>Example:
     *      <ul>
     *          <li>http://katalon.kms-technology.com/</li>
     *          <li>https://www.google.com</li>
     *          <li>file:///D:/Development/index.html</li>
     *          <li>kms-technology.com => http://kms-technology.com</li>
     *      </ul>
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_BROWSER)
    public static void navigateToUrl(String rawUrl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "navigateToUrl", rawUrl)
    }

    /**
     * Get title of the current window
     * @param flowControl
     * @return
     *      title of the current window
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static String getWindowTitle(FailureHandling flowControl) throws StepFailedException {
        return (String) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getWindowTitle", flowControl)
    }

    /**
     * Get title of the current window
     * @return
     *      title of the current window
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static String getWindowTitle() throws StepFailedException {
        return (String)KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getWindowTitle")
    }

    /**
     * Get url of the current window
     * @param flowControl
     * @return
     *      url of the current window
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static String getUrl(FailureHandling flowControl) throws StepFailedException {
        return (String) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getUrl", flowControl)
    }

    /**
     * Get url of the current window
     * @return
     *      url of the current window
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static String getUrl() throws StepFailedException {
        return (String) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getUrl")
    }

    /**
     * Get index of the current window
     * @param flowControl
     * @return
     *      index of the current window
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static int getWindowIndex(FailureHandling flowControl) throws StepFailedException {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getWindowIndex", flowControl)
    }

    /**
     * Get index of the current window
     * @return
     *      index of the current window
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static int getWindowIndex() throws StepFailedException {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getWindowIndex")
    }

    /**
     * Resize current window to take up the entire screen
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static void maximizeWindow(FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "maximizeWindow", flowControl)
    }

    /**
     * Resize current window to take up the entire screen
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static void maximizeWindow() throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "maximizeWindow")
    }

    /**
     * Wait for the given element to NOT present (disappear) within the given time in second unit
     * @param to 
     *          represent a web element
     * @param timeOut 
     *          system will wait at most timeout (seconds) to return result
     * @return
     *      true if the element is NOT present, and false if the element is present
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean waitForElementNotPresent(TestObject to, int timeOut, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "waitForElementNotPresent", to, timeOut, flowControl)
    }

    /**
     * Wait for the given element to NOT present (disappear) within the given time in second unit
     * @param to
     *          represent a web element
     * @param timeOut
     *          system will wait at most timeout (seconds) to return result
     * @return
     *      true if the element is NOT present, and false if the element is present
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean waitForElementNotPresent(TestObject to, int timeOut) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "waitForElementNotPresent", to, timeOut)
    }

    /**
     * Wait for the given element to present (appear) within the given time in second unit
     * @param to
     *      represent a web element
     * @param timeOut
     *       system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return
     *      true if the element is present, and false if the element is NOT present
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean waitForElementPresent(TestObject to, int timeOut, FailureHandling flowControl)
    throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "waitForElementPresent", to, timeOut, flowControl)
    }

    /**
     * Wait for the given element to present (appear) within the given time in second unit
     * @param to
     *      represent a web element
     * @param timeOut
     *       system will wait at most timeout (seconds) to return result
     * @return
     *      true if the element is present, and false if the element is NOT present
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean waitForElementPresent(TestObject to, int timeOut){
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "waitForElementPresent", to, timeOut)
    }

    /***
     * Verify if given web element is visible
     * @param to
     *      represent a web element
     * @param flowControl
     * @return
     *     true if the element is present and visible; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementVisible(TestObject to, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyElementVisible", to, flowControl)
    }

    /***
     * Verify if given web element is visible
     * @param to
     *      represent a web element
     * @return
     *     true if the element is present and visible; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementVisible(TestObject to) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyElementVisible", to)
    }

    /***
     * Verify if given web element is NOT visible
     * @param to
     *      represent a web element
     * @param flowControl
     * @return
     *     true if the element is present and NOT visible; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementNotVisible(TestObject to, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyElementNotVisible", to, flowControl)
    }

    /***
     * Verify if given web element is NOT visible
     * @param to
     *      represent a web element
     * @return
     *     true if the element is present and NOT visible; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementNotVisible(TestObject to) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyElementNotVisible", to)
    }

    /***
     * Wait until the given web element is visible within timeout.
     * @param to 
     *      represent a web element
     * @param timeOut
     *      how many seconds to wait (maximum)
     * @param flowControl
     * @return
     *     true if the element is present and visible; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean waitForElementVisible(TestObject to, int timeOut, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "waitForElementVisible", to, timeOut, flowControl)
    }

    /***
     * Wait until the given web element is visible within timeout.
     * @param to
     *      represent a web element
     * @param timeOut
     *      how many seconds to wait (maximum)
     * @return
     *     true if the element is present and visible; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean waitForElementVisible(TestObject to, int timeOut) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "waitForElementVisible", to, timeOut)
    }

    /***
     * Wait until the given web element is NOT visible within timeout.
     * @param to
     *      represent a web element
     * @param timeOut
     *      how many seconds to wait (maximum)
     * @param flowControl
     * @return
     *     true if the element is present but is NOT visible; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean waitForElementNotVisible(TestObject to, int timeOut, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "waitForElementNotVisible", to, timeOut, flowControl)
    }

    /***
     * Wait until the given web element is NOT visible within timeout.
     * @param to
     *      represent a web element
     * @param timeOut
     *      how many seconds to wait (maximum)
     * @return
     *     true if the element is present but is NOT visible; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean waitForElementNotVisible(TestObject to, int timeOut) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "waitForElementNotVisible", to, timeOut)
    }

    /***
     * Wait for the given element to be clickable within the given time in second
     * @param to
     *         represent a web element
     * @param timeOut
     *         how many seconds to wait
     * @param flowControl
     * @return
     *         true if the element is present and clickable; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean waitForElementClickable(TestObject to, int timeOut, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "waitForElementClickable", to, timeOut, flowControl)
    }

    /***
     * Wait for the given element to be clickable within the given time in second
     * @param to
     *         represent a web element
     * @param timeOut
     *         how many seconds to wait
     * @return
     *         true if the element is present and clickable; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean waitForElementClickable(TestObject to, int timeOut) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "waitForElementClickable", to, timeOut)
    }

    /***
     * Wait for the given element to be not clickable within the given time in second
     * @param to
     *         represent a web element
     * @param timeOut
     *         how many seconds to wait
     * @param flowControl
     * @return
     *         true if the element is present but is NOT clickable; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean waitForElementNotClickable(TestObject to, int timeOut, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "waitForElementNotClickable", to, timeOut, flowControl)
    }

    /***
     * Wait for the given element to be not clickable within the given time in second
     * @param to
     *         represent a web element
     * @param timeOut
     *         how many seconds to wait
     * @return
     *         true if the element is present but is NOT clickable; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean waitForElementNotClickable(TestObject to, int timeOut) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "waitForElementNotClickable", to, timeOut)
    }

    /***
     * Verify if the given element is clickable
     * @param to
     *         represent a web element
     * @param flowControl
     * @return
     *         true if the element is present and clickable; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementClickable(TestObject to, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyElementClickable", to, flowControl)
    }

    /***
     * Verify if the given element is clickable
     * @param to
     *         represent a web element
     * @return
     *         true if the element is present and clickable; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementClickable(TestObject to) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyElementClickable", to)
    }

    /***
     * Verify if the given element is NOT clickable
     * @param to
     *         represent a web element
     * @param flowControl
     * @return
     *         true if the element is present and NOT clickable; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementNotClickable(TestObject to, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyElementNotClickable", to, flowControl)
    }

    /***
     * Verify if the given element is NOT clickable
     * @param to
     *         represent a web element
     * @return
     *         true if the element is present and NOT clickable; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementNotClickable(TestObject to) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyElementNotClickable", to)
    }

    /**
     * Click on the given element
     * @param to
     *       represent a web element
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void click(TestObject to, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "click", to, flowControl)
    }

    /**
     * Click on the given element
     * @param to
     *       represent a web element
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void click(TestObject to) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "click", to)
    }

    /**
     * Click on the given element with the relative position (x, y) from the top-left corner of that element
     * @param to
     *       represent a web element
     * @param offsetX x position in relative to the element
     * @param offsetY y position in relative to the element
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void clickOffset(TestObject to, int offsetX, int offsetY, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "clickOffset", to, offsetX, offsetY, flowControl)
    }

    /**
     * Click on the given element with the relative position (x, y) from the top-left corner of that element
     * @param to
     *       represent a web element
     * @param offsetX x position in relative to the element
     * @param offsetY y position in relative to the element
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void clickOffset(TestObject to, int offsetX, int offsetY) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "clickOffset", to, offsetX, offsetY)
    }

    /**
     * If this current element is a form, or an element within a form, then this will be submitted. 
     * If this causes the current page to change, then this method will block until the new page is loaded.
     * @param to
     *      represent a web element
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_FORM)
    public static void submit(TestObject to, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "submit", to, flowControl)
    }

    /**
     * If this current element is a form, or an element within a form, then this will be submitted.
     * If this causes the current page to change, then this method will block until the new page is loaded.
     * @param to
     *      represent a web element
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_FORM)
    public static void submit(TestObject to) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "submit", to)
    }

    /**
     * Double click on the given web element
     * @param to
     *      represent a web element
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void doubleClick(TestObject to, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "doubleClick", to, flowControl)
    }

    /**
     * Double click on the given web element
     * @param to
     *      represent a web element
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void doubleClick(TestObject to) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "doubleClick", to)
    }

    /**
     * Right click on the given web element
     * @param to
     *      represent a web element
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void rightClick(TestObject to, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "rightClick", to, flowControl)
    }

    /**
     * Right click on the given web element
     * @param to
     *      represent a web element
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void rightClick(TestObject to) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "rightClick", to)
    }

    /**
     * Right click on the given element with the relative position (x, y) from the top-left corner of that element
     * @param to
     *       represent a web element
     * @param offsetX x position in relative to the element
     * @param offsetY y position in relative to the element
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void rightClickOffset(TestObject to, int offsetX, int offsetY, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "rightClickOffset", to, offsetX, offsetY, flowControl)
    }

    /**
     * Right click on the given element with the relative position (x, y) from the top-left corner of that element
     * @param to
     *       represent a web element
     * @param offsetX x position in relative to the element
     * @param offsetY y position in relative to the element
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void rightClickOffset(TestObject to, int offsetX, int offsetY) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "rightClickOffset", to, offsetX, offsetY)
    }

    /**
     * Simulate users hovering a mouse over the given element
     * @param to
     *       represent a web element
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void mouseOver(TestObject to, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "mouseOver", to, flowControl)
    }

    /**
     * Simulate users hovering a mouse over the given element
     * @param to
     *       represent a web element
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void mouseOver(TestObject to) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "mouseOver", to)
    }

    /**
     * Simulate users hovering a mouse over the given element with the relative position (x, y) from the top-left corner of that element
     * @param to
     *       represent a web element
     * @param offsetX x position in relative to the element
     * @param offsetY y position in relative to the element
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void mouseOverOffset(TestObject to, int offsetX, int offsetY, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "mouseOverOffset", to, offsetX, offsetY, flowControl);
    }

    /**
     * Simulate users hovering a mouse over the given element with the relative position (x, y) from the top-left corner of that element
     * @param to
     *       represent a web element
     * @param offsetX x position in relative to the element
     * @param offsetY y position in relative to the element
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void mouseOverOffset(TestObject to, int offsetX, int offsetY) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "mouseOverOffset", to, offsetX, offsetY)
    }

    /**
     * Simulates keystroke events on the specified element, as though you typed the value key-by-key
     * @param to
     *      represent a web element
     * @param strKeys
     *      the combination of keys to type
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_KEYBOARD)
    public static void sendKeys(TestObject to, String strKeys, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "sendKeys", to, strKeys, flowControl)
    }

    /**
     * Simulates keystroke events on the specified element, as though you typed the value key-by-key
     * @param to
     *      represent a web element
     * @param strKeys
     *      the combination of keys to type
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_KEYBOARD)
    public static void sendKeys(TestObject to, String strKeys) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "sendKeys", to, strKeys)
    }

    /**
     * Move the focus to the specified element; for example, if the element is an input field, move the cursor to that field
     * @param to
     *      represent a web element
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void focus(TestObject to, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "focus", to, flowControl)
    }

    /**
     * Move the focus to the specified element; for example, if the element is an input field, move the cursor to that field
     * @param to
     *      represent a web element
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void focus(TestObject to) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "focus", to)
    }

    /**
     * Get the visible (i.e. not hidden by CSS) innerText of the web element, including sub-elements, without any leading or trailing whitespace.
     * @param to
     *      represent a web element
     * @param flowControl
     * @return
     *       innerText of the web element
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_TEXT)
    public static String getText(TestObject to, FailureHandling flowControl) throws StepFailedException {
        return (String) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getText", to, flowControl)
    }

    /**
     * Get the visible (i.e. not hidden by CSS) innerText of the web element, including sub-elements, without any leading or trailing whitespace.
     * @param to
     *      represent a web element
     * @return
     *       innerText of the web element
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_TEXT)
    public static String getText(TestObject to) throws StepFailedException {
        return (String) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getText", to)
    }

    /**
     * Get attribute value of a web element
     * @param to
     *       represent a web element
     * @param attribute
     *      name of the attribute
     * @param flowControl
     * @return
     *      value of the attribute
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ATTRIBUTE)
    public static String getAttribute(TestObject to, String attribute, FailureHandling flowControl) throws StepFailedException {
        return (String) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getAttribute", to, attribute, flowControl)
    }

    /**
     * Get attribute value of a web element
     * @param to
     *       represent a web element
     * @param attribute
     *      name of the attribute
     * @return
     *      value of the attribute
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ATTRIBUTE)
    public static String getAttribute(TestObject to, String attribute) throws StepFailedException {
        return (String) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getAttribute", to, attribute)
    }

    /**
     * Set the value of an input field, as though you type it in. It also clears the previous value of the input field
     * @param to
     *      represent a web element
     * @param text
     *      the text to type
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_TEXT)
    public static void setText(TestObject to, String text, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "setText", to, text, flowControl)
    }

    /**
     * Set the value of an input field, as though you type it in. It also clears the previous value of the input field
     * @param to
     *      represent a web element
     * @param text
     *      the text to type
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_TEXT)
    public static void setText(TestObject to, String text) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "setText", to,text)
    }
    
    /**
     * Set encrypted text into an input field. It also clears the previous value of the input field.
     * To encrypt raw text, go to Help/Encrypt Text
     * @param to
     *       represent a web element
     * @param encryptedText
     *       the encrypted text
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_TEXT)
    public static void setEncryptedText(TestObject to, String encryptedText, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "setEncryptedText", to, encryptedText, flowControl)
    }
    
    /**
     * Set encrypted text into an input field. It also clears the previous value of the input field.
     * To encrypt raw text, go to Help/Encrypt Text
     * @param to
     *       represent a web element
     * @param encryptedText
     *       the encrypted text
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_TEXT)
    public static void setEncryptedText(TestObject to, String encryptedText) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "setEncryptedText", to, encryptedText)
    }

    /**
     * Set the value of an input field, as though you type it in. It also clears the previous value of the input field.
     * The text value will be masked.
     *
     * @param to
     *      represent a web element
     * @param text
     *      the text to type
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_TEXT)
    public static void setMaskedText(TestObject to, String text) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "setMaskedText", to, text)
    }

    /**
     * Set the value of an input field, as though you type it in. It also clears the previous value of the input field.
     * The text value will be masked.
     *
     * @param to
     *      represent a web element
     * @param text
     *      the text to type
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_TEXT)
    public static void setMaskedText(TestObject to, String text, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "setMaskedText", to, text, flowControl)
    }

    /**
     * Check a toggle-button (check-box/radio-button)
     * @param to
     *      represent a web element
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_CHECKBOX)
    public static void check(TestObject to, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "check", to, flowControl)
    }

    /**
     * Check a toggle-button (check-box/radio-button)
     * @param to
     *      represent a web element
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_CHECKBOX)
    public static void check(TestObject to) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "check", to)
    }

    /**
     * Uncheck a toggle-button (check-box/radio-button)
     * @param to
     *      represent a web element
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_CHECKBOX)
    public static void uncheck(TestObject to, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "uncheck", to, flowControl)
    }

    /**
     * Uncheck a toggle-button (check-box/radio-button)
     * @param to
     *      represent a web element
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_CHECKBOX)
    public static void uncheck(TestObject to) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "uncheck", to)
    }

    /**
     * Select the options at the given indices. Index starts from 0.
     *
     * @param to
     *          represent a web element
     * @param range
     *          index range of the options to select. Index starts from 0.
     <p>Example: 
     <p>2 - index 2
     <p>"2,3" - index 2 and 3
     <p>"2-5" - index 2 to 5 (2, 3, 4, 5)
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static void selectOptionByIndex(TestObject to, Object range, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "selectOptionByIndex", to, range, flowControl)
    }

    /**
     * Select the options at the given indices. Index starts from 0.
     *
     * @param to
     *          represent a web element
     * @param range
     *          index range of the options to select. Index starts from 0.
     <p>Example:
     <p>2 - index 2
     <p>"2,3" - index 2 and 3
     <p>"2-5" - index 2 to 5 (2, 3, 4, 5)
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static void selectOptionByIndex(TestObject to, Object range) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "selectOptionByIndex", to, range)
    }

    /**
     * Select all options that have a value matching the "value" argument.
     *
     * @param to
     *           represent a web element
     * @param value
     *           value of the options to select
     * @param isRegex
     *            true if value is regular expression, false if not
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static void selectOptionByValue(TestObject to, String value, boolean isRegex, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "selectOptionByValue", to, value, isRegex, flowControl)
    }

    /**
     * Select all options that have a value matching the "value" argument.
     *
     * @param to
     *           represent a web element
     * @param value
     *           value of the options to select
     * @param isRegex
     *            true if value is regular expression, false if not
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static void selectOptionByValue(TestObject to, String value, boolean isRegex) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "selectOptionByValue", to, value, isRegex)
    }

    /**
     * Selection all options of an object.
     *
     * @param to
     *         represent a web element
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static void selectAllOption(TestObject to, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "selectAllOption", to, flowControl)
    }

    /**
     * Selection all options of an object.
     *
     * @param to
     *         represent a web element
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static void selectAllOption(TestObject to) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "selectAllOption", to)
    }

    /**
     * Select all options with the given label (displayed text)
     *
     * @param to
     *         represent a web element
     * @param labelText
     *          displayed text of the options to select
     * @param isRegex
     *         true if label is regular expression, false if not
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static void selectOptionByLabel(TestObject to, String labelText, boolean isRegex, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "selectOptionByLabel", to, labelText, isRegex, flowControl)
    }

    /**
     * Select all options with the given label (displayed text)
     *
     * @param to
     *         represent a web element
     * @param labelText
     *          displayed text of the options to select
     * @param isRegex
     *         true if label is regular expression, false if not
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static void selectOptionByLabel(TestObject to, String labelText, boolean isRegex) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "selectOptionByLabel", to, labelText, isRegex)
    }

    /**
     * Deselect the options at the given indices. Index starts from 0.
     *
     * @param to
     *         represent a web element
     * @param index
     *  index range of the options to be deselected
     *  <p>Example: 
     *  <p>2 - index 2
     *  <p>"2,3" - index 2 and 3
     *  <p>"2-5" - index 2 to 5 (2, 3, 4, 5)
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static void deselectOptionByIndex(TestObject to, Object range, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "deselectOptionByIndex", to, range, flowControl)
    }

    /**
     * Deselect the options at the given indices. Index starts from 0.
     *
     * @param to
     *         represent a web element
     * @param index
     *  index range of the options to be deselected
     *  <p>Example:
     *  <p>2 - index 2
     *  <p>"2,3" - index 2 and 3
     *  <p>"2-5" - index 2 to 5 (2, 3, 4, 5)
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static void deselectOptionByIndex(TestObject to, Object range) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "deselectOptionByIndex", to, range)
    }

    /**
     * Deselect all options with the given value
     * @param to
     *         represent a web element
     * @param value
     *         value of the options to be deselected
     * @param isRegex
     *         true if value is regular expression, false if not
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static void deselectOptionByValue(TestObject to, String value, boolean isRegex, FailureHandling flowControl)
    throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "deselectOptionByValue", to, value, isRegex, flowControl)
    }

    /**
     * Deselect all options with the given value
     * @param to
     *         represent a web element
     * @param value
     *         value of the options to be deselected
     * @param isRegex
     *         true if value is regular expression, false if not
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static void deselectOptionByValue(TestObject to, String value, boolean isRegex) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "deselectOptionByValue", to, value, isRegex)
    }

    /**
     * Deselect the options with the given label (displayed text)
     *
     * @param to
     *         represent a web element
     * @param labelText
     *         displayed text of the options to be deselected
     * @param isRegex
     *         true if label is regular expression, false if not
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static void deselectOptionByLabel(TestObject to, String labelText, boolean isRegex,
            FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "deselectOptionByLabel", to, labelText, isRegex, flowControl)
    }

    /**
     * Deselect the options with the given label (displayed text)
     *
     * @param to
     *         represent a web element
     * @param labelText
     *         displayed text of the options to be deselected
     * @param isRegex
     *         true if label is regular expression, false if not
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static void deselectOptionByLabel(TestObject to, String labelText, boolean isRegex) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "deselectOptionByLabel", to, labelText, isRegex)
    }

    /**
     * Deselect all options
     *
     * @param to
     *         represent a web element
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static void deselectAllOption(TestObject to, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "deselectAllOption", to, flowControl)
    }

    /**
     * Deselect all options
     *
     * @param to
     *         represent a web element
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static void deselectAllOption(TestObject to) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "deselectAllOption", to)
    }

    /**
     * Verify if the given web element is checked.
     *
     * @param to
     *         represent a web element
     * @param timeOut
     *         system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return true if element is checked; otherwise, false
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_CHECKBOX)
    public static boolean verifyElementChecked(TestObject to, int timeOut, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyElementChecked", to, timeOut, flowControl)
    }

    /**
     * Verify if the given web element is checked.
     *
     * @param to
     *         represent a web element
     * @param timeOut
     *         system will wait at most timeout (seconds) to return result
     * @return true if element is checked; otherwise, false
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_CHECKBOX)
    public static boolean verifyElementChecked(TestObject to, int timeOut) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyElementChecked", to, timeOut)
    }

    /**
     * Verify if the given web element is NOT checked.
     *
     * @param to
     *         represent a web element
     * @param timeOut
     *         system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return true if element is not checked; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_CHECKBOX)
    public static boolean verifyElementNotChecked(TestObject to, int timeOut, FailureHandling flowControl)
    throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyElementNotChecked", to, timeOut, flowControl)
    }

    /**
     * Verify if the given web element is NOT checked.
     *
     * @param to
     *         represent a web element
     * @param timeOut
     *         system will wait at most timeout (seconds) to return result
     * @return true if element is not checked; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_CHECKBOX)
    public static boolean verifyElementNotChecked(TestObject to, int timeOut) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyElementNotChecked", to, timeOut)
    }

    /**
     * Verify if the given web element presents on the DOM
     *
     * @param to
     *          represent a web element
     * @param timeOut
     *          system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return true if element presents; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementPresent(TestObject to, int timeOut, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyElementPresent", to, timeOut, flowControl)
    }

    /**
     * Verify if the given web element presents on the DOM
     *
     * @param to
     *          represent a web element
     * @param timeOut
     *          system will wait at most timeout (seconds) to return result
     * @return true if element presents; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementPresent(TestObject to, int timeOut) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyElementPresent", to, timeOut)
    }

    /**
     * Verify if the given web element does NOT present on the DOM
     *
     * @param to
     *         represent a web element
     * @param timeOut
     *         system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return true if the element does NOT present; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementNotPresent(TestObject to, int timeOut, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyElementNotPresent", to, timeOut, flowControl)
    }

    /**
     * Verify if the given web element does NOT present on the DOM
     *
     * @param to
     *         represent a web element
     * @param timeOut
     *         system will wait at most timeout (seconds) to return result
     * @return true if the element does NOT present; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementNotPresent(TestObject to, int timeOut) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyElementNotPresent", to, timeOut)
    }

    /**
     * Simulate users clicking on "OK" button of alert class (alert,
     * confirmation popup, prompt popup)
     * 
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ALERT)
    public static void acceptAlert(FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "acceptAlert", flowControl)
    }

    /**
     * Simulate users clicking on "OK" button of alert class (alert,
     * confirmation popup, prompt popup)
     *
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ALERT)
    public static void acceptAlert() throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "acceptAlert")
    }

    /**
     * Simulate users clicking on "Cancel" button of alert class (alert,
     * confirmation popup, prompt popup).
     *
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ALERT)
    public static void dismissAlert(FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "dismissAlert", flowControl)
    }

    /**
     * Simulate users clicking on "Cancel" button of alert class (alert,
     * confirmation popup, prompt popup).
     *
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ALERT)
    public static void dismissAlert() throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "dismissAlert")
    }

    /**
     * Get displayed text of the alert class (alert, confirmation popup, prompt
     * popup).
     *
     * @param flowControl
     * @return value
     *      text of the alert
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ALERT)
    public static String getAlertText(FailureHandling flowControl) throws StepFailedException {
        return (String)KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getAlertText", flowControl)
    }

    /**
     * Get displayed text of the alert class (alert, confirmation popup, prompt
     * popup).
     *
     * @return value
     *      text of the alert
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ALERT)
    public static String getAlertText() throws StepFailedException {
        return (String)KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getAlertText")
    }

    /**
     * Simulate users typing text into prompt popup.
     *
     * @param text
     *            text to type into the prompt popup
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ALERT)
    public static void setAlertText(String text, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "setAlertText", text, flowControl)
    }

    /**
     * Simulate users typing text into prompt popup.
     *
     * @param text
     *            text to type into the prompt popup
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ALERT)
    public static void setAlertText(String text) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "setAlertText", text)
    }

    /**
     * Wait for alert to present
     *
     * @param timeout
     *            timeout waiting for alert to present
     * @param flowControl
     * @return true if alert is present and false if alert is not present
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ALERT)
    public static boolean waitForAlert(int timeOut, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "waitForAlert", timeOut, flowControl)
    }

    /**
     * Wait for alert to present
     *
     * @param timeout
     *            timeout waiting for alert to present
     * @return true if alert is present and false if alert is not present
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ALERT)
    public static boolean waitForAlert(int timeOut) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "waitForAlert", timeOut)
    }

    /**
     * Verify if alert presents
     *
     * @param timeout
     *            timeout waiting for alert to present
     * @param flowControl
     * @return true if alert is present and false if alert is not present
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ALERT)
    public static boolean verifyAlertPresent(int timeOut, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyAlertPresent", timeOut, flowControl)
    }

    /**
     * Verify if alert presents
     *
     * @param timeout
     *            timeout waiting for alert to present
     * @return true if alert is present and false if alert is not present
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ALERT)
    public static boolean verifyAlertPresent(int timeOut) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyAlertPresent", timeOut)
    }

    /**
     * Verify if alert does not present
     *
     * @param timeout
     *            timeout waiting for alert to not present
     * @param flowControl
     * @return true if alert is not present and false if alert is present
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ALERT)
    public static boolean verifyAlertNotPresent(int timeOut, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyAlertNotPresent", timeOut, flowControl)
    }

    /**
     * Verify if alert does not present
     *
     * @param timeout
     *            timeout waiting for alert to not present
     * @return true if alert is not present and false if alert is present
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ALERT)
    public static boolean verifyAlertNotPresent(int timeOut) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyAlertNotPresent", timeOut)
    }

    /**
     * Verify if the given texts present anywhere in the page source
     *
     * @param text
     *            to be verified if existing anywhere in page source
     * @param isRegex
     *             true if text is regular expression; otherwise, false
     * @param flowControl
     * @return true if text presents anywhere in the page source; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_TEXT)
    public static boolean verifyTextPresent(String text, boolean isRegex, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyTextPresent", text, isRegex, flowControl)
    }

    /**
     * Verify if the given texts present anywhere in the page source
     *
     * @param text
     *            to be verified if existing anywhere in page source
     * @param isRegex
     *             true if text is regular expression; otherwise, false
     * @return true if text presents anywhere in the page source; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_TEXT)
    public static boolean verifyTextPresent(String text, boolean isRegex) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyTextPresent", text, isRegex)
    }

    /**
     * Verify if the given texts do NOT present anywhere in the page source
     *
     * @param text
     *         text to be verified if NOT presenting anywhere in the page source
     * @param isRegex
     *         true if text is regular expression; otherwise, false
     * @param flowControl
     * @return true if text does NOT present anywhere in the page source; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_TEXT)
    public static boolean verifyTextNotPresent(String text, boolean isRegex, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyTextNotPresent", text, isRegex, flowControl)
    }

    /**
     * Verify if the given texts do NOT present anywhere in the page source
     *
     * @param text
     *         text to be verified if NOT presenting anywhere in the page source
     * @param isRegex
     *         true if text is regular expression; otherwise, false
     * @return true if text does NOT present anywhere in the page source; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_TEXT)
    public static boolean verifyTextNotPresent(String text, boolean isRegex) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyTextNotPresent", text, isRegex)
    }

    /**
     * Switch to the window with given title.
     *
     * @param title
     *            title of the window to switch to
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static void switchToWindowTitle(String title, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "switchToWindowTitle", title, flowControl)
    }

    /**
     * Switch to the window with given title.
     *
     * @param title
     *            title of the window to switch to
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static void switchToWindowTitle(String title) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "switchToWindowTitle", title)
    }

    /**
     * Close the window with given title.
     *
     * @param title
     *            title of the window to close
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static void closeWindowTitle(String title, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "closeWindowTitle", title, flowControl)
    }

    /**
     * Close the window with given title.
     *
     * @param title
     *            title of the window to close
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static void closeWindowTitle(String title) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "closeWindowTitle", title)
    }

    /**
     * Switch to the window with given url.
     *
     * @param url
     *            url of the window to switch to
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static void switchToWindowUrl(String url, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "switchToWindowUrl", url, flowControl)
    }

    /**
     * Switch to the window with given url.
     *
     * @param url
     *            url of the window to switch to
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static void switchToWindowUrl(String url) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "switchToWindowUrl", url)
    }

    /**
     * Close the window with given url.
     *
     * @param url
     *            url of the window to close
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static void closeWindowUrl(String url, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "closeWindowUrl", url, flowControl)
    }

    /**
     * Close the window with given url.
     *
     * @param url
     *            url of the window to close
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static void closeWindowUrl(String url) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "closeWindowUrl", url)
    }

    /**
     * Switch to the window with given index.
     *
     * @param index
     *            the index of the window to switch to, index is 0-based number
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static void switchToWindowIndex(Object index, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "switchToWindowIndex", index, flowControl)
    }

    /**
     * Switch to the window with given index.
     *
     * @param index
     *            the index of the window to switch to, index is 0-based number
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static void switchToWindowIndex(Object index) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "switchToWindowIndex", index)
    }

    /**
     * Close window with the given index.
     *
     * @param index
     *            the index of the window to close, index is 0-based number
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static void closeWindowIndex(Object index, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "closeWindowIndex", index, flowControl)
    }

    /**
     * Close window with the given index.
     *
     * @param index
     *            the index of the window to close, index is 0-based number
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static void closeWindowIndex(Object index) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "closeWindowIndex", index)
    }

    /**
     * Count the total number of options the given web element has
     *
     * @param to
     *         represent a web element
     * @param flowControl
     * @return the total number of options
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static int getNumberOfTotalOption(TestObject to, FailureHandling flowControl) throws StepFailedException {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getNumberOfTotalOption", to, flowControl)
    }

    /**
     * Count the total number of options the given web element has
     *
     * @param to
     *         represent a web element
     * @return the total number of options
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static int getNumberOfTotalOption(TestObject to) throws StepFailedException {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getNumberOfTotalOption", to)
    }

    /**
     * Count the number of options which are being selected the given web element has.
     *
     * @param to
     *         represent a web element
     * @param flowControl
     * @return number the number of selected options
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static int getNumberOfSelectedOption(TestObject to, FailureHandling flowControl) throws StepFailedException {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getNumberOfSelectedOption", to, flowControl)
    }

    /**
     * Count the number of options which are being selected the given web element has.
     *
     * @param to
     *         represent a web element
     * @return number the number of selected options
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static int getNumberOfSelectedOption(TestObject to) throws StepFailedException {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getNumberOfSelectedOption", to)
    }

    /**
     * Verify if the option with the given label (displayed text) presents
     *
     * @param to
     *         represent a web element
     * @param label
     *         displayed texts of the options to be verified if existing
     * @param isRegex
     *         true if label is regular expression, false if not
     * @param timeOut
     *         system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return true if all options with given displayed texts exist; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static boolean verifyOptionPresentByLabel(TestObject to, String label, boolean isRegex, int timeOut,
            FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyOptionPresentByLabel", to, label, isRegex, timeOut, flowControl)
    }

    /**
     * Verify if the option with the given label (displayed text) presents
     *
     * @param to
     *         represent a web element
     * @param label
     *         displayed texts of the options to be verified if existing
     * @param isRegex
     *         true if label is regular expression, false if not
     * @param timeOut
     *         system will wait at most timeout (seconds) to return result
     * @return true if all options with given displayed texts exist; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static boolean verifyOptionPresentByLabel(TestObject to, String label, boolean isRegex, int timeOut) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyOptionPresentByLabel", to, label, isRegex, timeOut)
    }

    /**
     * Verify if the options with the given value present.
     *
     * @param to
     *         represent a web element.
     * @param value
     *         value of the options to be verified if presenting.
     * @param isRegex
     *         true if value is regular expression, false by default.
     * @param timeOut
     *         system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return true if all options with given value present; otherwise, false.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static boolean verifyOptionPresentByValue(TestObject to, String value, boolean isRegex, int timeOut, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyOptionPresentByValue", to, value, isRegex, timeOut, flowControl)
    }

    /**
     * Verify if the options with the given value present.
     *
     * @param to
     *         represent a web element.
     * @param value
     *         value of the options to be verified if presenting.
     * @param isRegex
     *         true if value is regular expression, false by default.
     * @param timeOut
     *         system will wait at most timeout (seconds) to return result
     * @return true if all options with given value present; otherwise, false.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static boolean verifyOptionPresentByValue(TestObject to, String value, boolean isRegex, int timeOut) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyOptionPresentByValue", to, value, isRegex, timeOut)
    }

    /**
     * Verify if the options with the given displayed texts do not exist.
     *
     * @param to
     *         represent a web element
     * @param label
     *          displayed texts of the options to be verified if not existing
     * @param isRegex
     *         true if label is regular expression, false by default
     * @param timeOut
     *         system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return true if options with given displayed text do not present; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static boolean verifyOptionNotPresentByLabel(TestObject to, String label, boolean isRegex, int timeOut, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyOptionNotPresentByLabel", to, label, isRegex, timeOut, flowControl)
    }

    /**
     * Verify if the options with the given displayed texts do not exist.
     *
     * @param to
     *         represent a web element
     * @param label
     *          displayed texts of the options to be verified if not existing
     * @param isRegex
     *         true if label is regular expression, false by default
     * @param timeOut
     *         system will wait at most timeout (seconds) to return result
     * @return true if options with given displayed text do not present; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static boolean verifyOptionNotPresentByLabel(TestObject to, String label, boolean isRegex, int timeOut) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyOptionNotPresentByLabel", to, label, isRegex, timeOut)
    }

    /**
     * Verify if the options with the given value do not present.
     *
     * @param to
     *         represent a web element
     * @param value
     *         value of the options to be verified if NOT presenting.
     * @param isRegex
     *         true if label is regular expression, false by default.
     * @param timeOut
     *         system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return true if all options with given value do not present; otherwise, false.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static boolean verifyOptionNotPresentByValue(TestObject to, String value, boolean isRegex, int timeOut,
            FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyOptionNotPresentByValue", to, value, isRegex, timeOut, flowControl)
    }

    /**
     * Verify if the options with the given value do not present.
     *
     * @param to
     *         represent a web element
     * @param value
     *         value of the options to be verified if NOT presenting.
     * @param isRegex
     *         true if label is regular expression, false by default.
     * @param timeOut
     *         system will wait at most timeout (seconds) to return result
     * @return true if all options with given value do not present; otherwise, false.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static boolean verifyOptionNotPresentByValue(TestObject to, String value, boolean isRegex, int timeOut) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyOptionNotPresentByValue", to, value, isRegex, timeOut)
    }

    /**
     * Verify if the options with the given displayed texts are selected.
     *
     * @param to
     *         represent a web element
     * @param label
     *         displayed text of the option to be verified if being selected
     * @param isRegex
     *         true if value is regular expression, false by default.
     * @param timeOut
     *         system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return true if all options with given displayed texts are selected; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static boolean verifyOptionSelectedByLabel(TestObject to, String label, boolean isRegex, int timeOut, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyOptionSelectedByLabel", to,label, isRegex, timeOut, flowControl)
    }

    /**
     * Verify if the options with the given displayed texts are selected.
     *
     * @param to
     *         represent a web element
     * @param label
     *         displayed text of the option to be verified if being selected
     * @param isRegex
     *         true if value is regular expression, false by default.
     * @param timeOut
     *         system will wait at most timeout (seconds) to return result
     * @return true if all options with given displayed texts are selected; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static boolean verifyOptionSelectedByLabel(TestObject to, String label, boolean isRegex, int timeOut) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyOptionSelectedByLabel", to,label, isRegex, timeOut)
    }

    /**
     * Verify if the options with the given value are selected.
     *
     * @param to
     *         represent a web element
     * @param value
     *         value of the options to be verified if being selected.
     * @param isRegex
     *         true if value is regular expression, false by default.
     * @param timeOut
     *         system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return true if all options with given value are selected; otherwise, false.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static boolean verifyOptionSelectedByValue(TestObject to, String value, boolean isRegex, int timeOut, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyOptionSelectedByValue", to, value, isRegex, timeOut, flowControl)
    }

    /**
     * Verify if the options with the given value are selected.
     *
     * @param to
     *         represent a web element
     * @param value
     *         value of the options to be verified if being selected.
     * @param isRegex
     *         true if value is regular expression, false by default.
     * @param timeOut
     *         system will wait at most timeout (seconds) to return result
     * @return true if all options with given value are selected; otherwise, false.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static boolean verifyOptionSelectedByValue(TestObject to, String value, boolean isRegex, int timeOut) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyOptionSelectedByValue", to, value, isRegex, timeOut)
    }

    /**
     * Verify if the options with the given displayed texts are not selected.
     *
     * @param to
     *         represent a web element
     * @param label
     *         labels displayed texts of the options to be verified if not being selected.
     * @param isRegex
     *         true if label is regular expression, false by default.
     * @param timeOut
     *         system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return true if all options with given displayed texts are not selected; otherwise, false.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static boolean verifyOptionNotSelectedByLabel(TestObject to, String label, boolean isRegex, int timeOut, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyOptionNotSelectedByLabel", to, label, isRegex, timeOut, flowControl)
    }

    /**
     * Verify if the options with the given displayed texts are not selected.
     *
     * @param to
     *         represent a web element
     * @param label
     *         labels displayed texts of the options to be verified if not being selected.
     * @param isRegex
     *         true if label is regular expression, false by default.
     * @param timeOut
     *         system will wait at most timeout (seconds) to return result
     * @return true if all options with given displayed texts are not selected; otherwise, false.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static boolean verifyOptionNotSelectedByLabel(TestObject to, String label, boolean isRegex, int timeOut) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyOptionNotSelectedByLabel", to, label, isRegex, timeOut)
    }

    /**
     * Verify if the options with the given value are not selected.
     *
     * @param to
     *         represent a web element
     * @param value
     *         value of the options to be verified if not being selected.
     * @param isRegex
     *         true if label is regular expression, false by default.
     * @param timeOut
     *         system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return true if all options with given value are not selected; otherwise, false.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static boolean verifyOptionNotSelectedByValue(TestObject to, String value, boolean isRegex, int timeOut, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyOptionNotSelectedByValue", to, value, isRegex, timeOut, flowControl)
    }

    /**
     * Verify if the options with the given value are not selected.
     *
     * @param to
     *         represent a web element
     * @param value
     *         value of the options to be verified if not being selected.
     * @param isRegex
     *         true if label is regular expression, false by default.
     * @param timeOut
     *         system will wait at most timeout (seconds) to return result
     * @return true if all options with given value are not selected; otherwise, false.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static boolean verifyOptionNotSelectedByValue(TestObject to, String value, boolean isRegex, int timeOut) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyOptionNotSelectedByValue", to, value, isRegex, timeOut)
    }

    /**
     * Verify if the options at the given indices are selected.
     *
     * @param to
     *         represent a web element
     * @param range
     *            list of indexes of the options to be verified if being
     *            selected
     * @param timeOut
     *         system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return true if all options at given indices are selected; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static boolean verifyOptionSelectedByIndex(TestObject to, Object range, int timeOut, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyOptionSelectedByIndex", to, range, timeOut, flowControl)
    }

    /**
     * Verify if the options at the given indices are selected.
     *
     * @param to
     *         represent a web element
     * @param range
     *            list of indexes of the options to be verified if being
     *            selected
     * @param timeOut
     *         system will wait at most timeout (seconds) to return result
     * @return true if all options at given indices are selected; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static boolean verifyOptionSelectedByIndex(TestObject to, Object range, int timeOut) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyOptionSelectedByIndex", to, range, timeOut)
    }

    /**
     * Verify if the options at the given indices are not selected
     *
     * @param to
     *         represent a web element
     * @param indexes
     *            the indexes of the options to be verified if not being
     *            selected
     * @param timeOut
     *         system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return true if all options at given indices are not selected; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static boolean verifyOptionNotSelectedByIndex(TestObject to, Object range, int timeOut, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyOptionNotSelectedByIndex", to, range, timeOut, flowControl)
    }

    /**
     * Verify if the options at the given indices are not selected
     *
     * @param to
     *         represent a web element
     * @param indexes
     *            the indexes of the options to be verified if not being
     *            selected
     * @param timeOut
     *         system will wait at most timeout (seconds) to return result
     * @return true if all options at given indices are not selected; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static boolean verifyOptionNotSelectedByIndex(TestObject to, Object range, int timeOut) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyOptionNotSelectedByIndex", to, range, timeOut)
    }

    /**
     * Use this keyword to switch back to default Window, after deal with some
     * framed element
     *
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_FRAME)
    public static void switchToDefaultContent(FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "switchToDefaultContent", flowControl)
    }

    /**
     * Use this keyword to switch back to default Window, after deal with some
     * framed element
     *
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_FRAME)
    public static void switchToDefaultContent() throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "switchToDefaultContent")
    }

    /**
     * Delete all cookies of all windows.
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_BROWSER)
    public static void deleteAllCookies(FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "deleteAllCookies", flowControl)
    }

    /**
     * Delete all cookies of all windows.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_BROWSER)
    public static void deleteAllCookies() throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "deleteAllCookies")
    }

    /**
     * Wait for the web page to load within the given time in second unit.
     *
     * @param seconds
     *         the number of seconds to wait
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_BROWSER)
    public static void waitForPageLoad(int seconds, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "waitForPageLoad", seconds, flowControl)
    }

    /**
     * Wait for the web page to load within the given time in second unit.
     *
     * @param seconds
     *         the number of seconds to wait
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_BROWSER)
    public static void waitForPageLoad(int seconds) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "waitForPageLoad", seconds)
    }

    /**
     * Wait for Angular/AJAX to load within the given time in second unit.
     *
     * @param seconds
     *         the number of seconds to wait
     * @return true if Angular/AJAX is ready. Otherwise, false.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_BROWSER)
    public static boolean waitForAngularLoad(int seconds) throws StepFailedException {
        return KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "waitForAngularLoad", seconds)
    }

    /**
     * Wait for Angular/AJAX call to load within the given time in second unit.
     *
     * @param seconds
     *         the number of seconds to wait
     * @param flowControl
     * @return true if Angular/AJAX is ready. Otherwise, false.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_BROWSER)
    public static boolean waitForAngularLoad(int seconds, FailureHandling flowControl) throws StepFailedException {
        return KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "waitForAngularLoad", seconds, flowControl)
    }

    /**
     * Wait for jQuery to load within the given time in second unit.
     * 
     * @param seconds
     *         the number of seconds to wait
     * @return true if jQuery is ready. Otherwise, false.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_BROWSER)
    public static boolean waitForJQueryLoad(int seconds) throws StepFailedException {
        return KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "waitForJQueryLoad", seconds)
    }

    /**
     * Wait for jQuery to load within the given time in second unit.
     *
     * @param seconds
     *         the number of seconds to wait
     * @param flowControl
     * @return true if jQuery is ready. Otherwise, false.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_BROWSER)
    public static boolean waitForJQueryLoad(int seconds, FailureHandling flowControl) throws StepFailedException {
        return KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "waitForJQueryLoad", seconds, flowControl)
    }

    /**
     * Modify property of test object. If the property is not existed then the
     * property will be created. If the changed value is null then the existed
     * value will not be changed. Use when test object has attributes changing
     * in runtime. This keyword does not modify the object saved in Object
     * Repository, instead, it creates another test object, modify and return
     * this test object. Hence, users must use a variable to get the returned
     * object.
     *
     * @param testObject
     *          represent a web element
     * @param propertyName
     *          name of the property, for example, xpath, id, name,... 
     *          <p>If the property already exists in the object, the keyword will modify its related artifacts; 
     *          if not, the keyword will add new property.
     * @param matchCondition
     *          condition to match property name with property value, for example, equals, not equals,... 
     *          <p>In case the property already exists, input null to this argument to keep the old value of match condition.
     * @param modifyValue
     *          value of the property. 
     *          <p>In case the property already exists, input null to this argument to keep the old property value.
     * @param isActive
     *          true if the property is checked (used to find the test object); otherwise, false. 
     *          <p>In case the property already exists, input null to this argument to keep the old value.
     * @param flowControl
     * @return the newly created TestObject
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_UTILITIES)
    public static TestObject modifyObjectProperty(TestObject testObject, String propertyName, String matchCondition, String modifyValue, boolean isActive, FailureHandling flowControl) {
        return (TestObject) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "modifyObjectProperty", testObject, propertyName, matchCondition, modifyValue, isActive, flowControl)
    }

    /**
     * Modify property of test object. If the property is not existed then the
     * property will be created. If the changed value is null then the existed
     * value will not be changed. Use when test object has attributes changing
     * in runtime. This keyword does not modify the object saved in Object
     * Repository, instead, it creates another test object, modify and return
     * this test object. Hence, users must use a variable to get the returned
     * object.
     *
     * @param testObject
     *          represent a web element
     * @param propertyName
     *          name of the property, for example, xpath, id, name,...
     *          <p>If the property already exists in the object, the keyword will modify its related artifacts
     *          if not, the keyword will add new property.
     * @param matchCondition
     *          condition to match property name with property value, for example, equals, not equals,...
     *          <p>In case the property already exists, input null to this argument to keep the old value of match condition.
     * @param modifyValue
     *          value of the property.
     *          <p>In case the property already exists, input null to this argument to keep the old property value.
     * @param isActive
     *          true if the property is checked (used to find the test object); otherwise, false.
     *          <p>In case the property already exists, input null to this argument to keep the old value.
     * @return the newly created TestObject
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_UTILITIES)
    public static TestObject modifyObjectProperty(TestObject testObject, String propertyName, String matchCondition, String modifyValue, boolean isActive) {
        return (TestObject) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "modifyObjectProperty", testObject, propertyName, matchCondition, modifyValue, isActive)
    }

    /**
     * Remove existing property of test object. Use when test object 
     * has attributes changing in runtime. This keyword does not 
     * modify the object saved in Object Repository, instead, it creates 
     * another test object, modify and return this test object. Hence, 
     * users must use a variable to store the returned object.
     *
     * @param testObject
     *          represent a web element
     * @param propertyName
     *          name of the property, for example, xpath, id, name,...
     * @param flowControl
     * @return the new TestObject after its property is removed
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_UTILITIES)
    public static TestObject removeObjectProperty(TestObject testObject, String propertyName, FailureHandling flowControl) {
        return (TestObject) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "removeObjectProperty", testObject, propertyName, flowControl)
    }

    /**
     * Remove existing property of test object. Use when test object
     * has attributes changing in runtime. This keyword does not
     * modify the object saved in Object Repository, instead, it creates
     * another test object, modify and return this test object. Hence,
     * users must use a variable to store the returned object.
     *
     * @param testObject
     *          represent a web element
     * @param propertyName
     *          name of the property, for example, xpath, id, name,...
     * @return the new TestObject after its property is removed
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_UTILITIES)
    public static TestObject removeObjectProperty(TestObject testObject, String propertyName) {
        return (TestObject) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "removeObjectProperty", testObject, propertyName)
    }

    /***
     * Drag an object and drop it to another object
     *
     * @param sourceObject
     *            the source object
     * @param destinationObject
     *            the destination object
     * @param flowControl
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void dragAndDropToObject(TestObject sourceObject, TestObject destinationObject, FailureHandling flowControl) {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "dragAndDropToObject", sourceObject, destinationObject, flowControl)
    }

    /***
     * Drag an object and drop it to another object
     *
     * @param sourceObject
     *            the source object
     * @param destinationObject
     *            the destination object
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void dragAndDropToObject(TestObject sourceObject, TestObject destinationObject) {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "dragAndDropToObject", sourceObject, destinationObject)
    }

    /***
     * Drag an object and drop it to an offset location
     *
     * @param sourceObject
     *            the source object
     * @param xOffset
     *            x offset
     * @param yOffset
     *            y offset
     * @param flowControl
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void dragAndDropByOffset(TestObject sourceObject, int xOffset, int yOffset, FailureHandling flowControl) {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "dragAndDropByOffset", sourceObject, xOffset, yOffset, flowControl)
    }

    /***
     * Drag an object and drop it to an offset location
     *
     * @param sourceObject
     *            the source object
     * @param xOffset
     *            x offset
     * @param yOffset
     *            y offset
     * @param flowControl
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void dragAndDropByOffset(TestObject sourceObject, int xOffset, int yOffset) {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "dragAndDropByOffset", sourceObject, xOffset, yOffset)
    }

    /**
     * Navigate to a page that requires authentication. System will enter username and password
     * @param url
     *          url of the page to navigate (optional)
     * @param userName
     *          username to authenticate
     * @param password
     *          password to authenticate
     * @param timeout 
     *          time to wait since navigating to the page until entering username
     * @param flowControl
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_BROWSER)
    public static void authenticate(final String url, String userName, String password, int timeout, FailureHandling flowControl) {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "authenticate", url, userName, password, timeout, flowControl)
    }

    /**
     * Navigate to a page that requires authentication. System will enter username and password
     * @param url
     *          url of the page to navigate (optional)
     * @param userName
     *          username to authenticate
     * @param password
     *          password to authenticate
     * @param timeout
     *          time to wait since navigating to the page until entering username
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_BROWSER)
    public static void authenticate(final String url, String userName, String password, int timeout) {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "authenticate", url, userName, password, timeout)
    }

    /**
     * Click on an image on the web page
     * @param to
     *       represent an image
     * @param flowControl
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_IMAGE)
    public static void clickImage(TestObject to, FailureHandling flowControl) {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "clickImage", to, flowControl)
    }

    /**
     * Click on an image on the web page
     * @param to
     *       represent an image
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_IMAGE)
    public static void clickImage(TestObject to) {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "clickImage", to)
    }

    /**
     * Type on an image on the web page
     * @param to
     *       represent an image
     * @param text
     *          text to type on the image
     * @param flowControl
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_IMAGE)
    public static void typeOnImage(TestObject to, String text, FailureHandling flowControl) {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "typeOnImage", to, text, flowControl)
    }

    /**
     * Type on an image on the web page
     * @param to
     *       represent an image
     * @param text
     *          text to type on the image
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_IMAGE)
    public static void typeOnImage(TestObject to, String text) {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "typeOnImage", to, text)
    }

    /**
     * Verify if an image is present on page
     * @param to
     *       represent an image
     * @param flowControl
     * @return true if the image if present; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_IMAGE)
    public static boolean verifyImagePresent(TestObject to, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyImagePresent", to, flowControl)
    }

    /**
     * Verify if an image is present on page
     * @param to
     *       represent an image
     * @return true if the image if present; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_IMAGE)
    public static boolean verifyImagePresent(TestObject to) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyImagePresent", to)
    }

    /**
     * Wait for an image to be presented on page
     * @param to
     *       represent an image
     * @param timeOutInSeconds 
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return true if the image if present; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_IMAGE)
    public static boolean waitForImagePresent(TestObject to, int timeOutInSeconds, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "waitForImagePresent", to, timeOutInSeconds, flowControl)
    }

    /**
     * Wait for an image to be presented on page
     * @param to
     *       represent an image
     * @param timeOutInSeconds
     *      system will wait at most timeout (seconds) to return result
     * @return true if the image if present; otherwise, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_IMAGE)
    public static boolean waitForImagePresent(TestObject to, int timeOutInSeconds) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "waitForImagePresent", to, timeOutInSeconds)
    }

    /**
     * Internal method to find web element by test object
     * @param to
     *      represent a web element
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @return
     *      the found web element or null if cannot find any
     * @throws IllegalArgumentException
     * @throws WebElementNotFoundException
     * @throws StepFailedException
     */
    @CompileStatic
    public static WebElement findWebElement(TestObject to, int timeOut = RunConfiguration.getTimeOut()) throws IllegalArgumentException, WebElementNotFoundException, StepFailedException {
        return (WebElement) WebUIAbstractKeyword.findWebElement(to, timeOut)
    }

    /**
     * Internal method to find web elements by test object
     * @param to
     *      represent a web element
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @return
     *      the found web elements or null if cannot find any
     * 
     * @throws IllegalArgumentException
     * @throws WebElementNotFoundException
     * @throws StepFailedException
     */
    @CompileStatic
    public static List<WebElement> findWebElements(TestObject to, int timeOut) throws WebElementNotFoundException {
        return (List<WebElement>) WebUIAbstractKeyword.findWebElements(to, timeOut)
    }

    /**
     * Switch the current context into an iframe
     * @param to
     *      represent a web element
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return
     *      true if the current context is switched to the iframe; otherwise, false
     * @throws IllegalArgumentException
     * @throws WebElementNotFoundException
     * @throws StepFailedException
     * @throws WebDriverException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_FRAME)
    public static boolean switchToFrame(TestObject to, int timeOut, FailureHandling flowControl) throws IllegalArgumentException, WebElementNotFoundException, StepFailedException, WebDriverException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "switchToFrame", to, timeOut, flowControl)
    }

    /**
     * Switch the current context into an iframe
     * @param to
     *      represent a web element
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @return
     *      true if the current context is switched to the iframe; otherwise, false
     * @throws IllegalArgumentException
     * @throws WebElementNotFoundException
     * @throws StepFailedException
     * @throws WebDriverException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_FRAME)
    public static boolean switchToFrame(TestObject to, int timeOut) throws IllegalArgumentException, WebElementNotFoundException, StepFailedException, WebDriverException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "switchToFrame", to, timeOut)
    }

    /**
     * Take screenshot of the browser
     * @param fileName
     *      the absolute path of the saved screenshot image file
     * @return the captured file path.
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_UTILITIES)
    public static String takeScreenshot(String fileName) {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "takeScreenshot", fileName)
    }

    /**
     * Take screenshot of the browser
     * @param fileName
     *      the absolute path of the saved screenshot image file
     * @param flowControl
     * @return the captured file path.
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_UTILITIES)
    public static String takeScreenshot(String fileName, FailureHandling flowControl) {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "takeScreenshot", fileName, flowControl)
    }

    /**
     * Take screenshot of the browser
     * @param flowControl
     * @return the captured file path.
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_UTILITIES)
    public static String takeScreenshot(FailureHandling flowControl) {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "takeScreenshot", flowControl)
    }

    /**
     * Take screenshot of the browser.
     * @return the captured file path.
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_UTILITIES)
    public static String takeScreenshot() {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "takeScreenshot")
    }

    /**
     * Upload file to an input html element with type = "file"
     * @param to 
     *    represent a web element.
     * @param fileAbsolutePath
     *       absolute path of the file on local machine
     * @param flowControl
     *       flow control
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_KEYBOARD)
    public static void uploadFile(TestObject to, String fileAbsolutePath, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "uploadFile", to, fileAbsolutePath, flowControl)
    }

    /**
     * Upload file to an input html element with type = "file"
     * @param to
     *    represent a web element.
     * @param fileAbsolutePath
     *       absolute path of the file on local machine
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_KEYBOARD)
    public static void uploadFile(TestObject to, String fileAbsolutePath) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "uploadFile", to, fileAbsolutePath)
    }

    /**
     * scrolls a element into the visible area of the browser window
     * @param to
     *    represent a web element
     * @param timeOut
     *       maximum period of time (in seconds) that system will wait to find the element
     * @param flowControl
     *       flow control
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void scrollToElement(TestObject to, int timeOut, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "scrollToElement", to, timeOut, flowControl)
    }

    /**
     * scrolls a element into the visible area of the browser window
     * @param to
     *    represent a web element
     * @param timeOut
     *       maximum period of time (in seconds) that system will wait to find the element
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void scrollToElement(TestObject to, int timeOut) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "scrollToElement", to, timeOut)
    }

    /**
     * Deprecated. As of Katalon version 3.7.0.0, replaced by keyword com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords.verifyElementInViewport.
     * Verify if the web element is visible in current view port
     * @param to
     *      represent a web element
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return true if element is present and visible in viewport; otherwise, false
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    @Deprecated
    public static boolean verifyElementVisibleInViewport(TestObject to, int timeOut, FailureHandling flowControl) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyElementVisibleInViewport", to, timeOut, flowControl)
    }

    /**
     * Deprecated. As of Katalon version 3.7.0.0, replaced by keyword com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords.verifyElementInViewport.
     * Verify if the web element is visible in current view port
     * @param to
     *      represent a web element
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @return true if element is present and visible in viewport; otherwise, false
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    @Deprecated
    public static boolean verifyElementVisibleInViewport(TestObject to, int timeOut) {
        return verifyElementVisibleInViewport(to, timeOut, RunConfiguration.getDefaultFailureHandling());
    }

    /**
     * Verify if the web element is visible in current view port
     * @param to
     *      represent a web element
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return true if element is present and visible in viewport; otherwise, false
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementInViewport(TestObject to, int timeOut, FailureHandling flowControl) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyElementVisibleInViewport", to, timeOut, flowControl)
    }

    /**
     * Deprecated. As of Katalon version 3.7.0.0, replaced by keyword com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords.verifyElementInViewport.
     * Verify if the web element is visible in current view port
     * @param to
     *      represent a web element
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @return true if element is present and visible in viewport; otherwise, false
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    @Deprecated
    public static boolean verifyElementInViewport(TestObject to, int timeOut) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyElementVisibleInViewport", to, timeOut)
    }


    /**
     * Deprecated. As of Katalon version 3.7.0.0, replaced by keyword com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords.verifyElementNotInViewport.
     * Verify if the web element is NOT visible in current view port
     * @param to
     *      represent a web element
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return true if element is present and NOT visible in viewport; otherwise, false
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    @Deprecated
    public static boolean verifyElementNotVisibleInViewport(TestObject to, int timeOut, FailureHandling flowControl) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyElementNotVisibleInViewport", to, timeOut, flowControl)
    }

    /**
     * Deprecated. As of Katalon version 3.7.0.0, replaced by keyword com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords.verifyElementNotInViewport.
     * Verify if the web element is NOT visible in current view port
     * @param to
     *      represent a web element
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @return true if element is present and NOT visible in viewport; otherwise, false
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    @Deprecated
    public static boolean verifyElementNotVisibleInViewport(TestObject to, int timeOut) {
        return verifyElementNotInViewport(to, timeOut, RunConfiguration.getDefaultFailureHandling());
    }


    /**
     * Verify if the web element is NOT visible in current view port
     * @param to
     *      represent a web element
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return true if element is present and NOT visible in viewport; otherwise, false
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementNotInViewport(TestObject to, int timeOut, FailureHandling flowControl) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyElementNotVisibleInViewport", to, timeOut, flowControl)
    }

    /**
     * Verify if the web element is NOT visible in current view port
     * @param to
     *      represent a web element
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @return true if element is present and NOT visible in viewport; otherwise, false
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementNotInViewport(TestObject to, int timeOut) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyElementNotVisibleInViewport", to, timeOut)
    }

    /**
     * Get current viewport's width value
     * @param flowControl
     * @return current viewport's width
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static int getViewportWidth(FailureHandling flowControl) throws StepFailedException {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getViewportWidth", flowControl)
    }

    /**
     * Get current viewport's width value
     * @return current viewport's width
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static int getViewportWidth() throws StepFailedException {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getViewportWidth")
    }

    /** 
     * Get current viewport's height value
     * @param flowControl
     * @return current viewport's height
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static int getViewportHeight(FailureHandling flowControl) throws StepFailedException {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getViewportHeight", flowControl)
    }

    /**
     * Get current viewport's height value
     * @return current viewport's height
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static int getViewportHeight() throws StepFailedException {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getViewportHeight")
    }

    /**
     * Verify if the web element has an attribute with the specific name
     * @param to
     *      represent a web element
     * @param attributeName
     *      the name of the attribute to verify
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return true if element has the attribute with the specific name; otherwise, false
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementHasAttribute(TestObject to, String attributeName, int timeOut, FailureHandling flowControl) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyElementHasAttribute", to, attributeName, timeOut, flowControl)
    }

    /**
     * Verify if the web element has an attribute with the specific name
     * @param to
     *      represent a web element
     * @param attributeName
     *      the name of the attribute to verify
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @return true if element has the attribute with the specific name; otherwise, false
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementHasAttribute(TestObject to, String attributeName, int timeOut) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyElementHasAttribute", to, attributeName, timeOut)
    }

    /**
     * Verify if the web element doesn't have an attribute with the specific name
     * @param to
     *      represent a web element
     * @param attributeName
     *      the name of the attribute to verify
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return true if element doesn't have the attribute with the specific name; otherwise, false
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementNotHasAttribute(TestObject to, String attributeName, int timeOut, FailureHandling flowControl) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyElementNotHasAttribute", to, attributeName, timeOut, flowControl)
    }

    /**
     * Verify if the web element doesn't have an attribute with the specific name
     * @param to
     *      represent a web element
     * @param attributeName
     *      the name of the attribute to verify
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @return true if element doesn't have the attribute with the specific name; otherwise, false
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementNotHasAttribute(TestObject to, String attributeName, int timeOut) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyElementNotHasAttribute", to, attributeName, timeOut)
    }

    /**
     * Verify if the web element has an attribute with the specific name and value
     * @param to
     *      represent a web element
     * @param attributeName
     *      the name of the attribute to verify
     * @param attributeValue
     *      the value of the attribute to verify
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return true if element has the attribute with the specific name and value; otherwise, false
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementAttributeValue(TestObject to, String attributeName, String attributeValue, int timeOut, FailureHandling flowControl) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyElementAttributeValue", to, attributeName, attributeValue, timeOut, flowControl)
    }

    /**
     * Verify if the web element has an attribute with the specific name and value
     * @param to
     *      represent a web element
     * @param attributeName
     *      the name of the attribute to verify
     * @param attributeValue
     *      the value of the attribute to verify
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @return true if element has the attribute with the specific name and value; otherwise, false
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementAttributeValue(TestObject to, String attributeName, String attributeValue, int timeOut) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyElementAttributeValue", to, attributeName, attributeValue, timeOut)
    }

    /**
     * Wait until the given web element has an attribute with the specific name
     * @param to
     *      represent a web element
     * @param attributeName
     *      the name of the attribute to wait for
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @param flowControl
     * @return true if element has the attribute with the specific name; otherwise, false
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean waitForElementHasAttribute(TestObject to, String attributeName, int timeOut, FailureHandling flowControl) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "waitForElementHasAttribute", to, attributeName, timeOut, flowControl)
    }

    /**
     * Wait until the given web element has an attribute with the specific name
     * @param to
     *      represent a web element
     * @param attributeName
     *      the name of the attribute to wait for
     * @param timeOut
     *      system will wait at most timeout (seconds) to return result
     * @return true if element has the attribute with the specific name; otherwise, false
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean waitForElementHasAttribute(TestObject to, String attributeName, int timeOut) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "waitForElementHasAttribute", to, attributeName, timeOut)
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
    public static boolean waitForElementNotHasAttribute(TestObject to, String attributeName, int timeOut, FailureHandling flowControl) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "waitForElementNotHasAttribute", to, attributeName, timeOut, flowControl)
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
    public static boolean waitForElementNotHasAttribute(TestObject to, String attributeName, int timeOut) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "waitForElementNotHasAttribute", to, attributeName, timeOut)
    }

    /**
     * Wait until the given web element has an attribute with the specific name and value
     * @param to
     *      represent a web element
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
    public static boolean waitForElementAttributeValue(TestObject to, String attributeName, String attributeValue, int timeOut, FailureHandling flowControl) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "waitForElementAttributeValue", to, attributeName, attributeValue, timeOut, flowControl)
    }

    /**
     * Wait until the given web element has an attribute with the specific name and value
     * @param to
     *      represent a web element
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
    public static boolean waitForElementAttributeValue(TestObject to, String attributeName, String attributeValue, int timeOut) {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "waitForElementAttributeValue", to, attributeName, attributeValue, timeOut)
    }

    /**
     * Set the size of the current window. This will change the outer window dimension and the viewport, synonymous to window.resizeTo() in JS.
     * @param width
     *      the target viewport width
     * @param height
     *      the target viewport height
     * @param flowControl
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static void setViewPortSize(int width, int height, FailureHandling flowControl) {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "setViewPortSize", width, height, flowControl)
    }

    /**
     * Set the size of the current window. This will change the outer window dimension and the viewport, synonymous to window.resizeTo() in JS.
     * @param width
     *      the target viewport width
     * @param height
     *      the target viewport height
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static void setViewPortSize(int width, int height) {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "setViewPortSize", width, height)
    }

    /**
     * Scroll the viewport to a specific position
     * @param x
     *      x position
     * @param y
     *      y position
     * @param flowControl
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static void scrollToPosition(int x, int y, FailureHandling flowControl) {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "scrollToPosition", x, y, flowControl)
    }

    /**
     * Scroll the viewport to a specific position
     * @param x
     *      x position
     * @param y
     *      y position
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static void scrollToPosition(int x, int y) {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "scrollToPosition", x, y)
    }

    /**
     * Get current web page's width
     * @param flowControl
     * @return current web page's width
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static int getPageWidth(FailureHandling flowControl) {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getPageWidth", flowControl)
    }

    /**
     * Get current web page's width
     * @return current web page's width
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static int getPageWidth() {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getPageWidth")
    }

    /**
     * Get current web page's height
     * @param flowControl
     * @return current web page's height
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static int getPageHeight(FailureHandling flowControl) {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getPageHeight", flowControl)
    }

    /**
     * Get current web page's height
     * @param flowControl
     * @return current web page's height
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static int getPageHeight() {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getPageHeight")
    }

    /**
     * Get current view port left (x) position relatively to the web page
     * @param flowControl
     * @return current view port left (x) position
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static int getViewportLeftPosition(FailureHandling flowControl) {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getViewportLeftPosition", flowControl)
    }

    /**
     * Get current view port left (x) position relatively to the web page
     * @return current view port left (x) position
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static int getViewportLeftPosition() {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getViewportLeftPosition")
    }

    /**
     * Get current view port top (y) position relatively to the web page
     * @param flowControl
     * @return current view port top (y) position
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static int getViewportTopPosition(FailureHandling flowControl) {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getViewportTopPosition", flowControl)
    }

    /**
     * Get current view port top (y) position relatively to the web page
     * @return current view port top (y) position
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static int getViewportTopPosition() {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getViewportTopPosition")
    }

    /**
     * Get CSS value of a web element
     * @param to
     *      represent the web element
     * @param css
     *      represent the css property name of the element
     * @return the current, computed value of the property
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static String getCSSValue(TestObject to, String css) {
        return (String) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getCSSValue", to, css)
    }

    /**
     * Get CSS value of a web element
     * @param to
     *      represent the web element
     * @param css
     *      represent the css property name of the element
     * @return the current, computed value of the property
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static String getCSSValue(TestObject to, String css, FailureHandling flowControl) {
        return (String) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getCSSValue", to, css, flowControl)
    }

    /**
     * Get left position of web element
     * @param to represent the web element
     * @param flowControl
     * @return left position of web element
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static int getElementLeftPosition(TestObject to, FailureHandling flowControl) throws StepFailedException {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getElementLeftPosition", to, flowControl)
    }

    /**
     * Get the left position of web element
     * @param to represent the web element
     * @return left position of web element
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static int getElementLeftPosition(TestObject to) throws StepFailedException {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getElementLeftPosition", to)
    }

    /**
     * Get width of web element
     * @param to represent the web element
     * @param flowControl
     * @return width of web element
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static int getElementWidth(TestObject to, FailureHandling flowControl) {
        return (int)KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getElementWidth", to, flowControl)
    }

    /**
     * Get width of web element
     * @param to represent the web element
     * @return width of web element
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static int getElementWidth(TestObject to) {
        return (int)KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getElementWidth", to)
    }

    /**
     * Get height of web element
     * @param to represent the web element
     * @param flowControl
     * @return height of web element
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static int getElementHeight(TestObject to, FailureHandling flowControl) {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getElementHeight", to, flowControl)
    }

    /**
     * Get height of web element
     * @param to represent the web element
     * @return height of web element
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static int getElementHeight(TestObject to) {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getElementHeight", to)
    }

    /**
     * Verify if all expected options are present within the given test object.
     *
     * @param to
     *         represent a web element
     * @param expectedOptions
     *         the list of all expected options for the given web element
     * @param flowControl
     * @return true if all expected options are present. Otherwise, false.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static boolean verifyOptionsPresent(TestObject to, List expectedOptions, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyOptionsPresent", to, expectedOptions, flowControl)
    }

    /**
     * Verify if all expected options are present within the given test object.
     *
     * @param to
     *         represent a web element
     * @param expectedOptions
     *         the list of all expected options for the given web element
     * @return true if all expected options are present. Otherwise, false.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_COMBOBOX)
    public static boolean verifyOptionsPresent(TestObject to, List expectedOptions) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyOptionsPresent", to, expectedOptions)
    }

    /**
     * Verify text of an element.
     *
     * @param to
     *          represent a web element.
     * @param expectedText
     *          text of the element to verify.
     * @param flowControl
     * @return true if the element has the desired text, otherwise false.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementText(TestObject to, String expectedText, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyElementText", to, expectedText, flowControl)
    }

    /**
     * Verify text of an element.
     *
     * @param to
     *          represent a web element.
     * @param expectedText
     *          text of the element to verify.
     * @return true if the element has the desired text, otherwise false.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementText(TestObject to, String expectedText) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyElementText", to, expectedText)
    }

    /**
     * Get all links on current page
     * 
     * @since 4.8
     * @param isIncludedExternalLinks If false, only target to links on the same HOST. Otherwise, target to all links.
     * @param excludedLinks A list of excluded links (URLs)
     * @param flowControl FailureHandling
     * @return A list of links (URLs)
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static List getAllLinksOnCurrentPage(boolean isIncludedExternalLinks, List excludedLinks, FailureHandling flowControl) throws StepFailedException {
        return (List) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getAllLinksOnCurrentPage", isIncludedExternalLinks, excludedLinks, flowControl)
    }

    /**
     * Get all links on current page
     *
     * @since 4.8
     * @param isIncludedExternalLinks If false, only target to links on the same HOST. Otherwise, target to all links.
     * @param excludedLinks A list of excluded links (URLs)
     * @return A list of links (URLs)
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static List getAllLinksOnCurrentPage(boolean isIncludedExternalLinks, List excludedLinks) throws StepFailedException {
        return (List) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "getAllLinksOnCurrentPage", isIncludedExternalLinks, excludedLinks)
    }

    /**
     * Verify a list of links (URLs) are accessible
     * 
     * @since 4.8
     * @param links A list of links (URLs)
     * @param flowControl FailureHandling
     * @return true if all provided links are accessible. Otherwise, false.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyLinksAccessible(List links, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyLinksAccessible", links, flowControl)
    }

    /**
     * Verify a list of links (URLs) are accessible
     *
     * @since 4.8
     * @param links A list of links (URLs)
     * @return true if all provided links are accessible. Otherwise, false.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyLinksAccessible(List links) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyLinksAccessible", links)
    }

    /**
     * Verify all links (URLs) on the current page are accessible
     * 
     * @since 4.8
     * @param isIncludedExternalLinks If false, only target to links on the same HOST. Otherwise, target to all links.
     * @param excludedLinks A list of excluded links (URLs)
     * @param flowControl FailureHandling
     * @return true if all links on the current page are accessible. Otherwise, false.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyAllLinksOnCurrentPageAccessible(boolean isIncludedExternalLinks, List excludedLinks, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyAllLinksOnCurrentPageAccessible", isIncludedExternalLinks, excludedLinks, flowControl)
    }

    /**
     * Verify all links (URLs) on the current page are accessible
     *
     * @since 4.8
     * @param isIncludedExternalLinks If false, only target to links on the same HOST. Otherwise, target to all links.
     * @param excludedLinks A list of excluded links (URLs)
     * @return true if all links on the current page are accessible. Otherwise, false.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyAllLinksOnCurrentPageAccessible(boolean isIncludedExternalLinks, List excludedLinks) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "verifyAllLinksOnCurrentPageAccessible", isIncludedExternalLinks, excludedLinks)
    }    
    
    /**
     * Execute JavaScript on the currently selected frame or window.</br>
     * The provided script fragment will be executed as the body of an anonymous function.
     *
     * @since 5.0
     * @param script The JavaScript to execute.
     * @param arguments The arguments to the script. May be empty or null.
     * @param flowControl FailureHandling.
     * @return Boolean, Long, Double, String, List, WebElement, or null.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static Object executeJavaScript(String script, List arguments, FailureHandling flowControl) throws StepFailedException {
        return (Object) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "executeJavaScript", script, arguments, flowControl)
    }

    /**
     * Execute JavaScript on the currently selected frame or window.</br>
     * The provided script fragment will be executed as the body of an anonymous function.
     * 
     * @since 5.0
     * @param script The JavaScript to execute.
     * @param arguments The arguments to the script. May be empty or null.
     * @return Boolean, Long, Double, String, List, WebElement, or null.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_WINDOW)
    public static Object executeJavaScript(String script, List arguments) throws StepFailedException {
        return (Object) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "executeJavaScript", script, arguments)
    }
    
    
    /**
     * Clear all text of the test object.
     * 
     * @since 5.3
     * @param to represent a web element.
     * @param flowControl FailureHandling
     * @return true if text of object is clear. Otherwise, false.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean clearText(TestObject to, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "clearText", to, flowControl)
    }
    
    /**
     * Clear all text of the test object.
     * 
     * @since 5.3
     * @param to represent a web element.
     * @return true if text of object is clear. Otherwise, false.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean clearText(TestObject to) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "clearText", to)
    }
    
    /**
     * Convert a WebElement to a TestObject. It will create
     * a Test Object with no name that wraps around the given WebElement.
     * When the Test Object is used by built-in keywords, it is unwrapped
     * and the given WebElement will be used
     *
     * @since 6.2.0
     * @param webElement the WebElement retrieved by Selenium or other APIs
     * @param flowControl failureHandling
     * @return a TestObject that wraps around the given WebElement
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static TestObject convertWebElementToTestObject(WebElement webElement, FailureHandling flowControl) throws StepFailedException {
        return (TestObject) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "convertWebElementToTestObject", webElement, flowControl)
    }
    
    /**
     * Convert a WebElement to a TestObject. It will create
     * a Test Object with no name that wraps around the given WebElement.
     * When the Test Object is used by built-in keywords, it is unwrapped
     * and the given WebElement will be used
     *
     * @since 6.2.0
     * @param webElement the WebElement retrieved by Selenium or other APIs
     * @return a TestObject that wraps around the given WebElement
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static TestObject convertWebElementToTestObject(WebElement webElement) throws StepFailedException {
        return (TestObject) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "convertWebElementToTestObject", webElement)
    }
    
    /**
     * Enable smart wait functionality. Katalon will precede actions on elements by waiting
     * activities to ensure that the page is not loading elements or executing AJAX calls. 
     * Such waiting activities greatly increase the chance that elements are available or interactable. 
     * <p>
     * It is recommended to always enable smart wait functionality globally in project setting. 
     * </p>
     * 
     * @since 6.3.4
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void enableSmartWait() throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "enableSmartWait")
    }

    /**
     * Disable smart wait functionality. If global smart wait option
     * is enabled, then this keyword will turn off the smart wait functionality until the end of 
     * the calling test case or until the keyword {@link WebUiBuiltinKeywords#enableSmartWait} is called.
     * 
     * @since 6.3.4
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static void disableSmartWait() throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB, "disableSmartWait")
    }
}
