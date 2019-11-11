package mesi.orm.persistence;

import mesi.orm.query.FluentSelectable;
import mesi.orm.query.SelectQuery;

import java.util.List;

final class PersistenceFetcherWhereState extends PersistenceFetcherState {

    PersistenceFetcherWhereState(PersistenceManagerImpl pm, SelectQuery query) {
        super(pm, query);
    }

    @Override
    public PersistenceFetcher from(Class persistentClass) {
        throwUsageError("Invalid call: you can only call andWhere or orWhere after where");
        return pm;
    }

    @Override
    public List<Object> fetch() {
        return pm.fetch();
    }

    @Override
    public PersistenceFetcher where(String condition) {
        throwUsageError("Invalid call: you can only call andWhere or orWhere after where");
        return pm;
    }

    @Override
    public PersistenceFetcher andWhere(String condition) {
        query = query.andWhere(condition);
        pm.setFetchState(new PersistenceFetcherWhereState(pm, query));
        return pm;
    }

    @Override
    public PersistenceFetcher orWhere(String condition) {
        query = query.orWhere(condition);
        pm.setFetchState(new PersistenceFetcherWhereState(pm, query));
        return pm;
    }
}
