package com.kms.katalon.core.webui.common;
import java.awt.Robot;
import java.util.HashSet;
import java.util.MissingResourceException;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class BaseObject {

	protected WebDriver driver;
	protected WebElement element;
	protected String value;
	protected BaseObject parent;
	protected String loggerName;

	public BaseObject(WebDriver driver, WebElement webElement) {
		this.driver = driver;
		this.value = "";
		this.element = webElement;
	}

	public BaseObject() {
	}

	public Result click() {

		Result exist = isExist();
		if (exist.getReturnValue().equals(false)) {
			return exist;
		}

		Result result = new Result();
		if (this.element.isEnabled()) {
			try {
				//this.waitForElementVisible(element, EProcess.getInstance()
				//		.getTimeOut());
				try {
					//if(EProcess.getBrowserFromDriver(driver) == EBrowserType.INTERNET_EXPLORER){
					//	JavascriptExecutor executor = (JavascriptExecutor) driver;
					//	executor.executeScript("arguments[0].click();", element);						
					//}
					element.click();
				} catch (Exception e) {
					if(e.getMessage().startsWith("unknown error: Element is not clickable")){
						JavascriptExecutor executor = (JavascriptExecutor) driver;
						executor.executeScript("arguments[0].click();", element);												
					}
					else{
						throw e;
					}
				}
			} catch (Exception e) {
				result.setMessage(String.format(
						EngineLogger.getLogMessageFromProperties("INT055"),
						e.getMessage()));
				result.setNeedReNewDriver(true);
			}
		} else
			result.setMessage(String.format(EngineLogger
					.getLogMessageFromProperties("INT056")));

		return result;

	}

	/**
	 * Do double click action
	 * 
	 */
	public Result doubleClick() {
		Result exist = isExist();
		if (exist.getReturnValue().equals(false)) {
			return exist;
		}

		Result result = new Result();
		//if (this.element.isEnabled()) {
			try {
				
				//refreshWebElement();
				
				Actions action = new Actions(driver);
				action.moveToElement(element).build().perform();
				
				action.doubleClick(element);
				action.perform();

			} catch (TimeoutException te) {
				return reportUnhappyCase(false, false, "OBJ040");
			} catch (Exception e) {
				result.setMessage(String.format(
						EngineLogger.getLogMessageFromProperties("INT057"),
						e.getMessage()));
				result.setNeedReNewDriver(true);

			}
		//} else {
		//	result.setMessage(String.format(EngineLogger
		//			.getLogMessageFromProperties("INT056")));
		//}
		return result;
	}

	/**
	 * Wait for page loading. Automatically stop loading after timeout number.
	 * 
	 * @param int - timeout number
	 * 
	 */
	protected Result waitForPageLoaded(int timeOut) {
		return new EBrowser(driver).waitForPageLoaded(timeOut);

	}

	protected void handleTimeoutLoading() {
		System.out.println("Time's up");
		try {
			driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
		} catch (NoSuchElementException e) {
			// Stop by java script when something happen with tag name body
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("return window.stop()");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Stop loading");
	}

	protected void refreshWebElement() throws Exception {
		try {
			this.driver.switchTo().defaultContent();
			this.driver.switchTo().activeElement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected Result reportUnhappyCase(Object value,
			boolean needToReNewBrowser, String logMessageId, Object... params) {
		try {
			Result result = new Result();
			String message = String.format(
					EngineLogger.getLogMessageFromProperties(logMessageId),
					params);
			result.setMessage(message);
			result.setReturnValue(value);
			result.setNeedReNewDriver(needToReNewBrowser);
			return result;
		} catch (MissingResourceException ms) {
			System.out.println("Should go here because hardcode");
		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	protected Result reportHappyCase(Object value) {
		Result result = new Result();
		result.setReturnValue(value);
		return result;
	}

	/**
	 * Verify web element existence
	 * 
	 * @return bool
	 */
	public Result isExist() {
		try {
			refreshWebElement();
			if (element != null)
				return reportHappyCase(true);
			return reportUnhappyCase(false, false, "INT002");
		} catch (TimeoutException te) {
			return reportUnhappyCase(false, false, "OBJ040");
		} catch (Exception e) {
			return reportUnhappyCase(false, true, "INT002");
		}
	}

	public Result rightClick() {
		Result checkExist = isExist();
		if (checkExist.getReturnValue().equals(true)) {
			try {
				Actions action = new Actions(driver);
				//action.moveToElement(element).build().perform();
				//refreshWebElement();
				action.contextClick(element).build().perform();
				return reportHappyCase(null);
			} catch (TimeoutException te) {
				return reportUnhappyCase(false, false, "OBJ040");
			} catch (Exception e) {
				return reportUnhappyCase(null, true, "INT004", e.getMessage());
			}
		}
		return reportUnhappyCase(null, checkExist.needReNewDriver(), "INT005",
				checkExist.getMessage());

	}

	/**
	 * Get value from web element
	 * 
	 * @return string - current value
	 */
	
	public Result getValue() {
		Result checkExist = isExist();
		if (checkExist.getReturnValue().equals(true)) {
			try {
				value = element.getText();
				return reportHappyCase(value);
			} catch (Exception e) {
				return reportUnhappyCase(null, true, "INT008", e.getMessage());
			}
		}
		return reportUnhappyCase(false, checkExist.needReNewDriver(), "INT009",
				checkExist.getMessage());
	}
	
	/**
	 * Check if value get from web element matches regular expression
	 * 
	 * @param string
	 *            - regular expresion
	 * @return bool
	 */
	public Result checkText(String input) {
		Result value = getValue();
		if (value.getMessage() == null) {
			if (input == null) {
				if (value.getReturnValue() == null)
					return reportHappyCase(true);
				return reportUnhappyCase(false, false, "INT034",
						value.getReturnValue());
			}

			// Handle dollar sign in regex input
			// Dollar sign is not ending sign, it just normal character
			int dollarIdx = input.indexOf("$");
			if (dollarIdx == input.length() - 1) {
				String sub = input.substring(0, input.length() - 1);
				sub = sub.replace("$", "\\$");
				input = sub.concat(input.substring(input.length() - 1,
						input.length()));
			} else {
				input = input.replace("$", "\\$");
			}

			if (value.getReturnValue() != null) {
				String actualValue = value.getReturnValue().toString();
				if (actualValue.matches(input))
					return reportHappyCase(true);
				return reportUnhappyCase(false, false, "INT035", input,
						actualValue);
			} else {
				return reportUnhappyCase(false, false, "INT037", input);
			}

		} else
			return reportUnhappyCase(false, value.needReNewDriver(), "INT038",
					value.getMessage());

	}
	
	protected String getElementAttribute(String key) {
		String attribute = null;
		if (element != null) {
			try {
				if ("text".equalsIgnoreCase(key)
						|| "link_text".equalsIgnoreCase(key)) {
					attribute = element.getText();
				} else {
					attribute = element.getAttribute(key);
				}
			} catch (StaleElementReferenceException e) {
				// prevent org.openqa.selenium.StaleElementReferenceException
				// http://docs.seleniumhq.org/exceptions/stale_element_reference.jsp
				try {
					refreshWebElement();
				} catch (Exception e1) {
					return null;
				}
				attribute = element.getAttribute(key);
			}
		}
		return attribute;
	}

	/**
	 * Get element text
	 * 
	 * @return string - value of element
	 */
	public String getElementText() throws Exception {
		if (element == null) {
			refreshWebElement();
		}
		if (element != null) {
			return element.getText();
		}
		return null;
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public WebElement getElement() {
		if (element == null) {
			isExist();
		}
		return element;
	}

	public void setElement(WebElement element) {
		this.element = element;
	}

	public Result getCssValue(String propertyName) {
		Result exist = isExist();
		if (exist.getReturnValue().equals(false)) {
			return exist;
		}
		try {
			if (element != null) {
				String cssValue = this.element.getCssValue(propertyName);
				return this.reportHappyCase(cssValue);
			}
			return this.reportUnhappyCase(null, false, "INT039");
		} catch (Exception e) {
			// TODO: handle exception
			return this.reportUnhappyCase(null, true, "INT040", e.getMessage());
		}
	}

	/**
	 * Get attribute value of a web element
	 * 
	 * @param string
	 *            - attribute name
	 * @return string - value of attribute
	 */
	public Result getAttribute(String name) {
		Result exist = isExist();
		if (exist.getReturnValue().equals(false)) {
			return exist;
		}
		try {
			return this.reportHappyCase(this.getElementAttribute(name));
		} catch (Exception e) {
			// TODO: handle exception
			return this.reportUnhappyCase(null, true, "INT041", e.getMessage());
		}
	}

	// for window only
	protected boolean verifyStringExact(String verifyString, String value) {
		return value.equalsIgnoreCase(verifyString);
	}

	protected boolean verifyStringContains(String verifyString,
			String[] containsValues) {
		if (containsValues != null) {
			for (String value : containsValues) {
				if (!verifyString.toLowerCase().contains(value.toLowerCase())) {
					return false;
				}
			}
		}
		return true;
	}

	protected String[] getWindowsTitle() {
		Set<String> handles = driver.getWindowHandles();
		Set<String> titles = new HashSet<>();
		String returnHandle = driver.getWindowHandle();

		for (String handle : handles) {
			driver.switchTo().window(handle);
			titles.add(driver.getTitle());
		}
		// switch to origin window
		driver.switchTo().window(returnHandle);

		return titles.toArray(new String[0]);
	}

	public String getLoggerName() {
		return loggerName;
	}

	public void setLoggerName(String loggerName) {
		this.loggerName = loggerName;
	}

	/**
	 * Verify attribute value contains spcified data
	 * 
	 * @param attribute
	 * @param values
	 * @return
	 */
	public Result isAttributeContainsValues(String attribute, String[] values) {
		Result exist = isExist();
		if (exist.getReturnValue().equals(false)) {
			return exist;
		}
		String attributeValue = getElementAttribute(attribute);
		for (String value : values) {
			if (!attributeValue.toLowerCase().contains(value.toLowerCase()))
				return Result.reportUnHappyCase(false, false, "INT231",
						attribute, value);

		}
		return Result.reportHappyCase(true);
	}

	/**
	 * Do right mouse move action
	 * 
	 * 
	 */
	public Result mouseMove() {
		Result checkExist = isExist();
		if (checkExist.getReturnValue().equals(true)) {
			try {				
				Robot robot = new Robot();
				robot.mouseMove(0, 0);
				Thread.sleep(500);
				
				Actions action = new Actions(driver);
				action.moveToElement(element).build().perform();	
				
				return reportHappyCase(null);
			} catch (Exception e) {
				return reportUnhappyCase(null, true, "INT006", e.getMessage());
			}
		}
		return reportUnhappyCase(null, checkExist.needReNewDriver(), "INT007",
				checkExist.getMessage());
	}

	public Result isValueContain(String input) {
		Result value = getValue();
		if (value.getMessage() == null) {
			if (input == null) {
				if (value.getReturnValue() == null)
					reportHappyCase(true);
				reportUnhappyCase(false, value.needReNewDriver(), "INT013",
						value.getReturnValue());
			}

			if (value.getReturnValue() != null) {
				String actualValue = value.getReturnValue().toString()
						.toLowerCase();
				if (actualValue.contains(input.toLowerCase()))
					return reportHappyCase(true);
				return reportUnhappyCase(false, false, "INT014", actualValue,
						input);
			}

			return reportUnhappyCase(false, false, "INT015", input);
		}
		return reportUnhappyCase(false, value.needReNewDriver(), "INT016",
				value.getMessage());

	}

}