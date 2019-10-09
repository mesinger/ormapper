package mesi.orm.persistence;

import mesi.orm.conn.TableEntry;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

interface PersistenceManagerUtils {

    /**
     * parses a given PersistentStructure to a list
     * of TableEntrys used for CREATE TABLE
     * @param ps given PersistentStructure
     * @return list of TableEntry
     */
    static List<TableEntry> getTableEntriesOfPersistentStructure(PersistentStructure ps) {

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

        return entries;
    }

    /**
     * parses all persistent members of an given persistent object
     * recursive on all persistent super classes
     * @param o persistent object
     * @param cls class of o
     * @return PersistentStructure of o
     * @throws IllegalAccessException (member not accessible)
     */
    static PersistentStructure getPersistentStructureOf(Object o, Class cls) throws IllegalAccessException {

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

            if(isPrimary) {
                continue;
            }

            entries.add(new PersistentField(field.getName(), Optional.ofNullable(value), isNullable, isPrimary, isForeign));
        }

        return new PersistentStructure(tableName, entries, parentStructure);
    }

    /**
     * @param o
     * @return {o.classname}_table
     */
    static String getPersistenceObjectsTableName(Object o) {
        return getPersistenceObjectsTableName(o.getClass());
    }

    /**
     * @param cls
     * @return {classname}_table
     */
    static String getPersistenceObjectsTableName(Class cls) {
        return cls.getName().replaceAll("\\.", "_") + "_table";
    }
}
