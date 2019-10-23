package com.kms.katalon.core.testdata.reader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.kms.katalon.core.logging.model.ILogRecord;
import com.kms.katalon.core.logging.model.TestCaseLogRecord;
import com.kms.katalon.core.logging.model.TestStatus;
import com.kms.katalon.core.logging.model.TestStatus.TestStatusValue;
import com.kms.katalon.core.logging.model.TestSuiteLogRecord;
import com.kms.katalon.core.util.internal.DateUtil;

public class CsvWriter {

    private static final CellProcessor[] SUMMARY_PROCESSORS = new CellProcessor[] { 
            new NotNull(), // Suite Name
            new NotNull(), // Test Name
            new Optional(), // Browser
            new Optional(), // Description
            new Optional(), // Tag
            new NotNull(), // Start time
            new Optional(), // End Time
            new Optional(), // Duration
            new NotNull() // Status
            };

    private static final CellProcessor[] DETAILS_PROCESSORS = new CellProcessor[] { 
            new NotNull(), // Suite/Test/Step Name
            new Optional(), // Browser
            new Optional(), // Description
            new Optional(), // Tag
            new NotNull(), // Start time
            new Optional(), // End Time
            new Optional(), // Duration
            new NotNull() // Status
            };

    public static final String[] SUMMARY_HEADER = new String[] {
            "Suite Name",
            "Test Name",
            "Browser",
            "Description",
            "Tag",
            "Start time",
            "End time",
            "Duration",
            "Status" };

    public static final String[] DETAILS_HEADER = new String[] {
            "Suite/Test/Step Name",
            "Browser",
            "Description",
            "Tag",
            "Start time",
            "End time",
            "Duration",
            "Status" };

    public static void writeCsvReport(TestSuiteLogRecord suiteLog, File file, List<ILogRecord> filteredTestCaseRecords)
            throws IOException {
        writeCsvReport(suiteLog, file, filteredTestCaseRecords, true);
    }

    public static void writeCsvReport(TestSuiteLogRecord suiteLog, File file, List<ILogRecord> filteredTestCaseRecords,
            boolean stepsIncluded) throws IOException {
        ICsvListWriter csvWriter = new CsvListWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8), CsvPreference.STANDARD_PREFERENCE);

        try {
            // Test Suite
            String browser = suiteLog.getBrowser();

            csvWriter.writeHeader(DETAILS_HEADER);

            writeRecord(csvWriter, suiteLog, browser);

            // Test cases
            for (ILogRecord testLog : filteredTestCaseRecords) {
                // Blank line
                csvWriter.write(Arrays.asList(new Object[] { "", "", "", "", "", "", "", "" }), DETAILS_PROCESSORS);
                // Write test case line
                writeRecord(csvWriter, testLog, browser);

                // Test steps
                if (!stepsIncluded) {
                    continue;
                }
                for (ILogRecord step : testLog.getChildRecords()) {
                    writeRecord(csvWriter, step, browser);
                }
            }
        } finally {
            IOUtils.closeQuietly(csvWriter);
        }
    }

    private static void writeRecord(ICsvListWriter csvWriter, ILogRecord logRecord, String browserName)
            throws IOException {
        String tag = "";
        if (logRecord instanceof TestCaseLogRecord) {
            tag = StringUtils.defaultString(((TestCaseLogRecord) logRecord).getTag());
        }
        TestStatus status = logRecord.getStatus();
        if (logRecord instanceof TestSuiteLogRecord) {
            status = new TestStatus();
            status.setStatusValue(((TestSuiteLogRecord) logRecord).getSummaryStatus());
        }
        List<Object> writtenObjects =
                Arrays.asList(new Object[] {
                        logRecord.getName(),
                        browserName,
                        logRecord.getDescription(),
                        tag,
                        DateUtil.getDateTimeFormatted(logRecord.getStartTime()),
                        DateUtil.getDateTimeFormatted(logRecord.getEndTime()),
                        DateUtil.getElapsedTime(logRecord.getStartTime(), logRecord.getEndTime()),
                        status != null ? status.getStatusValue().name() : "" });
        csvWriter.write(writtenObjects, DETAILS_PROCESSORS);
    }

    public static void writeArraysToCsv(String[] header, List<Object[]> datas, File csvFile) throws IOException {
        ICsvListWriter csvWriter = null;
        try {
            csvWriter = new CsvListWriter(new OutputStreamWriter(new FileOutputStream(csvFile), StandardCharsets.UTF_8), CsvPreference.STANDARD_PREFERENCE);
            csvWriter.writeHeader(header);
            for (Object[] arr : datas) {
                csvWriter.write(Arrays.asList(arr), SUMMARY_PROCESSORS);
            }
        } finally {
            IOUtils.closeQuietly(csvWriter);
        }
    }

    public static void writeArraysToCsv(String[] header, List<Object[]> datas, File csvFile,
            CellProcessor[] cellProcessor) throws IOException {
        ICsvListWriter csvWriter = null;
        try {
            csvWriter = new CsvListWriter(new OutputStreamWriter(new FileOutputStream(csvFile), StandardCharsets.UTF_8),
                    CsvPreference.STANDARD_PREFERENCE);
            csvWriter.writeHeader(header);
            for (Object[] arr : datas) {
                csvWriter.write(Arrays.asList(arr), cellProcessor);
            }
        } finally {
            IOUtils.closeQuietly(csvWriter);
        }
    }
}
