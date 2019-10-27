package mesi.orm.persistence;

import mesi.orm.query.FluentSelectable;
import mesi.orm.query.SelectQuery;

final class FluentSelectableSelectState extends FluentSelectableState {

    FluentSelectableSelectState(PersistenceManagerImpl pm, SelectQuery query) {
        super(pm, query);
    }

    @Override
    public FluentSelectable select(String... columns) {
        throwUsageError("Invalid call: you can only call from after select");
        return pm;
    }

    @Override
    public FluentSelectable from(Class persistentClass) {
        query = query.from(persistentClass);
        query.setTargetClass(persistentClass);
        pm.setSelectableState(new FluentSelectableFromState(pm, query));
        return pm;
    }

    @Override
    public FluentSelectable where(String condition) {
        throwUsageError("Invalid call: you can only call from after select");
        return pm;
    }

    @Override
    public FluentSelectable andWhere(String condition) {
        throwUsageError("Invalid call: you can only call from after select");
        return pm;
    }

    @Override
    public FluentSelectable orWhere(String condition) {
        throwUsageError("Invalid call: you can only call from after select");
        return pm;
    }

    @Override
    public FluentSelectable orderBy(String... columns) {
        throwUsageError("Invalid call: you can only call from after select");
        return pm;
    }
}
