package mesi.orm.conn;

import mesi.orm.exception.ORMesiException;
import mesi.orm.query.Query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Base class for connections to different rdbm systems
 */
public abstract class DatabaseConnection implements DatabaseConnector, DatabaseModifier, DatabaseReader {

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
            throw new ORMesiException("Missing driver for " + DRIVER_CLASS_NAME);
        } catch (SQLException e) {
            throw new ORMesiException("Error while connecting to database.\n" + e.getMessage());
        }
    }

    @Override
    public boolean close() {

        try {

            rawConnection.close();

            isConnectionEstablished = false;

            return true;

        } catch (SQLException e) {
            throw new ORMesiException("Error while closing connection to database.\n" + e.getMessage());
        }
    }

    @Override
    public void createTable(Query query) {

        final String sql = query.raw();

        try {

            rawConnection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            rawConnection.setAutoCommit(false);

            var stmt = rawConnection.createStatement();
            stmt.execute(sql);

            rawConnection.commit();
            rawConnection.setAutoCommit(true);

            stmt.close();

        } catch (SQLException e) {
            throw new ORMesiException("error while processing query " + e.getMessage());
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
            throw new ORMesiException("error while processing query " + e.getMessage());
        }
    }

    @Override
    public long insert(Query query) {

        try {

            rawConnection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            rawConnection.setAutoCommit(false);

            var stmt = rawConnection.createStatement();
            stmt.execute(query.raw());

            var rs = stmt.getGeneratedKeys();
            if(rs.next()) {
                long generatedId = rs.getLong(1);

                rawConnection.commit();
                rawConnection.setAutoCommit(true);

                stmt.close();

                return generatedId;
            }
            else {
                rawConnection.rollback();
                rawConnection.setAutoCommit(true);
                stmt.close();

                throw new ORMesiException("cannot retrieve generated id while inserting persistent object");
            }


        } catch (SQLException e) {
            throw new ORMesiException("error while processing query " + e.getMessage());
        }
    }

    @Override
    public DisposeableResultSet select(Query query) {

        try {

            rawConnection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            rawConnection.setAutoCommit(false);

            var stmt = rawConnection.createStatement();
            var resultSet = stmt.executeQuery(query.raw());

            rawConnection.commit();

            return new DisposeableResultSet(resultSet, stmt);

        } catch (SQLException e) {
            throw new ORMesiException("error while processing query " + e.getMessage());
        }
    }

    // abstract query creation functions
    protected abstract String tableExistsQuery();
}
