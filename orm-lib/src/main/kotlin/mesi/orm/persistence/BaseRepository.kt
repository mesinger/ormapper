package mesi.orm.persistence

import mesi.orm.cache.RepositoryCache
import mesi.orm.conn.DatabaseConnection
import mesi.orm.persistence.fetch.ResultSetParser
import mesi.orm.persistence.transform.PersistentObject
import mesi.orm.persistence.transform.PersistentProperty
import mesi.orm.query.QueryBuilder
import mesi.orm.util.Persistence
import java.sql.ResultSet
import kotlin.reflect.KClass

/**
 * base respository, which has to be extended by the
 * user with needed primary and entity types
 */
class BaseRepository<PRIMARY : Any, ENTITY : Any>(
        private val database : DatabaseConnection,
        private val queryBuilder: QueryBuilder,
        private val resultParser : ResultSetParser,
        private val cache : RepositoryCache,
        private val entityClass : KClass<ENTITY>
) : Repository<PRIMARY, ENTITY> {

    init {
        database.open()
    }

    override fun save(entity: ENTITY) {
        val persistentObject = PersistentObject.Builder.from(entity)

        if(!database.tableExists(persistentObject.tableName)) {

            val createQuery = queryBuilder.create(persistentObject.tableName)
            persistentObject.properties.forEach { createQuery.addColumn(it) }
            database.createTable(createQuery)
        }

        val insertQuery = queryBuilder.insert(persistentObject)
        database.insert(insertQuery)
    }

    override fun update(entity: ENTITY) {

    }

    override fun get(id: PRIMARY): ENTITY? {

        if(cache.exists(entityClass.toString(), id.toString())) {
            return cache.get(entityClass.toString(), id.toString()) as ENTITY?
        }

        val instance = entityClass.java.getConstructor().newInstance()
        val primaryName = Persistence.getNameOfPrimaryKey(instance)

        val query = queryBuilder.select().from(entityClass.java).where("$primaryName=$id")

        database.select(query).use {
            val rs = it.resultSet

            val entity = getFromResultSet(rs)

            entity?.let { cache.put(entityClass.toString(), id.toString(), entity) }

            return entity as ENTITY?
        }
    }

    private fun getForeign(primary : Any, primaryName : String, clazz : KClass<*>) : Any? {

        if(cache.exists(clazz.toString(), primary.toString())) {
            return cache.get(clazz.toString(), primary.toString())
        }

        val query = queryBuilder.select().from(clazz.java).where("$primaryName=$primary")

        database.select(query).use {

            val entity = getFromResultSet(it.resultSet, clazz)

            entity?.let { cache.put(clazz.toString(), primary.toString(), entity) }

            return entity as ENTITY?
        }
    }

    private fun getFromResultSet(rs : ResultSet, clazz : KClass<*> = entityClass) : Any? {

        val instance = clazz.java.getConstructor().newInstance()
        val persistentObject = PersistentObject.from(instance)

        if(rs.next()) {

            persistentObject.getAllNonForeigns().forEach { prop ->
                val value = resultParser.parsePropertyFrom(prop, rs)

                applyValueToInstance(instance, value!!, clazz, prop)
            }

            persistentObject.getForeignsSimple().forEach { prop->
                var primary = resultParser.parsePropertyFrom(prop, rs)!!
                val foreignInstance = prop.kotlinClass.java.getConstructor().newInstance()
                val primaryName = Persistence.getNameOfPrimaryKey(foreignInstance)
                if(primary is String)
                    primary = "'$primary'"

                val fetchedForeign = getForeign(primary, primaryName, prop.kotlinClass)

                applyValueToInstance(instance, fetchedForeign!!, clazz, prop)
            }

            persistentObject.getForeignsComplex().forEach { prop ->
                val primaryListasString = (resultParser.parsePropertyFrom(prop, rs)!! as String).removePrefix(";")
                val primariesList = (primaryListasString as String).split(';')

                val foreignInstances = mutableListOf<Any>()

                primariesList.forEach { primary -> foreignInstances.add(getForeign(primary, Persistence.getNameOfPrimaryKey(prop.kotlinClass.java.getConstructor().newInstance()), prop.kotlinClass)!!)}

                applyValueToInstance(instance, foreignInstances, clazz, prop)
            }

            return instance

        } else {
            return null
        }
    }

    private fun applyValueToInstance(instance : Any, value : Any, clazz: KClass<*>, prop : PersistentProperty) {
        val property = clazz.java.getDeclaredField(prop.name)
        property.trySetAccessible()

        if(property.canAccess(instance)) property.set(instance, value)
    }
}