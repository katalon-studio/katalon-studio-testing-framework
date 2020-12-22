package com.kms.katalon.core.webui.driver;

import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.webui.constants.CoreWebuiMessageConstants;

public class DriverConfigurationProvider implements IDriverConfigurationProvider {
    private KeywordLogger logger;

    DriverConfigurationProvider(KeywordLogger logger) {
        this.logger = logger;
    }

    @Override
    public int getActionDelayInMilisecond() {
        int actionDelay = 0;
        final Map<String, Object> executionGeneralProperties = RunConfiguration.getExecutionGeneralProperties();
        if (executionGeneralProperties.containsKey(DriverFactory.ACTION_DELAY)) {
            actionDelay = RunConfiguration.getIntProperty(DriverFactory.ACTION_DELAY, executionGeneralProperties);
        }

        if (executionGeneralProperties.containsKey(DriverFactory.USE_ACTION_DELAY_IN_SECOND)) {
            TimeUnit useInSec = TimeUnit.valueOf((String) RunConfiguration.getExecutionGeneralProperties().get(DriverFactory.USE_ACTION_DELAY_IN_SECOND));
            if (useInSec.equals(TimeUnit.SECONDS)) {
                actionDelay = actionDelay * 1000;
            }
        }

        if (RunConfiguration.getPort() > 0) {
            logger.logInfo(MessageFormat.format(CoreWebuiMessageConstants.KW_MSG_ACTION_DELAY_X, actionDelay));
        }
        return actionDelay;
    }
}
