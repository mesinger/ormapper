package mesi.orm.query;

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
    public InsertQuery insert(Class persistentClass, Object persistentObject) {
        return new SQLiteInsertQuery(persistentClass, persistentObject);
    }
}
