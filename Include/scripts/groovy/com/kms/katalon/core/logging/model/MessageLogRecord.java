package com.kms.katalon.core.logging.model;

import java.io.File;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

public class MessageLogRecord extends AbstractLogRecord {
    private TestStatus testStatus;

    private String attachment;

    public MessageLogRecord() {
        super("");
        setType(ILogRecord.LOG_TYPE_MESSAGE);
    }

    public void setStatus(TestStatus testStatus) {
        this.testStatus = testStatus;
    }

    @Override
    public TestStatus getStatus() {
        return testStatus;
    }

    public String getAttachment() {
        return getAttachment(false);
    }

    public String getAttachment(boolean isInAbsolutePath) {
        if (!isInAbsolutePath || StringUtils.isBlank(attachment)) {
            // keep as it is
            return attachment;
        }
        String logFolder = getLogFolder();
        if (!attachment.contains(StringEscapeUtils.escapeJava(logFolder))) {
            if (attachment.contains(File.separator)) {
                // custom absolute file path
                return attachment;
            }
            // relative file path to log folder
            return StringEscapeUtils.escapeJava(logFolder + File.separator + attachment);
        }
        // absolute file path to log folder
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    @Override
    public String getJUnitMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getJUnitMessage());
        String attachmentFile = getAttachment(true);
        if (StringUtils.isNotBlank(attachmentFile)) {
            sb.append("[[ATTACHMENT|" + attachmentFile + "]]");
            sb.append(LINE_SEPARATOR);
        }
        return sb.toString();
    }

    private String getLogFolder() {
        TestSuiteLogRecord testSuiteLogRecord = (TestSuiteLogRecord) getParentLogRecordByType(this.getParentLogRecord(),
                ILogRecord.LOG_TYPE_TEST_SUITE);
        return testSuiteLogRecord.getLogFolder();
    }

}
