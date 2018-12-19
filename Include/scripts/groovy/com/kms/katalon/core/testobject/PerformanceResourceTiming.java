package com.kms.katalon.core.testobject;

public interface PerformanceResourceTiming {
    /**
     * The total time that starts from the request is sent until Katalon receives the last byte of the response.
     * 
     * @since 5.4
     */
    long getElapsedTime();

    /**
     * The amount of time that starts from the request is sent until the fist byte comes. Also know as TTFB (Time to
     * first byte).
     * 
     * @since 5.4
     */
    long getWaitingTime();

    /**
     * The amount of time that starts from first byte comes until the last byte comes.
     * 
     * @since 5.4
     */
    long getContentDownloadTime();
}
