package mesi.orm.conn;

import mesi.orm.exception.ORMesiException;

/**
 * Factory for creating database connection objects for different database systems
 */
public interface DatabaseConnectionFactory {

    /**
     * Creates the desired DatabaseConnection and passes a connection string
     * to a DatabaseConnection, which will be used while a connection attempt
     * @param databaseSystemType type of rdbms to be used
     * @param connectionString connection string used for connecting to the database
     * @return
     */
    static DatabaseConnection create(DatabaseSystem databaseSystemType, String connectionString) {

        if(databaseSystemType == null) {
            throw new ORMesiException("Unsupported RDBMS");
        }

        if(connectionString == null || connectionString.length() == 0) {
            throw new ORMesiException("Invalid connection string");
        }

        switch (databaseSystemType) {
            case SQLITE:
                return new SQLiteConnection(connectionString);
            case MSSQL:
                return new MSSQLConnection(connectionString);
            default:
                throw new ORMesiException("Unsupported RDBMS");
        }
    }
}
