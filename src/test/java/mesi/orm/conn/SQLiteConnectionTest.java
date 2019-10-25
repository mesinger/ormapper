package mesi.orm.conn;

import mesi.orm.persistence.PersistentField;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class SQLiteConnectionTest {

    @Mock
    private Connection rawConnection;

    @InjectMocks
    private SQLiteConnection connection;

    @Test
    public void testConstructor() {

        final var dummyurl = "dummy";
        var connection = new SQLiteConnection(dummyurl);

        assertEquals(JDBCDRIVERNAME.SQLITE, connection.DRIVER_CLASS_NAME);
        assertEquals(dummyurl, connection.JDBC_URL);
    }

    @Test
    public void testTableExistsQuery() {
        final String expected = "SELECT name FROM sqlite_master WHERE type='table' AND name=?;";
        final String actual = connection.tableExistsQuery();
        assertEquals(expected, actual);
    }

    @Test
    public void testMocks() {
        assertNotNull(rawConnection);
        assertNotNull(connection);
        assertEquals(rawConnection, connection.rawConnection);
    }
}
