package mesi.orm.persistence;

import java.util.List;

/**
 * allows fetching of persistent data
 * and returning pojos
 */
public interface PersistenceFetcher {

    /**
     * executes the already built select query
     * parses db resultset to a list of objects
     * @return list of queried objects from the database
     */
    List<Object> fetch();

    /**
     * add where conditions to the current fetching
     * @param condition
     * @return this
     */
    PersistenceFetcher where(String condition);
    PersistenceFetcher andWhere(String condition);
    PersistenceFetcher orWhere(String condition);
}
