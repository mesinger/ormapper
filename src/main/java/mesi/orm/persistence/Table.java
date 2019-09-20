package mesi.orm.persistence;

/***
 * table annoation for persistent entities,
 * where name defines the name of the table created
 * in the rdbms
 */
@Deprecated
public @interface Table {
    String name();
}
