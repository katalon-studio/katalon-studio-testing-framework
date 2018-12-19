package com.kms.katalon.core.event;

import org.greenrobot.eventbus.EventBus;

public class EventBusSingleton {

    private static EventBusSingleton instance;
    
    private EventBus eventBus;
    
    private EventBusSingleton() {
    }
    
    public static EventBusSingleton getInstance() {
        if (instance == null) {
            instance = new EventBusSingleton();
        }
        return instance;
    }
    
    public EventBus getEventBus() {
        return EventBus.getDefault();
    }
    
    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }
}
