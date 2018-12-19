package com.kms.katalon.core.reporting;

import java.util.ArrayList;
import java.util.List;

import com.kms.katalon.core.logging.model.ILogRecord;
import com.kms.katalon.core.logging.model.TestCaseLogRecord;
import com.kms.katalon.core.logging.model.TestStepLogRecord;
import com.kms.katalon.core.logging.model.TestStatus.TestStatusValue;

public class JsCallStepModel extends JsModel {

	private TestStepLogRecord callerStep;
	private List<JsStepModel> callingSteps;
	private JsModel status;

	private List<String> listStrings;
	private JsTestModel testModel;
	private TestCaseLogRecord calledTest;

	public JsCallStepModel(TestStepLogRecord callerStep, TestCaseLogRecord testLog, List<String> listStrings) {
		this.callerStep = callerStep;
		this.calledTest = testLog;
		this.listStrings = listStrings;
		this.testModel = new JsTestModel(testLog, listStrings, callerStep);
	}

	private void init() {

		this.callingSteps = new ArrayList<JsStepModel>();
		this.status = new JsModel();

		props.add(new JsModelProperty("Type", "0", null));
		props.add(new JsModelProperty("name", calledTest.getName(), listStrings));
		props.add(new JsModelProperty("timeout", "0", null));
		props.add(new JsModelProperty("doc", "0", null));
		props.add(new JsModelProperty("args", "0", null));

		// The Status
		initStatus();

		// Sub steps
		for (ILogRecord logRecord : calledTest.getChildRecords()) {
			if (logRecord instanceof TestStepLogRecord) {
				callingSteps.add(new JsStepModel((TestStepLogRecord) logRecord, listStrings, callerStep.getName()));
			}
		}

		// No Log Records
	}

	@Override
	public StringBuilder toArrayString() {
		init();
		// Create intermediate step to present for every loop
		StringBuilder sb = new StringBuilder();
		// Start step
		sb.append(ARRAY_OPEN);
		// Properties
		for (JsModelProperty prop : props) {
			sb.append(prop.getPropertyValue());
			sb.append(ARRAY_DLMT);
		}
		// Status
		sb.append(status.toArrayString());
		sb.append(ARRAY_DLMT);

		// Called keyword/step
		sb.append(ARRAY_OPEN);
		for (int i = 0; i < testModel.getSteps().size(); i++) {
			sb.append(testModel.getSteps().get(i).toArrayString());
			if (i < testModel.getSteps().size() - 1) {
				sb.append(ARRAY_DLMT);
			}
		}
		sb.append(ARRAY_CLOSE);
		sb.append(ARRAY_DLMT);
		// messages
		sb.append(ARRAY_EMPTY);
		// End step
		sb.append(ARRAY_CLOSE);

		return sb;
	}

	public JsTestModel getTestModel() {
		return this.testModel;
	}

	private void initStatus() {
		long startTime = calledTest.getStartTime();
		long elapsedTime = calledTest.getEndTime() - startTime;
		TestStatusValue theStatus = calledTest.getStatus().getStatusValue();
		String statVal = theStatus.ordinal() + "";
		status.props.add(new JsModelProperty("status", statVal, null));
		status.props.add(new JsModelProperty("startTime", String.valueOf(startTime), null));
		status.props.add(new JsModelProperty("elapsedTime", String.valueOf(elapsedTime), null));
	}
}
