package mesi.orm.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ORMesiExceptionsTest {

    @Test
    public void testAllCustomExceptions() {
        assertThrows(ORMesiException.class, () -> {throw new ORMesiException("error");});
        assertThrows(ORMesiConnectionException.class, () -> {throw new ORMesiConnectionException("error");});
        assertThrows(ORMesiPersistenceException.class, () -> {throw new ORMesiPersistenceException("error");});
        assertThrows(ORMesiSqlException.class, () -> {throw new ORMesiSqlException("error");});
    }
}
