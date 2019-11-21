package mesi.orm.query;

import mesi.orm.persistence.transform.PersistentObject;

/**
 * used for building insert statements
 */
public abstract class InsertQuery extends Query {

    protected InsertQuery(PersistentObject persistentObject) {}
}
