package mesi.orm.persistence

import mesi.orm.persistence.fetch.RepositoryFetchable

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
}