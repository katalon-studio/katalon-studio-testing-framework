package com.kms.katalon.core.util.internal;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.kms.katalon.core.constants.StringConstants;

public class JsonUtil {

    /**
     * Convert an object to JSON string. Pretty print by default.
     * 
     * @param src source object
     * @return JSON string
     */
    public static String toJson(Object src) {
        return toJson(src, true);
    }

    /**
     * Convert an object to JSON string.
     * 
     * @param src source object
     * @param prettyPrint pretty print
     * @return JSON string
     */
    public static String toJson(Object src, boolean prettyPrint) {
        Gson gson = prettyPrint ? new GsonBuilder().setPrettyPrinting().create() : new Gson();
        return gson.toJson(src);
    }

    /**
     * Convert an object to JSON string. Pretty print by default.
     * 
     * @param src source object
     * @param typeOfSrc type of source
     * @return JSON string
     */
    public static String toJson(Object src, Type typeOfSrc) {
        return toJson(src, typeOfSrc, true);
    }

    /**
     * Convert an object to JSON string.
     * 
     * @param src source object
     * @param typeOfSrc type of source
     * @param prettyPrint pretty print
     * @return JSON string
     */
    public static String toJson(Object src, Type typeOfSrc, boolean prettyPrint) {
        Gson gson = prettyPrint ? new GsonBuilder().setPrettyPrinting().create() : new Gson();
        return gson.toJson(src, typeOfSrc);
    }

    /**
     * Convert JSON string to Object
     * 
     * @param json JSON string
     * @param typeOfT type of target object
     * @return target object
     * @throws IllegalArgumentException if the input is invalid JSON syntax
     */
    public static <T> T fromJson(String json, Type typeOfT) throws IllegalArgumentException {
        try {
            return new Gson().fromJson(json, typeOfT);
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException(MessageFormat.format(StringConstants.EXC_MSG_INVALID_JSON_SYNTAX,
                    e.getMessage()));
        }
    }
    
    public static <T> T fromJson(Reader reader, Type typeOfT) throws IllegalArgumentException {
        try {
            return new Gson().fromJson(reader, typeOfT);
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException(MessageFormat.format(StringConstants.EXC_MSG_INVALID_JSON_SYNTAX,
                    e.getMessage()));
        }
    }
    
    public static <T> T fromJson(Reader reader, Class<T> classOfT) throws IllegalArgumentException {
        try {
            return new Gson().fromJson(reader, classOfT);
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException(MessageFormat.format(StringConstants.EXC_MSG_INVALID_JSON_SYNTAX,
                    e.getMessage()));
        }
    }

    /**
     * Convert JSON string to Object
     * 
     * @param json JSON string
     * @param classOfT class of target object
     * @return target object
     * @throws IllegalArgumentException if the input is invalid JSON syntax
     */
    public static <T> T fromJson(String json, Class<T> classOfT) throws IllegalArgumentException {
        try {
            return (T) new Gson().fromJson(json, classOfT);
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException(MessageFormat.format(StringConstants.EXC_MSG_INVALID_JSON_SYNTAX,
                    e.getMessage()));
        }
    }

    public static JsonObject toJsonObject(Object src) {
        Gson gson = new Gson();
        return gson.toJsonTree(src).getAsJsonObject();
    }
    
    public static void mergeJsonObject(JsonObject src, JsonObject dst) {
        for (Map.Entry<String, JsonElement> entry : src.entrySet()) {
            dst.add(entry.getKey(), entry.getValue());
        }
    }
}
