package mesi.orm.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersistenceManagerImplTest {

    private PersistenceManager pm;

    @BeforeEach
    private void setup() {
        pm = new PersistenceManagerImpl();
    }

    @Test
    public void test() throws Exception {
//        throw new RuntimeException("todo");
    }
}
