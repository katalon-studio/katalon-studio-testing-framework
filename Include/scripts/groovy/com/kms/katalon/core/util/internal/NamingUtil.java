package com.kms.katalon.core.util.internal;

import java.io.File;
import java.text.MessageFormat;

import org.apache.commons.io.FilenameUtils;

public class NamingUtil {

    public static String getUniqueFileName(String preferredName, String location) {
        return getUniqueFileName(preferredName, location, null);
    }

    public static String getUniqueFileName(String preferredName, String location, Integer sequenceNumber) {
        File parent = new File(location);
        if (!parent.exists()) {
            return preferredName;
        }

        do {
            String fileNameToBeTested = buildSequenceName(preferredName, sequenceNumber);
            File file = new File(FilenameUtils.concat(parent.getAbsolutePath(), fileNameToBeTested));
            if (file.exists()) {
                sequenceNumber = sequenceNumber != null
                        ? sequenceNumber + 1
                        : 1;
                continue;
            }
            return file.getName();
        } while (true);
    }

    private static String buildSequenceName(String preferredName, Integer sequenceNumber) {
        return sequenceNumber != null
                ? MessageFormat.format("{0} ({1})", preferredName, sequenceNumber)
                : preferredName;
    }
}
