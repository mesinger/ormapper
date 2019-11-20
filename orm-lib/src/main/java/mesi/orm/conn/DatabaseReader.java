package mesi.orm.conn;

import mesi.orm.query.Query;

/**
 * performs query operations on database systems
 */
public interface DatabaseReader {
    /**
     * used for executing a select query on a database
     * @param query containing the desired select query
     * @return resultset from database
     */
    DisposeableResultSet select(Query query);
}
