package mesi.orm.query;

import mesi.orm.exception.ORMesiPersistenceException;
import mesi.orm.persistence.PersistenceManager;
import mesi.orm.persistence.PersistentUtil;

import java.util.Date;
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

        if(type.equals(int.class) || type.equals(Integer.class) || type.equals(long.class) || type.equals(Long.class)) {
            return QUERYTYPE.INTEGER;
        }
        else if(type.equals(float.class) || type.equals(Float.class) || type.equals(double.class) || type.equals(Double.class)) {
            return QUERYTYPE.FLOAT;
        }
        else if (type.equals(String.class)) {
            return QUERYTYPE.TEXT;
        }
        else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
            return QUERYTYPE.BOOL;
        }
        else if(type.equals(Date.class)) {
            return QUERYTYPE.DATE;
        }
        else if(PersistentUtil.isObjectPersistent(type)) {
            return QUERYTYPE.PRIMARY;
        } else {
            throw new ORMesiPersistenceException("Cannot persist members of type " + type.getName());
        }
    }
}
