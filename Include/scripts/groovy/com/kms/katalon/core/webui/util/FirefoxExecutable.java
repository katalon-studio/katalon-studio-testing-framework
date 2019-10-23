package com.kms.katalon.core.webui.util;

import static org.openqa.selenium.Platform.MAC;
import static org.openqa.selenium.Platform.UNIX;
import static org.openqa.selenium.Platform.WINDOWS;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.os.ExecutableFinder;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.google.common.collect.ImmutableList;

// This class duplicated codes from org.openqa.selenium.firefox.internal.Executable as it is internal.
// Should not be re-factored.
public class FirefoxExecutable {
    private static final String VERSION_SEPARATOR_REGEX = "\\.";

    private static final String MOZILLA_FIREFOX_VERSION_STRING_PREFIX = "Mozilla Firefox";

    private static final File SYSTEM_BINARY = locateFirefoxBinaryFromSystemProperty();

    private static final File PLATFORM_BINARY = locateFirefoxBinaryFromPlatform();

    private FirefoxExecutable() {
    }

    public static int getFirefoxVersion(DesiredCapabilities desiredCapabilities) {
        File defaultFirefoxBinary = FirefoxExecutable.getFirefoxBinaryFile(getFirefoxBinary(desiredCapabilities));
        try {
        	if (defaultFirefoxBinary == null) {
        		return 0;
        	}
            String firefoxVersionString = ConsoleCommandExecutor.runConsoleCommandAndCollectFirstResult(
                    new String[] { defaultFirefoxBinary.getAbsolutePath(), "-v", "|", "more" });
            if (firefoxVersionString == null
                    || !firefoxVersionString.startsWith(MOZILLA_FIREFOX_VERSION_STRING_PREFIX)) {
                return 0;
            }
            firefoxVersionString = firefoxVersionString.substring(MOZILLA_FIREFOX_VERSION_STRING_PREFIX.length())
                    .trim();
            String firefoxVersionMajor = firefoxVersionString.split(VERSION_SEPARATOR_REGEX)[0];
            Number firefoxVersion = NumberUtils.createNumber(firefoxVersionMajor);
            return firefoxVersion.intValue();
        } catch (IOException | InterruptedException | NumberFormatException e) {
            // Exception happened, ignore
        }
        return 0;
    }

    private static String getFirefoxBinary(DesiredCapabilities desiredCapabilities) {
        if (desiredCapabilities == null || desiredCapabilities.getCapability(FirefoxDriver.BINARY) == null) {
            return null;
        }
        Object raw = desiredCapabilities.getCapability(FirefoxDriver.BINARY);
        if (raw instanceof String) {
            return (String) raw;
        }
        return null;
    }

    public static File getFirefoxBinaryFile(String userSpecifiedBinaryPath) {
        if (userSpecifiedBinaryPath != null) {

            File userSpecifiedBinaryFile = new File(userSpecifiedBinaryPath);
            // It should exist and be a file.
            if (userSpecifiedBinaryFile.exists() && userSpecifiedBinaryFile.isFile()) {
                return userSpecifiedBinaryFile;
            }

            throw new WebDriverException("Specified firefox binary location does not exist or is not a real file: "
                    + userSpecifiedBinaryPath);
        }

        if (SYSTEM_BINARY != null && SYSTEM_BINARY.exists()) {
            return SYSTEM_BINARY;
        }

        if (PLATFORM_BINARY != null && PLATFORM_BINARY.exists()) {
            return PLATFORM_BINARY;
        }

        return null;
    }

    private static File locateFirefoxBinaryFromSystemProperty() {
        String binaryName = System.getProperty(FirefoxDriver.SystemProperty.BROWSER_BINARY);
        if (binaryName == null)
            return null;

        File binary = new File(binaryName);
        if (binary.exists())
            return binary;

        Platform current = Platform.getCurrent();
        if (current.is(WINDOWS)) {
            if (!binaryName.endsWith(".exe"))
                binaryName += ".exe";

        } else if (current.is(MAC)) {
            if (!binaryName.endsWith(".app"))
                binaryName += ".app";
            binaryName += "/Contents/MacOS/firefox-bin";
        }

        binary = new File(binaryName);
        if (binary.exists())
            return binary;

        throw new WebDriverException(String.format("'%s' property set, but unable to locate the requested binary: %s",
                FirefoxDriver.SystemProperty.BROWSER_BINARY, binaryName));
    }

    /**
     * Locates the firefox binary by platform.
     */
    @SuppressWarnings("deprecation")
    private static File locateFirefoxBinaryFromPlatform() {
        File binary = null;

        Platform current = Platform.getCurrent();
        if (current.is(WINDOWS)) {
        	File firefoxBinary64File = new File("C:\\Program Files\\Mozilla Firefox\\firefox.exe");
        	if (firefoxBinary64File.exists()) {
        		return firefoxBinary64File;
        	}
        	File firefoxBinary32File = new File("C:\\Program Files\\Mozilla Firefox (x86)\\firefox.exe");
        	if (firefoxBinary32File.exists()) {
        		return firefoxBinary32File;
        	}

        } else if (current.is(MAC)) {
            binary = new File("/Applications/Firefox.app/Contents/MacOS/firefox-bin");
            // fall back to homebrew install location if default is not found
            if (!binary.exists()) {
                binary = new File(System.getProperty("user.home") + binary.getAbsolutePath());
            }
        }

        if (binary != null && binary.exists()) {
            return binary;
        }

        ExecutableFinder binaryFinder = new ExecutableFinder();
        if (current.is(UNIX)) {
            String systemFirefox = binaryFinder.find("firefox-bin");
            if (systemFirefox != null) {
                return new File(systemFirefox);
            }
        }

        String systemFirefox = binaryFinder.find("firefox");
        if (systemFirefox != null) {
            return new File(systemFirefox);
        }

        return null;
    }

    private static File findExistingBinary(final ImmutableList<String> paths) {
        for (String path : paths) {
            File file = new File(path);
            if (file.exists()) {
                return file;
            }
        }
        return null;
    }

}
