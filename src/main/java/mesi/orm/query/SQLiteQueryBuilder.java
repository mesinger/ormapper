package mesi.orm.query;

class SQLiteQueryBuilder implements QueryBuilder {

    SQLiteQueryBuilder() {}

    @Override
    public CreateQuery create(String tableName) {
        return new SQLiteCreateQuery(tableName);
    }
}
