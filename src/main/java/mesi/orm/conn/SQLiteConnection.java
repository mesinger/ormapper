package mesi.orm.conn;

import mesi.orm.persistence.PersistentField;

import java.sql.Connection;
import java.util.Arrays;
import java.util.stream.Collectors;

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
    protected String createTableQuery(String tableName, TableEntry... entries) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE " + tableName + " (\n");

        sql.append(TableEntryTranslator.sqlite(entries));

        sql.append("\n);");

        return sql.toString();
    }

    @Override
    protected String tableExistsQuery() {
        return "SELECT name FROM sqlite_master WHERE type='table' AND name=?;";
    }

    @Override
    protected String insertQuery(String tableName, PersistentField... fields) {

        StringBuilder query = new StringBuilder("INSERT INTO " + tableName + " (");

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

        return query.toString();
    }
}
