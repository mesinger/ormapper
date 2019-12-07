package mesi.orm.util

import mesi.orm.exception.ORMesiException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class PersistenceTest {

    @Test
    fun testGetPrimaryKey() {
        assertNotNull(Persistence.getPrimaryKey(WithPrimary(1)))
        assertThrows<ORMesiException> { Persistence.getPrimaryKey(WithoutPrimary(1)) }
    }

    @Test
    fun testGetEnums() {
        assertEquals(1, Persistence.getEnums(WithEnums(Enum.First)).size)
    }

    @Test
    fun testGetForeigns() {
//        val o = WithForeigns(1, PersistentClass(1), PersistentClass(1), listOf(PersistentClass(1), PersistentClass(2)))
//////        mockkStatic(Persistence::class)
//////
//////        assertEquals(0, Persistence.getForeigns(WithoutForeigns(PersistentClass(1))).size)
//////        assertEquals(3, Persistence.getForeigns(o).size)
    }


}