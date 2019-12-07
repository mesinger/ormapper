package mesi.orm.util

import org.junit.jupiter.api.Test
import kotlin.reflect.KClass
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ReflectedTest {

    @Test
    fun testIsPersistent() {
        assertFalse(Reflected.isPersistent(NotPersistentClass::class))
        assertTrue(Reflected.isPersistent(PersistentClass::class))
    }

    @Test
    fun testGetAllProperties() {
        assertEquals(1, Reflected.getAllProperties(PersistentClass::class as KClass<Any>).size)
        assertEquals(2, Reflected.getAllProperties(SubPersistentClass::class as KClass<Any>).size)
    }
}