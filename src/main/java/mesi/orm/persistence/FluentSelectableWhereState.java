package mesi.orm.persistence;

import mesi.orm.exception.ORMesiQueryException;
import mesi.orm.query.FluentSelectable;
import mesi.orm.query.SelectQuery;

final class FluentSelectableWhereState extends FluentSelectableState {

    FluentSelectableWhereState(PersistenceManagerImpl pm, SelectQuery query) {
        super(pm, query);
    }

    @Override
    public FluentSelectable select(String... columns) {
        throw new ORMesiQueryException("Invalid call: you can only call andWhere, orWhere or orderBy after where");
    }

    @Override
    public FluentSelectable from(Class persistentClass) {
        throw new ORMesiQueryException("Invalid call: you can only call andWhere, orWhere or orderBy after where");
    }

    @Override
    public FluentSelectable where(String condition) {
        throw new ORMesiQueryException("Invalid call: you can only call andWhere, orWhere or orderBy after where");
    }

    @Override
    public FluentSelectable andWhere(String condition) {
        query = query.andWhere(condition);
        pm.setSelectableState(new FluentSelectableWhereState(pm, query));
        return pm;
    }

    @Override
    public FluentSelectable orWhere(String condition) {
        query = query.orWhere(condition);
        pm.setSelectableState(new FluentSelectableWhereState(pm, query));
        return pm;
    }

    @Override
    public FluentSelectable orderBy(String... columns) {
        query = query.orderBy(columns);
        pm.setSelectableState(new FluentSelectableOrState(pm, query));
        return pm;
    }
}
