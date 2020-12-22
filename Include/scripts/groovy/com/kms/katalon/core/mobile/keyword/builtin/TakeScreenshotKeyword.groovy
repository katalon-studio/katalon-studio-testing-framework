package com.kms.katalon.core.mobile.keyword.builtin

import java.awt.Color
import java.awt.image.BufferedImage

import javax.imageio.ImageIO

import org.apache.commons.lang.StringUtils

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.mobile.constants.StringConstants
import com.kms.katalon.core.mobile.helper.MobileScreenCaptor
import com.kms.katalon.core.mobile.keyword.internal.MobileAbstractKeyword
import com.kms.katalon.core.mobile.keyword.internal.MobileKeywordMain
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.internal.PathUtil
import com.kms.katalon.core.util.internal.TestOpsUtil

import groovy.transform.CompileStatic
import io.appium.java_client.AppiumDriver

@Action(value = "takeScreenshot")
public class TakeScreenshotKeyword extends MobileAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        if (!isValidData(params)) {
            return null
        }
        
        String fileName = ((String)params[0])
        boolean isTestOpsVisionCheckPoint = (boolean)params[3]
        if (isTestOpsVisionCheckPoint) {
            fileName = TestOpsUtil.replaceTestOpsVisionFileName(fileName.trim())
        } else if (fileName == null){
            fileName = defaultFileName()
        }
        List<TestObject> ignoredElements = (List<TestObject>)params[1]
        Color hidingColor = (Color)params[2]
        FailureHandling flow = params[4] == null ? RunConfiguration.getDefaultFailureHandling()
                                    : (FailureHandling)params[4]
        return takeScreenshot(fileName, ignoredElements, hidingColor, isTestOpsVisionCheckPoint, flow)
    }

    private boolean isValidData(Object[] params) {
        if (params.length != 5) {
            return false
        }

        if (params[0] != null && !(params[0] instanceof String)) {
            return false;
        }
        
        if (params[1] != null && !(params[1] instanceof List<TestObject>)) {
            return false;
        }
        
        if (params[2] != null && !(params[2] instanceof Color)) {
            return false;
        }
        
        if (params[3] != null && !(params[3] instanceof Boolean)) {
            return false;
        }
        
        if (params[4] != null && !(params[4] instanceof FailureHandling)) {
            return false;
        }

        return true;
    }

    private String defaultFileName() {
        return logger.getLogFolderPath() + File.separator + System.currentTimeMillis() + ".png";
    }

    @CompileStatic
    public String takeScreenshot(String fileName, List<TestObject> ignoredElements,
                Color hidingColor, boolean isTestOpsVisionCheckPoint,
                FailureHandling flowControl)
                throws StepFailedException {
        return MobileKeywordMain.runKeyword({
            AppiumDriver driver = getAnyAppiumDriver()
            String context = driver.getContext()
            
            try {
                internalSwitchToNativeContext(driver)
                if (isTestOpsVisionCheckPoint) {
                    if (StringUtils.isBlank(fileName)) {
                        MobileKeywordMain.stepFailed(StringConstants.KW_MSG_SCREENSHOT_EXCEPTION_FILENAME_NULL_EMPTY, flowControl, null, true)
                    }
                    
                    BufferedImage screenshot = MobileScreenCaptor.takeViewportScreenshot(driver, ignoredElements, hidingColor)
                    File saveFile = new File(fileName)
                    PathUtil.ensureDirectory(saveFile, true)
                    ImageIO.write(screenshot, TestOpsUtil.DEFAULT_IMAGE_EXTENSION, saveFile)
                } else {
                    MobileScreenCaptor.takeScreenshot(driver, fileName)
                }
                
                Map<String, String> attributes = new HashMap<>()
                attributes.put(StringConstants.XML_LOG_ATTACHMENT_PROPERTY, PathUtil.getRelativePathForLog(fileName));
                logger.logPassed(StringConstants.KW_LOG_PASSED_SCREENSHOT_IS_TAKEN, attributes)
                return fileName
            } finally {
                driver.context(context)
            }
        }, flowControl, false, StringConstants.KW_MSG_UNABLE_TO_TAKE_SCREENSHOT)
    }
}
