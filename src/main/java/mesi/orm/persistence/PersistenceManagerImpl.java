package mesi.orm.persistence;

import com.google.inject.Inject;
import lombok.NoArgsConstructor;
import mesi.orm.conn.DatabaseConnection;
import mesi.orm.exception.ORMesiPersistenceException;
import mesi.orm.query.QueryBuilder;

import java.util.stream.Collectors;

/***
 * Persistence manager implementation
 * NoArgsConstructor only used for unit testing
 */
@NoArgsConstructor
final class PersistenceManagerImpl implements PersistenceManager {

    private DatabaseConnection databaseConnection;
    private QueryBuilder queryBuilder;

    @Inject
    PersistenceManagerImpl(DatabaseConnection connection, QueryBuilder queryBuilder) {

        databaseConnection = connection;
        databaseConnection.open();

        this.queryBuilder = queryBuilder;
    }

    @Override
    public void persist(Object o) {

        checkPersistenceValidityOfObject(o);

        final String tableName = PersistenceManagerUtils.getPersistenceObjectsTableName(o);
        final var persistentStructure = PersistenceManagerUtils.getPersistentStructureOf(o, o.getClass());

        createTableIfNeeded(tableName, o);

        persistentStructure.getAllForeignFields().stream()
                .forEach(foreign -> foreign.getValue().ifPresent(foreignObject -> persist(foreignObject)));

        databaseConnection.insert(tableName, persistentStructure.getAllFields().toArray(PersistentField[]::new));
    }

    /**
     * checks if the given object is a valid perstent object, otherwise throws runtimeexception
     * @param o
     */
    private void checkPersistenceValidityOfObject(Object o) {
        if(!PersistenceManager.isObjectPersistent(o)) {
            throw new ORMesiPersistenceException("Object of type " + o.getClass().getName() + " misses the " + Persistent.class.getName() + " annotation");
        }

        if(!PersistenceManager.hasPersistentObjectIdentification(o)) {
            throw new ORMesiPersistenceException("Persistent objects need exactly one member of type Long (or long) annotated with " + Id.class.getName());
        }
    }

    private void createTableIfNeeded(String tableName, Object o) {
        if(!databaseConnection.tableExists(tableName)) {

            var query = queryBuilder.create(tableName);

            var primaryField = PersistentUtil.getPrimaryField(o).orElseThrow(() -> new ORMesiPersistenceException("Object of type " + o.getClass().getName() + " misses primary key member"));
            var fields = PersistentUtil.getAllPersistentMembers(o).stream().filter(field -> field.getAnnotation(Id.class) == null).collect(Collectors.toList());
            fields.add(primaryField);

            fields.stream().forEach(
                    field -> query.addColumn(field, o.getClass())
            );

            databaseConnection.createTable(query);
        }
    }
}
