package com.kms.katalon.core.util;

import com.kms.katalon.core.model.RunningMode;

public class ApplicationRunningMode {

    private static RunningMode get;
   
    //This should be called only once in application startup
    public static void setRunningMode(RunningMode runningMode) {
        ApplicationRunningMode.get = runningMode;
    }
    
    public static RunningMode get() {
        return get;
    }
}
