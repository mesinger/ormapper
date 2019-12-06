package mesi.orm.persistence.fetch

/**
 * fetchable repository which can build complex query statements with where clauses
 */
interface RepositoryFetchable<PRIMARY, ENTITY> {

    /**
     * returns, if available, a signle entity of type [ENTITY] with the given primary [id]
     */
    fun get(id : PRIMARY) : ENTITY?

    /**
     * entry call for building a select query, returns this
     */
    fun getAll() : RepositoryFetchable<PRIMARY, ENTITY>

    /**
     * appends the where [condition] to the query
     */
    fun where(condition : String) : RepositoryFetchable<PRIMARY, ENTITY>

    /**
     * links two where clauses with and
     */
    fun and() : RepositoryFetchable<PRIMARY, ENTITY>

    /**
     * linsk two where clauses with or
     */
    fun or() : RepositoryFetchable<PRIMARY, ENTITY>

    /**
     * last call in an select query, returns list of found entities
     */
    fun fetch() : List<ENTITY>
}