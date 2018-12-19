package com.kms.katalon.core.reporting;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

public class JsModel {
	protected static final String ARRAY_OPEN = "[";
	protected static final String ARRAY_CLOSE = "]";
	protected static final String ARRAY_DLMT = ",";
	protected static final String ARRAY_EMPTY = "[]";
	protected static final String EMPTY_STRING = "\"*\"";
	protected static final String EMPTY_STRING_INDEX = "0";
	
	protected void appendAndEscapeString(List<String> listStrings, String string) {
		if(listStrings != null){
			listStrings.add(StringEscapeUtils.escapeHtml(StringEscapeUtils.escapeJava(string)));
		}
	}
	
	protected List<JsModelProperty> props;
	
	protected List<JsModel> childs;
	
	public JsModel(){
		props = new ArrayList<JsModelProperty>();
		childs = new ArrayList<JsModel>();
	}
	
	protected StringBuilder toArrayString(){
		StringBuilder sb = new StringBuilder();
		sb.append(ARRAY_OPEN);
		for(int i=0; i<props.size(); i++){
			sb.append(props.get(i).getPropertyValue());
			if(i < props.size()-1){
				sb.append(ARRAY_DLMT);
			}			
		}
		sb.append(ARRAY_CLOSE);
		return sb;
	}
}
