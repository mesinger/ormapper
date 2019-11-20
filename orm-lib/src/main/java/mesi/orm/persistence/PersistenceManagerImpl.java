package mesi.orm.persistence;

import com.google.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mesi.orm.conn.DatabaseConnection;
import mesi.orm.exception.ORMesiFetchException;
import mesi.orm.exception.ORMesiPersistenceException;
import mesi.orm.exception.ORMesiQueryException;
import mesi.orm.query.QueryBuilder;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/***
 * Persistence manager implementation
 * NoArgsConstructor only used for unit testing
 */
@NoArgsConstructor
final class PersistenceManagerImpl implements PersistenceManager, PersistenceFetcher {

    private DatabaseConnection databaseConnection;
    @Getter(AccessLevel.PACKAGE)
    private QueryBuilder queryBuilder;
    @Setter(AccessLevel.PACKAGE)
    private PersistenceFetcherState fetchState = new PersistenceFetcherInitialState(this);
    private ResultSetParser resultSetParser;

    @Inject
    PersistenceManagerImpl(DatabaseConnection connection, QueryBuilder queryBuilder, ResultSetParser resultSetParser) {

        databaseConnection = connection;
        databaseConnection.open();

        this.queryBuilder = queryBuilder;
        this.resultSetParser = resultSetParser;
    }

    @Override
    public void persist(Object o) {

        checkPersistenceValidityOfObject(o);

        createTableIfNeeded(o);

        PersistentUtil.getAllPersistentMembers(o).stream()
                .filter(field -> field.getAnnotation(Foreign.class) != null)
                .forEach(foreignField -> persistForeign(foreignField, o));

        long generatedId = databaseConnection.insert(queryBuilder.insert(o.getClass(), o));

        try {
            final var idField = o.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(o, generatedId);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PersistenceFetcher from(Class persistentClass) {
        fetchState = new PersistenceFetcherInitialState(this);
        fetchState.from(persistentClass);
        return this;
    }

    @Override
    public List<Object> fetch() {

        final var query = fetchState.query;
        final var targetClass = query.getTargetClass().orElseThrow(() -> new ORMesiFetchException("Missing target class"));
        var persistentObjects = new ArrayList<Object>();

        if(query == null) {
            throw new ORMesiQueryException("Invalid query statement");
        }

        try (var drs = databaseConnection.select(query)) {

            final var rs = drs.getResultSet();

            final var memberNamesAndClasses = PersistentUtil.getAllPersistentMemberNamesAndClass(targetClass);

            while(rs.next()) {

                var persistentObject = targetClass.getConstructor().newInstance();

                for(Map.Entry<String, Class> entry : memberNamesAndClasses.entrySet()) {
                    var member = targetClass.getDeclaredField(entry.getKey());
                    member.setAccessible(true);

                    if(member.getAnnotation(Enum.class) == null) {
                        member.set(persistentObject, resultSetParser.parseObjectFromResultSet(entry.getKey(), rs, entry.getValue()));
                    }
                }

                persistentObjects.add(persistentObject);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ORMesiFetchException("Error while reading from query result");
        }

        return persistentObjects;
    }

    /**
     * checks if the given object is a valid perstent object, otherwise throws runtimeexception
     * @param o
     */
    private void checkPersistenceValidityOfObject(Object o) {
        if(!PersistentUtil.isObjectPersistent(o)) {
            throw new ORMesiPersistenceException("Object of type " + o.getClass().getName() + " misses the " + Persistent.class.getName() + " annotation");
        }

        if(!PersistentUtil.hasPersistentObjectIdentification(o)) {
            throw new ORMesiPersistenceException("Persistent objects need exactly one member of type Long (or long) annotated with " + Id.class.getName());
        }
    }

    private void createTableIfNeeded(Object o) {

        final var tableName = PersistentUtil.getPersistenceObjectsTableName(o);

        if(!databaseConnection.tableExists(tableName)) {

            var query = queryBuilder.create(tableName);

            var primaryField = PersistentUtil.getPrimaryField(o).orElseThrow(() -> new ORMesiPersistenceException("Object of type " + o.getClass().getName() + " misses primary key member"));
            var fields = PersistentUtil.getAllPersistentMembers(o).stream().filter(field -> field.getAnnotation(Id.class) == null).collect(Collectors.toList());
            fields.add(primaryField);

            fields.stream().forEach(
                    field -> query.addColumn(field, field.getType())
            );

            databaseConnection.createTable(query);
        }
    }

    private void persistForeign(Field foreignField, Object o) {

        try {

            foreignField.setAccessible(true);

            final var foreignAnnotation = foreignField.getAnnotation(Foreign.class);
            final var foreignRelation = foreignAnnotation.relationType();
            final var foreignObject = foreignField.get(o);

            if(foreignObject == null && foreignField.getAnnotation(Nullable.class) == null) {
                throw new ORMesiPersistenceException("member " + foreignField.getName() + " is null, but not annotated with " + Nullable.class.getName());
            }

            switch (foreignRelation) {
                case ONETOONE:
                case ONETOMANY:

                    if(foreignObject != null) {
                        persist(foreignObject);
                    }

                    break;
                case MANYTOONE:

                    if(!(foreignObject instanceof Set) || !(foreignObject instanceof List)) {
                        throw new ORMesiPersistenceException("many to one or many to many relations have to be implemented as List or Set");
                    }



                case MANYTOMANY:
                    // TODO
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PersistenceFetcher where(String condition) {
        fetchState.where(condition);
        return this;
    }

    @Override
    public PersistenceFetcher andWhere(String condition) {
        fetchState.andWhere(condition);
        return this;
    }

    @Override
    public PersistenceFetcher orWhere(String condition) {
        fetchState.orWhere(condition);
        return this;
    }
}
