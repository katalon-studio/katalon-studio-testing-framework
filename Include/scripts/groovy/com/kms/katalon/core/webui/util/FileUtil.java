package com.kms.katalon.core.webui.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import com.kms.katalon.core.exception.StepFailedException;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.testobject.TestObject;
import com.kms.katalon.core.util.internal.TestOpsUtil;
import com.kms.katalon.core.webui.common.WebUiCommonHelper;
import com.kms.katalon.core.webui.constants.StringConstants;
import com.kms.katalon.core.webui.driver.DriverFactory;
import com.kms.katalon.core.webui.driver.WebUIDriverType;
import com.assertthat.selenium_shutterbug.core.Capture;
import com.assertthat.selenium_shutterbug.core.Shutterbug;

public class FileUtil {

    private static final String SCREENSHOT_FOLDER = "resources/screen";

    private static final String KMS_IE_DRIVER_FOLDER = "resources/drivers/kmsie";

    private static final String AUTHENTICATION_FOLDER = "resources/authentication";

    private static final String EXTENSIONS_FOLDER_NAME = "resources/extensions";
    
    private static final KeywordLogger logger = KeywordLogger.getInstance(FileUtil.class);

    public static String takesScreenshot(String fileName, boolean isTestOpsVisionCheckPoint)
            throws Exception {
        if(!isTestOpsVisionCheckPoint) {
            takeDefaultScreenshot(fileName);
            return fileName;
        }
        
        if (StringUtils.isBlank(fileName)) {
            throw new IllegalArgumentException(StringConstants.KW_SCREENSHOT_EXCEPTION_FILENAME_NULL_EMPTY);
        }
        String savedFileName = TestOpsUtil.replaceTestOpsVisionFileName(fileName);
        BufferedImage image = takeViewportScreenshot();
        saveImage(image, savedFileName);
        return TestOpsUtil.getRelativePathForLog(savedFileName);
    }
    
    public static String takeFullPageScreenshot(String fileName, List<TestObject> ignoredElements,
            boolean isTestOpsVisionCheckPoint) throws Exception {
        if (isTestOpsVisionCheckPoint && StringUtils.isBlank(fileName)) {
            throw new IllegalArgumentException(StringConstants.KW_SCREENSHOT_EXCEPTION_FILENAME_NULL_EMPTY);
        }
        String savedFileName = isTestOpsVisionCheckPoint ? TestOpsUtil.replaceTestOpsVisionFileName(fileName)
                : fileName;
        WebDriver driver = DriverFactory.getWebDriver();
        Map<WebElement, String> states = hideElements(driver, ignoredElements);
        BufferedImage image = Shutterbug.shootPage(driver, Capture.FULL_SCROLL).getImage();
        restoreElements(driver, states);
        saveImage(image, savedFileName);
        return TestOpsUtil.getRelativePathForLog(savedFileName);
    }

    public static String takeElementScreenshot(String fileName, TestObject element, boolean isTestOpsVisionCheckPoint)
            throws Exception {
        if (isTestOpsVisionCheckPoint && StringUtils.isBlank(fileName)) {
            throw new IllegalArgumentException(StringConstants.KW_SCREENSHOT_EXCEPTION_FILENAME_NULL_EMPTY);
        }
        if (element == null) {
            throw new IllegalArgumentException(StringConstants.KW_SCREENSHOT_EXCEPTION_ELEMENT_NULL);
        }

        String savedFileName = isTestOpsVisionCheckPoint ? TestOpsUtil.replaceTestOpsVisionFileName(fileName)
                : fileName;
        WebDriver driver = DriverFactory.getWebDriver();
        WebElement capturedElement = WebUiCommonHelper.findWebElement(element, 0);
        BufferedImage image = Shutterbug.shootElement(driver, capturedElement).getImage();
        saveImage(image, savedFileName);
        return TestOpsUtil.getRelativePathForLog(savedFileName);
    }

    public static String takeAreaScreenshot(String fileName, Rectangle rect, boolean isTestOpsVisionCheckPoint)
            throws IOException, IllegalArgumentException {
        if (isTestOpsVisionCheckPoint && StringUtils.isBlank(fileName)) {
            throw new IllegalArgumentException(StringConstants.KW_SCREENSHOT_EXCEPTION_FILENAME_NULL_EMPTY);
        }
        if (rect == null) {
            throw new IllegalArgumentException(StringConstants.KW_SCREENSHOT_EXCEPTION_AREA_NULL);
        }

        String savedFileName = isTestOpsVisionCheckPoint ? TestOpsUtil.replaceTestOpsVisionFileName(fileName)
                : fileName;
        WebDriver driver = DriverFactory.getWebDriver();
        BufferedImage image = Shutterbug.shootPage(driver, Capture.VIEWPORT).getImage();
        if ((rect.x + rect.width) > image.getWidth() || (rect.y + rect.height) > image.getHeight()) {
            throw new IllegalArgumentException(StringConstants.KW_SCREENSHOT_EXCEPTION_AREA_LARGER);
        }

        saveImage(image.getSubimage(rect.x, rect.y, rect.width, rect.height), savedFileName);
        return TestOpsUtil.getRelativePathForLog(savedFileName);
    }

    private static Map<WebElement, String> hideElements(WebDriver driver, List<TestObject> testObjects) throws Exception {
        if (testObjects == null || driver == null) {
            return null;
        }

        Map<WebElement, String> preState = new HashMap<>();
        JavascriptExecutor jsx = (JavascriptExecutor)driver;
        int counter = 0;
        for (TestObject to : testObjects) {
            try {
                WebElement element = WebUiCommonHelper.findWebElement(to, 0);
                String state = jsx.executeScript("return arguments[0].style.visibility", element).toString();
                preState.put(element, state);
                jsx.executeScript("arguments[0].style.visibility = 'hidden'", element);
                ++counter;
            } catch (Exception e) {
                logger.logInfo(MessageFormat.format(StringConstants.KW_LOG_INFO_SCREENSHOT_FULLPAGE_FAIL_HIDE_OBJECT, to.getObjectId()));
            }
        }
        
        logger.logInfo(MessageFormat.format(StringConstants.KW_LOG_INFO_SCREENSHOT_FULLPAGE_HIDDEN_COUNTER, counter));
        return preState;
    }

    private static void restoreElements(WebDriver driver, Map<WebElement, String> states) throws Exception {
        if (states == null || driver == null) {
            return;
        }
        
        JavascriptExecutor jsx = (JavascriptExecutor) driver;
        for (WebElement e : states.keySet()) {
            jsx.executeScript("arguments[0].style.visibility = '" + states.get(e) + "'", e);
        }
    }

    public static File extractScreenFiles() throws Exception {
        String path = FileUtil.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        path = URLDecoder.decode(path, "utf-8");
        File jarFile = new File(path);
        if (jarFile.isFile()) {
            JarFile jar = new JarFile(jarFile);
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String name = jarEntry.getName();
                if (name.startsWith(SCREENSHOT_FOLDER) && name.endsWith(".png")) {
                    String mappingFileName = name.replace(SCREENSHOT_FOLDER + "/", "");
                    File tmpFile = new File(System.getProperty("java.io.tmpdir") + mappingFileName);
                    if (tmpFile.exists()) {
                        tmpFile.delete();
                    }
                    FileOutputStream fos = new FileOutputStream(tmpFile);
                    IOUtils.copy(jar.getInputStream(jarEntry), fos);
                    fos.flush();
                    fos.close();
                }
            }
            jar.close();
            return new File(System.getProperty("java.io.tmpdir"));
        } else { // Run with IDE
            File folder = new File(path + "../" + SCREENSHOT_FOLDER);
            return folder;
        }
    }

    public static File getAuthenticationDirectory() throws IOException {
        String path = FileUtil.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        path = URLDecoder.decode(path, "utf-8");
        File jarFile = new File(path);
        if (jarFile.isFile()) {
            String kmsIePath = jarFile.getParentFile().getParentFile().getAbsolutePath() + "/configuration/"
                    + AUTHENTICATION_FOLDER;
            return new File(kmsIePath);
        } else { // Run with IDE
            File folder = new File(path + "../" + AUTHENTICATION_FOLDER);
            return folder;
        }
    }

    /**
     * Return a file representing directory resources/extensions
     * 
     * @return {@link File}
     * @throws IOException
     */
    public static File getExtensionsDirectory() throws IOException {
        String path = FileUtil.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        path = URLDecoder.decode(path, "utf-8");
        File jarFile = new File(path);
        if (jarFile.isFile()) {
            String kmsIePath = jarFile.getParentFile().getParentFile().getAbsolutePath() + "/configuration/"
                    + EXTENSIONS_FOLDER_NAME;
            return new File(kmsIePath);
        } else { // Run with IDE
            File folder = new File(path + ".." + File.separator + EXTENSIONS_FOLDER_NAME);
            return folder;
        }
    }

    public static String getRelativePath(String path, String baseDir) {
        String relativePath = new File(baseDir).toPath().relativize(new File(path).toPath()).toString();
        return FilenameUtils.separatorsToUnix(relativePath);
    }

    public static boolean isInBaseFolder(String absolutePath, String absoluteBaseDir) {
        File file = new File(absolutePath);
        File baseDir = new File(absoluteBaseDir);
        return file.getAbsolutePath().startsWith(baseDir.getAbsolutePath());
    }
    
    private static BufferedImage takeViewportScreenshot() throws Exception {
        WebDriver driver = DriverFactory.getWebDriver();
        BufferedImage image = Shutterbug.shootPage(driver, Capture.VIEWPORT).getImage();
        if (WebUIDriverType.IOS_DRIVER.getName().equals(DriverFactory.getExecutedBrowser().getName())) {
            return removeBrowserAndOSStatusBar(driver, image);
        }
        return image;
    }
    
    private static BufferedImage removeBrowserAndOSStatusBar(WebDriver driver, BufferedImage image) {
        int viewportWidth = WebUiCommonHelper.getViewportWidth(driver) * 2;
        int viewportHeight = WebUiCommonHelper.getViewportHeight(driver) * 2;
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        if (viewportHeight != image.getHeight() || viewportWidth != image.getWidth()) {
            return image.getSubimage(imageWidth - viewportWidth, imageHeight - viewportHeight, viewportWidth, viewportHeight);
        }
        return image;
    }
    
    private static void saveImage(BufferedImage image, String fileName) throws IOException, SecurityException {
        File file = new File(fileName);
        TestOpsUtil.ensureDirectory(file, true);
        ImageIO.write(image, TestOpsUtil.DEFAULT_IMAGE_EXTENSION, file);
    }
    
    private static void takeDefaultScreenshot(String fileName) throws WebDriverException, StepFailedException, IOException {
        FileUtils.copyFile(((TakesScreenshot) DriverFactory.getWebDriver()).getScreenshotAs(OutputType.FILE),
                new File(fileName), false);
    }
}
