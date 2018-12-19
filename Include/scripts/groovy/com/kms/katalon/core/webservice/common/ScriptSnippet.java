package com.kms.katalon.core.webservice.common;

public class ScriptSnippet {

    private String name;
    
    private String script;
    
    public ScriptSnippet(String name, String script) {
        this.name = name;
        this.script = script;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }
}
