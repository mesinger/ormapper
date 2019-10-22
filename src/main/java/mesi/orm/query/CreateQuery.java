package mesi.orm.query;

import mesi.orm.persistence.Foreign;
import mesi.orm.persistence.Id;
import mesi.orm.persistence.Nullable;

import java.lang.reflect.Field;

abstract class CreateQuery extends Query{

    protected CreateQuery(String tableName) {

    }

    protected abstract CreateQuery addColumn(String name, QUERYTYPE dataType, boolean isPrimary, boolean isNullable, boolean isForeign, String foreignTable, String foreignRef);

    public CreateQuery addColumn(Field reflectedField, Class cls) {

        reflectedField.setAccessible(true);

        final var name = reflectedField.getName();
        final var isPrimary = reflectedField.getAnnotation(Id.class) != null;
        final var dataType = (isPrimary ? QUERYTYPE.PRIMARY : QueryUtil.getTypeOf(cls));
        final var isNullable = reflectedField.getAnnotation(Nullable.class) != null;
        final var isForeign = reflectedField.getAnnotation(Foreign.class) != null;

        String foreignTable = null;
        String foreignRef = null;

        if(isForeign) {
            foreignTable = reflectedField.getAnnotation(Foreign.class).table();
            foreignRef = reflectedField.getAnnotation(Foreign.class).ref();
        }

        return addColumn(name, dataType, isPrimary, isNullable, isForeign, foreignTable, foreignRef);
    }
}
