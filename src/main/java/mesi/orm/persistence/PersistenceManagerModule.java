package mesi.orm.persistence;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import mesi.orm.conn.DatabaseConnection;
import mesi.orm.conn.DatabaseConnectionFactory;
import mesi.orm.conn.RDBMS;
import mesi.orm.conn.SQLiteConnection;

/**
 * PersistenceManager modules used
 * with google guice DI
 */
abstract class PersistenceManagerModule {

    static class SQLite extends AbstractModule {

        private final String connectionString;

        SQLite(String connectionString) {
            super();
            this.connectionString = connectionString;
        }

        @Override
        protected void configure() {
            bind(PersistenceManager.class).to(PersistenceManagerImpl.class);
        }

        @Provides
        DatabaseConnection provideDbConnection() {
            return DatabaseConnectionFactory.create(RDBMS.SQLITE, connectionString);
        }
    }
}
