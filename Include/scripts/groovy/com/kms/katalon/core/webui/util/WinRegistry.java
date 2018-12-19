package com.kms.katalon.core.webui.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

public class WinRegistry {

    public static final int HKEY_CURRENT_USER = 0x80000001;
    public static final int REG_SUCCESS = 0;
    public static final int REG_NOTFOUND = 2;
    private static final int KEY_READ = 0x20019;

    private static Preferences userRoot = Preferences.userRoot();

    private static Method regOpenKey = null;
    private static Method regCloseKey = null;
    private static Method regQueryInfoKey = null;
    private static Method regEnumKeyEx = null;
    private static Method regEnumValue = null;
    private static Method regQueryValueEx = null;
    private static Method regCreateKeyEx = null;
    private static Method regSetValueEx = null;

    private static Class<? extends Preferences> userClass = userRoot.getClass();

    public static final String FEATURE_CONTROL_REGISTRY_PATH = "Software\\Microsoft\\Internet Explorer\\Main\\FeatureControl";
    public static final String FEATURE_HTTP_USERNAME_PASSWORD_DISABLE_SUBKEY = "FEATURE_HTTP_USERNAME_PASSWORD_DISABLE";
    public static final String ADD_DWORD_IEXPLORE_EXE = "REG ADD \"HKEY_CURRENT_USER\\Software\\Microsoft\\Internet Explorer\\Main\\FeatureControl\\FEATURE_HTTP_USERNAME_PASSWORD_DISABLE\" /v iexplore.exe /t REG_DWORD /d 0 /f";

    static {
        try {
            regOpenKey = userClass.getDeclaredMethod("WindowsRegOpenKey", new Class[] { int.class, byte[].class, int.class });
            regOpenKey.setAccessible(true);
            regCloseKey = userClass.getDeclaredMethod("WindowsRegCloseKey", new Class[] { int.class });
            regCloseKey.setAccessible(true);
            regQueryInfoKey = userClass.getDeclaredMethod("WindowsRegQueryInfoKey1", new Class[] { int.class });
            regQueryInfoKey.setAccessible(true);
            regEnumKeyEx = userClass.getDeclaredMethod("WindowsRegEnumKeyEx", new Class[] { int.class, int.class, int.class });
            regEnumKeyEx.setAccessible(true);
            regEnumValue = userClass.getDeclaredMethod("WindowsRegEnumValue", new Class[] { int.class, int.class, int.class });
            regEnumValue.setAccessible(true);
            regQueryValueEx = userClass.getDeclaredMethod("WindowsRegQueryValueEx", new Class[] { int.class, byte[].class });
            regQueryValueEx.setAccessible(true);
            regCreateKeyEx = userClass.getDeclaredMethod("WindowsRegCreateKeyEx", new Class[] { int.class, byte[].class });
            regCreateKeyEx.setAccessible(true);
            regSetValueEx = userClass.getDeclaredMethod("WindowsRegSetValueEx", new Class[] { int.class, byte[].class, byte[].class });
            regSetValueEx.setAccessible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Use only for IE in order to enable passing username and password on URL.
     * @throws Exception
     */
    public static void enableUsernamePasswordOnURL() throws Exception {
        List<String> subKeys;
        subKeys = readStringSubKeys(HKEY_CURRENT_USER, FEATURE_CONTROL_REGISTRY_PATH);

        if (!subKeys.contains(FEATURE_HTTP_USERNAME_PASSWORD_DISABLE_SUBKEY)) {
            createKey(FEATURE_CONTROL_REGISTRY_PATH + "\\" + FEATURE_HTTP_USERNAME_PASSWORD_DISABLE_SUBKEY);
            Runtime.getRuntime().exec(ADD_DWORD_IEXPLORE_EXE);
        }
    }
    
    /**-------------------------------------------------------------------------
     Edit registry in Window - reference solution from : https://stackoverflow.com/questions/62289/read-write-to-windows-registry-using-java/6163701
     */
   
    public static List<String> readStringSubKeys(int hkey, String key)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        return readStringSubKeys(userRoot, hkey, key);
    }

    private static List<String> readStringSubKeys(Preferences root, int hkey, String key)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        List<String> results = new ArrayList<String>();
        int[] handles = (int[]) regOpenKey.invoke(root,
                new Object[] { new Integer(hkey), toCstr(key), new Integer(KEY_READ) });
        if (handles[1] != REG_SUCCESS) {
            return null;
        }
        int[] info = (int[]) regQueryInfoKey.invoke(root, new Object[] { new Integer(handles[0]) });
        
        //Fix: info[2] was being used here with wrong results.
        int count = info[0]; 
        int maxlen = info[3]; 
        for (int index = 0; index < count; index++) {
            byte[] name = (byte[]) regEnumKeyEx.invoke(root,
                    new Object[] { new Integer(handles[0]), new Integer(index), new Integer(maxlen + 1) });
            results.add(new String(name).trim());
        }
        regCloseKey.invoke(root, new Object[] { new Integer(handles[0]) });
        return results;
    }

    public static Map<String, String> readStringValues(String key)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        return readStringValues(userRoot, key);
    }

    public static String readString(String key, String valueName)
            throws InvocationTargetException, IllegalArgumentException, IllegalAccessException {
        return readString(userRoot, key, valueName);
    }

    private static Map<String, String> readStringValues(Preferences root, String key)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        HashMap<String, String> results = new HashMap<String, String>();
        int[] handles = (int[]) regOpenKey.invoke(root,
                new Object[] { new Integer(HKEY_CURRENT_USER), toCstr(key), new Integer(KEY_READ) });
        if (handles[1] != REG_SUCCESS) {
            return null;
        }
        int[] info = (int[]) regQueryInfoKey.invoke(root, new Object[] { new Integer(handles[0]) });

        int count = info[0]; // count
        int maxlen = info[3]; // value length max
        for (int index = 0; index < count; index++) {
            byte[] name = (byte[]) regEnumValue.invoke(root,
                    new Object[] { new Integer(handles[0]), new Integer(index), new Integer(maxlen + 1) });
            String value = readString(key, new String(name));
            results.put(new String(name).trim(), value);
        }
        regCloseKey.invoke(root, new Object[] { new Integer(handles[0]) });
        return results;
    }

    private static String readString(Preferences root, String key, String value)
            throws InvocationTargetException, IllegalAccessException, IllegalArgumentException {
        int[] handles = (int[]) regOpenKey.invoke(root,
                new Object[] { new Integer(HKEY_CURRENT_USER), toCstr(key), new Integer(KEY_READ) });
        if (handles[1] != REG_SUCCESS) {
            return null;
        }
        byte[] valb = (byte[]) regQueryValueEx.invoke(root, new Object[] { new Integer(handles[0]), toCstr(value) });
        regCloseKey.invoke(root, new Object[] { new Integer(handles[0]) });
        return (valb != null ? new String(valb).trim() : null);
    }

    public static void createKey(String key)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        int[] ret;
        ret = createKey(userRoot, key);
        regCloseKey.invoke(userRoot, new Object[] { new Integer(ret[0]) });

        if (ret[1] != REG_SUCCESS) {
            throw new IllegalArgumentException("rc=" + ret[1] + "  key=" + key);
        }
    }

    private static int[] createKey(Preferences root, String key)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        return (int[]) regCreateKeyEx.invoke(root, new Object[] { new Integer(HKEY_CURRENT_USER), toCstr(key) });
    }

    // utility
    private static byte[] toCstr(String str) {
        byte[] result = new byte[str.length() + 1];

        for (int i = 0; i < str.length(); i++) {
            result[i] = (byte) str.charAt(i);
        }
        result[str.length()] = 0;
        return result;
    }

}
