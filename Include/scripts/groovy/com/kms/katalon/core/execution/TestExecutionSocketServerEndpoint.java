package com.kms.katalon.core.execution;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;

import com.google.gson.Gson;
import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.main.TestCaseExecutor;
import com.kms.katalon.core.testcase.BrokenTestCaseSummary;
import com.kms.katalon.core.util.TimeUtil;

/**
 * This is the end point that receives the captured MHTML from browser
 * extension's socket client. The important method is
 * {@link TestExecutionSocketServerEndpoint#handleMhtml(ByteBuffer)} which reads
 * the byte array returned by the socket client and separates it into the two
 * parts (hashed test case ID + MHTML file)
 * 
 * @author thanhto
 *
 */
public class TestExecutionSocketServerEndpoint extends Endpoint {

    private final KeywordLogger logger = KeywordLogger.getInstance(TestExecutionSocketServerEndpoint.class);

    public static final String TEST_NAME = "testExecutedEntityId";

    public static final String TEST_ARTIFACT_FOLDER = "testArtifactFolder";

    private Session session;

    private TestExecutionSocketServer socketServer;

    private static final int DEFAULT_MAX_TEXT_MESSAGE_SIZE = 1024 * 1024 * 10; // 10MB

    private static final int DEFAULT_MAX_BINARY_MESSAGE_SIZE = 1024 * 1024 * 10 * 2; // 20MB

    private Thread thread;

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        socketServer.removeActiveSocket(this);
        super.onClose(session, closeReason);
    }

    @Override
    public void onError(Session session, Throwable thr) {
        socketServer.removeActiveSocket(this);
        super.onError(session, thr);
    }

    /**
     * This handler receives byte array sent from browser extension and create a
     * MHTML file.
     * 
     * @see TestCaseExecutor#notifyTestExecutionSocketServerEndpoint
     * 
     * @param msg
     * a {@link ByteBuffer} sent from browser extension's socket
     * client
     */
    public void handleMhtml(ByteBuffer msg) {
        thread = new Thread(new Runnable() {
            @SuppressWarnings("unchecked")
            @Override
            public void run() {
                Date startTime = new Date();
                logger.logInfo("Start generating Time Capsule at " + startTime.toString());
                FileOutputStream fos = null;
                try {
                    byte[] bytes = new byte[64];
                    ByteBuffer mhtml = msg.get(bytes, 0, 64);
                    String keyToMap = new String(bytes);
                    Map<String, String> data = (Map<String, String>) TestExecutionDataProvider.getInstance()
                            .getTestExecutionData(keyToMap);
                    String folderToStoreMhtml = data.getOrDefault(TEST_ARTIFACT_FOLDER, "");
                    String testName = TestExecutionStringUtil
                            .getUnoffensiveTestCaseName(data.getOrDefault(TEST_NAME, ""));
                    String pathToMhtml = folderToStoreMhtml + File.separator + testName + "."
                            + BrokenTestCaseSummary.Constants.MHTML_EXTENSION;
                    File file = new File(pathToMhtml);
                    file.createNewFile();
                    fos = new FileOutputStream(file);
                    while (mhtml.hasRemaining()) {
                        fos.write(mhtml.get());
                    }
                    String mhtmlPathRelativeToProjectFolder = getPathRelativeToProjectFolder(pathToMhtml);
                    Date endTime = new Date();
                    logger.logInfo("Done generating Time Capsule at " + endTime.toString());
                    logger.logInfo("Generating Time Capsule took "
                            + TimeUtil.getDateDiff(startTime, endTime, TimeUnit.MILLISECONDS) + " miliseconds");
                    logger.logInfo("Time Capsule is available at " + mhtmlPathRelativeToProjectFolder);
                } catch (IOException | NullPointerException e) {
                    logger.logWarning(e.getMessage());
                } finally {
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        // ignore it
                    }
                }
            }
        });
        thread.start();
    }

    private String getPathRelativeToProjectFolder(String absolutePath) {
        String base = RunConfiguration.getProjectDir();
        return new File(base).toURI().relativize(new File(absolutePath).toURI()).getPath();
    }

    /**
     * This method waits within 5 seconds for a thread that handles MHTML to
     * start. This logic is needed because MHTML should be sent from browser's
     * extension after Katalon sends {@link TestExecutionCommand EXECUTION_END}
     * command, but Katalon's thread will continue after sending the command
     * which may terminate before receiving the MHTML
     * <p>
     * After 5 seconds, if a thread does start, it waits until the thread is
     * done.
     * </p>
     * 
     */
    public synchronized void waitForMhtmlHandlingThread() {
        int counter = 0;
        while (thread == null || counter < 5) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Ignore it
            }
            counter++;
        }
        try {
            thread.join();
        } catch (InterruptedException e) {
            // Ignore it
        }
    }

    public void notifyExecutionEndedAndWaitForMHTML(Map<String, String> data) {
        sendMessage(new TestExecutionMessage(TestExecutionCommand.EXECUTION_ENDED, data));
        waitForMhtmlHandlingThread();
    }

    public void close() {
        try {
            waitForMhtmlHandlingThread();
            session.close();
        } catch (IOException e) {
            logger.logWarning(e.getMessage());
        }
    }

    public void sendMessage(TestExecutionMessage message) {
        sendText(new Gson().toJson(message));
    }

    private void sendText(String text) {
        try {
            Basic remote = session.getBasicRemote();
            remote.sendText(text);
        } catch (IOException e) {
            logger.logWarning(e.getMessage());
        }
    }

    @Override
    public void onOpen(Session arg0, EndpointConfig arg1) {
        session = arg0;
        session.setMaxTextMessageBufferSize(DEFAULT_MAX_TEXT_MESSAGE_SIZE);
        session.setMaxBinaryMessageBufferSize(DEFAULT_MAX_BINARY_MESSAGE_SIZE);
        socketServer = TestExecutionSocketServer.getInstance();
        socketServer.addActiveSocket(this);
        session.addMessageHandler(new MessageHandler.Whole<ByteBuffer>() {

            @Override
            public void onMessage(ByteBuffer arg0) {
                handleMhtml(arg0);
            }

        });
    }
}
