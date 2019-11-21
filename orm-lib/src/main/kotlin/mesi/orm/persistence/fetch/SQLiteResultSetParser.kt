package mesi.orm.persistence.fetch

import mesi.orm.persistence.transform.PersistentProperty
import mesi.orm.persistence.transform.PersistentPropertyType
import java.sql.ResultSet

internal class SQLiteResultSetParser : ResultSetParser{
    override fun parsePropertyFrom(persistentProperty: PersistentProperty, rs: ResultSet) : Any {
        return when(persistentProperty.type) {
            PersistentPropertyType.BOOL -> rs.getBoolean(persistentProperty.name)
            PersistentPropertyType.LONG -> rs.getLong(persistentProperty.name)
            PersistentPropertyType.DOUBLE -> rs.getDouble(persistentProperty.name)
            PersistentPropertyType.STRING -> rs.getString(persistentProperty.name)
            PersistentPropertyType.TIME -> rs.getTime(persistentProperty.name)
            PersistentPropertyType.DATE -> rs.getDate(persistentProperty.name)
            PersistentPropertyType.DATETIME -> rs.getDate(persistentProperty.name)
        }
    }
}