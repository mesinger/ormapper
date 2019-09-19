package mesi.orm.conn;

/**
 * Establishes a connection to a SQLite database
 */
public class SQLiteConnection extends DatabaseConnection {
    SQLiteConnection(String connectionstring) {
        super(JDBCDRIVERNAME.SQLITE, connectionstring);
    }
}
