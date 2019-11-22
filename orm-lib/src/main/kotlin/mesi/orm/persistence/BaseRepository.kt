package mesi.orm.persistence

import mesi.orm.conn.DatabaseConnection
import mesi.orm.conn.DatabaseConnectionFactory
import mesi.orm.conn.DatabaseSystem
import mesi.orm.exception.ORMesiException
import mesi.orm.persistence.annotations.Persistent
import mesi.orm.persistence.annotations.Primary
import mesi.orm.persistence.fetch.ResultSetParser
import mesi.orm.persistence.transform.PersistentObject
import mesi.orm.query.QueryBuilder
import mesi.orm.query.QueryBuilderFactory
import mesi.orm.util.Persistence
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
        val persistentObject = PersistentObject.from(instance)

        val query = queryBuilder.select().from(entityClass.java).where("$primaryName=$id")

        database.select(query).use {
            val rs = it.resultSet

            if(rs.next()) {

                persistentObject.properties.forEach { prop ->
                    run {
                        val value = resultParser.parsePropertyFrom(prop, rs)

                        val property = entityClass.java.getDeclaredField(prop.name)
                        property.trySetAccessible()

                        if(property.canAccess(instance)) property.set(instance, value)
                    }
                }

                return instance

            } else {
                return null
            }
        }
    }

    companion object Factory {

        inline fun <reified PRIMARY : Any, reified ENTITY : Any> create(system: DatabaseSystem, connectionString: String): BaseRepository<PRIMARY, ENTITY> {

            // checks for no-arg constructor
            try {
                ENTITY::class.java.getConstructor().newInstance()
            } catch (ex : NoSuchMethodException) {
                throw ORMesiException("Class ${ENTITY::class.simpleName} should have a no-arg constructor")
            }

            if(!Persistence.isAnnotatedWithPersistent(ENTITY::class)) {
                throw ORMesiException("Class ${ENTITY::class.simpleName} needs to be annotated with ${Persistent::class.qualifiedName}")
            }

            if(!Persistence.hasValidPrimaryProperty(ENTITY::class.java.getConstructor().newInstance())) {
                throw ORMesiException("Class ${ENTITY::class.simpleName} needs exactly one property annotated with ${Primary::class.qualifiedName} of type kotlin.Long or kotlin.String")
            }

            return BaseRepository<PRIMARY, ENTITY>(
                    DatabaseConnectionFactory.create(system, connectionString),
                    QueryBuilderFactory.create(system),
                    ResultSetParser.Factory.create(system),
                    ENTITY::class
            )
        }
    }
}