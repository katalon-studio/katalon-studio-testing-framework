package com.kms.katalon.core.network;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum ProxyOption {
    NO_PROXY("No Proxy"), USE_SYSTEM("Use system proxy configuration"), MANUAL_CONFIG("Manual proxy configuration");

    private String displayName;

    private ProxyOption(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static String[] stringValues() {
        ProxyOption[] values = values();
        return Arrays.asList(values)
                .stream()
                .map(proxyServerType -> proxyServerType.toString())
                .collect(Collectors.toList())
                .toArray(new String[values.length]);
    }

    public static String[] displayStringValues() {
        ProxyOption[] values = values();
        return Arrays.asList(values)
                .stream()
                .map(proxyServerType -> proxyServerType.getDisplayName())
                .collect(Collectors.toList())
                .toArray(new String[values.length]);
    }

    public static ProxyOption valueOfDisplayName(String displayName) {
        return Arrays.asList(values())
                .parallelStream()
                .filter(proxyOption -> proxyOption.getDisplayName().equals(displayName))
                .findAny()
                .orElse(null);
    }
}
