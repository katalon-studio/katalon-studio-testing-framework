package com.kms.katalon.core.logging;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;


public enum LogLevel {
    START(10020),
    END(10030),
    PASSED(10000),
    INFO(10010),
    WARNING(10040),
    FAILED(10050),
    ERROR(10060),
    ABORTED(10090), // not use
    INCOMPLETE(10100),
    NOT_RUN(9909),
    RUN_DATA(20000),
    DEBUG(10009);
    
    private final int value;
    private Level level;
    
    private LogLevel(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    public Level getLevel() {
        if (level == null) {
            level = new InternalLogLevel(name(), getValue());;
        }
        return level;
    }
    
    public static LogLevel valueOf(Level level) {
        for (LogLevel logLevel : values()) {
            if (logLevel.getValue() == level.intValue()) {
                return logLevel;
            }
        }
        return null;        
    }
    
	public static Set<LogLevel> getResultLogs() {
	    Set<LogLevel> resultLogs = new LinkedHashSet<LogLevel>();
	    resultLogs.add(PASSED);
	    resultLogs.add(FAILED);
	    resultLogs.add(ERROR);
	    resultLogs.add(INCOMPLETE);
	    
	    return resultLogs;
	}
	
	private class InternalLogLevel extends Level {
        private static final long serialVersionUID = 7111238540539667071L;

        protected InternalLogLevel(String arg0, int arg1) {
            super(arg0, arg1);
        }
	}
}
