package com.kms.katalon.core.appium.driver;

import java.io.IOException;
import java.text.MessageFormat;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kms.katalon.core.appium.constants.AppiumStringConstants;
import com.kms.katalon.core.constants.StringConstants;
import com.kms.katalon.core.logging.KeywordLogger;

public class AppiumRequestService {
    
    private static final String APPIUM_URL_STATUS_PATH = "/status";

    private final KeywordLogger logger = KeywordLogger.getInstance(this.getClass());

    private String appiumServerUrl;

    public AppiumRequestService(String appiumServerUrl) {
        this.appiumServerUrl = appiumServerUrl;
    }

    private String sendGetRequest(String url) throws UnsupportedOperationException, IOException {
        try (CloseableHttpClient client = HttpClientBuilder.create().build();
                CloseableHttpResponse response = client.execute(new HttpGet(url))) {
            return IOUtils.toString(response.getEntity().getContent(), StringConstants.DF_CHARSET);
        }
    }

    public void logAppiumInfo() {
        try {
            String appiumJsonResponseStatus = sendGetRequest(appiumServerUrl + APPIUM_URL_STATUS_PATH);

            JsonObject parser = new JsonParser().parse(appiumJsonResponseStatus).getAsJsonObject();
            String appiumVersion = parser.getAsJsonObject("value")
                    .getAsJsonObject("build")
                    .getAsJsonPrimitive("version")
                    .getAsString();

            logger.logRunData(AppiumStringConstants.XML_LOG_APPIUM_VERSION, appiumVersion);
        } catch (UnsupportedOperationException | IOException e) {
            logger.logWarning(MessageFormat.format(AppiumStringConstants.MSG_UNABLE_TO_GET_APPIUM_STATUS,
                    e.getMessage()));
        }
    }
}
