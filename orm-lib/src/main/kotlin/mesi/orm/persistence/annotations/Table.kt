package mesi.orm.persistence.annotations

/**
 * sets the table name of a persistent class
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Table(val tableName : String)