package mesi.orm.conn;

/**
 * opens or closes a connection to a specific rdbms
 */
public interface DatabaseConnector {

    /**
     * Opens the connecition to the underlying rdbms
     */
    boolean open();

    /**
     * Closes the connection to the underlying rdbms
     */
    boolean close();
}
