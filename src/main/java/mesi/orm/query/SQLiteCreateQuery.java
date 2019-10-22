package mesi.orm.query;

class SQLiteCreateQuery extends CreateQuery {

    protected SQLiteCreateQuery(String tableName) {
        super(tableName);

        head.append("CREATE TABLE " + tableName + "(\n");
    }

    @Override
    protected CreateQuery addColumn(String name, QUERYTYPE dataType, boolean isPrimary, boolean isNullable, boolean isForeign, String foreignTable, String foreignRef) {

        head.append((isForeign) ? "fk_" : "");
        head.append(name + " ");
        head.append(translateQueryType(dataType) + " ");
        head.append((isPrimary ? "PRIMARY KEY AUTOINCREMENT " : ""));
        head.append(((!isNullable && !isPrimary) ? "NOT NULL " : ""));
        head.setLength(head.length() - 1);
        head.append(",\n");

        if(isForeign) {
            tail.append("FOREIGN KEY (fk_" + name + ") REFERENCES " + foreignTable + " (" + foreignRef + "),\n");
        }

        return this;
    }

    private String translateQueryType(QUERYTYPE dataType) {
        switch (dataType) {
            case PRIMARY:
            case INTEGER:
            case BOOL:
                return "INTEGER";
            case FLOAT:
                return "DOUBLE";
            case TEXT:
                return "TEXT";
            default:
                throw new RuntimeException("invalid querytype");
        }
    }
}
