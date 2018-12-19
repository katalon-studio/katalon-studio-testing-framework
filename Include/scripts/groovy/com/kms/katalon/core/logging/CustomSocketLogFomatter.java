package com.kms.katalon.core.logging;

public class CustomSocketLogFomatter extends CustomXmlFormatter {
    
    // No need to format for text sending through socket
    @Override
    protected String formatString(String text) {
        return text;
    }
}
