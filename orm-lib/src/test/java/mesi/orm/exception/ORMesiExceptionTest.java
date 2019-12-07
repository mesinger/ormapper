package mesi.orm.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ORMesiExceptionTest {

    @Test
    public void testAllCustomExceptions() {
        assertThrows(ORMesiException.class, () -> {throw new ORMesiException("error");});
    }
}
