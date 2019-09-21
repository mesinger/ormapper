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
    void createTable(String tableName, TableEntry ... entries);
}
