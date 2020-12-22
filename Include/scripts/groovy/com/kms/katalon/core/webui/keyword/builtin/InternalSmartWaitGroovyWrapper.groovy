package com.kms.katalon.core.webui.keyword.builtin

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.webui.common.internal.SmartWait
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword

/**
 * This class should not be made visible to end-users
 */
@Action(value = "internalSmartWaitGroovyWrapper")
class InternalSmartWaitGroovyWrapper extends WebUIAbstractKeyword {

    @Override
    public Object execute(Object... params) {
        String webUiKeyword = (String) params[0];
        if(shouldDoSmartWait(webUiKeyword)) {
            boolean localSmartWaitEnabled = (boolean) Optional
                    .ofNullable(RunConfiguration.getExecutionProperties().get(RunConfiguration.LOCAL_SMART_WAIT_MODE))
                    .orElse(false);

            boolean globalSmartWaitEnabled = (boolean) Optional
                    .ofNullable(RunConfiguration.getExecutionProperties().get(RunConfiguration.GLOBAL_SMART_WAIT_MODE))
                    .orElse(false);

            if (localSmartWaitEnabled || globalSmartWaitEnabled) {
                SmartWait.doSmartWait();
            }
            return true;
        }
        return false;
    }

    boolean shouldDoSmartWait(String webUiKeyword) {
        // We can retrieve the list of keywords for which Smart Wait Function will be triggered here
        return webUiKeyword.startsWith("takeScreenshot");
    }
}
