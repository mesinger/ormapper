package mesi.orm.cache

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BaseRepositoryCacheTest {

    private lateinit var cache : RepositoryCache

    @BeforeEach
    fun setup() {
        cache = BaseRepositoryCache()
    }

    @Test
    fun testCachingAndReleasing() {

        val type = "type"
        val id = "id"

        assertNull(cache.get(type, id))
        assertFalse(cache.exists(type, id))

        cache.put(type, id, Any())

        assertNotNull(cache.get(type, id))
        assertTrue(cache.exists(type, id))

        cache.release()

        assertNull(cache.get(type, id))
        assertFalse(cache.exists(type, id))
    }
}