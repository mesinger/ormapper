package mesi.orm.query;

public interface QueryBuilder {
    CreateQuery create(String tableName);
    SelectQuery select(String... columns);
}
