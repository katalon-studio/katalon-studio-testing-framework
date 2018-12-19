package com.kms.katalon.core.webui.common;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.kms.katalon.core.testobject.ConditionType;
import com.kms.katalon.core.testobject.TestObjectProperty;

public class XPathBuilder {
    
    public static enum AggregationType {
        UNION,
        INTERSECT
    }

    private static final String XPATH_INTERSECT_FORMULA = "%s[count(. | %s) = count(%s)]";

    private static final String XPATH_CONDITION_TYPE_NOT_MATCHES = "not(matches(%s, '%s'))";

    private static final String XPATH_CONDITION_TYPE_MATCHES = "matches(%s, '%s')";

    private static final String XPATH_CONDITION_TYPE_ENDS_WITH = "ends-with(%s, %s)";

    private static final String XPATH_CONDITION_TYPE_STARTS_WITH = "starts-with(%s, %s)";

    private static final String XPATH_CONDITION_TYPE_NOT_EQUALS = "%s != %s";

    private static final String XPATH_CONDITION_TYPE_NOT_CONTAINS = "not(contains(%s, %s))";

    private static final String XPATH_CONDITION_TYPE_EQUALS = "%s = %s";

    private static final String XPATH_CONDITION_TYPE_CONTAINS = "contains(%s, %s)";

    private String tag;

    private String xpath;

    private List<String> predicates;

    private List<TestObjectProperty> properties;
    
    public XPathBuilder(List<TestObjectProperty> properties) {
        this.properties = properties;
    }
    
    /**
     * convenient function to avoid changing signature
     * @return
     */
    public String build() {
        return build(AggregationType.INTERSECT);
    }

    /**
     * Union: "or" of all XPath locators, each contains a single condition
     * @param aggregationType
     * @return
     */
    public String build(AggregationType aggregationType) {
        
        boolean isIntersect = aggregationType.equals(AggregationType.INTERSECT);
        boolean isUnion = !isIntersect;
        
        boolean hasTagCondition = false;
        tag = "*";
        predicates = new ArrayList<>();
        xpath = StringUtils.EMPTY;

        if (properties != null && !properties.isEmpty()) {
            for (TestObjectProperty p : properties) {
                if (isUnion || p.isActive()) { // if union, use all properties
                    String propertyName = p.getName();
                    String propertyValue = p.getValue();
                    ConditionType conditionType = p.getCondition();
                    switch (PropertyType.nameOf(propertyName)) {
                        case ATTRIBUTE:
                            predicates.add(buildExpression("@" + propertyName, propertyValue, conditionType));
                            break;
                        case TAG:
                            hasTagCondition = true;
                            tag = propertyValue;
                            break;
                        case TEXT:
                            String textExpression = buildExpression("text()", propertyValue, conditionType);
                            String dotExpression = buildExpression(".", propertyValue, conditionType);
                            predicates.add(String.format("(%s or %s)", textExpression, dotExpression));
                            break;
                        case XPATH:
                            xpath = propertyValue;
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        List<String> xpaths = new ArrayList<>();

        if (StringUtils.isNotEmpty(xpath)) {
            xpaths.add(xpath);
        }

        if (!predicates.isEmpty()) {
            StringBuilder propertyBuilder = new StringBuilder();
            final String operator = isIntersect ? " and " : " or "; // intersect = and, union = or 
            final String xpathTag = isIntersect ? tag : "*";
            propertyBuilder.append("//")
                    .append(xpathTag)
                    .append("[")
                    .append(StringUtils.join(predicates, operator))
                    .append("]");
            xpaths.add(propertyBuilder.toString());
        }
        
        if (isUnion && hasTagCondition) { // union
            xpaths.add("//" + tag);
        }

        return combineXpathLocators(xpaths, aggregationType);
    }
    
    public List<Entry<String, String>> buildXpathBasedLocators() {
        
        List<Entry<String, String>> locators = new ArrayList<>();
        
        if (properties != null && !properties.isEmpty()) {
            for (TestObjectProperty p : properties) {
                String propertyName = p.getName();
                String propertyValue = p.getValue();
                ConditionType conditionType = p.getCondition();
                Entry<String, String> entry;
                switch (PropertyType.nameOf(propertyName)) {
                    case TEXT:
                        String textExpression = buildExpression("text()", propertyValue, conditionType);
                        String dotExpression = buildExpression(".", propertyValue, conditionType);
                        String predicate = String.format("(%s or %s)", textExpression, dotExpression);
                        String locator = "//*[" + predicate + "]";
                        entry = new AbstractMap.SimpleEntry<>(propertyName, locator);
                        break;
                    case XPATH:
                        entry = new AbstractMap.SimpleEntry<>(propertyName, propertyValue);
                        break;
                    default:
                        entry = null;
                        break;
                }
                if (entry != null) {
                    locators.add(entry);
                }
            }
        }
        
        return locators;
    }

    private String combineXpathLocators(List<String> xpathList, AggregationType aggregationType) {
        String xpathString;
        if (aggregationType.equals(AggregationType.INTERSECT)) {
            StringBuilder xpathStringBuilder = new StringBuilder();
            for (String xpath : xpathList) {
                if (xpathStringBuilder.toString().isEmpty()) {
                    xpathStringBuilder.append(xpath);
                } else {
                    String existingXpath = xpathStringBuilder.toString();
                    xpathStringBuilder = new StringBuilder(String.format(XPATH_INTERSECT_FORMULA, existingXpath, xpath, xpath));
                }
            }
            xpathString = xpathStringBuilder.toString();
        } else {
            xpathString = StringUtils.join(xpathList, " | ");
        }
        return xpathString;
    }

    private String buildExpression(String propertyName, String propertyValue, ConditionType contidionType) {
        switch (contidionType) {
            case CONTAINS:
                return String.format(XPATH_CONDITION_TYPE_CONTAINS, propertyName, escapeSingleQuote(propertyValue));
            case ENDS_WITH:
                return String.format(XPATH_CONDITION_TYPE_ENDS_WITH, propertyName, escapeSingleQuote(propertyValue));
            case EQUALS:
                return String.format(XPATH_CONDITION_TYPE_EQUALS, propertyName, escapeSingleQuote(propertyValue));
            case MATCHES_REGEX:
                return String.format(XPATH_CONDITION_TYPE_MATCHES, propertyName, propertyValue);
            case NOT_CONTAIN:
                return String.format(XPATH_CONDITION_TYPE_NOT_CONTAINS, propertyName, escapeSingleQuote(propertyValue));
            case NOT_EQUAL:
                return String.format(XPATH_CONDITION_TYPE_NOT_EQUALS, propertyName, escapeSingleQuote(propertyValue));
            case NOT_MATCH_REGEX:
                return String.format(XPATH_CONDITION_TYPE_NOT_MATCHES, propertyName, propertyValue);
            case STARTS_WITH:
                return String.format(XPATH_CONDITION_TYPE_STARTS_WITH, propertyName, escapeSingleQuote(propertyValue));
            default:
                return StringUtils.EMPTY;
        }
    }

    private String escapeSingleQuote(String s) {
        if (!s.contains("'")) {
            return qoute(s);
        }

        String[] strings = s.split("'");

        StringBuilder xpathBuilder = new StringBuilder("concat(");
        for (int i = 0; i < strings.length; i++) {
            if (i > 0) {
                xpathBuilder.append(" , \"'\" , ");
            }

            xpathBuilder.append(qoute(strings[i]));
        }

        xpathBuilder.append(")");
        return xpathBuilder.toString();
    }

    private String qoute(String s) {
        return "'" + s + "'";
    }

    public enum PropertyType {
        TAG, ATTRIBUTE, TEXT, XPATH;

        public static PropertyType nameOf(String name) {
            try {
                return valueOf(name.toUpperCase());
            } catch (IllegalArgumentException e) {
                return PropertyType.ATTRIBUTE;
            }
        }
    }
}
