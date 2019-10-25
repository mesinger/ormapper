package mesi.orm.conn;

import mesi.orm.persistence.PersistentField;

final class MSSQLConnection extends DatabaseConnection{
    MSSQLConnection(String connectionstring) {
        super(JDBCDRIVERNAME.MSSQL, connectionstring);
    }

    @Override
    protected String tableExistsQuery() {
        throw new RuntimeException("todo");
    }
}
