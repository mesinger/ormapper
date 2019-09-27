package mesi.orm.persistence;

import mesi.orm.conn.RDBMS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PersistenceManagerImplTest {

    private PersistenceManager pm;

    @BeforeEach
    private void setup() {
        pm = PersistenceManagerFactory.create(RDBMS.SQLITE, "testing");
    }

    @Test
    public void testGetPersistenceObjectsTableEntries() {
        throw new RuntimeException("todo");
    }

    @Test
    public void testGetPersistenceObjectsTableName() {
        throw new RuntimeException("todo");
    }
}
