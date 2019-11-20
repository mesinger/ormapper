package mesi.orm.conn;

import java.sql.Connection;

/**
 * Establishes a connection to a SQLite database
 */
public final class SQLiteConnection extends DatabaseConnection {
    SQLiteConnection(String connectionstring) {
        super(JDBCDRIVERNAME.SQLITE, connectionstring);
    }

    /***
     * this constructor should only be used for unit testing
     * @param connectionstring
     * @param testConnection
     */
    SQLiteConnection(String connectionstring, Connection testConnection) {
        super(JDBCDRIVERNAME.SQLITE, connectionstring);
        this.rawConnection = testConnection;
    }

    @Override
    protected String tableExistsQuery() {
        return "SELECT name FROM sqlite_master WHERE type='table' AND name=?;";
    }
}
