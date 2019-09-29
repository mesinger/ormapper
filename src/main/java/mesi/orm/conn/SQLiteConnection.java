package mesi.orm.conn;

import mesi.orm.exception.ORMesiSqlException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Establishes a connection to a SQLite database
 */
public class SQLiteConnection extends DatabaseConnection {
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
    public void createTable(String tableName, TableDescriptor... entries) {

        final String sql = createTableQuery(tableName, entries);

        try {

            var stmt = rawConnection.createStatement();
            stmt.execute(sql);

            stmt.close();

        } catch (SQLException e) {
            throw new ORMesiSqlException("error while processing query " + e.getMessage());
        }
    }

    @Override
    public boolean tableExists(String tableName) {

        final String query = "SELECT name FROM sqlite_master WHERE type='table' AND name=?;";

        try {

            var preparedStatement = rawConnection.prepareStatement(query);
            preparedStatement.setString(1, tableName);

            boolean tableExists = preparedStatement.execute();

            preparedStatement.close();

            return tableExists;

        } catch (SQLException e) {
            throw new ORMesiSqlException("error while processing query " + e.getMessage());
        }
    }

    private String createTableQuery(String tableName, TableDescriptor... entries) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE " + tableName + " (\n");

        sql.append(TableDescriptorTranslator.sqlite(entries));

        sql.append("\n);");

        return sql.toString();
    }
}
