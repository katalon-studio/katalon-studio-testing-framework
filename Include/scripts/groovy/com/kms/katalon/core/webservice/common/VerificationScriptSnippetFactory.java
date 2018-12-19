package com.kms.katalon.core.webservice.common;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.FrameworkUtil;

import com.kms.katalon.core.exception.KatalonRuntimeException;

public class VerificationScriptSnippetFactory {
    
    private static final String SNIPPET_NODE = "snippet";
    
    private static final String SNIPPET_NAME_NODE = "name";
    
    private static final String SNIPPET_SCRIPT_NODE = "script";
    
    private static final String SNIPPET_COMMON_NODE = "common";
    
    private static List<ScriptSnippet> snippets = new ArrayList<>();
    
    private static ScriptSnippet commonScriptSnippet;
    
    private static List<ScriptSnippet> loadSnippets() {
        
        URL url = FileLocator.find(FrameworkUtil.getBundle(
                VerificationScriptSnippetFactory.class),
                new Path("/resource/snippet/verification_snippet.xml"),
                null);
        
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(url);
            Element rootElement = document.getRootElement();
            
            for (Object snippetObj : rootElement.elements(SNIPPET_NODE)) {
                Element snippetElement = (Element) snippetObj;
                String snippetName = snippetElement.elementText(SNIPPET_NAME_NODE);
                String snippetScript = snippetElement.element(SNIPPET_SCRIPT_NODE).getText().trim();
                ScriptSnippet snippet = new ScriptSnippet(snippetName, snippetScript);
                
                if (snippet.getName().equals(SNIPPET_COMMON_NODE)) {
                    commonScriptSnippet = snippet;
                } else {
                    snippets.add(snippet);
                }
            }
        } catch (DocumentException e) {
            throw new KatalonRuntimeException(e);
        }
        
        return snippets;
    }
    
    public static List<ScriptSnippet> getSnippets() {
        if (snippets.isEmpty()) {
            loadSnippets();
        }
        return snippets;
    }
    
    public static ScriptSnippet getCommonScriptSnippet() {
        if (commonScriptSnippet == null) {
            loadSnippets();
        }
        return commonScriptSnippet;
    }
}
