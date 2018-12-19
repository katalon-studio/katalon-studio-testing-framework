package com.kms.katalon.core.helper.screenrecorder;

public abstract class AbstractVideoRecorder implements IVideoRecorder {

    protected VideoConfiguration videoConfig;

    protected long startTime;

    protected boolean started;

    protected boolean interrupted;

    protected String currentVideoLocation;

    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public boolean isInterrupted() {
        return interrupted;
    }

    @Override
    public String getCurrentVideoLocation() {
        return currentVideoLocation;
    }
}
