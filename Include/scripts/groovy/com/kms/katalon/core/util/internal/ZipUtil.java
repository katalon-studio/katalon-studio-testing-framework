package com.kms.katalon.core.util.internal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class ZipUtil {
    
    public static void extract(File file, File destFolder) throws IOException {
        ZipFile zipFile = new ZipFile(file);
        try {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File entryDestination = new File(destFolder, entry.getName());
                if (entry.isDirectory()) {
                    entryDestination.mkdirs();
                } else {
                    entryDestination.getParentFile().mkdirs();
                    InputStream in = zipFile.getInputStream(entry);
                    OutputStream out = new FileOutputStream(entryDestination);
                    IOUtils.copy(in, out);
                    IOUtils.closeQuietly(in);
                    out.close();
                }
            }
        } finally {
            zipFile.close();
        }
    }
    
    public static void extractContent(File file, File destFolder, int startFragment) throws IOException {
        ZipFile zipFile = new ZipFile(file);
        try {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String entryName = entry.getName();
                if (startFragment > 0) {
                    int startOffset = StringUtils.ordinalIndexOf(entryName, "/", startFragment);
                    if (startOffset < 0) {
                        continue;
                    }
                    entryName = entryName.substring(startOffset + 1, entryName.length());
                }
                File entryDestination = new File(destFolder, entryName);
                if (entry.isDirectory()) {
                    entryDestination.mkdirs();
                } else {
                    entryDestination.getParentFile().mkdirs();
                    InputStream in = zipFile.getInputStream(entry);
                    OutputStream out = new FileOutputStream(entryDestination);
                    IOUtils.copy(in, out);
                    IOUtils.closeQuietly(in);
                    out.close();
                }
            }
        } finally {
            zipFile.close();
        }
    }
    
    public static Path compress(List<Path> files, Path zipfile) throws IOException {
        try (OutputStream zipFileOutputStream = Files.newOutputStream(zipfile);
                ZipOutputStream zipOutputStream = new ZipOutputStream(zipFileOutputStream)) {
            for (Path file : files) {
                InputStream fileInputStream = Files.newInputStream(file);
                ZipEntry zipEntry = new ZipEntry(file.toFile().getName());
                zipOutputStream.putNextEntry(zipEntry);
     
                IOUtils.copy(fileInputStream, zipOutputStream);
                IOUtils.closeQuietly(fileInputStream);
            }
            return zipfile;
        }
    }
}
