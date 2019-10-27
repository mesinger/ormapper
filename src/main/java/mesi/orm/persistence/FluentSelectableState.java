package mesi.orm.persistence;

import mesi.orm.exception.ORMesiQueryException;
import mesi.orm.query.FluentSelectable;
import mesi.orm.query.SelectQuery;

abstract class FluentSelectableState implements FluentSelectable {

    protected final PersistenceManagerImpl pm;
    protected SelectQuery query;

    FluentSelectableState(PersistenceManagerImpl pm) {
        this.pm = pm;
    }

    FluentSelectableState(PersistenceManagerImpl pm, SelectQuery query) {
        this.pm = pm;
        this.query = query;
    }

    protected void throwUsageError(String msg) {
        query = null;
        pm.setSelectableState(new FluentSelectableInitialState(pm));
        throw new ORMesiQueryException(msg);
    }
}
