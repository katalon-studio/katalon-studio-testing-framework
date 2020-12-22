package com.kms.katalon.core.util;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

public class MimeTypeUtil {
    private MimeTypeUtil() {
        // Hide the default Constructor
    }

    public static String getMimeType(String filePath) {
        String mimeType = null;

        mimeType = MimetypesFileTypeMapUtil.getContentType(FilenameUtils.getExtension(filePath));
        if (StringUtils.isNotBlank(mimeType)) {
            return mimeType;
        }

        try {
            mimeType = Files.probeContentType(Paths.get(filePath));
            if (StringUtils.isNotBlank(mimeType)) {
                return mimeType;
            }
        } catch (IOException e) {
            // Just skip
        }

        mimeType = URLConnection.guessContentTypeFromName(filePath);
        if (StringUtils.isNotBlank(mimeType)) {
            return mimeType;
        }

        URLConnection connection;
        try {
            connection = (new File(filePath)).toURI().toURL().openConnection();
            mimeType = connection.getContentType();
            if (StringUtils.isNotBlank(mimeType) && !StringUtils.equals(mimeType, "content/unknown")) {
                return mimeType;
            }
        } catch (IOException e) {
            // Just skip
        }

        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        mimeType = mimeTypesMap.getContentType(filePath);
        if (StringUtils.isNotBlank(mimeType)) {
            return mimeType;
        }

        return mimeType;
    }
}
