package com.kms.katalon.core.webui.exception;

import java.text.MessageFormat;

import org.openqa.selenium.By;

import com.kms.katalon.core.webui.constants.StringConstants;

public class WebElementNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public WebElementNotFoundException(String objectId, By locator) {
		super(MessageFormat.format(StringConstants.EXC_WEB_ELEMENT_NOT_FOUND, objectId, locator.toString()));
	}
	
	public WebElementNotFoundException(String objectId, String locator) {
        super(MessageFormat.format(StringConstants.EXC_WEB_ELEMENT_NOT_FOUND, objectId, locator));
    }
}
