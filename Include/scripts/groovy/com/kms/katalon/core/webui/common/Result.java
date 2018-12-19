package com.kms.katalon.core.webui.common;

public class Result {
	private Object returnValue;
	private String message;
	private RunningStatus status;
	private boolean needReNewDriver;

	public Result() {
		message = null;
		returnValue = null;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String value) {
		message = value;
	}

	public Object getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(Object value) {
		returnValue = value;
	}

	public RunningStatus getStatus() {
		return status;
	}

	public void setStatus(RunningStatus status) {
		this.status = status;
	}

	/**
	 * @return the needReNewWebDriver
	 */
	public boolean needReNewDriver() {
		return needReNewDriver;
	}

	/**
	 * @param needReNewDriver
	 *            the needReNewWebDriver to set
	 */
	public void setNeedReNewDriver(boolean needReNewDriver) {
		this.needReNewDriver = needReNewDriver;
	}

	@Override
	public String toString() {
		return "Result [returnValue=" + returnValue + ", message=" + message
				+ "]";
	}

	public static Result reportHappyCase(Object value) {
		Result result = new Result();
		result.setReturnValue(value);
		return result;
	}

	public static Result reportUnHappyCase(Object value,
			boolean needToReNewDriver, String logMessageId, Object... params) {
		Result result = new Result();
		String message = String.format(
				EngineLogger.getLogMessageFromProperties(logMessageId), params);
		result.setMessage(message);
		result.setReturnValue(value);
		result.setNeedReNewDriver(needToReNewDriver);
		return result;
	}

	public static Result reportUnHappyCaseWithMessage(Object value,
			boolean needToReNewDriver, String message) {
		Result result = new Result();
		result.setMessage(message);
		result.setReturnValue(value);
		result.setNeedReNewDriver(needToReNewDriver);
		return result;
	}

}
