package mesi.orm.query;

import java.util.List;

public interface FluentSelectable {

    FluentSelectable select(String... columns);
    FluentSelectable from(Class persistentClass);
    FluentSelectable where(String condition);
    FluentSelectable andWhere(String condition);
    FluentSelectable orWhere(String condition);
    FluentSelectable orderBy(String... columns);

    default FluentSelectable select(List<String> columns) {
        return select(columns.toArray(String[]::new));
    }

    default FluentSelectable orderBy(List<String> columns) {
        return orderBy(columns.toArray(String[]::new));
    }
}
