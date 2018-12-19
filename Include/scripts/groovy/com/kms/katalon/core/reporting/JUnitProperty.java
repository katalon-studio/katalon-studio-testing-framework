package com.kms.katalon.core.reporting;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "property")
public class JUnitProperty {

    public JUnitProperty() {
    }

    public JUnitProperty(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @XmlAttribute(name = "name", required = true)
    protected String name;

    @XmlAttribute(name = "value", required = true)
    protected String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
