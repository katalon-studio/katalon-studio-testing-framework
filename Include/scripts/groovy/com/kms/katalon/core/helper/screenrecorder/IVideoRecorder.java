package com.kms.katalon.core.helper.screenrecorder;

import java.io.File;

public interface IVideoRecorder {

    public static final String VIDEO_TEMP_LOCATION = System.getProperty("java.io.tmpdir") + File.separator + "Katalon"
            + File.separator + "video";

    public void start() throws VideoRecorderException;

    public void stop() throws VideoRecorderException;

    /**
     * Reload the current instance which is based on previous parameters input
     * 
     * @throws VideoRecorderException
     */
    public void reload() throws VideoRecorderException;

    public boolean isStarted();

    public boolean isInterrupted();

    public String getCurrentVideoLocation();
    
    public void delete();
}
