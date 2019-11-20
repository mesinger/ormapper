package mesi.orm.query;

/**
 * entry point for building mesi.orm.query.Query objects
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
     * @param persistentClass
     * @param persistentObject
     * @return insert query
     */
    InsertQuery insert(Class persistentClass, Object persistentObject);
}
