package com.kms.katalon.core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum for flow control for test execution
 * <p>
 * - <b>STOP_ON_FAILURE</b>: stop execution when errors or fail cases happened
 * <p>
 * - <b>CONTINUE_ON_FAILURE</b>: continue execution when errors or fail cases happened
 * <p>
 * - <b>OPTIONAL</b>: continue execution when errors or fail cases happened and not marking tests as failed
 */
public enum FailureHandling {
    STOP_ON_FAILURE,	// Default
    CONTINUE_ON_FAILURE,
    OPTIONAL;

    public static String[] valueStrings() {
        List<String> valueStrings = new ArrayList<String>();
        for (FailureHandling failure : values()) {
            valueStrings.add(failure.name());
        }
        return valueStrings.toArray(new String[0]);
    }
}
