package mesi.orm.query;

import mesi.orm.conn.RDBMS;
import mesi.orm.exception.ORMesiConnectionException;

/**
 * Factory for creating querybuilder objects for different database systems
 */
public interface QueryBuilderFactory {

    /**
     * Creates the desired QueryBuilder
     * @param rdbmsType type of rdbms to be used
     * @return
     */
    static QueryBuilder create(RDBMS rdbmsType) {

        if(rdbmsType == null) {
            throw new ORMesiConnectionException("Unsupported RDBMS");
        }

        switch (rdbmsType) {
            case SQLITE:
                return new SQLiteQueryBuilder();
            case MSSQL:
                throw new ORMesiConnectionException("Unsupported RDBMS");
            default:
                throw new ORMesiConnectionException("Unsupported RDBMS");
        }
    }
}
