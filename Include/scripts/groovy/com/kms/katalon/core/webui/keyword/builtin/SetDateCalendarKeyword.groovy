package com.kms.katalon.core.webui.keyword.builtin

import groovy.transform.CompileStatic

import java.text.MessageFormat
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import java.util.regex.Matcher

import org.apache.commons.io.FileUtils
import org.openqa.selenium.Alert
import org.openqa.selenium.By
import org.openqa.selenium.Dimension
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.NoSuchWindowException
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebDriverException
import org.openqa.selenium.WebElement
import org.openqa.selenium.By.ByTagName
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.FluentWait
import org.openqa.selenium.support.ui.Select
import org.openqa.selenium.support.ui.Wait
import org.openqa.selenium.support.ui.WebDriverWait

import com.google.common.base.Function
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.keyword.BuiltinKeywords
import com.kms.katalon.core.keyword.internal.KeywordExecutor
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.util.internal.ExceptionsUtil
import com.kms.katalon.core.util.internal.PathUtil
import com.kms.katalon.core.webui.common.ScreenUtil
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.constants.StringConstants
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.driver.WebUIDriverType
import com.kms.katalon.core.webui.exception.BrowserNotOpenedException
import com.kms.katalon.core.webui.exception.WebElementNotFoundException
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain
import com.kms.katalon.core.webui.util.FileUtil


@Action(value = "setDateCalendar")
public class SetDateCalendarKeyword extends WebUIAbstractKeyword {
    
    static final String DATE_PATTERN_1 = "(\\b\\d{1,2}\\D{0,3})?\\b(?:Jan(?:uary)?|Feb(?:ruary)?|Mar(?:ch)?|Apr(?:il)?|May|Jun(?:e)?|Jul(?:y)?|Aug(?:ust)?|Sep(?:tember)?|Oct(?:ober)?|(Nov|Dec)(?:ember)?)\\D?(\\d{1,2}\\D?)?\\D?((19[7-9]\\d|20\\d{2})|\\d{2})"
    static final String DATE_PATTERN_2 = "([0-9]{4}[-/]?((0[13-9]|1[012])[-/]?(0[1-9]|[12][0-9]|30)|(0[13578]|1[02])[-/]?31|02[-/]?(0[1-9]|1[0-9]|2[0-8]))|([0-9]{2}(([2468][048]|[02468][48])|[13579][26])|([13579][26]|[02468][048]|0[0-9]|1[0-6])00)[-/]?02[-/]?29)"
    
    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return super.getSupportLevel(params)
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        
        TestObject to = (TestObject) params[0]
        int day = (int) params[1]
        int month = (int) params[2]
        int year = (int) params[3]
        int slideTimeOut = (int) params[4]
        // get the greeting message
        // retrieve flowControl (if any)
        // flowControl should be the last argument and of type FailureHandling
        FailureHandling flowControl = (FailureHandling)(
                params.length > 5 &&
                params[5] instanceof FailureHandling ?
                params[5] :
                RunConfiguration.getDefaultFailureHandling()
                )
        // action!
        setDate(to, day, month, year, slideTimeOut, flowControl)
    }

    @CompileStatic
    public String setDate(TestObject to, int day, int month, int year,int slideTimeOut, FailureHandling flowControl) throws StepFailedException {
        WebUIKeywordMain.runKeyword( {
            boolean isSwitchIntoFrame = false
            try {
                if (to == null) {
                    to = new TestObject("tempBody").addProperty("css", ConditionType.EQUALS, "body")
                }
                isSwitchIntoFrame = WebUiCommonHelper.switchToParentFrame(to)

                //convert the TestObject to WebElement
                WebElement calendar = WebUiCommonHelper.findWebElement(to, RunConfiguration.getTimeOut());

                //get current Day Month Year
                Calendar cal = Calendar.getInstance();
                int curMonth = cal.get(Calendar.MONTH) + 1;
                int curYear = cal.get(Calendar.YEAR);


                //get all child element in calendar object
                List<WebElement> allChildElement = calendar.findElements(By.xpath(".//*"))
                List<WebElement> displayedElements = filterTheElementsDisplayed(allChildElement);

                //get next and previous month btn
                WebElement nextBtn = getNextMonthElement(displayedElements);
                WebElement prevBtn = getPreviousMonthElement(displayedElements);
                
                String firstDate = getFirstDateElementVisible(displayedElements);
                
                JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getWebDriver();

                curMonth = js.executeScript("return new Date('" + firstDate + "').getMonth() + 1") as Integer
                curYear =  js.executeScript("return new Date('" + firstDate + "').getFullYear()") as Integer
               

                //calculate move Month
                int moveMonth = (year - curYear - 1) * 12 + 12 + (month - curMonth);

                //moving the calendar
                if (moveMonth > 0){
                    for (int i = 0; i < moveMonth; i++){
                        nextBtn.click();
                        Thread.sleep(slideTimeOut);
                    }

                }
                else
                    for (int i = 0; i < 0 - moveMonth; i++){
                        prevBtn.click();
                        Thread.sleep(slideTimeOut);
                    }

                //calendar object is changed after move. we have update our element
                allChildElement = calendar.findElements(By.xpath(".//*"))
                displayedElements = filterTheElementsDisplayed(allChildElement)
                List<WebElement> dateObjects = getAllElementsHasDateValue(displayedElements)
                
                //we just need click to the object has text equal the input day
                
                List<WebElement> dayObjects = getDateElementsWithMonthValue(dateObjects, month);
                
                dayObjects = getDateElementsWithDayValue(dayObjects, day)
                
                if (dayObjects.size() == 1)
                    dayObjects[0].click();
                
                if (dayObjects.size() == 0)
                    WebUIKeywordMain.stepFailed("Cannot detect the date.", flowControl, null, true)
                
            }
            finally {
                if (isSwitchIntoFrame) {
                    WebUiCommonHelper.switchToDefaultContent()
                }
            }
        },
        flowControl,
        true, // screenshot should be taken
        "Something wrong! Cannot set the calendar date." // error message in case of failure
        )
    }

    @CompileStatic
    public String getElementTagAttribute(WebElement we){
        if (we == null)
            return ""

        String innerHTML = we.getAttribute("innerHTML");
        String outerHTML = we.getAttribute("outerHTML");
        return outerHTML.replace(">" + innerHTML + "<", "")
    }


    @CompileStatic
    public WebElement getPreviousMonthElement(List<WebElement> listWE){

        WebElement tmpPrevious
        for(WebElement we : listWE){
            if (getElementTagAttribute(we).toUpperCase().contains("PREV"))
                return we
        }

        return null
    }

    @CompileStatic
    public WebElement getNextMonthElement(List<WebElement> listWE){

        WebElement tmpNext;
        for(WebElement we : listWE){
            if (getElementTagAttribute(we).toUpperCase().contains("NEXT"))
                return we
        }

        return null
    }
    
    @CompileStatic
    public String getDateFormatOfElement(WebElement we){

        List<String> regExPatterns = new ArrayList<String>();
        
        //add all date pattern we have
        regExPatterns.add(DATE_PATTERN_1);
        regExPatterns.add(DATE_PATTERN_2);
        
        for(String regEx : regExPatterns){
                        Pattern pattern = Pattern.compile(regEx);
            Matcher matcher = pattern.matcher(getElementTagAttribute(we));
            
            if (matcher.find()){
                return (String) matcher.group()
            }
        }
        
        return ""
    }
    
    @CompileStatic
    public List<WebElement> getAllElementsHasDateValue(List<WebElement> listWE){

        List<WebElement> tmpList = new ArrayList<WebElement>();
        for(WebElement we : listWE){
            def d1 = getElementTagAttribute(we)
            def d2 = getDateFormatOfElement(we)
            if (getDateFormatOfElement(we).length() > 1)
                tmpList.add(we)
        }

        return tmpList
    }
    
    @CompileStatic
    public List<WebElement> filterTheElementsDisplayed(List<WebElement> listWE){
        
        List<WebElement> tmpList = new ArrayList<WebElement>();
        for(WebElement we : listWE){
            if (we.isDisplayed())
                tmpList.add(we)
        }

        return tmpList;
    }
            
    @CompileStatic
    public String getFirstDateElementVisible(List<WebElement> listWE){
        for(WebElement we : listWE){
            if (getDateFormatOfElement(we).length() > 1 && we.isDisplayed())
                return getDateFormatOfElement(we);
        }
        return "";
    }
    
    @CompileStatic
    public List<WebElement> getDateElementsWithDayValue(List<WebElement> listWE, int day){
        
        List<WebElement> tmpList = new ArrayList<WebElement>();
        for(WebElement we : listWE){
            if (we.getText() == day.toString())
                tmpList.add(we)
        }

        return tmpList;
    }
    
    @CompileStatic
    public List<WebElement> getDateElementsWithMonthValue(List<WebElement> listWE, int month){
        
        List<WebElement> tmpList = new ArrayList<WebElement>();
        for(WebElement we : listWE){
            String date = getDateFormatOfElement(we);
            JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getWebDriver();
            int objectMonth = (int) js.executeScript("return new Date('" + date + "').getMonth() + 1") as Integer;
            if (objectMonth == month)
                tmpList.add(we);
        }

        return tmpList;
    }

}
