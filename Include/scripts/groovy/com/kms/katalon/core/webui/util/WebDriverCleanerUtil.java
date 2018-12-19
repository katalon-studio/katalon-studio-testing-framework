package com.kms.katalon.core.webui.util;

import java.io.IOException;

import static com.kms.katalon.core.util.internal.ProcessUtil.killProcessOnUnix;
import static com.kms.katalon.core.util.internal.ProcessUtil.killProcessOnWindows;;

public class WebDriverCleanerUtil {
    public static void cleanup() throws InterruptedException, IOException {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().contains("win")) {
            killProcessOnWindows("chromedriver.exe");
            killProcessOnWindows("geckodriver.exe");
            killProcessOnWindows("MicrosoftWebDriver.exe");
            killProcessOnWindows("IEDriverServer.exe");
        } else {
            killProcessOnUnix("chromedriver");
            killProcessOnUnix("geckodriver");
        }
    }
}
