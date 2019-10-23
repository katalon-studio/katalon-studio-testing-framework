package com.kms.katalon.core.webui.keyword.builtin

import java.text.MessageFormat

import org.apache.commons.lang3.StringUtils
import org.openqa.selenium.By
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.webui.constants.CoreWebuiMessageConstants
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain

import groovy.transform.CompileStatic

@Action(value = "getAllLinksOnCurrentPage")
public class GetAllLinksOnCurrentPageKeyword extends WebUIAbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        boolean isIncludedExternalLinks = getBooleanValue(params, 0)
        List<String> excludedLinks = getExcludedLinks(params, 1)
        FailureHandling flowControl = getFailureHandling(params, 2)
        return getAllLinksOnCurrentPage(isIncludedExternalLinks, excludedLinks, flowControl)
    }

    @CompileStatic
    public List getAllLinksOnCurrentPage(boolean isIncludedExternalLinks, List<String> excludedLinks, FailureHandling flowControl) throws StepFailedException {
        String currentUrl = DriverFactory.getWebDriver().getCurrentUrl()
        if (StringUtils.isBlank(currentUrl)) {
            logger.logWarning(CoreWebuiMessageConstants.KW_LOG_WARNING_NO_PAGE_OPEN)
            return Collections.emptyList()
        }
        return (List) WebUIKeywordMain.runKeyword({
            List<WebElement> elements = findWebElements()
            List<String> links = new ArrayList<String>()
            URI currentURI = toURI(currentUrl)
            if (currentURI == null) {
                return links
            }

            String currentHost = currentURI.getHost()
            for (WebElement element : elements) {
                String link = getLinkAttribute(element)
                // ignore empty URL
                if (StringUtils.isBlank(link)) {
                    continue
                }

                link = removeFragmentsFromLink(link)
                // ignore duplicate URL
                if (links.contains(link)) {
                    continue
                }

                if (isIncludedExternalLinks) {
                    links.add(link)
                    continue
                }

                // validate internal URL
                if (isSameHost(currentHost, link)) {
                    links.add(link)
                }
            }
            // remove all excluded links
            links.removeAll(excludedLinks)
            logger.logPassed(MessageFormat.format(CoreWebuiMessageConstants.KW_LOG_PASSED_FOUND_X_UNIQUE_LINKS_ON_PAGE, links.size(), currentUrl))
            return links
        }, flowControl, false, MessageFormat.format(CoreWebuiMessageConstants.KW_MSG_UNABLE_TO_GET_ALL_LINKS_ON_PAGE, currentUrl))
    }

    @CompileStatic
    private List<WebElement> findWebElements() {
        int timeOut = RunConfiguration.getTimeOut()
        final By locator = By.xpath("//*[@href] | //*[@src] | //*[@cite] | //object[@data]")
        try {
            logger.logDebug(CoreWebuiMessageConstants.KW_LOG_INFO_FINDING_ALL_LINKS_ON_PAGE)
            WebDriver webDriver = DriverFactory.getWebDriver();
            float timeCount = 0;
            long miliseconds = System.currentTimeMillis();
            while (timeCount < timeOut) {
                try {
                    List<WebElement> webElements = webDriver.findElements(locator);
                    if (webElements != null && webElements.size() > 0) {
                        logger.logDebug(MessageFormat.format(CoreWebuiMessageConstants.KW_LOG_INFO_FOUND_X_ELEMENTS, webElements.size()))
                        return webElements;
                    }
                } catch (NoSuchElementException e) {
                    // not found element yet, moving on
                }

                timeCount += ((System.currentTimeMillis() - miliseconds) / 1000);
                Thread.sleep(500);
                timeCount += 0.5;
                miliseconds = System.currentTimeMillis();
            }
        } catch (TimeoutException e) {
            // timeOut, do nothing
        } catch (InterruptedException e) {
            // interrupted, do nothing
        }
        return Collections.emptyList();
    }

    @CompileStatic
    private String removeFragmentsFromLink(String url) {
        if (!url.contains("#")) {
            return url
        }

        URI uri = toURI(url)
        if (uri == null) {
            // Ignore invalid URL
            return url
        }

        // Remove fragment parts
        return StringUtils.removeEnd(url, "#" + uri.getRawFragment())
    }

    @CompileStatic
    private URI toURI(String url) {
        try {
            return URI.create(url)
        } catch(Exception e) {
            // do nothing
            logger.logError(MessageFormat.format(CoreWebuiMessageConstants.KW_LOG_ERROR_X_IS_AN_INVALID_URL, url, e.getMessage()), null, e)
            return null
        }
    }

    @CompileStatic
    private boolean isSameHost(String currentHost, String url) {
        try {
            return StringUtils.equalsIgnoreCase(currentHost, URI.create(url).getHost())
        } catch(Exception e) {
            // do nothing
            return false
        }
    }

    @CompileStatic
    private String getLinkAttribute(WebElement element) {
        // a, area, base, link
        // See https://www.w3schools.com/tags/att_href.asp
        String href = element.getAttribute("href")
        if (href != null) {
            if (href.startsWith("http")) {
                // Allow protocols: http and https
                return href
            }
            // Not allow protocols such as mailto, tel, ftp, javascript, file...
            return StringUtils.EMPTY
        }

        // audio, embed, iframe, img, input, script, source, track, video
        // See https://www.w3schools.com/tags/att_src.asp
        String src = element.getAttribute("src")
        if (src != null) {
            return src
        }

        // blockquote, del, ins, q
        // See https://www.w3schools.com/tags/att_cite.asp
        String cite = element.getAttribute("cite")
        if (cite != null) {
            return cite
        }

        // object
        // See https://www.w3schools.com/tags/att_data.asp
        String data = element.getAttribute("data")
        if (data != null) {
            return data
        }

        return StringUtils.EMPTY
    }

    @CompileStatic
    private List<String> getExcludedLinks(Object[] params, int index) {
        Object param = getParam(params, index)
        if (param instanceof List<String>) {
            return (List<String>) param
        }
        return Collections.emptyList()
    }
}
