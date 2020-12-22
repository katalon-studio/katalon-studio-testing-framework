package com.kms.katalon.core.util.internal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;

import com.kms.katalon.core.configuration.RunConfiguration;

public class TestOpsUtil {

    public static final String TESTOPS_VISION_FILE_NAME_PREFIX = "keyes-";

    public static final String TESTOPS_VISION_REPORT_FOLDER = "keyes";

    public static final String DEFAULT_IMAGE_EXTENSION = "png";

    public static String replaceTestOpsVisionFileName(String originalFilePath) {
        if (originalFilePath == null) {
            return null;
        }

        String fullPath = addDefaultImageExtension(originalFilePath);
        int nameIndex = originalFilePath.lastIndexOf(File.separatorChar);
        if (nameIndex < 0) {
            return Paths.get(RunConfiguration.getReportFolder(), TESTOPS_VISION_REPORT_FOLDER,
                    (TESTOPS_VISION_FILE_NAME_PREFIX + fullPath)).toString();
        }

        String path = fullPath.substring(0, nameIndex);
        String fileName = fullPath.substring(nameIndex + 1);
        Path p = Paths.get(path, TESTOPS_VISION_REPORT_FOLDER, (TESTOPS_VISION_FILE_NAME_PREFIX + fileName));
        if (!p.isAbsolute()) {
            p = Paths.get(RunConfiguration.getReportFolder(), p.toString());
        }

        return p.toString();
    }
    
    public static File ensureDirectory(File file, boolean isFile) throws IOException, SecurityException {
        return PathUtil.ensureDirectory(file, isFile);
    }

    private static String addDefaultImageExtension(String fileName) {
        if (!FilenameUtils.isExtension(fileName, DEFAULT_IMAGE_EXTENSION)) {
            return fileName + "." + DEFAULT_IMAGE_EXTENSION;
        }
        return fileName;
    }
    
    public static String getRelativePathForLog(String fileName) {
        return PathUtil.getRelativePathForLog(fileName);
    }
    
}
