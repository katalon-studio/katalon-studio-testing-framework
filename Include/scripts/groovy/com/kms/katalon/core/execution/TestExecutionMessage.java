package com.kms.katalon.core.execution;

public class TestExecutionMessage {
    private TestExecutionCommand command;

    private Object data;
    
    public TestExecutionMessage(TestExecutionCommand command) {
        this.command = command;
    }
    
    public TestExecutionMessage(TestExecutionCommand command, Object data) {
        this(command);
        this.data = data;
    }

    public TestExecutionCommand getCommand() {
        return command;
    }

    public void setCommand(TestExecutionCommand command) {
        this.command = command;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
