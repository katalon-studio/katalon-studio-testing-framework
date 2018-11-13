package com.kms.katalon.core.mobile.keyword.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.kms.katalon.core.testobject.ConditionType;
import com.kms.katalon.core.testobject.TestObjectProperty;

public class SelectorBuilderHelper {
    
    public static String buildXpathFromProperties(List<TestObjectProperty> properties) {
        Map<String, String> binding = buildLocator(properties);
    	return binding.containsKey("xpath") ? binding.get("xpath") : "";
    }
    
    public static List<TestObjectProperty> escapeSpecialProperties(List<TestObjectProperty> properties) {
        List<TestObjectProperty> specialProperties = new ArrayList<TestObjectProperty>();
        for (TestObjectProperty property : properties) {
            if (property.getCondition().equals(ConditionType.EXPRESSION)) {
                specialProperties.add(property);
                properties.remove(property);
            }
        }
        return specialProperties;
    }
    
    private static Map<String, String> buildLocator(List<TestObjectProperty> propEntities) {
		Map<String, String> binding = new HashMap<String, String>();
		StringBuilder exp = new StringBuilder();
		String tagName = "";
		for (int i = 0; i < propEntities.size(); i++) {
			TestObjectProperty prop = propEntities.get(i);
			String propName = prop.getName();
			String propVal = prop.getValue();
			String mCondition = prop.getCondition().toString();
			if (propName.equals("id") || propName.equals("name")) {
				if (propEntities.size() == 1 && mCondition.equals(ConditionType.EQUALS.toString())) {
					binding.put(propName, propVal);
					break;
				}
			} else if (propName.equals("xpath") || propName.equals("css") || propName.equals("cssSelector")) {
				binding.put(propName, propVal);
				break;
			}
			if (propName.equalsIgnoreCase("ref_element") || propName.equalsIgnoreCase("parent_frame")) {
				continue;
			}
			if (propName.equalsIgnoreCase("tagName") || propName.equalsIgnoreCase("tag") || propName.equalsIgnoreCase("type")) {
				tagName = propVal;
				continue;
			}
			if (!exp.toString().isEmpty()) {
				exp.append(" and ");
			}
			if (propName.equals("text") || propName.equals("link_text")) {
				propName = "text()";
			}
			// If attribute, append '@' before attribute name, skip it if method
			if (!propName.endsWith("()")) {
				propName = "@" + propName;
			}
			if (mCondition.equals(ConditionType.EQUALS.toString())) {
				exp.append(String.format("%s = '%s'", propName, propVal));
			} else if (mCondition.equals(ConditionType.NOT_EQUAL.toString())) {
				exp.append(String.format("%s != '%s'", propName, propVal));
			} else if (mCondition.equals(ConditionType.CONTAINS.toString())) {
				exp.append(String.format("contains(%s,'%s')", propName, propVal));
			} else if (mCondition.equals(ConditionType.NOT_CONTAIN.toString())) {
				exp.append(String.format("not(contains(%s,'%s'))", propName, propVal));
			} else if (mCondition.equals(ConditionType.STARTS_WITH.toString())) {
				exp.append(String.format("starts-with(%s,'%s')", propName, propVal));
			}
		}
		if (!binding.containsKey("name") && !binding.containsKey("id") && !binding.containsKey("xpath")
				&& !binding.containsKey("css") && !binding.containsKey("cssSelector") && !exp.toString().equals("")) {
			StringBuilder xpath = new StringBuilder();
			xpath.append("//");
			xpath.append(tagName.equals("") ? "*" : tagName);
			xpath.append("[" + exp + "]");

			binding.put("xpath", xpath.toString());
		}
		else if(tagName != null && !tagName.equals("")){
			binding.put("xpath", "//" + tagName);
		}

		return binding;
	}
    
    //TODO: Need to refactor later
    public static String makeXpath(List<TestObjectProperty> propEntities) {
        StringBuilder exp = new StringBuilder();
        String tagName = "";
        for (int i = 0; i < propEntities.size(); i++) {
            TestObjectProperty prop = propEntities.get(i);
            String propName = prop.getName();
            String propVal = prop.getValue();
            String mCondition = prop.getCondition().toString();

            if ("xpath".equals(propName)) {
                return propVal;
            }

            if ("ref_element".equalsIgnoreCase(propName) || "parent_frame".equalsIgnoreCase(propName)) {
                continue;
            }
            if ("tagName".equalsIgnoreCase(propName) || "tag".equalsIgnoreCase(propName)
                    || "type".equalsIgnoreCase(propName)) {
                tagName = propVal;
                continue;
            }
            if (!StringUtils.isEmpty(exp.toString())) {
                exp.append(" and ");
            }
            if ("text".equals(propName) || "link_text".equals(propName)) {
                propName = "text()";
            }
            // If attribute, append '@' before attribute name, skip it if method
            if (!StringUtils.endsWith(propName, "()")) {
                propName = "@" + propName;
            }
            if (mCondition.equals(ConditionType.EQUALS.toString())) {
                exp.append(String.format("%s = '%s'", propName, propVal));
            } else if (mCondition.equals(ConditionType.NOT_EQUAL.toString())) {
                exp.append(String.format("%s != '%s'", propName, propVal));
            } else if (mCondition.equals(ConditionType.CONTAINS.toString())) {
                exp.append(String.format("contains(%s,'%s')", propName, propVal));
            } else if (mCondition.equals(ConditionType.NOT_CONTAIN.toString())) {
                exp.append(String.format("not(contains(%s,'%s'))", propName, propVal));
            } else if (mCondition.equals(ConditionType.STARTS_WITH.toString())) {
                exp.append(String.format("starts-with(%s,'%s')", propName, propVal));
            }
        }
        if (!StringUtils.isEmpty(exp.toString())) {
            StringBuilder xpath = new StringBuilder();
            xpath.append("//");
            xpath.append(StringUtils.isEmpty(tagName) ? "*" : tagName);
            xpath.append("[" + exp + "]");

            return xpath.toString();
        } 
        if (StringUtils.isNotBlank(tagName)) {
            return "//" + tagName;
        }
        return "";
    }

}
