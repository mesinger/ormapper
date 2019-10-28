package mesi.orm.query;

import mesi.orm.exception.ORMesiPersistenceException;
import mesi.orm.persistence.PersistenceManager;

import java.util.Optional;

/**
 * utility functions for query building
 */
public interface QueryUtil {

    /**
     * translates an objects type
     * to a supported database data type
     *
     * @param type class of object
     * @return corresponding database entry type, or throws on invalid objects
     */
    static QUERYTYPE getTypeOf(Class type) {

        if (Number.class.isAssignableFrom(type)) {

            if (type.equals(double.class) || type.equals(Double.class) || type.equals(float.class) || type.equals(Float.class)) {
                return QUERYTYPE.FLOAT;
            } else {
                return QUERYTYPE.INTEGER;
            }
        } else if (type.equals(String.class)) {
            return QUERYTYPE.TEXT;
        } else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
            return QUERYTYPE.BOOL;
        }
        else {
            if(PersistenceManager.isObjectPersistent(type)) {
                return QUERYTYPE.INTEGER;
            } else {
                throw new ORMesiPersistenceException("Cannot persist members of type " + type.getName());
            }
        }
    }
}
