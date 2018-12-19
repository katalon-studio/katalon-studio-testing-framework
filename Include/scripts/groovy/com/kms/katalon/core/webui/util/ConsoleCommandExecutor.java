package com.kms.katalon.core.webui.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsoleCommandExecutor {
    private ConsoleCommandExecutor() {
        // Hide constructor
    }
    
    public static List<String> runConsoleCommandAndCollectResults(String[] command,
            Map<String, String> addtionalEnvironmentVariables) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.environment().putAll(addtionalEnvironmentVariables);

        Process process = pb.start();
        process.waitFor();
        List<String> resultLines = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultLines.add(line);
            }
        }
        return resultLines;
    }

    public static List<String> runConsoleCommandAndCollectResults(String[] command) throws IOException,
            InterruptedException {
        return runConsoleCommandAndCollectResults(command, new HashMap<String, String>());
    }

    public static String runConsoleCommandAndCollectFirstResult(String[] command) throws IOException,
            InterruptedException {
        List<String> resultLines = runConsoleCommandAndCollectResults(command);
        if (!resultLines.isEmpty()) {
            return resultLines.get(0);
        }
        return "";
    }
}
