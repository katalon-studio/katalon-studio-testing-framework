package com.kms.katalon.core.webui.common;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class ECombobox extends EInput {
	protected Select selectEle;

	public ECombobox(WebDriver driver, WebElement selElement) {
		super(driver, selElement);
		if (element != null) {
			selectEle = new Select(element);
		}
	}

	/**
	 * Verify select box existence
	 * 
	 * @return bool
	 * 
	 */
	@Override
	public Result isExist() {
		try {
			refreshWebElement();
			if (selectEle == null && element != null) {
				selectEle = new Select(element);
			}

			if (selectEle != null)
				return reportHappyCase(true);
			else
				return reportUnhappyCase(false, false, "INT071");
		} catch (Exception e) {
			return reportUnhappyCase(false, true, "INT071");
		}

	}

	public void getElementValue() throws Exception {
		Result checkExist = isExist();
		if (checkExist.getReturnValue().equals(true)) {
			WebElement curElement = selectEle.getFirstSelectedOption();
			if (curElement != null) {
				value = curElement.getText();
			}
		}

	}

	/**
	 * Verify existence of list of items
	 * 
	 * @param string
	 *            [] - expected items
	 * @return bool
	 * 
	 */
	public Result isItemExist(String[] input) {
		Result checkExist = isExist();
		if (checkExist.getReturnValue().equals(true)) {
			try {
				List<String> options = getListOptionText();
				int flag = 0;
				for (String item : input) {
					if (options.indexOf(item) > -1) {
						flag++;
					}
				}
				if (flag == input.length)
					return reportHappyCase(true);
				return reportUnhappyCase(false, false, "INT072");
			} catch (Exception e) {
				return reportUnhappyCase(false, true, "INT073", e.getMessage());
			}
		}
		return Result.reportUnHappyCaseWithMessage(false,
				checkExist.needReNewDriver(), checkExist.getMessage());

	}

	/**
	 * Check if select box is empty
	 * 
	 * @return bool
	 * 
	 */
	private Result isEmpty() {
		Result checkExist = isExist();
		if (checkExist.getReturnValue().equals(true)) {
			try {
				if (countOptions() == 0)
					return reportHappyCase(true);
				return reportUnhappyCase(false, false, "INT074");

			} catch (Exception e) {
				return reportUnhappyCase(false, true, "INT075", e.getMessage());
			}
		}
		return Result.reportUnHappyCaseWithMessage(false,
				checkExist.needReNewDriver(), checkExist.getMessage());

	}

	/**
	 * Verify number of items of select box
	 * 
	 * @param int - expected number
	 * @return bool
	 * 
	 */
	public Result isNumberOfItemsEqual(int input) {
		Result checkExist = isExist();

		if (checkExist.getReturnValue().equals(true)) {
			try {
				int totalItems = countOptions();
				if (totalItems == input)
					return reportHappyCase(true);
				return reportUnhappyCase(false, false, "INT076", input,
						totalItems);

			} catch (Exception e) {
				return reportUnhappyCase(false, true, "INT077", e.getMessage());
			}

		}
		return reportUnhappyCase(false, checkExist.needReNewDriver(), "INT078",
				checkExist.getMessage());

	}

	/**
	 * Check if specified items are selected
	 * 
	 * @param string
	 *            - expected items
	 * @return bool
	 * 
	 */
	public Result isItemSelected(String[] input) {
		// TODO Auto-generated method stub
		Result checkExist = isExist();

		if (checkExist.getReturnValue().equals(true)) {
			try {
				List<WebElement> selectOptions = selectEle
						.getAllSelectedOptions();
				int flag = 0;
				List<String> allSelectedOptions = new ArrayList<String>();
				for (WebElement selectedOption : selectOptions) {
					allSelectedOptions.add(selectedOption.getText());
				}
				for (String item : input) {
					for (String child : allSelectedOptions) {

						if (child.equals(item)) {
							flag++;
							break;
						}
					}
				}
				if (flag == input.length)
					return reportHappyCase(true);
				return reportUnhappyCase(false, false, "INT079");
			} catch (Exception e) {
				return reportUnhappyCase(false, true, "INT080" + e.getMessage());
			}
		}
		return reportUnhappyCase(false, checkExist.needReNewDriver(), "INT081",
				checkExist.getMessage());

	}

	/**
	 * Multiple select items in select box
	 * 
	 * @param string
	 *            [] - items
	 * @return
	 */
	public Result selectItems(String[] input) {
		Result checkEnable = isEnable();
		if (checkEnable.getReturnValue().equals(true)) {
			for (String item : input) {
				boolean isScuceed = false;
				try {
					isScuceed = selectItemByIndex(item);
					element.sendKeys(Keys.RETURN);
				} catch (StaleElementReferenceException e) {
					try {
						// sleep to cleanup DOM and Cache
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					try {
						isScuceed = selectItemByIndex(item);
					} catch (StaleElementReferenceException e2) {
						return reportUnhappyCase(null, false, "INT089", item,
								e.getMessage());
					}
				}
				if (!isScuceed) {
					return reportUnhappyCase(null, false, "INT090", item);
				}
			}
			return reportHappyCase(input);
		}
		return reportUnhappyCase(null, checkEnable.needReNewDriver(), "INT091",
				checkEnable.getMessage());
	}

	/**
	 * Select one item in select box
	 * 
	 * @param string
	 *            - item
	 * @return
	 */
	public Result selectItem(String input) {
		Result checkEnable = isEnable();
		if (checkEnable.getReturnValue().equals(true)) {
			boolean isSucceed = false;
			try {
				isSucceed = selectItemByIndex(input);
				// selectEle.selectByVisibleText(item);
				element.sendKeys(Keys.RETURN);
			} catch (StaleElementReferenceException e) {
				try {
					// sleep to cleanup DOM and Cache
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				try {
					isSucceed = selectItemByIndex(input);
				} catch (StaleElementReferenceException e2) {
					return reportUnhappyCase(null, false, "INT092", input,
							e.getMessage());
				}

			}
			if (isSucceed) {
				return reportHappyCase(input);
			} else {
				return reportUnhappyCase(null, false, "INT093", input);
			}
		}
		return reportUnhappyCase(null, checkEnable.needReNewDriver(), "INT094",
				checkEnable.getMessage());
	}

	public Result selectItemsByVisibleText(String[] input) {
		Result checkEnable = isEnable();
		if (checkEnable.getReturnValue().equals(true)) {
			isExist();
			selectEle = new Select(element);
			element.sendKeys(Keys.RETURN);
			for(String item : input) {
				try {
					selectEle.selectByVisibleText(item);
					element.sendKeys(Keys.RETURN);
				} catch (StaleElementReferenceException e) {
					return reportUnhappyCase(null, false, "INT090", item);
				}
			}
			return reportHappyCase(input);
		}
		return reportUnhappyCase(null, checkEnable.needReNewDriver(), "INT091", checkEnable.getMessage());
	}
	
	private boolean selectItemByIndex(String item) {
		isExist();
		selectEle = new Select(element);
		element.sendKeys(Keys.RETURN);
		for (int i = 0; i < selectEle.getOptions().size(); i++) {
			if (selectEle.getOptions().get(i).getText().matches(item)) {
				selectEle.selectByIndex(i);
				return true;
			}
		}
		return false;
	}

	/**
	 * Select items in select box by indexes
	 * 
	 * @param int[] - item indexes
	 * @return
	 */
	public Result selectItemsByIndex(Integer[] input) {

		Result checkEnable = isEnable();
		if (checkEnable.getReturnValue().equals(true)) {
			List<WebElement> allOptions = selectEle.getOptions();
			List<String> selectedItems = new ArrayList<String>();
			for (int item : input) {
				try {
					selectEle.selectByIndex(item);
					selectedItems.add(allOptions.get(item).getText());
				} catch (Exception e) {
					return reportUnhappyCase(null, true, "INT095", item,
							e.getMessage());
				}
			}

			return reportHappyCase((String[]) selectedItems
					.toArray(new String[0]));
		}

		return reportUnhappyCase(null, checkEnable.needReNewDriver(), "INT096",
				checkEnable.getMessage());

	}

	/**
	 * Select first item in select box
	 * 
	 */
	public Result selectFirst() {
		Result checkEnable = isEnable();
		if (checkEnable.getReturnValue().equals(true)) {
			Result checkEmpty = isEmpty();
			if (checkEmpty.getMessage() != null) {
				Result returnValue = selectItemsByIndex(new Integer[] { 1 });
				if (returnValue.getMessage() == null) {
					String firstItem = ((String[]) returnValue.getReturnValue())[0];
					return reportHappyCase(firstItem);
				}
				return reportUnhappyCase(null, false, "INT097",
						returnValue.getMessage());
			}
			return reportUnhappyCase(null, checkEmpty.needReNewDriver(),
					"INT098", checkEmpty.getMessage());

		}

		return reportUnhappyCase(null, checkEnable.needReNewDriver(), "INT099",
				checkEnable.getMessage());

	}

	/**
	 * Select first item in select box
	 * 
	 */
	public Result selectLast() {

		Result checkEnable = isEnable();
		int last = -1;
		if (checkEnable.getReturnValue().equals(true)) {
			try {
				last = countOptions();
			} catch (Exception e) {
				return reportUnhappyCase(null, true, "INT100", e.getMessage());
			}
			if (last > 0) {
				Result returnValue = selectItemsByIndex(new Integer[] { last });
				if (returnValue.getMessage() == null) {
					String lastItem = ((String[]) returnValue.getReturnValue())[0];
					return reportHappyCase(lastItem);
				}
				return reportUnhappyCase(null, returnValue.needReNewDriver(),
						"INT101", returnValue.getMessage());
			}
			return reportUnhappyCase(null, false, "INT102");
		}

		return reportUnhappyCase(null, checkEnable.needReNewDriver(), "INT103",
				checkEnable.getMessage());

	}

	/**
	 * Select random an item
	 * 
	 */
	public Result selectRandom() {

		Result checkEnable = isEnable();
		int count, idx;
		if (checkEnable.getReturnValue().equals(true)) {
			try {
				count = countOptions();
			} catch (Exception e) {
				return reportUnhappyCase(null, false, "INT104", e.getMessage());
			}
			if (count > 0) {
				if (count > 1) {
					Random ran = new Random();
					idx = ran.nextInt(count - 1) + 1;
				} else
					idx = 1;
				Result returnValue = selectItemsByIndex(new Integer[] { idx });
				if (returnValue.getMessage() == null) {
					String selectedItem = ((String[]) returnValue
							.getReturnValue())[0];
					return reportHappyCase(selectedItem);
				}
				return reportUnhappyCase(null, returnValue.needReNewDriver(),
						"INT106", returnValue.getMessage());
			}
			return reportUnhappyCase(null, false, "INT106");
		}
		return reportUnhappyCase(null, checkEnable.needReNewDriver(), "INT107",
				checkEnable.getMessage());

	}

	/**
	 * Check if select box is list box
	 * 
	 * @return bool
	 */
	public Result isListBox() {
		Result checkExist = isExist();
		if (checkExist.getReturnValue().equals(true)) {
			if (selectEle.isMultiple()) {
				return reportHappyCase(true);
			}
			return reportUnhappyCase(false, false, "INT108");
		}
		return reportUnhappyCase(false, checkExist.needReNewDriver(), "INT109",
				checkExist.getMessage());
	}

	/*
	 * Helper
	 */
	/**
	 * get List Options Text of element
	 * 
	 * @return
	 * @throws SelException
	 */
	public List<String> getListOptionText() throws Exception {
		if (isExist().getReturnValue().equals(true)) {
			List<String> lstText = new ArrayList<String>();
			List<WebElement> elements = selectEle.getOptions();
			for (WebElement child : elements) {
				lstText.add(child.getText());
			}
			return lstText;
		}
		return null;
	}

	/**
	 * Count options of select
	 * 
	 * @return
	 * @throws SelException
	 */
	public int countOptions() throws Exception {
		if (isExist().getReturnValue().equals(true)) {
			List<WebElement> elements = selectEle.getOptions();
			return elements.size();
		}
		return 0;
	}

	public List<String> getSelectedOptionText() throws Exception {
		if (isExist().getReturnValue().equals(true)) {
			List<String> lstText = new ArrayList<String>();
			List<WebElement> elements = selectEle.getOptions();
			for (WebElement child : elements) {
				if(child.isSelected()){
					lstText.add(child.getText());
				}
			}
			return lstText;
		}
		return Collections.emptyList();
	}	
	
	public List<Integer> getSelectedOptionIndexes() throws Exception {
		if (isExist().getReturnValue().equals(true)) {
			List<Integer> lstIndexes = new ArrayList<Integer>();
			List<WebElement> elements = selectEle.getOptions();
			for (int i=0; i < elements.size(); i++) {
				if(elements.get(i).isSelected()){
					lstIndexes.add(i);
				}
			}
			return lstIndexes;
		}
		return Collections.emptyList();
	}	
}
