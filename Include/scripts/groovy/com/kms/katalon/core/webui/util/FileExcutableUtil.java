package com.kms.katalon.core.webui.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;

public class FileExcutableUtil {
    public static void makeFileExecutable(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            Set<PosixFilePermission> perms = new HashSet<>();
            for (PosixFilePermission permission : PosixFilePermission.values()) {
                perms.add(permission);
            }
            Files.setPosixFilePermissions(file.toPath(), perms);
        }
    }
}