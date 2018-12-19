package com.kms.katalon.core.webui.common;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class EInput extends BaseObject {
	public EInput(WebDriver driver, WebElement selElement) {
		super(driver, selElement);
	}

	/**
	 * Check if input element is enable or disable
	 * 
	 * @param bool
	 *            expected state
	 * @return bool
	 */
	public Result checkState(Boolean state) {
		Result checkExist = isExist();
		if (checkExist.getReturnValue().equals(true)) {
			try {
				if (this.element.isEnabled()) {
					if (state.equals(true))
						return reportHappyCase(true);
					else
						return reportUnhappyCase(false, false, "INT134");
				} else {
					if (state.equals(false))
						return reportHappyCase(true);
					else
						return reportUnhappyCase(false, false, "INT133");
				}

			} catch (Exception e) {
				return reportUnhappyCase(false, true, "INT135", e.getMessage());
			}
		}
		return reportUnhappyCase(false, checkExist.needReNewDriver(), "INT136",
				checkExist.getMessage());
	}

	/**
	 * Check if input element is enable
	 * 
	 * @return bool
	 * 
	 */
	public Result isEnable() {
		return checkState(true);
	}

	/**
	 * Check if input element is disable
	 * 
	 * @return bool
	 * 
	 */
	public Result isDisable() {
		return checkState(false);
	}

	public void getElementValue() throws Exception {
		value = getElementAttribute("value");
	}

	/**
	 * Find parent element by for attributed recursively
	 * 
	 * @param webElement
	 * @return
	 */
	protected WebElement findParentElementRecursively(WebElement webElement) {
		if ("input".equalsIgnoreCase(webElement.getTagName())) {
			return webElement;
		}
		try {
			if (webElement.getTagName().equalsIgnoreCase("html")) {
				return null;
			}
		} catch (StaleElementReferenceException e) {
			e.printStackTrace();
		}

		String forAtt = webElement.getAttribute("for");
		if (forAtt != null) {
			// find web element by ID
			return findWebElementById(forAtt);
		} else {
			WebElement parentElement = webElement.findElement(By.xpath(".."));
			return findParentElementRecursively(parentElement);
		}
	}

	/**
	 * Find web element by id
	 * 
	 * @param string
	 *            id
	 * @return
	 */
	protected WebElement findWebElementById(String id) {
		try {
			if (id != null) {
				//WebDriverWait wait = new WebDriverWait(driver, EProcess
				//		.getInstance().getTimeOut());
				//wait.until(ExpectedConditions.presenceOfElementLocated(By
				//		.id(id)));

				WebElement element = driver.findElement(By.id(id));
				if (element != null) {

					return element;
				}
			}

		} catch (Exception e) {
			throw e;
		}
		return null;
	}

}
