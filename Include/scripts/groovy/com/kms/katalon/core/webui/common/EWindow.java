package com.kms.katalon.core.webui.common;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import com.kms.katalon.core.webui.constants.StringConstants;

public class EWindow {

	private WebDriver driver;

	// private static String windowBeforeSwitching = null;

	public EWindow(WebDriver parent) {
		this.driver = parent;
	}

	/**
	 * get window title
	 * 
	 * @param
	 * @return
	 */
	public Result getWindowTitle() {
		Result result = new Result();
		try {
			result.setReturnValue(driver.getTitle());

		} catch (Exception e) {
			result.setMessage(String.format(EngineLogger.getLogMessageFromProperties("INT198"), e.getMessage()));
			result.setNeedReNewDriver(true);
		}
		return result;
	}

	/**
	 * Verify if a specified window title is in opening windows
	 * 
	 * @param windowTitle
	 * @return
	 * @author tuanle
	 */
	public Result existWindow(String windowTitle) {
		String returnWindowHandle = driver.getWindowHandle();

		Set<String> handles = driver.getWindowHandles();
		for (String handle : handles) {
			driver.switchTo().window(handle);
			if (driver.getTitle().matches(windowTitle)) {
				return Result.reportHappyCase(true);
			}
		}
		driver.switchTo().window(returnWindowHandle);
		return Result.reportUnHappyCase(false, false, "INT201", windowTitle);
	}

	public boolean switchToWindowUsingTitle(WebDriver driver, String title) {
		Set<String> availableWindows = driver.getWindowHandles();
		for (String windowId : availableWindows) {
			driver = driver.switchTo().window(windowId);
			if (driver.getTitle().equals(title)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean closeWindowUsingTitle(WebDriver driver, String title) {
		Set<String> availableWindows = driver.getWindowHandles();
		for (String windowId : availableWindows) {
			driver = driver.switchTo().window(windowId);
			if (driver.getTitle().equals(title)) {
				driver.close();
				return true;
			}
		}
		return false;
	}
	
	public boolean switchToWindowUsingUrl(WebDriver driver, String url) {
		Set<String> availableWindows = driver.getWindowHandles();
		for (String windowId : availableWindows) {
			if (driver.switchTo().window(windowId).getCurrentUrl().equals(url)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean closeWindowUsingUrl(WebDriver driver, String url) {
		Set<String> availableWindows = driver.getWindowHandles();
		for (String windowId : availableWindows) {
			if (driver.switchTo().window(windowId).getCurrentUrl().equals(url)) {
				driver.close();
				return true;
			}
		}
		return false;
	}

	public boolean switchToWindowUsingIndex(WebDriver driver, int index) {
		List<String> availableWindows = new ArrayList<String>(driver.getWindowHandles());
		if (index >= 0 && index < availableWindows.size()) {
			driver.switchTo().window(availableWindows.get(index));
			return true;
		}
		return false;
	}
	
	public boolean closeWindowUsingIndex(WebDriver driver, int index) {
		List<String> availableWindows = new ArrayList<String>(driver.getWindowHandles());
		if (index >= 0 && index < availableWindows.size()) {
			driver.switchTo().window(availableWindows.get(index));
			driver.close();
			return true;
		}
		return false;
	}
	
	

	private void stopLoading() {
		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("return window.stop()");
		} catch (Exception e) {

		}
	}

	/**
	 * close all window with title
	 * 
	 * @param windowTitle
	 * @return
	 */

	public Result closeWindow(String windowTitle) {
		stopLoading();

		try {
			String currentWindowHandle = driver.getWindowHandle();
			Set<String> windowIterator = driver.getWindowHandles();
			windowIterator.remove(currentWindowHandle);
			if (driver.getTitle().equalsIgnoreCase(windowTitle)) {
				driver.switchTo().window(currentWindowHandle).close();
				if (windowIterator.size() > 0)
					driver.switchTo().window(windowIterator.iterator().next());
				return Result.reportHappyCase(null);
			} else {
				for (String windowHandle : windowIterator) {
					driver.switchTo().window(windowHandle);
					if (driver.getTitle().equalsIgnoreCase(windowTitle)) {
						driver.switchTo().window(windowHandle).close();
						driver.switchTo().window(currentWindowHandle);
						return Result.reportHappyCase(null);
					}

				}

			}
			return Result.reportUnHappyCase(null, false, "INT205", windowTitle);

		} catch (Exception e) {
			return Result.reportUnHappyCaseWithMessage(false, true, EngineLogger.getLogMessageFromProperties("INT206")
					+ windowTitle + EngineLogger.getLogMessageFromProperties("INT207") + e.getMessage());
		}
	}

	/**
	 * switch to window with title
	 * 
	 * @param windowTitle
	 * @return
	 */

	public Result switchToWindow(String windowTitle) {
		Result result = new Result();
		int timeOut = 60000;
		String originalInput = windowTitle;
		windowTitle = RunnerService.removeSpecialCharacters(windowTitle);
		try {
			if (driver.getWindowHandles().size() > 0) {
				if (driver.getWindowHandles().size() > 1) {
					Thread.sleep(5000);
				}
				String parentWindowHandle = null;
				try {
					parentWindowHandle = driver.getWindowHandle();
				} catch (Exception e) {
				}
				Set<String> windowIterator = driver.getWindowHandles();
				windowIterator.remove(parentWindowHandle);
				for (String windowHandle : windowIterator) {
					driver.switchTo().window(windowHandle);
					Result waitForPageLoad = this.waitForPageLoaded(timeOut);
					if (waitForPageLoad.getReturnValue().equals(true)
							&& RunnerService.removeSpecialCharacters(driver.getTitle()).equalsIgnoreCase(windowTitle)) {

						// windowBeforeSwitching = parentWindowHandle;
						return result;
					}
				}
				if (parentWindowHandle != null) {
					driver.switchTo().window(parentWindowHandle);
					Result waitForPageLoad = this.waitForPageLoaded(timeOut);
					if (waitForPageLoad.getReturnValue().equals(true)
							&& RunnerService.removeSpecialCharacters(driver.getTitle()).equalsIgnoreCase(windowTitle)) {

						// windowBeforeSwitching = parentWindowHandle;
						return result;
					}
				}

				result.setMessage(String.format(EngineLogger.getLogMessageFromProperties("INT209"), originalInput));
			} else
				result.setMessage(String.format(EngineLogger.getLogMessageFromProperties("INT210")));
		} catch (TimeoutException te) {
			result.setMessage(String.format(EngineLogger.getLogMessageFromProperties("INT208"), originalInput,
					te.getMessage()));
		} catch (Exception e) {
			result.setMessage(String.format(EngineLogger.getLogMessageFromProperties("INT208"), originalInput,
					e.getMessage()));
			result.setNeedReNewDriver(true);
		}

		return result;
	}

	public Result getURL() {
		Result result = new Result();
		try {
			result.setReturnValue(driver.getCurrentUrl());

		} catch (Exception e) {
			result.setMessage(MessageFormat.format(StringConstants.COMM_EXC_FAILED_TO_GET_URL, e.getMessage()));
			result.setNeedReNewDriver(true);
		}
		return result;
	}

	protected boolean verifyStringContains(String verifyString, String[] containsValues) {
		if (containsValues != null) {
			for (String value : containsValues) {
				if (!verifyString.toLowerCase().contains(value.toLowerCase())) {
					return false;
				}
			}
		}
		return true;
	}

	public Result isURLContainValue(String value) {
		Result result = new Result();
		try {
			String url = driver.getCurrentUrl();
			if (!url.toLowerCase().contains(value)) {
				result.setMessage(MessageFormat.format(StringConstants.COMM_EXC_URL_DOES_NOT_CONTAIN, url, value));
				result.setReturnValue(false);
				return result;
			}
			result.setReturnValue(true);
		} catch (Exception e) {
			result.setMessage(MessageFormat.format(StringConstants.COMM_MSG_EXC_ERROR, e.getMessage()));
			result.setReturnValue(false);
			result.setNeedReNewDriver(true);
		}
		return result;
	}

	protected Result waitForPageLoaded(int timeOut) {
		return new EBrowser(driver).waitForPageLoaded(timeOut);
	}

	protected void handleTimeoutLoading() {
		System.out.println("Time's up");
		try {
			driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
		} catch (Exception e) {
		}
		System.out.println("Stop loading");
	}

	/**
	 * get window title and verify if title is equal to specified value
	 * 
	 * @param
	 * @return
	 */
	public Result isWindowTitleEqual(String windowTitle) {
		Result result = new Result();
		String originalTitle = windowTitle;
		windowTitle = RunnerService.removeSpecialCharacters(windowTitle);
		try {
			String actualTitle = driver.getTitle();
			if (RunnerService.removeSpecialCharacters(actualTitle).equalsIgnoreCase(windowTitle))
				result.setReturnValue(true);
			else {
				result.setMessage(String.format(EngineLogger.getLogMessageFromProperties("INT199"), originalTitle,
						actualTitle));
				result.setReturnValue(false);
			}

		} catch (Exception e) {
			result.setMessage(String.format(EngineLogger.getLogMessageFromProperties("INT200"), e.getMessage()));
			result.setNeedReNewDriver(true);
		}
		return result;
	}

}
