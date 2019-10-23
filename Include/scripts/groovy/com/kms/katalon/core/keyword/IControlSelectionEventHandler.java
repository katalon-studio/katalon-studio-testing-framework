package com.kms.katalon.core.keyword;

import java.util.Map;

public interface IControlSelectionEventHandler {

	public void handle(IActionProvider actionProvider, Map<String, Object> dataFields, IContext context);

}
