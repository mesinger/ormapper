package mesi.orm.persistence;

import mesi.orm.conn.RDBMS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersistenceManagerImplTest {

    private PersistenceManagerImpl pm;

    @BeforeEach
    private void setup() {
        pm = new PersistenceManagerImpl();
    }

    @Test
    public void testGetPersistenceObjectsTableEntries() {
        throw new RuntimeException("todo");
    }

    @Test
    public void testGetPersistenceObjectsTableName() throws Exception {

        Method pGetPersistenceObjectsTableName = pm.getClass().getDeclaredMethod("getPersistenceObjectsTableName", Object.class);
        pGetPersistenceObjectsTableName.setAccessible(true);

        final String className = Object.class.getName();
        assertEquals(className + "_table", pGetPersistenceObjectsTableName.invoke(pm, new Object()));
    }
}
