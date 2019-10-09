package mesi.orm.conn;

import mesi.orm.exception.ORMesiConnectionException;
import mesi.orm.exception.ORMesiSqlException;
import mesi.orm.persistence.PersistentField;
import mesi.orm.persistence.PersistentStructure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Base class for connections to different rdbm systems
 */
public abstract class DatabaseConnection implements DatabaseConnector, DatabaseModifier {

    protected final String DRIVER_CLASS_NAME;
    protected final String JDBC_URL;
    protected Connection rawConnection;

    private boolean isConnectionEstablished = false;

    protected DatabaseConnection(String driver, String jdbcurl) {
        DRIVER_CLASS_NAME = driver;
        JDBC_URL = jdbcurl;
    }

    @Override
    public boolean open() {

        if(isConnectionEstablished) return false;

        try {

            Class.forName(DRIVER_CLASS_NAME);
            rawConnection = DriverManager.getConnection(JDBC_URL);

            isConnectionEstablished = true;

            return true;

        } catch (ClassNotFoundException e) {
            throw new ORMesiConnectionException("Missing driver for " + DRIVER_CLASS_NAME);
        } catch (SQLException e) {
            throw new ORMesiConnectionException("Error while connecting to database.\n" + e.getMessage());
        }
    }

    @Override
    public boolean close() {

        try {

            rawConnection.close();

            isConnectionEstablished = false;

            return true;

        } catch (SQLException e) {
            throw new ORMesiConnectionException("Error while closing connection to database.\n" + e.getMessage());
        }
    }

    @Override
    public void createTable(String tableName, TableEntry... entries) {

        final String sql = createTableQuery(tableName, entries);

        try {

            rawConnection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            rawConnection.setAutoCommit(false);

            var stmt = rawConnection.createStatement();
            stmt.execute(sql);

            rawConnection.commit();
            rawConnection.setAutoCommit(true);

            stmt.close();

        } catch (SQLException e) {
            throw new ORMesiSqlException("error while processing query " + e.getMessage());
        }
    }

    @Override
    public boolean tableExists(String tableName) {

        final String query = tableExistsQuery();

        try {

            rawConnection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            rawConnection.setAutoCommit(false);

            var preparedStatement = rawConnection.prepareStatement(query);
            preparedStatement.setString(1, tableName);

            var resultSet = preparedStatement.executeQuery();
            boolean tableExists = resultSet.next();

            rawConnection.commit();
            rawConnection.setAutoCommit(true);

            resultSet.close();
            preparedStatement.close();

            return tableExists;

        } catch (SQLException e) {
            throw new ORMesiSqlException("error while processing query " + e.getMessage());
        }
    }

    @Override
    public void insert(String tableName, PersistentField... fields) {

        final var query = insertQuery(tableName, fields);

        try {

            rawConnection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            rawConnection.setAutoCommit(false);

            var stmt = rawConnection.createStatement();
            stmt.execute(query);

            rawConnection.commit();
            rawConnection.setAutoCommit(true);

            stmt.close();

        } catch (SQLException e) {
            throw new ORMesiSqlException("error while processing query " + e.getMessage());
        }
    }

    // abstract query creation functions
    protected abstract String createTableQuery(String tableName, TableEntry... entries);
    protected abstract String tableExistsQuery();
    protected abstract String insertQuery(String tableName, PersistentField... fields);
}
