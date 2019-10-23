package com.kms.katalon.core.logging.logback;

import java.util.Arrays;
import java.util.List;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.spi.FilterReply;

public class SystemOutFilter extends ch.qos.logback.core.filter.AbstractMatcherFilter {

    @Override
    public FilterReply decide(Object event) {
        if (!isStarted()) {
            return FilterReply.NEUTRAL;
        }

        LoggingEvent loggingEvent = (LoggingEvent) event;

        List<Level> eventsToRemove = Arrays.asList(Level.WARN, Level.ERROR);
        if (eventsToRemove.contains(loggingEvent.getLevel())) {
            return FilterReply.DENY;
        } else {
            return FilterReply.NEUTRAL;
        }
    }

}