package mesi.orm.persistence;

import com.google.inject.Guice;
import com.google.inject.Injector;
import mesi.orm.conn.RDBMS;
import mesi.orm.exception.ORMesiException;

interface ResultSetParserFactory {

    static ResultSetParser create(RDBMS databaseSystem) {

        if(databaseSystem == null) {
            throw new ORMesiException("I can't construct this ResultSetParser");
        }

        switch (databaseSystem) {
            case SQLITE:
                return new SQLiteResultSetParser();
            case MSSQL:
                throw new RuntimeException("todo");
            default:
                throw new ORMesiException("I can't construct this ResultSetParser");
        }
    }
}
