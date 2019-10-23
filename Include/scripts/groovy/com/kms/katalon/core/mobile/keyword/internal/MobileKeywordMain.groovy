package com.kms.katalon.core.mobile.keyword.internal;

import groovy.lang.Closure
import groovy.transform.CompileStatic

import org.openqa.selenium.TimeoutException

import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.KeywordMain
import com.kms.katalon.core.mobile.helper.MobileScreenCaptor
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.util.internal.ExceptionsUtil

public class MobileKeywordMain {

    private static final String TIMED_OUT_WAITING_FOR_PAGE_LOAD = "Timed out waiting for page load."
	
	@CompileStatic
    public static runKeyword(Closure closure, FailureHandling flowControl, boolean takeScreenShot, String errorMessage) {
        try {
            return closure.call();
        } catch (Throwable e) {
            stepFailed(errorMessage, flowControl, e, takeScreenShot);
        }
    }
    
    @CompileStatic
    public static int runKeywordAndReturnInt(Closure closure, FailureHandling flowControl, boolean takeScreenShot, String errorMessage) {
        try {
            return (int) closure.call();
        } catch (Throwable e) {
            stepFailed(errorMessage, flowControl, e, takeScreenShot);
        }
        return -1;
    }

    @CompileStatic
    public static stepFailed(String message, FailureHandling flHandling, Throwable e, boolean takeScreenShot)
            throws StepFailedException {
        KeywordMain.stepFailed(message, flHandling, e, new MobileScreenCaptor().takeScreenshotAndGetAttributes(takeScreenShot));
    }
           
    @CompileStatic
    private static boolean isPageLoadTimeoutException(Throwable e) {
        return (e instanceof TimeoutException) && (e.getMessage().startsWith(TIMED_OUT_WAITING_FOR_PAGE_LOAD));
    }
}
