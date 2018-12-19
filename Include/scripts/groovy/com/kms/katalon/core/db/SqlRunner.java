package com.kms.katalon.core.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;

import com.kms.katalon.core.constants.StringConstants;

public class SqlRunner implements AutoCloseable {
    private Connection connection;

    private PreparedStatement preparedStatement;

    private QueryRunner queryRunner;

    private String query;

    private Object[] params;

    /**
     * @param databaseConnection Database Connection
     * @param query SQL query
     * @throws SQLException
     * @see {@link com.kms.katalon.core.db.DatabaseConnection#DatabaseConnection(DatabaseType, String)}
     */
    public SqlRunner(DatabaseConnection databaseConnection, String query) throws SQLException {
        this(databaseConnection.getConnection(), query);
    }

    /**
     * @param databaseConnection Database Connection
     * @param query SQL query
     * @param params Parameter objects for query
     * @throws SQLException
     * @see {@link com.kms.katalon.core.db.DatabaseConnection#DatabaseConnection(DatabaseType, String)}
     */
    public SqlRunner(DatabaseConnection databaseConnection, String query, Object[] params) throws SQLException {
        this(databaseConnection.getConnection(), query, params);
    }

    /**
     * @param connection Connection
     * @param query SQL query
     * @throws SQLException
     * @see {@link java.sql.Connection}
     */
    public SqlRunner(Connection connection, String query) throws SQLException {
        if (connection == null || connection.isClosed()) {
            throw new SQLException(StringConstants.EXC_DATABASE_CONNECTION_IS_CLOSED);
        }
        prepare(connection, query);
    }

    /**
     * @param connection Connection
     * @param query SQL query
     * @param params Parameter objects for query
     * @throws SQLException
     * @see {@link java.sql.Connection}
     */
    public SqlRunner(Connection connection, String query, Object[] params) throws SQLException {
        this(connection, query);
        this.params = params;
        queryRunner.fillStatement(preparedStatement, this.params);
    }

    /**
     * Prepare SQL statement
     * 
     * @param conn Connection
     * @param query SQL query
     * @throws SQLException
     */
    private void prepare(Connection conn, String query) throws SQLException {
        connection = conn;
        // Disable auto commit
        connection.setAutoCommit(false);
        // Enable read-only
        connection.setReadOnly(true);

        // for advance purpose
        queryRunner = new QueryRunner();
        this.query = query;

        preparedStatement = connection.prepareStatement(this.query);
        preparedStatement.closeOnCompletion();
    }

    /**
     * Execute the given SELECT SQL query.
     * 
     * @return ResultSet
     * @throws SQLException
     */
    public ResultSet query() throws SQLException {
        return preparedStatement.executeQuery();
    }

    /**
     * Execute the given SQL SELECT query. The caller is responsible for closing the connection.
     * 
     * @param resultSetHandler The handler that converts the results into an object.
     * @return The object returned by the handler.
     * @throws SQLException
     * @see {@link ListResultSetHandler}
     */
    public <T> T query(ListResultSetHandler<T> resultSetHandler) throws SQLException {
        if (params == null) {
            return queryRunner.query(connection, query, resultSetHandler);
        }
        return queryRunner.query(connection, query, resultSetHandler, params);
    }

    /**
     * Close connection, avoid closing if null and hide any SQLExceptions that occur.
     * 
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() {
        DbUtils.closeQuietly(connection);
    }

    public boolean isConnectionAlive() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
