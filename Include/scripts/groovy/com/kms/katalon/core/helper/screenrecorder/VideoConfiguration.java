package com.kms.katalon.core.helper.screenrecorder;

import java.awt.Point;
import java.awt.image.BufferedImage;

import atu.testrecorder.ATUTestRecorder;
import atu.testrecorder.media.image.Images;

public class VideoConfiguration {
    private Recorder recorder = Recorder.ATU;

    private VideoFileFormat videoFormat = VideoFileFormat.AVI;

    private VideoCursor cursor = VideoCursor.WHITE;

    /**
     * Possible values are 8, 16, 24
     */
    private int depth = 16; // default 24

    private long screenRate = 15; // default 15

    private long mouseRate = 15; // default 30

    private long maxFrameDuration = 1000L; // default 1000

    private Point cursorOffset = new Point(-8, -5);

    public VideoConfiguration() {
        // use default configuration
    }

    public VideoConfiguration(Recorder recorder, VideoFileFormat videoFormat, int depth, long screenRate,
            long mouseRate, long maxFrameDuration, VideoCursor cursor) {
        this.recorder = recorder;
        this.videoFormat = videoFormat;
        this.depth = depth;
        this.screenRate = screenRate;
        this.mouseRate = mouseRate;
        this.maxFrameDuration = maxFrameDuration;
        this.cursor = cursor;
    }

    public Recorder getRecorder() {
        return recorder;
    }

    public VideoFileFormat getVideoFormat() {
        return videoFormat;
    }

    public int getDepth() {
        return depth;
    }

    public long getScreenRate() {
        return screenRate;
    }

    public long getMouseRate() {
        return mouseRate;
    }

    public int getAviSyncInterval() {
        return (int) (Math.max(getScreenRate(), getMouseRate()) * 60);
    }

    public int getQuicktimeSyncInterval() {
        return (int) Math.max(getScreenRate(), getMouseRate());
    }

    public long getMaxFrameDuration() {
        return maxFrameDuration;
    }

    public VideoCursor getCursor() {
        return cursor;
    }

    public Point getCursorOffset() {
        return cursorOffset;
    }

    public BufferedImage getCursorImage() {
        if (getCursor() == VideoCursor.BLACK) {
            return Images.toBufferedImage(
                    Images.createImage(ATUTestRecorder.class, "/atu/testrecorder/media/images/Cursor.black.png"));
        }
        return Images.toBufferedImage(
                Images.createImage(ATUTestRecorder.class, "/atu/testrecorder/media/images/Cursor.white.png"));
    }

    public void setVideoFormat(VideoFileFormat videoFormat) {
        this.videoFormat = videoFormat;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
