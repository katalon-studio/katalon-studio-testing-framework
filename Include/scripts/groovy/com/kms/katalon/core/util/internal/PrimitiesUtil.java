package com.kms.katalon.core.util.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("unchecked")
public class PrimitiesUtil {
    private static final Map<Class<?>, Object> DEFAULTS;

    static {
        HashMap<Class<?>, Object> map = new HashMap<>();
        put(map, Boolean.class, Boolean.valueOf(false));
        put(map, Character.class, Character.valueOf(' '));
        put(map, Byte.class, Byte.valueOf((byte) 0));
        put(map, Short.class, Short.valueOf((short) 0));
        put(map, Integer.class, Integer.valueOf(0));
        put(map, Long.class, Long.valueOf(0L));
        put(map, Float.class, Float.valueOf(0.0F));
        put(map, Double.class, Double.valueOf(0.0D));
        DEFAULTS = Collections.unmodifiableMap(map);
    }

    public static <T> T defaultValue(Class<T> type) {
        return (T) DEFAULTS.get(type);
    }

    private static <T> void put(Map<Class<?>, Object> map, Class<T> type, T value) {
        map.put(type, value);
    }
}
