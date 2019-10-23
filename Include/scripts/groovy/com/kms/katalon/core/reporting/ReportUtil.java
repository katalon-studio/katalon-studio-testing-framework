package com.kms.katalon.core.reporting;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.kms.katalon.core.constants.StringConstants;
import com.kms.katalon.core.logging.TestSuiteXMLLogParser;
import com.kms.katalon.core.logging.XMLLoggerParser;
import com.kms.katalon.core.logging.XMLParserException;
import com.kms.katalon.core.logging.XmlLogRecord;
import com.kms.katalon.core.logging.model.ILogRecord;
import com.kms.katalon.core.logging.model.MessageLogRecord;
import com.kms.katalon.core.logging.model.TestStatus;
import com.kms.katalon.core.logging.model.TestSuiteCollectionLogRecord;
import com.kms.katalon.core.logging.model.TestStatus.TestStatusValue;
import com.kms.katalon.core.logging.model.TestSuiteLogRecord;
import com.kms.katalon.core.reporting.template.ResourceLoader;
import com.kms.katalon.core.testdata.reader.CsvWriter;
import com.kms.katalon.core.util.internal.DateUtil;

public class ReportUtil {

    private static StringBuilder generateVars(List<String> strings, TestSuiteLogRecord suiteLogEntity,
            StringBuilder model) throws IOException {
        StringBuilder sb = new StringBuilder();
        List<String> lines = IOUtils
                .readLines(ResourceLoader.class.getResourceAsStream(ResourceLoader.HTML_TEMPLATE_VARS));
        for (String line : lines) {
            if (line.equals(ResourceLoader.HTML_TEMPLATE_SUITE_MODEL_TOKEN)) {
                sb.append(model);
            } else if (line.equals(ResourceLoader.HTML_TEMPLATE_STRINGS_CONSTANT_TOKEN)) {
                sb.append(StringUtils.join(strings, (",")));
            } else if (line.equals(ResourceLoader.HTML_TEMPLATE_EXEC_ENV_TOKEN)) {
                StringBuilder envInfoSb = new StringBuilder();
                envInfoSb.append("{");
                envInfoSb.append(String.format("\"host\" : \"%s\", ", suiteLogEntity.getHostName()));
                envInfoSb.append(String.format("\"os\" : \"%s\", ", suiteLogEntity.getOs()));
                envInfoSb.append(String.format("\"" + StringConstants.APP_VERSION + "\" : \"%s\", ",
                        suiteLogEntity.getAppVersion()));
                if (suiteLogEntity.getBrowser() != null && !suiteLogEntity.getBrowser().equals("")) {
                    if (suiteLogEntity.getRunData().containsKey("browser")) {
                        envInfoSb.append(
                                String.format("\"browser\" : \"%s\",", suiteLogEntity.getRunData().get("browser")));
                    } else {
                        envInfoSb.append(String.format("\"browser\" : \"%s\",", suiteLogEntity.getBrowser()));
                    }
                }
                if (suiteLogEntity.getDeviceName() != null && !suiteLogEntity.getDeviceName().equals("")) {
                    envInfoSb.append(String.format("\"deviceName\" : \"%s\",", suiteLogEntity.getDeviceName()));
                }
                if (suiteLogEntity.getDeviceName() != null && !suiteLogEntity.getDeviceName().equals("")) {
                    envInfoSb.append(String.format("\"devicePlatform\" : \"%s\",", suiteLogEntity.getDevicePlatform()));
                }
                envInfoSb.append("\"\" : \"\"");

                envInfoSb.append("}");
                sb.append(envInfoSb);
            } else {
                sb.append(line);
                sb.append("\n");
            }
        }
        return sb;
    }

    public static String getOs() {
        return System.getProperty("os.name") + " " + System.getProperty("sun.arch.data.model") + "bit";
    }

    public static String getHostName() {
        String hostName = "Unknown";
        try {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostName = addr.getCanonicalHostName();
        } catch (UnknownHostException ex) {}
        return hostName;
    }

    private static void collectInfoLines(ILogRecord logRecord, List<ILogRecord> rmvLogs) {
        if (logRecord instanceof MessageLogRecord) {
            if (logRecord.getStatus().getStatusValue() == TestStatusValue.INCOMPLETE
                    || logRecord.getStatus().getStatusValue() == TestStatusValue.INFO) {
                rmvLogs.add(logRecord);
            }
        }
        for (ILogRecord childLogRecord : logRecord.getChildRecords()) {
            collectInfoLines(childLogRecord, rmvLogs);
        }
    }

    public static void writeLogRecordToFiles(String logFolder) throws Exception {
        TestSuiteLogRecord testSuiteLogRecord = generate(logFolder);
        if (testSuiteLogRecord != null) {
            writeLogRecordToFiles(testSuiteLogRecord, new File(logFolder));
        }
    }

    public static void writeLogRecordToCSVFile(TestSuiteLogRecord suiteLogEntity, File destFile,
            List<ILogRecord> filteredTestCases) throws IOException {
        writeLogRecordToCSVFile(suiteLogEntity, destFile, filteredTestCases, true);
    }

    public static void writeLogRecordToCSVFile(TestSuiteLogRecord suiteLogEntity, File destFile,
            List<ILogRecord> filteredTestCases, boolean stepsIncluded) throws IOException {
        CsvWriter.writeCsvReport(suiteLogEntity, destFile, filteredTestCases, stepsIncluded);
    }

    public static void writeLogRecordToFiles(TestSuiteLogRecord suiteLogEntity, File logFolder) throws Exception {
        writeHtmlReport(suiteLogEntity, logFolder);

        // Write CSV file
        writeCSVReport(suiteLogEntity, logFolder);

        writeSimpleHTMLReport(suiteLogEntity, logFolder);

//        writeJsonReport(suiteLogEntity, logFolder);

        writeJUnitReport(suiteLogEntity, logFolder);
    }

    public static void writeLogRecordToJUnitFile(String logFolder) throws Exception {
        TestSuiteLogRecord testSuiteLogRecord = generate(logFolder);
        if (testSuiteLogRecord != null) {
            writeJUnitReport(testSuiteLogRecord, new File(logFolder));
        }
    }
    
    public static JUnitTestSuite generateJUnitTestSuite(TestSuiteLogRecord suiteLogEntity) {
        JUnitReportObjectFactory factory = new JUnitReportObjectFactory();

        String testSuiteName = suiteLogEntity.getName();
        String totalTests = suiteLogEntity.getTotalTestCases() + "";
        String totalError = suiteLogEntity.getTotalErrorTestCases() + "";
        String totalFailure = suiteLogEntity.getTotalFailedTestCases() + "";
        String duration = ((float)(suiteLogEntity.getEndTime() - suiteLogEntity.getStartTime()) / 1000) + "";

        JUnitProperties properties = factory.createProperties();
        List<JUnitProperty> propertyList = properties.getProperty();
        propertyList.add(new JUnitProperty("deviceName", suiteLogEntity.getDeviceName()));
        propertyList.add(new JUnitProperty("devicePlatform", suiteLogEntity.getDevicePlatform()));
        propertyList.add(new JUnitProperty("logFolder", StringEscapeUtils.escapeJava(suiteLogEntity.getLogFolder())));
        propertyList.add(new JUnitProperty("logFiles", factory.sanitizeReportLogs(suiteLogEntity)));
        propertyList.add(new JUnitProperty("attachments", factory.sanitizeReportAttachments(suiteLogEntity)));
        suiteLogEntity.getRunData().forEach((name, value) -> propertyList.add(new JUnitProperty(name, value)));

        JUnitTestSuite ts = factory.createTestSuite();
        ts.setProperties(properties);
        ts.setId(suiteLogEntity.getId());
        ts.setName(testSuiteName);
        ts.setHostname(suiteLogEntity.getHostName());
        ts.setTime(duration);
        ts.setTimestamp(DateUtil.getDateTimeFormatted(suiteLogEntity.getStartTime()));
        ts.setSystemOut(suiteLogEntity.getSystemOutMsg().trim());
        ts.setSystemErr(suiteLogEntity.getSystemErrorMsg().trim());

        // tests: The total number of tests in the suite, required
        ts.setTests(totalTests);
        // errors: The total number of tests in the suite that error
        ts.setErrors(totalError);
        // failures: The total number of tests in the suite that failed
        ts.setFailures(totalFailure);

        Arrays.asList(suiteLogEntity.getChildRecords()).stream().forEach(item -> {
            JUnitTestCase tc = factory.createTestCase();
            String time = ((float) (item.getEndTime() - item.getStartTime()) / 1000) + "";

            tc.setClassname(item.getId());
            tc.setName(item.getName());
            tc.setTime(time);
            
            TestStatus status = item.getStatus();
            TestStatusValue statusValue = status.getStatusValue();
            String statusName = statusValue.name();
            String message = StringUtils.removeStart(item.getMessage(),
                    item.getName() + " " + statusName + " because (of) ");
            tc.setStatus(statusName);
            if (TestStatusValue.ERROR == statusValue) {
                JUnitError error = factory.createError();
                error.setType(statusName);
                error.setMessage(message);
                tc.getError().add(error);
            }
            if (TestStatusValue.FAILED == statusValue) {
                JUnitFailure failure = factory.createFailure();
                failure.setType(statusName);
                failure.setMessage(message);
                tc.getFailure().add(failure);
            }

            tc.getSystemOut().add(item.getSystemOutMsg().trim());
            tc.getSystemErr().add(item.getSystemErrorMsg().trim());
            ts.getTestcase().add(tc);
        });

        return ts;
    }
    
    public static void writeJUnitReport(TestSuiteCollectionLogRecord suiteCollectionLogRecord, File logFolder)
            throws JAXBException, IOException {
        JUnitReportObjectFactory factory = new JUnitReportObjectFactory();
        List<JUnitTestSuite> tsList = new ArrayList<>();

        for(TestSuiteLogRecord suiteLogEntity : suiteCollectionLogRecord.getTestSuiteRecords()) {
            JUnitTestSuite ts = generateJUnitTestSuite(suiteLogEntity);
            tsList.add(ts);
        }

        String testSuiteCollectionName = suiteCollectionLogRecord.getTestSuiteCollectionId();
        String testSuiteCollectionTotalTests = suiteCollectionLogRecord.getTotalTestCases();
        String testSuiteCollectiontotalError = suiteCollectionLogRecord.getTotalErrorTestCases();
        String testSuiteCollectionTotalFailure = suiteCollectionLogRecord.getTotalFailedTestCases();
        String testSuiteCollectionDuration = ((float) (suiteCollectionLogRecord.getEndTime()
                - suiteCollectionLogRecord.getStartTime()) / 1000) + "";

        JUnitTestSuites tss = factory.createTestSuites();
        // errors: total number of tests with error result from all test suite
        tss.setErrors(testSuiteCollectiontotalError);
        // failures: total number of failed tests from all test suite
        tss.setFailures(testSuiteCollectionTotalFailure);
        // tests: total number of tests from all test suite
        tss.setTests(testSuiteCollectionTotalTests);
        // time: in seconds to execute all test suites
        tss.setTime(testSuiteCollectionDuration);
        // name
        tss.setName(testSuiteCollectionName);

        tss.getTestsuite().addAll(tsList);

        JAXBContext context = JAXBContext
                .newInstance(new Class[] { JUnitError.class, JUnitFailure.class, JUnitProperties.class,
                        JUnitProperty.class, JUnitTestCase.class, JUnitTestSuites.class, JUnitTestSuite.class });
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(tss, new File(logFolder, "JUnit_Report.xml"));
    }

    public static void writeJUnitReport(TestSuiteLogRecord suiteLogEntity, File logFolder)
            throws JAXBException, IOException {
    	
        JUnitReportObjectFactory factory = new JUnitReportObjectFactory();
        JUnitTestSuite ts = generateJUnitTestSuite(suiteLogEntity);

        // This is a single test suite. Thus, the info for the test suites is the same as test suite
        JUnitTestSuites tss = factory.createTestSuites();
        // errors: total number of tests with error result from all test suite
        tss.setErrors(ts.getErrors());
        // failures: total number of failed tests from all test suite
        tss.setFailures(ts.getFailures());
        // tests: total number of tests from all test suite
        tss.setTests(ts.getTests());
        // time: in seconds to execute all test suites
        tss.setTime(ts.getTime());
        // name
        tss.setName(ts.getName());

        tss.getTestsuite().add(ts);

        JAXBContext context = JAXBContext
                .newInstance(new Class[] { JUnitError.class, JUnitFailure.class, JUnitProperties.class,
                        JUnitProperty.class, JUnitTestCase.class, JUnitTestSuites.class, JUnitTestSuite.class });
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(tss, new File(logFolder, "JUnit_Report.xml"));
    }

//    public static void writeJsonReport(TestSuiteLogRecord suiteLogEntity, File logFolder) throws IOException {
//        List<String> excludedFieldNames = Arrays.asList(suiteLogEntity.getJsonExcludedFields());
//        ExclusionStrategy excludeFields = new ExclusionStrategy() {
//
//            @Override
//            public boolean shouldSkipField(FieldAttributes paramFieldAttributes) {
//                return excludedFieldNames.size() == 0 ? false
//                        : excludedFieldNames.contains(paramFieldAttributes.getName());
//            }
//
//            @Override
//            public boolean shouldSkipClass(Class<?> paramClass) {
//                return false;
//            }
//        };
//        String json = new GsonBuilder().addSerializationExclusionStrategy(excludeFields)
//                .create()
//                .toJson(suiteLogEntity);
//        FileUtils.writeStringToFile(new File(logFolder, "JSON_Report.json"), json, StringConstants.DF_CHARSET);
//    }

    public static void writeTSCollectionHTMLReport(String reportTitle, String tsReportsJson, File destDir)
            throws IOException, URISyntaxException {
        StringBuilder htmlSb = new StringBuilder();
        readFileToStringBuilder(ResourceLoader.HTML_COLLECTION_INDEX_TEMPLATE, htmlSb);
        String template = htmlSb.toString();
        template = StringUtils.replace(template, "REPORT_TITLE", reportTitle);
        template = StringUtils.replace(template, "TEST_SUITE_REPORT_LIST", tsReportsJson);
        FileUtils.writeStringToFile(new File(destDir, "index.html"), template, StringConstants.DF_CHARSET);

        htmlSb = new StringBuilder();
        readFileToStringBuilder(ResourceLoader.HTML_COLLECTION_FRAME_TEMPLATE, htmlSb);
        template = htmlSb.toString();
        template = StringUtils.replace(template, "REPORT_TITLE", reportTitle);
        template = StringUtils.replace(template, "TEST_SUITE_REPORT_LIST", tsReportsJson);
        FileUtils.writeStringToFile(new File(destDir, "index-frame-view.html"), template, StringConstants.DF_CHARSET);
    }

    public static void writeHtmlReport(TestSuiteLogRecord suiteLogEntity, File logFolder)
            throws IOException, URISyntaxException {
        StringBuilder htmlSb = prepareHtmlContent(suiteLogEntity);

        // Write main HTML Report
        FileUtils.writeStringToFile(new File(logFolder, logFolder.getName() + ".html"), htmlSb.toString(),
                StringConstants.DF_CHARSET);
    }

    private static StringBuilder prepareHtmlContent(TestSuiteLogRecord suiteLogEntity)
            throws IOException, URISyntaxException {
        List<String> strings = new ArrayList<String>();

        JsSuiteModel jsSuiteModel = new JsSuiteModel(suiteLogEntity, strings);
        StringBuilder sbModel = jsSuiteModel.toArrayString();

        StringBuilder htmlSb = new StringBuilder();
        readFileToStringBuilder(ResourceLoader.HTML_TEMPLATE_FILE, htmlSb);
        htmlSb.append(generateVars(strings, suiteLogEntity, sbModel));
        readFileToStringBuilder(ResourceLoader.HTML_TEMPLATE_CONTENT, htmlSb);
        return htmlSb;
    }

    public static void writeHtmlReportAppendHashCodeToName(TestSuiteLogRecord suiteLogEntity, File logFolder,
            int reportDirLocationHashCode) throws IOException, URISyntaxException {
        StringBuilder htmlSb = prepareHtmlContent(suiteLogEntity);

        FileUtils.writeStringToFile(new File(logFolder, logFolder.getName() + reportDirLocationHashCode + ".html"),
                htmlSb.toString(), StringConstants.DF_CHARSET);
    }
    
    public static void writeExecutionUUIDToFile(String UUID, File logFolder) throws IOException, URISyntaxException {
        FileUtils.writeStringToFile(new File(logFolder, "execution.uuid"),
        		UUID, StringConstants.DF_CHARSET);
    }

    public static void writeCSVReport(TestSuiteLogRecord suiteLogEntity, File logFolder) throws IOException {
        CsvWriter.writeCsvReport(suiteLogEntity, new File(logFolder, logFolder.getName() + ".csv"),
                Arrays.asList(suiteLogEntity.getChildRecords()));
    }

    public static void writeSimpleHTMLReport(TestSuiteLogRecord suiteLogEntity, File logFolder)
            throws IOException, URISyntaxException {
        List<ILogRecord> infoLogs = new ArrayList<ILogRecord>();
        collectInfoLines(suiteLogEntity, infoLogs);
        for (ILogRecord infoLog : infoLogs) {
            infoLog.getParentLogRecord().removeChildRecord(infoLog);
        }
        List<String> simpleStrings = new LinkedList<String>();
        JsSuiteModel simpleJsSuiteModel = new JsSuiteModel(suiteLogEntity, simpleStrings);
        StringBuilder simpleSbModel = simpleJsSuiteModel.toArrayString();
        StringBuilder simpleHtmlSb = new StringBuilder();
        readFileToStringBuilder(ResourceLoader.HTML_TEMPLATE_FILE, simpleHtmlSb);
        simpleHtmlSb.append(generateVars(simpleStrings, suiteLogEntity, simpleSbModel));
        readFileToStringBuilder(ResourceLoader.HTML_TEMPLATE_CONTENT, simpleHtmlSb);
        FileUtils.writeStringToFile(new File(logFolder, "Report.html"), simpleHtmlSb.toString(),
                StringConstants.DF_CHARSET);
    }

    public static void writeLogRecordToHTMLFile(TestSuiteLogRecord suiteLogEntity, File destFile,
            List<ILogRecord> filteredTestCases) throws IOException, URISyntaxException {

        List<String> strings = new LinkedList<String>();

        JsSuiteModel jsSuiteModel = new JsSuiteModel(suiteLogEntity, strings);
        StringBuilder sbModel = jsSuiteModel.toArrayString();

        StringBuilder htmlSb = new StringBuilder();
        readFileToStringBuilder(ResourceLoader.HTML_TEMPLATE_FILE, htmlSb);
        htmlSb.append(generateVars(strings, suiteLogEntity, sbModel));
        readFileToStringBuilder(ResourceLoader.HTML_TEMPLATE_CONTENT, htmlSb);

        List<ILogRecord> infoLogs = new ArrayList<ILogRecord>();
        collectInfoLines(suiteLogEntity, infoLogs);
        for (ILogRecord infoLog : infoLogs) {
            infoLog.getParentLogRecord().removeChildRecord(infoLog);
        }
        strings = new LinkedList<String>();
        jsSuiteModel = new JsSuiteModel(suiteLogEntity, strings, filteredTestCases);
        sbModel = jsSuiteModel.toArrayString();
        htmlSb = new StringBuilder();
        readFileToStringBuilder(ResourceLoader.HTML_TEMPLATE_FILE, htmlSb);
        htmlSb.append(generateVars(strings, suiteLogEntity, sbModel));
        readFileToStringBuilder(ResourceLoader.HTML_TEMPLATE_CONTENT, htmlSb);
        FileUtils.writeStringToFile(destFile, htmlSb.toString(), StringConstants.DF_CHARSET);
    }

    public static List<XmlLogRecord> getAllLogRecords(String logFolder)
            throws XMLParserException, IOException, XMLStreamException {
        return XMLLoggerParser.readFromLogFolder(logFolder);
    }

    public static TestSuiteLogRecord generate(String logFolder, IProgressMonitor progressMonitor)
            throws XMLParserException, IOException, XMLStreamException {
        return new TestSuiteXMLLogParser().readTestSuiteLogFromXMLFiles(logFolder, progressMonitor);
    }

    public static TestSuiteLogRecord generate(String logFolder)
            throws XMLParserException, IOException, XMLStreamException {
        return generate(logFolder, new NullProgressMonitor());
    }

    private static void readFileToStringBuilder(String fileName, StringBuilder sb)
            throws IOException, URISyntaxException {
        String path = ResourceLoader.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        path = URLDecoder.decode(path, "utf-8");
        File jarFile = new File(path);
        if (jarFile.isFile()) {
            JarFile jar = new JarFile(jarFile);
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String name = jarEntry.getName();
                if (name.endsWith(fileName)) {
                    StringBuilderWriter sbWriter = new StringBuilderWriter(new StringBuilder());
                    IOUtils.copy(jar.getInputStream(jarEntry), sbWriter);
                    sbWriter.flush();
                    sbWriter.close();
                    sb.append(sbWriter.getBuilder());
                    break;
                }
            }
            jar.close();
        } else { // Run with IDE
                 // sb.append(FileUtils.readFileToString(new
                 // File(ResourceLoader.class.getResource(fileName).getContent()
                 // )));
            InputStream is = (InputStream) ResourceLoader.class.getResource(fileName).getContent();
            sb.append(IOUtils.toString(is, "UTF-8"));
        }
    }
}
