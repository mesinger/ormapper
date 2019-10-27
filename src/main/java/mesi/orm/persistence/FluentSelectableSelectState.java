package mesi.orm.persistence;

import mesi.orm.exception.ORMesiQueryException;
import mesi.orm.query.FluentSelectable;
import mesi.orm.query.SelectQuery;

final class FluentSelectableSelectState extends FluentSelectableState {

    FluentSelectableSelectState(PersistenceManagerImpl pm, SelectQuery query) {
        super(pm, query);
    }

    @Override
    public FluentSelectable select(String... columns) {
        throw new ORMesiQueryException("Invalid call: you can only call from after select");
    }

    @Override
    public FluentSelectable from(Class persistentClass) {
        query = query.from(persistentClass);
        pm.setSelectableState(new FluentSelectableFromState(pm, query));
        return pm;
    }

    @Override
    public FluentSelectable where(String condition) {
        throw new ORMesiQueryException("Invalid call: you can only call from after select");
    }

    @Override
    public FluentSelectable andWhere(String condition) {
        throw new ORMesiQueryException("Invalid call: you can only call from after select");
    }

    @Override
    public FluentSelectable orWhere(String condition) {
        throw new ORMesiQueryException("Invalid call: you can only call from after select");
    }

    @Override
    public FluentSelectable orderBy(String... columns) {
        throw new ORMesiQueryException("Invalid call: you can only call from after select");
    }
}
