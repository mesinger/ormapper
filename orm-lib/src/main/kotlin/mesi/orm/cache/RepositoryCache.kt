package mesi.orm.cache

/**
 * interface for cache implementations
 * for repositories
 */
interface RepositoryCache {

    /**
     * stores an object [value] of type [type] (as string) with identification [id] (as string)
     */
    fun put(type: String, id: String, value: Any)

    /**
     * returns true, if an object of type [type] exists with [id] in the cache
     */
    fun exists(type: String, id: String): Boolean {
        return get(type, id) != null
    }

    /**
     * returns, if present, an object of type [type], and identification [id]
     */
    fun get(type: String, id: String): Any?

    /**
     * releases the cache
     */
    fun release(): Unit

    companion object Factory {
        fun create(): RepositoryCache {
            return BaseRepositoryCache()
        }
    }
}

