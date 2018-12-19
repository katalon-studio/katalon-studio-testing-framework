package com.kms.katalon.core.webui.driver;

import org.junit.Assert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

import com.thoughtworks.selenium.webdriven.ElementFinder;
import com.thoughtworks.selenium.webdriven.JavascriptLibrary;
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;

import groovy.lang.Closure;

public class KatalonWebDriverBackedSelenium extends WebDriverBackedSelenium {

	public static final String WAIT_FOR_PAGE_TO_LOAD_IN_SECONDS = "30000";
	
	private JavascriptLibrary javascriptLibrary;
	private ElementFinder elementFinder;
	public static StringBuffer verificationErrors = new StringBuffer();	

	public KatalonWebDriverBackedSelenium(WebDriver maker, String baseUrl) {
		super(maker, baseUrl);
		
		javascriptLibrary = new JavascriptLibrary();
		elementFinder = new ElementFinder(javascriptLibrary);
	}

	public KatalonWebDriverBackedSelenium(String baseUrl) {
		this(DriverFactory.getWebDriver(), baseUrl);
	}
	
	public void chooseCancelOnNextPrompt() {
		this.getWrappedDriver().switchTo().alert().dismiss();
	}
	
	static public void verifyEquals(Object actual, Object expected) {
		try {
			org.testng.Assert.assertEquals(actual, expected, null);
		} catch (Error error) {
			verificationErrors.append(error.toString());
		}
	}
	
	static public void verifyTrue(boolean condition) {
		try {
			org.testng.Assert.assertTrue(condition, null);
		} catch (Error error) {
			verificationErrors.append(error.toString());
		}
	}
	
	static public void verifyFalse(boolean condition) {
		try {
			org.testng.Assert.assertFalse(condition, null);
		} catch (Error error) {
			verificationErrors.append(error.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	public void typeKeys(String locator, Keys value) {
		super.typeKeys(locator, value.toString());
	}
	
	public void sendKeys(String locator, Keys value) {
		elementFinder.findElement(this.getWrappedDriver(), locator).sendKeys(value);
	}
	
	@SuppressWarnings("deprecation")
	public void andWait() {
		super.waitForPageToLoad(WAIT_FOR_PAGE_TO_LOAD_IN_SECONDS);
	}
	
	public void waitFor(Closure<Boolean> callable) {
		try {
			for (int second = 0;; second++) {
				Boolean satisfied = callable.call();
				if (second >= 60) Assert.fail("timeout");
				try {
					if (satisfied)
						break;
				} catch (Exception e) {
				}
				Thread.sleep(1000);
			}
		} catch (Exception e) {
		}
	}
}
