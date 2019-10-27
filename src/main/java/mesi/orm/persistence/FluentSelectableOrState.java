package mesi.orm.persistence;

import mesi.orm.exception.ORMesiQueryException;
import mesi.orm.query.FluentSelectable;
import mesi.orm.query.SelectQuery;

final class FluentSelectableOrState extends FluentSelectableState {

    FluentSelectableOrState(PersistenceManagerImpl pm, SelectQuery query) {
        super(pm, query);
    }

    @Override
    public FluentSelectable select(String... columns) {
        throw new ORMesiQueryException("Invalid call: no further calls expected after orderBy");
    }

    @Override
    public FluentSelectable from(Class persistentClass) {
        throw new ORMesiQueryException("Invalid call: no further calls expected after orderBy");
    }

    @Override
    public FluentSelectable where(String condition) {
        throw new ORMesiQueryException("Invalid call: no further calls expected after orderBy");
    }

    @Override
    public FluentSelectable andWhere(String condition) {
        throw new ORMesiQueryException("Invalid call: no further calls expected after orderBy");
    }

    @Override
    public FluentSelectable orWhere(String condition) {
        throw new ORMesiQueryException("Invalid call: no further calls expected after orderBy");
    }

    @Override
    public FluentSelectable orderBy(String... columns) {
        throw new ORMesiQueryException("Invalid call: no further calls expected after orderBy");
    }
}
