package mesi.orm.conn;

import mesi.orm.exception.ORMesiConnectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DatabaseConnectionFactoryTest {

    @Test
    public void testSQLiteConnection() {
        var connection = DatabaseConnectionFactory.create(RDBMS.SQLITE, "a");
        assertNotNull(connection);
    }

    @Test
    public void testMSSQLConnection() {
        var connection = DatabaseConnectionFactory.create(RDBMS.MSSQL, "a");
        assertNotNull(connection);
    }

    @Test
    public void testInvalidType() {
        assertThrows(
                ORMesiConnectionException.class,
                () -> DatabaseConnectionFactory.create(null, "a"),
                "DatabaseConnectionFactory should throw if RDBMS is null"
                );
    }

    @Test
    public void testInvalidConnectionString() {
        assertThrows(
                ORMesiConnectionException.class,
                () -> DatabaseConnectionFactory.create(RDBMS.SQLITE, ""),
                "DatabaseConnectionFactory should throw on an empty connection string"
        );

        assertThrows(
                ORMesiConnectionException.class,
                () -> DatabaseConnectionFactory.create(RDBMS.SQLITE, null),
                "DatabaseConnectionFactory should throw on a null connection string"
        );
    }
}
