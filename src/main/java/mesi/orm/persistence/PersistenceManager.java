package mesi.orm.persistence;

import lombok.AccessLevel;
import lombok.Getter;
import mesi.orm.conn.DatabaseConnection;
import mesi.orm.conn.DatabaseConnectionFactory;
import mesi.orm.conn.RDBMS;
import mesi.orm.conn.TableEntry;
import mesi.orm.exception.ORMesiPersistenceException;

/***
 * Every persistance manager has to inherit from this base class.
 * Database annotation is required
 */
public abstract class PersistenceManager implements PersistenceOperations {

    private DatabaseConnectionFactory databaseConnectionFactory = new DatabaseConnectionFactory();
    private RDBMS databaseSystem;
    @Getter(AccessLevel.PROTECTED)
    private DatabaseConnection databaseConnection;

    public PersistenceManager(String connectionString) {
        validateAnnotations();

        databaseConnection = databaseConnectionFactory
                .create(
                        databaseSystem,
                        connectionString
                );

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

    void validateDatabaseAnnotation() {

        var annotation = this.getClass().getAnnotation(Database.class);

        if(annotation == null) {
            throw new ORMesiPersistenceException("Missing annotation " + Database.class.getName() + " for " + this.getClass().getName());
        }

        databaseSystem = annotation.system();
    }

    @Override
    public void persist(Object o) {

        if(!PersistenceOperations.isObjectPersistent(o)) {
            throw new ORMesiPersistenceException("Object of type " + o.getClass().getName() + " misses the " + Persistent.class.getName() + " annotation");
        }

        if(!PersistenceOperations.hasPersistentObjectIdentification(o)) {
            throw new ORMesiPersistenceException("Persistent objects need exactly one member annotated with " + Id.class.getName());
        }

        final String tableName = getPersistenceObjectsTableName(o);

        if(!databaseConnection.tableExists(tableName)) {

            final var tableEntries = getPersistenceObjectsTableEntries(o);
            databaseConnection.createTable(tableName, tableEntries);
        }

        throw new RuntimeException("not finished yet");
    }

    private TableEntry[] getPersistenceObjectsTableEntries(Object o) {
        throw new RuntimeException("not implemented");
    }

    private String getPersistenceObjectsTableName(Object o) {
        return o.getClass().getName() + "_table";
    }
}
