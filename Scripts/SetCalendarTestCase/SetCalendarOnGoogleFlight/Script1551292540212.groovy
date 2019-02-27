import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

WebUI.openBrowser('https://www.google.com/flights')

WebUI.maximizeWindow()

WebUI.delay(5)

WebUI.click(findTestObject('GoogleFlight/startDate'))

WebUI.delay(2)

WebUI.setDate(findTestObject('GoogleFlight/calendar'), 10, 4, 2019, 2000, FailureHandling.STOP_ON_FAILURE)

WebUI.setDate(findTestObject('GoogleFlight/calendar'), 25, 5, 2019, 2000, FailureHandling.STOP_ON_FAILURE)

WebUI.delay(15)

WebUI.closeBrowser()