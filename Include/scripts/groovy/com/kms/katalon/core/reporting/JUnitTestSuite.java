package com.kms.katalon.core.reporting;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.persistence.oxm.annotations.XmlCDATA;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "properties", "testcase", "systemOut", "systemErr" })
@XmlRootElement(name = "testsuite")
public class JUnitTestSuite {

    protected JUnitProperties properties;

    protected List<JUnitTestCase> testcase;

    @XmlCDATA
    @XmlElement(name = "system-out")
    protected String systemOut;

    @XmlCDATA
    @XmlElement(name = "system-err")
    protected String systemErr;

    @XmlAttribute(name = "name", required = true)
    protected String name;

    @XmlAttribute(name = "tests", required = true)
    protected String tests;

    @XmlAttribute(name = "failures")
    protected String failures;

    @XmlAttribute(name = "errors")
    protected String errors;

    @XmlAttribute(name = "time")
    protected String time;

    @XmlAttribute(name = "disabled")
    protected String disabled;

    @XmlAttribute(name = "skipped")
    protected String skipped;

    @XmlAttribute(name = "timestamp")
    protected String timestamp;

    @XmlAttribute(name = "hostname")
    protected String hostname;

    @XmlAttribute(name = "id")
    protected String id;

    @XmlAttribute(name = "package")
    protected String _package;

    public JUnitProperties getProperties() {
        return properties;
    }

    public void setProperties(JUnitProperties properties) {
        this.properties = properties;
    }

    public List<JUnitTestCase> getTestcase() {
        if (testcase == null) {
            testcase = new ArrayList<JUnitTestCase>();
        }
        return this.testcase;
    }

    public String getSystemOut() {
        return systemOut;
    }

    public void setSystemOut(String value) {
        this.systemOut = value;
    }

    public String getSystemErr() {
        return systemErr;
    }

    public void setSystemErr(String value) {
        this.systemErr = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
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

    public String getErrors() {
        return errors;
    }

    public void setErrors(String value) {
        this.errors = value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String value) {
        this.time = value;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String value) {
        this.disabled = value;
    }

    public String getSkipped() {
        return skipped;
    }

    public void setSkipped(String value) {
        this.skipped = value;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String value) {
        this.timestamp = value;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String value) {
        this.hostname = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

    public String getPackage() {
        return _package;
    }

    public void setPackage(String value) {
        this._package = value;
    }

}
