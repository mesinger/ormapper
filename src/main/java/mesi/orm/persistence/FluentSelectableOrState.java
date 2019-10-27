package mesi.orm.persistence;

import mesi.orm.query.FluentSelectable;
import mesi.orm.query.SelectQuery;

final class FluentSelectableOrState extends FluentSelectableState {

    FluentSelectableOrState(PersistenceManagerImpl pm, SelectQuery query) {
        super(pm, query);
    }

    @Override
    public FluentSelectable select(String... columns) {
        throwUsageError("Invalid call: no further calls expected after orderBy");
        return pm;
    }

    @Override
    public FluentSelectable from(Class persistentClass) {
        throwUsageError("Invalid call: no further calls expected after orderBy");
        return pm;
    }

    @Override
    public FluentSelectable where(String condition) {
        throwUsageError("Invalid call: no further calls expected after orderBy");
        return pm;
    }

    @Override
    public FluentSelectable andWhere(String condition) {
        throwUsageError("Invalid call: no further calls expected after orderBy");
        return pm;
    }

    @Override
    public FluentSelectable orWhere(String condition) {
        throwUsageError("Invalid call: no further calls expected after orderBy");
        return pm;
    }

    @Override
    public FluentSelectable orderBy(String... columns) {
        throwUsageError("Invalid call: no further calls expected after orderBy");
        return pm;
    }
}
