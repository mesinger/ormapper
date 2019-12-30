package mesi.orm.query

import mesi.orm.persistence.transform.PersistentObject
import mesi.orm.persistence.transform.PersistentProperty
import mesi.orm.persistence.transform.PersistentPropertyType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

internal class SQLiteUpdateQuery(persistentObject: PersistentObject) : UpdateQuery(persistentObject) {
    init {
        head.append("UPDATE ${persistentObject.tableName} SET ")

        persistentObject.properties
                .map(this::mapValue)
                .forEach{ head.append("${it.name} = ${it.value}, ")}

        head.setLength(head.length - 2)

        val idName = persistentObject.getPrimary()!!.name
        var idValue = persistentObject.getPrimary()!!.value

        if(idValue is String) {
            idValue = "'$idValue'"
        }

        head.append(" WHERE $idName = $idValue;")
    }

    private fun mapValue(property: PersistentProperty): PersistentProperty {
        when (property.type) {
            PersistentPropertyType.STRING -> {
                property.value = "'" + property.value + "'"
            }
            PersistentPropertyType.TIME -> {
                val modifiedProperty = property.value as LocalTime
                property.value = "'" + modifiedProperty.format(DateTimeFormatter.ISO_LOCAL_TIME) + "'"
            }
            PersistentPropertyType.DATE -> {
                val modifiedProperty = property.value as LocalDate
                property.value = "'" + modifiedProperty.format(DateTimeFormatter.ISO_LOCAL_DATE) + "'"
            }
            PersistentPropertyType.DATETIME -> {
                val modifiedProperty = property.value as LocalDateTime
                property.value = "'" + modifiedProperty.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "'"
            }
        }
        return property
    }
}