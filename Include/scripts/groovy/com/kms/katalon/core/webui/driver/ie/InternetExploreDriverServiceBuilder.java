package com.kms.katalon.core.webui.driver.ie;

import org.openqa.selenium.ie.InternetExplorerDriverService;

import com.google.common.collect.ImmutableList;

public class InternetExploreDriverServiceBuilder extends InternetExplorerDriverService.Builder {
    private static final String LOG_FILE_ARGUMENT = "--log-file";

    /**
     * Override this method to avoid issue https://github.com/seleniumhq/selenium-google-code-issue-archive/issues/6279
     */
    @Override
    protected ImmutableList<String> createArgs() {
        ImmutableList<String> parentArgs = super.createArgs();
        ImmutableList.Builder<String> argsBuilder = ImmutableList.builder();
        for (String parentArg : parentArgs) {
            if (!parentArg.startsWith(LOG_FILE_ARGUMENT)) {
                argsBuilder.add(parentArg);
                continue;
            }
            argsBuilder.add(String.format(LOG_FILE_ARGUMENT + "=\"%s\"", "\"" + getLogFile().getAbsolutePath() + "\""));
        }
        return argsBuilder.build();
    }
}