package mesi.orm.conn;

import mesi.orm.query.Query;

public interface DatabaseReader {
    DisposeableResultSet select(Query query);
}
