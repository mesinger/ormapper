package mesi.orm.persistence;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * util functionalitiy for persistence
 */
public interface PersistentUtil {

    /**
     * recursively adds all persistent members from current class, and all persistent super classes
     * @param o analyzed o.getClass()
     * @return list of reflected fields of all persistent (super) classes
     */
    static List<Field> getAllPersistentMembers(Object o) {
        return getAllPersistentMembers(o.getClass());
    }

    /**
     * recursively adds all persistent members from current class, and all persistent super classes
     * @param cls analyzed class
     * @return list of reflected fields of all persistent (super) classes
     */
    static List<Field> getAllPersistentMembers(Class cls) {
        if(cls.getSuperclass() == null || cls.getAnnotation(Persistent.class) == null) return new ArrayList<Field>();
        var fields = new ArrayList<Field>(Arrays.asList(cls.getDeclaredFields()));
        fields.addAll(getAllPersistentMembers(cls.getSuperclass()));
        return fields;
    }

    /**
     * recursively adds all persistent members from current class, and all persistent super classes
     * @param cls analyzed class
     * @return list of all persistent member names
     */
    static Map<String, Class> getAllPersistentMemberNames(Class cls) {
        return getAllPersistentMembers(cls).stream().collect(Collectors.toMap(Field::getName, Field::getType));
    }

    /**
     * @param o o.getClass()
     * @return optional with the found primary field
     */
    static Optional<Field> getPrimaryField(Object o) {
        return getPrimaryField(o.getClass());
    }

    /**
     * @param cls class
     * @return optional with the found primary field
     */
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
