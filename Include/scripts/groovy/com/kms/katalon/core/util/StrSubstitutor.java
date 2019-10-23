package com.kms.katalon.core.util;

import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import groovy.text.GStringTemplateEngine;

public class StrSubstitutor {

    private Map<String, Object> variables;
    
    public StrSubstitutor() {
        this(Collections.emptyMap());
    }
    
    public StrSubstitutor(Map<String, Object> variables) {
        this.variables = variables;
    }

    /**
     * Use {@link GStringTemplateEngine} to convert a template string into
     * a string with variable values.
     * 
     * @param str A template string
     * @return A string with variable values if the template string is not null. Otherwise return an empty string
     */
	public String replace(String str) {
        if (StringUtils.isBlank(str)) {
            return StringUtils.EMPTY;
        }
        if (!str.contains("${") || !str.contains("}")) {
            return str;
        }
        // Pass 1: use simple substitutor
        try {
        	str = org.apache.commons.lang3.text.StrSubstitutor.replace(str, variables);
        } catch (Exception ignored) {
        	// keep the original string if anything went wrong
        }
        // Pass 2: use GStringTemplateEngine to handle leftover
        try {
            GStringTemplateEngine engine = new GStringTemplateEngine();
            return engine.createTemplate(str).make(variables).toString();
        } catch (Exception e) {
        	// keep the original string if anything went wrong
        }
        return str;
    }
}
