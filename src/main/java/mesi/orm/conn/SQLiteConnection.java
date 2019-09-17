package mesi.orm.conn;

/**
 * Establishes a connection to a SQLite database
 */
public class SQLiteConnection extends DatabaseConnection {
    private static final String SQLITE_DRIVER_NAME = "org.sqlite.JDBC";

    SQLiteConnection(String connectionstring) {
        super(SQLITE_DRIVER_NAME, connectionstring);
    }
}
