package mesi.orm.persistence

/**
 * sets the table name of a persistent class
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Table(val tableName : String)