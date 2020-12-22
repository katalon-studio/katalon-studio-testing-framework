package com.kms.katalon.core.testdata;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import com.kms.katalon.core.constants.StringConstants;
import com.kms.katalon.core.db.DatabaseConnection;
import com.kms.katalon.core.db.ListObjectResultSetHandler;
import com.kms.katalon.core.db.SqlRunner;

public class DBData extends AbstractTestData {
    private String query;

    private DatabaseConnection databaseConnection;

    private List<List<Object>> fetchedData;

    private List<String> columnNames;

    private ListObjectResultSetHandler rsHandler;

    private Date retrievedDate;

    public DBData(DatabaseConnection databaseConnection, String query) throws SQLException {
        super(databaseConnection.getConnectionUrl(), true);
        this.query = query;
        this.databaseConnection = databaseConnection;
        this.rsHandler = new ListObjectResultSetHandler();
        this.fetchedData = fetchData();
        this.columnNames = rsHandler.getColumnNames();
    }

    public List<List<Object>> getData() {
        return fetchedData;
    }

    /**
     * Collect all data of this test data
     * 
     * @return a {@link List} that contains all data of each rows, which each item is a {@link List} of raw data from
     * each cell in that row
     * @throws IOException if any io errors happened
     */
    @Override
    public List<List<Object>> getAllData() {
        return getData();
    }

    public Date getRetrievedDate() {
        return retrievedDate;
    }

    /**
     * Get the type of the test data
     * 
     * @see TestDataType
     * @return type of test data
     */
    @Override
    public TestDataType getType() {
        return TestDataType.DB_DATA;
    }

    /**
     * Get all column names of the test data
     * 
     * @return an array that contains names of all columns
     * @throws IOException if any io errors happened
     */
    @Override
    public String[] getColumnNames() {
        return rsHandler.getColumnNames().toArray(new String[getColumnNumbers()]);
    }

    /**
     * Get total rows of the test data
     * 
     * @return total rows of the test data
     */
    @Override
    public int getRowNumbers() {
        return fetchedData.size();
    }

    /**
     * Get total column of the test data
     * 
     * @return total columns of the test data
     * @throws IOException if any io errors happened
     */
    @Override
    public int getColumnNumbers() {
        return rsHandler.getColumnCount();
    }

    @Override
    protected Object internallyGetValue(int columnIndex, int rowIndex) throws IndexOutOfBoundsException {
        return fetchedData.get(rowIndex).get(columnIndex);
    }

    @Override
    protected Object internallyGetValue(String columnName, int rowIndex) throws IndexOutOfBoundsException {
        return internallyGetValue(columnNames.indexOf(columnName), rowIndex);
    }

    @Override
    protected void verifyColumnName(String columnName) throws IOException {
        if (columnNames.indexOf(columnName) == -1) {
            throw new IllegalArgumentException(
                    MessageFormat.format(StringConstants.TD_COLUMN_NAME_X_FOR_TEST_DATA_Y_INVALID, columnName,
                            getSourceUrl(), columnNames.toString()));
        }
    }

    private List<List<Object>> fetchData() throws SQLException {
        SqlRunner sqlRunner = null;
        try {
            sqlRunner = new SqlRunner(databaseConnection, query);
            return sqlRunner.query(rsHandler);
        } finally {
            if (sqlRunner != null) {
                sqlRunner.close();
            }
            retrievedDate = new Date();
        }
    }

    @Override
    public TestDataInfo getDataInfo() {
        if (databaseConnection == null) {
            return null;
        }

        return databaseConnection.getDBDataInfo();
    }
}
