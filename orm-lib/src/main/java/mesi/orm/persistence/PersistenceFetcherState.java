package mesi.orm.persistence;

import mesi.orm.exception.ORMesiQueryException;
import mesi.orm.query.SelectQuery;

/**
 * base class for
 * state pattern used for FluentSelectable implementations
 */
abstract class PersistenceFetcherState implements PersistenceFetcher {

    protected final PersistenceManagerImpl pm;
    protected SelectQuery query;

    PersistenceFetcherState(PersistenceManagerImpl pm) {
        this.pm = pm;
    }

    PersistenceFetcherState(PersistenceManagerImpl pm, SelectQuery query) {
        this.pm = pm;
        this.query = query;
    }

    protected void throwUsageError(String msg) {
        query = null;
        pm.setFetchState(new PersistenceFetcherInitialState(pm));
        throw new ORMesiQueryException(msg);
    }

    public abstract PersistenceFetcher from(Class cls);
}
