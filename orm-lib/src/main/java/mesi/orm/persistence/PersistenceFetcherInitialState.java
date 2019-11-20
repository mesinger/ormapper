package mesi.orm.persistence;

import java.util.List;

final class PersistenceFetcherInitialState extends PersistenceFetcherState {

    PersistenceFetcherInitialState(PersistenceManagerImpl pm) {
        super(pm);
    }

    @Override
    public PersistenceFetcher from(Class cls) {
        query = pm.getQueryBuilder().select().from(cls);
        query.setTargetClass(cls);
        pm.setFetchState(new PersistenceFetcherFromState(pm, query));
        return pm;
    }

    @Override
    public List<Object> fetch() {
        throwUsageError("Invalid call: Every query has to start with from");
        return List.of();
    }

    @Override
    public PersistenceFetcher where(String condition) {
        throwUsageError("Invalid call: Every query has to start with from");
        return pm;
    }

    @Override
    public PersistenceFetcher andWhere(String condition) {
        throwUsageError("Invalid call: Every query has to start with from");
        return pm;
    }

    @Override
    public PersistenceFetcher orWhere(String condition) {
        throwUsageError("Invalid call: Every query has to start with select");
        return pm;
    }
}
