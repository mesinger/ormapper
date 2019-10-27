package mesi.orm.query;

import mesi.orm.persistence.PersistentUtil;

import java.util.Arrays;
import java.util.List;

class SQLiteSelectQuery extends SelectQuery {

    SQLiteSelectQuery(String... columns) {
        super(columns);
        head.append("SELECT ");
        Arrays.stream(columns).forEach(column -> head.append(column + ", "));
        head.setLength(head.length() - 2);
        head.append(" ");
    }

    @Override
    public SelectQuery from(Class persistentClass) {
        head.append("FROM " + PersistentUtil.getPersistenceObjectsTableName(persistentClass) + " ");
        return this;
    }

    @Override
    public SelectQuery where(String condition) {
        wherePart.append(condition);
        return this;
    }

    @Override
    public SelectQuery andWhere(String condition) {
        wherePart.append(" AND " + condition);
        return this;
    }

    @Override
    public SelectQuery orWhere(String condition) {
        wherePart.append(" OR " + condition);
        return this;
    }

    @Override
    public SelectQuery orderBy(List<String> columns) {
        columns.stream()
                .forEach(column -> orderPart.append(column + ", "));
        return this;
    }
}
