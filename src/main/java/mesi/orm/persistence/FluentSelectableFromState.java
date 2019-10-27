package mesi.orm.persistence;

import mesi.orm.exception.ORMesiQueryException;
import mesi.orm.query.FluentSelectable;
import mesi.orm.query.SelectQuery;

final class FluentSelectableFromState extends FluentSelectableState {

    FluentSelectableFromState(PersistenceManagerImpl pm, SelectQuery query) {
        super(pm, query);
    }

    @Override
    public FluentSelectable select(String... columns) {
        throw new ORMesiQueryException("Invalid call: you can only call where or orderBy after from");
    }

    @Override
    public FluentSelectable from(Class persistentClass) {
        throw new ORMesiQueryException("Invalid call: you can only call where or orderBy after from");
    }

    @Override
    public FluentSelectable where(String condition) {
        query = query.where(condition);
        pm.setSelectableState(new FluentSelectableWhereState(pm, query));
        return pm;
    }

    @Override
    public FluentSelectable andWhere(String condition) {
        throw new ORMesiQueryException("Invalid call: you can only call where or orderBy after from");
    }

    @Override
    public FluentSelectable orWhere(String condition) {
        throw new ORMesiQueryException("Invalid call: you can only call where or orderBy after from");
    }

    @Override
    public FluentSelectable orderBy(String... columns) {
        query = query.orderBy(columns);
        pm.setSelectableState(new FluentSelectableOrState(pm, query));
        return pm;
    }
}
