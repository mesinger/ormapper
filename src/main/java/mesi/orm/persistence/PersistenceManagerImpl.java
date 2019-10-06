package mesi.orm.persistence;

import com.google.inject.Inject;
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
            throw new ORMesiPersistenceException("Persistent objects need exactly one member of type Long (or long) annotated with " + Id.class.getName());
        }

        try {

            final String tableName = PersistenceManagerUtils.getPersistenceObjectsTableName(o);
            final var persistentStructure = PersistenceManagerUtils.getPersistentStructureOf(o, o.getClass());

            if(persistentStructure.getPersistentStrucutreId().isEmpty()) {
                persistentStructure.setPrimaryKey(1L);
            }

            if(!databaseConnection.tableExists(tableName)) {
                databaseConnection.createTable(tableName, PersistenceManagerUtils.getTableEntriesOfPersistentStructure(persistentStructure).toArray(TableEntry[]::new));
            }

            databaseConnection.insert(tableName, persistentStructure.getAllFields().toArray(PersistentField[]::new));

        } catch (IllegalAccessException e) {
            throw new ORMesiPersistenceException("Class " + o.getClass().getName() + " cannot be persisted.\n" + e.getMessage());
        }
    }
}
