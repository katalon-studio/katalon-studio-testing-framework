package com.kms.katalon.core.helper.screenrecorder;

public class VideoRecorderBuilder {

    private String outputDirLocation = VideoRecorder.VIDEO_TEMP_LOCATION;

    private String outputVideoName;

    private VideoConfiguration videoConfig = new VideoConfiguration();

    private static VideoRecorderBuilder instance;

    private VideoRecorderBuilder() {
        // Not allow creating from outside
    }

    public static VideoRecorderBuilder get() {
        if (instance == null) {
            instance = new VideoRecorderBuilder();
        }
        return instance;
    }

    public VideoRecorderBuilder setOutputDirLocation(String outputDirLocation) {
        this.outputDirLocation = outputDirLocation;
        return this;
    }

    public VideoRecorderBuilder setOutputVideoName(String outputVideoName) {
        this.outputVideoName = outputVideoName;
        return this;
    }

    public VideoRecorderBuilder setVideoConfig(VideoConfiguration videoConfig) {
        this.videoConfig = videoConfig;
        return this;
    }

    public VideoRecorder create() throws VideoRecorderException {
        return new VideoRecorder(outputDirLocation, outputVideoName, videoConfig);
    }
}
