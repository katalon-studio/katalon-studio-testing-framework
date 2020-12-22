package com.kms.katalon.core.util.internal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.util.ConsoleCommandExecutor;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;

public class ProcessUtil {
    // TODO: find a way to get pid without using reflection
    private static int getPidOfUnixProcess(Process p) throws NoSuchFieldException, IllegalAccessException {
        Field f = p.getClass().getDeclaredField("pid");
        f.setAccessible(true);
        return (Integer) f.get(p);
    }

    // TODO: find a way to get handle without using reflection
    private static WinNT.HANDLE getHandle(Process p) throws ReflectiveOperationException {
        Field f = p.getClass().getDeclaredField("handle");
        f.setAccessible(true);
        long hndl = f.getLong(p);
        WinNT.HANDLE handle = new WinNT.HANDLE();
        handle.setPointer(Pointer.createConstant(hndl));
        return handle;
    }

    /**
     * Terminate a process. Only support Windows and Unix based systems for now
     * 
     * @param p the process to terminate
     * @throws ReflectiveOperationException
     * @throws IOException 
     */
    public static void terminateProcess(Process p) throws ReflectiveOperationException, IOException {
        if (Platform.isWindows()) {
            terminateWindowsProcess(p);
        } else if (Platform.isLinux() || Platform.isMac()) {
            int pid = getPidOfUnixProcess(p);
            try {
                ConsoleCommandExecutor
                        .runConsoleCommandAndCollectFirstResult(new String[] { "kill", "-9", String.valueOf(pid) });
            } catch (InterruptedException ignore) {
                // ignore this
            }
        }
    }

    private static void terminateWindowsProcess(Process p) throws ReflectiveOperationException {
        WinNT.HANDLE handle = null;
        try {
            handle = getHandle(p);
            Kernel32.INSTANCE.TerminateProcess(handle, 0);
            Kernel32.INSTANCE.WaitForSingleObject(handle, RunConfiguration.getTimeOut() * 1000);
        } finally {
            if (handle != null) {
                Kernel32.INSTANCE.CloseHandle(handle);
            }
        }
    }

    public static void killProcessOnWindows(String processName) throws InterruptedException, IOException {
        killProcessOnWindows(processName, null, null);
    }
    
    public static void killProcessOnWindows(String processName, File logFile, File errorLogFile) throws InterruptedException, IOException {
        ProcessBuilder pb = new ProcessBuilder("taskkill", "/f", "/im", processName, "/t");
        if (logFile != null) {
            FileWriter writer = new FileWriter(logFile, true);
            writer.write(String.format("\r\nTerminating process \"%s\"\r\n", processName));
            writer.close();
            pb.redirectOutput(Redirect.appendTo(logFile));
        }
        if (errorLogFile != null) {
            pb.redirectError(Redirect.appendTo(errorLogFile));
        }
        pb.start().waitFor();
    }

    public static void killProcessOnUnix(String processName) throws InterruptedException, IOException {
        killProcessOnUnix(processName, null, null);
    }

    public static void killProcessOnUnix(String processName, File logFile, File errorLogFile) throws InterruptedException, IOException {
        ProcessBuilder pb = new ProcessBuilder("killall", processName);
        if (logFile != null) {
            FileWriter writer = new FileWriter(logFile, true);
            writer.write(String.format("\r\nTerminating process \"%s\"\r\n", processName));
            writer.close();
            pb.redirectOutput(Redirect.appendTo(logFile));
        }
        if (errorLogFile != null) {
            pb.redirectError(Redirect.appendTo(errorLogFile));
        }
        pb.start().waitFor();
    }

    public static List<String> readSync(Process process) throws InterruptedException, IOException {
        return read(process, true);
    }

    public static List<String> read(Process process) throws InterruptedException, IOException {
        return read(process, false);
    }

    public static List<String> read(Process process, boolean waitForProcessEnd) throws InterruptedException, IOException {
        List<String> output = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                output.add(line);
            }
            if (waitForProcessEnd) {
                process.waitFor();
            }
        } catch (IOException error) {
            // Just skip
        } finally {
            reader.close();
        }
        return output;
    }
}
