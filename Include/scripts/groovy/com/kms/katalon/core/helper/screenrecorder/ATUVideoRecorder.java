package com.kms.katalon.core.helper.screenrecorder;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;

import atu.testrecorder.media.MovieWriter;
import atu.testrecorder.media.avi.AVIWriter;
import atu.testrecorder.media.color.Colors;
import atu.testrecorder.media.quicktime.QuickTimeWriter;

public class ATUVideoRecorder extends AbstractVideoRecorder {

    private MovieWriter movieWriter;

    private Robot robot;

    private Rectangle screenBounds;

    private BufferedImage screenCapture;

    private List<MouseCapture> mouseCaptures;

    private BufferedImage videoImg;

    private Graphics2D videoGraphics;

    private ScheduledThreadPoolExecutor screenTimer;

    private Runnable screenTimerCommand;

    private ScheduledThreadPoolExecutor mouseTimer;

    private Runnable mouseTimerCommand;

    private final Object sync = new Object();

    private String outputDirLocation;

    private String outputVideoName;

    private File videoFile;

    private File videoTempFile;

    public ATUVideoRecorder(String outputDirLocation, String outputVideoName) throws VideoRecorderException {
        this.videoConfig = new VideoConfiguration();
        this.outputDirLocation = outputDirLocation;
        this.outputVideoName = outputVideoName;
        recorder(outputDirLocation, outputVideoName);
    }

    public ATUVideoRecorder(String outputDirLocation, String outputVideoName, VideoConfiguration videoConfig)
            throws VideoRecorderException {
        this.videoConfig = videoConfig;
        this.outputDirLocation = outputDirLocation;
        this.outputVideoName = outputVideoName;
        recorder(outputDirLocation, outputVideoName);
    }

    @Override
    public void reload() throws VideoRecorderException {
        recorder(outputDirLocation, outputVideoName);
        started = false;
    }

    private void recorder(String outputDirLocation, String outputVideoName) throws VideoRecorderException {
        try {
            Window window = new Window(null);
            GraphicsConfiguration cfg = window.getGraphicsConfiguration();
            screenBounds = cfg.getBounds();
            robot = new Robot(cfg.getDevice());
            mouseCaptures = Collections.synchronizedList(new LinkedList<>());

            videoImg = getVideoImage(videoConfig);
            videoGraphics = videoImg.createGraphics();
            videoGraphics.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
            videoGraphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
            videoGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

            screenTimerCommand = new Runnable() {

                @Override
                public void run() {
                    try {
                        grabScreen();
                    } catch (VideoRecorderException e) {
                        // LogUtil.logError(e);
                    }
                }
            };
            mouseTimerCommand = new Runnable() {

                @Override
                public void run() {
                    grabMouse();
                }
            };

            createMovieWriter(outputDirLocation, outputVideoName);
        } catch (VideoRecorderException e) {
            throw e;
        } catch (Exception e) {
            throw new VideoRecorderException(e);
        }
    }

    private BufferedImage getVideoImage(VideoConfiguration videoConfig) throws VideoRecorderException {
        int depth = videoConfig.getDepth();
        int screenWidth = screenBounds.width;
        int screenHeight = screenBounds.height;

        if (depth == 24) {
            return new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
        }

        if (depth == 16) {
            return new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_USHORT_555_RGB);
        }

        if (depth == 8) {
            return new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_BYTE_INDEXED,
                    Colors.createMacColors());
        }

        throw new VideoRecorderException("Unsupported color depth " + depth);
    }

    protected void createMovieWriter(String outputDirLocation, String outputVideoName)
            throws VideoRecorderException, IOException {
        File videoFile = getVideoFile(outputDirLocation, outputVideoName);
        int videoDepth = videoConfig.getDepth();
        int videoWidth = screenBounds.width;
        int videoHeight = screenBounds.height;
        VideoFileFormat videoFormat = videoConfig.getVideoFormat();

        if (videoFormat == VideoFileFormat.AVI) {
            AVIWriter aviWriter = new AVIWriter(videoFile);
            aviWriter.addVideoTrack(AVIWriter.VIDEO_SCREEN_CAPTURE, 1L, videoConfig.getMouseRate(), videoWidth,
                    videoHeight, videoDepth, videoConfig.getAviSyncInterval());
            if (videoDepth == 8) {
                aviWriter.setPalette(0, (IndexColorModel) videoImg.getColorModel());
            }
            movieWriter = aviWriter;
            return;
        }

        if (videoFormat == VideoFileFormat.MOV) {
            QuickTimeWriter movWriter = new QuickTimeWriter(videoFile);
            movWriter.addVideoTrack(QuickTimeWriter.VIDEO_ANIMATION, 1000L, videoWidth, videoHeight, videoDepth,
                    videoConfig.getQuicktimeSyncInterval());
            movieWriter = movWriter;
            return;
        }

        throw new VideoRecorderException("Unsupported video format " + videoFormat);
    }

    private File getVideoFile(String outputDirLocation, String outputVideoName) throws IOException {
        if (outputDirLocation == null || outputDirLocation.trim().isEmpty()) {
            outputDirLocation = VIDEO_TEMP_LOCATION;
        }

        if (outputVideoName == null || outputVideoName.trim().isEmpty()) {
            outputVideoName = System.currentTimeMillis() + "";
        }

        File outputDir = new File(outputDirLocation + File.separator);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        } else if (!outputDir.isDirectory()) {
            throw new IOException("\"" + outputDirLocation + "\" is not a directory.");
        }

        String fileLocationWithoutExt = outputDirLocation + File.separator + outputVideoName;
        currentVideoLocation = fileLocationWithoutExt + ".tmp";
        videoTempFile = new File(currentVideoLocation);

        currentVideoLocation = fileLocationWithoutExt + videoConfig.getVideoFormat().getExtension();
        videoFile = new File(currentVideoLocation);

        return videoTempFile;
    }

    @Override
    public void start() throws VideoRecorderException {
        startTime = System.currentTimeMillis();
        interrupted = true;
        screenTimer = createTimerExecutor(videoConfig.getScreenRate(), screenTimerCommand);
        mouseTimer = createTimerExecutor(videoConfig.getMouseRate(), mouseTimerCommand);
        started = true;
    }

    private ScheduledThreadPoolExecutor createTimerExecutor(long rate, Runnable command) {
        long initialDelay = 1000 / rate;
        long period = 1000 / rate;
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleAtFixedRate(command, initialDelay, period, TimeUnit.MILLISECONDS);
        return executor;
    }

    @Override
    public void stop() throws VideoRecorderException {
        try {
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                throw new VideoRecorderException(e.getMessage());
            }

            mouseTimer.shutdown();
            screenTimer.shutdown();

            try {
                mouseTimer.awaitTermination(1000 / videoConfig.getMouseRate(), TimeUnit.MILLISECONDS);
                screenTimer.awaitTermination(1000 / videoConfig.getScreenRate(), TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new VideoRecorderException(e.getMessage());
            }

            synchronized (this.sync) {
                try {
                    movieWriter.close();
                } catch (IOException e) {
                    throw new VideoRecorderException(e.getMessage());
                }
                this.movieWriter = null;
            }

            videoGraphics.dispose();
            videoImg.flush();
            interrupted = false;
        } finally {
            started = false;
            if (interrupted) {
                videoTempFile.delete();
                return;
            }
            videoTempFile.renameTo(videoFile);
            videoTempFile.delete();
        }
    }

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

    private void grabScreen() throws VideoRecorderException {
        screenCapture = robot.createScreenCapture(new Rectangle(0, 0, screenBounds.width, screenBounds.height));
        long now = System.currentTimeMillis();
        videoGraphics.drawImage(screenCapture, 0, 0, null);

        boolean hasMouseCapture = false;
        Point cursorOffset = videoConfig.getCursorOffset();
        BufferedImage cursorImage = videoConfig.getCursorImage();
        if (videoConfig.getCursor() != VideoCursor.NONE) {
            Point prevMouseLoc = new Point(0, 0);
            while (!mouseCaptures.isEmpty() && (mouseCaptures.get(0).time < now)) {
                MouseCapture mc = mouseCaptures.remove(0);
                long mcTime = mc.time;
                if (mcTime <= startTime) {
                    continue;
                }

                hasMouseCapture = true;
                Point mcLocation = mc.location;
                mcLocation.translate(-screenBounds.x, -screenBounds.y);

                synchronized (this.sync) {
                    if (movieWriter.isVFRSupported() && mcLocation.equals(prevMouseLoc)
                            && (mcTime - startTime <= videoConfig.getMaxFrameDuration())) {
                        return;
                    }

                    prevMouseLoc.setLocation(mcLocation);

                    int startDestRectX = mcLocation.x + cursorOffset.x;
                    int startDestRectY = mcLocation.y + cursorOffset.y;

                    videoGraphics.drawImage(cursorImage, startDestRectX, startDestRectY, null);
                    if (movieWriter == null) {
                        return;
                    }

                    try {
                        movieWriter.writeFrame(0, videoImg, mcTime - startTime);
                    } catch (IOException e) {
                        throw new VideoRecorderException(e);
                    }

                    startTime = mcTime;

                    int endDestRectX = startDestRectX + cursorImage.getWidth() - 1;
                    int endDestRectY = startDestRectY + cursorImage.getHeight() - 1;

                    int startSrcRectX = startDestRectX;
                    int startSrcRectY = startDestRectY;
                    int endSrcRectX = endDestRectX;
                    int endSrcRectY = endDestRectY;

                    videoGraphics.drawImage(screenCapture, startDestRectX, startDestRectY, endDestRectX, endDestRectY,
                            startSrcRectX, startSrcRectY, endSrcRectX, endSrcRectY, null);
                }
            }
        }

        if (hasMouseCapture) {
            return;
        }

        if (videoConfig.getCursor() != VideoCursor.NONE) {
            Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
            videoGraphics.drawImage(cursorImage, mouseLoc.x + cursorOffset.x, mouseLoc.x + cursorOffset.y, null);
        }

        synchronized (this.sync) {
            try {
                movieWriter.writeFrame(0, videoImg, now - startTime);
            } catch (IOException e) {
                throw new VideoRecorderException(e);
            }
        }

        startTime = now;
    }

    private void grabMouse() {
        mouseCaptures.add(new MouseCapture(System.currentTimeMillis(), MouseInfo.getPointerInfo().getLocation()));
    }

    private static class MouseCapture {
        public long time;

        public Point location;

        public MouseCapture(long time, Point location) {
            this.time = time;
            this.location = location;
        }
    }

    @Override
    public void delete() {
        if (videoFile != null && videoFile.exists()) {
            FileUtils.deleteQuietly(videoFile);
        }
    }
}
