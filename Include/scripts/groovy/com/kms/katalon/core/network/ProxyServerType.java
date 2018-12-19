package com.kms.katalon.core.network;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum ProxyServerType {
    HTTP, HTTPS, SOCKS;

    public static String[] stringValues() {
        ProxyServerType[] values = values();
        return Arrays.asList(values)
                .stream()
                .map(proxyServerType -> proxyServerType.toString())
                .collect(Collectors.toList())
                .toArray(new String[values.length]);
    }
}
