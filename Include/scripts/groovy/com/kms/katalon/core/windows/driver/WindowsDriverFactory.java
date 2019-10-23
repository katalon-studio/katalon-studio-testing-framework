package com.kms.katalon.core.windows.driver;

import java.io.IOException;
import java.net.Proxy;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.http.HttpClient;
import org.openqa.selenium.remote.http.HttpClient.Factory;

import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.util.internal.JsonUtil;
import com.kms.katalon.core.util.internal.ProxyUtil;
import com.kms.katalon.core.windows.constants.WindowsDriverConstants;
import com.thoughtworks.selenium.SeleniumException;

import io.appium.java_client.MobileCommand;
import io.appium.java_client.remote.AppiumCommandExecutor;
import io.appium.java_client.windows.WindowsDriver;

public class WindowsDriverFactory {

    private static KeywordLogger logger = KeywordLogger.getInstance(WindowsDriverFactory.class);

    public static final String DESIRED_CAPABILITIES_PROPERTY = "desiredCapabilities";

    public static final String WIN_APP_DRIVER_PROPERTY = "winAppDriverUrl";

    private static WindowsSession windowsSession;

    public static WindowsDriver<WebElement> getWindowsDriver() {
        return windowsSession.getRunningDriver();
    }

    public static WindowsSession getWindowsSession() {
        return windowsSession;
    }

    @SuppressWarnings("unchecked")
    public static WindowsDriver<WebElement> startApplication(String appFile)
            throws SeleniumException, IOException, URISyntaxException {
        Map<String, Object> userConfigProperties = RunConfiguration.getDriverPreferencesProperties("Windows");
        if (userConfigProperties == null) {
            userConfigProperties = new HashMap<String, Object>();
        }

        String remoteAddressURLAsString = (String) userConfigProperties.getOrDefault(WIN_APP_DRIVER_PROPERTY,
                WindowsDriverConstants.DEFAULT_WIN_APP_DRIVER_URL);
        URL remoteAddressURL = new URL(remoteAddressURLAsString);
        if (!remoteAddressURLAsString.equals(WindowsDriverConstants.DEFAULT_WIN_APP_DRIVER_URL)) {
            logger.logInfo(String.format("Starting application %s on the test machine at address %s", appFile,
                    remoteAddressURL.toString()));
            logger.logRunData(WIN_APP_DRIVER_PROPERTY, remoteAddressURL.toString());
        } else {
            logger.logInfo(String.format("Starting application %s on the local machine at address %s", appFile,
                    remoteAddressURL.toString()));
        }

        Object desiredCapabilitiesAsObject = userConfigProperties.getOrDefault(DESIRED_CAPABILITIES_PROPERTY, null);
        DesiredCapabilities desiredCapabilities = (desiredCapabilitiesAsObject instanceof Map)
                ? new DesiredCapabilities((Map<String, Object>) desiredCapabilitiesAsObject)
                : new DesiredCapabilities();
        logger.logRunData(DESIRED_CAPABILITIES_PROPERTY, JsonUtil.toJson(desiredCapabilities.toJson(), false));

        Proxy proxy = ProxyUtil.getProxy(RunConfiguration.getProxyInformation());
        return startApplication(remoteAddressURL, appFile, desiredCapabilities, proxy).getRunningDriver();
    }

    public static WindowsSession startApplication(URL remoteAddressURL, String appFile,
            DesiredCapabilities initCapabilities, Proxy proxy)
            throws SeleniumException, IOException, URISyntaxException {
        try {
            windowsSession = new WindowsSession(remoteAddressURL, appFile, initCapabilities, proxy);

            DesiredCapabilities desiredCapabilities = new DesiredCapabilities(initCapabilities);
            desiredCapabilities.setCapability("app", appFile);
            WindowsDriver<WebElement> windowsDriver = newWindowsDriver(remoteAddressURL, desiredCapabilities, proxy);
            windowsDriver.getWindowHandle();

            windowsSession.setApplicationDriver(windowsDriver);
            return windowsSession;
        } catch (WebDriverException e) {
            if (!(e instanceof NoSuchWindowException) && !(e instanceof SessionNotCreatedException)) {
                throw e;
            }
            if (e.getMessage() != null && e.getMessage().contains("The system cannot find the file specified")) {
                // appFile is not correct
                throw e;
            }
            if ("Root".equals(appFile)) {
                throw e;
            }
            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
            desiredCapabilities.setCapability("app", "Root");
            WindowsDriver<WebElement> desktopDriver = newWindowsDriver(remoteAddressURL, desiredCapabilities, proxy);

            long startTime = System.currentTimeMillis();
            WebElement webElement = null;
            while (System.currentTimeMillis() - startTime <= 20000L) {
                try {
                    webElement = desktopDriver.findElementByTagName("Window");
                } catch (NoSuchElementException ignored) {}
                break;
            }
            if (webElement == null) {
                throw e;
            }

            String appTopLevelWindow = webElement.getAttribute("NativeWindowHandle");

            if (StringUtils.isNotEmpty(appTopLevelWindow)) {
                DesiredCapabilities retryDesiredCapabilities = new DesiredCapabilities(initCapabilities);
                retryDesiredCapabilities.setCapability("appTopLevelWindow",
                        Integer.toHexString(Integer.parseInt(appTopLevelWindow)));
                WindowsDriver<WebElement> windowsDriver = newWindowsDriver(remoteAddressURL, retryDesiredCapabilities,
                        proxy);

                windowsSession.setApplicationDriver(windowsDriver);
                windowsSession.setDesktopDriver(desktopDriver);
                return windowsSession;
            }
            throw e;
        }
    }

    private static WindowsDriver<WebElement> newWindowsDriver(URL remoteAddressURL,
            DesiredCapabilities desiredCapabilities, Proxy proxy) throws IOException, URISyntaxException {
        if (remoteAddressURL != null) {
            return new WindowsDriver<WebElement>(getAppiumExecutorForRemoteDriver(remoteAddressURL, proxy),
                    desiredCapabilities);
        } else {
            return new WindowsDriver<WebElement>(desiredCapabilities);
        }
    }

    public static AppiumCommandExecutor getAppiumExecutorForRemoteDriver(URL remoteWebServerUrl, Proxy proxy)
            throws IOException, URISyntaxException {
        Factory clientFactory = getClientFactoryForRemoteDriverExecutor(proxy);
        AppiumCommandExecutor executor = new AppiumCommandExecutor(MobileCommand.commandRepository, remoteWebServerUrl,
                clientFactory);
        return executor;
    }

    private static Factory getClientFactoryForRemoteDriverExecutor(Proxy proxy) {
        return new Factory() {

            private org.openqa.selenium.remote.internal.OkHttpClient.Factory factory;
            {
                factory = new org.openqa.selenium.remote.internal.OkHttpClient.Factory();
            }

            @Override
            public HttpClient createClient(URL url) {
                return Factory.super.createClient(url);
            }

            @Override
            public void cleanupIdleClients() {
                factory.cleanupIdleClients();
            }

            @Override
            public org.openqa.selenium.remote.internal.OkHttpClient.Builder builder() {
                return factory.builder().proxy(proxy);
            }
        };
    }
}
