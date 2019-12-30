package mesi.orm.query;

import mesi.orm.persistence.transform.PersistentObject;

class SQLiteQueryBuilder implements QueryBuilder {

    SQLiteQueryBuilder() {}

    @Override
    public CreateQuery create(String tableName) {
        return new SQLiteCreateQuery(tableName);
    }

    @Override
    public SelectQuery select(String... columns) {
        return new SQLiteSelectQuery(columns);
    }

    @Override
    public InsertQuery insert(PersistentObject persistentObject) {
        return new SQLiteInsertQuery(persistentObject);
    }

    @Override
    public UpdateQuery update(PersistentObject persistentObject) {
        return new SQLiteUpdateQuery(persistentObject);
    }
}
