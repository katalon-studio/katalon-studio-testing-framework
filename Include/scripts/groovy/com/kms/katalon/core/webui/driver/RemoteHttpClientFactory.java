package com.kms.katalon.core.webui.driver;

import java.net.URL;

import org.openqa.selenium.remote.internal.OkHttpClient;

public class RemoteHttpClientFactory implements org.openqa.selenium.remote.http.HttpClient.Factory {
    
    final OkHttpClient okHttpClient;

    public RemoteHttpClientFactory(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    @Override
    public org.openqa.selenium.remote.http.HttpClient createClient(URL url) {
        return (org.openqa.selenium.remote.http.HttpClient) okHttpClient;
    }

    @Override
    public void cleanupIdleClients() {
    }

    @Override
    public org.openqa.selenium.remote.http.HttpClient.Builder builder() {
        return null;
    }
}
