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
@XmlType(name = "", propOrder = { "skipped", "error", "failure", "systemOut", "systemErr" })
@XmlRootElement(name = "testcase")
public class JUnitTestCase {

    protected String skipped;

    protected List<JUnitError> error;

    protected List<JUnitFailure> failure;

    @XmlCDATA
    @XmlElement(name = "system-out")
    protected List<String> systemOut;

    @XmlCDATA
    @XmlElement(name = "system-err")
    protected List<String> systemErr;

    @XmlAttribute(name = "name", required = true)
    protected String name;

    @XmlAttribute(name = "assertions")
    protected String assertions;

    @XmlAttribute(name = "time")
    protected String time;

    @XmlAttribute(name = "classname")
    protected String classname;

    @XmlAttribute(name = "status")
    protected String status;

    public String getSkipped() {
        return skipped;
    }

    public void setSkipped(String skipped) {
        this.skipped = skipped;
    }

    public List<JUnitError> getError() {
        if (error == null) {
            error = new ArrayList<JUnitError>();
        }
        return this.error;
    }

    public List<JUnitFailure> getFailure() {
        if (failure == null) {
            failure = new ArrayList<JUnitFailure>();
        }
        return this.failure;
    }

    public List<String> getSystemOut() {
        if (systemOut == null) {
            systemOut = new ArrayList<String>();
        }
        return this.systemOut;
    }

    public List<String> getSystemErr() {
        if (systemErr == null) {
            systemErr = new ArrayList<String>();
        }
        return this.systemErr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssertions() {
        return assertions;
    }

    public void setAssertions(String assertions) {
        this.assertions = assertions;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
