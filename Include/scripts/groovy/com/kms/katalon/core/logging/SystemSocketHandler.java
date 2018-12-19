package com.kms.katalon.core.logging;

import java.io.IOException;
import java.util.logging.LogRecord;
import java.util.logging.SocketHandler;

public class SystemSocketHandler extends SocketHandler {
    public SystemSocketHandler(String hostAddess, int port) throws IOException {
        super(hostAddess, port);
    }
    
    @Override
    protected void reportError(String arg0, Exception arg1, int arg2) {
        // Ignore for socket error
    }
}
