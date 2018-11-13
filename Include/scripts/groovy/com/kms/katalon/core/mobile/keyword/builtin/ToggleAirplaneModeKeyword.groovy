package com.kms.katalon.core.mobile.keyword.builtin

import groovy.transform.CompileStatic
import io.appium.java_client.AppiumDriver
import io.appium.java_client.MobileElement
import io.appium.java_client.TouchAction
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.AndroidKeyCode
import io.appium.java_client.android.Connection
import io.appium.java_client.ios.IOSDriver
import io.appium.java_client.remote.HideKeyboardStrategy

import java.text.MessageFormat
import java.util.concurrent.TimeUnit

import org.apache.commons.io.FileUtils
import org.apache.commons.lang.StringUtils
import org.codehaus.groovy.transform.tailrec.VariableReplacedListener.*
import org.openqa.selenium.Dimension
import org.openqa.selenium.OutputType
import org.openqa.selenium.Point
import org.openqa.selenium.ScreenOrientation
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebDriverException
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.touch.TouchActions
import org.openqa.selenium.remote.mobile.RemoteNetworkConnection
import org.openqa.selenium.support.ui.FluentWait

import com.google.common.base.Function
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.helper.KeywordHelper
import com.kms.katalon.core.keyword.BuiltinKeywords
import com.kms.katalon.core.keyword.internal.KeywordExecutor
import com.kms.katalon.core.keyword.internal.KeywordMain
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.mobile.constants.StringConstants
import com.kms.katalon.core.mobile.helper.MobileCommonHelper
import com.kms.katalon.core.mobile.helper.MobileDeviceCommonHelper
import com.kms.katalon.core.mobile.helper.MobileElementCommonHelper
import com.kms.katalon.core.mobile.helper.MobileGestureCommonHelper
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.mobile.keyword.*
import com.kms.katalon.core.mobile.keyword.internal.MobileAbstractKeyword
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.mobile.keyword.internal.MobileKeywordMain

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
                    driver.setConnection(isTurnOn ? Connection.AIRPLANE : Connection.ALL);
                } else {
                    String deviceModel = MobileDriverFactory.getDeviceModel()
                    //ResourceBundle resourceBundle = ResourceBundle.getBundle("resource")
                    //String[] point = resourceBundle.getString(deviceModel).split(";")
                    if(MobileCommonHelper.deviceModels.get(deviceModel) == null){
                        throw new StepFailedException("Device info not found. Please use ideviceinfo -u <udid> to read ProductType of iOS devices")
                    }
                    if(MobileCommonHelper.airPlaneButtonCoords.get(MobileCommonHelper.deviceModels.get(deviceModel)) == null || MobileCommonHelper.airPlaneButtonCoords.get(MobileCommonHelper.deviceModels.get(deviceModel)).equals("")) {
                        throw new StepFailedException("AirplaneMode button coordinator not found.")
                    }

                    String[] point = MobileCommonHelper.airPlaneButtonCoords.get(MobileCommonHelper.deviceModels.get(deviceModel)).split(";")
                    int x = Integer.parseInt(point[0])
                    int y = Integer.parseInt(point[1])
                    Dimension size = driver.manage().window().getSize()
                    MobileCommonHelper.swipe(driver, 50, size.height, 50, size.height - 300)
                    Thread.sleep(500)
                    TouchAction tap = new TouchAction(driver).tap(x, y).release()
                    tap.perform()
                    MobileCommonHelper.swipe(driver, 50, 1, 50, size.height)
                }
                logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_TOGGLE_AIRPLANE_MODE, mode))
            } finally {
                driver.context(context)
            }

        }, flowControl, true, StringConstants.KW_MSG_CANNOT_TOGGLE_AIRPLANE_MODE)
    }
}
