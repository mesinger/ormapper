package mesi.orm.conn;

import mesi.orm.query.Query;

/***
 * Performs CRUD operations on the underlying database system
 */
interface DatabaseModifier {

    /***
     * creates a new table on the database system
     * @param query sql query for creating a given table
     */
    void createTable(Query query);

    /***
     * checks if a given table is already present in the database system
     * @param tableName name of table
     * @return true, if table exists, false otherwise
     */
    boolean tableExists(String tableName);

    /**
     * inserts a new row
     * of a given list of fields in
     * a table named tableName
     * @param query
     * @return generated primary key
     */
    long insert(Query query);
}
