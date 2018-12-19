package com.kms.katalon.core.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Transform <code>java.sql.ResultSet</code> data into <code>List&lt;List&lt;Object&gt;&gt;</code>
 * 
 * @see java.sql.ResultSet
 * @see org.apache.commons.dbutils.ResultSetHandler
 */
public class ListObjectResultSetHandler extends ListResultSetHandler<List<List<Object>>> {

    @Override
    public List<List<Object>> convert(ResultSet rs) throws SQLException {
        List<List<Object>> result = new ArrayList<List<Object>>();
        while (rs.next()) {
            List<Object> row = new ArrayList<Object>();
            for (int i = 1; i <= getColumnCount(); i++) {
                row.add(rs.getObject(i));
            }
            result.add(row);
        }
        return result;
    }

}
