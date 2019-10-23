package com.kms.katalon.core.webui.driver;

import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.webui.common.internal.SmartWait;
import com.kms.katalon.selenium.driver.IDelayableDriver;

/**
 * Event listener that triggers smart wait functionality on
 * <ul>
 * <li>before finding element</li>
 * </ul>
 *
 */
public class SmartWaitWebEventListener extends AbstractWebDriverEventListener {

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
                .orElse(false);

        boolean globalSmartWaitEnabled = (boolean) Optional
                .ofNullable(RunConfiguration.getExecutionProperties().get(RunConfiguration.GLOBAL_SMART_WAIT_MODE))
                .orElse(false);

        if (localSmartWaitEnabled || globalSmartWaitEnabled) {
            SmartWait.doSmartWait();
        }
    }

}
