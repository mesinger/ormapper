package mesi.orm.transaction

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import mesi.orm.exception.ORMesiException
import org.junit.jupiter.api.*
import kotlin.test.assertEquals

class BaseRepositoryTransactionTest {

    @Nested
    inner class HasBegun {

        private lateinit var transaction : BaseRepositoryTransaction
        private lateinit var mockedObject : Any
        private lateinit var block : () -> Unit

        @BeforeEach
        fun setup() {
            mockedObject = mockk()
            every { mockedObject.toString() } returns "any"
            block = { mockedObject.toString() }

            transaction = BaseRepositoryTransaction()
            transaction.addTask(block)
            transaction.begin()
        }

        @Test
        fun testAddTask() {
            assertEquals(1, transaction.executingTasks.size)
        }

        @Test
        fun testCommit() {
            assertDoesNotThrow { transaction.commit() }
            verify(exactly = 1) { mockedObject.toString() }
        }

        @Test
        fun testRollback() {
            assertDoesNotThrow { transaction.rollback() }
            verify(exactly = 0) { mockedObject.toString() }
        }
    }

    @Nested
    inner class HasNotBegun {

        private lateinit var transaction : BaseRepositoryTransaction

        @BeforeEach
        fun setup() {
            transaction = BaseRepositoryTransaction()
        }

        @Test
        fun testCommit() {
            assertThrows<ORMesiException> { transaction.commit() }
        }

        @Test
        fun testRollback() {
            assertThrows<ORMesiException> { transaction.rollback() }
        }
    }
}