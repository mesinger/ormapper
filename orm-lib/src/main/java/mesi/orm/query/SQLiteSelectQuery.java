package mesi.orm.query;

import mesi.orm.util.Persistence;

import java.util.Arrays;

class SQLiteSelectQuery extends SelectQuery {

    SQLiteSelectQuery(String... columns) {
        super(columns);
        head.append("SELECT ");

        if(columns.length == 0) {
            head.append("*");
        }
        else {
            Arrays.stream(columns).forEach(column -> head.append(column + ", "));
            head.setLength(head.length() - 2);
        }

        head.append(" ");
    }

    @Override
    public SelectQuery from(Class persistentClass) {
        head.append("FROM " + Persistence.INSTANCE.getTableName(persistentClass) + " ");
        return this;
    }

    @Override
    public SelectQuery where(String condition) {
        wherePart.append(condition);
        return this;
    }

    @Override
    public SelectQuery and() {
        wherePart.append(" AND ");
        return this;
    }

    @Override
    public SelectQuery or() {
        wherePart.append(" OR ");
        return this;
    }
}
