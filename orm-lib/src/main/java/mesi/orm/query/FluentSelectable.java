package mesi.orm.query;

import java.util.List;

/**
 * fluentapi for creating select queries
 */
public interface FluentSelectable {

    /**
     * select part of query
     * @param columns columns which should be selected (e.g '*', 'name', ...)
     * @return this
     */
    FluentSelectable select(String... columns);

    /**
     * from part of query
     * @param persistentClass persistent class
     * @return this
     */
    FluentSelectable from(Class persistentClass);

    /**
     * where part of query
     * @param condition (e.g. 'id=1')
     * @return this
     */
    FluentSelectable where(String condition);

    /**
     * and where part of query
     * @param condition (e.g. 'id=1')
     * @return this
     */
    FluentSelectable andWhere(String condition);

    /**
     * or where part of query
     * @param condition (e.g. 'id=1')
     * @return this
     */
    FluentSelectable orWhere(String condition);

    /**
     * order by part of query
     * @param columns (e.g. 'lastname', 'firstname')
     * @return this
     */
    FluentSelectable orderBy(String... columns);

    /**
     * select part of query
     * @param columns columns which should be selected (e.g '*', 'name', ...)
     * @return this
     */
    default FluentSelectable select(List<String> columns) {
        return select(columns.toArray(String[]::new));
    }

    /**
     * order by part of query
     * @param columns (e.g. 'lastname', 'firstname')
     * @return this
     */
    default FluentSelectable orderBy(List<String> columns) {
        return orderBy(columns.toArray(String[]::new));
    }
}
