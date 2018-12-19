package com.kms.katalon.core.reporting;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "testsuite" })
@XmlRootElement(name = "testsuites")
public class JUnitTestSuites {

    protected List<JUnitTestSuite> testsuite;

    @XmlAttribute(name = "name")
    protected String name;

    @XmlAttribute(name = "time")
    protected String time;

    @XmlAttribute(name = "tests")
    protected String tests;

    @XmlAttribute(name = "failures")
    protected String failures;

    @XmlAttribute(name = "disabled")
    protected String disabled;

    @XmlAttribute(name = "errors")
    protected String errors;

    public List<JUnitTestSuite> getTestsuite() {
        if (testsuite == null) {
            testsuite = new ArrayList<JUnitTestSuite>();
        }
        return this.testsuite;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String value) {
        this.time = value;
    }

    public String getTests() {
        return tests;
    }

    public void setTests(String value) {
        this.tests = value;
    }

    public String getFailures() {
        return failures;
    }

    public void setFailures(String value) {
        this.failures = value;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String value) {
        this.disabled = value;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String value) {
        this.errors = value;
    }

}
