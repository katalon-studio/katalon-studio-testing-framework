package com.kms.katalon.core.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.FrameworkUtil;

import com.kms.katalon.core.model.RunningMode;

public class LogbackUtil {

    public static File getLogbackConfigFile() throws IOException {
        Path path;
        if (isConsoleMode() || isMacOs()) {
            path =  new Path("/resources/logback/logback-console.xml");
        } else {
            path =  new Path("/resources/logback/logback.xml");
        }
  
        URL logbackConfigFileUrl = FileLocator.find(FrameworkUtil.getBundle(LogbackUtil.class), path, null);
        return FileUtils.toFile(FileLocator.toFileURL(logbackConfigFileUrl));
    }
    
    private static boolean isConsoleMode() {
        return ApplicationRunningMode.get() == RunningMode.CONSOLE;
    }
    
    private static boolean isMacOs() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }
}
