package mesi.orm.query;

import mesi.orm.exception.ORMesiException;
import mesi.orm.persistence.transform.PersistentPropertyType;

class SQLiteCreateQuery extends CreateQuery {

    protected SQLiteCreateQuery(String tableName) {
        super(tableName);

        head.append("CREATE TABLE " + tableName + "(\n");
    }

    @Override
    protected CreateQuery addColumn(String name, PersistentPropertyType dataType, boolean isPrimary, boolean isForeign, String foreignTable, String foreignRef) {

        head.append(name + " ");
        head.append(translateQueryType(dataType) + " ");
        head.append((isPrimary ? "PRIMARY KEY " : ""));
        head.append("NOT NULL ");
        head.setLength(head.length() - 1);
        head.append(",\n");

        if(isForeign) {
            tail.append("FOREIGN KEY (" + name + ") REFERENCES " + foreignTable + " (" + foreignRef + "),\n");
        }

        return this;
    }

    private String translateQueryType(PersistentPropertyType dataType) {
        switch (dataType) {
            case BOOL:
            case LONG:
                return "INTEGER";
            case DOUBLE:
                return "DOUBLE";
            case STRING:
            case TIME:
            case DATE:
            case DATETIME:
                return "TEXT";
            default:
                throw new ORMesiException("unsupported type");
        }
    }
}
