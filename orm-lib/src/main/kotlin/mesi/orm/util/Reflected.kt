package mesi.orm.util

import mesi.orm.persistence.annotations.Persistent
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.superclasses

/**
 * static functions
 * used for validating persistent objects
 * and extracting informations
 */
object Reflected {

    /**
     * checks if [Persistent] annotation is present on [o]
     */
    fun isPersistent(clazz : KClass<out Any>) : Boolean {
        return clazz.findAnnotation<Persistent>() != null
    }

    /**
     * returns collection of all properties,
     * recursively over all super classes
     */
    fun getAllPropertiesRecursive(clazz : KClass<Any>) : Collection<KProperty1<Any, *>> {
        return mutableListOf<KProperty1<Any, *>>().apply {
            addAll(getAllProperties(clazz))
            clazz.superclasses.forEach { addAll(it.memberProperties as Collection<KProperty1<Any, *>>) }
        }
    }

    /**
     * returns collections of all properties
     * in class
     */
    fun getAllProperties(clazz : KClass<Any>) : Collection<KProperty1<Any, *>> {
        return mutableListOf<KProperty1<Any, *>>().apply { addAll(clazz.memberProperties) }
    }
}