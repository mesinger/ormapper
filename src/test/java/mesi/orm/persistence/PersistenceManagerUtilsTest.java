package mesi.orm.persistence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PersistenceManagerUtilsTest {

    private final Student student = new Student("mesi", 23, "if17b021");
    private final PersistentStructure persistentStructure = PersistenceManagerUtils.getPersistentStructureOf(student, student.getClass());

    public PersistenceManagerUtilsTest() throws IllegalAccessException {
    }

    @Test
    public void testGetTableEntriesOfPersistentStructure() {

        final var tableEntries = PersistenceManagerUtils.getTableEntriesOfPersistentStructure(persistentStructure);
        assertEquals(persistentStructure.getAllFields().size(), tableEntries.size());
    }

    @Test
    public void testGetPersistentStructureOf() throws Exception {

        assertEquals(PersistenceManagerUtils.getPersistenceObjectsTableName(student), persistentStructure.getTableName());
        assertEquals(2, persistentStructure.getFields().size());
        assertEquals(4, persistentStructure.getAllFields().size());
        assertNotNull(persistentStructure.getParentStructure().get());
        assertEquals(2, persistentStructure.getParentStructure().get().getAllFields().size());
        assertNull(persistentStructure.getParentStructure().get().getParentStructure().orElseGet(() -> null));
    }

    @Test
    public void testGetPersistenceObjectsTableName() {

        Object dummy = new Object();

        final var expected = dummy.getClass().getName() + "_table";

        assertEquals(expected, PersistenceManagerUtils.getPersistenceObjectsTableName(dummy));
        assertEquals(expected, PersistenceManagerUtils.getPersistenceObjectsTableName(dummy.getClass()));
    }
}

@Persistent
class Person {
    @Id
    protected Long id;
    protected String name;
    protected int age;
}

@Persistent
class Student extends Person {
    @Id
    protected Long id = 1L;
    protected String uid;

    Student(String name, int age, String uid) {
        this.name = name;
        this.age = age;
        this.uid = uid;
    }
}
