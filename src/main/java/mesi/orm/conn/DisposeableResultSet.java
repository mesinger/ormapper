package mesi.orm.conn;

import lombok.Getter;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * sql resultset wrapper
 * implements autocloseable
 * for java's try with resurce syntax
 */
public class DisposeableResultSet implements AutoCloseable {

    @Getter
    private final ResultSet resultSet;
    private final Statement statement;

    DisposeableResultSet(ResultSet resultSet, Statement statement) {
        this.resultSet = resultSet;
        this.statement = statement;
    }

    @Override
    public void close() throws Exception {
        resultSet.close();
        statement.close();
    }
}
