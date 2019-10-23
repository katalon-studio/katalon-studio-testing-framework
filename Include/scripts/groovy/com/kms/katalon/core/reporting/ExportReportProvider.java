package com.kms.katalon.core.reporting;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public interface ExportReportProvider {

    String[] getSupportedTypeForTestSuite();

    String[] getSupportedTypeForTestSuiteCollection();

    File exportTestSuite(File exportLocation, String projectDir, String reportId, String formatType)
            throws IOException, URISyntaxException;

    File exportTestSuiteCollection(File exportLocation, String projectDir, String reportId, String formatType)
            throws IOException, URISyntaxException;
}
