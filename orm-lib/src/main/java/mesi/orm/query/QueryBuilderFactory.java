package mesi.orm.query;

import mesi.orm.conn.DatabaseSystem;
import mesi.orm.exception.ORMesiException;

/**
 * Factory for creating querybuilder objects for different database systems
 */
public interface QueryBuilderFactory {

    /**
     * Creates the desired QueryBuilder
     * @param rdbmsType type of rdbms to be used
     * @return
     */
    static QueryBuilder create(DatabaseSystem rdbmsType) {

        if(rdbmsType == null) {
            throw new ORMesiException("Unsupported RDBMS");
        }

        switch (rdbmsType) {
            case SQLITE:
                return new SQLiteQueryBuilder();
            case MSSQL:
                throw new ORMesiException("Unsupported RDBMS");
            default:
                throw new ORMesiException("Unsupported RDBMS");
        }
    }
}
