package com.kms.katalon.core.util;

import com.kms.katalon.core.model.RunningMode;

public class ApplicationRunningMode {

    private static RunningMode runningMode;
   
    //This should be called only once in application startup
    public static void set(RunningMode runningMode) {
        ApplicationRunningMode.runningMode = runningMode;
    }
    
    public static RunningMode get() {
        return runningMode;
    }
}
