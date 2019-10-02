package mesi.orm.conn;

import mesi.orm.persistence.PersistentField;

class MSSQLConnection extends DatabaseConnection{
    MSSQLConnection(String connectionstring) {
        super(JDBCDRIVERNAME.MSSQL, connectionstring);
    }


    @Override
    protected String createTableQuery(String tableName, TableEntry... entries) {
        throw new RuntimeException("todo");
    }

    @Override
    protected String tableExistsQuery() {
        throw new RuntimeException("todo");
    }

    @Override
    protected String insertQuery(String tableName, PersistentField... fields) {
        throw new RuntimeException("todo");
    }
}
