package mesi.orm.conn;

/***
 * Performs CRUD operations on the underlying database system
 */
interface DatabaseModifier {

    /***
     * creates a new table on the database system
     * @param tableName name of the created table
     * @param entries list of columns
     */
    void createTable(String tableName, TableDescriptor... entries);

    /***
     * checks if a given table is already present in the database system
     * @param tableName name of table
     * @return true, if table exists, false otherwise
     */
    boolean tableExists(String tableName);
}
