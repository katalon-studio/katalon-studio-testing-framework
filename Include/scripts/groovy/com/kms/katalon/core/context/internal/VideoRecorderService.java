package com.kms.katalon.core.context.internal;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;

import com.kms.katalon.core.constants.CoreMessageConstants;
import com.kms.katalon.core.constants.StringConstants;
import com.kms.katalon.core.helper.screenrecorder.VideoRecorder;
import com.kms.katalon.core.helper.screenrecorder.VideoRecorderBuilder;
import com.kms.katalon.core.helper.screenrecorder.VideoRecorderException;
import com.kms.katalon.core.helper.screenrecorder.VideoSubtitleWriter;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.logging.KeywordLogger.KeywordStackElement;
import com.kms.katalon.core.setting.VideoRecorderSetting;
import com.kms.katalon.core.util.internal.ExceptionsUtil;

public class VideoRecorderService implements ExecutionListenerEventHandler {
    
    private final KeywordLogger logger = KeywordLogger.getInstance(this.getClass());
    
    private String reportFolder;

    private VideoRecorderSetting videoRecorderSetting;

    private long videoStartTime;

    private long actionStartTime;

    private VideoRecorder videoRecorder;

    private VideoSubtitleWriter videoSubtitleWriter;

    private InternalTestCaseContext lastestTestCaseContext;

    public VideoRecorderService(String reportFolder, VideoRecorderSetting videoRecorderSetting) {
        this.reportFolder = reportFolder;
        this.videoRecorderSetting = videoRecorderSetting;
    }

    private boolean shouldRecord() {
        return videoRecorderSetting.isEnable()
                && (videoRecorderSetting.isAllowedRecordIfPassed() || videoRecorderSetting.isAllowedRecordIfFailed());
    }

    @Override
    public void handleListenerEvent(ExecutionListenerEvent listenerEvent, Object[] testContext) {
        if (!shouldRecord()) {
            return;
        }
        switch (listenerEvent) {
            case BEFORE_TEST_CASE: {
                lastestTestCaseContext = (InternalTestCaseContext) testContext[0];
                if (!lastestTestCaseContext.isMainTestCase()) {
                    return;
                }
                System.out.println(MessageFormat.format(CoreMessageConstants.EXEC_LOG_START_RECORDING_VIDEO,
                        lastestTestCaseContext.getTestCaseId()));
                int testCaseIndex = lastestTestCaseContext.getTestCaseIndex();
                try {
                    String videoFolderName = reportFolder + "/videos";
                    String videoFileName = String.format("test_%d", testCaseIndex + 1);
                    videoRecorder = VideoRecorderBuilder.get()
                            .setVideoConfig(videoRecorderSetting.toVideoConfiguration())
                            .setOutputDirLocation(videoFolderName)
                            .setOutputVideoName(videoFileName)
                            .create();

                    videoSubtitleWriter = new VideoSubtitleWriter(
                            new File(videoFolderName, videoFileName).getAbsolutePath());

                    videoStartTime = actionStartTime = System.currentTimeMillis();

                    videoRecorder.start();
                } catch (VideoRecorderException e) {
                    System.err.println(ExceptionsUtil.getStackTraceForThrowable(e));
                }
                break;
            }
            case AFTER_TEST_CASE: {
                lastestTestCaseContext = (InternalTestCaseContext) testContext[0];
                if (!lastestTestCaseContext.isMainTestCase()) {
                    return;
                }
                try {
                    if (videoRecorder == null || !videoRecorder.isStarted()) {
                        return;
                    }

                    stopVideoRecording();
                    String testCaseStatus = lastestTestCaseContext.getTestCaseStatus();
                    if (("PASSED".equals(testCaseStatus) && !videoRecorderSetting.isAllowedRecordIfPassed())
                            || ("FAILED".equals(testCaseStatus) && !videoRecorderSetting.isAllowedRecordIfFailed())) {
                        deleteVideo();
                    } else {
                        Map<String, String> attributes = new HashMap<>();
                        attributes.put(StringConstants.XML_LOG_ATTACHMENT_PROPERTY,
                                videoRecorder.getCurrentVideoLocation());
                        String message = MessageFormat.format(CoreMessageConstants.EXEC_LOG_VIDEO_RECORDING_COMPLETED,
                                lastestTestCaseContext.getTestCaseId());
                        logger.startKeyword("Video", new HashMap<>(), new Stack<>());
                        logger.logInfo(message, attributes);
                        logger.endKeyword("Video", new HashMap<>(), new Stack<>());
                        System.out.println(message);
                    }
                } catch (VideoRecorderException e) {
                    System.err.println(ExceptionsUtil.getStackTraceForThrowable(e));
                }
                break;
            }
            case AFTER_TEST_STEP: {
                if (lastestTestCaseContext == null || !lastestTestCaseContext.isMainTestCase()) {
                    return;
                }
                try {
                    writeSub((int) testContext[0], (String) testContext[1], (String) testContext[2]);
                } catch (IOException e) {
                    System.err.println(ExceptionsUtil.getStackTraceForThrowable(e));
                }
            }
            case BEFORE_TEST_STEP: {
                if (lastestTestCaseContext == null || !lastestTestCaseContext.isMainTestCase()) {
                    return;
                }
                actionStartTime = System.currentTimeMillis();
            }
            default:
                break;
        }
    }

    protected void stopVideoRecording() throws VideoRecorderException {
        if (videoRecorder == null || !videoRecorder.isStarted()) {
            return;
        }
        videoRecorder.stop();
    }

    private void deleteVideo() {
        if (videoRecorder != null) {
            videoRecorder.delete();
        }
        if (videoSubtitleWriter != null) {
            videoSubtitleWriter.delete();
        }
    }

    private void writeSub(int stepIndex, String stepDescription, String stepName) throws IOException {
        String description = StringUtils.defaultIfEmpty(stepDescription, stepName);
        if (StringUtils.isEmpty(description) || videoSubtitleWriter == null) {
            return;
        }
        videoSubtitleWriter.writeSub(actionStartTime - videoStartTime, System.currentTimeMillis() - videoStartTime,
                String.format("%d. %s", stepIndex + 1, description));
    }
}
