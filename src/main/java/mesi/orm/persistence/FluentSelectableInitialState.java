package mesi.orm.persistence;

import mesi.orm.exception.ORMesiQueryException;
import mesi.orm.query.FluentSelectable;

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
        throw new ORMesiQueryException("Invalid call: Every query has to start with select");
    }

    @Override
    public FluentSelectable where(String condition) {
        throw new ORMesiQueryException("Invalid call: Every query has to start with select");
    }

    @Override
    public FluentSelectable andWhere(String condition) {
        throw new ORMesiQueryException("Invalid call: Every query has to start with select");
    }

    @Override
    public FluentSelectable orWhere(String condition) {
        throw new ORMesiQueryException("Invalid call: Every query has to start with select");
    }

    @Override
    public FluentSelectable orderBy(String... columns) {
        throw new ORMesiQueryException("Invalid call: Every query has to start with select");
    }
}
