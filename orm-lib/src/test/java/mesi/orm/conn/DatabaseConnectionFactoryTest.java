package mesi.orm.conn;

import mesi.orm.exception.ORMesiException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DatabaseConnectionFactoryTest {

    @Test
    public void testSQLiteConnection() {
        var connection = DatabaseConnectionFactory.create(DatabaseSystem.SQLITE, "a");
        assertNotNull(connection);
    }

    @Test
    public void testMSSQLConnection() {
        var connection = DatabaseConnectionFactory.create(DatabaseSystem.MSSQL, "a");
        assertNotNull(connection);
    }

    @Test
    public void testInvalidType() {
        assertThrows(
                ORMesiException.class,
                () -> DatabaseConnectionFactory.create(null, "a"),
                "DatabaseConnectionFactory should throw if RDBMS is null"
                );
    }

    @Test
    public void testInvalidConnectionString() {
        assertThrows(
                ORMesiException.class,
                () -> DatabaseConnectionFactory.create(DatabaseSystem.SQLITE, ""),
                "DatabaseConnectionFactory should throw on an empty connection string"
        );

        assertThrows(
                ORMesiException.class,
                () -> DatabaseConnectionFactory.create(DatabaseSystem.SQLITE, null),
                "DatabaseConnectionFactory should throw on a null connection string"
        );
    }
}
