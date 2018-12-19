package com.kms.katalon.core.logging.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import static ch.qos.logback.core.pattern.color.ANSIConstants.*;

import ch.qos.logback.core.pattern.CompositeConverter;

/**
 * Highlights inner-text depending on the level, in bold red for events of level ERROR, in red for WARN,
 * in BLUE for INFO, and in the default color for other levels.
 */
public class ColorConverter extends CompositeConverter<ILoggingEvent> {
    
    final private static String SET_DEFAULT_COLOR = ESC_START + "0;" + DEFAULT_FG + ESC_END;

    @Override
    protected String transform(ILoggingEvent event, String in) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String line : in.split("\\r\\n|\\r|\\n")) {
            if (first) {
                first = false;
            } else {
                sb.append("\n");
            }
            sb.append(ESC_START);
            sb.append(getForegroundColorCode(event));
            sb.append(ESC_END);
            sb.append(line);
            sb.append(SET_DEFAULT_COLOR);
        }
        return sb.toString();
    }

    private String getForegroundColorCode(ILoggingEvent event) {
        Level level = event.getLevel();
        switch (level.toInt()) {
        case Level.ERROR_INT:
            return RED_FG;
        default:
            return DEFAULT_FG;
        }

    }
}