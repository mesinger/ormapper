package mesi.orm.persistence;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface PersistentUtil {

    static List<Field> getAllPersistentMembers(Object o) {
        return getAllPersistentMembers(o.getClass());
    }

    static List<Field> getAllPersistentMembers(Class cls) {
        if(cls.getSuperclass() == null || cls.getAnnotation(Persistent.class) == null) return new ArrayList<Field>();
        var fields = new ArrayList<Field>(Arrays.asList(cls.getDeclaredFields()));
        fields.addAll(getAllPersistentMembers(cls.getSuperclass()));
        return fields;
    }

    static Optional<Field> getPrimaryField(Object o) {
        return getPrimaryField(o.getClass());
    }

    static Optional<Field> getPrimaryField(Class cls) {
        return Arrays.stream(cls.getDeclaredFields())
                .filter(field -> field.getAnnotation(Id.class) != null)
                .findFirst();
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
