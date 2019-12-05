package mesi.orm.cache

internal inline class RawCache(val dict : MutableMap<String, Any> = mutableMapOf())

internal open class BaseRepositoryCache internal constructor () : RepositoryCache {

    protected var raw = RawCache()

    override fun put(type: String, id: String, value: Any) {
        raw.dict[getKey(type, id)] = value
    }

    override fun get(type: String, id: String): Any? {
        return raw.dict[getKey(type, id)]
    }

    override fun release() {
        raw.dict.clear()
    }

    private fun getKey(type: String, id: String) : String {
        return "$type-$id"
    }
}