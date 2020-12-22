package com.kms.katalon.core.webui.util;

import java.io.File;
import java.io.IOException;

import static com.kms.katalon.core.util.internal.ProcessUtil.killProcessOnUnix;
import static com.kms.katalon.core.util.internal.ProcessUtil.killProcessOnWindows;;

public class WebDriverCleanerUtil {
    public static void cleanup() throws InterruptedException, IOException {
        cleanup(null, null);
    }

    public static void cleanup(File logFile, File errorLogFile) throws InterruptedException, IOException {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().contains("win")) {
            killProcessOnWindows("chromedriver.exe", logFile, errorLogFile);
            killProcessOnWindows("geckodriver.exe", logFile, errorLogFile);
            killProcessOnWindows("MicrosoftWebDriver.exe", logFile, errorLogFile);
            killProcessOnWindows("msedgedriver.exe", logFile, errorLogFile);
            killProcessOnWindows("IEDriverServer.exe", logFile, errorLogFile);
        } else {
            killProcessOnUnix("chromedriver", logFile, errorLogFile);
            killProcessOnUnix("geckodriver", logFile, errorLogFile);
            killProcessOnUnix("msedgedriver", logFile, errorLogFile);
        }
    }
    
    public static String getOS() {
        return System.getProperty("os.name");
    }

    public static void terminateChromedriver() throws InterruptedException, IOException {
        String os = getOS();
        if (os.toLowerCase().contains("win")) {
            killProcessOnWindows("chromedriver.exe");
        } else {
            killProcessOnUnix("chromedriver");
        }
    }

    public static void terminateGeckodriver() throws InterruptedException, IOException {
        String os = getOS();
        if (os.toLowerCase().contains("win")) {
            killProcessOnWindows("geckodriver.exe");
        } else {
            killProcessOnUnix("geckodriver");
        }
    }

    public static void terminateMicrosoftWebDriver() throws InterruptedException, IOException {
        String os = getOS();
        if (os.toLowerCase().contains("win")) {
            killProcessOnWindows("MicrosoftWebDriver.exe");
        }
    }
    
    public static void terminateEdgeChromiumDriver() throws InterruptedException, IOException {
        String os = getOS();
        if (os.toLowerCase().contains("win")) {
            killProcessOnWindows("msedgedriver.exe");
        } else {
            killProcessOnUnix("msedgedriver");
        }
    }

    public static void terminateIEDriverServer() throws InterruptedException, IOException {
        String os = getOS();
        if (os.toLowerCase().contains("win")) {
            killProcessOnWindows("IEDriverServer.exe");
        }
    }
}
