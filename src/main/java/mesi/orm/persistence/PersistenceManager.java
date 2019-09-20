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
public abstract class PersistenceManager implements PersistenceOperations {

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

    @Override
    public void persist(Object o) {

        if(!PersistenceOperations.isObjectPersistent(o)) {
            throw new ORMesiPersistenceException("Object of type " + o.getClass().getName() + " misses the " + Persistent.class.getName() + " annotation");
        }

        if(!PersistenceOperations.hasPersistentObjectIdentification(o)) {
            throw new ORMesiPersistenceException("Persistent objects need exactly one member annotated with " + Id.class.getName());
        }

        throw new RuntimeException("not finished");
    }
}
