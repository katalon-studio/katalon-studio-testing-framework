package com.kms.katalon.core.setting;

import com.kms.katalon.core.helper.screenrecorder.VideoConfiguration;
import com.kms.katalon.core.helper.screenrecorder.VideoFileFormat;
import com.kms.katalon.core.helper.screenrecorder.VideoQuality;

public class VideoRecorderSetting {
    private boolean enable;

    private boolean useBrowserRecorder;

    private VideoFileFormat videoFormat;

    private VideoQuality videoQuality;

    private boolean recordAllTestCases;

    private boolean allowedRecordIfFailed;

    private boolean allowedRecordIfPassed;
    
    public VideoRecorderSetting() {
        enable = false;
        videoFormat = VideoFileFormat.AVI;
        videoQuality = VideoQuality.LOW;
        useBrowserRecorder = false;
        recordAllTestCases = false;
        allowedRecordIfPassed = false;
        allowedRecordIfFailed = true;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isUseBrowserRecorder() {
        return useBrowserRecorder;
    }

    public void setUseBrowserRecorder(boolean useBrowserRecorder) {
        this.useBrowserRecorder = useBrowserRecorder;
    }

    public boolean isRecordAllTestCases() {
        return recordAllTestCases;
    }

    public void setRecordAllTestCases(boolean recordAllTestCases) {
        this.recordAllTestCases = recordAllTestCases;
    }

    public VideoQuality getVideoQuality() {
        return videoQuality;
    }

    public void setVideoQuality(VideoQuality videoQuality) {
        this.videoQuality = videoQuality;
    }

    public boolean isAllowedRecordIfFailed() {
        return allowedRecordIfFailed;
    }

    public void setAllowedRecordIfFailed(boolean allowedRecordIfFailed) {
        this.allowedRecordIfFailed = allowedRecordIfFailed;
    }

    public boolean isAllowedRecordIfPassed() {
        return allowedRecordIfPassed;
    }

    public void setAllowedRecordIfPassed(boolean allowedRecordIfPassed) {
        this.allowedRecordIfPassed = allowedRecordIfPassed;
    }

    public VideoFileFormat getVideoFormat() {
        return videoFormat;
    }

    public void setVideoFormat(VideoFileFormat videoFormat) {
        this.videoFormat = videoFormat;
    }

    public VideoConfiguration toVideoConfiguration() {
        VideoConfiguration config = new VideoConfiguration();
        config.setVideoFormat(videoFormat);
        config.setDepth(videoQuality.getDepth());
        return config;
    }
}
