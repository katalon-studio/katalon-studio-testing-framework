package com.kms.katalon.core.testobject;

import java.util.Map;

import org.openqa.selenium.By;

public interface SelectorCollector {

    /**
     * Returns the selector method that Katalon will use to detect object.
     * 
     * @return Available selector method:
     * <ul>
     * <li>BASIC: The default selector method. Katalon will combine all active properties to detect object.</li>
     * <li>XPATH: Detects object by using Selenium {@link By#xpath(String)} xpath selector</li>
     * <li>CSS: Detects object by using Selenium {@link By#cssSelector(String)} CSS selector</li>
     * </ul>
     */
    SelectorMethod getSelectorMethod();

    /**
     * Changes the selector method to detect the object.
     * 
     * @param selectorMethod
     * <ul>
     * <li>BASIC: The default selector method. Katalon will combine all active properties to detect object.</li>
     * <li>XPATH: Detects object by using Selenium {@link By#xpath(String)} xpath selector</li>
     * <li>CSS: Detects object by using Selenium {@link By#cssSelector(String)} CSS selector</li>
     * </ul>
     * @see #setSelectorValue(SelectorMethod, String)
     * @see #getSelectorCollection()
     */
    void setSelectorMethod(SelectorMethod selectorMethod);

    /**
     * Puts a selector value to <code>selectorCollection</code>.
     * 
     * @param selectorMethod
     * <ul>
     * <li>XPATH: Detects object by using Selenium {@link By#xpath(String)} xpath selector</li>
     * <li>CSS: Detects object by using Selenium {@link By#cssSelector(String)} CSS selector</li>
     * </ul>
     * @param selectorValue
     * Selector value that's based on each method
     */
    void setSelectorValue(SelectorMethod selectorMethod, String selectorValue);

    /**
     * Returns all selector methods and theirs values.
     */
    Map<SelectorMethod, String> getSelectorCollection();
}
