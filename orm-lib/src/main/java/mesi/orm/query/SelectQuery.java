package mesi.orm.query;

import lombok.Setter;

import java.util.Optional;

/**
 * used for building insert statements
 */
public abstract class SelectQuery extends Query {

    protected StringBuilder wherePart = new StringBuilder();

    @Setter
    private Class targetClass;

    protected SelectQuery(String... columns) {}

    public abstract SelectQuery from(Class persistentClass);

    public abstract SelectQuery where(String condition);
    public abstract SelectQuery and();
    public abstract SelectQuery or();

    @Override
    public String raw() {

         StringBuilder raw = head;

         if(wherePart.length() != 0) {
             raw.append("WHERE " + wherePart + " ");
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