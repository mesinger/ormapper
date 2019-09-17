package mesi.orm.conn;

import mesi.orm.exception.ORMesiConnectionException;

/**
 * Factory for creating database connection objects for different database systems
 */
public class DatabaseConnectionFactory {

    /**
     * Creates the desired DatabaseConnection and passes a connection string
     * to a DatabaseConnection, which will be used while a connection attempt
     * @param rdbmsType type of rdbms to be used
     * @param connectionString connection string used for connecting to the database
     * @return
     */
    public DatabaseConnection create(RDBMS rdbmsType, String connectionString) {
        switch (rdbmsType) {
            case SQLITE:
                return new SQLiteConnection(connectionString);
            case MSSQL:
                throw new RuntimeException("missing impl for mssql");
            default:
                throw new ORMesiConnectionException("Unsupported RDBMS");
        }
    }
}
