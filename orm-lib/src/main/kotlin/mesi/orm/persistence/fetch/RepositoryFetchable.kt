package mesi.orm.persistence.fetch

/**
 * fetchable repository which can build complex query statements with where clauses
 */
interface RepositoryFetchable<PRIMARY, ENTITY> {
    fun get(id : PRIMARY) : ENTITY?
}