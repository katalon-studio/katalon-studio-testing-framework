package com.kms.katalon.core.webui.keyword.builtin

import groovy.transform.CompileStatic

import java.text.MessageFormat
import java.util.AbstractMap
import java.util.ArrayList
import java.util.List
import java.util.AbstractMap.SimpleEntry
import java.util.Map.Entry
import java.util.regex.Matcher
import java.util.regex.Pattern

import org.apache.commons.lang.math.NumberUtils
import org.apache.commons.lang3.StringUtils
import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.constants.StringConstants
import com.kms.katalon.core.exception.StepErrorException
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.helper.KeywordHelper
import com.kms.katalon.core.keyword.internal.AbstractKeyword
import com.kms.katalon.core.keyword.internal.KeywordExecutor
import com.kms.katalon.core.keyword.internal.KeywordMain
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.logging.ErrorCollector
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.logging.model.TestStatus
import com.kms.katalon.core.main.TestCaseMain
import com.kms.katalon.core.main.TestResult
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testcase.TestCaseBinding
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.SelectorMethod
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.TestObjectBuilder
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.webui.common.XPathBuilder
import com.kms.katalon.core.configuration.RunConfiguration

@Action(value = "convertWebElementToTestObject")
public class ConvertWebElementToTestObjectKeyword extends AbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return SupportLevel.BUITIN
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        WebElement webElement = (WebElement) params[0]
        FailureHandling flowControl = (FailureHandling)(params.length > 1 && params[1] instanceof FailureHandling ? params[1] : RunConfiguration.getDefaultFailureHandling())
        return convertWebElementToTestObject(webElement, flowControl)
    }

    @CompileStatic
    public Object convertWebElementToTestObject(WebElement webElement, FailureHandling flowControl) throws StepFailedException {
        return KeywordMain.runKeyword({
            logger.logDebug(StringConstants.KW_LOG_INFO_CONVERT_WEB_ELEMENT_TO_TEST_OBJECT);
            TestObject testObject = new TestObject();
            testObject.setCachedWebElement(webElement);
            return testObject;
        }, flowControl, StringConstants.KW_LOG_INFO_FAIL_TO_CONVERT_WEB_ELEMENT_TO_TEST_OBJECT)
    }
}
