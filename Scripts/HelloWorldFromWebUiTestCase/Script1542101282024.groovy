import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

'''
This sample demonstrates how to create a new built-in keyword.
See also "Include/scripts/groovy/com/kms/katalon/core/webui/keyword/WebUiBuiltInKeywords.groovy".
'''
WebUI.openBrowser("https://www.katalon.com")
def message = WebUI.helloWorldFromWebUi("It works!", FailureHandling.STOP_ON_FAILURE)
message = "${message} Again!"
WebUI.helloWorldFromWebUi(message, FailureHandling.STOP_ON_FAILURE)
WebUI.closeBrowser()