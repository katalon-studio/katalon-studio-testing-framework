package com.kms.katalon.core.mobile.keyword.builtin

import java.text.MessageFormat

import org.apache.commons.lang3.StringUtils
import org.codehaus.groovy.transform.tailrec.VariableReplacedListener.*
import org.openqa.selenium.Dimension

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.mobile.constants.StringConstants
import com.kms.katalon.core.mobile.helper.MobileCommonHelper
import com.kms.katalon.core.mobile.keyword.*
import com.kms.katalon.core.mobile.keyword.internal.MobileAbstractKeyword
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.mobile.keyword.internal.MobileKeywordMain
import com.kms.katalon.core.model.FailureHandling

import groovy.transform.CompileStatic
import io.appium.java_client.AppiumDriver
import io.appium.java_client.MobileElement
import io.appium.java_client.TouchAction
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.connection.ConnectionState
import io.appium.java_client.ios.IOSDriver
import io.appium.java_client.touch.TapOptions
import io.appium.java_client.touch.offset.ElementOption
import io.appium.java_client.touch.offset.PointOption

@Action(value = "toggleAirplaneMode")
public class ToggleAirplaneModeKeyword extends MobileAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        String mode = (String) params[0]
        FailureHandling flowControl = (FailureHandling)(params.length > 1 && params[1] instanceof FailureHandling ? params[1] : RunConfiguration.getDefaultFailureHandling())
        toggleAirplaneMode(mode,flowControl)
    }

    @CompileStatic
    public void toggleAirplaneMode(String mode, FailureHandling flowControl) throws StepFailedException {
        MobileKeywordMain.runKeyword({
            AppiumDriver<?> driver = getAnyAppiumDriver()
            String context = driver.getContext()
            try {
                internalSwitchToNativeContext(driver)

                boolean isTurnOn = false
                if (StringUtils.equalsIgnoreCase("yes", mode) || StringUtils.equalsIgnoreCase("on", mode) || StringUtils.equalsIgnoreCase("true", mode)) {
                    isTurnOn = true
                }
                if (driver instanceof AndroidDriver) {
                    AndroidDriver androidDriver = (AndroidDriver) driver
                    androidDriver.setConnection(isTurnOn ? new ConnectionState(ConnectionState.AIRPLANE_MODE_MASK)
                            : new ConnectionState(ConnectionState.WIFI_MASK))
                } else {
                    IOSDriver iOSDriver = (IOSDriver) driver
                    Dimension size = driver.manage().window().getSize()

                    String deviceModel = MobileDriverFactory.getDeviceModel()
                    String deviceOSVersion = MobileDriverFactory.getDeviceOSVersion()
                    if (StringUtils.containsIgnoreCase(deviceModel, "simulator")) {
                        logger.logWarning("Toggle Airplane Mode is not available for Simulator")
                        return
                    }
                    
                    logger.logDebug("Device model: " + deviceModel)
                    logger.logDebug("Device version: " + deviceOSVersion)
                    boolean swipeUpToOpenControlCenter = true

                    if ((getMajorVersion(deviceOSVersion) >= 12)
                        && (isIPhoneXOrLater(deviceModel) || isIPad(deviceModel))) {
                        swipeUpToOpenControlCenter = false
                    }

                    /**
                     * https://support.apple.com/en-vn/HT202769#open
                     */
                    if (swipeUpToOpenControlCenter) {
                        logger.logDebug("Swipe up from the bottom middle of the screen")
                        MobileCommonHelper.swipe(driver,
                                (size.getWidth() / 2) as int,
                                size.getHeight(),
                                (size.getWidth() / 2) as int,
                                (size.getHeight() / 2) as int)
                    } else {
                        logger.logDebug("Swipe down from the upper-right corner of the screen")
                        MobileCommonHelper.swipe(driver,
                            size.getWidth(),
                            0,
                            size.getWidth(),
                            (size.getHeight() / 2) as int)
                    }

                    List toggleAirplaneButtonList = iOSDriver.findElementsByXPath("//XCUIElementTypeSwitch[@visible='true' and @label='Airplane Mode']")
                    if (toggleAirplaneButtonList == null || toggleAirplaneButtonList.isEmpty()) {
                        logger.logFailed("Could not find Airplane Mode button at XPATH: //XCUIElementTypeSwitch[@visible='true' and @label='Airplane Mode']")
                        return
                    }
                    MobileElement airplaneButton = toggleAirplaneButtonList.get(0);
                    boolean isEnabled = airplaneButton.getAttribute("value") == "1" ? true : false
                    if (isTurnOn != isEnabled) {
                        TouchAction tapAtAirPlaneButton = new TouchAction(driver)
                                .tap(TapOptions.tapOptions().withElement(ElementOption.element(airplaneButton, 1, 1)))
                        tapAtAirPlaneButton.release().perform()

                        logger.logInfo("Airplane Mode switched from " + getSwitchStatus(isEnabled) + " to " + getSwitchStatus(isTurnOn))
                    } else {
                        logger.logInfo("Airplane Mode already switched to " + getSwitchStatus(isEnabled))
                    }

                    PointOption topScreenPoint = PointOption.point(0, 0)
                    TouchAction touchAtTopScreenAction = new TouchAction(driver)
                            .tap(TapOptions.tapOptions().withPosition(topScreenPoint))
                    touchAtTopScreenAction.release().perform()
                }
                logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_TOGGLE_AIRPLANE_MODE, mode))
            } finally {
                driver.context(context)
            }
        }, flowControl, true, StringConstants.KW_MSG_CANNOT_TOGGLE_AIRPLANE_MODE)
    }

    @CompileStatic
    String getSwitchStatus(boolean status) {
        return status ? "ON" : "OFF"
    }

    @CompileStatic
    int getMajorVersion(String version) {
        return Integer.parseInt(version.split("\\.")[0])
    }

    @CompileStatic
    boolean isIPhoneXOrLater(String deviceModel) {
        if (!deviceModel.contains("iPhone")) {
            return false
        }
        String[] versionNumbers = deviceModel.replace("iPhone", "").split(",")
        int majorVersion = Integer.parseInt(versionNumbers[0])
        int minorVersion = Integer.parseInt(versionNumbers[1])
        
        //https://www.theiphonewiki.com/wiki/Models
        return (majorVersion >= 11 ||
            (majorVersion == 10 && (minorVersion == 3 || minorVersion == 6)))
    }

    @CompileStatic
    boolean isIPad(String deviceModel) {
        return deviceModel.contains("iPad")
    }
}
