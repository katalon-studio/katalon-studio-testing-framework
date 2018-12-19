package com.kms.katalon.core.context.internal;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.kms.katalon.core.configuration.RunConfiguration;

public class ExecutionEventManager {
    
    private static ExecutionEventManager instance;
    
    public static ExecutionEventManager getInstance() {
        if (instance == null) {
            instance = new ExecutionEventManager();
        }
        return instance;
    }

    private Set<ExecutionListenerEventHandler> eventHandlers;

    private ExecutionEventManager() {
        eventHandlers = new LinkedHashSet<>();
        eventHandlers.add(new TestListenerCollector(
                new File(RunConfiguration.getProjectDir(), "Test Listeners").getAbsolutePath()));
    }
    
    public void addListenerEventHandle(ExecutionListenerEventHandler eventHandler) {
        eventHandlers.add(eventHandler);
    }

    public void publicEvent(ExecutionListenerEvent listenerEvent, Object[] injectedObjects) {
        Iterator<ExecutionListenerEventHandler> iterator = eventHandlers.iterator();
        while (iterator.hasNext()) {
            iterator.next().handleListenerEvent(listenerEvent, injectedObjects);
        }
    }

}
