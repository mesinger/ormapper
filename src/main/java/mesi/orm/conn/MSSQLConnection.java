package mesi.orm.conn;

class MSSQLConnection extends DatabaseConnection{
    MSSQLConnection(String connectionstring) {
        super(JDBCDRIVERNAME.MSSQL, connectionstring);
    }
}
