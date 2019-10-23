package com.kms.katalon.core.keyword

import groovy.transform.CompileStatic

import java.text.MessageFormat

import org.apache.commons.lang.ObjectUtils
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.math.NumberUtils

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointCell
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.constants.StringConstants
import com.kms.katalon.core.exception.StepErrorException
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.helper.KeywordHelper
import com.kms.katalon.core.keyword.internal.KeywordExecutor
import com.kms.katalon.core.logging.ErrorCollector
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.logging.model.TestStatus
import com.kms.katalon.core.main.TestCaseMain
import com.kms.katalon.core.main.TestResult
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testcase.TestCaseBinding
import com.kms.katalon.core.testdata.TestData

@CompileStatic
public class BuiltinKeywords {

    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_UTILITIES)
    public static void comment(String message) {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_BUILT_IN, "comment", message)
    }

    /**
     * Verify if two strings match each other, the second string can be a
     * regular expression.
     *
     * @param actualText
     * @param expectedText
     * @param isRegex
     *            whether string2 is regular expression or not, has two values
     *            true, false (default if left empty)
     * @param flowControl
     * @return whether two strings match, has two values true, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_TEXT)
    public static boolean verifyMatch(String actualText, String expectedText, boolean isRegex, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_BUILT_IN, "verifyMatch", actualText, expectedText, isRegex, flowControl)
    }

    /**
     * Verify if two strings match each other, the second string can be a
     * regular expression.
     *
     * @param actualText
     * @param expectedText
     * @param isRegex
     *            whether string2 is regular expression or not, has two values
     *            true, false (default if left empty)
     * @return whether two strings match, has two values true, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_TEXT)
    public static boolean verifyMatch(String actualText, String expectedText, boolean isRegex) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_BUILT_IN, "verifyMatch", actualText, expectedText, isRegex)
    }

    /**
     * Verify if two strings do not match each other, the second string can be a
     * regular expression.
     *
     * @param actualText
     * @param expectedText
     * @param isRegex
     *            whether string2 is regular expression or not, has two values
     *            true, false (default if left empty)
     * @param flowControl
     * @return whether two strings do not match, has two values true, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_TEXT)
    public static boolean verifyNotMatch(String actualText, String expectedText, boolean isRegex, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_BUILT_IN, "verifyNotMatch", actualText, expectedText, isRegex, flowControl)
    }

    /**
     * Verify if two strings do not match each other, the second string can be a
     * regular expression.
     *
     * @param actualText
     * @param expectedText
     * @param isRegex
     *            whether string2 is regular expression or not, has two values
     *            true, false (default if left empty)
     * @return whether two strings do not match, has two values true, false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_TEXT)
    public static boolean verifyNotMatch(String actualText, String expectedText, boolean isRegex) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_BUILT_IN, "verifyNotMatch", actualText, expectedText, isRegex)
    }

    /**
     * Verify if two objects are equal.
     *
     * @param actualObject
     * @param expectedObject
     * @param flowControl
     * @return true if actual object is equals with expected object; otherwise, false
     * @throws StepFailedException
     * @throws StepErrorException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_NUMBER)
    public static boolean verifyEqual(Object actualObject, Object expectedObject, FailureHandling flowControl) throws StepFailedException, StepErrorException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_BUILT_IN, "verifyEqual", actualObject, expectedObject, flowControl)
    }

    /**
     * Verify if two objects are equal.
     *
     * @param actualObject
     * @param expectedObject
     * @return true if actual object is equals with expected object; otherwise, false
     * @throws StepFailedException
     * @throws StepErrorException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_NUMBER)
    public static boolean verifyEqual(Object actualObject, Object expectedObject) throws StepFailedException, StepErrorException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_BUILT_IN, "verifyEqual", actualObject, expectedObject)
    }

    /**
     * Verify if two objects are not equal.
     *
     * @param actualNumber
     * @param expectedNumber
     * @param flowControl
     * @return true if actual object is not equals with expected object; otherwise, false
     * @throws StepFailedException
     * @throws StepErrorException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_NUMBER)
    public static boolean verifyNotEqual(Object actualObject, Object expectedObject, FailureHandling flowControl) throws StepFailedException, StepErrorException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_BUILT_IN, "verifyNotEqual", actualObject, expectedObject, flowControl)
    }

    /**
     * Verify if two objects are not equal.
     *
     * @param actualNumber
     * @param expectedNumber
     * @return true if actual object is not equals with expected object; otherwise, false
     * @throws StepFailedException
     * @throws StepErrorException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_NUMBER)
    public static boolean verifyNotEqual(Object actualObject, Object expectedObject) throws StepFailedException, StepErrorException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_BUILT_IN, "verifyNotEqual", actualObject, expectedObject)
    }

    /**
     * Verify if the actual number is greater than the expected number
     * @param actualNumber
     * @param expectedNumber
     * @param flowControl
     * @return
     *      true the actual number is greater than the expected number; otherwise false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_NUMBER)
    public static boolean verifyGreaterThan(Object actualNumber, Object expectedNumber, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_BUILT_IN, "verifyGreaterThan", actualNumber, expectedNumber, flowControl)
    }

    /**
     * Verify if the actual number is greater than the expected number
     * @param actualNumber
     * @param expectedNumber
     * @return
     *      true the actual number is greater than the expected number; otherwise false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_NUMBER)
    public static boolean verifyGreaterThan(Object actualNumber, Object expectedNumber) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_BUILT_IN, "verifyGreaterThan", actualNumber, expectedNumber)
    }

    /**
     * Verify if the actual number is greater than or equal with the expected number
     * @param actualNumber
     * @param expectedNumber
     * @param flowControl
     * @return
     *      true the actual number is greater than or equal with the expected number ; otherwise false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_NUMBER)
    public static boolean verifyGreaterThanOrEqual(Object actualNumber, Object expectedNumber, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_BUILT_IN, "verifyGreaterThanOrEqual", actualNumber, expectedNumber, flowControl)
    }

    /**
     * Verify if the actual number is greater than or equal with the expected number
     * @param actualNumber
     * @param expectedNumber
     * @return
     *      true the actual number is greater than or equal with the expected number ; otherwise false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_NUMBER)
    public static boolean verifyGreaterThanOrEqual(Object actualNumber, Object expectedNumber) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_BUILT_IN, "verifyGreaterThanOrEqual", actualNumber, expectedNumber)
    }

    /**
     * Verify if the actual number is less than the expected number
     * @param actualNumber
     * @param expectedNumber
     * @param flowControl
     * @return
     *      true the actual number is less than the expected number; otherwise false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_NUMBER)
    public static boolean verifyLessThan(Object actualNumber, Object expectedNumber, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_BUILT_IN, "verifyLessThan", actualNumber, expectedNumber, flowControl)
    }

    /**
     * Verify if the actual number is less than the expected number
     * @param actualNumber
     * @param expectedNumber
     * @return
     *      true the actual number is less than the expected number; otherwise false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_NUMBER)
    public static boolean verifyLessThan(Object actualNumber, Object expectedNumber) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_BUILT_IN, "verifyLessThan", actualNumber, expectedNumber)
    }

    /**
     * Verify if the actual number is less than or equal with the expected number
     * @param actualNumber
     * @param expectedNumber
     * @param flowControl
     * @return
     *      true the actual number is less than or equal with the expected number; otherwise false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_NUMBER)
    public static boolean verifyLessThanOrEqual(Object actualNumber, Object expectedNumber, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_BUILT_IN, "verifyLessThanOrEqual", actualNumber, expectedNumber, flowControl)
    }

    /**
     * Verify if the actual number is less than or equal with the expected number
     * @param actualNumber
     * @param expectedNumber
     * @return
     *      true the actual number is less than or equal with the expected number; otherwise false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_NUMBER)
    public static boolean verifyLessThanOrEqual(Object actualNumber, Object expectedNumber) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_BUILT_IN, "verifyLessThanOrEqual", actualNumber, expectedNumber)
    }

    /**
     * Concatenate string array into a single string
     * @param strings
     *      a string array
     * @param flowControl
     * @return
     *      the result string
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_TEXT)
    public static String concatenate(String[] strings, FailureHandling flowControl) throws StepFailedException {
        return (String) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_BUILT_IN, "concatenate", strings, flowControl)
    }

    /**
     * Concatenate string array into a single string
     * @param strings
     *      a string array
     * @return
     *      the result string
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_TEXT)
    public static String concatenate(String[] strings) throws StepFailedException {
        return (String) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_BUILT_IN, "concatenate", strings)
    }

    /**
     * Call and execute another test case
     * @param calledTestCase
     *      represents a test case
     * @param binding
     *      contains variables bindings for the called test case. 
     *      If the <code>binding<code> parameter is null, default values of all variables are used.
     * @param flowControl
     * @return returned value of called test case
     * @throws Exception
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_UTILITIES)
    public static Object callTestCase(TestCase calledTestCase, Map binding, FailureHandling flowControl) throws Exception {
        return (Object)KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_BUILT_IN, "callTestCase", calledTestCase, binding, flowControl)
    }

    /**
     * Call and execute another test case
     * @param calledTestCase
     *      represents a test case
     * @param binding
     *      contains variables bindings for the called test case. 
     *      If the <code>binding<code> parameter is null, default values of all variables are used.
     * @return returned value of called test case      
     * @throws Exception
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_UTILITIES)
    public static Object callTestCase(TestCase calledTestCase, Map binding) throws Exception {
        return (Object)KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_BUILT_IN, "callTestCase", calledTestCase, binding)
    }

    /**
     * Delay execution for a specific time (in seconds)
     * @param second
     *      number of seconds to delay. Eg: 0.5, 10, 30.
     * @param flowControl
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_UTILITIES)
    public static void delay(Number second, FailureHandling flowControl) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_BUILT_IN, "delay", second, flowControl)
    }

    /**
     * Delay execution for a specific time (in seconds)
     * @param second
     *      number of seconds to delay. Eg: 0.5, 10, 30.
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_UTILITIES)
    public static void delay(Number second) throws StepFailedException {
        KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_BUILT_IN, "delay", second)
    }

    /**
     * Verify Checkpoint
     * 
     * @param checkpoint Checkpoint
     * @param logChangedValues <code>true</code> will log all the changed value between checkpoint data and the source. <code>false</code> will not log any changed value.
     * @param flowControl failure handling
     * @return <code>true</code> if checked data of checkpoint matches their source data. Otherwise, <code>false</code>.
     * @throws StepFailedException if data does not match
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_UTILITIES)
    public static boolean verifyCheckpoint(Checkpoint checkpoint, boolean logChangedValues, FailureHandling flowControl) throws StepFailedException {
        return KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_BUILT_IN, "verifyCheckpoint", checkpoint, logChangedValues, flowControl)
    }

    /**
     * Verify Checkpoint
     * 
     * @param checkpoint Checkpoint
     * @param logChangedValues <code>true</code> will log all the changed value between checkpoint data and the source. <code>false</code> will not log any changed value.
     * @return <code>true</code> if checked data of checkpoint matches their source data. Otherwise, <code>false</code>.
     * @throws StepFailedException if data does not match
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_UTILITIES)
    public static boolean verifyCheckpoint(Checkpoint checkpoint, boolean logChangedValues) throws StepFailedException {
        return KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_BUILT_IN, "verifyCheckpoint", checkpoint, logChangedValues)
    }
}
