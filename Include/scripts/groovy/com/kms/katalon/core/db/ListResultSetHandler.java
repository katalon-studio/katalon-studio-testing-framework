package com.kms.katalon.core.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.ResultSetHandler;

public abstract class ListResultSetHandler<T> implements ResultSetHandler<T> {

    protected List<String> columnNames;

    private int columnCount;

    public abstract T convert(ResultSet rs) throws SQLException;

    public List<String> getColumnNames() {
        return columnNames;
    }

    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public T handle(ResultSet rs) throws SQLException {
        getColumnNames(rs);
        return convert(rs);
    }

    protected void getColumnNames(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        columnCount = metaData.getColumnCount();

        // Handle column names
        columnNames = new ArrayList<String>();
        for (int i = 1; i <= columnCount; i++) {
            // beware of getColumnLabel() and getColumnName()
            columnNames.add(metaData.getColumnLabel(i));
        }
    }
}
