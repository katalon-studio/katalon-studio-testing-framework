package com.kms.katalon.core.main;

import java.util.List;

import org.codehaus.groovy.ast.MethodNode;

public class TestCaseMethodNodeWrapper {
    private List<MethodNode> methodNodes;
    
    private String actionType; // "set up" or "tear down"

    private boolean ignoredIfFailed;

    private String startMessage;

    public TestCaseMethodNodeWrapper(List<MethodNode> methodNodes, 
            String actionType, boolean ignoredIfFailed, String startMessage) {
        setMethodNodes(methodNodes);
        setIgnoredIfFailed(ignoredIfFailed);
        setStartMessage(startMessage);
        setActionType(actionType);
    }

    public List<MethodNode> getMethodNodes() {
        return methodNodes;
    }

    private void setMethodNodes(List<MethodNode> methodNodes) {
        this.methodNodes = methodNodes;
    }

    public String getStartMessage() {
        return startMessage;
    }

    private void setStartMessage(String startMessage) {
        this.startMessage = startMessage;
    }

    public boolean isIgnoredIfFailed() {
        return ignoredIfFailed;
    }

    private void setIgnoredIfFailed(boolean ignoredIfFailed) {
        this.ignoredIfFailed = ignoredIfFailed;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
}
