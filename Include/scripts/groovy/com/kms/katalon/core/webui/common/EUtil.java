package com.kms.katalon.core.webui.common;
import java.awt.HeadlessException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.text.MessageFormat;
import java.util.List;
import java.util.regex.Pattern;

import com.kms.katalon.core.webui.constants.StringConstants;

public class EUtil {

	public EUtil() {
	}

	/**
	 * Verify two string
	 * 
	 * @param value1
	 * @param value2
	 * @return
	 */
	public Result equal(String value1, String value2) {
		Result result = new Result();
		try {
			if (value1.equals(value2)) {
				result.setReturnValue(true);
			} else {
				result.setMessage(String.format(
						EngineLogger.getLogMessageFromProperties("INT192"),
						value1, value2));
				result.setReturnValue(false);
			}
		} catch (Exception e) {
			// TODO: handle exception\
			result.setMessage(String.format(
					EngineLogger.getLogMessageFromProperties("INT193"),
					e.getMessage()));
			result.setReturnValue(false);
		}
		return result;
	}

	public Result split(String value, String delimiter) {
		return Result.reportHappyCase(value.split(delimiter));
	}

	public Result concatMulti(Object[] inputs) {
		if (inputs != null) {
			String output = "";
			for (Object input : inputs) {
				output += input.toString();
			}
			return Result.reportHappyCase(output);
		}
		return Result.reportUnHappyCase(false, false, "INT196");
	}

	private Result executeCommand(String command) {
		Result result = new ExecuteCommandAction(command).execute();
		// Re-factor result
		@SuppressWarnings("unchecked")
		List<String> outputs = (List<String>) result.getReturnValue();
		String resultString = "";
		if (outputs != null) {
			for (String output : outputs) {
				resultString += output; // + "\n";
			}
		}
		// resultString = resultString.replace("\"", "\\\"");
		result.setReturnValue(resultString);

		return result;
	}

	public Result executeCommand(Object[] command) {
		String output = "";
		if (command != null) {
			for (Object input : command) {
				output += input.toString() + " ";
			}
			return executeCommand(output);
		}
		return Result.reportUnHappyCase(false, false,
				MessageFormat.format(StringConstants.COMM_MSG_FAILED_TO_EXEC_COMMAND, output));
	}

	public Result setURL(String urlFormat, String json) {
		return Result.reportUnHappyCase(null, false, "INT197");
	}

	public Result queryDB(String sql) {
		String sqlStatement = "cmd /c DB.py execute \"" + sql + "\"";
		return executeCommand(sqlStatement);
	}

	public Result selectQueryValueAtIndex(String queryResult, String index) {
		String command = "cmd /c DB.py get_value \"" + queryResult + "\" \""
				+ index + "\"";
		Result tmpResult = executeCommand(command);
		String value = tmpResult.getReturnValue().toString().trim()
				.replace("'", "");
		tmpResult.setReturnValue(value);
		return tmpResult;
	}

	public Result copyDataToClipboard(String value) {
		Result result = new Result();
		try {
			// Mode data to clipbboard
			Thread.sleep(2000);
			StringSelection stringSelection = new StringSelection(value);
			Toolkit.getDefaultToolkit().getSystemClipboard()
					.setContents(stringSelection, null);

			// Paste data from clipboard to text
			keyboardPaste();
			Thread.sleep(1000);

			// result.setReturnValue(true);
		} catch (HeadlessException e) {
			result.setMessage(MessageFormat.format(StringConstants.COMM_MSG_EXC_ERROR, e.getMessage()));
		} catch (InterruptedException e2) {
			result.setMessage(MessageFormat.format(StringConstants.COMM_MSG_EXC_ERROR, e2.getMessage()));
		}

		return result;
	}

	public Result keyboardPaste() {
		Result result = new Result();
		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			Thread.sleep(100);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);

			// result.setReturnValue(true);
		} catch (Exception e) {
			result.setMessage(MessageFormat.format(StringConstants.COMM_MSG_EXC_ERROR, e.getMessage()));
		}
		return result;
	}

	public Result subString(String text, String beginString, String endString) {
		try {
			int beginLength = beginString.length();
			int beginIndex = text.indexOf(beginString) + beginLength;
			int endIndex = text.indexOf(endString, beginIndex);

			return Result.reportHappyCase(text.substring(beginIndex, endIndex)
					.trim());
		} catch (Exception e) {
			return Result.reportUnHappyCase(null, false, "INT230",
					e.getMessage());
		}
	}

	public Result isTextContains(String text, String containText) {
		if (text == null) {
			throw new IllegalStateException(StringConstants.COMM_EXC_TEXT_VAL_CANNOT_BE_NULL);
		}
		if (containText == null) {
			throw new IllegalStateException(StringConstants.COMM_EXC_CONTAIN_TEXT_VAL_CANNOT_BE_NULL);
		}
		if (text.toLowerCase().contains(containText.toLowerCase())) {
			return Result.reportHappyCase(true);
		}
		return Result.reportUnHappyCase(false, false, "INT256", text,
				containText);
	}

	public Result multiply(Object op1, Integer op2) {
		double tmp = Double.parseDouble(op1.toString());
		double result = tmp * op2;
		return Result.reportHappyCase(result);

	}

	public Result divide(Object op1, Integer op2) {
		double tmp = Double.parseDouble(op1.toString());
		double result = tmp / op2;
		return Result.reportHappyCase(result);

	}

	public Result add(Object op1, Integer op2) {
		double tmp = Double.parseDouble(op1.toString());
		double result = tmp + op2;
		return Result.reportHappyCase(result);

	}

	public Result subtract(Object op1, Integer op2) {
		double tmp = Double.parseDouble(op1.toString());
		double result = tmp - op2;
		return Result.reportHappyCase(result);

	}
	
	public Result match(String value1, String value2) {
		Result result = new Result();
		try {
			Pattern p = Pattern.compile(value2, Pattern.DOTALL);
			boolean matches = p.matcher(value1).matches();
			if (matches) {
				result.setReturnValue(true);
			} else {
				result.setMessage(String.format(EngineLogger.getLogMessageFromProperties("INT262"),value1, value2));
				result.setReturnValue(false);
			}
		} catch (Exception e) {
			result.setMessage(String.format(EngineLogger.getLogMessageFromProperties("INT193"),e.getMessage()));
			result.setReturnValue(false);
		}
		return result;
	}
	
    public void enterUserInfo(String username, String password) throws InterruptedException {
        EKeyboard key = new EKeyboard();
        copyDataToClipboard(username);
        key.pressKeys("/tab", true);
        copyDataToClipboard(password);
        Thread.sleep(1000);
        key.pressKeys("/enter", true);
        Thread.sleep(1000);
    }

}
