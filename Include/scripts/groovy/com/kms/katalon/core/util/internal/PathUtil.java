package com.kms.katalon.core.util.internal;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.regex.Pattern;

import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.constants.StringConstants;

public class PathUtil {
    private static final String relativeSeparator = "/";
    private static final String windowSeparator = "\\";

    private PathUtil() {
        // Disable default constructor
    }

    public static String absoluteToRelativePath(String absolutePath, String projectFolderPath) {
        return PathUtil.getRelativePath(absolutePath, projectFolderPath);
    }

    public static String relativeToAbsolutePath(String relativePath, String projectFolderPath) {
        Path path = new File(relativePath).toPath();
        if (path.isAbsolute()) {
            return path.toAbsolutePath().toString();
        } else {
            Path projectPath = new File(projectFolderPath).toPath();
            return projectPath.resolve(path).normalize().toString();
        }
    }

    private static String getRelativePath(String targetPath, String basePath) {
        // We need the -1 argument to split to make sure we get a trailing
        // "" token if the base ends in the path separator and is therefore
        // a directory. We require directory paths to end in the path
        // separator -- otherwise they are indistinguishable from files.
        String[] base = basePath.replace(windowSeparator, relativeSeparator)
                .split(Pattern.quote(relativeSeparator), -1);
        String[] target = targetPath.replace(windowSeparator, relativeSeparator).split(
                Pattern.quote(relativeSeparator), 0);

        // First get all the common elements. Store them as a string,
        // and also count how many of them there are.
        String common = "";
        int commonIndex = 0;
        for (int i = 0; i < target.length && i < base.length; i++) {
            if (target[i].equals(base[i])) {
                common += target[i] + relativeSeparator;
                commonIndex++;
            } else
                break;
        }

        if (commonIndex == 0) {
            // Whoops -- not even a single common path element. This most
            // likely indicates differing drive letters, like C: and D:.
            // These paths cannot be relativized. Return the target path.
            return targetPath;
            // This should never happen when all absolute paths
            // begin with / as in *nix.
        }

        String relative = "";
        if (base.length == commonIndex) {
            // Comment this out if you prefer that a relative path not start
            // with ./
            // relative = "." + relativeSeperator;
        } else {
            int numDirsUp = base.length - commonIndex;
            // The number of directories we have to backtrack is the length of
            // the base path MINUS the number of common path elements, minus
            // one because the last element in the path isn't a directory.
            for (int i = 1; i <= (numDirsUp); i++) {
                relative += ".." + relativeSeparator;
            }
        }
        relative += targetPath.replace(windowSeparator, relativeSeparator).substring(common.length());

        return relative;
    }

    /**
     * Returns an instance of {@link URL} that parsed from the given <code>rawUrl</code>
     * 
     * @param rawUrl
     *            input of an URL.
     *            <ul>
     *            <li>https://www.google.com</li>
     *            <li>file:///D:/Development/index.html</li>
     *            <li>ftp://ftp.google.com/</li>
     *            </ul>
     * @param defaultProtocol
     *            : In case the given <code>rawUrl</code> doesn't contain its protocol, system will use this parameter
     *            instead. </p>Example: <li><code>rawUrl = google.com</code> and <code>defaultProtocol = https</code>
     *            then <code>URL = https://www.google.com</code></li>
     * 
     * @return an instance of {@link URL}
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    public static URL getUrl(String rawUrl, String defaultProtocol) throws MalformedURLException, URISyntaxException {
        URL url = null;
        try {
            url = new URL(rawUrl);
        } catch (MalformedURLException e) {
            try {
                url = new URI(rawUrl).toURL();
            } catch (IllegalArgumentException ex) {
                url = new URI(defaultProtocol + "://" + rawUrl).toURL();
            } catch (URISyntaxException ex) {
                url = Paths.get(rawUrl).toUri().toURL();
            }
        }
        return url;
    }
    
    
    /** Get relative path for xml log. The path will start from the report folder.
     * @param fileName Absolute path of the file.
     * @return relative path.
     */
    public static String getRelativePathForLog(String fileName) {
        String reportPath = RunConfiguration.getReportFolder();
        if (!reportPath.endsWith(File.separator)) {
            reportPath = reportPath + File.separator;
        }
        
        int reportPathIndex = fileName.indexOf(reportPath);
        if (reportPathIndex != 0) {
            return fileName;
        }
        
        return fileName.replace(reportPath, "");
    }
    
    /**
     * Create path for file if not existed. If <b>isFile</b> create path to parent directory of the file.
     * Or else create directory for the path.
     * @param file File whose parent need to be created.
     * @param isFile <b>true</b> if the path in <b><i>file</i></b> file. <b>false</b> if path is directory.
     * @return File whose path has been created.
     * @throws IOException 
     */
    public static File ensureDirectory(File file, boolean isFile) throws IOException {
        if (file == null) {
            throw new IOException(StringConstants.UTIL_EXC_FILE_NOT_NULL);
        }

        if (file.exists()) {
            return file;
        }
        
        if (!isValidFile(file)) {
            throw new IOException(
                    MessageFormat.format(StringConstants.UTIL_EXC_FILE_PATH_INVALID, file.getAbsolutePath()));
        }
        
        if (isFile) {
            File parent = file.getParentFile();
            if (parent == null || parent.exists()) {
                return file;
            }
            parent.mkdirs();
        } else {
            file.mkdirs();
        }

        return file;
    }
    
    private static boolean isValidFile(File file) {
        try {
            Paths.get(file.getAbsolutePath());
            return true;
        } catch (InvalidPathException e) {
            return false;
        }
    }
    
}
