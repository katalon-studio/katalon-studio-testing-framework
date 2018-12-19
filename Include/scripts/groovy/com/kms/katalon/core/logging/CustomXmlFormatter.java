package com.kms.katalon.core.logging;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.LogRecord;
import java.util.logging.XMLFormatter;

import org.apache.commons.lang3.StringEscapeUtils;


public class CustomXmlFormatter extends XMLFormatter {
    @Override
    public String format(LogRecord record) {
        int nestedLevel = 0;
        Map<String, String> attributes = null;
        if (record instanceof XmlLogRecord) {
            XmlLogRecord logRecord = (XmlLogRecord) record;
            nestedLevel = logRecord.getNestedLevel();
            attributes = logRecord.getProperties();

            Iterator<Entry<String, String>> it = logRecord.getProperties().entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, String> pair = it.next();
                pair.setValue(formatString(pair.getValue()));
            }
        }

        if (record.getMessage() != null) {
            record.setMessage(formatString(record.getMessage()));
        }

        String formattedText = super.format(record);
        StringBuilder sbFormattedText = new StringBuilder(formattedText.substring(0, formattedText.length()
                - "</record>\n".length()));
        sbFormattedText.append("  <nestedLevel>" + nestedLevel + "</nestedLevel>\n");
        if (attributes != null) for (String key : attributes.keySet())
            sbFormattedText.append(String.format("  <property name=\"%s\">%s</property>\n", key, attributes.get(key)));
        sbFormattedText.append("</record>");
        sbFormattedText.append("\n");
        return sbFormattedText.toString();
    }

    protected String formatString(String text) {
        return StringEscapeUtils.escapeXml(StringEscapeUtils.escapeJava(text));
    }
}
