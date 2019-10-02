package mesi.orm.conn;

import mesi.orm.exception.ORMesiSqlException;
import mesi.orm.persistence.PersistentField;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Establishes a connection to a SQLite database
 */
class SQLiteConnection extends DatabaseConnection {
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
    public void createTable(String tableName, TableEntry... entries) {

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

            rawConnection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            rawConnection.setAutoCommit(false);

            var preparedStatement = rawConnection.prepareStatement(query);
            preparedStatement.setString(1, tableName);

            boolean tableExists = preparedStatement.execute();

            rawConnection.commit();
            rawConnection.setAutoCommit(true);

            preparedStatement.close();

            return tableExists;

        } catch (SQLException e) {
            throw new ORMesiSqlException("error while processing query " + e.getMessage());
        }
    }

    @Override
    public void insert(String tableName, PersistentField... fields) {

        StringBuilder query = new StringBuilder("INSERT INTO " + tableName + " (");

        final int numberOfFields = fields.length;

        var columnNames = Arrays.stream(fields).map(field -> field.getName()).collect(Collectors.toList());
        var values = Arrays.stream(fields).map(field -> {
            if(TableEntry.getTypeOf(field.getValue()).equals(TableEntryType.STRING)) {
                return "'" + field.getValue() + "'";
            }
            else {
                return field.getValue();
            }
        }).collect(Collectors.toList());

        for(var columName : columnNames) {
            query.append(columName + ", ");
        }

        query.setLength(query.length() - 2);
        query.append(") VALUES (");

        for(var value: values) {
            query.append(value + ", ");
        }

        query.setLength(query.length() - 2);
        query.append(");");

        try {

            rawConnection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            rawConnection.setAutoCommit(false);

            var stmt = rawConnection.createStatement();
            stmt.execute(query.toString());

            rawConnection.commit();
            rawConnection.setAutoCommit(true);

            stmt.close();

        } catch (SQLException e) {
            throw new ORMesiSqlException("error while processing query " + e.getMessage());
        }
    }

    private String createTableQuery(String tableName, TableEntry... entries) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE " + tableName + " (\n");

        sql.append(TableEntryTranslator.sqlite(entries));

        sql.append("\n);");

        return sql.toString();
    }
}
