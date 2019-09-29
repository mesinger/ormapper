package mesi.orm.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PersistenceManagerTest {

    private NotPersistentObject notPersistentObject;
    private PersistentObject persistentObject;
    private PersistentObjectWithId persistentObjectWithId;
    private PersistentObjectWithId2 persistentObjectWithId2;

    @BeforeEach
    private void setup() {
        notPersistentObject = new NotPersistentObject();
        persistentObject = new PersistentObject();
        persistentObjectWithId = new PersistentObjectWithId();
        persistentObjectWithId2 = new PersistentObjectWithId2();
    }

    @Test
    public void testPersist() {
        throw new RuntimeException("todo");
    }

    @Test
    public void testisObjectPersistent() {
        assertTrue(PersistenceManager.isObjectPersistent(persistentObject));
        assertTrue(PersistenceManager.isObjectPersistent(persistentObjectWithId));
        assertTrue(PersistenceManager.isObjectPersistent(persistentObjectWithId2));
        assertFalse(PersistenceManager.isObjectPersistent(notPersistentObject));
    }

    @Test
    public void hasPersistentObjectIdentification() {
        assertFalse(PersistenceManager.hasPersistentObjectIdentification(persistentObject));
        assertTrue(PersistenceManager.hasPersistentObjectIdentification(persistentObjectWithId));
        assertTrue(PersistenceManager.hasPersistentObjectIdentification(persistentObjectWithId2));
    }
}

class NotPersistentObject {

}

@Persistent
class PersistentObject {
    private long id;
}

@Persistent
class PersistentObjectWithId {
    @Id
    private Long id;
}

@Persistent
class PersistentObjectWithId2 {
    @Id
    private long id;
}
