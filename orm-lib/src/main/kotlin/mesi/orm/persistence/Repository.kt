package mesi.orm.persistence

import mesi.orm.cache.RepositoryCache
import mesi.orm.conn.DatabaseConnectionFactory
import mesi.orm.conn.DatabaseSystem
import mesi.orm.exception.ORMesiException
import mesi.orm.persistence.annotations.Persistent
import mesi.orm.persistence.fetch.RepositoryFetchable
import mesi.orm.persistence.fetch.ResultSetParser
import mesi.orm.query.QueryBuilderFactory
import mesi.orm.transaction.RepositoryTransaction
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

    /**
     * returns an initialized [RepositoryTransaction]
     */
    fun getTransaction() : RepositoryTransaction
}