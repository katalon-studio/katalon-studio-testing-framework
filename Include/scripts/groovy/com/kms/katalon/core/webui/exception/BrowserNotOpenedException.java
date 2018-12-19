package com.kms.katalon.core.webui.exception;

import org.openqa.selenium.WebDriverException;

import com.kms.katalon.core.webui.constants.StringConstants;

public class BrowserNotOpenedException extends WebDriverException {
	private static final long serialVersionUID = 1L;

	public BrowserNotOpenedException() {
		super(StringConstants.EXC_BROWSER_IS_NOT_OPENED);
	}
}
