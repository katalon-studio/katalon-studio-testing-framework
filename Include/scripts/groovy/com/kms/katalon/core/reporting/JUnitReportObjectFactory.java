package com.kms.katalon.core.reporting;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.kms.katalon.core.logging.model.TestSuiteLogRecord;

@XmlRegistry
public class JUnitReportObjectFactory {

    private final static QName _SystemOut_QNAME = new QName("", "system-out");

    private final static QName _Skipped_QNAME = new QName("", "skipped");

    private final static QName _SystemErr_QNAME = new QName("", "system-err");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package:
     * com.kms.katalon.core.reporting
     */
    public JUnitReportObjectFactory() {
    }

    /**
     * Create an instance of {@link JUnitFailure }
     */
    public JUnitFailure createFailure() {
        return new JUnitFailure();
    }

    /**
     * Create an instance of {@link JUnitTestSuites }
     */
    public JUnitTestSuites createTestSuites() {
        return new JUnitTestSuites();
    }

    /**
     * Create an instance of {@link JUnitTestSuite }
     */
    public JUnitTestSuite createTestSuite() {
        return new JUnitTestSuite();
    }

    /**
     * Create an instance of {@link Properties }
     */
    public JUnitProperties createProperties() {
        return new JUnitProperties();
    }

    /**
     * Create an instance of {@link JUnitProperty }
     */
    public JUnitProperty createProperty() {
        return new JUnitProperty();
    }

    /**
     * Create an instance of {@link JUnitTestCase }
     */
    public JUnitTestCase createTestCase() {
        return new JUnitTestCase();
    }

    /**
     * Create an instance of {@link JUnitError }
     */
    public JUnitError createError() {
        return new JUnitError();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "system-out")
    public JAXBElement<String> createSystemOut(String value) {
        return new JAXBElement<String>(_SystemOut_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "skipped")
    public JAXBElement<String> createSkipped(String value) {
        return new JAXBElement<String>(_Skipped_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "system-err")
    public JAXBElement<String> createSystemErr(String value) {
        return new JAXBElement<String>(_SystemErr_QNAME, String.class, null, value);
    }

    public String sanitizeReportAttachments(TestSuiteLogRecord suiteLogEntity) {
        return listToString(Arrays.asList(suiteLogEntity.getAttachments(true)));
    }

    public String sanitizeReportLogs(TestSuiteLogRecord suiteLogEntity) {
        List<String> logFiles = suiteLogEntity.getLogFiles()
                .stream()
                .map(item -> StringEscapeUtils.escapeJava(suiteLogEntity.getLogFolder() + File.separator + item))
                .collect(Collectors.toList());
        return listToString(logFiles);
    }

    private String listToString(List<String> list) {
        return StringUtils.join(list, ", ");
    }

}
