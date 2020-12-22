package com.kms.katalon.core.webservice.definition;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import com.kms.katalon.core.model.SSLClientCertificateSettings;
import com.kms.katalon.core.network.ProxyInformation;
import com.kms.katalon.core.webservice.common.HttpUtil;
import com.kms.katalon.core.webservice.exception.SendRequestException;
import com.kms.katalon.core.webservice.setting.SSLCertificateOption;

public class WebServiceDefinitionWebLoader extends AbstractWebServiceDefinitionLoader {

    protected WebServiceDefinitionWebLoader(String definitionLocation) {
        super(definitionLocation);
    }

    private Map<String, String> httpHeaders = new HashMap<>();

    private ProxyInformation proxyInformation;

    private SSLCertificateOption sslCertOption;

    private SSLClientCertificateSettings clientCertSettings;

    public void setHttpHeaders(Map<String, String> httpHeaders) {
        this.httpHeaders.putAll(httpHeaders);
    }

    public void setProxyInformation(ProxyInformation proxyInfo) {
        this.proxyInformation = proxyInfo;
    }

    public void setSslCertificateOption(SSLCertificateOption certOption) {
        this.sslCertOption = certOption;
    }

    public void setSslClientCertSettings(SSLClientCertificateSettings clientCertSettings) {
        this.clientCertSettings = clientCertSettings;
    }

    public InputStream load() throws SendRequestException, UnsupportedOperationException, IOException {
        HttpGet get = buildGetRequest(getDefinitionLocation());
        HttpResponse response = HttpUtil.sendRequest(get, true, proxyInformation, sslCertOption, clientCertSettings);
        HttpEntity responseEntity = response.getEntity();
        InputStream is = null;
        if (responseEntity != null) {
            is = responseEntity.getContent();
        }
        return is;
    }

    private HttpGet buildGetRequest(String url) {
        HttpGet get = new HttpGet(url);
        setHttpConnectionHeaders(get);
        return get;
    }

    private void setHttpConnectionHeaders(HttpGet get) {
        if (httpHeaders != null) {
            httpHeaders.entrySet().stream().forEach(e -> get.addHeader(e.getKey(), e.getValue()));
        }
    }
}
