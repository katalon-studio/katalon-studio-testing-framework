package com.kms.katalon.core.event;

public class TestingEvent {

    private TestingEventType type;

    private Object data;

    public TestingEvent(TestingEventType type) {
        this(type, null);
    }

    public TestingEvent(TestingEventType type, Object data) {
        super();
        this.type = type;
        this.data = data;
    }

    public TestingEventType getType() {
        return type;
    }

    public void setType(TestingEventType type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
