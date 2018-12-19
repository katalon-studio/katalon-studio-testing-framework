package com.kms.katalon.core.webui.common;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ECheckbox extends EInput {

	public ECheckbox(WebDriver driver, WebElement selElement) {
		super(driver, selElement);
	}

	/**
	 * Check if check box is checked
	 * 
	 * @return bool
	 */
	public Result isChecked() {
		Result checkExist = isExist();
		if (checkExist.getReturnValue().equals(true)) {
			try {
				if (element.isSelected())
					return reportHappyCase(true);
				else
					return reportUnhappyCase(false, false, "INT063");
			} catch (Exception e) {
				return reportUnhappyCase(false, true, "INT064", e.getMessage());
			}
		}

		return reportUnhappyCase(false, checkExist.needReNewDriver(), "INT065",
				"Object doesn't exists");
	}

}
