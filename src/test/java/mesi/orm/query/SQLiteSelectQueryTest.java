package mesi.orm.query;

import mesi.orm.persistence.Id;
import mesi.orm.persistence.Persistent;
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

        final var expected = "SELECT name, surname FROM mesi_orm_query_Person_table WHERE name='mesi' AND surname='dee' ORDER BY surname;";

        final var query = builder.select("name", "surname")
                .from(Person.class)
                .where("name='mesi'")
                .andWhere("surname='dee'")
                .orderBy("surname");

        assertEquals(expected, query.raw());
    }
}
