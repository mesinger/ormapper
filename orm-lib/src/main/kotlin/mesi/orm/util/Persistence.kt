package mesi.orm.util

import mesi.orm.exception.ORMesiException
import mesi.orm.persistence.annotations.*
import mesi.orm.persistence.transform.PersistentProperty
import mesi.orm.persistence.transform.PersistentPropertyType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createType
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.jvm.jvmErasure

object Persistence {

    /**
     * returns the [instance] primary key as [PersistentProperty]
     */
    fun getPrimaryKey(instance : Any) : PersistentProperty {
        val clazz : KClass<Any> = instance::class as KClass<Any>
        Reflected.getAllProperties(clazz).find { prop -> prop.findAnnotation<Primary>() != null }?.let {

            val name = it.name
            val type = getPropertyType(it)
            val value = it.get(instance)

            return PersistentProperty(name, type, value, it.returnType.jvmErasure, isEnum = false, isPrimary = true, isForeign = false)
        }

        throw ORMesiException("\nNo primary key for class ${clazz.simpleName}")
    }

    /**
     * returns all with [PersistentEnum] annotated properties in [instance]
     */
    fun getEnums(instance : Any) : List<PersistentProperty> {

        val clazz : KClass<Any> = instance::class as KClass<Any>
        val properties = mutableListOf<PersistentProperty>()

        Reflected.getAllPropertiesRecursive(clazz)
                .filter { prop -> prop.findAnnotation<PersistentEnum>() != null }
                .forEach { prop ->
                    val name = prop.name
                    val type = PersistentPropertyType.STRING
                    val value = prop.get(instance).toString()

                    properties.add(PersistentProperty(name, type, value, prop.returnType.jvmErasure, isEnum = true, isPrimary = false, isForeign = false))
                }

        return properties
    }

    /**
     * returns all with [Foreign] annotated properties in [instance]
     */
    fun getForeigns(instance: Any) : List<PersistentProperty> {

        val clazz : KClass<Any> = instance::class as KClass<Any>
        val properties = mutableListOf<PersistentProperty>()

        Reflected.getAllPropertiesRecursive(clazz)
                .filter { prop -> prop.findAnnotation<Foreign>() != null }
                .filter {prop -> prop.findAnnotation<Foreign>()!!.relation == ForeignRelation.ONE_TO_ONE || prop.findAnnotation<Foreign>()!!.relation == ForeignRelation.MANY_TO_ONE }
                .forEach { prop ->

                    val foreignInstance = prop.get(instance).let {it}.run { prop.returnType.jvmErasure.java.getConstructor().newInstance() }
                    val foreignPrimaryKey = Persistence.getPrimaryKey(foreignInstance)

                    val name = prop.name
                    val value = foreignPrimaryKey.value
                    val type = foreignPrimaryKey.type
                    val foreignTableName = getTableName(foreignInstance::class)
                    val foreignRef = foreignPrimaryKey.name

                    properties.add(PersistentProperty(name, type, value, prop.returnType.jvmErasure, isEnum = false, isPrimary = false, isForeign = true, foreignTable = foreignTableName, foreignRef = foreignRef))
                }

        return properties
    }

    /**
     * returns all other properties in an [instance]
     */
    fun getOthers(instance: Any) : List<PersistentProperty> {

        val clazz : KClass<Any> = instance::class as KClass<Any>
        val properties = mutableListOf<PersistentProperty>()

        Reflected.getAllPropertiesRecursive(clazz)
                .filter { prop -> prop.findAnnotation<Primary>() == null  }
                .filter { prop -> prop.findAnnotation<PersistentEnum>() == null  }
                .filter { prop -> prop.findAnnotation<PersistentTransient>() == null  }
                .filter { prop -> prop.findAnnotation<Foreign>() == null  }
                .forEach { prop ->
                    val name = prop.name
                    val type = getPropertyType(prop)
                    val value = prop.get(instance)

                    properties.add(PersistentProperty(name, type, value, prop.returnType.jvmErasure, isEnum = false, isPrimary = false, isForeign = false))
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
            prop.returnType.isSubtypeOf(LocalDateTime::class.createType()) -> PersistentPropertyType.DATETIME
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

    fun getTableName(clazz : Class<*>) : String {

        val tableAnnotation = clazz.getAnnotation(Table::class.java)

        tableAnnotation?.
                let { return tableAnnotation.tableName }
                ?: kotlin.run { return clazz.simpleName + "s" }
    }

    fun isAnnotatedWithPersistent(clazz: KClass<*>) : Boolean {
        return clazz.findAnnotation<Persistent>() != null
    }

    fun hasValidPrimaryProperty(instance: Any) : Boolean {
        return getPrimaryKey(instance) != null
    }

    fun getNameOfPrimaryKey(instance: Any) : String {
        return getPrimaryKey(instance).name
    }
}