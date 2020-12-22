package com.kms.katalon.core.util;

import java.util.HashMap;
import java.util.Map;

public class MimetypesFileTypeMapUtil {

    private MimetypesFileTypeMapUtil() {
        // Hide the default Constructor
    }

    private static Map<String, String> mimetypesFileTypeMap;

    static {
        mimetypesFileTypeMap = new HashMap<>();
        mimetypesFileTypeMap.put("aac", "audio/aac");
        mimetypesFileTypeMap.put("abw", "application/x-abiword");
        mimetypesFileTypeMap.put("arc", "application/octet-stream");
        mimetypesFileTypeMap.put("avi", "video/x-msvideo");
        mimetypesFileTypeMap.put("azw", "application/vnd.amazon.ebook");
        mimetypesFileTypeMap.put("bin", "application/octet-stream");
        mimetypesFileTypeMap.put("bz", "application/x-bzip");
        mimetypesFileTypeMap.put("bz2", "application/x-bzip2");
        mimetypesFileTypeMap.put("csh", "application/x-csh");
        mimetypesFileTypeMap.put("css", "text/css");
        mimetypesFileTypeMap.put("csv", "text/csv");
        mimetypesFileTypeMap.put("doc", "application/msword");
        mimetypesFileTypeMap.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        mimetypesFileTypeMap.put("eot", "application/vnd.ms-fontobject");
        mimetypesFileTypeMap.put("epub", "application/epub+zip");
        mimetypesFileTypeMap.put("gif", "image/gif");
        mimetypesFileTypeMap.put("htm", "text/html");
        mimetypesFileTypeMap.put("html", "text/html");
        mimetypesFileTypeMap.put("ico", "image/x-icon");
        mimetypesFileTypeMap.put("ics", "text/calendar");
        mimetypesFileTypeMap.put("jar", "application/java-archive");
        mimetypesFileTypeMap.put("jpeg", "image/jpeg");
        mimetypesFileTypeMap.put("jpg", "image/jpeg");
        mimetypesFileTypeMap.put("js", "application/javascript");
        mimetypesFileTypeMap.put("json", "application/json");
        mimetypesFileTypeMap.put("mid", "audio/midi");
        mimetypesFileTypeMap.put("midi", "audio/midi");
        mimetypesFileTypeMap.put("mpeg", "video/mpeg");
        mimetypesFileTypeMap.put("mpkg", "application/vnd.apple.installer+xml");
        mimetypesFileTypeMap.put("odp", "application/vnd.oasis.opendocument.presentation");
        mimetypesFileTypeMap.put("ods", "application/vnd.oasis.opendocument.spreadsheet");
        mimetypesFileTypeMap.put("odt", "application/vnd.oasis.opendocument.text");
        mimetypesFileTypeMap.put("oga", "audio/ogg");
        mimetypesFileTypeMap.put("ogv", "video/ogg");
        mimetypesFileTypeMap.put("ogx", "application/ogg");
        mimetypesFileTypeMap.put("otf", "font/otf");
        mimetypesFileTypeMap.put("png", "image/png");
        mimetypesFileTypeMap.put("pdf", "application/pdf");
        mimetypesFileTypeMap.put("ppt", "application/vnd.ms-powerpoint");
        mimetypesFileTypeMap.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        mimetypesFileTypeMap.put("rar", "application/x-rar-compressed");
        mimetypesFileTypeMap.put("rtf", "application/rtf");
        mimetypesFileTypeMap.put("sh", "application/x-sh");
        mimetypesFileTypeMap.put("svg", "image/svg+xml");
        mimetypesFileTypeMap.put("swf", "application/x-shockwave-flash");
        mimetypesFileTypeMap.put("tar", "application/x-tar");
        mimetypesFileTypeMap.put("tif", "image/tiff");
        mimetypesFileTypeMap.put("tiff", "image/tiff");
        mimetypesFileTypeMap.put("ts", "application/typescript");
        mimetypesFileTypeMap.put("ttf", "font/ttf");
        mimetypesFileTypeMap.put("vsd", "application/vnd.visio");
        mimetypesFileTypeMap.put("wav", "audio/x-wav");
        mimetypesFileTypeMap.put("weba", "audio/webm");
        mimetypesFileTypeMap.put("webm", "video/webm");
        mimetypesFileTypeMap.put("webp", "image/webp");
        mimetypesFileTypeMap.put("woff", "font/woff");
        mimetypesFileTypeMap.put("woff2", "font/woff2");
        mimetypesFileTypeMap.put("xhtml", "application/xhtml+xml");
        mimetypesFileTypeMap.put("xls", "application/vnd.ms-excel");
        mimetypesFileTypeMap.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        mimetypesFileTypeMap.put("xml", "application/xml");
        mimetypesFileTypeMap.put("xul", "application/vnd.mozilla.xul+xml");
        mimetypesFileTypeMap.put("zip", "application/zip");
        mimetypesFileTypeMap.put("3gp", "video/3gpp");
        mimetypesFileTypeMap.put("3g2", "video/3gpp2");
        mimetypesFileTypeMap.put("7z", "application/x-7z-compressed");
    }

    public static String getContentType(String extensionFile) {
        return mimetypesFileTypeMap.get(extensionFile);
    }

}
