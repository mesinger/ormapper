package mesi.orm.conn;

class MSSQLConnection extends DatabaseConnection{
    MSSQLConnection(String connectionstring) {
        super(JDBCDRIVERNAME.MSSQL, connectionstring);
    }

    @Override
    public void createTable(String tableName, TableEntry... entries) {
        throw new RuntimeException("todo");
    }
}
