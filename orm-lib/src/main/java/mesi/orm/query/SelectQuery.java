package mesi.orm.query;

import lombok.Setter;

import java.util.List;
import java.util.Optional;

/**
 * used for building insert statements
 */
public abstract class SelectQuery extends Query {

    protected StringBuilder wherePart = new StringBuilder();
    protected StringBuilder orderPart = new StringBuilder();

    @Setter
    private Class targetClass;

    protected SelectQuery(String... columns) {}

    public abstract SelectQuery from(Class persistentClass);

    public abstract SelectQuery where(String condition);
    public abstract SelectQuery andWhere(String condition);
    public abstract SelectQuery orWhere(String condition);

    public SelectQuery orderBy(String... columns) {
        return orderBy(List.of(columns));
    }

    public abstract SelectQuery orderBy(List<String> columns);

    @Override
    public String raw() {

         StringBuilder raw = head;

         if(wherePart.length() != 0) {
             raw.append("WHERE " + wherePart + " ");
         }

         if(orderPart.length() != 0) {
             raw.append("ORDER BY " + orderPart);
             raw.setLength(raw.length() - 2);
         }

         raw.append(";");

         return raw.toString();
    }

    /**
     * @return class which was passed on a previous from call, or null
     */
    public Optional<Class> getTargetClass() {
        if(targetClass == null) return Optional.empty();
        else return Optional.of(targetClass);
    }
}