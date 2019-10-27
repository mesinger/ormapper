package mesi.orm.persistence;

import mesi.orm.exception.ORMesiQueryException;
import mesi.orm.query.FluentSelectable;

import java.util.List;

final class FluentSelectableInitialState extends FluentSelectableState {

    FluentSelectableInitialState(PersistenceManagerImpl pm) {
        super(pm);
    }

    @Override
    public FluentSelectable select(String... columns) {
        query = pm.getQueryBuilder().select(columns);
        pm.setSelectableState(new FluentSelectableSelectState(pm, query));
        return pm;
    }

    @Override
    public FluentSelectable from(Class persistentClass) {
        throwUsageError("Invalid call: Every query has to start with select");
        return pm;
    }

    @Override
    public FluentSelectable where(String condition) {
        throwUsageError("Invalid call: Every query has to start with select");
        return pm;
    }

    @Override
    public FluentSelectable andWhere(String condition) {
        throwUsageError("Invalid call: Every query has to start with select");
        return pm;
    }

    @Override
    public FluentSelectable orWhere(String condition) {
        throwUsageError("Invalid call: Every query has to start with select");
        return pm;
    }

    @Override
    public FluentSelectable orderBy(String... columns) {
        throwUsageError("Invalid call: Every query has to start with select");
        return pm;
    }
}
