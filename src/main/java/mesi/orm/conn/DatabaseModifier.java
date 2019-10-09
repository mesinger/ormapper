package mesi.orm.conn;

import mesi.orm.persistence.PersistentField;
import mesi.orm.persistence.PersistentStructure;

/***
 * Performs CRUD operations on the underlying database system
 */
interface DatabaseModifier {

    /***
     * creates a new table on the database system
     * @param tableName name of the created table
     * @param entries list of columns
     */
    void createTable(String tableName, TableEntry... entries);

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
     * @param tableName name of table
     * @param fields list of fields
     */
    void insert(String tableName, PersistentField... fields);
}
