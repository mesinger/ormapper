package mesi.orm.conn

import java.sql.ResultSet
import java.sql.Statement

/**
 * disopseable resultset which should be used with 'use'
 */
internal class DisposeableResultSet(val resultSet: ResultSet, private val statement : Statement) : AutoCloseable {
    override fun close() {
        resultSet.close()
        statement.close()
    }
}