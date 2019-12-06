package mesi.orm.persistence.transform

import mesi.orm.persistence.annotations.ForeignRelation
import mesi.orm.util.Persistence
import kotlin.reflect.KClass

/**
 * simple representation of persistent pojos
 */
class PersistentObject constructor(val tableName : String, val properties : Set<PersistentProperty>) {

    fun getForeignsSimple() : List<PersistentProperty> {
        return properties.filter { it.isForeign }.filter { it.foreignRelation == ForeignRelation.ONE_TO_ONE || it.foreignRelation == ForeignRelation.MANY_TO_ONE }
    }

    fun getForeignsComplex() : List<PersistentProperty> {
        return properties.filter { it.isForeign }.filter { it.foreignRelation == ForeignRelation.ONE_TO_MANY }
    }

    fun getAllNonForeigns() : List<PersistentProperty> {
        return properties.filter { !it.isForeign }
    }

    fun getPrimary() : PersistentProperty? {
        return properties.find { it.isPrimary }
    }

    fun getAllWithoutForeigns() : List<PersistentProperty> {
        return properties.filter { !it.isForeign }
    }

    companion object Builder {
        fun from(instance : Any) : PersistentObject {

            val tableName = Persistence.getTableName(instance::class)

            val properties = mutableSetOf<PersistentProperty>().apply {
                add(Persistence.getPrimaryKey(instance))
                addAll(Persistence.getEnums(instance))
                addAll(Persistence.getForeigns(instance))
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
        val kotlinClass : KClass<*>,
        val isEnum : Boolean,
        val isPrimary : Boolean,
        val isForeign : Boolean,
        val foreignRelation : ForeignRelation = ForeignRelation.ONE_TO_ONE,
        val foreignTable : String = "",
        val foreignRef : String = ""
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
