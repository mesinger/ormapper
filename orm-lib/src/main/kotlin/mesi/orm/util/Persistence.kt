package mesi.orm.util

import mesi.orm.exception.ORMesiException
import mesi.orm.persistence.Nullable
import mesi.orm.persistence.Primary
import mesi.orm.persistence.Table
import mesi.orm.persistence.transform.PersistentProperty
import mesi.orm.persistence.transform.PersistentPropertyType
import java.time.LocalDate
import java.time.LocalTime
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createType
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubtypeOf

object Persistence {

    /**
     * returns the [instance] primary key as [PersistentProperty]
     */
    fun getPrimaryKey(instance : Any) : PersistentProperty {
        val clazz : KClass<Any> = instance::class as KClass<Any>
        Reflected.getAllProperties(clazz).filter { prop -> prop.findAnnotation<Primary>() != null }.firstOrNull()?.let {

            val name = it.name
            val type = getPrimaryKeyType(it)
            val value = it.get(instance)

            return PersistentProperty(name, type, value, isPrimary = true, isNullable = false, isForeign = false, foreignTable = "", foreignRef = "")
        }

        throw ORMesiException("No primary key for class ${clazz.simpleName}")
    }

    /**
     * maps reflected [prop] to a [PersistentPropertyType]
     * throws [ORMesiException] in case of an invalid primary type
     */
    fun getPrimaryKeyType(prop : KProperty1<*, *>) : PersistentPropertyType {
        return when {
            prop.returnType.isSubtypeOf(Long::class.createType()) -> PersistentPropertyType.LONG
            prop.returnType.isSubtypeOf(String::class.createType()) -> PersistentPropertyType.STRING
            else -> throw ORMesiException("Invalid type for primary key\nOnly use kotlin.Long, or kotlin.String as primary keys")
        }
    }

    fun getOther(instance: Any) : List<PersistentProperty> {

        val clazz : KClass<Any> = instance::class as KClass<Any>

        val properties = mutableListOf<PersistentProperty>()

        Reflected.getAllPropertiesRecursive(clazz)
                .filter { prop -> prop.findAnnotation<Primary>() == null  }
                .filter { prop -> prop.findAnnotation<Transient>() == null  }
                .forEach { prop ->
                    val name = prop.name
                    val type = getPropertyType(prop)
                    val value = prop.get(instance)
                    val isNullable = prop.findAnnotation<Nullable>() != null

                    properties.add(PersistentProperty(name, type, value, isPrimary = false, isNullable = isNullable, isForeign = false, foreignTable = "", foreignRef = ""))
                }

        return properties
    }

    fun getPropertyType(prop : KProperty1<*, *>) : PersistentPropertyType {
        return when {
            prop.returnType.isSubtypeOf(Long::class.createType()) || prop.returnType.isSubtypeOf(Int::class.createType()) -> PersistentPropertyType.LONG
            prop.returnType.isSubtypeOf(Double::class.createType()) || prop.returnType.isSubtypeOf(Float::class.createType()) -> PersistentPropertyType.DOUBLE
            prop.returnType.isSubtypeOf(String::class.createType()) -> PersistentPropertyType.STRING
            prop.returnType.isSubtypeOf(Boolean::class.createType()) -> PersistentPropertyType.BOOL
            prop.returnType.isSubtypeOf(LocalTime::class.createType()) -> PersistentPropertyType.TIME
            prop.returnType.isSubtypeOf(LocalDate::class.createType()) -> PersistentPropertyType.DATE
            else -> throw ORMesiException("Unsupported type found")
        }
    }

    /**
     * returns [Table.tableName] if [Table] annotation is present
     * otherwise class.simpleName
     */
    fun getTableName(clazz : KClass<out Any>) : String {

        val tableAnnotation = clazz.findAnnotation<Table>()

        tableAnnotation?.
                let { return tableAnnotation.tableName }
                ?: kotlin.run { return clazz.simpleName + "s" }
    }
}