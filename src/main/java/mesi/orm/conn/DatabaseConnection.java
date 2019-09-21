package mesi.orm.conn;

import mesi.orm.exception.ORMesiConnectionException;

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
}
