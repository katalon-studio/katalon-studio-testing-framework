package com.kms.katalon.core.keyword.builtin

import groovy.transform.CompileStatic

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.keyword.internal.AbstractKeyword
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.logging.KeywordLogger

@Action(value = "comment")
public class CommentKeyword extends AbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return SupportLevel.BUITIN
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        String message = (String) params[0]
        comment(message)
    }

    @CompileStatic
    public void comment(String message) {
        // Just a comment line, do nothing
        logger.logInfo(message)
    }
}
