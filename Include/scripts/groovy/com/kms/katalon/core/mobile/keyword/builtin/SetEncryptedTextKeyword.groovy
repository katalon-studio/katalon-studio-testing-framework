package com.kms.katalon.core.mobile.keyword.builtin

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.helper.KeywordHelper
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.mobile.constants.StringConstants
import com.kms.katalon.core.mobile.helper.MobileCommonHelper
import com.kms.katalon.core.mobile.keyword.internal.MobileAbstractKeyword
import com.kms.katalon.core.mobile.keyword.internal.MobileKeywordMain
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.selenium.util.SeleniumKeysUtil
import com.kms.katalon.util.CryptoUtil

import groovy.transform.CompileStatic
import java.text.MessageFormat
import org.openqa.selenium.WebElement

@Action(value = "setEncryptedText")
public class SetEncryptedTextKeyword extends MobileAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object... params) {
        TestObject to = getTestObject(params[0])
        String text = (String) params[1]
        int timeout = (int) params[2]
        FailureHandling flowControl = (FailureHandling)(params.length > 3 && params[3] instanceof FailureHandling ? params[3] : RunConfiguration.getDefaultFailureHandling())
        setEncryptedText(to, text, timeout, flowControl)
    }
    
    @CompileStatic
    public void setEncryptedText(TestObject to, String encryptedText, int timeout, FailureHandling flowControl) {
        MobileKeywordMain.runKeyword({
            KeywordHelper.checkTestObjectParameter(to)
            timeout = KeywordHelper.checkTimeout(timeout)
            
            WebElement element = findElement(to, timeout * 1000)
            if (element == null) {
                MobileKeywordMain.stepFailed(MessageFormat.format(StringConstants.KW_MSG_OBJ_NOT_FOUND, to.getObjectId()), flowControl, null, true)
                return
            }
            
            if (encryptedText == null) {
                MobileKeywordMain.stepFailed(MessageFormat.format(StringConstants.KW_MSG_FAILED_PARAM_X_CANNOT_BE_NULL, "encryptedText"), flowControl, null, true)
                return
            }
            
            CryptoUtil.CrytoInfo cryptoInfo = CryptoUtil.getDefault(encryptedText)
            String rawText = CryptoUtil.decode(cryptoInfo)
            
            element.clear()
            element.sendKeys(rawText)
            logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_ENCRYPTED_TEXT_HAS_BEEN_SET_TO_ELEMENT, encryptedText, to.getObjectId()))
        }, flowControl, true,
            (to != null)
                ? MessageFormat.format(StringConstants.KW_MSG_FAILED_SET_ELEMENT_ENCRYPTED_TEXT, to.getObjectId())
                : StringConstants.KW_MSG_FAILED_TO_SET_ELEMENT_TEXT)
    }
}
