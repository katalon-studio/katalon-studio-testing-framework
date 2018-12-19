package com.kms.katalon.core.util.internal;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;

public class JarUtil {
    public static InputStream getResourceAsInputStream(Class<?> clazz, String filePath) throws IOException {
        String projectPath = clazz.getProtectionDomain().getCodeSource().getLocation().getFile();
        projectPath = URLDecoder.decode(projectPath, "utf-8");

        File jarFile = new File(projectPath);
        if (jarFile.isDirectory()) { // built by IDE
            return new FileInputStream(new File(jarFile, filePath).getAbsolutePath());
        } else {
            return clazz.getClassLoader().getResourceAsStream(filePath);
        }
    }

    public static File[] getFiles(Class<?> clazz, String filePath, File tempFolder)
            throws IOException, URISyntaxException {
        String projectPath = clazz.getProtectionDomain().getCodeSource().getLocation().getFile();
        projectPath = URLDecoder.decode(projectPath, "utf-8");

        File bundleJarFile = new File(projectPath);
        if (bundleJarFile.isDirectory()) { // built by IDE
            FileUtils.copyDirectory(new File(bundleJarFile, filePath), tempFolder);
            return tempFolder.listFiles();
        } else {
            List<File> classpathFiles = new ArrayList<>();
            final JarFile jar = new JarFile(bundleJarFile);
            try {
                final Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = entries.nextElement();
                    String name = jarEntry.getName();
                    if (name.startsWith(filePath + "/") && !name.endsWith("/")) {
                        File tempClasspathEntry = new File(tempFolder, name.replaceFirst(filePath + "/", ""));
                        copySafely(jar.getInputStream(jarEntry), tempClasspathEntry);
                        classpathFiles.add(tempClasspathEntry);
                    }
                }
                return classpathFiles.toArray(new File[classpathFiles.size()]);
            } finally {
                jar.close();
            }
        }
    }

    private static void copySafely(InputStream is, File dest) throws IOException {
        try {
            FileUtils.copyInputStreamToFile(is, dest);
        } finally {
            if (is != null) {
                closeQuietly(is);
            }
        }
    }

    private static void closeQuietly(Closeable stream) {
        try {
            stream.close();
        } catch (IOException e) {
            // Nothing to do
        }
    }
}
