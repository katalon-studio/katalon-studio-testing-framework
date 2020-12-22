package com.kms.katalon.core.helper.screenrecorder;

public abstract class AbstractVideoRecorder implements IVideoRecorder {

    protected VideoConfiguration videoConfig;

    protected long startTime = 0L;

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
    
    @Override
    public long getStartTime() {
        return startTime;
    }
    
    @Override
    public void pause() throws VideoRecorderException {
    }
    
    @Override
    public void resume() throws VideoRecorderException {
    }
}
