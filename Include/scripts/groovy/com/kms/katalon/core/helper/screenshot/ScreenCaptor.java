package com.kms.katalon.core.helper.screenshot;

import java.io.File;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.constants.StringConstants;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.util.internal.ExceptionsUtil;

public abstract class ScreenCaptor {

    protected static final String SCREENSHOT_EXT = "png";
    
    protected final KeywordLogger logger = KeywordLogger.getInstance(this.getClass());

    /**
     * System will take browser screenshot when the executed keyword decides it can do
     * <code> takeScreenShot = true </code> and screenshot capture options is enabled by users
     * 
     * @param takeScreenShot
     *            true if keyword can take screenshot
     * @return Map of attributes contains attachment XML log
     */
    public final Map<String, String> takeScreenshotAndGetAttributes(boolean takeScreenShot) {
        if (!takeScreenShot || !isScreenCaptureEnabled()) {
            return Collections.emptyMap();
        }

        Map<String, String> attributes = new HashMap<>();
        try {
            File newScreenshot = getNewFile(logger.getLogFolderPath());

            take(newScreenshot);

            attributes.put(StringConstants.XML_LOG_ATTACHMENT_PROPERTY, newScreenshot.getName());
        } catch (ScreenCaptureException e) {
            logger.logWarning(
                    MessageFormat.format(StringConstants.KW_LOG_WARNING_CANNOT_TAKE_SCREENSHOT,
                            ExceptionsUtil.getMessageForThrowable(e)));
        }

        return attributes;
    }

    protected abstract void take(File newFile) throws ScreenCaptureException;

    private String newFileName() {
        return System.currentTimeMillis() + "." + SCREENSHOT_EXT;
    }

    private File getNewFile(String parentFolder) {
        File newFile = null;
        while (newFile == null || newFile.exists()) {
            newFile = new File(parentFolder, newFileName());
        }
        return newFile;
    }

    private final boolean isScreenCaptureEnabled() {
        @SuppressWarnings("unchecked")
        Boolean screenCaptureOption = (Boolean) ((Map<String, Object>) RunConfiguration
                .getExecutionGeneralProperties().get(StringConstants.CONF_PROPERTY_REPORT))
                .get(StringConstants.CONF_PROPERTY_SCREEN_CAPTURE_OPTION);

        return screenCaptureOption.booleanValue();
    }
}
