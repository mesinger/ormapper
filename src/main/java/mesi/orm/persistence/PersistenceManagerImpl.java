package mesi.orm.persistence;

import com.google.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mesi.orm.conn.DatabaseConnection;
import mesi.orm.conn.TableEntry;
import mesi.orm.exception.ORMesiPersistenceException;

/***
 * Persistence manager implementation
 * NoArgsConstructor only used for unit testing
 */
@NoArgsConstructor
final class PersistenceManagerImpl implements PersistenceManager {
    
    private DatabaseConnection databaseConnection;

    @Inject
    PersistenceManagerImpl(DatabaseConnection connection) {

        databaseConnection = connection;
        databaseConnection.open();
    }

    @Override
    public void persist(Object o) {

        if(!PersistenceManager.isObjectPersistent(o)) {
            throw new ORMesiPersistenceException("Object of type " + o.getClass().getName() + " misses the " + Persistent.class.getName() + " annotation");
        }

        if(!PersistenceManager.hasPersistentObjectIdentification(o)) {
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
