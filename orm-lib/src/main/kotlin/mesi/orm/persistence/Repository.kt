package mesi.orm.persistence

import mesi.orm.cache.RepositoryCache
import mesi.orm.conn.DatabaseConnectionFactory
import mesi.orm.conn.DatabaseSystem
import mesi.orm.exception.ORMesiException
import mesi.orm.persistence.annotations.Persistent
import mesi.orm.persistence.fetch.RepositoryFetchable
import mesi.orm.persistence.fetch.ResultSetParser
import mesi.orm.query.QueryBuilderFactory
import mesi.orm.util.Persistence

/**
 * manages access
 * to persistent elements
 * capable of storing and fetching
 * [PRIMARY] type of primary identifier
 * [ENTITY] type of persistent class
 */
interface Repository<PRIMARY, ENTITY> : RepositoryFetchable<PRIMARY, ENTITY> {

    /**
     * tries to persist given [entity]
     * and on success returning [PRIMARY] key
     * of the persisted object.
     * returns null if entity already exists
     */
    fun save(entity: ENTITY)

    /**
     * tries to persist given [entity]
     * which already exists in the database, and
     * returns the updated [ENTITY] on success
     */
    fun update(entity: ENTITY)

    companion object Factory {

        inline fun <reified PRIMARY : Any, reified ENTITY : Any> create(system: DatabaseSystem, connectionString: String): Repository<PRIMARY, ENTITY> {

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
                    ResultSetParser.create(system),
                    RepositoryCache.create(),
                    ENTITY::class
            )
        }
    }
}