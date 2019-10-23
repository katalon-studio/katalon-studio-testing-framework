package com.kms.katalon.core.webui.util;

import org.apache.commons.lang.SystemUtils;

public class OSUtil {
    private static String OS = System.getProperty("os.name").toLowerCase();

    public static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }

    public static boolean isMac() {
        return (OS.indexOf("mac") >= 0);
    }

    public static boolean isUnix() {
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
    }

    public static boolean isSolaris() {
        return (OS.indexOf("sunos") >= 0);
    }

    public static boolean is64Bit() {
        if (SystemUtils.OS_ARCH.endsWith("64")) {
            return true;
        } else if (SystemUtils.OS_ARCH.endsWith("86")) {
            return false;
        }
        return false;
    }

}
