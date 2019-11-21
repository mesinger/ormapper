package mesi.orm.query;

import mesi.orm.persistence.transform.PersistentProperty;
import mesi.orm.persistence.transform.PersistentPropertyType;

/**
 * used for building create table statements
 */
public abstract class CreateQuery extends Query{

    protected CreateQuery(String tableName) {

    }

    protected abstract CreateQuery addColumn(String name, PersistentPropertyType dataType, boolean isPrimary, boolean isForeign, String foreignTable, String foreignRef);

    /**
     * adds a new column to the create table statement
     * @param property Represents property in persistent class
     * @return this
     */
    public CreateQuery addColumn(PersistentProperty property) {

        return addColumn(
                property.getName(),
                property.getType(),
                property.isPrimary(),
                property.isForeign(),
                property.getForeignTable(),
                property.getForeignRef()
        );
    }
}
