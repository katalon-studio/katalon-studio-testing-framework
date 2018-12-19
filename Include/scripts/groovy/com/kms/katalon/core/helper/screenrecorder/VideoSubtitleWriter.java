package com.kms.katalon.core.helper.screenrecorder;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DurationFormatUtils;

public class VideoSubtitleWriter {

    private static final String SUB_TITLE_EXT = ".srt";

    private static final String SUB_DATE_TIME_FORMAT = "HH:mm:ss,SSS";

    private int counter = 0;

    private File subFile;

    public VideoSubtitleWriter(String fileName) {
        subFile = new File(fileName + SUB_TITLE_EXT);
    }

    public void writeSub(long start, long end, String sub) throws IOException {
        counter++;

        StringBuilder subtitleBuilder = new StringBuilder();
        subtitleBuilder.append(counter)
                .append("\n")
                .append(String.format("%s --> %s", getTimeFormat(start), getTimeFormat(end)))
                .append("\n")
                .append(sub)
                .append("\n\n");

        FileUtils.write(subFile, subtitleBuilder.toString(), true);
    }

    private String getTimeFormat(long time) {
        return DurationFormatUtils.formatDuration(time, SUB_DATE_TIME_FORMAT);
    }

    public void delete() {
        if (subFile != null && subFile.exists()) {
            FileUtils.deleteQuietly(subFile);
        }
    }
}
