package mesi.orm.query;

import mesi.orm.persistence.*;

import java.lang.reflect.Field;

/**
 * used for building create table statements
 */
public abstract class CreateQuery extends Query{

    protected CreateQuery(String tableName) {

    }

    protected abstract CreateQuery addColumn(String name, QUERYTYPE dataType, boolean isPrimary, boolean isNullable, boolean isForeign, String foreignTable, String foreignRef);

    /**
     * adds a new column to the create table statement
     * @param reflectedField member of persistent class
     * @param cls class of field
     * @return this
     */
    public CreateQuery addColumn(Field reflectedField, Class cls) {

        reflectedField.setAccessible(true);

        final var name = reflectedField.getName();
        final var isPrimary = reflectedField.getAnnotation(Id.class) != null;
        var isEnum = reflectedField.getAnnotation(Transient.class) != null;
        final var dataType = (isPrimary ? QUERYTYPE.PRIMARY : (isEnum ? QUERYTYPE.TEXT : QueryUtil.getTypeOf(cls)));
        final var isNullable = reflectedField.getAnnotation(Nullable.class) != null;
        final var isForeign = reflectedField.getAnnotation(Foreign.class) != null;

        String foreignTable = null;
        String foreignRef = null;

        if(isForeign) {
            foreignTable = PersistentUtil.getPersistenceObjectsTableName(reflectedField.getDeclaringClass());
            foreignRef = "id";
        }

        return addColumn(name, dataType, isPrimary, isNullable, isForeign, foreignTable, foreignRef);
    }
}
