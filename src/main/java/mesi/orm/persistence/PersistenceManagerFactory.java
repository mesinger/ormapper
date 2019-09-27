package mesi.orm.persistence;

import com.google.inject.Guice;
import com.google.inject.Injector;
import mesi.orm.conn.RDBMS;
import mesi.orm.exception.ORMesiException;

public interface PersistenceManagerFactory {

    static PersistenceManager create(RDBMS databaseSystem, String connectionString) {

        Injector dependencyInjector = null;

        if(databaseSystem == null) {
            throw new ORMesiException("I can't construct this PersistenceManager");
        }

        switch (databaseSystem) {
            case SQLITE:
                dependencyInjector = Guice.createInjector(new PersistenceManagerModule.SQLite(connectionString));
                break;
            case MSSQL:
                throw new RuntimeException("todo");
        }

        return dependencyInjector.getInstance(PersistenceManager.class);
    }
}
