package mesi.orm.persistence.fetch

import mesi.orm.conn.DatabaseSystem
import mesi.orm.persistence.transform.PersistentProperty
import java.sql.ResultSet

/**
 * parses database resultsets to java objects
 */
interface ResultSetParser {

    /**
     * parses results from [ResultSet] by extracting informations from [persistentProperty]
     */
    fun parsePropertyFrom(persistentProperty: PersistentProperty, rs: ResultSet) : Any?

    companion object Factory {
        fun create(system : DatabaseSystem) : ResultSetParser {
            return when(system){
                DatabaseSystem.SQLITE -> SQLiteResultSetParser()
                DatabaseSystem.MSSQL -> TODO()
            }
        }
    }
}