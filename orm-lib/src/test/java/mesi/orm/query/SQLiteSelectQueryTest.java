package mesi.orm.query;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SQLiteSelectQueryTest {

    private QueryBuilder builder;

    @BeforeEach
    public void setup() {
        builder = new SQLiteQueryBuilder();
    }

    @Test
    public void testSelectQuery() {

        final var expected = "SELECT name, surname FROM Persons WHERE name='mesi' AND surname='dee' ;";

        final var query = builder.select("name", "surname")
                .from(Person.class)
                .where("name='mesi'")
                .and()
                .where("surname='dee'");

        assertEquals(expected, query.raw());
    }
}
