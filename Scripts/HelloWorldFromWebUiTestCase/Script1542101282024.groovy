import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import internal.GlobalVariable as GlobalVariable

'\nThis sample demonstrates how to create a new built-in keyword.\nSee also "Include/scripts/groovy/com/kms/katalon/core/webui/keyword/WebUiBuiltInKeywords.groovy".\n'
WebUI.openBrowser('http://awskbbicodevnp-uswest-ico-react-test.s3-website-us-west-1.amazonaws.com/dashboard')

WebUI.maximizeWindow()

WebUI.waitForElementClickable(findTestObject('login'), 15)

WebUI.setText(findTestObject('username'), 'testfull')

WebUI.setText(findTestObject('password'), '123456')

WebUI.delay(5)

WebUI.click(findTestObject('login'), FailureHandling.STOP_ON_FAILURE)

WebUI.delay(15)

WebUI.click(findTestObject('clickmore'), FailureHandling.STOP_ON_FAILURE)

WebUI.delay(5)

WebUI.click(findTestObject('startDate'), FailureHandling.STOP_ON_FAILURE)

WebUI.delay(2)

WebUI.setDate(findTestObject('calendar'), '', FailureHandling.STOP_ON_FAILURE)

WebUI.delay(15)

WebUI.closeBrowser()

