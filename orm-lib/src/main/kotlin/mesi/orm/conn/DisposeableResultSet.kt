package mesi.orm.conn

import java.sql.ResultSet
import java.sql.Statement

internal class DisposeableResultSet(val resultSet: ResultSet, private val statement : Statement) : AutoCloseable {
    override fun close() {
        resultSet.close()
        statement.close()
    }
}