package mesi.orm.persistence;

import lombok.AccessLevel;
import lombok.Getter;
import mesi.orm.conn.DatabaseConnection;
import mesi.orm.conn.DatabaseConnectionFactory;
import mesi.orm.exception.ORMesiPersistenceException;

/***
 * Every persistance manager has to inherit from this base class.
 * Database annotation is required
 */
public abstract class PersistenceManager {

    private DatabaseConnectionFactory databaseConnectionFactory = new DatabaseConnectionFactory();
    @Getter(AccessLevel.PROTECTED)
    private DatabaseConnection databaseConnection;

    protected PersistenceManager() {
        validateAnnotations();
        databaseConnection.open();
    }

    /***
     * this constructor is used for unit testing only
     * @param databaseConnectionFactory mocked factroy
     */
    PersistenceManager(DatabaseConnectionFactory databaseConnectionFactory) {
        this.databaseConnectionFactory = databaseConnectionFactory;
    }

    private void validateAnnotations() {
        validateDatabaseAnnotation();
    }

    protected void validateDatabaseAnnotation() {

        var annotation = this.getClass().getAnnotation(Database.class);

        if(annotation == null) {
            throw new ORMesiPersistenceException("Missing annotation " + Database.class.getName() + " for " + this.getClass().getName());
        }

        databaseConnection = databaseConnectionFactory
                .create(
                        annotation.system(),
                        annotation.connectionString()
                );
    }
}
