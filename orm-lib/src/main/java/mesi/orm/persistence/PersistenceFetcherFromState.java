package mesi.orm.persistence;

import mesi.orm.query.SelectQuery;

import java.util.List;

final class PersistenceFetcherFromState extends PersistenceFetcherState {

    PersistenceFetcherFromState(PersistenceManagerImpl pm, SelectQuery query) {
        super(pm, query);
    }

    @Override
    public PersistenceFetcher from(Class persistentClass) {
        throwUsageError("Invalid call: you can only call where after from");
        return pm;
    }

    @Override
    public List<Object> fetch() {
        return pm.fetch();
    }

    @Override
    public PersistenceFetcher where(String condition) {
        query = query.where(condition);
        pm.setFetchState(new PersistenceFetcherWhereState(pm, query));
        return pm;
    }

    @Override
    public PersistenceFetcher andWhere(String condition) {
        throwUsageError("Invalid call: you can only call where after from");
        return pm;
    }

    @Override
    public PersistenceFetcher orWhere(String condition) {
        throwUsageError("Invalid call: you can only call where after from");
        return pm;
    }
}
