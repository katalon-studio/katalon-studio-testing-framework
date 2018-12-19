package com.kms.katalon.core.logging;

public class XmlLogRecordException {
	private String className;
	private String methodName;
	private int lineNumber;

	public String getClassName() {
		if (className == null) {
			className = "";
		}
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		if (methodName == null) {
			methodName = "";
		}
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	
	@Override
	public String toString() {
		return getClassName() + "." +getMethodName() + ":" + Integer.toString(getLineNumber());
	}
}
