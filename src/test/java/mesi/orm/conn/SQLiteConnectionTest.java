package mesi.orm.conn;

import mesi.orm.exception.ORMesiSqlException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SQLiteConnectionTest {

    @Mock
    private Connection rawConnection;

    @InjectMocks
    private SQLiteConnection connection;

    private TableDescriptor[] dummyEntries = {new TableDescriptor("id", TableEntryType.INT, false, true, false, null, null)};

    @Test
    public void testConstructor() {

        final var dummyurl = "dummy";
        var connection = new SQLiteConnection(dummyurl);

        assertEquals(JDBCDRIVERNAME.SQLITE, connection.DRIVER_CLASS_NAME);
        assertEquals(dummyurl, connection.JDBC_URL);
    }

    @Test
    public void testCreateTable() throws Exception {
        when(rawConnection.createStatement()).thenReturn(mock(Statement.class));
        //this should not throw
        connection.createTable("table", dummyEntries);
    }

    @Test
    public void testCreateTableThrows() throws Exception {
        Statement mockedStatement = mock(Statement.class);
        when(rawConnection.createStatement()).thenReturn(mockedStatement);

        Method pCreateQuery = connection.getClass().getDeclaredMethod("createTableQuery", String.class, TableDescriptor[].class);
        pCreateQuery.setAccessible(true);
        String sql = (String) pCreateQuery.invoke(connection, "table", dummyEntries);

        when(mockedStatement.execute(sql)).thenThrow(ORMesiSqlException.class);

        assertThrows(ORMesiSqlException.class, () -> connection.createTable("table", dummyEntries));
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

        Method pCreateTableQuery = connection.getClass().getDeclaredMethod("createTableQuery", String.class, TableDescriptor[].class);
        pCreateTableQuery.setAccessible(true);

        String actual = (String) pCreateTableQuery.invoke(connection,
                tableName,
                new TableDescriptor[] {
                        new TableDescriptor("id", TableEntryType.INT, false, true, false, null, null),
                        new TableDescriptor("string", TableEntryType.STRING, false, false, false, null, null),
                        new TableDescriptor("double", TableEntryType.DOUBLE, true, false, false, null, null),
                        new TableDescriptor("bool", TableEntryType.BOOL, false, false, false, null, null),
                        new TableDescriptor("foreign_id", TableEntryType.INT, false, false, true, "foreigntable", "id")
                }
                );

        assertEquals(expectation, actual);
    }

    @Test
    public void testTableExists() throws Exception {

        PreparedStatement statement = mock(PreparedStatement.class);
        when(statement.execute()).thenReturn(true);

        when(rawConnection.prepareStatement("SELECT name FROM sqlite_master WHERE type='table' AND name=?;")).thenReturn(statement);

        assertTrue(connection.tableExists("table"));
        assertTrue(connection.tableExists(null));
    }

    @Test
    public void testTableExistsFails() throws Exception {

        PreparedStatement statement = mock(PreparedStatement.class);
        when(statement.execute()).thenThrow(SQLException.class);

        when(rawConnection.prepareStatement("SELECT name FROM sqlite_master WHERE type='table' AND name=?;")).thenReturn(statement);

        assertThrows(ORMesiSqlException.class, () -> connection.tableExists(""));
    }

    @Test
    public void testMocks() {
        assertNotNull(rawConnection);
        assertNotNull(connection);
        assertEquals(rawConnection, connection.rawConnection);
    }
}
