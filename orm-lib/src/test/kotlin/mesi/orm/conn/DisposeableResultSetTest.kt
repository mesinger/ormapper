package mesi.orm.conn

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.sql.ResultSet
import java.sql.Statement

class DisposeableResultSetTest {

    @Test
    fun testDisposeable() {

        val rs : ResultSet = mockk()
        val statement : Statement = mockk()

        every { rs.close() } returns Unit
        every { statement.close() } returns Unit

        val disposeableResultSet = DisposeableResultSet(rs, statement)
        disposeableResultSet.use {
            // works on the result set
        }

        verify(exactly = 1) {
            rs.close()
            statement.close()
        }
    }
}