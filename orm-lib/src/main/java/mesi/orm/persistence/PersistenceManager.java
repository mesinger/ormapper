package mesi.orm.persistence;

import mesi.orm.query.FluentSelectable;

import java.lang.reflect.Field;
import java.util.List;

/***
 * interface for PersistenceManagers
 * hold methods which are used for storing and accessing
 * between objects and databases
 */
public interface PersistenceManager {
    /***
     * persists an Persistent entity in the underlying rdbms
     * @param o object to be mapped (has to be annotated with @Persistent)
     */
    void persist(Object o);

    /**
     * first call for fetching data
     * @param persistentClass
     * @return this as PersistenceFetcher
     */
    PersistenceFetcher from(Class persistentClass);
}
