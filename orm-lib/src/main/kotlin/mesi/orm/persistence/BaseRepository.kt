package mesi.orm.persistence

import mesi.orm.conn.DatabaseConnection
import mesi.orm.conn.DatabaseConnectionFactory
import mesi.orm.conn.DatabaseSystem
import mesi.orm.exception.ORMesiException
import mesi.orm.persistence.annotations.Persistent
import mesi.orm.persistence.fetch.RepositoryFetchable
import mesi.orm.persistence.fetch.ResultSetParser
import mesi.orm.persistence.transform.PersistentObject
import mesi.orm.persistence.transform.PersistentProperty
import mesi.orm.query.QueryBuilder
import mesi.orm.query.QueryBuilderFactory
import mesi.orm.query.SelectQuery
import mesi.orm.util.Persistence
import java.sql.ResultSet
import kotlin.reflect.KClass

/**
 * base respository, which has to be extended by the
 * user with needed primary and entity types
 */
class BaseRepository<PRIMARY : Any, ENTITY : Any>
(private val database : DatabaseConnection, private val queryBuilder: QueryBuilder, private val resultParser : ResultSetParser, private val entityClass : KClass<ENTITY>)
    : Repository<PRIMARY, ENTITY> {

    private var selectQuery : SelectQuery

    init {
        database.open()
        selectQuery = queryBuilder.select().from(entityClass.java)
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
        TODO()
    }

    override fun get(id: PRIMARY): ENTITY? {
        val instance = entityClass.java.getConstructor().newInstance()
        val primaryName = Persistence.getNameOfPrimaryKey(instance)
        val persistentObject = PersistentObject.from(instance)

        val query = queryBuilder.select().from(entityClass.java).where("$primaryName=$id")

        database.select(query).use {
            val rs = it.resultSet

            if(rs.next()) {

                persistentObject.properties.forEach { prop -> injectPropertyFromResultSet(instance, prop, rs)}
                return instance

            } else {
                return null
            }
        }
    }

    private fun getForeign(primary : Any, clazz : KClass<*>) : Any? {
        return clazz.java.getConstructor().newInstance()
    }

    override fun fetch(): List<ENTITY> {
        val instance = entityClass.java.getConstructor().newInstance()
        val persistentObject = PersistentObject.from(instance)

        val entities = mutableListOf<ENTITY>()

        database.select(selectQuery).use {
            val rs = it.resultSet

            while(rs.next()){
                val entity = entityClass.java.getConstructor().newInstance()

                persistentObject.getAllWithoutForeigns().forEach { injectPropertyFromResultSet(entity, it, rs) }
                persistentObject.getForeigns().forEach {  }

                entities.add(entity)
            }

            return entities
        }
    }

    override fun getAll(): RepositoryFetchable<PRIMARY, ENTITY> {
        selectQuery = queryBuilder.select().from(entityClass.java)
        return this
    }

    override fun where(condition: String): RepositoryFetchable<PRIMARY, ENTITY> {
        selectQuery = selectQuery.where(condition)
        return this
    }

    override fun and(): RepositoryFetchable<PRIMARY, ENTITY> {
        selectQuery = selectQuery.and()
        return this
    }

    override fun or(): RepositoryFetchable<PRIMARY, ENTITY> {
        selectQuery = selectQuery.or()
        return this
    }

    private fun injectPropertyFromResultSet(instance : ENTITY, prop : PersistentProperty, rs : ResultSet) {
        val value = resultParser.parsePropertyFrom(prop, rs)

        val property = entityClass.java.getDeclaredField(prop.name)
        property.trySetAccessible()

        if(property.canAccess(instance)) property.set(instance, value)
    }

    private fun injectPropertyFromValue(instance : ENTITY, prop : PersistentProperty, value : Any) {

        val property = entityClass.java.getDeclaredField(prop.name)
        property.trySetAccessible()

        if(property.canAccess(instance)) property.set(instance, value)
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

            if(Persistence.getPrimaryKey(ENTITY::class.java.getConstructor().newInstance()).kotlinClass != PRIMARY::class) {
                throw ORMesiException("Repository for class ${ENTITY::class.simpleName} expects primary keys of type ${PRIMARY::class.qualifiedName}")
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