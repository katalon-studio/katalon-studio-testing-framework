package com.kms.katalon.core.appium.driver;

import java.io.File;
import java.io.PrintStream;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;

public class AppiumOutputStreamHandler extends Tailer {
    
    private AppiumOutputStreamHandler(String appiumLogFilePath, PrintStream outStream) {
        super(new File(appiumLogFilePath), new TailerListenerAdapter() {
            @Override
            public void handle(String line) {
                outStream.println(line);
            }
        });
    }

    public static AppiumOutputStreamHandler create(String appiumLogFilePath, PrintStream outStream) {
        return new AppiumOutputStreamHandler(appiumLogFilePath, outStream);
    }
}
