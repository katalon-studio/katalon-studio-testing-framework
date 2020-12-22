package com.kms.katalon.core.context.internal;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.openqa.selenium.WebDriver;

import com.kms.katalon.core.constants.CoreMessageConstants;
import com.kms.katalon.core.constants.StringConstants;
import com.kms.katalon.core.event.EventBusSingleton;
import com.kms.katalon.core.event.TestingEvent;
import com.kms.katalon.core.helper.screenrecorder.CDTVideoRecorder;
import com.kms.katalon.core.helper.screenrecorder.Recorder;
import com.kms.katalon.core.helper.screenrecorder.VideoConfiguration;
import com.kms.katalon.core.helper.screenrecorder.VideoRecorder;
import com.kms.katalon.core.helper.screenrecorder.VideoRecorderBuilder;
import com.kms.katalon.core.helper.screenrecorder.VideoRecorderException;
import com.kms.katalon.core.helper.screenrecorder.VideoSubtitleWriter;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.logging.model.TestStatus;
import com.kms.katalon.core.setting.VideoRecorderSetting;
import com.kms.katalon.core.util.internal.ExceptionsUtil;
import com.kms.katalon.core.util.internal.TestOpsUtil;

public class VideoRecorderService implements ExecutionListenerEventHandler {

    private static final String VIDEO_KEYWORD_NAME = "Video";

    private static final String VIDEOS_FOLDER_NAME = "videos";

    private static final String SCREEN_RECORDING_NAME_PATTERN = "test_%d";

    private static final String BROWSER_RECORDING_NAME_PATTERN = "recording-%d";

    private final KeywordLogger logger = KeywordLogger.getInstance(this.getClass());

    private String reportFolder;

    private VideoRecorderSetting videoRecorderSetting;

    private long actionStartTime = 0;

    private VideoRecorder screenRecorder;

    private VideoRecorder browserRecorder;

    private VideoSubtitleWriter screenRecorderSubWriter;

    private int prevStepIndex = 0;

    private String prevStepName;

    private String prevStepDescription;

    private InternalTestCaseContext lastestTestCaseContext;

    public VideoRecorderService(String reportFolder, VideoRecorderSetting videoRecorderSetting) {
        this.reportFolder = reportFolder;
        this.videoRecorderSetting = videoRecorderSetting;
        EventBusSingleton.getInstance().getEventBus().register(this);
    }

    private boolean shouldRecord() {
        return isEnableScreenRecorder() || isEnableBrowserRecorder();
    }

    private boolean isEnableScreenRecorder() {
        return videoRecorderSetting.isEnable() && !videoRecorderSetting.isUseBrowserRecorder()
                && (videoRecorderSetting.isAllowedRecordIfPassed() || videoRecorderSetting.isAllowedRecordIfFailed());
    }

    private boolean isEnableBrowserRecorder() {
        return videoRecorderSetting.isEnable() && videoRecorderSetting.isUseBrowserRecorder();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTestingEvent(TestingEvent event) {
        switch (event.getType()) {
            case BROWSER_OPENED:
                if (isEnableBrowserRecorder()) {
                    startBrowserRecorder((WebDriver) event.getData());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void handleListenerEvent(ExecutionListenerEvent listenerEvent, Object[] testContext) {
        if (!shouldRecord()) {
            return;
        }
        switch (listenerEvent) {
            case BEFORE_TEST_CASE: {
                lastestTestCaseContext = (InternalTestCaseContext) testContext[0];
                if (!isMainTestCase()) {
                    return;
                }
                if (isEnableScreenRecorder()) {
                    startScreenRecorder();
                }
                break;
            }
            case AFTER_TEST_CASE: {
                lastestTestCaseContext = (InternalTestCaseContext) testContext[0];
                if (!isMainTestCase()) {
                    return;
                }
                try {
                    flushSubTitles();
                    if (isEnableBrowserRecorder()) {
                        stopBrowserRecording();
                    }
                    if (isEnableScreenRecorder()) {
                        stopScreenRecording();
                    }
                } catch (VideoRecorderException error) {
                    logger.logError(ExceptionsUtil.getStackTraceForThrowable(error));
                }
                break;
            }
            case BEFORE_TEST_STEP: {
                if (!isMainTestCase()) {
                    return;
                }
                safeWriteSub((int) testContext[0], (String) testContext[1], (String) testContext[2]);
                break;
            }
            case AFTER_TEST_STEP: {
                break;
            }
            default:
                break;
        }
    }

    private void startBrowserRecorder(WebDriver webDriver) {
        try {
            String videoFolderName = getVideoFolder();
            int testCaseIndex = lastestTestCaseContext.getTestCaseIndex();
            String videoFileName = getBrowserRecordingName(testCaseIndex + 1);

            VideoConfiguration cdtVideoConfig = videoRecorderSetting.toVideoConfiguration();
            cdtVideoConfig.setRecorder(Recorder.CDT);
            VideoRecorder newBrowserRecorder = VideoRecorderBuilder.get()
                    .setVideoConfig(cdtVideoConfig)
                    .setOutputDirLocation(videoFolderName)
                    .setOutputVideoName(videoFileName)
                    .create();

            boolean isNewTestCase = browserRecorder == null || !StringUtils
                    .equals(browserRecorder.getCurrentVideoLocation(), newBrowserRecorder.getCurrentVideoLocation());

            if (isNewTestCase) {
                browserRecorder = newBrowserRecorder;
                ((CDTVideoRecorder) browserRecorder.getDelegate()).setDriver(webDriver);
                browserRecorder.start();

                startVideoSubWriter(videoFolderName, videoFileName);
            } else {
                ((CDTVideoRecorder) browserRecorder.getDelegate()).setDriver(webDriver);
                browserRecorder.resume();
            }
        } catch (VideoRecorderException error) {
            logger.logError(ExceptionsUtil.getStackTraceForThrowable(error));
        }
    }

    private void startScreenRecorder() {
        System.out.println(MessageFormat.format(CoreMessageConstants.EXEC_LOG_START_RECORDING_VIDEO,
                lastestTestCaseContext.getTestCaseId()));
        int testCaseIndex = lastestTestCaseContext.getTestCaseIndex();
        try {
            String videoFolderName = getVideoFolder();
            String videoFileName = getScreenRecordingName(testCaseIndex + 1);
            screenRecorder = VideoRecorderBuilder.get()
                    .setVideoConfig(videoRecorderSetting.toVideoConfiguration())
                    .setOutputDirLocation(videoFolderName)
                    .setOutputVideoName(videoFileName)
                    .create();

            startVideoSubWriter(videoFolderName, videoFileName);

            screenRecorder.start();
        } catch (VideoRecorderException error) {
            logger.logError(ExceptionsUtil.getStackTraceForThrowable(error));
        }
    }

    private void startVideoSubWriter(String videoFolderName, String videoFileName) {
        screenRecorderSubWriter = new VideoSubtitleWriter(new File(videoFolderName, videoFileName).getAbsolutePath());
    }

    private void flushSubTitles() {
        safeWriteSub(0, null, null);
    }

    private void safeWriteSub(int stepIndex, String stepDescription, String stepName) {
        try {
            writeSub(stepIndex, stepDescription, stepName);
        } catch (IOException error) {
            logger.logError(ExceptionsUtil.getStackTraceForThrowable(error));
        }
    }

    private void writeSub(int stepIndex, String stepDescription, String stepName) throws IOException {
        String description = StringUtils.defaultIfEmpty(prevStepDescription, prevStepName);
        if (StringUtils.isNotBlank(description) && screenRecorderSubWriter != null) {
            long subStartTime = Math.max(actionStartTime - getStartTime(), 0);
            long subEndTime = Math.max(System.currentTimeMillis() - getStartTime(), 0);
            if (getStartTime() == 0) { // Video Recorder was not started yet
                subStartTime = 0;
                subEndTime = 0;
            }
            screenRecorderSubWriter.writeSub(subStartTime, subEndTime,
                    String.format("%d. %s", prevStepIndex + 1, description));
        }

        prevStepIndex = stepIndex;
        prevStepName = stepName;
        prevStepDescription = stepDescription;
        actionStartTime = System.currentTimeMillis();
    }

    private void stopBrowserRecording() throws VideoRecorderException {
        if (browserRecorder == null || !browserRecorder.isStarted()) {
            return;
        }

        browserRecorder.stop();

        if (isTestCasePassed() && !videoRecorderSetting.isRecordAllTestCases()) {
            browserRecorder.delete();
            if (screenRecorderSubWriter != null) {
                screenRecorderSubWriter.delete();
            }
        } else {
            String videoLocation = browserRecorder.getCurrentVideoLocation();
            logVideoRecordingStep(videoLocation);
        }
    }

    private void stopScreenRecording() throws VideoRecorderException {
        if (screenRecorder == null || !screenRecorder.isStarted()) {
            return;
        }

        screenRecorder.stop();

        if ((isTestCasePassed() && !videoRecorderSetting.isAllowedRecordIfPassed())
                || (isTestCaseFailed() && !videoRecorderSetting.isAllowedRecordIfFailed())) {
            screenRecorder.delete();
            if (screenRecorderSubWriter != null) {
                screenRecorderSubWriter.delete();
            }
        } else {
            String videoLocation = screenRecorder.getCurrentVideoLocation();
            logVideoRecordingStep(videoLocation);
        }
    }

    private long getStartTime() {
        if (isEnableBrowserRecorder()) {
            return browserRecorder.getStartTime();
        }
        if (isEnableScreenRecorder()) {
            return screenRecorder.getStartTime();
        }
        return 0;
    }

    private boolean isMainTestCase() {
        return lastestTestCaseContext != null && lastestTestCaseContext.isMainTestCase();
    }

    private boolean isTestCaseFailed() {
        String testCaseStatus = lastestTestCaseContext.getTestCaseStatus();
        return TestStatus.TestStatusValue.valueOf(testCaseStatus) == TestStatus.TestStatusValue.FAILED;
    }

    private boolean isTestCasePassed() {
        String testCaseStatus = lastestTestCaseContext.getTestCaseStatus();
        return TestStatus.TestStatusValue.valueOf(testCaseStatus) == TestStatus.TestStatusValue.PASSED;
    }

    private void logVideoRecordingStep(String videoLocation) {
        if (StringUtils.isBlank(videoLocation)) {
            return;
        }
        Map<String, String> attributes = new HashMap<>();
        attributes.put(StringConstants.XML_LOG_VIDEO_ATTACHMENT_PROPERTY,
                TestOpsUtil.getRelativePathForLog(videoLocation));
        String message = MessageFormat.format(CoreMessageConstants.EXEC_LOG_VIDEO_RECORDING_COMPLETED,
                lastestTestCaseContext.getTestCaseId());
        logger.startKeyword(VIDEO_KEYWORD_NAME, new HashMap<>(), new Stack<>());
        logger.logInfo(message, attributes);
        logger.endKeyword(VIDEO_KEYWORD_NAME, new HashMap<>(), new Stack<>());
    }

    private String getVideoFolder() {
        String rawPath = FilenameUtils.concat(reportFolder, VIDEOS_FOLDER_NAME);
        return FilenameUtils.separatorsToSystem(rawPath);
    }

    private String getBrowserRecordingName(int id) {
        return getVideoRecordingName(BROWSER_RECORDING_NAME_PATTERN, id);
    }

    private String getScreenRecordingName(int id) {
        return getVideoRecordingName(SCREEN_RECORDING_NAME_PATTERN, id);
    }

    private String getVideoRecordingName(String pattern, int id) {
        return String.format(pattern, id);
    }
}
