package com.kms.katalon.core.helper.screenrecorder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;

import com.github.kklisura.cdt.protocol.commands.Page;
import com.github.kklisura.cdt.protocol.events.page.ScreencastFrame;
import com.github.kklisura.cdt.protocol.support.types.EventHandler;
import com.github.kklisura.cdt.protocol.types.page.StartScreencastFormat;
import com.github.kklisura.cdt.services.ChromeDevToolsService;
import com.kms.katalon.constants.DocumentationMessageConstants;
import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.constants.CoreMessageConstants;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.util.CDTUtils;
import com.kms.katalon.core.util.ConsoleCommandExecutor;

public class CDTVideoRecorder extends AbstractVideoRecorder {

    private final KeywordLogger logger = KeywordLogger.getInstance(this.getClass());

    private static final String FRAME_DURATIONS_FILE = "durations.txt";

    private static final String FRAME_NAME_PATTERN = "frame-%d.jpg";

    private static final String VIDEO_NAME_PATTERN = "{0}.mp4";

    private String outputDirLocation;

    private String outputVideoName;

    private String framesFolder;

    private WebDriver driver;

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    private Page page;

    double startTimestamp = 0.0d;

    double lastFrameTimestamp = 0.0d;

    long numFrames = 0L;

    Point videoSize = new Point(0, 0);

    Process ffmpegProcess;

    File inputFile;

    public CDTVideoRecorder(String outputDirLocation, String outputVideoName, VideoConfiguration videoConfig) {
        this.outputDirLocation = outputDirLocation;
        this.outputVideoName = outputVideoName;
        this.videoConfig = videoConfig;
    }

    @Override
    public void start() throws VideoRecorderException {
        driver = isAlive(driver) ? driver : findActiveDriver();
        if (driver == null) {
            logger.logWarning(CoreMessageConstants.MSG_ERR_NO_DRIVER_FOUND);
            return;
        }
        if (!ConsoleCommandExecutor.test("ffmpeg")) {
            logger.logWarning(MessageFormat.format(CoreMessageConstants.MSG_ERR_FFMPEG_NOT_INSTALLED,
                    DocumentationMessageConstants.BROWSER_BASED_RECORDER));
            return;
        }
        startCDTRecording();
        started = true;
    }

    @Override
    public void resume() throws VideoRecorderException {
        start();
    }

    private WebDriver findActiveDriver() {
        Object[] drivers = RunConfiguration.getStoredDrivers();
        for (Object driverI : drivers) {
            if (!(driverI instanceof WebDriver)) {
                continue;
            }
            if (isAlive(((WebDriver) driverI))) {
                return (WebDriver) driverI;
            }
        }
        return null;
    }

    public void startCDTRecording() {
        if (driver == null) {
            return;
        }
        ChromeDevToolsService chromeDevToolsService = CDTUtils.getService(driver);
        if (chromeDevToolsService == null) {
            return;
        }

        page = chromeDevToolsService.getPage();
        page.enable();

        page.onScreencastFrame(new EventHandler<ScreencastFrame>() {

            @Override
            public void onEvent(ScreencastFrame frame) {
                handleScreencastFrame(frame);
            }
        });

        page.startScreencast(StartScreencastFormat.JPEG, 100, null, null, 1);
    }

    private void handleScreencastFrame(ScreencastFrame frame) {
        page.screencastFrameAck(frame.getSessionId());

        numFrames += 1;
        double frameTimestamp = frame.getMetadata().getTimestamp();

        if (startTimestamp == 0) {
            startTimestamp = frameTimestamp;
            startTime = System.currentTimeMillis();
            inputFile = new File(getFramesFolder(), FRAME_DURATIONS_FILE);
        }
        if (lastFrameTimestamp == 0) {
            lastFrameTimestamp = frameTimestamp;
        }

        dumpFrameTime(frameTimestamp);

        videoSize.x = (int) Math.max(videoSize.x, frame.getMetadata().getDeviceWidth());
        videoSize.y = (int) Math.max(videoSize.y, frame.getMetadata().getDeviceHeight());

        try {
            storeFrame(getFramePath(numFrames), frame.getData());
            storeFrameDuration(frameTimestamp, numFrames);
        } catch (VideoRecorderException | IOException error) {
            logger.logError(ExceptionUtils.getFullStackTrace(error));
        } finally {
            lastFrameTimestamp = frameTimestamp;
        }
    }

    @Override
    public void stop() throws VideoRecorderException {
        stopScreencast();
        saveRecording();
        removeTempData();
        started = false;
    }

    private void stopScreencast() {
        if (page != null && isStarted()) {
            try {
                if (isAlive(driver)) {
                    page.stopScreencast();
                }
            } catch (Exception error) {
                // Just skip
            }
        }
    }

    private boolean isAlive(WebDriver driver) {
        try {
            driver.getTitle(); // Check if the connection is still available
        } catch (Exception error) {
            return false;
        }
        return true;
    }

    private void saveRecording() throws VideoRecorderException {
        if (page != null && isStarted()) {
            try {
                File videoFolder = new File(getVideoFolder());
                if (!videoFolder.exists()) {
                    videoFolder.mkdirs();
                }

                int evenWidth = videoSize.x % 2 == 0 ? videoSize.x : (videoSize.x + 1);
                int eventHeight = videoSize.y % 2 == 0 ? videoSize.y : (videoSize.y + 1);
                String ffmpegCommandPattern = "ffmpeg -y -f concat -i \"{0}\" -vf \"scale={1,number,#}:{2,number,#}\" -vcodec libx264 -crf 15 -pix_fmt yuv420p \"{3}\"";
                String ffmpegCommand = MessageFormat.format(ffmpegCommandPattern, FRAME_DURATIONS_FILE, evenWidth,
                        eventHeight, getVideoPath());
                logger.logDebug(ffmpegCommand);

                List<String> encodeResult = ConsoleCommandExecutor.execSync(ffmpegCommand, getFramesFolder());
                logger.logDebug(String.join("\r\n", encodeResult));
            } catch (Exception error) {
                throw new VideoRecorderException(error);
            }
        }
    }

    private void removeTempData() {
        try {
            FileUtils.deleteDirectory(new File(getFramesFolder()));
        } catch (IOException error) {
            // just skip
        }
    }

    @Override
    public void reload() throws VideoRecorderException {
        if (isStarted()) {
            stop();
        }
        resetVariables();
        start();
    }

    private void resetVariables() {
        page = null;
        startTimestamp = 0.0d;
        lastFrameTimestamp = 0.0d;
        numFrames = 0;
        videoSize = new Point(0, 0);
    }

    @Override
    public void delete() {
        File videoFile = new File(getVideoPath());
        if (videoFile.exists()) {
            FileUtils.deleteQuietly(videoFile);
        }
    }

    public static void storeFrame(String fileName, String base64Data) throws VideoRecorderException {
        try {
            File file = new File(fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                fileOutputStream.write(Base64.getDecoder().decode(base64Data));
            }
        } catch (IOException error) {
            throw new VideoRecorderException(error);
        }
    }

    private void storeFrameDuration(double frameTimestamp, long frameNumber) throws IOException {
        double frameDuration = frameTimestamp - lastFrameTimestamp;
        if (frameDuration > 0) {
            String durationLine = MessageFormat.format("duration {0,number,#.###}\n", frameDuration);
            FileUtils.write(inputFile, durationLine, true);
        }

        String frameLine = MessageFormat.format("file ''{0}''\n", getFrameName(frameNumber));
        FileUtils.write(inputFile, frameLine, true);
    }

    private void dumpFrameTime(double frameTimestamp) {
        double dif = frameTimestamp - lastFrameTimestamp;
        String frameTimeInfo = MessageFormat.format("Frame {0} ({1}ms)", numFrames, dif * 1000);
        logger.logDebug(frameTimeInfo);
    }

    @Override
    public String getCurrentVideoLocation() {
        return getVideoPath();
    }

    private String getVideoName() {
        return MessageFormat.format(VIDEO_NAME_PATTERN, outputVideoName);
    }

    private String getVideoFolder() {
        return outputDirLocation;
    }

    private String getVideoPath() {
        return FilenameUtils.concat(getVideoFolder(), getVideoName());
    }

    private String getFramesFolder() {
        if (StringUtils.isBlank(framesFolder)) {
            framesFolder = FilenameUtils.concat(VIDEO_TEMP_LOCATION, "frames-" + new Date().getTime());
        }
        return framesFolder;
    }

    private String getFrameName(long frameNumber) {
        return String.format(FRAME_NAME_PATTERN, frameNumber);
    }

    private String getFramePath(long frameNumber) {
        String frameName = getFrameName(frameNumber);
        return Paths.get(getFramesFolder(), frameName).toString();
    }
}
