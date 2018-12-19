package com.kms.katalon.core.context.internal;

public interface ExecutionListenerEventHandler {
    void handleListenerEvent(ExecutionListenerEvent listenerEvent, Object[] injectedObjects);
}
