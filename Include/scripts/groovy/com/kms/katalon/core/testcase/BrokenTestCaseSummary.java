package com.kms.katalon.core.testcase;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;

import com.kms.katalon.core.execution.TestExecutionStringUtil;
import com.kms.katalon.core.logging.XmlLogRecord;
import com.kms.katalon.core.logging.model.ILogRecord;
import com.kms.katalon.core.logging.model.TestCaseLogRecord;
import com.kms.katalon.core.logging.model.TestStatus.TestStatusValue;
import com.kms.katalon.core.logging.model.TestStepLogRecord;

public class BrokenTestCaseSummary {

    public static class Constants {
        public static String WEB_ELEMENT_NOT_FOUND_EXCEPTION = "WebElementNotFoundException";

        public static String MHTML_EXTENSION = "mhtml";
    }

    public static class Builder {

        /**
         * Build a {@link BrokenTestCaseSummary} from the given
         * {@link ILogRecord} which is provided in the Report Part.
         * 
         * @param {@link
         * {@link ILogRecord} element
         * @return
         */
        public static BrokenTestCaseSummary buildFrom(ILogRecord element) {
            if (element instanceof TestCaseLogRecord) {
                TestCaseLogRecord tcLogRecord = (TestCaseLogRecord) element;
                Optional<TestStepLogRecord> optWebElementNotFoundTestStep = Arrays.asList(tcLogRecord.getChildRecords())
                        .stream()
                        .filter(a -> a instanceof TestStepLogRecord)
                        .map(a -> (TestStepLogRecord) a)
                        .filter(a -> !a.getStatus().equals(TestStatusValue.PASSED))
                        .filter(a -> !StringUtils.isEmpty(a.getMessage())
                                && a.getMessage().contains("com.kms.katalon.core.webui.exception"))
                        .findFirst();
                if (optWebElementNotFoundTestStep.isPresent()) {
                    TestStepLogRecord webElementNotFoundTestStep = optWebElementNotFoundTestStep.get();
                    String testObject = getRawString(BrokenTestCaseSummary.Utils
                            .extractFirstTestObject(webElementNotFoundTestStep.getMessage()));
                    String testCase = getRawString(tcLogRecord.getName());
                    String rootCause = getRawString(
                            BrokenTestCaseSummary.Utils.getCausedBySentence(webElementNotFoundTestStep.getMessage()));
                    String errorMessage = getRawString(webElementNotFoundTestStep.getMessage());
                    return new BrokenTestCaseSummary(testCase, testObject, rootCause, errorMessage);
                }
            }
            return null;
        }

        /**
         * Build a {@link BrokenTestCaseSummary} by extracting information from
         * the given {@link XmlLogRecord} and testCaseId which is provided in
         * Log Viewer
         * 
         * @param result
         * {@link XmlLogRecord} The log record of that Test Case
         * @param testCaseId
         * The ID of Test Case
         * @return {@link BrokenTestCaseSummary}
         */
        public static BrokenTestCaseSummary buildFrom(XmlLogRecord result, String testCaseId) {
            String tcId = getRawString(testCaseId);
            String testObject = getRawString(BrokenTestCaseSummary.Utils.extractFirstTestObject(result.getMessage()));
            String rootCause = getRawString(BrokenTestCaseSummary.Utils.getCausedBySentence(result.getMessage()));
            String errorMessage = result.getMessage();
            BrokenTestCaseSummary brokenTestCaseSummary = new BrokenTestCaseSummary(tcId, testObject, rootCause,
                    errorMessage);
            return brokenTestCaseSummary;
        }
    }

    public static class Utils {
        public static String getCausedBySentence(String msg) {
            String causedBy = "";
            try {
                causedBy = msg.substring(msg.indexOf("Caused by:"));
                causedBy = causedBy.substring(0, causedBy.indexOf("\n"));
            } catch (Exception e) {
                return "";
            }
            return causedBy;
        }

        public static String extractFirstTestObject(String msg) {
            String testObject = "";
            try {
                testObject = msg.substring(msg.indexOf("'Object Repository"));
                testObject = testObject.substring(0, testObject.indexOf("'", 1) + 1);
            } catch (Exception e) {
                return "";
            }
            return testObject;
        }

        public static String getFolderWithLatestTimeStamp(String logFolder) throws IOException {
            return Files.list(Paths.get(logFolder))
                    .filter(f -> Files.isDirectory(f))
                    .max(Comparator.comparingLong(f -> f.toFile().lastModified()))
                    .map(f -> f.toFile().getAbsolutePath())
                    .orElse("");
        }
    }

    private String tc;

    private String to;

    private String rc;

    private String em;

    private String pathTomhtml;

    public BrokenTestCaseSummary(String testCase, String testObject, String rootCause, String errorMessage) {
        this.tc = testCase;
        this.to = testObject;
        this.rc = rootCause;
        this.em = errorMessage;
    }

    public String getTestCase() {
        return tc;
    }

    public String getTestObject() {
        return to;
    }

    public String getRootCause() {
        return rc;
    }

    public String getErrorMessage() {
        return em;
    }

    private static String getRawString(String str) {
        return str.replace("'", "");
    }

    public void setMhtml(String pathToMhtml) {
        this.pathTomhtml = pathToMhtml;
    }

    public String getMhtml() {
        return pathTomhtml;
    }

    /**
     * Look for the first file with extension `mhtml` in the folder specified by
     * the argument and set it to internal mhtml attribute. If you want to look
     * for a MHTML file of a specific test case, use
     * {@link BrokenTestCaseSummary#searchAndSetMhtmlFile(String, String)}
     * 
     * @param logFolder
     */
    public void searchAndSetMhtmlFile(String logFolder) {
        File f = new File(logFolder);
        File[] matchingFiles = f.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(Constants.MHTML_EXTENSION);
            }
        });
        if (matchingFiles != null && matchingFiles.length > 0) {
            setMhtml(matchingFiles[0].getAbsolutePath());
        }
    }

    /**
     * Look for the first MHTML matching the provided test case Id
     * 
     * @param logFolder
     * @param testCaseId
     */
    public void searchAndSetMhtmlFile(String logFolder, String testCaseId) {
        File f = new File(logFolder);
        File[] matchingFiles = f.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return (TestExecutionStringUtil.getUnoffensiveTestCaseName(testCaseId) + "."
                        + Constants.MHTML_EXTENSION).equals(name);
            }
        });
        if (matchingFiles.length > 0) {
            setMhtml(matchingFiles[0].getAbsolutePath());
        }
    }
}
