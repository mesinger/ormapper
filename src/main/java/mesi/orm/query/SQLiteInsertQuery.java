package mesi.orm.query;

import mesi.orm.exception.ORMesiPersistenceException;
import mesi.orm.persistence.Foreign;
import mesi.orm.persistence.Id;
import mesi.orm.persistence.Nullable;
import mesi.orm.persistence.PersistentUtil;

import java.util.stream.Collectors;

class SQLiteInsertQuery extends InsertQuery {

    SQLiteInsertQuery(Class persistentClass, Object persistentObject) {
        super(persistentClass, persistentObject);

        head.append("INSERT INTO " + PersistentUtil.getPersistenceObjectsTableName(persistentClass) + " (");

        final var columnsAndValues = PersistentUtil.getAllPersistentMembers(persistentClass).stream()
                .filter(field -> field.getAnnotation(Id.class) == null)
                .filter(field -> {
                    field.setAccessible(true);
                    try {
                        if(field.getAnnotation(Nullable.class) != null && field.get(persistentObject) == null) return false;
                        else if(field.getAnnotation(Nullable.class) == null && field.get(persistentObject) == null) throw new ORMesiPersistenceException("field " + field.getName() + " in class " + persistentClass.getName() + " is null, but not annotated with " + Nullable.class.getName());
                        else return true;
                    }
                    catch (IllegalAccessException ex) {
                        throw new ORMesiPersistenceException("leck mi am arsch");
                    }
                })
                .map(field -> {
                    try {

                        field.setAccessible(true);
                        boolean isString = field.get(persistentObject).getClass().equals(String.class);

                        String name = field.getName();
                        Object value = isString ? "'" + field.get(persistentObject) + "'" : field.get(persistentObject);

                        if(field.getAnnotation(Foreign.class) != null) {
                            name = "fk_" + field.getName();
                        }

                        return new Irgendwas(name, value);

                    }
                    catch (Exception ex) {
                        throw new ORMesiPersistenceException("cannot insert element of type " + persistentClass.getName() + " because of " + ex.getMessage());
                    }
                })
                .collect(Collectors.toList());

        columnsAndValues.stream().forEach(column -> head.append(column.name + ", "));
        head.setLength(head.length() - 2);
        head.append(") ");

        tail.append("VALUES (");
        columnsAndValues.stream().forEach(value -> tail.append(value.value + ", "));
        tail.setLength(tail.length() - 2);
        tail.append(");");
    }

    private class Irgendwas {
        public final String name;
        public final Object value;

        public Irgendwas(String name, Object value) {
            this.name = name;
            this.value = value;
        }
    }
}
