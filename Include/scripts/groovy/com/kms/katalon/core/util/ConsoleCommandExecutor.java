package com.kms.katalon.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import com.kms.katalon.core.util.internal.ProcessUtil;

public class ConsoleCommandExecutor {
    private static final String PATH = "PATH";

    private static final String MAC_PATH_ENV_FILE = "/etc/paths";

    private static final String LINUX_ENV_FILE = "/etc/environment";

    private ConsoleCommandExecutor() {
    }

    public static List<String> runConsoleCommandAndCollectResults(String[] command,
            Map<String, String> addtionalEnvironmentVariables) throws IOException, InterruptedException {
        return runConsoleCommandAndCollectResults(command, addtionalEnvironmentVariables, StringUtils.EMPTY, false);
    }

    public static List<String> runConsoleCommandAndCollectResults(String[] command,
            Map<String, String> addtionalEnvironmentVariables, boolean redirectErrorStream) throws IOException, InterruptedException {
        return runConsoleCommandAndCollectResults(command, addtionalEnvironmentVariables, StringUtils.EMPTY, redirectErrorStream);
    }

    public static List<String> runConsoleCommandAndCollectResults(String[] command,
            Map<String, String> addtionalEnvironmentVariables, String directory, boolean redirectErrorStream)
            throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);
        if (StringUtils.isNotEmpty(directory)) {
            pb.directory(new File(directory));
        }
        Map<String, String> existingEnvironmentVariables = pb.environment();
        existingEnvironmentVariables.putAll(addtionalEnvironmentVariables);
        pb.redirectErrorStream(redirectErrorStream);

        Process process = pb.start();
        List<String> resultLines = new ArrayList<String>();

        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
        try {
            String line;
            while ((line = br.readLine()) != null) {
                resultLines.add(line);
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }
        process.waitFor();

        return resultLines;
    }

    public static List<String> runConsoleCommandError(String[] command,
            Map<String, String> addtionalEnvironmentVariables, String directory) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);
        if (StringUtils.isNotEmpty(directory)) {
            pb.directory(new File(directory));
        }
        Map<String, String> existingEnvironmentVariables = pb.environment();
        existingEnvironmentVariables.putAll(addtionalEnvironmentVariables);

        Process process = pb.start();
        process.waitFor();
        List<String> resultLines = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        try {
            String line;
            while ((line = br.readLine()) != null) {
                resultLines.add(line);
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return resultLines;
    }
    
    public static String runConsoleCommandAndCollectFirstResult(String[] command,
            Map<String, String> addtionalEnvironmentVariables,
            String directory) throws IOException, InterruptedException {
        List<String> resultLines = runConsoleCommandAndCollectResults(command, addtionalEnvironmentVariables,
                directory, false);
        if (!resultLines.isEmpty()) {
            return resultLines.get(0);
        }
        return "";
    }

    public static String runConsoleCommandAndCollectFirstResult(String[] command,
            Map<String, String> addtionalEnvironmentVariables) throws IOException, InterruptedException {
        return runConsoleCommandAndCollectFirstResult(command, addtionalEnvironmentVariables, StringUtils.EMPTY);
    }

    public static List<String> runConsoleCommandAndCollectResults(String[] command)
            throws IOException, InterruptedException {
        return runConsoleCommandAndCollectResults(command, new HashMap<String, String>());
    }

    public static String runConsoleCommandAndCollectFirstResult(String[] command)
            throws IOException, InterruptedException {
        List<String> resultLines = runConsoleCommandAndCollectResults(command);
        if (!resultLines.isEmpty()) {
            return resultLines.get(0);
        }
        return "";
    }
    
    public static String runConsoleCommandAndCollectFirstResult(String[] command, boolean redirectErrorStream)
            throws IOException, InterruptedException {
        List<String> resultLines = runConsoleCommandAndCollectResults(command, new HashMap<String, String>(), StringUtils.EMPTY, redirectErrorStream);
        if (!resultLines.isEmpty()) {
            return resultLines.get(0);
        }
        return "";
    }

    public static boolean test(String program) {
        return SystemUtils.IS_OS_WINDOWS
                ? windowsTest(program)
                : unixTest(program);
    }

    private static boolean windowsTest(String program) {
        try {
            Runtime.getRuntime().exec(program);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static boolean unixTest(String program) {
        try {
            String testCommand = MessageFormat.format("which {0}", program);
            List<String> result = execSync(testCommand);
            return result.size() > 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    public static List<String> execSync(String command) throws IOException, InterruptedException {
        return execSync(command, null);
    }

    public static List<String> execSync(String command, String workingDir) throws IOException, InterruptedException {
        Process process = exec(command, workingDir);
        if (process == null) {
            return Collections.emptyList();
        }
        return ProcessUtil.readSync(process);
    }

    public static Process exec(String command) throws IOException {
        return exec(command, null);
    }

    public static Process exec(String command, String workingDir) throws IOException {
        ProcessBuilder processBuilder = build(command, workingDir);
        if (processBuilder == null) {
            return null;
        }
        return processBuilder.start();
    }

    public static ProcessBuilder build(String command) {
        return build(command, null);
    }

    public static ProcessBuilder build(String command, String workingDir) {
        return build(command, workingDir, true);
    }

    public static ProcessBuilder build(String command, String workingDir, boolean redirectError) {
        if (StringUtils.isBlank(command)) {
            return null;
        }

        String[] commands;
        if (SystemUtils.IS_OS_WINDOWS) {
            String[] windowsCommands = new String[] { "cmd.exe", "/C", command };
            commands = windowsCommands;
        } else {
            String[] unixLikeCommands = new String[] { "/bin/sh", "-c", command };
            commands = unixLikeCommands;
        }

        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.redirectErrorStream(redirectError);
        Map<String, String> envs = processBuilder.environment();
        envs.putAll(getEnvs(envs));
        if (workingDir != null) {
            processBuilder.directory(new File(workingDir));
        }

        return processBuilder;
    }

    private static Map<String, String> getEnvs(Map<String, String> existingEnvs) {
        Map<String, String> envs = new HashMap<String, String>();
        if (SystemUtils.IS_OS_MAC) {
            try {
                envs.putAll(getMacEnvs(existingEnvs));
            } catch (IOException error) {
                // Just skip
            }
        } else if (SystemUtils.IS_OS_LINUX) {
            try {
                envs.putAll(getLinuxEnvs(existingEnvs));
            } catch (IOException error) {
                // Just skip
            }
        }
        return envs;
    }

    private static Map<String, String> getMacEnvs(Map<String, String> existingEnvs) throws IOException {
        Map<String, String> envs = new HashMap<String, String>();
        envs.put(PATH, getMacPathEnvVar(existingEnvs));
        return envs;
    }

    private static String getMacPathEnvVar(Map<String, String> existingEnvs) throws IOException {
        String systemPath = StringUtils.defaultString(System.getenv(PATH));
        String existingPath = existingEnvs.get(PATH);
        String path = joinPaths(systemPath, existingPath);

        File pathsFile = new File(MAC_PATH_ENV_FILE);
        if (pathsFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(pathsFile))) {
                for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                    if (StringUtils.isBlank(line)) {
                        continue;
                    }
                    path = joinPaths(path, line.trim());
                }
            }
        }
        return path;
    }

    private static Map<String, String> getLinuxEnvs(Map<String, String> existingEnvs) throws IOException {
        Map<String, String> envs = new HashMap<String, String>();
        envs.put(PATH, getLinuxPathEnvVar(existingEnvs));
        return envs;
    }

    private static String getLinuxPathEnvVar(Map<String, String> existingEnvs) throws IOException {
        String systemPath = StringUtils.defaultString(System.getenv(PATH));
        String existingPath = existingEnvs.get(PATH);
        String path = joinPaths(systemPath, existingPath);

        File pathsFile = new File(LINUX_ENV_FILE);
        if (pathsFile.exists()) {
            String pathPrefix = MessageFormat.format("{0}=\"", PATH);
            try (BufferedReader reader = new BufferedReader(new FileReader(pathsFile))) {
                for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                    if (StringUtils.isBlank(line) || !StringUtils.startsWith(line, pathPrefix)) {
                        continue;
                    }
                    line = line.trim();
                    String trimmedPath = line.substring(pathPrefix.length(), line.length() - 1); // PATH="..."
                    path = joinPaths(path, trimmedPath);
                    break;
                }
            }
        }
        return path;
    }

    private static String joinPaths(String ...paths) {
        return String.join(":", paths);
    }
}
