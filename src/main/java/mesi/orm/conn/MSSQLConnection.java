package mesi.orm.conn;

final class MSSQLConnection extends DatabaseConnection{
    MSSQLConnection(String connectionstring) {
        super(JDBCDRIVERNAME.MSSQL, connectionstring);
    }

    @Override
    protected String tableExistsQuery() {
        throw new RuntimeException("todo");
    }
}
