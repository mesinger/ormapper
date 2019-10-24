package mesi.orm.query;

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
                "id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "name TEXT NOT NULL,\n" +
                "surname TEXT,\n" +
                "fk_other INTEGER NOT NULL,\n" +
                "FOREIGN KEY (fk_other) REFERENCES foreigntablename (id)" +
                ");";

        final var query = builder.create("tablename")
                .addColumn("id", QUERYTYPE.PRIMARY, true, false, false, null, null)
                .addColumn("name", QUERYTYPE.TEXT, false, false, false, null, null)
                .addColumn("surname", QUERYTYPE.TEXT, false, true, false, null, null)
                .addColumn("other", QUERYTYPE.INTEGER, false, false, true, "foreigntablename", "id");

        assertEquals(expected, query.raw());
    }
}
