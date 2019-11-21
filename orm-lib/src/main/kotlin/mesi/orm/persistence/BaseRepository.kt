package mesi.orm.persistence

import mesi.orm.conn.DatabaseConnection
import mesi.orm.conn.DatabaseConnectionFactory
import mesi.orm.conn.DatabaseSystem
import mesi.orm.persistence.transform.PersistentObject
import mesi.orm.query.QueryBuilder
import mesi.orm.query.QueryBuilderFactory

/**
 * base respository, which has to be extended by the
 * user with needed primary and entity types
 */
class BaseRepository<PRIMARY : Any, ENTITY : Any>(private val database : DatabaseConnection, private val queryBuilder: QueryBuilder) : Repository<PRIMARY, ENTITY> {

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

    companion object Factory {

        inline fun <reified PRIMARY : Any, reified ENTITY : Any> create(system : DatabaseSystem, connectionString : String) : BaseRepository<PRIMARY, ENTITY> {
            return BaseRepository<PRIMARY, ENTITY>(DatabaseConnectionFactory.create(system, connectionString), QueryBuilderFactory.create(system))
        }
    }
}