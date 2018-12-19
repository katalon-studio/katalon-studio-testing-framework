package com.kms.katalon.core.webui.keyword.builtin

import groovy.transform.CompileStatic

import java.text.MessageFormat
import java.util.concurrent.TimeUnit

import org.apache.commons.io.FileUtils
import org.openqa.selenium.Alert
import org.openqa.selenium.By
import org.openqa.selenium.Dimension
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.NoSuchWindowException
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebDriverException
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.FluentWait
import org.openqa.selenium.support.ui.Select
import org.openqa.selenium.support.ui.Wait
import org.openqa.selenium.support.ui.WebDriverWait

import com.google.common.base.Function
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.BuiltinKeywords
import com.kms.katalon.core.keyword.internal.KeywordExecutor
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.util.internal.ExceptionsUtil
import com.kms.katalon.core.util.internal.PathUtil
import com.kms.katalon.core.webui.common.ScreenUtil
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.constants.StringConstants
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.driver.WebUIDriverType
import com.kms.katalon.core.webui.exception.BrowserNotOpenedException
import com.kms.katalon.core.webui.exception.WebElementNotFoundException
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain
import com.kms.katalon.core.webui.util.FileUtil

@Action(value = "modifyObjectProperty")
public class ModifyObjectPropertyKeyword extends WebUIAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        TestObject testObject = getTestObject(params[0])
        String propertyName = (String) params[1]
        String matchCondition = (String) params[2]
        String modifyValue = (String) params[3]
        boolean isActive = (boolean) params[4]
        FailureHandling flowControl = (FailureHandling)(params.length > 5 && params[5] instanceof FailureHandling ? params[5] : RunConfiguration.getDefaultFailureHandling())
        return modifyObjectProperty(testObject,propertyName,matchCondition,modifyValue,isActive,flowControl)
    }

    @CompileStatic
    public TestObject modifyObjectProperty(TestObject testObject, String propertyName, String matchCondition,
            String modifyValue, boolean isActive, FailureHandling flowControl) {
        Object result = WebUIKeywordMain.runKeyword({
            logger.logDebug(StringConstants.KW_LOF_INFO_CHK_TO)
            if (testObject == null) {
                throw new IllegalArgumentException(StringConstants.KW_EXC_TO_IS_NULL)
            }
            logger.logDebug(StringConstants.KW_LOG_INFO_CHK_PROP_NAME)
            if (propertyName == null) {
                throw new IllegalArgumentException(StringConstants.KW_EXC_PROP_NAME_IS_NULL)
            }
            TestObjectProperty property = testObject.findProperty(propertyName)
            if (property == null) {
                logger.logDebug(MessageFormat.format(StringConstants.KW_LOG_INFO_NOT_FOUND_PROP_CREATING_NEW_PROP, propertyName, testObject.getObjectId(), propertyName))
                property = new TestObjectProperty(propertyName, ConditionType.EQUALS, null, false)
                testObject.addProperty(property)
            }
            logger.logDebug(StringConstants.KW_LOG_INFO_CHK_MODIFY_VAL)
            if (modifyValue == null) {
                logger.logDebug(StringConstants.KW_LOG_INFO_MODIFY_VAL_NULL_SO_NOT_MODIFYING_VAL)
            } else {
                property.setValue(modifyValue)
            }

            logger.logDebug(StringConstants.KW_LOG_INFO_CHK_MATCH_COND)
            if (matchCondition == null) {
                logger.logDebug(StringConstants.KW_LOG_INFO_MATCH_COND_NULL_SO_NOT_MODIFYING_MATCH_COND)
            } else {
                ConditionType conditionType = ConditionType.fromValue(matchCondition)
                if (conditionType == null) {
                    StringBuilder conditionList = new StringBuilder()
                    boolean isFirst = true
                    for (ConditionType condition : ConditionType.values()) {
                        if (!isFirst) {
                            conditionList.append(", ")
                        }
                        conditionList.append("'")
                        conditionList.append(condition.toString())
                        conditionList.append("'")
                        isFirst = false
                    }
                    logger.logDebug(MessageFormat.format(StringConstants.KW_LOG_INFO_INVALID_MATCH_COND, conditionList.toString()))
                } else {
                    property.setCondition(conditionType)
                }
            }
            property.setActive(isActive)
            logger.logPassed(StringConstants.KW_MSG_MODIFY_OBJ_PROP_SUCESSFULLY)
            return testObject
        }, flowControl, true, StringConstants.KW_MSG_CANNOT_MODIFY_OBJ_PROP)
        if (result instanceof TestObject) {
            return (TestObject) result
        }
        return null
    }
}
