package mesi.orm.persistence.transform

import mesi.orm.util.Persistence

/**
 * simple representation of persistent pojos
 */
class PersistentObject constructor(val tableName : String, val properties : Set<PersistentProperty>) {

    companion object Builder {
        fun from(instance : Any) : PersistentObject {

            val tableName = Persistence.getTableName(instance::class)

            val properties = mutableSetOf<PersistentProperty>().apply {
                add(Persistence.getPrimaryKey(instance))
                addAll(Persistence.getEnums(instance))
                addAll(Persistence.getOthers(instance))
            }

            return PersistentObject(tableName, properties)
        }
    }
}

data class PersistentProperty(
        val name : String,
        val type : PersistentPropertyType,
        var value : Any?,
        val isPrimary : Boolean,
        val isForeign : Boolean,
        val foreignTable : String? = "",
        val foreignRef : String? = ""
)

enum class PersistentPropertyType {
    BOOL,
    LONG,
    DOUBLE,
    STRING,
    TIME,
    DATE,
    DATETIME
}
