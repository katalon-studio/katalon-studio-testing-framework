package com.kms.katalon.core.mobile.keyword.builtin

import groovy.transform.CompileStatic

import java.nio.file.InvalidPathException
import java.text.MessageFormat

import org.apache.commons.lang3.StringUtils

import com.google.common.base.Preconditions
import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.KeywordMain
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.mobile.constants.CoreMobileMessageConstants
import com.kms.katalon.core.mobile.constants.StringConstants
import com.kms.katalon.core.mobile.keyword.internal.MobileAbstractKeyword
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.mobile.keyword.internal.MobileKeywordMain
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.util.internal.PathUtil

@Action(value = "startApplication")
public class StartApplicationKeyword extends MobileAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        String appFile = (String) params[0]
        boolean uninstallAfterCloseApp = (boolean) params[1]
        FailureHandling flowControl = (FailureHandling)(params.length > 2 && params[2] instanceof FailureHandling ? params[2] : RunConfiguration.getDefaultFailureHandling())
        startApplication(appFile,uninstallAfterCloseApp,flowControl)
    }

    @CompileStatic
    public void startApplication(String appFile, boolean uninstallAfterCloseApp, FailureHandling flowControl) throws StepFailedException {
        MobileKeywordMain.runKeyword({
            Preconditions.checkArgument(StringUtils.isNotEmpty(appFile), CoreMobileMessageConstants.KW_MSG_APP_FILE_MISSING);
            String applicationFileAbs = getAbsolutePath(appFile)
            logger.logDebug(MessageFormat.format(StringConstants.KW_LOG_INFO_STARTING_APP_AT, StringUtils.defaultString(applicationFileAbs)))
            MobileDriverFactory.startMobileDriver(applicationFileAbs, uninstallAfterCloseApp)
            logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_START_APP_AT,  StringUtils.defaultString(applicationFileAbs)))
        }, flowControl, false, MessageFormat.format(StringConstants.KW_MSG_UNABLE_TO_START_APP_AT, StringUtils.defaultString(appFile)))
    }
    
    private String getAbsolutePath(String filePath) {
        try {
            String absFilePath = PathUtil.relativeToAbsolutePath(filePath, RunConfiguration.getProjectDir())
            File absFile = new File(absFilePath)
            return absFile.isFile() && absFile.exists() ? absFilePath : filePath
        } catch (InvalidPathException e) {
            return filePath
        }
    }    
}
