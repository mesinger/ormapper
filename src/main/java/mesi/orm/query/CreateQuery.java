package mesi.orm.query;

abstract class CreateQuery extends Query{
    protected CreateQuery(String tableName) {

    }
    abstract CreateQuery addColumn(String name, QUERYTYPE dataType, boolean isPrimary, boolean isNullable, boolean isForeign, String foreignTable, String foreignRef);
}
