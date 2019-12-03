package mesi.orm.persistence

import mesi.orm.conn.DatabaseConnection
import mesi.orm.conn.DatabaseConnectionFactory
import mesi.orm.conn.DatabaseSystem
import mesi.orm.exception.ORMesiException
import mesi.orm.persistence.annotations.Persistent
import mesi.orm.persistence.fetch.ResultSetParser
import mesi.orm.persistence.transform.PersistentObject
import mesi.orm.query.QueryBuilder
import mesi.orm.query.QueryBuilderFactory
import mesi.orm.util.Persistence
import java.sql.ResultSet
import kotlin.reflect.KClass

/**
 * base respository, which has to be extended by the
 * user with needed primary and entity types
 */
class BaseRepository<PRIMARY : Any, ENTITY : Any>(private val database : DatabaseConnection, private val queryBuilder: QueryBuilder, private val resultParser : ResultSetParser, private val entityClass : KClass<ENTITY>) : Repository<PRIMARY, ENTITY> {

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
        val instance = entityClass.java.getConstructor().newInstance()
        val primaryName = Persistence.getNameOfPrimaryKey(instance)

        val query = queryBuilder.select().from(entityClass.java).where("$primaryName=$id")

        database.select(query).use {
            val rs = it.resultSet
            return getFromResultSet(rs) as ENTITY?
        }
    }

    private fun getForeign(primary : Any, primaryName : String, clazz : KClass<*>) : Any? {

        val query = queryBuilder.select().from(clazz.java).where("$primaryName=$primary")

        database.select(query).use {
            return getFromResultSet(it.resultSet, clazz)
        }
    }

    private fun getFromResultSet(rs : ResultSet, clazz : KClass<*> = entityClass) : Any? {

        val instance = clazz.java.getConstructor().newInstance()
        val persistentObject = PersistentObject.from(instance)

        if(rs.next()) {

            persistentObject.getAllNonForeigns().forEach { prop ->
                val value = resultParser.parsePropertyFrom(prop, rs)

                val property = clazz.java.getDeclaredField(prop.name)
                property.trySetAccessible()

                if(property.canAccess(instance)) property.set(instance, value)
            }

            persistentObject.getForeigns().forEach { prop->
                var primary = resultParser.parsePropertyFrom(prop, rs)!!
                val foreignInstance = prop.kotlinClass.java.getConstructor().newInstance()
                val primaryName = Persistence.getNameOfPrimaryKey(foreignInstance)
                if(primary is String)
                    primary = "'$primary'"

                val fetchedForeign = getForeign(primary, primaryName, prop.kotlinClass)

                val property = clazz.java.getDeclaredField(prop.name)
                property.trySetAccessible()

                if(property.canAccess(instance)) property.set(instance, fetchedForeign)
            }

            return instance

        } else {
            return null
        }
    }
}