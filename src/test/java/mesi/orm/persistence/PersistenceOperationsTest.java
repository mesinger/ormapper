package mesi.orm.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PersistenceOperationsTest {

    private NotPersistentObject notPersistentObject;
    private PersistentObject persistentObject;
    private PersistentObjectWithId persistentObjectWithId;

    @BeforeEach
    private void setup() {
        notPersistentObject = new NotPersistentObject();
        persistentObject = new PersistentObject();
        persistentObjectWithId = new PersistentObjectWithId();
    }

    @Test
    public void testisObjectPersistent() {
        assertTrue(PersistenceOperations.isObjectPersistent(persistentObject));
        assertTrue(PersistenceOperations.isObjectPersistent(persistentObjectWithId));
        assertFalse(PersistenceOperations.isObjectPersistent(notPersistentObject));
    }

    @Test
    public void hasPersistentObjectIdentification() {
        assertFalse(PersistenceOperations.hasPersistentObjectIdentification(persistentObject));
        assertTrue(PersistenceOperations.hasPersistentObjectIdentification(persistentObjectWithId));
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
    private long id;
}
