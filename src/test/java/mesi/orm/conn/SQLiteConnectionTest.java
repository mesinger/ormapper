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
    public void testCreateTableQuery() throws Exception {

        final String tableName = "dummytable";
        final String expectation =
                "CREATE TABLE " + tableName + " (\n"
                        + "id INTEGER NOT NULL PRIMARY KEY, \n"
                        + "string TEXT NOT NULL, \n"
                        + "double REAL, \n"
                        + "bool INTEGER NOT NULL, \n"
                        + "foreign_id INTEGER NOT NULL, \n"
                        + "FOREIGN KEY (foreign_id) REFERENCES foreigntable (id)\n"
                        + ");";

        Method pCreateTableQuery = connection.getClass().getDeclaredMethod("createTableQuery", String.class, TableEntry[].class);
        pCreateTableQuery.setAccessible(true);

        String actual = (String) pCreateTableQuery.invoke(connection,
                tableName,
                new TableEntry[] {
                        new TableEntry("id", TableEntryType.INT, false, true, false, null, null),
                        new TableEntry("string", TableEntryType.STRING, false, false, false, null, null),
                        new TableEntry("double", TableEntryType.DOUBLE, true, false, false, null, null),
                        new TableEntry("bool", TableEntryType.BOOL, false, false, false, null, null),
                        new TableEntry("foreign_id", TableEntryType.INT, false, false, true, "foreigntable", "id")
                }
        );

        assertEquals(expectation, actual);
    }

    @Test
    public void testTableExistsQuery() {
        final String expected = "SELECT name FROM sqlite_master WHERE type='table' AND name=?;";
        final String actual = connection.tableExistsQuery();
        assertEquals(expected, actual);
    }

    @Test
    public void testInsertQuery() {

        final PersistentField[] dummyFields = {
                new PersistentField("id", Optional.of(1), false, false, false),
                new PersistentField("name", Optional.of("mesi"), false, false, false)
        };

        final String expected = "INSERT INTO tablename (id, name) VALUES (1, 'mesi');";
        final String actual = connection.insertQuery("tablename", dummyFields);

        assertEquals(expected, actual);
    }

    @Test
    public void testMocks() {
        assertNotNull(rawConnection);
        assertNotNull(connection);
        assertEquals(rawConnection, connection.rawConnection);
    }
}
