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

@Action(value = "waitForElementNotHasAttribute")
public class WaitForElementNotHasAttributeKeyword extends MobileAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        TestObject to = getTestObject(params[0])
        String attributeName = (String) params[1]
        int timeout = (int) params[2]
        FailureHandling flowControl = (FailureHandling)(params.length > 3 && params[3] instanceof FailureHandling ? params[3] : RunConfiguration.getDefaultFailureHandling())
        return waitForElementNotHasAttribute(to, attributeName, timeout, flowControl)
    }

    @CompileStatic
    public boolean waitForElementNotHasAttribute(TestObject to, String attributeName, int timeout, FailureHandling flowControl) {
        MobileKeywordMain.runKeyword({
            try {
                KeywordHelper.checkTestObjectParameter(to)
                logger.logDebug(StringConstants.COMM_LOG_INFO_CHECKING_ATTRIBUTE_NAME)
                if (attributeName == null) {
                    throw new IllegalArgumentException(StringConstants.COMM_EXC_ATTRIBUTE_NAME_IS_NULL)
                }
                timeout = KeywordHelper.checkTimeout(timeout)
                WebElement foundElement = findElement(to, timeout)
                if (foundElement == null) {
                    logger.logWarning(MessageFormat.format(StringConstants.KW_LOG_FAILED_ELEMENT_X_EXISTED, to.getObjectId()))
                    return false
                }
                Boolean notHasAttribute = new FluentWait<WebElement>(foundElement)
                        .pollingEvery(500, TimeUnit.MILLISECONDS).withTimeout(timeout, TimeUnit.SECONDS)
                        .until(new Function<WebElement, Boolean>() {
                            @Override
                            public Boolean apply(WebElement element) {
                                return MobileCommonHelper.getAttributeValue(foundElement, attributeName) == null
                            }
                        })
                if (notHasAttribute) {
                    logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_FAILED_OBJ_X_HAS_ATTRIBUTE_Y, [to.getObjectId(), attributeName] as Object[]))
                    return true
                }
            } catch (TimeoutException e) {
                logger.logWarning(MessageFormat.format(StringConstants.KW_LOG_PASSED_OBJ_X_HAS_ATTRIBUTE_Y, [to.getObjectId(), attributeName] as Object[]), null, e)
            }
            return false
        }, flowControl, true, (to != null) ? MessageFormat.format(StringConstants.KW_MSG_CANNOT_WAIT_OBJ_X_NOT_HAS_ATTRIBUTE_Y, [to.getObjectId(), attributeName] as Object[]) : StringConstants.KW_MSG_CANNOT_WAIT_OBJ_NOT_HAS_ATTRIBUTE)
    }
}
