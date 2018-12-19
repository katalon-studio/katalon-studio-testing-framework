package com.kms.katalon.core.helper.screenrecorder;

public class VideoRecorder implements IVideoRecorder {

    private AbstractVideoRecorder delegate;

    private String outputDirLocation;

    private String outputVideoName;

    public VideoRecorder(String outputDirLocation, String outputVideoName, VideoConfiguration videoConfig)
            throws VideoRecorderException {
        this.outputDirLocation = outputDirLocation;
        this.outputVideoName = outputVideoName;
        initRecorder(videoConfig);
    }

    public VideoRecorder(String outputDirLocation, String outputVideoName) throws VideoRecorderException {
        this.outputDirLocation = outputDirLocation;
        this.outputVideoName = outputVideoName;
        initRecorder(new VideoConfiguration());
    }

    public void setOutputDirLocation(String outputDirLocation) throws VideoRecorderException {
        this.outputDirLocation = outputDirLocation;
        reload();
    }

    public void setOutputVideoName(String outputVideoName) throws VideoRecorderException {
        this.outputVideoName = outputVideoName;
        reload();
    }

    private void initRecorder(VideoConfiguration videoConfig) throws VideoRecorderException {
        Recorder recorderConfig = videoConfig.getRecorder();
        if (Recorder.ATU == recorderConfig) {
            delegate = new ATUVideoRecorder(outputDirLocation, outputVideoName, videoConfig);
            return;
        }
    }

    @Override
    public void start() throws VideoRecorderException {
        if (isStarted()) {
            // have to stop the current recording before start the new one
            stop();
            reload();
        }

        delegate.start();
    }

    @Override
    public void stop() throws VideoRecorderException {
        if (!isStarted()) {
            return;
        }

        delegate.stop();
    }

    @Override
    public void reload() throws VideoRecorderException {
        delegate.reload();
    }

    @Override
    public boolean isStarted() {
        return delegate.isStarted();
    }

    @Override
    public boolean isInterrupted() {
        return delegate.isInterrupted();
    }

    @Override
    public String getCurrentVideoLocation() {
        return delegate.getCurrentVideoLocation();
    }

    @Override
    public void delete() {
        delegate.delete();
    }

}
