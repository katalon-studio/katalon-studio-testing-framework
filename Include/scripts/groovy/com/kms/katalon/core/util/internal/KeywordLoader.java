package com.kms.katalon.core.util.internal;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

public class KeywordLoader {

    public static List<Class<?>> loadAllClass(String... packageNames) {
        List<Class<?>> cls = new ArrayList<Class<?>>();

        for (String packageName : packageNames) {
            cls.addAll(getAllClassInPackage(packageName));
        }

        return cls;
    }

    public static List<Class<?>> loadAllClass(Class<?> aClass) {
        return getAllClassInPackage(aClass.getPackage().getName());
    }

    private static List<Class<?>> getAllClassInPackage(String packageName) {
        try {
            ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
            ClassLoader cld = KeywordLoader.class.getClassLoader();
            Enumeration<URL> urls = cld.getResources(packageName.replace('.', '/'));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url.getProtocol().contains("jar")) {
                    String file = url.getFile();
                    file = file.substring(5, file.indexOf("!"));
                    return getAllClassInPackageJar(file, packageName);
                }
                String path = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8.name());
                if (path.startsWith("/") && path.contains(":")) {
                    path = path.substring(1);
                }
                File f = new File(path);
                if (!f.isDirectory()) {
                    continue;
                }
                for (String fileName : f.list()) {
                    if (!fileName.endsWith(".class")) {
                        continue;
                    }
                    classes.add(cld.loadClass((packageName + "." + fileName).replaceAll("\\.class", "")));
                }
            }
            return classes;
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    private static List<Class<?>> getAllClassInPackageJar(String jarFile, String packageName) throws Exception {
    	JarFile jar = null;
        try {
        	jar = new JarFile(URLDecoder.decode(jarFile, StandardCharsets.UTF_8.name()));
            ClassLoader cld = KeywordLoader.class.getClassLoader();
            Pattern pat = Pattern.compile(packageName.replace('.', '/') + "/[^/]+");
            ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                if (pat.matcher(name).matches()) {
                    classes.add(cld.loadClass(name.replace('/', '.').replaceAll("\\.class", "")));
                }
            }
            return classes;
        } catch (Exception ex) {
            return Collections.emptyList();
        } finally {
        	if (jar != null) {
        		jar.close();
        	}
        }
    }

    public static List<Class<?>> listClasses(String... packages) {
        try {
            return loadAllClass(packages);
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }
}
