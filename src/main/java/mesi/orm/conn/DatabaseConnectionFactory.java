package mesi.orm.conn;

import mesi.orm.exception.ORMesiConnectionException;

/**
 * Factory for creating database connection objects for different database systems
 */
public interface DatabaseConnectionFactory {

    /**
     * Creates the desired DatabaseConnection and passes a connection string
     * to a DatabaseConnection, which will be used while a connection attempt
     * @param rdbmsType type of rdbms to be used
     * @param connectionString connection string used for connecting to the database
     * @return
     */
    static DatabaseConnection create(RDBMS rdbmsType, String connectionString) {

        if(rdbmsType == null) {
            throw new ORMesiConnectionException("Unsupported RDBMS");
        }

        if(connectionString == null || connectionString.length() == 0) {
            throw new ORMesiConnectionException("Invalid connection string");
        }

        switch (rdbmsType) {
            case SQLITE:
                return new SQLiteConnection(connectionString);
            case MSSQL:
                return new MSSQLConnection(connectionString);
            default:
                throw new ORMesiConnectionException("Unsupported RDBMS");
        }
    }
}
