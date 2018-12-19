package com.kms.katalon.core.webui.common;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

public class EBrowser {
	protected WebDriver driver;

	public EBrowser(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * Navigate to an url
	 * 
	 * @param string
	 *            url
	 * 
	 */
	public Result navigateToURL(String url) {
		Result result = new Result();
		try {
			if(url.toLowerCase().startsWith("http://") || url.toLowerCase().startsWith("https://")){
				driver.navigate().to(url);	
			}
			else{
				driver.navigate().to("http://" + url);
			}
		} catch (TimeoutException te) {
			handleTimeoutLoading();
		} catch (Exception e) {
			result.setMessage(String.format(
					EngineLogger.getLogMessageFromProperties("INT047"), url,
					e.getMessage()));
			result.setNeedReNewDriver(true);

		}
		return result;
	}

	/**
	 * Do back browser
	 * 
	 */
	public Result back() {
		Result result = new Result();
		try {
			driver.navigate().back();
			// this.waitForPageLoaded(EProcess.getInstance().getTimeOut());
		} catch (TimeoutException te) {
			handleTimeoutLoading();
		} catch (Exception e) {
			result.setMessage(String.format(
					EngineLogger.getLogMessageFromProperties("INT048"),
					e.getMessage()));
			result.setNeedReNewDriver(true);

		}
		return result;
	}

	/**
	 * Do forward browser
	 * 
	 */
	public Result forward() {
		Result result = new Result();
		try {
			driver.navigate().forward();
			//this.waitForPageLoaded(EProcess.getInstance().getTimeOut());

		} catch (TimeoutException te) {
			handleTimeoutLoading();
		} catch (Exception e) {
			result.setMessage(String.format(
					EngineLogger.getLogMessageFromProperties("INT049"),
					e.getMessage()));
			result.setNeedReNewDriver(true);

		}
		return result;
	}

	/**
	 * Refresh browser
	 * 
	 */
	public Result refresh() {
		Result result = new Result();
		try {
			driver.navigate().refresh();

		} catch (TimeoutException te) {
			handleTimeoutLoading();
		} catch (Exception e) {
			result.setMessage(String.format(
					EngineLogger.getLogMessageFromProperties("INT050"),
					e.getMessage()));
			result.setNeedReNewDriver(true);

		}
		return result;
	}

	/**
	 * Clear all browser cookies
	 * 
	 * @return bool
	 * 
	 */
	public Result deleteCookies() {
		Result result = new Result();
		try {
			driver.manage().deleteAllCookies();
		} catch (Exception e) {
			result.setMessage(String.format(
					EngineLogger.getLogMessageFromProperties("INT051"),
					e.getMessage()));
			result.setNeedReNewDriver(true);

		}
		return result;
	}

	public Result delay(int second) {
		Result result = new Result();
		if (second >= 0) {
			try {
				Thread.sleep(second * 1000);
				result.setReturnValue(second * 1000);
			} catch (InterruptedException e) {
				result.setReturnValue(false);
				result.setMessage(String.format(
						EngineLogger.getLogMessageFromProperties("INT052"),
						e.getMessage()));
				result.setNeedReNewDriver(true);
			}
			return result;
		}
		result.setMessage(String.format(
				EngineLogger.getLogMessageFromProperties("INT053"), second));
		result.setReturnValue(false);
		return result;
	}

	/**
	 * Wait for page loading. Do stop browser if page is still loading after
	 * timeOut
	 * 
	 * @param: int pageLoadingTimeOut
	 * @return: bool
	 * 
	 */
	public Result waitForPageLoaded(int timeOut) {
		return Result.reportHappyCase(true);
		// try {
		// new EWait().wait(new Runnable() {
		// @Override
		// public void run() {
		// String readyState = ((JavascriptExecutor) driver)
		// .executeScript("return document.readyState")
		// .toString();
		// if (readyState.equals("interactive")
		// || readyState.equals("complete"))
		// return;
		// }
		// }, timeOut * 2);
		// return Result.reportHappyCase(true);
		// } catch (java.util.concurrent.TimeoutException e1) {
		// handleTimeoutLoading();
		// return Result.reportHappyCase(true);
		// } catch (Exception e) {
		// return Result.reportUnHappyCase(false, true, "INT001",
		// e.getMessage());
		// }

	}

	protected void handleTimeoutLoading() {
		System.out.println("Time's up");
		try {
			driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
		} catch (Exception e) {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("return window.stop()");
		}
		System.out.println("Stop loading");
	}

	/**
	 * Check is application is blocked by SSL
	 * 
	 * @return: bool
	 */
	protected boolean isCertificatedBlock() {
		try {
			if (driver.getTitle().contains("Certificate"))
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public Result navigateToCertificatedPage() {
		try {
			while (isCertificatedBlock()) {
				driver.navigate()
						.to("javascript:document.getElementById('overridelink').click()");
			}
		} catch (Exception e) {
			// return Result.reportUnHappyCase(false, logMessageId, params)
		}
		return Result.reportHappyCase(true);
	}
}
