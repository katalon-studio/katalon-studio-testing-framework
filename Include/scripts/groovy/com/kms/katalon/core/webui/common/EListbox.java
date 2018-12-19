package com.kms.katalon.core.webui.common;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class EListbox extends ECombobox {

	public EListbox(WebDriver driver, WebElement webElement) {
		super(driver, webElement);
	}

	public void getElementValue() throws Exception {
		Result checkExist = isExist();
		if (checkExist.getReturnValue().equals(true)) {
			List<WebElement> elements = element.findElements(By
					.tagName("option"));
			if (elements.size() > 0) {
				value = "";
				for (WebElement child : elements) {
					// if (element.getAttribute("selected") != null) {
					if (child.isSelected()) {
						value = value + ", " + child.getText();
					}
				}
				if (value.length() > 1) {
					value = value.substring(2);
				}
			}
		}
	}

	/**
	 * Select all items in select box
	 * 
	 * @return string[] selected items
	 */
	public Result selectAll() {
		try {
			int count = countOptions();
			Integer[] allIndex = new Integer[count];
			for (int i = 0; i < count; i++) {
				allIndex[i] = i + 1;
			}
			Result result = selectItemsByIndex(allIndex);
			if (result.getMessage() == null) {
				return reportHappyCase((String[]) result.getReturnValue());
			}
			return reportUnhappyCase(null, result.needReNewDriver(), "INT146",
					result.getMessage());
		} catch (Exception e) {
			return reportUnhappyCase(null, true, "INT147", e.getMessage());
		}
	}

	/**
	 * Select range of items by name
	 * 
	 * @param string
	 *            begin
	 * @param string
	 *            end
	 * @return string[] selected items
	 * 
	 */
	public Result selectFromTo(String begin, String end) {
		try {
			List<String> lstOption = getListOptionText();
			int idxBegin = lstOption.indexOf(begin) + 1;
			int idxEnd = lstOption.indexOf(end) + 1;
			Result selectItems = selectIndexFromTo(idxBegin, idxEnd);
			if (selectItems.getMessage() == null)
				return reportHappyCase((String[]) (selectItems.getReturnValue()));
			return reportUnhappyCase(null, selectItems.needReNewDriver(),
					"INT148", selectItems.getMessage());
		} catch (Exception e) {
			return reportUnhappyCase(null, true, "INT149", e.getMessage());
		}
	}

	/**
	 * Select range of items by index
	 * 
	 * @param int beginIndex
	 * @param int endIndex
	 * @return string[] selected items
	 * 
	 */
	public Result selectIndexFromTo(int start, int end) {
		Result checkEnable = isEnable();
		if (checkEnable.getReturnValue().equals(true)) {
			List<WebElement> allOptions = selectEle.getOptions();
			List<String> selectedItems = new ArrayList<String>();

			if (start <= 0 || end < start) {
				return reportUnhappyCase(null, false, "INT150");
			}

			for (int i = start; i <= end; i++) {
				try {
					selectEle.selectByIndex(i - 1);
					selectedItems.add(allOptions.get(i - 1).getText());
				} catch (Exception e) {
					return reportUnhappyCase(null, true, "ITN151",
							Integer.toString(i), e.getMessage());
				}
			}
			return reportHappyCase((String[]) selectedItems
					.toArray(new String[0]));
		}
		return Result.reportUnHappyCaseWithMessage(null,
				checkEnable.needReNewDriver(), checkEnable.getMessage());
	}

	/**
	 * Unselect list of items by index
	 * 
	 * @param int[] indexes
	 * @return string[] unselected items
	 * 
	 */
	public Result unSelectItemsByIndex(Integer[] input) {
		Result checkEnable = isEnable();
		if (checkEnable.getReturnValue().equals(true)) {
			List<WebElement> allOptions = selectEle.getOptions();
			List<String> deSelectedItems = new ArrayList<String>();
			for (int item : input) {
				try {
					selectEle.deselectByIndex(item);
					deSelectedItems.add(allOptions.get(item).getText());
				} catch (Exception e) {
					return reportUnhappyCase(null, true, "INT152", item,
							e.getMessage());
				}
			}

			return reportHappyCase((String[]) deSelectedItems
					.toArray(new String[0]));
		}
		return reportUnhappyCase(null, checkEnable.needReNewDriver(), "INT153",
				checkEnable.getMessage());
	}

	/**
	 * Unselect range of items by name
	 * 
	 * @param string
	 *            begin item name
	 * @param string
	 *            end item name
	 * @return string[] unselected items
	 * 
	 */
	public Result unSelectFromTo(String begin, String end) {
		try {
			List<String> lstOption = getListOptionText();
			int idxBegin = lstOption.indexOf(begin) + 1;
			int idxEnd = lstOption.indexOf(end) + 1;
			Result deSelectItems = unSelectIndexFromTo(idxBegin, idxEnd);
			if (deSelectItems.getMessage() == null)
				return reportHappyCase((String[]) (deSelectItems
						.getReturnValue()));
			return reportUnhappyCase(null, deSelectItems.needReNewDriver(),
					"INT154", begin, end, deSelectItems.getMessage());
		} catch (Exception e) {
			return reportUnhappyCase(null, true, "INT155", begin, end,
					e.getMessage());
		}
	}

	/**
	 * Unselect range of items by index
	 * 
	 * @param int start index
	 * @param int end index
	 * @return string[] unselected items
	 * 
	 */
	public Result unSelectIndexFromTo(int start, int end) {

		Result checkEnable = isEnable();
		if (checkEnable.getReturnValue().equals(true)) {
			List<WebElement> allOptions = selectEle.getOptions();
			List<String> deSelectedItems = new ArrayList<String>();

			if (start <= 0 || end < start) {
				return reportUnhappyCase(null, false, "INT156");
			}

			for (int i = start; i <= end; i++) {
				try {
					selectEle.deselectByIndex(i - 1);
					deSelectedItems.add(allOptions.get(i - 1).getText());
				} catch (Exception e) {
					return reportUnhappyCase(null, true, "INT157",
							Integer.toString(i), e.getMessage());
				}
			}
			return reportHappyCase((String[]) deSelectedItems
					.toArray(new String[0]));
		}
		return reportUnhappyCase(null, checkEnable.needReNewDriver(), "INT158",
				checkEnable.getMessage());
	}

	/**
	 * Unselect all items
	 * 
	 * @return unselected items
	 */
	public Result unSelectAll() {
		try {
			int count = countOptions();
			Integer[] allIndex = new Integer[count];
			for (int i = 0; i < count; i++) {
				allIndex[i] = i + 1;
			}
			Result result = unSelectItemsByIndex(allIndex);
			if (result.getMessage() == null) {
				return reportHappyCase((String[]) result.getReturnValue());
			}
			return reportUnhappyCase(null, result.needReNewDriver(),
					result.getMessage());
		} catch (Exception e) {
			return reportUnhappyCase(null, true, "INT159", e.getMessage());
		}
	}

	/**
	 * unselect list of items
	 * 
	 * @param string
	 *            [] items
	 * @return unselected items
	 * 
	 */
	public Result unSelectItemsByVisibleText(String[] input) {
		Result checkEnable = isEnable();
		if (checkEnable.getReturnValue().equals(true)) {
			for (String item : input) {
				try {
					selectEle.deselectByVisibleText(item);
				} catch (Exception e) {
					return reportUnhappyCase(null, true, "INT160", item,
							e.getMessage());
				}
			}
			return reportHappyCase(input);
		}
		return reportUnhappyCase(null, checkEnable.needReNewDriver(), "INT161",
				checkEnable.getMessage());
	}
	
	public Result unSelectItemsByValue(String[] input) {
		Result checkEnable = isEnable();
		if (checkEnable.getReturnValue().equals(true)) {
			for (String item : input) {
				try {
					selectEle.deselectByValue(item);
				} catch (Exception e) {
					return reportUnhappyCase(null, true, "INT160", item,
							e.getMessage());
				}
			}
			return reportHappyCase(input);
		}
		return reportUnhappyCase(null, checkEnable.needReNewDriver(), "INT161",
				checkEnable.getMessage());
	}
}
