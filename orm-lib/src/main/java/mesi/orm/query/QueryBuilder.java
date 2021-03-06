package mesi.orm.query;

import mesi.orm.persistence.transform.PersistentObject;

/**
 * interface for building mesi.orm.query.Query objects
 */
public interface QueryBuilder {

    /**
     * @param tableName
     * @return create table query
     */
    CreateQuery create(String tableName);

    /**
     * @param columns
     * @return select query
     */
    SelectQuery select(String... columns);

    /**
     * @param persistentObject
     * @return insert query
     */
    InsertQuery insert(PersistentObject persistentObject);

    /**
     * @param persistentObject
     * @return
     */
    UpdateQuery update(PersistentObject persistentObject);
}
