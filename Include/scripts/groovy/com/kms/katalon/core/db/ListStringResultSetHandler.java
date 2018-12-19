package com.kms.katalon.core.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;

public class ListStringResultSetHandler extends ListResultSetHandler<List<List<String>>> {

    @Override
    public List<List<String>> convert(ResultSet rs) throws SQLException {
        // Handle result set
        List<List<String>> result = new ArrayList<List<String>>();
        while (rs.next()) {
            List<String> row = new ArrayList<String>();
            for (int i = 0; i < getColumnCount(); i++) {
                row.add(ObjectUtils.toString(rs.getObject(i + 1), null));
            }
            result.add(row);
        }
        return result;
    }

}
