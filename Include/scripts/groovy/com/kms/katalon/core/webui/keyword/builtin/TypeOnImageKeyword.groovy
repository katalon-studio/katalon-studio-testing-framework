package com.kms.katalon.core.webui.keyword.builtin

import java.text.MessageFormat

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.internal.PathUtil
import com.kms.katalon.core.webui.common.ScreenUtil
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.constants.StringConstants
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain

import groovy.transform.CompileStatic

@Action(value = "typeOnImage")
public class TypeOnImageKeyword extends WebUIAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        TestObject to = getTestObject(params[0])
        String text = (String) params[1]
        FailureHandling flowControl = (FailureHandling)(params.length > 2 && params[2] instanceof FailureHandling ? params[2] : RunConfiguration.getDefaultFailureHandling())
        typeOnImage(to,text,flowControl)
    }

    @CompileStatic
    public void typeOnImage(TestObject to, String text, FailureHandling flowControl) {
        String imagePath = to.getImagePath()
        WebUIKeywordMain.runKeyword({
            WebUiCommonHelper.checkTestObjectParameter(to)
            if (imagePath == null || imagePath.equals("")) {
                throw new IllegalArgumentException(StringConstants.KW_EXC_NO_IMAGE_FILE_PROP_IN_OBJ)
            }
            if (text == null) {
                throw new IllegalArgumentException(StringConstants.COMM_EXC_TEXT_IS_NULL)
            }
            logger.logDebug(MessageFormat.format(StringConstants.KW_LOG_INFO_TYPING_ON_IMG_X, imagePath))
            if (to.getUseRelativeImagePath()) {
                imagePath = PathUtil.relativeToAbsolutePath(imagePath, RunConfiguration.getProjectDir())
            }

            ScreenUtil screenUtil = new ScreenUtil()
            screenUtil.typeOnImage(imagePath, text)
            logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_TYPED_ON_IMG_X, imagePath))
        }, flowControl, true, (imagePath != null) ? MessageFormat.format(StringConstants.KW_MSG_CANNOT_TYPE_ON_IMG_X, imagePath) : StringConstants.KW_MSG_CANNOT_TYPE_ON_IMG)
    }
}
