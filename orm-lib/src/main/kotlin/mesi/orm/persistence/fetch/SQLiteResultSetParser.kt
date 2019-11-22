package mesi.orm.persistence.fetch

import mesi.orm.exception.ORMesiException
import mesi.orm.persistence.transform.PersistentProperty
import mesi.orm.persistence.transform.PersistentPropertyType
import java.sql.ResultSet
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.reflect.full.functions

internal class SQLiteResultSetParser : ResultSetParser{
    override fun parsePropertyFrom(persistentProperty: PersistentProperty, rs: ResultSet) : Any? {
        return when(persistentProperty.type) {
            PersistentPropertyType.BOOL -> rs.getBoolean(persistentProperty.name)
            PersistentPropertyType.LONG -> {
                when(persistentProperty.kotlinClass) {
                    Int::class -> rs.getInt(persistentProperty.name)
                    Long::class -> rs.getLong(persistentProperty.name)
                    else -> throw ORMesiException("\nInvalid integer value stored in database for ${persistentProperty.kotlinClass.simpleName}:${persistentProperty.name}")
                }
            }
            PersistentPropertyType.DOUBLE -> {
                when(persistentProperty.kotlinClass) {
                    Float::class -> rs.getFloat(persistentProperty.name)
                    Double::class -> rs.getDouble(persistentProperty.name)
                    else -> throw ORMesiException("\nInvalid float value stored in database for ${persistentProperty.kotlinClass.simpleName}:${persistentProperty.name}")
                }
            }
            PersistentPropertyType.STRING -> {
                val stringRepresentation = rs.getString(persistentProperty.name)
                if(persistentProperty.isEnum) {
                    return persistentProperty.kotlinClass.functions.find { it.name == "valueOf" }?.call(stringRepresentation)
                }
                else {
                    return stringRepresentation
                }
            }
            PersistentPropertyType.TIME -> {
                val stringRepresentation = rs.getString(persistentProperty.name)
                return LocalTime.parse(stringRepresentation, DateTimeFormatter.ISO_LOCAL_TIME)
            }
            PersistentPropertyType.DATE -> {
                val stringRepresentation = rs.getString(persistentProperty.name)
                return LocalDate.parse(stringRepresentation, DateTimeFormatter.ISO_LOCAL_DATE)
            }
            PersistentPropertyType.DATETIME -> {
                val stringRepresentation = rs.getString(persistentProperty.name)
                return LocalDateTime.parse(stringRepresentation, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            }
        }
    }
}