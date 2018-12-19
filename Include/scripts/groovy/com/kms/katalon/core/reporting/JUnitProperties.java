package com.kms.katalon.core.reporting;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "property" })
@XmlRootElement(name = "properties")
public class JUnitProperties {

    @XmlElement(required = true)
    protected List<JUnitProperty> property;

    public List<JUnitProperty> getProperty() {
        if (property == null) {
            property = new ArrayList<JUnitProperty>();
        }
        return this.property;
    }

}
