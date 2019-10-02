package mesi.orm.persistence;

import com.google.inject.Inject;
import lombok.NoArgsConstructor;
import mesi.orm.conn.DatabaseConnection;
import mesi.orm.conn.TableEntry;
import mesi.orm.exception.ORMesiPersistenceException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

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

            final String tableName = getPersistenceObjectsTableName(o);
            final var persistentStructure = getPersistentStructureOf(o, o.getClass());

            if(!databaseConnection.tableExists(tableName)) {
                databaseConnection.createTable(tableName, getTableEntriesOfPersistentStructure(persistentStructure));
            }

            databaseConnection.insert(tableName, persistentStructure.getAllFields().toArray(PersistentField[]::new));

        } catch (IllegalAccessException e) {
            throw new ORMesiPersistenceException("Class " + o.getClass().getName() + " cannot be persisted.\n" + e.getMessage());
        }
    }

    private TableEntry[] getTableEntriesOfPersistentStructure(PersistentStructure ps) {

        var entries = new ArrayList<TableEntry>();

        for(PersistentField field : ps.getAllFields()) {

            entries.add(
                    new TableEntry(
                            field.getName(),
                            TableEntry.getTypeOf(field.getValue()),
                            field.isNullable(),
                            field.isPrimary(),
                            field.isForeign(),
                            field.getForeignTableName(),
                            field.getForeignRef()
                    )
            );
        }

        return entries.toArray(TableEntry[]::new);
    }

    private PersistentStructure getPersistentStructureOf(Object o, Class cls) throws IllegalAccessException {

        Optional<PersistentStructure> parentStructure = Optional.empty();

        final var superClass = cls.getSuperclass();

        if(PersistenceManager.isObjectPersistent(superClass)) {
            parentStructure = Optional.of(getPersistentStructureOf(o, superClass));
        }

        final var tableName = getPersistenceObjectsTableName(cls);

        var entries = new ArrayList<PersistentField>();

        for(Field field : cls.getDeclaredFields()) {
            field.setAccessible(true);

            var value = field.get(o);
            boolean isNullable = field.getAnnotation(Nullable.class) != null;
            boolean isPrimary = field.getAnnotation(Id.class) != null;
            boolean isForeign = field.getAnnotation(Foreign.class) != null;

            entries.add(new PersistentField(field.getName(), value, isNullable, isPrimary, isForeign));
        }

        return new PersistentStructure(tableName, entries, parentStructure);
    }

    /**
     * @param o
     * @return {o.classname}_table
     */
    private String getPersistenceObjectsTableName(Object o) {
        return getPersistenceObjectsTableName(o.getClass());
    }

    /**
     * @param cls
     * @return {classname}_table
     */
    private String getPersistenceObjectsTableName(Class cls) {
        return cls.getName() + "_table";
    }
}
