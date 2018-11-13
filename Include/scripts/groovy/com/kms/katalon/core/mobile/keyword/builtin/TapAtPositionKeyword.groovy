package com.kms.katalon.core.mobile.keyword.builtin

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
import com.kms.katalon.core.mobile.keyword.internal.MobileKeywordMain

@Action(value = "tapAtPosition")
public class TapAtPositionKeyword extends MobileAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        Number x = (Number) params[0]
        Number y = (Number) params[1]
        FailureHandling flowControl = (FailureHandling)(params.length > 2 && params[2] instanceof FailureHandling ? params[2] : RunConfiguration.getDefaultFailureHandling())
        tapAtPosition(x,y,flowControl)
    }

    @CompileStatic
    public void tapAtPosition(Number x, Number y, FailureHandling flowControl) throws StepFailedException {
        MobileKeywordMain.runKeyword({
            MobileElementCommonHelper.tapAtPosition(x, y)
        }, flowControl, true, (x != null && y != null) ?
        MessageFormat.format(StringConstants.KW_LOG_FAILED_TAPPED_AT_X_Y, [x, y] as Object[]) : StringConstants.KW_LOG_FAILED_TAPPED_AT_POSITION )
    }
}
