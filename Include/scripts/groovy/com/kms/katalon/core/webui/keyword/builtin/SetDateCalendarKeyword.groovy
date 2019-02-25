package com.kms.katalon.core.webui.keyword.builtin

import groovy.transform.CompileStatic

import java.text.MessageFormat
import java.util.concurrent.TimeUnit

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
        // get the greeting message
        // retrieve flowControl (if any)
        // flowControl should be the last argument and of type FailureHandling
        FailureHandling flowControl = (FailureHandling)(
                params.length > 4 &&
                params[4] instanceof FailureHandling ?
                params[4] :
                RunConfiguration.getDefaultFailureHandling()
                )
        // action!
        setDate(to, day, month, year, flowControl)
    }

    @CompileStatic
    public String setDate(TestObject to, int day, int month, int year, FailureHandling flowControl) throws StepFailedException {
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
                int curDay = cal.get(Calendar.DAY_OF_MONTH)
                int curMonth = cal.get(Calendar.MONTH) + 1;
                int curYear = cal.get(Calendar.YEAR);
                
                //calculate move Month
                int moveMonth = (year - curYear - 1) * 12 + 12 + (month - curMonth);
                
                // get all child element in calendar object
                List<WebElement> allTd = calendar.findElements(By.xpath(".//*"))
                
                //get next and previous month btn
                WebElement nextBtn = getNextMonthElement(allTd);
                WebElement prevBtn = getPreviousMonthElement(allTd);
                
                if (moveMonth > 0){
                    for (int i = 0; i < moveMonth; i++){
                        nextBtn.click();
                        Thread.sleep(2000);
                    }
                    
                }
                else
                    for (int i = 0; i < 0 - moveMonth; i++){
                        prevBtn.click();
                        Thread.sleep(2000);
                    }
                          
                WebElement td;
                String t = "";
                for (WebElement i : allTd){
                    t += i.getText();
                }               
                    
                for(int index = 0; index < allTd.size(); index++){
                    if (allTd[index].getText().length() > 0){
                        td = allTd[index]
                        break;
                    }
                }
                td.click()

                for(int index = 0; index < allTd.size(); index++){
                    if (allTd[index].getText().length() > 0){
                        td = allTd[index + 2]
                        break;
                    }
                }
                td.click()
                logger.logInfo("here we go")
            }
            finally {
                if (isSwitchIntoFrame) {
                    WebUiCommonHelper.switchToDefaultContent()
                }
            }
        },
        flowControl,
        false, // no screenshot to be taken
        "Cannot say hello with message '${}'!" // error message in case of failure
        )
    }
    
    @CompileStatic
    public String getElementTagAttribute(WebElement we){
        if (we == null)
            return "";
            
        String innerHTML = we.getAttribute("innerHTML");
        String outerHTML = we.getAttribute("outerHTML");
        return outerHTML.replace(innerHTML, "");
    }
    
    
    @CompileStatic
    public WebElement getPreviousMonthElement(List<WebElement> listWE){
        
        WebElement tmpPrevious; 
        for(WebElement we : listWE){
            if (getElementTagAttribute(we).toUpperCase().contains("PREV"))
            return we;
        }
        
        return null;
    }
    
    @CompileStatic
    public WebElement getNextMonthElement(List<WebElement> listWE){
        
        WebElement tmpNext;
        for(WebElement we : listWE){
            if (getElementTagAttribute(we).toUpperCase().contains("NEXT"))
            return we;
        }
        
        return null;
    }
    
}
