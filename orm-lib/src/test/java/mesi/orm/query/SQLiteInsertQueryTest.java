package mesi.orm.query;

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

        final var expected = "INSERT INTO mesi_orm_query_Person_table (name, surname) VALUES ('mesi', 'mesinger');";

        final var person = new Person("mesi", "mesinger");

        final var query = builder.insert(Person.class, person);

        assertEquals(expected, query.raw());
    }
}
