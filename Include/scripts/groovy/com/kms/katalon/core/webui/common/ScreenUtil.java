package com.kms.katalon.core.webui.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Comparator;
import java.util.List;

import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ImageTarget;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.api.robot.Keyboard;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopKeyboard;
import org.sikuli.api.robot.desktop.DesktopMouse;

import com.kms.katalon.core.exception.StepFailedException;
import com.kms.katalon.core.webui.constants.StringConstants;
import com.kms.katalon.core.webui.driver.DriverFactory;
import com.kms.katalon.core.webui.driver.WebUIDriverType;
import com.kms.katalon.core.webui.util.FileUtil;

public class ScreenUtil {

    private ScreenRegion mainScreen;

    private double similarity = 0.75; // Default value

    public ScreenUtil() {
        mainScreen = new DesktopScreenRegion();
    }

    public ScreenUtil(double similarity) {
        this();
        this.similarity = similarity;
    }

    public void clickImage(String imagePath) throws Exception {
        ScreenRegion reg = findImage(imagePath);
        if (reg == null) {
            throw new Exception(StringConstants.COMM_EXC_CANNOT_RECOGNIZE_IMG_ON_SCREEN);
        }
        Mouse mouse = new DesktopMouse();
        mouse.click(reg.getCenter());
    }

    public boolean isImageExist(String imagePath) throws Exception {
        ScreenRegion reg = findImage(imagePath);
        return reg != null;
    }

    public void type(String string) {
        Keyboard keyboard = new DesktopKeyboard();
        keyboard.type(string);
    }

    public void typeOnImage(String imagePath, String text) throws Exception {
        clickImage(imagePath);
        type(text);
    }

    public boolean waitForImagePresent(String imagePath, int seconds) throws Exception {
        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            Target imageTarget = new ImageTarget(imgFile);
            imageTarget.setMinScore(this.similarity);
            ScreenRegion reg = mainScreen.wait(imageTarget, seconds * 1000);
            return reg != null;
        } else {
            throw new Exception(StringConstants.COMM_EXC_IMG_FILE_DOES_NOT_EXIST);
        }
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    public void authenticate(String userName, String password) throws Exception {
        // FIREFOX_DRIVER, IE_DRIVER, CHROME_DRIVER, SAFARI_DRIVER
        String usrImg = "";
        String passImg = "";
        String okImg = "";
        WebUIDriverType driver = (WebUIDriverType) DriverFactory.getExecutedBrowser();
        if (driver == null) {
            throw new StepFailedException(StringConstants.DRI_ERROR_MSG_NO_BROWSER_SET);
        }
        switch (driver) {
            case FIREFOX_DRIVER:
                usrImg = "auth_dlg_usr_win7_ff.png";
                passImg = "auth_dlg_pass_win7_ff.png";
                okImg = "auth_dlg_ok_win7_ff.png";
                break;
            case IE_DRIVER:
                usrImg = "auth_dlg_usr_win7_ie.png";
                passImg = "auth_dlg_pass_win7_ie.png";
                okImg = "auth_dlg_ok_win7_ie.png";
                break;
            case CHROME_DRIVER:
                usrImg = "auth_dlg_usr_win7_chrome.png";
                passImg = "auth_dlg_pass_win7_chrome.png";
                okImg = "auth_dlg_ok_win7_chrome.png";
                break;
            default:
                throw new Exception(StringConstants.COMM_EXC_BROWSER_IS_NOT_SUPPORTED);
        }
        File screenFolder = FileUtil.extractScreenFiles();
        typeOnImage(screenFolder + File.separator + usrImg, userName);
        typeOnImage(screenFolder + File.separator + passImg, password);
        clickImage(screenFolder + File.separator + okImg);
    }

    private ScreenRegion findImage(String imagePath) throws Exception {
        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            Target target = new ImageTarget(imgFile);
            target.setMinScore(this.similarity);
            ScreenRegion reg = this.mainScreen.find(target);
            return reg;
        } else {
            throw new Exception(StringConstants.COMM_EXC_IMG_FILE_DOES_NOT_EXIST);
        }
    }
    
    /**
     * Call {@link DesktopScreenRegion#capture()} to
     * capture the screenshot of the current screen
     * 
     * @return
     */
    public BufferedImage getScreenRegionImage() {
        return mainScreen.capture();
    }

    /**
     * Get all {@link ScreenRegion} on the screen matching the image specified
     * in imagePath. The returned array is sorted by matched score in descending
     * order
     * 
     * @param imagePath
     * Path to target image
     * @return A list of matched {@link ScreenRegion}
     * @throws Exception
     */
    public List<ScreenRegion> findImages(String imagePath) throws Exception {
        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            Target target = new ImageTarget(imgFile);
            target.setMinScore(this.similarity);
            List<ScreenRegion> regions = this.mainScreen.findAll(target);
            regions.sort(new Comparator<ScreenRegion>() {
                @Override
                public int compare(ScreenRegion o1, ScreenRegion o2) {
                    double reg1Score = o1.getScore();
                    double reg2Score = o2.getScore();
                    if (reg1Score > reg2Score)
                        return -1;
                    if (reg1Score < reg2Score)
                        return 1;
                    return 0;
                }
            });
            return regions;
        } else {
            throw new Exception(StringConstants.COMM_EXC_IMG_FILE_DOES_NOT_EXIST);
        }
    }
}
