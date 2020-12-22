package com.kms.katalon.core.webservice.keyword;

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.exception.StepErrorException
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.BuiltinKeywords
import com.kms.katalon.core.keyword.internal.KeywordExecutor
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.webservice.constants.StringConstants

import groovy.transform.CompileStatic

@CompileStatic
public class WSBuiltInKeywords extends BuiltinKeywords {

    /**
     * Send a HTTP Request to web server
     * @param request the object represents for a HTTP Request, user need to define it from Object Repository->New->Web Service Request, and get it by ObjectRepository.findRequestObject("requestObjectId")
     * @param flowControl
     * @return
     * @throws Exception
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_REQUEST)
    public static ResponseObject sendRequest(RequestObject request, FailureHandling flowControl) throws Exception {
        return (ResponseObject) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB_SERVICE, "sendRequest", request, flowControl)
    }

    /**
     * Send a HTTP Request to web server
     * @param request the object represents for a HTTP Request, user need to define it from Object Repository->New->Web Service Request, and get it by ObjectRepository.findRequestObject("requestObjectId")
     * @return
     * @throws Exception
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_REQUEST)
    public static ResponseObject sendRequest(RequestObject request) throws Exception {
        return (ResponseObject) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB_SERVICE, "sendRequest", request)
    }

    /**
     * Send a HTTP Request to web server and verify the response
     * @param request the object represents for a HTTP Request, user need to define it from Object Repository->New->Web Service Request, and get it by ObjectRepository.findRequestObject("requestObjectId")
     * @param flowControl
     * @return
     * @throws Exception
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_REQUEST)
    public static ResponseObject sendRequestAndVerify(RequestObject request, FailureHandling flowControl) throws Exception {
        return (ResponseObject) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB_SERVICE, "sendRequestAndVerify", request, flowControl)
    }
    
    /**
     * Send a HTTP Request to web server and verify the response
     * @param request the object represents for a HTTP Request, user need to define it from Object Repository->New->Web Service Request, and get it by ObjectRepository.findRequestObject("requestObjectId")
     * @return
     * @throws Exception
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_REQUEST)
    public static ResponseObject sendRequestAndVerify(RequestObject request) throws Exception {
        return (ResponseObject) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB_SERVICE, "sendRequestAndVerify", request)
    }
    
    /**
     * Get the number of expected elements (JSON/XML) in the response (output) of a web service call
     * @param response the object represents for a HTTP Response, user can get responded content type, data, header properties (sometimes user may want to get cookie from response header)
     * @param locator an expression Katalon will use to go through and look for expected element(s), please refer to our user guide for how to write it
     * @param flowControl
     * @return the number of expected elements
     * @throws StepErrorException
     * @throws StepFailedException
     * 
     * @since 7.2.0
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static int getElementsCount(ResponseObject response, String locator, FailureHandling flowControl) throws StepErrorException, StepFailedException {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB_SERVICE, "getElementsCount", response, locator, flowControl)
    }
    
    /**
     * Get the number of expected elements (JSON/XML) in the response (output) of a web service call
     * @param response the object represents for a HTTP Response, user can get responded content type, data, header properties (sometimes user may want to get cookie from response header)
     * @param locator an expression Katalon will use to go through and look for expected element(s), please refer to our user guide for how to write it
     * @return the number of expected elements
     * @throws StepErrorException
     * @throws StepFailedException
     * 
     * @since 7.2.0
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static int getElementsCount(ResponseObject response, String locator) throws StepErrorException, StepFailedException {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB_SERVICE, "getElementsCount", response, locator)
    }
    
    /**
     * Verify number of expected elements (JSON/XML) in the response (output) of a web service call 
     * @param response the object represents for a HTTP Response, user can get responded content type, data, header properties (sometimes user may want to get cookie from response header)
     * @param locator an expression Katalon will use to go through and look for expected element(s), please refer to our user guide for how to write it   
     * @param count the expected number of element(s) should appear in the responded data (usually is JSON/XML)
     * @param flowControl
     * @return true if your expectation is met, otherwise false 
     * @throws Exception
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementsCount(ResponseObject response, String locator, int count, FailureHandling flowControl) throws Exception {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB_SERVICE, "verifyElementsCount", response, locator, count, flowControl)
    }

    /**
     * Verify number of expected elements (JSON/XML) in the response (output) of a web service call
     * @param response the object represents for a HTTP Response, user can get responded content type, data, header properties (sometimes user may want to get cookie from response header)
     * @param locator an expression Katalon will use to go through and look for expected element(s), please refer to our user guide for how to write it
     * @param count the expected number of element(s) should appear in the responded data (usually is JSON/XML)
     * @return true if your expectation is met, otherwise false
     * @throws Exception
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementsCount(ResponseObject response, String locator, int count) throws Exception {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB_SERVICE, "verifyElementsCount", response, locator, count)
    }
    
    /**
     * Get property value of an element (defined by a locator) in the returned data from a web service call
     * @param response the object represents for a HTTP Response, user can get responded content type, data, header properties (sometimes user may want to get cookie from response header)
     * @param locator an expression Katalon will use to go through and look for expected element(s), please refer to our user guide for how to write it
     * @param flowControl
     * @return  property value of the expected element
     * @throws StepErrorException
     * @throws StepFailedException
     * 
     * @since 7.2.0
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static Object getElementPropertyValue(ResponseObject response, String locator, FailureHandling flowControl) throws StepErrorException, StepFailedException {
        return KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB_SERVICE, "getElementPropertyValue", response, locator, flowControl)
    }
    
    /**
     * Get property value of an element (defined by a locator) in the returned data from a web service call
     * @param response the object represents for a HTTP Response, user can get responded content type, data, header properties (sometimes user may want to get cookie from response header)
     * @param locator an expression Katalon will use to go through and look for expected element(s), please refer to our user guide for how to write it
     * @return property value of the expected element
     * @throws StepErrorException
     * @throws StepFailedException
     * 
     * @since 7.2.0
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static Object getElementPropertyValue(ResponseObject response, String locator) throws StepErrorException, StepFailedException {
        return KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB_SERVICE, "getElementPropertyValue", response, locator)
    }

    /**
     * Verify that there is an element with expected property value appear in the returned data from a web service call
     * @param response the object represents for a HTTP Response, user can get responded content type, data, header properties (sometimes user may want to get cookie from response header)
     * @param locator an expression Katalon will use to go through and look for expected element(s), please refer to our user guide for how to write it
     * @param value the expected value of element you want to verify in the responded data (usually is JSON/XML)
     * @param flowControl
     * @return true if your expectation is met, otherwise false 
     * @throws StepErrorException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementPropertyValue(ResponseObject response, String locator, Object value, FailureHandling flowControl) throws StepErrorException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB_SERVICE, "verifyElementPropertyValue", response, locator, value, flowControl)
    }

    /**
     * Verify that there is an element with expected property value appear in the returned data from a web service call
     * @param response the object represents for a HTTP Response, user can get responded content type, data, header properties (sometimes user may want to get cookie from response header)
     * @param locator an expression Katalon will use to go through and look for expected element(s), please refer to our user guide for how to write it
     * @param value the expected value of element you want to verify in the responded data (usually is JSON/XML)
     * @return true if your expectation is met, otherwise false
     * @throws StepErrorException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementPropertyValue(ResponseObject response, String locator, Object value) throws StepErrorException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB_SERVICE, "verifyElementPropertyValue", response, locator, value)
    }

    /**
     * Get text of the element (defined by a locator) which appears in the returned data from a web service call
     * @param response the object represents for a HTTP Response, user can get responded content type, data, header properties (sometimes user may want to get cookie from response header)
     * @param locator an expression Katalon will use to go through and look for expected element(s), please refer to our user guide for how to write it
     * @param flowControl
     * @return text of the expected element
     * @throws StepErrorException
     * @throws StepFailedException
     * 
     * @since 7.2.0
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static String getElementText(ResponseObject response, String locator, FailureHandling flowControl) throws StepErrorException, StepFailedException {
        return (String) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB_SERVICE, "getElementText", response, locator, flowControl)
    }
    
    /**
     * Get text of the element (defined by a locator) which appears in the returned data from a web service call
     * @param response the object represents for a HTTP Response, user can get responded content type, data, header properties (sometimes user may want to get cookie from response header)
     * @param locator an expression Katalon will use to go through and look for expected element(s), please refer to our user guide for how to write it
     * @return text of the expected element
     * @throws StepErrorException
     * @throws StepFailedException
     * 
     * @since 7.2.0
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static String getElementText(ResponseObject response, String locator) throws StepErrorException, StepFailedException {
        return (String) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB_SERVICE, "getElementText", response, locator)
    }
    
    /**
     * Verify that there is an element with expected text appear in the returned data from a web service call
     * @param response the object represents for a HTTP Response, user can get responded content type, data, header properties (sometimes user may want to get cookie from response header)
     * @param locator an expression Katalon will use to go through and look for expected element(s), please refer to our user guide for how to write it
     * @param text the expected text of element you want to verify in the responded data (usually is JSON/XML)
     * @param flowControl
     * @return true if your element text is found, otherwise false
     * @throws StepFailedException
     * @throws StepErrorException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementText(ResponseObject response, String locator, String text, FailureHandling flowControl) throws StepFailedException, StepErrorException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB_SERVICE, "verifyElementText", response, locator, text, flowControl)
    }

    /**
     * Verify that there is an element with expected text appear in the returned data from a web service call
     * @param response the object represents for a HTTP Response, user can get responded content type, data, header properties (sometimes user may want to get cookie from response header)
     * @param locator an expression Katalon will use to go through and look for expected element(s), please refer to our user guide for how to write it
     * @param text the expected text of element you want to verify in the responded data (usually is JSON/XML)
     * @return true if your element text is found, otherwise false
     * @throws StepFailedException
     * @throws StepErrorException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyElementText(ResponseObject response, String locator, String text) throws StepFailedException, StepErrorException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB_SERVICE, "verifyElementText", response, locator, text)
    }

    /**
     * Verify that an expected text appear in the returned data from a web service call
     * @param response the object represents for a HTTP Response, user can get responded content type, data, header properties (sometimes user may want to get cookie from response header)
     * @param string the text you want to look for
     * @param useRegex use regular expression or not
     * @param flowControl
     * @return true if your text is found, otherwise false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_TEXT)
    public static boolean containsString(ResponseObject response, String string, boolean useRegex, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB_SERVICE, "containsString", response, string, useRegex, flowControl)
    }

    /**
     * Verify that an expected text appear in the returned data from a web service call
     * @param response the object represents for a HTTP Response, user can get responded content type, data, header properties (sometimes user may want to get cookie from response header)
     * @param string the text you want to look for
     * @param useRegex use regular expression or not
     * @return true if your text is found, otherwise false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_TEXT)
    public static boolean containsString(ResponseObject response, String string, boolean useRegex) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB_SERVICE, "containsString", response, string, useRegex)
    }

    /**
     * Get status code in the returned data from a web service call
     *
     * @param responseObject the object represents for a HTTP Response, user can get responded content type, data, header properties (sometimes user may want to get cookie from response header)
     * @param flowControl
     * @return the response status code
     * @throws StepErrorException
     * @throws StepFailedException
     * 
     * @since 7.2.0
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static int getResponseStatusCode(ResponseObject responseObject, FailureHandling flowControl) throws StepErrorException, StepFailedException {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB_SERVICE, "getResponseStatusCode", responseObject, flowControl)
    }
    
    /**
     * Get status code in the returned data from a web service call
     *
     * @param responseObject the object represents for a HTTP Response, user can get responded content type, data, header properties (sometimes user may want to get cookie from response header)
     * @return the response status code
     * @throws StepErrorException
     * @throws StepFailedException
     * 
     * @since 7.2.0
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static int getResponseStatusCode(ResponseObject responseObject) throws StepErrorException, StepFailedException {
        return (int) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB_SERVICE, "getResponseStatusCode", responseObject)
    }
    
    /**
     * Verify status code in the returned data from a web service call
     * 
     * @param responseObject the object represents for a HTTP Response, user can get responded content type, data, header properties (sometimes user may want to get cookie from response header)
     * @param expectedStatusCode the expected status code
     * @param flowControl FailureHandling
     * @return true if the response status code is the same as the expected status code, otherwise false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyResponseStatusCode(ResponseObject responseObject, int expectedStatusCode, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB_SERVICE, "verifyResponseStatusCode", responseObject, expectedStatusCode, flowControl)
    }

    /**
     * Verify status code in the returned data from a web service call
     *
     * @param responseObject the object represents for a HTTP Response, user can get responded content type, data, header properties (sometimes user may want to get cookie from response header)
     * @param expectedStatusCode the expected status code
     * @return true if the response status code is the same as the expected status code, otherwise false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyResponseStatusCode(ResponseObject responseObject, int expectedStatusCode) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB_SERVICE, "verifyResponseStatusCode", responseObject, expectedStatusCode)
    }

    /**
     * Verify status code valid in a range of status codes in the returned data from a web service call
     * 
     * @param responseObject the object represents for a HTTP Response, user can get responded content type, data, header properties (sometimes user may want to get cookie from response header)
     * @param fromStatusCode from status code
     * @param toStatusCode to status code
     * @param flowControl FailureHandling
     * @return true if the response status code is in a range of the expected status code, otherwise false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyResponseStatusCodeInRange(ResponseObject responseObject, int fromStatusCode, int toStatusCode, FailureHandling flowControl) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB_SERVICE, "verifyResponseStatusCodeInRange", responseObject, fromStatusCode, toStatusCode, flowControl)
    }

    /**
     * Verify status code valid in a range of status codes in the returned data from a web service call
     *
     * @param responseObject the object represents for a HTTP Response, user can get responded content type, data, header properties (sometimes user may want to get cookie from response header)
     * @param fromStatusCode from status code
     * @param toStatusCode to status code
     * @return true if the response status code is in a range of the expected status code, otherwise false
     * @throws StepFailedException
     */
    @CompileStatic
    @Keyword(keywordObject = StringConstants.KW_CATEGORIZE_ELEMENT)
    public static boolean verifyResponseStatusCodeInRange(ResponseObject responseObject, int fromStatusCode, int toStatusCode) throws StepFailedException {
        return (boolean) KeywordExecutor.executeKeywordForPlatform(KeywordExecutor.PLATFORM_WEB_SERVICE, "verifyResponseStatusCodeInRange", responseObject, fromStatusCode, toStatusCode)
    }
}
