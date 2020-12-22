package com.kms.katalon.core.util;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.text.StringEscapeUtils;

public class EncodingUtil {
    private EncodingUtil() {
        // Hide the default Constructor
    }

    public static String encodeURIComponent(String text) {
        return safeCallJsFunction("encodeURIComponent", text);
    }

    private static String safeCallJsFunction(String function, Object... arguments) {
        try {
            return callJsFunction(function, arguments);
        } catch (ScriptException error) {
            return null;
        }
    }

    public static String callJsFunction(String function, Object... arguments) throws ScriptException {
        List<String> args = Arrays.stream(arguments).map(arg -> toJSValue(arg)).collect(Collectors.toList());
        String script = MessageFormat.format("{0}({1})", function, String.join(",", args));
        return evalJS(script);
    }

    private static String toJSValue(Object value) {
        if (value == null) {
            return "null";
        }
        if (!(value instanceof String)) {
            return value.toString();
        }
        return MessageFormat.format("\"{0}\"", StringEscapeUtils.escapeJava((String) value));
    }

    private static String evalJS(String script) throws ScriptException {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        return (String) engine.eval(script);
    }
}
