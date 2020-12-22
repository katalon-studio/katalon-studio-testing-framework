package com.kms.katalon.core.webui.driver;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.hash.Hashing;
import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.webui.common.internal.SmartWait;
import com.kms.katalon.selenium.driver.IDelayableDriver;

/**
 * Event listener that triggers smart wait functionality on
 * <ul>
 * <li>before finding element</li>
 * </ul>
 *
 */
public class KatalonSmartEventListener extends AbstractWebDriverEventListener {

	private static String encode(String testArtifactName) {
		return Hashing.sha256().hashString(testArtifactName, StandardCharsets.UTF_8).toString();
	}

    /**
     * This method injects Test Case's hashed ID into a DOM element after the
     * first navigation occurs in the test case.
     * Browser extension will read the value of this DOM element.
     * <p>
     * The Test Case is hashed
     * into equal-length string so that the extension script can append this
     * equal-length string to the MHTML file in a byte array and send back to server
     * </p>
     */
    public static void makeTestCaseIDAvailableToBrowserExtension(WebDriver webDriver) {
        // Wait for page load
        new WebDriverWait(webDriver, 30).until(
                driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
        // Retrieve element #katalon_only_element and set current test case
        WebElement element = webDriver.findElement(By.id("katalon_only_element"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        String currentTestCase = (String) RunConfiguration.getExecutionProperties()
                .getOrDefault(RunConfiguration.CURRENT_TESTCASE, "");
        js.executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);", element, "value",
                encode(currentTestCase));
    }

    @Override
    public void afterNavigateTo(String url, WebDriver driver) {
        try {
            if (RunConfiguration.shouldApplyTimeCapsule()) {
                makeTestCaseIDAvailableToBrowserExtension(DriverFactory.getWebDriver());
            }
        } catch (Exception e) {
            KeywordLogger.getInstance(KatalonSmartEventListener.class).logWarning(
                    "Could not make test case ID available to browser, Time Capsule is not supported on this browser.");
        }
        super.afterNavigateTo(url, driver);
    }

    @Override
    public void beforeClickOn(WebElement element, WebDriver driver) {
        doDelay(driver);
    }

    @Override
    public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
        doDelay(driver);
    }

    @Override
    public void beforeNavigateBack(WebDriver driver) {
        doDelay(driver);
    }

    @Override
    public void beforeNavigateForward(WebDriver driver) {
        doDelay(driver);
    }

    @Override
    public void beforeNavigateRefresh(WebDriver driver) {
        doDelay(driver);
    }

    @Override
    public void beforeNavigateTo(String url, WebDriver driver) {
        doDelay(driver);
    }

    @Override
    public void beforeAlertAccept(WebDriver driver) {
        doDelay(driver);
    }

    @Override
    public void beforeAlertDismiss(WebDriver driver) {
        doDelay(driver);
    }

    private void doDelay(WebDriver driver) {
        if (IDelayableDriver.class.isInstance(driver)) {
            ((IDelayableDriver) driver).delay();
        }
    }

    @Override
    public void beforeFindBy(By arg0, WebElement arg1, WebDriver arg2) {
        doSmartWait();
    }

    private void doSmartWait() {

        boolean localSmartWaitEnabled = (boolean) Optional
                .ofNullable(RunConfiguration.getExecutionProperties().get(RunConfiguration.LOCAL_SMART_WAIT_MODE))
                .orElse(true);

        boolean globalSmartWaitEnabled = (boolean) Optional
                .ofNullable(RunConfiguration.getExecutionProperties().get(RunConfiguration.GLOBAL_SMART_WAIT_MODE))
                .orElse(false);

        if (globalSmartWaitEnabled && localSmartWaitEnabled) {
            SmartWait.doSmartWait();
        }
    }

}
