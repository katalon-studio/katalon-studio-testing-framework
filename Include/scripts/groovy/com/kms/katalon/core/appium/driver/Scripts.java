package com.kms.katalon.core.appium.driver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import io.appium.java_client.service.local.AppiumServiceBuilder;


public enum Scripts {
    GET_PATH_TO_DEFAULT_NODE_UNIX("get_path_to_default_node.sh"),
    GET_NODE_JS_EXECUTABLE("getExe.js")
    ;
    private static final String RESOURCE_FOLDER = "/scripts/";
    private final String script;

    Scripts(String script) {
        this.script = script;
    }

    public File getScriptFile() {
        InputStream inputStream = AppiumServiceBuilder.class.getResourceAsStream(RESOURCE_FOLDER + this.script);
        byte[] bytes;
        try {
            bytes = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String[] splittedName =  this.script.split("\\.");
        File scriptFile;
        try {
            scriptFile = File.createTempFile(splittedName[0], "." + splittedName[1]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!scriptFile.exists()) {
            try {
                scriptFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        FileOutputStream output;
        try {
            output = new FileOutputStream(scriptFile, true);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            output.write(bytes);
            output.flush();
            output.close();
            return scriptFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
