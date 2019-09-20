package mesi.orm.persistence;

/***
 * interface for PersistenceManager
 * hold methods which are used for storing and accessing
 * between objects and databases
 */
interface PersistenceOperations {
    /***
     * persists an Persistent entity in the underlying rdbms
     * @param o object to be mapped (has to be annotated with @Persistent)
     */
    void persist(Object o);

    /***
     * checks if an object is annotated as Persistent
     * @param o object
     * @return true, if object is annotated with @Persistent, false otherwise
     */
    static boolean isObjectPersistent(Object o) {
        return o.getClass().getAnnotation(Persistent.class) != null;
    }

    /***
     * checks if persistent object has exactly one member tagged as id
     * @param o persistent object
     * @return true, if object has exactly one member tagged as @Id, false otherwise
     */
    static boolean hasPersistentObjectIdentification(Object o) {
        throw new RuntimeException("not implemented");
    }
}
