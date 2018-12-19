package com.kms.katalon.core.webui.common;

import org.openqa.selenium.Alert;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import com.kms.katalon.core.exception.StepFailedException;
import com.kms.katalon.core.webui.driver.DriverFactory;

public class EConfirmMessage {
	private static final String JAVA_SCRIPT_ERROR_H_IS_NULL_MESSAGE = "[JavaScript Error: \"h is null\"";
	private WebDriver driver;
	private Alert alert;
	private Boolean isPresent;

	public EConfirmMessage(WebDriver driver) throws WebDriverException, StepFailedException {
		this.driver = driver;
		isPresent = false;
		try {
			alert = this.driver.switchTo().alert();
			isPresent = true;

		} catch (Exception e) {
			if (!(e instanceof NoSuchWindowException)
					&& !e.getMessage().startsWith(JAVA_SCRIPT_ERROR_H_IS_NULL_MESSAGE)) {
				return;
			}
			DriverFactory.switchToAvailableWindow();
			this.driver = DriverFactory.getWebDriver();
			alert = this.driver.switchTo().alert();
			isPresent = true;
		}

	}

	/**
	 * Get content of confirm message
	 * 
	 * @return string message
	 * 
	 */
	public Result getValue() {
		// TODO Auto-generated method stub
		if (isPresent.equals(true)) {
			try {
				return Result.reportHappyCase(alert.getText());

			} catch (Exception e) {
				return Result.reportUnHappyCase(null, true, "INT110", e.getMessage());
			}
		} else
			return Result.reportUnHappyCase(null, false, "INT111");

	}

	/**
	 * Verify content of message
	 * 
	 * @param expected
	 *            message
	 * @return bool
	 */
	public Result verifyConfirmMessage(String input) {
		Result value = getValue();
		if (value.getMessage() == null) {
			if (input == null) {
				if (value.getReturnValue() == null)
					return Result.reportHappyCase(true);
				return Result.reportUnHappyCase(false, false, "INT112", value.getReturnValue());
			}

			if (value.getReturnValue() != null) {
				String actualValue = value.getReturnValue().toString();
				boolean matches = false;
				if (input.startsWith("regex=")) {
					input = input.replace("regex=", "");
					matches = actualValue.matches(input);
				} else {
					matches = actualValue.equals(input);
				}

				if (matches) {
					return Result.reportHappyCase(true);
				} else {
					return Result.reportUnHappyCase(false, false, "INT114", actualValue, input);
				}
			} else {
				return Result.reportUnHappyCase(false, false, "INT115", input);
			}

		} else
			return Result.reportUnHappyCaseWithMessage(false, value.needReNewDriver(), value.getMessage());
	}

	/**
	 * Do confirm action
	 * 
	 * @param input
	 *            - option for YES, NO, OK, Cancel
	 * 
	 * @throws SelException
	 */
	public Result confirm(String input) {
		try {

			if (isPresent.equals(true)) {
				if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("ok")) {
					alert.accept();
					return Result.reportHappyCase(null);
				} else if (input.equalsIgnoreCase("no") || input.equalsIgnoreCase("cancel")) {
					alert.dismiss();
					return Result.reportHappyCase(null);
				}
			} else {
				return Result.reportUnHappyCase(null, false, "INT116");
			}

		} catch (Exception e) {
			return Result.reportUnHappyCase(null, true, "INT117", e.getMessage());
		}
		return Result.reportUnHappyCase(null, false, "INT118");
	}

	/**
	 * Set text to select box
	 * 
	 * @param string
	 *            - input
	 */
	public Result setText(String input) {
		try {

			if (isPresent.equals(true)) {
				driver.switchTo().alert().sendKeys(input);
				return Result.reportHappyCase(null);
			} else {
				return Result.reportUnHappyCase(null, false, "INT119");
			}

		} catch (Exception e) {
			return Result.reportUnHappyCase(null, true, "INT120", e.getMessage());
		}
	}
}