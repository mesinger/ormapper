package mesi.orm.query;

import mesi.orm.persistence.transform.PersistentPropertyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SQLiteCreateQueryTest {

    private QueryBuilder builder;

    @BeforeEach
    public void setup() {
        builder = new SQLiteQueryBuilder();
    }

    @Test
    public void testCreateQuery() {

        final var expected = "CREATE TABLE tablename(\n" +
                "id INTEGER PRIMARY KEY NOT NULL,\n" +
                "name TEXT NOT NULL,\n" +
                "surname TEXT NOT NULL,\n" +
                "other INTEGER NOT NULL,\n" +
                "FOREIGN KEY (other) REFERENCES foreigntablename (id)" +
                ");";

        final var query = builder.create("tablename")
                .addColumn("id", PersistentPropertyType.LONG, true, false, null, null)
                .addColumn("name", PersistentPropertyType.STRING, false, false, null, null)
                .addColumn("surname", PersistentPropertyType.STRING, false, false, null, null)
                .addColumn("other", PersistentPropertyType.LONG, false, true, "foreigntablename", "id");

        assertEquals(expected, query.raw());
    }
}
