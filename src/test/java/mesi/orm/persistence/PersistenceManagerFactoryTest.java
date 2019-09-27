package mesi.orm.persistence;

import mesi.orm.exception.ORMesiException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PersistenceManagerFactoryTest {

    @Test
    public void testCreate() {

//        this throws thanks to mockito not providing static mocking capabilities
//        PersistenceManager pm = PersistenceManagerFactory.create(RDBMS.SQLITE, "a");
//        assertNotNull(pm);

        assertThrows(
                ORMesiException.class,
                () -> PersistenceManagerFactory.create(null, "a")
                );
    }
}
