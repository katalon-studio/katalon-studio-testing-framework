package com.kms.katalon.core.util.internal;

import java.io.IOException;
import java.lang.reflect.Field;

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
        ProcessBuilder pb = new ProcessBuilder("taskkill", "/f", "/im", processName, "/t");
        pb.start().waitFor();
    }

    public static void killProcessOnUnix(String processName) throws InterruptedException, IOException {
        ProcessBuilder pb = new ProcessBuilder("killall", processName);
        pb.start().waitFor();
    }
}
