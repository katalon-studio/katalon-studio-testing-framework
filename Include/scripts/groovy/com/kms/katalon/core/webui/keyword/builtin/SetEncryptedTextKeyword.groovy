package com.kms.katalon.core.webui.keyword.builtin;

import groovy.transform.CompileStatic
import java.text.MessageFormat
import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.internal.Action;
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.constants.StringConstants
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.util.CryptoUtil

@Action(value = StringConstants.SET_ENCRYPTED_TEXT_KEYWORD)
public class SetEncryptedTextKeyword extends WebUIAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object... params) {
        TestObject to = getTestObject(params[0])
        String encryptedText = (String) params[1]
        FailureHandling flowControl = (FailureHandling)(params.length > 2 && params[2] instanceof FailureHandling ? params[2] : RunConfiguration.getDefaultFailureHandling())
        return setEncryptedText(to, encryptedText, flowControl)
    }
    
    @CompileStatic
    public void setEncryptedText(TestObject to, String encryptedText, FailureHandling flowControl) {
        WebUIKeywordMain.runKeyword({
            boolean isSwitchIntoFrame = false
            try {
                WebUiCommonHelper.checkTestObjectParameter(to)
                logger.logDebug(StringConstants.KW_LOG_INFO_CHECKING_TXT)
                if (encryptedText == null) {
                    throw new IllegalArgumentException(StringConstants.KW_EXC_TXT_IS_NULL)
                }
                CryptoUtil.CrytoInfo cryptoInfo = CryptoUtil.getDefault(encryptedText)
                String rawText = CryptoUtil.decode(cryptoInfo)
                
                isSwitchIntoFrame = WebUiCommonHelper.switchToParentFrame(to)
                WebElement webElement = WebUIAbstractKeyword.findWebElement(to)
                
                logger.logDebug(MessageFormat.format(StringConstants.KW_LOG_INFO_CLEARING_OBJ_TXT, to.getObjectId()))
                webElement.clear()
                
                logger.logDebug(MessageFormat.format(StringConstants.KW_LOG_INFO_SETTING_OBJ_TXT_TO_ENCRYPTED_VAL, to.getObjectId()))
                webElement.sendKeys(rawText)
                
                logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_ENCRYPTED_TXT_IS_SET_ON_OBJ, to.getObjectId()))
            } finally {
                if (isSwitchIntoFrame) {
                    WebUiCommonHelper.switchToDefaultContent()
                }
            }
        }, flowControl, true, (to != null) ? MessageFormat.format(StringConstants.KW_MSG_CANNOT_SET_ENCRYPTED_TEXT_FOR_OBJECT, to.getObjectId())
        : StringConstants.KW_MSG_CANNOT_SET_TXT)
    }
}
