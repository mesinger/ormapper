package mesi.orm.query;

import mesi.orm.persistence.transform.PersistentObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SQLiteInsertQueryTest {

    private QueryBuilder builder;

    @BeforeEach
    public void setup() {
        builder = new SQLiteQueryBuilder();
    }

    @Test
    public void testInsertStatement() {

        final var expected = "INSERT INTO Persons (id, name, surname) VALUES (1, 'mesi', 'mesinger');";

        final var person = new Person(1, "mesi", "mesinger");
        final var persistentObject = PersistentObject.Builder.from(person);

        final var query = builder.insert(persistentObject);

        assertEquals(expected, query.raw());
    }
}
