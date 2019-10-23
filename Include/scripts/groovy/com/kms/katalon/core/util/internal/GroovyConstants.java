package com.kms.katalon.core.util.internal;

import java.util.regex.Pattern;

public class GroovyConstants {
	
	public static final Pattern VARIABLE_NAME_REGEX = Pattern.compile("^[a-zA-Z_$][a-zA-Z_$0-9]*$");
	
	public static final String GROOVY_KEYWORDS[] = { "abstract", "assert", "boolean", "break", "byte", "case", "catch",
            "char", "class", "const", "continue", "default", "do", "double", "else", "extends", "false", "final",
            "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long",
            "native", "new", "null", "package", "private", "protected", "public", "return", "short", "static",
            "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try",
            "void", "volatile", "while", "as", "in", "def", "trait" };
	
	public static boolean isValidVariableName(String variableName) {
        if (variableName == null || variableName.isEmpty())
            return false;

        for (String groovyKeyword : GROOVY_KEYWORDS) {
            if (groovyKeyword.equals(variableName)) {
                return false;
            }
        }

        return GroovyConstants.VARIABLE_NAME_REGEX.matcher(variableName).find();
    }
}
