package com.kms.katalon.core.testdata;

public class TestDataColumn {
    private String testDataId;
    private String columnName;
    private String variableName;

    public TestDataColumn(String testDataId, String name) {
        this.testDataId = testDataId;
        this.columnName = name;
    }

    public String getTestDataId() {
        return testDataId;
    }

    public void setTestDataId(String testDataId) {
        this.testDataId = testDataId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }
    
    public String getNewClassScript() {
        return "new " + TestDataColumn.class.getSimpleName() + "('" + testDataId + "', '" + columnName + "')";
    }
}
